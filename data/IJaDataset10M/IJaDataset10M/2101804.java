package com.sun.tools.script.shell;

import java.io.*;
import java.net.*;
import java.text.*;
import java.util.*;
import javax.script.*;

/**
 * This is the main class for Java script shell.
 */
public class Main {

    /**
     * main entry point to the command line tool
     * @param args command line argument array
     */
    public static void main(String[] args) {
        String[] scriptArgs = processOptions(args);
        for (Command cmd : scripts) {
            cmd.run(scriptArgs);
        }
        System.exit(EXIT_SUCCESS);
    }

    private static interface Command {

        public void run(String[] arguments);
    }

    /**
     * Parses and processes command line options.
     * @param args command line argument array
     */
    private static String[] processOptions(String[] args) {
        String currentLanguage = DEFAULT_LANGUAGE;
        String currentEncoding = null;
        checkClassPath(args);
        boolean seenScript = false;
        boolean seenStdin = false;
        for (int i = 0; i < args.length; i++) {
            String arg = args[i];
            if (arg.equals("-classpath") || arg.equals("-cp")) {
                i++;
                continue;
            }
            if (!arg.startsWith("-")) {
                int numScriptArgs;
                int startScriptArg;
                if (seenScript) {
                    numScriptArgs = args.length - i;
                    startScriptArg = i;
                } else {
                    numScriptArgs = args.length - i - 1;
                    startScriptArg = i + 1;
                    ScriptEngine se = getScriptEngine(currentLanguage);
                    addFileSource(se, args[i], currentEncoding);
                }
                String[] result = new String[numScriptArgs];
                System.arraycopy(args, startScriptArg, result, 0, numScriptArgs);
                return result;
            }
            if (arg.startsWith("-D")) {
                String value = arg.substring(2);
                int eq = value.indexOf('=');
                if (eq != -1) {
                    System.setProperty(value.substring(0, eq), value.substring(eq + 1));
                } else {
                    if (!value.equals("")) {
                        System.setProperty(value, "");
                    } else {
                        usage(EXIT_CMD_NO_PROPNAME);
                    }
                }
                continue;
            } else if (arg.equals("-?") || arg.equals("-help")) {
                usage(EXIT_SUCCESS);
            } else if (arg.equals("-e")) {
                seenScript = true;
                if (++i == args.length) usage(EXIT_CMD_NO_SCRIPT);
                ScriptEngine se = getScriptEngine(currentLanguage);
                addStringSource(se, args[i]);
                continue;
            } else if (arg.equals("-encoding")) {
                if (++i == args.length) usage(EXIT_CMD_NO_ENCODING);
                currentEncoding = args[i];
                continue;
            } else if (arg.equals("-f")) {
                seenScript = true;
                if (++i == args.length) usage(EXIT_CMD_NO_FILE);
                ScriptEngine se = getScriptEngine(currentLanguage);
                if (args[i].equals("-")) {
                    if (seenStdin) {
                        usage(EXIT_MULTIPLE_STDIN);
                    } else {
                        seenStdin = true;
                    }
                    addInteractiveMode(se);
                } else {
                    addFileSource(se, args[i], currentEncoding);
                }
                continue;
            } else if (arg.equals("-l")) {
                if (++i == args.length) usage(EXIT_CMD_NO_LANG);
                currentLanguage = args[i];
                continue;
            } else if (arg.equals("-q")) {
                listScriptEngines();
            }
            usage(EXIT_UNKNOWN_OPTION);
        }
        if (!seenScript) {
            ScriptEngine se = getScriptEngine(currentLanguage);
            addInteractiveMode(se);
        }
        return new String[0];
    }

    /**
     * Adds interactive mode Command
     * @param se ScriptEngine to use in interactive mode.
     */
    private static void addInteractiveMode(final ScriptEngine se) {
        scripts.add(new Command() {

            public void run(String[] args) {
                setScriptArguments(se, args);
                processSource(se, "-", null);
            }
        });
    }

    /**
     * Adds script source file Command
     * @param se ScriptEngine used to evaluate the script file
     * @param fileName script file name
     * @param encoding script file encoding
     */
    private static void addFileSource(final ScriptEngine se, final String fileName, final String encoding) {
        scripts.add(new Command() {

            public void run(String[] args) {
                setScriptArguments(se, args);
                processSource(se, fileName, encoding);
            }
        });
    }

    /**
     * Adds script string source Command
     * @param se ScriptEngine to be used to evaluate the script string
     * @param source Script source string
     */
    private static void addStringSource(final ScriptEngine se, final String source) {
        scripts.add(new Command() {

            public void run(String[] args) {
                setScriptArguments(se, args);
                String oldFile = setScriptFilename(se, "<string>");
                try {
                    evaluateString(se, source);
                } finally {
                    setScriptFilename(se, oldFile);
                }
            }
        });
    }

