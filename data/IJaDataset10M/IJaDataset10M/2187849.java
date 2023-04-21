package org.jgetfile;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Properties;
import java.util.regex.Pattern;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.jgetfile.commandline.OptionsManager;
import org.jgetfile.crawler.ICrawler;
import org.jgetfile.crawler.ThreadPoolExecutorCrawler;
import org.jgetfile.crawler.file.FlatHostPathFileNamer;
import org.jgetfile.crawler.file.IFileNamer;
import org.jgetfile.crawler.filter.BeanShellScriptFilter;
import org.jgetfile.crawler.filter.ILinkFilter;
import org.jgetfile.crawler.filter.IncludeExcludeFilter;
import org.jgetfile.crawler.filter.RegularExpressionFilter;
import org.jgetfile.crawler.link.LinkManager;
import org.jgetfile.crawler.manager.ObjectManager;
import org.jgetfile.crawler.model.ICrawlerModel;
import org.jgetfile.crawler.model.MaxDepthModel;
import org.jgetfile.crawler.model.MaxIterationsModel;
import org.jgetfile.crawler.model.UnboundedIterationsModel;
import org.jgetfile.crawler.util.JGetFileUtils;
import org.jgetfile.events.JwgetParserEventListener;
import org.jgetfile.file.FileDownloadManager;
import bsh.Interpreter;

/**
 * This is the main class for jgetfile, it handles reading in the command line options,
 * building the appropriate objects, and kicking off the crawling.
 * 
 * @author Samuel Mendenhall
 */
public class jgetfile {

    private static final transient Logger logger = Logger.getLogger(jgetfile.class.getName());

    private static final FileDownloadManager fileDownloadManager = FileDownloadManager.getInstance();

    private static ObjectManager objectManager = ObjectManager.getInstance();

    private static LinkManager linkManager = LinkManager.getInstance();

    private static OptionsManager optionsManager = null;

