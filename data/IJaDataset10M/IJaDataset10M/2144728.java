package org.dllearner.cli;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.xmlbeans.XmlObject;
import org.dllearner.configuration.IConfiguration;
import org.dllearner.configuration.spring.ApplicationContextBuilder;
import org.dllearner.configuration.spring.DefaultApplicationContextBuilder;
import org.dllearner.configuration.util.SpringConfigurationXMLBeanConverter;
import org.dllearner.confparser3.ConfParserConfiguration;
import org.dllearner.confparser3.ParseException;
import org.dllearner.core.*;
import org.dllearner.learningproblems.PosNegLP;
import org.dllearner.utilities.Files;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

/**
 * 
 * New commandline interface.
 * 
 * @author Jens Lehmann
 *
 */
public class CLI {

    private static Logger logger = LoggerFactory.getLogger(CLI.class);

    private ApplicationContext context;

    private IConfiguration configuration;

    private File confFile;

    private LearningAlgorithm algorithm;

    private KnowledgeSource knowledgeSource;

    private boolean writeSpringConfiguration = false;

    private boolean performCrossValidation = false;

    private int nrOfFolds = 10;

    public CLI() {
    }

    public CLI(File confFile) {
        this();
        this.confFile = confFile;
    }

    public void init() throws IOException {
        if (context == null) {
            Resource confFileR = new FileSystemResource(confFile);
            List<Resource> springConfigResources = new ArrayList<Resource>();
            configuration = new ConfParserConfiguration(confFileR);
            ApplicationContextBuilder builder = new DefaultApplicationContextBuilder();
            context = builder.buildApplicationContext(configuration, springConfigResources);
        }
    }

    public void run() throws IOException {
        if (writeSpringConfiguration) {
            SpringConfigurationXMLBeanConverter converter = new SpringConfigurationXMLBeanConverter();
            XmlObject xml;
            if (configuration == null) {
                Resource confFileR = new FileSystemResource(confFile);
                configuration = new ConfParserConfiguration(confFileR);
                xml = converter.convert(configuration);
            } else {
                xml = converter.convert(configuration);
            }
            String springFilename = confFile.getCanonicalPath().replace(".conf", ".xml");
            File springFile = new File(springFilename);
            if (springFile.exists()) {
                logger.warn("Cannot write Spring configuration, because " + springFilename + " already exists.");
            } else {
                Files.createFile(springFile, xml.toString());
            }
        }
        if (performCrossValidation) {
            AbstractReasonerComponent rs = context.getBean(AbstractReasonerComponent.class);
            PosNegLP lp = context.getBean(PosNegLP.class);
            AbstractCELA la = context.getBean(AbstractCELA.class);
            new CrossValidation(la, lp, rs, nrOfFolds, false);
        } else {
            for (Entry<String, LearningAlgorithm> entry : context.getBeansOfType(LearningAlgorithm.class).entrySet()) {
                algorithm = entry.getValue();
                logger.info("Running algorithm instance \"" + entry.getKey() + "\"(" + algorithm.getClass().getSimpleName() + ")");
                algorithm.start();
            }
        }
    }

    public boolean isWriteSpringConfiguration() {
        return writeSpringConfiguration;
    }

    public void setWriteSpringConfiguration(boolean writeSpringConfiguration) {
        this.writeSpringConfiguration = writeSpringConfiguration;
    }

    /**
	 * @param args
	 * @throws ParseException 
	 * @throws IOException 
	 * @throws ReasoningMethodUnsupportedException 
	 */
    public static void main(String[] args) throws ParseException, IOException, ReasoningMethodUnsupportedException {
        System.out.println("DL-Learner command line interface");
        if (args.length == 0) {
            System.out.println("You need to give a conf file as argument.");
            System.exit(0);
        }
        File file = new File(args[args.length - 1]);
        if (!file.exists()) {
            System.out.println("File \"" + file + "\" does not exist.");
            System.exit(0);
        }
        Resource confFile = new FileSystemResource(file);
        List<Resource> springConfigResources = new ArrayList<Resource>();
        try {
            IConfiguration configuration = new ConfParserConfiguration(confFile);
            ApplicationContextBuilder builder = new DefaultApplicationContextBuilder();
            ApplicationContext context = builder.buildApplicationContext(configuration, springConfigResources);
            CLI cli;
            if (context.containsBean("cli")) {
                cli = (CLI) context.getBean("cli");
            } else {
                cli = new CLI();
            }
            cli.setContext(context);
            cli.setConfFile(file);
            cli.run();
        } catch (Exception e) {
            String stacktraceFileName = "log/error.log";
            Throwable primaryCause = findPrimaryCause(e);
            logger.error("An Error Has Occurred During Processing.");
            logger.error(primaryCause.getMessage());
            logger.debug("Stack Trace: ", e);
            logger.error("Terminating DL-Learner...and writing stacktrace to: " + stacktraceFileName);
            FileOutputStream fos = new FileOutputStream(stacktraceFileName);
            PrintStream ps = new PrintStream(fos);
            e.printStackTrace(ps);
        }
    }

    /**
     * Find the primary cause of the specified exception.
     *
     * @param e The exception to analyze
     * @return The primary cause of the exception.
     */
    private static Throwable findPrimaryCause(Exception e) {
        Throwable[] throwables = ExceptionUtils.getThrowables(e);
        int componentInitExceptionIndex = ExceptionUtils.indexOfThrowable(e, ComponentInitException.class);
        Throwable primaryCause;
        if (componentInitExceptionIndex > -1) {
            primaryCause = throwables[componentInitExceptionIndex];
        } else {
            primaryCause = ExceptionUtils.getRootCause(e);
        }
        return primaryCause;
    }

    public void setContext(ApplicationContext context) {
        this.context = context;
    }

    public ApplicationContext getContext() {
        return context;
    }

    public File getConfFile() {
        return confFile;
    }

    public void setConfFile(File confFile) {
        this.confFile = confFile;
    }

    public boolean isPerformCrossValidation() {
        return performCrossValidation;
    }

    public void setPerformCrossValidation(boolean performCrossValiation) {
        this.performCrossValidation = performCrossValiation;
    }

    public int getNrOfFolds() {
        return nrOfFolds;
    }

    public void setNrOfFolds(int nrOfFolds) {
        this.nrOfFolds = nrOfFolds;
    }

    public LearningAlgorithm getLearningAlgorithm() {
        return algorithm;
    }

    public KnowledgeSource getKnowledgeSource() {
        return knowledgeSource;
    }
}
