package org.cyberaide.core;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Scanner;
import java.util.Set;
import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;
import org.cyberaide.calendar.Less;
import org.cyberaide.info.ManCLI;
import org.cyberaide.io.StandardIO;
import org.cyberaide.util.Path;

public abstract class CommandPlugin {

    /**
	 * Initialize logger
	 */
    private Options internalOpt = new Options();

    private boolean stopAtNonOption = false;

    private Queue<String> fileNames = new LinkedList<String>();

    protected Logger log = Logger.getLogger(this.getClass());

    private Map<String, Set<String>> alias = new HashMap<String, Set<String>>();

    private CommandLine cmdLine;

    private static boolean interactive = false;

    private String command;

    private static ShellContext context;

    public static ShellContext getContext() {
        return context;
    }

    public static void setContext(ShellContext context) {
        CommandPlugin.context = context;
    }

    static {
        DOMConfigurator.configure(Path.getConfDirBase("log4jconfig.xml") + "log4jconfig.xml");
    }

    protected void setCommand(String line) {
        this.command = line;
    }

    protected String getCommand() {
        return this.command;
    }

    private static void loadExceptions() {
    }

    protected void setQueue(Queue<String> que) {
        this.fileNames = que;
    }

    protected static void setInteractive(boolean val) {
        interactive = val;
    }

    protected static boolean getinteractive() {
        return interactive;
    }

    protected void setStopAtNonOption(boolean val) {
        this.stopAtNonOption = val;
    }

    protected CommandLine getLine() {
        return cmdLine;
    }

    protected void setLine(CommandLine cmd) {
        this.cmdLine = cmd;
    }

    public void setLine(String line) {
        try {
            setLine(getParser().parse(getOptions(), line.split(" ")));
        } catch (ParseException e) {
            log.error(e.getMessage());
        }
    }

    protected void addOption(String name, boolean hasArgs, boolean hasOptArgs) {
        addOption(name, null, hasArgs, hasOptArgs);
    }

    protected void addOption(String name) {
        addOption(name, null, false, false);
    }

    protected void addOption(String name, boolean hasArgs) {
        addOption(name, null, hasArgs, false);
    }

    protected void addOption(String shortName, String longName) {
        addOption(shortName, longName, false, false);
    }

    protected void addOption(String shortName, String longName, boolean hasArgs) {
        addOption(shortName, longName, hasArgs, false);
    }

    protected void addOption(String shortName, String longName, boolean hasArgs, boolean hasOptArgs) {
        if (!(getOptions().hasOption(shortName) || getOptions().hasOption(longName))) {
            options.addOption(getOption(shortName, longName, hasArgs, hasOptArgs));
        } else {
            log.error("Option \"" + shortName + "\" already exists");
        }
    }

    public static Option getOption(String shortName, String longName, boolean hasArgs, boolean hasOptArgs) {
        Option opt = new Option(shortName, "Refer man page");
        if (hasArgs) {
            opt.setArgs(Option.UNLIMITED_VALUES);
        }
        opt.setDescription("Refer man page");
        if (longName != null) opt.setLongOpt(longName);
        opt.setOptionalArg(hasOptArgs);
        return opt;
    }

    private CommandLineParser parser;

    private Options options;

    private String prompt;

    private Scanner reader;

    protected CommandLineParser getParser() {
        return parser;
    }

    protected void setParser(CommandLineParser parser) {
        this.parser = parser;
    }

    public CommandPlugin() {
        parser = new BasicParser();
        options = new Options();
        initParentOptions();
        initOptions();
        setReader(new Scanner(System.in));
    }

    public String getPrompt() {
        return prompt;
    }

    public void setPrompt(String prompt) {
        this.prompt = prompt;
    }

    protected void printPrompt() {
        StandardIO.__out.print(prompt + "> ");
    }

    public void setReader(Scanner stream) {
        reader = stream;
    }

    protected Scanner getReader() {
        return this.reader;
    }