    @SuppressWarnings("static-access")
    public static void main(String[] args) {
        try {
            ICrawlerModel model = null;
            String SERVER = "";
            String START = "/";
            optionsManager = new OptionsManager(args);
            File log4jProps = new File("log4j.properties");
            System.out.println("log4j.properties file path: " + log4jProps.getAbsolutePath());
            URL log4jPropsURL = log4jProps.toURL();
            if (log4jProps.exists()) {
                PropertyConfigurator.configure(log4jPropsURL);
            } else {
                System.out.println("log4j.properties does not exist, using default settings.");
                PropertyConfigurator.configure(getDefaultProperties());
            }
            if (optionsManager.hasOption("r")) {
                SERVER = optionsManager.getOptionValue("r");
            } else {
                throw new Exception("Please specify a root address to start from.");
            }
            if (optionsManager.hasOption("md")) {
                int maxDownloadsPerConnection = Integer.parseInt((optionsManager.getOptionValue("md").equals("") ? "0" : optionsManager.getOptionValue("md")));
                fileDownloadManager.setMaxDownloadsPerConnection(maxDownloadsPerConnection);
                logger.info("Max Downloads per connection set to " + maxDownloadsPerConnection);
            } else {
                fileDownloadManager.setMaxDownloadsPerConnection(2);
                logger.info("Max Downloads per connection defaulting to 2");
            }
            if (optionsManager.hasOption("dir")) {
                File localDownloadDir = new File(optionsManager.getOptionValue("dir"));
                if (!localDownloadDir.exists()) {
                    FileUtils.forceMkdir(localDownloadDir);
                }
                fileDownloadManager.setLocalDownloadDirectory(localDownloadDir);
                logger.info("Local download directory set to " + localDownloadDir.getAbsolutePath());
            } else {
                fileDownloadManager.setLocalDownloadDirectory(new File(""));
                logger.info("Local download directory set to " + fileDownloadManager.getLocalDownloadDirectory().getAbsolutePath());
            }
            if (optionsManager.hasOption("re")) {
                String regexp = optionsManager.getOptionValue("re");
                if (regexp == null || regexp.equals("")) {
                    throw new Exception("Please specify a regular expression.");
                }
                try {
                    @SuppressWarnings("unused") Pattern pattern = Pattern.compile(regexp);
                } catch (Exception e) {
                    throw new Exception(e.getMessage());
                }
                RegularExpressionFilter regexpFilter = new RegularExpressionFilter(regexp);
                objectManager.addLinkFilter(regexpFilter);
            }
            if (optionsManager.hasOption("als")) {
                String scriptName = optionsManager.getOptionValue("als");
                if (scriptName.equals("")) {
                    throw new Exception("The -als option requires a BeanShell script, ex. -als somescipt.bsh");
                }
                File script = new File(scriptName);
                if (!script.exists()) {
                    throw new Exception("The BeanShell script specified does not exist: " + script.getAbsolutePath());
                }
                Interpreter i = new Interpreter();
                try {
                    i.set("origin", "www.foo.com");
                    i.set("link", "www.foo.com");
                    i.set("linkDepth", 1);
                    i.source(script.getAbsolutePath());
                    i.get("acceptLink");
                } catch (Exception e) {
                    throw new Exception("Invalid BeanShell..." + e.getMessage());
                }
                BeanShellScriptFilter bshFilter = new BeanShellScriptFilter(script);
                objectManager.addLinkFilter(bshFilter);
            }
            Collection<String> inc = new ArrayList<String>();
            Collection<String> exc = new ArrayList<String>();
            if (optionsManager.hasOption("e")) {
                String[] excludes = optionsManager.getOptionValues("e");
                StringBuilder temp = new StringBuilder();
                if (!(excludes == null)) {
                    for (String exclude : excludes) {
                        exc.add(exclude);
                        temp.append(exclude).append(" ");
                    }
                }
            }
            if (optionsManager.hasOption("i")) {
                String[] includes = optionsManager.getOptionValues("i");
                StringBuilder temp = new StringBuilder();
                if (!(includes == null)) {
                    for (String include : includes) {
                        inc.add(include);
                        temp.append(include).append(" ");
                    }
                }
            }
            if (optionsManager.hasOption("i") || optionsManager.hasOption("e")) {
                ILinkFilter incExcFilter = new IncludeExcludeFilter(inc, exc);
                objectManager.addLinkFilter(incExcFilter);
            }
            for (ILinkFilter filter : objectManager.getLinkFilters()) {
                logger.info(filter);
            }
            if (optionsManager.hasOption("de")) {
                linkManager.setDynamicallyBuildExtensionExcludes(true);
                logger.info("Dynamically building extention excludes turned on.");
            }
            Collection<String> extensions = new ArrayList<String>();
            if (optionsManager.hasOption("ext")) {
                String[] exts = optionsManager.getOptionValues("ext");
                StringBuilder temp = new StringBuilder();
                if (!(exts == null) && !(exts.length == 0)) {
                    for (String ext : exts) {
                        extensions.add((StringUtils.contains(ext, ".") ? StringUtils.substringAfterLast(ext, ".") : ext));
                        temp.append(ext).append(" ");
                    }
                    logger.info("Extensions to search for: " + temp);
                } else {
                    throw new Exception("Please specify at least one extension to download, ex. -ext .pdf,.doc.,.txt");
                }
            } else {
                throw new Exception("Please specify the extensions to download, ex. -ext .pdf,.doc.,.txt");
            }
            linkManager.setExtensions(extensions);
            if (optionsManager.hasOption("d")) {
                int depth = Integer.valueOf(optionsManager.getOptionValue("d"));
                model = new MaxDepthModel(depth);
                logger.info("Set the model to MaxDepth with a depth of " + depth);
            } else if (optionsManager.hasOption("it")) {
                int maxIterations = Integer.valueOf(optionsManager.getOptionValue("it"));
                model = new MaxIterationsModel(maxIterations);
                logger.info("Set the model to MaxIterations with max iterations " + maxIterations);
            } else {
                model = new UnboundedIterationsModel();
                logger.info("Set the model to UnboundedIterations");
            }
            if (optionsManager.hasOption("s")) {
                START = (optionsManager.getOptionValue("s").equals("") ? "/" : optionsManager.getOptionValue("s"));
                logger.info("Starting at relative address " + START);
            }
            if (optionsManager.hasOption("mc")) {
                int maxConnectionsToHosts = Integer.parseInt((optionsManager.getOptionValue("mc").equals("") ? "1" : optionsManager.getOptionValue("mc")));
                fileDownloadManager.setMaxConnectionsToHosts(maxConnectionsToHosts);
                logger.info("Max connections to hosts set to " + maxConnectionsToHosts);
            } else {
                fileDownloadManager.setMaxConnectionsToHosts(1);
                logger.info("Max connections to hosts defaulting to " + 1);
            }
            IFileNamer fileNamer = null;
            if (optionsManager.hasOption("ffn")) {
                fileNamer = new FlatHostPathFileNamer();
                logger.info("Using the FlatHostPathFileNamer");
            } else {
                fileNamer = new FlatHostPathFileNamer();
                logger.info("Defaulting to the FlatHostPathFileNamer");
            }
            if (optionsManager.hasOption("df")) {
                long size = Long.parseLong(optionsManager.getOptionValue("df"));
                fileDownloadManager.setDeleteFilesLessThan(size);
                logger.info("Deleting files less than " + FileUtils.byteCountToDisplaySize(size));
            } else {
            }
            if (optionsManager.hasOption("iU")) {
                String regexp = optionsManager.getOptionValue("iU");
                Pattern pattern = null;
                if (regexp == null || regexp.equals("")) {
                    throw new Exception("Please specify a regular expression.");
                }
                try {
                    pattern = Pattern.compile(regexp);
                    JGetFileUtils.setPatternInnerURL(pattern);
                    logger.info("Inner URL regexp set to: " + pattern.pattern());
                } catch (Exception e) {
                    throw new Exception(e.getMessage());
                }
            }
            if (optionsManager.hasOption("ct")) {
                int numCrawlerThreads = Integer.parseInt(optionsManager.getOptionValue("ct"));
                objectManager.setMaxCrawlerThreads(numCrawlerThreads);
                logger.info("Number of crawler threads set to " + numCrawlerThreads);
            } else {
                objectManager.setMaxCrawlerThreads(5);
                logger.info("Number of crawler threads set to " + 5);
            }
            if (optionsManager.hasOption("v")) {
                objectManager.setVerbose(true);
                logger.info("Output set to verbose");
            }
            if (optionsManager.hasOption("vv")) {
                objectManager.setVerbose(true);
                objectManager.setVeryVerbose(true);
                logger.info("Output set to very verbose");
            }
            JwgetParserEventListener jwgetEvent = new JwgetParserEventListener();
            ICrawler crawler = new ThreadPoolExecutorCrawler();
            objectManager.setModel(model);
            objectManager.addParserListener(jwgetEvent);
            objectManager.setFileNamer(fileNamer);
            crawler.start(SERVER, START);
        } catch (Exception e) {
            logger.error(e.getStackTrace());
            optionsManager.printHelp();
        }
    }

    /**
	 * Returns a standard set of log4j properties
	 * 
	 * @return log4j properties
	 */
    public static Properties getDefaultProperties() {
        Properties p = new Properties();
        p.setProperty("log4j.category.org.jgetfile", "INFO, stdout");
        p.setProperty("log4j.category.org.jgetfile.crawler", "INFO, stdout");
        p.setProperty("log4j.appender.stdout", "org.apache.log4j.ConsoleAppender");
        p.setProperty("log4j.appender.stdout.layout", "org.apache.log4j.PatternLayout");
        p.setProperty("log4j.appender.stdout.layout.ConversionPattern", "%m%n");
        return p;
    }
}
