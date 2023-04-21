package tigerunit.runner;

import tigerunit.util.cli.Options;
import tigerunit.util.cli.HelpFormatter;
import tigerunit.util.cli.OptionBuilder;
import tigerunit.util.cli.Option;
import tigerunit.util.cli.CommandLine;
import tigerunit.util.cli.PosixParser;
import tigerunit.util.Log;
import tigerunit.util.Config;
import java.io.PrintWriter;
import java.io.PrintStream;
import java.io.File;
import java.util.jar.JarFile;

public class BaseCommandLineTestRunner extends BaseTestRunner {

    private static final Options options = new Options();

    private static final HelpFormatter helpFormatter = new HelpFormatter();

    static {
        addOption(OptionBuilder.withLongOpt("help").withDescription("print this message").create("h"));
        addOption(OptionBuilder.withLongOpt("version").withDescription("print version information").create("v"));
        addOption(OptionBuilder.withLongOpt("debug").withDescription("print debugging information").create("g"));
        addOption(OptionBuilder.withLongOpt("include").hasArgs().withArgName("categories").withDescription("comma-delimited list of categories to include").create("i"));
        addOption(OptionBuilder.withLongOpt("exclude").hasArgs().withArgName("categories").withDescription("comma-delimited list of categories to exclude").create("x"));
        addOption(OptionBuilder.withLongOpt("out").hasArg().withArgName("file").withDescription("file to which standard output will be directed").create("o"));
        addOption(OptionBuilder.withLongOpt("err").hasArg().withArgName("file").withDescription("file to which error output will be directed").create("e"));
        addOption(OptionBuilder.withLongOpt("loader").hasArg().withArgName("loader").withDescription("TestLoader with which to load test cases").create("l"));
        addOption(OptionBuilder.withLongOpt("config").hasArg().withArgName("file").withDescription("configuration file").create("f"));
        addOption(OptionBuilder.withLongOpt("directories").hasArgs().withArgName("directories").withValueSeparator(',').withDescription("comma-delimited list of directories from which to load tests").create("d"));
        addOption(OptionBuilder.withLongOpt("jars").hasArgs().withArgName("jars").withValueSeparator(',').withDescription("comma-delimited list of jar files from which to load tests").create("j"));
        addOption(OptionBuilder.withLongOpt("classes").hasArgs().withArgName("classes").withValueSeparator(',').withDescription("comma-delimited list of classes from which to load tests").create("c"));
        addOption(OptionBuilder.withLongOpt("formatter").hasArg().withArgName("formatter[,output file/dir[,prefix[,extension]]]").withDescription("formatter class name or alias, and optionally " + "an output file or directory; if specifying a " + "directory, one output file will be written for " + "each test, and prefix and extension specify the " + "prefix and extension of the output files").create("r"));
        addOption(OptionBuilder.withLongOpt("fork").hasArg().withArgName("value(off,on,reloading)[,timeout]").withDescription("run each test in a seperate process; if value " + "is 'on' or 'reloading' the optional timout " + "specifies the maximum run time (in milliseconds) " + "for each test.").create("k"));
        addOption(OptionBuilder.withDescription("don't filter standard exclusions for stack traces").create("nofilterstack"));
    }

    protected static void addOption(Option option) {
        options.addOption(option);
    }

    protected static CommandLine parse(String[] args) throws Exception {
        return new PosixParser().parse(options, args, false);
    }

    protected static void printUsage(Class mainClass) {
        PrintWriter pw = new PrintWriter(Log.getOutputStream());
        helpFormatter.printUsage(pw, mainClass, "-h");
        pw.flush();
    }

    protected static void printHelp(Class mainClass) {
        PrintWriter pw = new PrintWriter(Log.getOutputStream());
        helpFormatter.printHelp(pw, mainClass, options);
        pw.flush();
    }

    private boolean filterStack = false;

    public void setFilterStack(boolean filterStack) {
        this.filterStack = filterStack;
    }

    public void addFormatter(ResultFormatter formatter) {
        formatter.setFilterStackTraces(this.filterStack);
        super.addFormatter(formatter);
    }