    /**
     * Prints list of script engines available and exits.
     */
    private static void listScriptEngines() {
        List<ScriptEngineFactory> factories = engineManager.getEngineFactories();
        for (ScriptEngineFactory factory : factories) {
            getError().println(getMessage("engine.info", new Object[] { factory.getLanguageName(), factory.getLanguageVersion(), factory.getEngineName(), factory.getEngineVersion() }));
        }
        System.exit(EXIT_SUCCESS);
    }

    /**
     * Processes a given source file or standard input.
     * @param se ScriptEngine to be used to evaluate
     * @param filename file name, can be null
     * @param encoding script file encoding, can be null
     */
    private static void processSource(ScriptEngine se, String filename, String encoding) {
        if (filename.equals("-")) {
            BufferedReader in = new BufferedReader(new InputStreamReader(getIn()));
            boolean hitEOF = false;
            String prompt = getPrompt(se);
            se.put(ScriptEngine.FILENAME, "<STDIN>");
            while (!hitEOF) {
                getError().print(prompt);
                String source = "";
                try {
                    source = in.readLine();
                } catch (IOException ioe) {
                    getError().println(ioe.toString());
                }
                if (source == null) {
                    hitEOF = true;
                    break;
                }
                Object res = evaluateString(se, source, false);
                if (res != null) {
                    res = res.toString();
                    if (res == null) {
                        res = "null";
                    }
                    getError().println(res);
                }
            }
        } else {
            FileInputStream fis = null;
            try {
                fis = new FileInputStream(filename);
            } catch (FileNotFoundException fnfe) {
                getError().println(getMessage("file.not.found", new Object[] { filename }));
                System.exit(EXIT_FILE_NOT_FOUND);
            }
            evaluateStream(se, fis, filename, encoding);
        }
    }

    /**
     * Evaluates given script source
     * @param se ScriptEngine to evaluate the string
     * @param script Script source string
     * @param exitOnError whether to exit the process on script error
     */
    private static Object evaluateString(ScriptEngine se, String script, boolean exitOnError) {
        try {
            return se.eval(script);
        } catch (ScriptException sexp) {
            getError().println(getMessage("string.script.error", new Object[] { sexp.getMessage() }));
            if (exitOnError) System.exit(EXIT_SCRIPT_ERROR);
        } catch (Exception exp) {
            exp.printStackTrace(getError());
            if (exitOnError) System.exit(EXIT_SCRIPT_ERROR);
        }
        return null;
    }

    /**
     * Evaluate script string source and exit on script error
     * @param se ScriptEngine to evaluate the string
     * @param script Script source string
     */
    private static void evaluateString(ScriptEngine se, String script) {
        evaluateString(se, script, true);
    }

    /**
     * Evaluates script from given reader
     * @param se ScriptEngine to evaluate the string
     * @param reader Reader from which is script is read
     * @param name file name to report in error.
     */
    private static Object evaluateReader(ScriptEngine se, Reader reader, String name) {
        String oldFilename = setScriptFilename(se, name);
        try {
            return se.eval(reader);
        } catch (ScriptException sexp) {
            getError().println(getMessage("file.script.error", new Object[] { name, sexp.getMessage() }));
            System.exit(EXIT_SCRIPT_ERROR);
        } catch (Exception exp) {
            exp.printStackTrace(getError());
            System.exit(EXIT_SCRIPT_ERROR);
        } finally {
            setScriptFilename(se, oldFilename);
        }
        return null;
    }

    /**
     * Evaluates given input stream
     * @param se ScriptEngine to evaluate the string
     * @param is InputStream from which script is read
     * @param name file name to report in error
     */
    private static Object evaluateStream(ScriptEngine se, InputStream is, String name, String encoding) {
        BufferedReader reader = null;
        if (encoding != null) {
            try {
                reader = new BufferedReader(new InputStreamReader(is, encoding));
            } catch (UnsupportedEncodingException uee) {
                getError().println(getMessage("encoding.unsupported", new Object[] { encoding }));
                System.exit(EXIT_NO_ENCODING_FOUND);
            }
        } else {
            reader = new BufferedReader(new InputStreamReader(is));
        }
        return evaluateReader(se, reader, name);
    }

    /**
     * Prints usage message and exits
     * @param exitCode process exit code
     */
    private static void usage(int exitCode) {
        getError().println(getMessage("main.usage", new Object[] { PROGRAM_NAME }));
        System.exit(exitCode);
    }

    /**
     * Gets prompt for interactive mode
     * @return prompt string to use
     */
    private static String getPrompt(ScriptEngine se) {
        List<String> names = se.getFactory().getNames();
        return names.get(0) + "> ";
    }

    /**
     * Get formatted, localized error message
     */
    private static String getMessage(String key, Object[] params) {
        return MessageFormat.format(msgRes.getString(key), params);
    }

    private static InputStream getIn() {
        return System.in;
    }

    private static PrintStream getError() {
        return System.err;
    }

