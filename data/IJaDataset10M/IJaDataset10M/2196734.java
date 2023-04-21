package webservicesapi.command.impl;

import org.apache.commons.configuration.AbstractFileConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webservicesapi.command.Command;
import webservicesapi.command.CommandSet;
import webservicesapi.command.InvalidCommandException;
import webservicesapi.command.impl.java.JavaConfiguration;
import webservicesapi.command.impl.java.JavaRunner;
import webservicesapi.output.OutputQueue;
import java.io.File;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Note: Unimplemented.
 * 
 * Launches java applications.
 * 
 * <p/>
 * Specify a set of library directories, jar file, running directory
 * Java heap space
 */
public class JavaCommandSet implements CommandSet {

    public static final String JAVA_COMMAND_NAME = "java";

    private Logger logger = LoggerFactory.getLogger(SmsCommandSet.class);

    private AbstractFileConfiguration config;

    private Map<String, JavaConfiguration> apps;

    private JavaRunner runner;

    public JavaCommandSet(AbstractFileConfiguration config) {
        this.config = config;
        apps = parseApplications(config);
        runner = new JavaRunner();
    }

    @Override
    public Set<Command> getCommands() {
        HashSet<Command> commands = new HashSet<Command>();
        commands.add(new CommandBase() {

            public String[] getRequiredProperties() {
                return null;
            }

            @Override
            public String getCommandName() {
                return JAVA_COMMAND_NAME;
            }

            public void processCommand(String command, String parameter, OutputQueue queue) throws InvalidCommandException {
                JavaConfiguration conf = apps.get(parameter);
                if (conf != null) {
                } else {
                    logger.warn("Java configuration not found: " + parameter);
                }
            }

            @Override
            public String getUsage() {
                return "<application name>";
            }

            public String getHelp() {
                return "Runs predefined java applications";
            }
        });
        return commands;
    }

    public Map<String, JavaConfiguration> parseApplications(AbstractFileConfiguration config) {
        Map<String, JavaConfiguration> applications = new HashMap<String, JavaConfiguration>();
        Iterator<String> i = config.getKeys();
        while (i.hasNext()) {
            String key = i.next();
            if (key.startsWith("java.")) {
                String profile = getProfileName(key);
                if (profile != null) {
                    JavaConfiguration ja = applications.get(profile);
                    if (ja == null) {
                        ja = new JavaConfiguration(profile);
                        applications.put(profile, ja);
                    }
                    if (key.startsWith("java.lib.")) {
                        File libs = new File(config.getString(key));
                        if (libs.isDirectory()) {
                            List<File> jars = getJars(libs);
                            ja.setLibraries(jars);
                        }
                    } else if (key.startsWith("java.jar.")) {
                        File f = new File(config.getString(key));
                        ja.setJar(f);
                    } else if (key.startsWith("java.main.")) {
                        ja.setMainClass(config.getString(key));
                    } else if (key.startsWith("java.source.")) {
                        File f = new File(config.getString(key));
                        ja.setSource(f);
                    } else if (key.startsWith("java.options.")) {
                        List<String> f = config.getList(key);
                        ja.setOptions(f);
                    } else {
                        logger.warn("Unknown java application key specified: " + key);
                    }
                } else {
                    logger.warn("Invalid java application key specified: " + key);
                }
            }
        }
        return applications;
    }

    /**
     * Recursively get all jar files contained within the specified directory.
     * Note that this method just looks for files that end in .jar
     * and doesn't do any other validation.
     *
     * @param libs
     * @return
     */
    private List<File> getJars(File libs) {
        List<File> files = new ArrayList<File>();
        for (File curr : libs.listFiles()) {
            if (curr.isDirectory()) {
                files.addAll(getJars(curr));
            } else {
                if (curr.getName().endsWith(".jar")) {
                    files.add(curr);
                }
            }
        }
        return files;
    }

    public String getProfileName(String key) {
        Pattern pattern = Pattern.compile("[\\.]([a-zA-Z]*)");
        Matcher matcher = pattern.matcher(key);
        if (matcher.find() && matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }
}