    /**
     * Initialize the test runner from command line arguments.
     * @param args command line arguments
     * @return true if init was successful ant test execution can proceed,
     * otherwise false.
     * @throws Exception
     */
    protected boolean init(String[] args) {
        try {
            CommandLine cl = parse(args);
            return init(cl);
        } catch (Exception ex) {
            printUsage(getClass());
            runFailed(ex.getMessage());
        }
        return false;
    }

    protected boolean init(CommandLine cl) throws Exception {
        if (cl.hasOption("o")) {
            String file = cl.getOptionValue("o");
            Log.setOutputStream(new PrintStream(file));
        }
        if (cl.hasOption("e")) {
            String file = cl.getOptionValue("e");
            Log.setErrorStream(new PrintStream(file));
        }
        if (cl.hasOption('h')) {
            printHelp(getClass());
            return false;
        }
        if (cl.hasOption('v')) {
            Log.printVersion();
            return false;
        }
        String forkType = null;
        if (cl.hasOption("k")) {
            forkType = cl.getOptionValue("k");
        }
        if (forkType == null) {
            forkType = Config.get(PROP_FORK, "off");
        }
        boolean fork = !"off".equals(forkType);
        boolean reload = false;
        int timeout = 0;
        if (fork) {
            String[] parts = forkType.split(",");
            reload = ("reloading".equals(parts[0]));
            if (parts.length > 1) {
                timeout = Integer.parseInt(parts[1]);
            }
        }
        initSession(fork, reload, timeout);
        String testLoaderClass = null;
        if (cl.hasOption('l')) {
            testLoaderClass = cl.getOptionValue('l');
        }
        setTestLoader(testLoaderClass);
        setFilterStack(cl.hasOption("nofilterstack"));
        if (cl.hasOption('r')) {
            for (String formatter : cl.getOptionValues('r')) {
                addFormatterFromString(formatter);
            }
        }
        if (cl.hasOption('i')) {
            for (String category : cl.getOptionValues('i')) {
                getTestLoader().addIncludedCategory(category);
            }
        }
        if (cl.hasOption('x')) {
            for (String category : cl.getOptionValues('x')) {
                getTestLoader().addExcludedCategory(category);
            }
        }
        if (cl.hasOption('f')) {
            String[] configFiles = cl.getOptionValues('f');
            for (String configFile : configFiles) {
                getTestLoader().addConfigFile(configFile);
            }
        }
        if (cl.hasOption('d')) {
            String[] dirs = cl.getOptionValues('d');
            for (String dir : dirs) {
                File d = new File(dir);
                if (!d.exists() || !d.isDirectory()) {
                } else {
                    getTestLoader().addDirectory(d);
                }
            }
        }
        if (cl.hasOption('j')) {
            String[] jars = cl.getOptionValues('j');
            for (String jar : jars) {
                try {
                    getTestLoader().addJar(new JarFile(jar));
                } catch (Exception ex) {
                }
            }
        }
        if (cl.hasOption('c')) {
            String[] classes = cl.getOptionValues('c');
            for (String className : classes) {
                getTestLoader().addClassName(className);
            }
        }
        String[] remainingArgs = cl.getArgs();
        if (remainingArgs != null) {
            for (String className : remainingArgs) {
                getTestLoader().addClassName(className);
            }
        }
        return true;
    }

    private void addFormatterFromString(String formatter) throws Exception {
        String[] parts = formatter.split(",");
        if (parts.length == 1) {
            addFormatter(parts[0]);
        } else {
            String name = parts[0];
            File f = new File(parts[1]);
            if ((parts.length == 2 && !f.exists()) || (f.exists() && !f.isDirectory())) {
                addFormatter(name, f);
            } else {
                String prefix = null;
                String extension = null;
                if (parts.length > 2) {
                    prefix = parts[2];
                }
                if (parts.length > 3) {
                    extension = parts[3];
                }
                addFormatter(name, f, prefix, extension);
            }
        }
    }
}