    private static ScriptEngine getScriptEngine(String lang) {
        ScriptEngine se = engines.get(lang);
        if (se == null) {
            se = engineManager.getEngineByName(lang);
            if (se == null) {
                getError().println(getMessage("engine.not.found", new Object[] { lang }));
                System.exit(EXIT_ENGINE_NOT_FOUND);
            }
            initScriptEngine(se);
            engines.put(lang, se);
        }
        return se;
    }

    private static void initScriptEngine(ScriptEngine se) {
        se.put("engine", se);
        List<String> exts = se.getFactory().getExtensions();
        InputStream sysIn = null;
        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        for (String ext : exts) {
            sysIn = cl.getResourceAsStream("com/sun/tools/script/shell/init." + ext);
            if (sysIn != null) break;
        }
        if (sysIn != null) {
            evaluateStream(se, sysIn, "<system-init>", null);
        }
    }

    /**
     * Checks for -classpath, -cp in command line args. Creates a ClassLoader
     * and sets it as Thread context loader for current thread.
     *
     * @param args command line argument array
     */
    private static void checkClassPath(String[] args) {
        String classPath = null;
        for (int i = 0; i < args.length; i++) {
            if (args[i].equals("-classpath") || args[i].equals("-cp")) {
                if (++i == args.length) {
                    usage(EXIT_CMD_NO_CLASSPATH);
                } else {
                    classPath = args[i];
                }
            }
        }
        if (classPath != null) {
            ClassLoader parent = Main.class.getClassLoader();
            URL[] urls = pathToURLs(classPath);
            URLClassLoader loader = new URLClassLoader(urls, parent);
            Thread.currentThread().setContextClassLoader(loader);
        }
        engineManager = new ScriptEngineManager();
    }

    /**
     * Utility method for converting a search path string to an array
     * of directory and JAR file URLs.
     *
     * @param path the search path string
     * @return the resulting array of directory and JAR file URLs
     */
    private static URL[] pathToURLs(String path) {
        String[] components = path.split(File.pathSeparator);
        URL[] urls = new URL[components.length];
        int count = 0;
        while (count < components.length) {
            URL url = fileToURL(new File(components[count]));
            if (url != null) {
                urls[count++] = url;
            }
        }
        if (urls.length != count) {
            URL[] tmp = new URL[count];
            System.arraycopy(urls, 0, tmp, 0, count);
            urls = tmp;
        }
        return urls;
    }

    /**
     * Returns the directory or JAR file URL corresponding to the specified
     * local file name.
     *
     * @param file the File object
     * @return the resulting directory or JAR file URL, or null if unknown
     */
    private static URL fileToURL(File file) {
        String name;
        try {
            name = file.getCanonicalPath();
        } catch (IOException e) {
            name = file.getAbsolutePath();
        }
        name = name.replace(File.separatorChar, '/');
        if (!name.startsWith("/")) {
            name = "/" + name;
        }
        if (!file.isFile()) {
            name = name + "/";
        }
        try {
            return new URL("file", "", name);
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException("file");
        }
    }

    private static void setScriptArguments(ScriptEngine se, String[] args) {
        se.put("arguments", args);
        se.put(ScriptEngine.ARGV, args);
    }

    private static String setScriptFilename(ScriptEngine se, String name) {
        String oldName = (String) se.get(ScriptEngine.FILENAME);
        se.put(ScriptEngine.FILENAME, name);
        return oldName;
    }

    private static final int EXIT_SUCCESS = 0;

    private static final int EXIT_CMD_NO_CLASSPATH = 1;

    private static final int EXIT_CMD_NO_FILE = 2;

    private static final int EXIT_CMD_NO_SCRIPT = 3;

    private static final int EXIT_CMD_NO_LANG = 4;

    private static final int EXIT_CMD_NO_ENCODING = 5;

    private static final int EXIT_CMD_NO_PROPNAME = 6;

    private static final int EXIT_UNKNOWN_OPTION = 7;

    private static final int EXIT_ENGINE_NOT_FOUND = 8;

    private static final int EXIT_NO_ENCODING_FOUND = 9;

    private static final int EXIT_SCRIPT_ERROR = 10;

    private static final int EXIT_FILE_NOT_FOUND = 11;

    private static final int EXIT_MULTIPLE_STDIN = 12;

    private static final String DEFAULT_LANGUAGE = "js";

    private static List<Command> scripts;

    private static ScriptEngineManager engineManager;

    private static Map<String, ScriptEngine> engines;

    private static ResourceBundle msgRes;

    private static String BUNDLE_NAME = "com.sun.tools.script.shell.messages";

    private static String PROGRAM_NAME = "jrunscript";

    static {
        scripts = new ArrayList<Command>();
        engines = new HashMap<String, ScriptEngine>();
        msgRes = ResourceBundle.getBundle(BUNDLE_NAME, Locale.getDefault());
    }
}