    protected String getPath() {
        return Thread.currentThread().getContextClassLoader().getResource("").getFile().replaceAll("%20", " ");
    }

    protected String normalizeCommand(String command) {
        command = command.trim();
        if (command.charAt(0) != '-') command = "-" + command.trim();
        return command;
    }

    public void read() {
        while (true) {
            try {
                if (!context.isScript()) printPrompt();
                String nxtLine = context.getIo().in.readLine();
                if (nxtLine == null) {
                    break;
                } else if (nxtLine.equals("") || nxtLine.startsWith("#")) {
                    continue;
                } else if (nxtLine.startsWith("!")) {
                    launchApp(nxtLine.replace("!", "").trim());
                } else {
                    nxtLine = normalizeCommand(nxtLine);
                    nxtLine = preParse(nxtLine);
                    setCommand(nxtLine);
                    cmdLine = getParser().parse(options, nxtLine.split(" "), stopAtNonOption);
                    if (!isExit(cmdLine)) {
                        if (!executeParent()) execute();
                    } else break;
                }
            } catch (IOException e) {
                log.error(e);
            } catch (Exception e) {
                log.error(e);
            }
            postParse();
        }
    }

    public void runCommand(String cmd) {
        if (cmd != null) {
            try {
                if (cmd.equals("") || cmd.startsWith("#")) {
                } else if (cmd.startsWith("!")) {
                } else {
                    cmd = normalizeCommand(cmd);
                    setCommand(cmd);
                    cmdLine = getParser().parse(options, cmd.split(" "), stopAtNonOption);
                    if (!isExit(cmdLine)) {
                        if (!executeParent()) execute();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void launchApp(String trim) {
        String os = System.getProperty("os.name");
        String p = "";
        if (os.toLowerCase().startsWith("windows")) {
            System.out.println("Not implemented for Windows xp/vista");
        } else if (os.toLowerCase().contains("linux")) {
            exec(trim);
        } else {
            System.out.println("OS not known");
        }
    }

    private void exec(String string) {
        try {
            Process p = new ProcessBuilder(string.split(" ")).start();
        } catch (IOException e) {
            log.error("Cannot launch process " + string);
        }
    }

    private void postParse() {
        context.getIo().out.setSystemOut();
    }

    private String preParse(String nxtLine) {
        String cmd;
        if (nxtLine.contains(">>")) {
            String fName[] = nxtLine.split(">>");
            cmd = fName[0];
            try {
                context.getIo().out.redirect(new FileOutputStream(new File(fName[1].trim()), true));
            } catch (FileNotFoundException e) {
                log.error("error in afterParse");
            }
            return cmd;
        } else if (nxtLine.contains(">")) {
            String fName[] = nxtLine.split(">");
            cmd = fName[0];
            try {
                context.getIo().out.redirect(new FileOutputStream(new File(fName[1].trim())));
            } catch (FileNotFoundException e) {
                log.error("error in preParse");
            }
            return cmd;
        } else {
            return nxtLine;
        }
    }

    public void read(String[] command) {
        try {
            command[1] = normalizeCommand(command[1]);
            cmdLine = getParser().parse(options, command, stopAtNonOption);
            if (!isExit(cmdLine)) {
                executeParent();
                execute();
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    public void read(String nxtLine) {
        try {
            nxtLine = normalizeCommand(nxtLine);
            nxtLine = preParse(nxtLine);
            setCommand(nxtLine);
            cmdLine = getParser().parse(options, nxtLine.split(" "), stopAtNonOption);
            if (!isExit(cmdLine)) {
                executeParent();
                execute();
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        postParse();
    }

    protected Options getOptions() {
        return options;
    }

    /**
	 * Initialize all the options that shall remain common among all objects Eg.
	 * Read from file, Verbose mode, etc.
	 */
    private void initParentOptions() {
        setPrompt(this.getClass().getSimpleName().replaceAll("CLI", "").toLowerCase());
        addOption("e", "exit");
        internalOpt.addOption(new Option("exit", "exit"));
        addOption("return");
        internalOpt.addOption(new Option("exit", ""));
        addOption("i", "interact");
        addOption("v", "verbose");
        addOption("d", "setloglevel", true);
        addOption("load", false);
        internalOpt.addOption(new Option("load", " [-f] scriptName"));
        addOption("save");
        addOption("alias", true);
        internalOpt.addOption(new Option("alias", " [cmd[=newName]]"));
        addOption("f", "file", true);
        addOption("help", true, true);
        internalOpt.addOption(new Option("help", ""));
        addOption("man", "man", true, true);
        internalOpt.addOption(new Option("man", " cmdName"));
        addOption("more", "more", true);
        internalOpt.addOption(new Option("more", " fileName"));
        addOption("history", "history", true);
        internalOpt.addOption(new Option("history", ""));
        addOption("less", "less", true, true);
        addOption("copy", "cp", true);
    }

    public abstract void initOptions();

    private boolean executeParent() {
        if (hasOption("load")) {
            if (hasOption("verbose")) {
                Logger.getRootLogger().setLevel(Level.ALL);
            } else Logger.getRootLogger().setLevel(Level.ERROR);
            if (hasOption("f")) {
                String fName = getOption("file").getValue().trim();
                try {
                    context.getIo().in.setInputStream(new FileInputStream(Path.CURDIR + "etc" + Path.fs + new File(fName)));
                    context.setScript(true);
                    read();
                    context.setScript(false);
                    context.getIo().in.restore();
                } catch (FileNotFoundException e) {
                    log.error(fName + ": No such file or directory");
                }
                return true;
            } else {
                log.error("Missing '-f' file option");
                return true;
            }
        }
        if (hasOption("exit")) {
            System.exit(0);
        }
        if (hasOption("alias")) {
            Map<String, String> optAlias;
            Option opt;
            String str[] = getOption("alias").getValues();
            optAlias = createMappings(str);
            for (String s : optAlias.keySet()) {
                if (options.hasOption(s)) {
                    if (!this.alias.containsKey(s)) {
                        alias.put(s, new HashSet<String>());
                    }
                    opt = (Option) options.getOption(s).clone();
                    opt.setLongOpt(optAlias.get(s));
                    if (!hasAlias(opt)) {
                        options.addOption(opt);
                        alias.get(s).add(optAlias.get(s));
                    } else {
                        log.error("Alias: " + opt.getLongOpt() + " already exists");
                    }
                }
            }
            return true;
        }
        if (hasOption("setloglevel")) {
            Logger.getRootLogger().setLevel(Level.toLevel(getOptionValue("setloglevel")));
            log.error("Level changed to : " + Logger.getRootLogger().getLevel());
            return true;
        }
        if (hasOption("save")) {
            if (hasOption("f")) {
                String fileName = getPath() + "../../" + getOption("file").getValue();
                try {
                    BufferedWriter bwr = new BufferedWriter(new FileWriter(new File(fileName)));
                    for (String str : alias.keySet()) {
                        StringBuffer sbuf = new StringBuffer();
                        sbuf.append(this.getClass().getName() + ".alias." + str + "=");
                        for (String optAlias : alias.get(str)) {
                            sbuf.append(optAlias + ",");
                        }
                        sbuf.append("\n");
                        bwr.write(sbuf.toString());
                    }
                    bwr.close();
                } catch (Exception e) {
                    log.error(e.getMessage());
                }
            }
            return true;
        } else if (hasOption("help")) {
            Map<String, CommandPlugin> cmds = ShellManager.getPlugins();
            System.out.println("These shell commands are defined internally.Type `help' to see this list." + "Type `man name' to find out more about the function `name'.\n");
            for (String s : cmds.keySet()) {
                System.out.println(s.toLowerCase() + " [arguments....]");
            }
            Collection<Option> opts = internalOpt.getOptions();
            for (Option opt : opts) {
                System.out.println(opt.getOpt() + opt.getDescription());
            }
            return true;
        } else if (hasOption("man")) {
            ManCLI man = new ManCLI();
            String[] cmdArr = this.getOption("man").getValues();
            int section = 0;
            int num = 20;
            String cmd = null;
            try {
                if (cmdArr == null) {
                    StandardIO.__out.println("Usage: man < command >");
                } else {
                    cmd = cmdArr[0];
                    if (hasOption("less")) {
                        man.setIfPages(true);
                        if (this.getOption("less").getValue() != null) num = Integer.parseInt(getOption("less").getValue());
                    }
                    if (cmd.equals("all")) {
                        man.listAll();
                    } else {
                        try {
                            if (cmd != null) section = Integer.parseInt(cmd);
                        } catch (Exception e) {
                        }
                        try {
                            man.listMan(cmd, section);
                        } catch (FileNotFoundException e) {
                            try {
                                man.listType(cmd, section);
                            } catch (FileNotFoundException ee) {
                                log.error("Command doest not exist");
                            }
                        }
                    }
                    if (man.getIfPages()) {
                        Less.output(man.getBuffers(), num);
                        man.setBuffers("");
                        man.setIfPages(false);
                    }
                }
            } catch (Exception e) {
                log.error("Command doest not exist");
            }
            return true;
        } else if (hasOption("more")) {
            String fName = getOptionValue("more");
            try {
                Scanner sc = new Scanner(new File(fName));
                while (sc.hasNextLine()) {
                    System.out.println(sc.nextLine());
                }
            } catch (FileNotFoundException e) {
                log.error(fName + ": No such file or directory");
            }
            return true;
        } else if (hasOption("history")) {
            return true;
        } else if (hasOption("copy")) {
            String[] urls = getOptionValues("copy");
            if (urls.length != 2) {
                log.error("Usage: copy <source> <destination>");
            } else {
                new org.cyberaide.gridftp.GridFTP().copy(urls[0], urls[1]);
            }
            return true;
        }
        return false;
    }

    private boolean hasAlias(Option opt) {
        return getOptions().hasOption(opt.getLongOpt());
    }

    private Map<String, String> createMappings(String[] str) {
        Scanner sc;
        Map<String, String> alias = new HashMap<String, String>();
        for (String s : str) {
            sc = new Scanner(s).useDelimiter("=");
            String opt = sc.next();
            String longOpt = getOptions().getOption(opt).getOpt();
            alias.put(longOpt, sc.next());
        }
        return alias;
    }

    public abstract void execute() throws Exception;

    private boolean isExit(CommandLine line) {
        return line.hasOption("return");
    }

    protected boolean hasOption(String cmd) {
        String opt = getOptions().getOption(cmd).getOpt();
        if (opt != null) {
            if (cmdLine.hasOption(opt)) return true; else {
                if (alias.get(opt) != null) {
                    for (String str : alias.get(opt)) {
                        if (cmdLine.hasOption(str)) return true;
                    }
                }
            }
        }
        return false;
    }

    protected Option getOption(String cmd) {
        if (getOptions() != null && cmdLine != null) {
            String opt = getOptions().getOption(cmd).getOpt();
            if (opt != null) {
                if (cmdLine.hasOption(opt)) {
                    return getOptions().getOption(opt);
                } else {
                    if (alias.get(opt) != null) {
                        for (String str : alias.get(opt)) {
                            if (cmdLine.hasOption(str)) {
                                return getOptions().getOption(str);
                            }
                        }
                    }
                }
            }
        }
        return null;
    }

    protected String getOptionValue(String cmd) {
        if (getOption(cmd) == null) return null; else return getOption(cmd).getValue();
    }

    protected String[] getOptionValues(String cmd) {
        if (getOption(cmd) == null) return null; else return getOption(cmd).getValues();
    }

    protected String arrayToString(String[] data) {
        return arrayToString(data, " ");
    }

    protected String arrayToString(String[] data, String seperator) {
        StringBuilder dataString = new StringBuilder();
        for (String d : data) {
            dataString.append(d + " ");
        }
        return dataString.toString();
    }
}
