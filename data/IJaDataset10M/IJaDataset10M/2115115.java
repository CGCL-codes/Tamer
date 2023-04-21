package net.sourceforge.jdefprog.annoproc;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Set;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import java.lang.annotation.Annotation;
import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import net.sourceforge.jdefprog.annocheck.ApInfoProvider;
import net.sourceforge.jdefprog.annocheck.RulesChecker;
import net.sourceforge.jdefprog.annocheck.elements.ApBehavior;
import net.sourceforge.jdefprog.annocheck.elements.ApField;
import net.sourceforge.jdefprog.annocheck.elements.ApParameter;
import net.sourceforge.jdefprog.annocheck.elements.ApType;
import net.sourceforge.jdefprog.logging.LoggingUtils;
import net.sourceforge.jdefprog.msg.LocatedMessageEmitter;
import net.sourceforge.jdefprog.reflection.*;

/**
 * The AnnotationsProcessor can be initialized using Annotations processing options (take a look at the
 * documentation of your IDE or of your compiler to know how to specify these options).
 * 
 * Option "log_path" specifiy the name of the file where to locate the log file, if not specified the default
 * location is the file named jdefprog_annoproc_log.txt under the home directory of the user.
 * 
 * Option "log_level" can be used to specify how much the logger should be verbose. Accepted values are
 * FINEST,FINER,FINE,INFO,WARNING,SEVERE.
 * 
 * @author Federico Tomassetti (f.tomassetti@gmail.com)
 */
@SupportedAnnotationTypes({ "net.sourceforge.jdefprog.cc.*", "net.sourceforge.jdefprog.dbc.*" })
@SupportedSourceVersion(SourceVersion.RELEASE_6)
public class AnnotationsProcessor extends AbstractProcessor {

    private static Logger logger = Logger.getLogger(AnnotationsProcessor.class.getCanonicalName());

    private Logger rootLogger;

    protected String getLogRootPackage() {
        return "net.sourceforge.jdefprog";
    }

    private static final String LOG_PATH_OPT = "log_path";

    private static final String LOG_LEVEL_OPT = "log_level";

    private static final String MCL_DUMP_PATH = "mcl_dump_path";

    private static final String[] OPTIONS = new String[] { LOG_LEVEL_OPT, LOG_PATH_OPT, MCL_DUMP_PATH };

    private static final String DEFAULT_LOG_PATH = "%h/jdefprog_annoproc_log.txt";

    protected String getLogFileName(ProcessingEnvironment processingEnv) {
        String logPathStr = this.processingEnv.getOptions().get(LOG_PATH_OPT);
        if (null == logPathStr) {
            return DEFAULT_LOG_PATH;
        }
        return logPathStr;
    }

    private static boolean isKnownOption(String optionName) {
        for (String opt : OPTIONS) {
            if (opt.equals(optionName)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        String logLevelStr = this.processingEnv.getOptions().get(LOG_LEVEL_OPT);
        try {
            initRootLogger(this.getLogRootPackage(), logLevelStr, this.getLogFileName(processingEnv));
        } catch (Exception e) {
        }
        logger.finer("Initializing checker");
        checker = getChecker(processingEnv);
        logger.fine("Annotations processor initialized");
        mclpDumperPath = this.processingEnv.getOptions().get(MCL_DUMP_PATH);
        if (null != mclpDumperPath) {
            new File(mclpDumperPath).delete();
        }
        for (String optionName : this.processingEnv.getOptions().keySet()) {
            if (!isKnownOption(optionName)) {
                logger.warning("Unknown option found: " + optionName);
            }
        }
    }

    private String mclpDumperPath;

    private RulesChecker<AnnotationLocation> getChecker(ProcessingEnvironment pe) {
        LocatedMessageEmitter<AnnotationLocation> msgEmitter = new ProcMessageEmitter(pe.getMessager());
        RulesChecker<AnnotationLocation> checker = new RulesChecker<AnnotationLocation>(msgEmitter, new ApInfoProvider(pe.getTypeUtils(), pe.getElementUtils()));
        return checker;
    }

    private static final Level DEFAULT_LOG_LEVEL = Level.WARNING;

    protected void addHandlerToRootLogger(Handler h) {
        this.rootLogger.addHandler(h);
    }

    private void initRootLogger(String packageName, String logLevelStr, String logFileName) throws SecurityException, IOException {
        Handler fh = null;
        fh = new FileHandler(logFileName);
        fh.setFormatter(new SimpleFormatter());
        this.rootLogger = Logger.getLogger(packageName);
        rootLogger.addHandler(fh);
        Level logLevel = getLogLevel(logLevelStr);
        fh.setLevel(logLevel);
        rootLogger.setLevel(logLevel);
    }

    private static Level getLogLevel(String logLevelStr) {
        if (null != logLevelStr && logLevelStr.length() > 0) {
            try {
                return Level.parse(logLevelStr);
            } catch (IllegalArgumentException iae) {
                logger.warning("Indicated log level is not correct ('" + logLevelStr + "')");
                return DEFAULT_LOG_LEVEL;
            }
        } else {
            return DEFAULT_LOG_LEVEL;
        }
    }

    private RulesChecker<AnnotationLocation> checker;

    @Override
    public boolean process(Set<? extends TypeElement> typeElements, RoundEnvironment roundEnv) {
        logger.fine("Starting annotations processing");
        PrintStream dumperMclStream = null;
        if (null != mclpDumperPath) {
            try {
                dumperMclStream = new PrintStream(new FileOutputStream(new File(mclpDumperPath), true));
                net.sourceforge.jdefprog.annocheck.MclAnalysisConfiguration.getInstance().setMclDumperStream(dumperMclStream);
            } catch (FileNotFoundException e) {
                logger.severe("Can not set dumper stream on file " + mclpDumperPath);
            }
        }
        for (Class<? extends Annotation> annotationType : checker.getKnownAnnotationTypes()) {
            logger.fine("Looking for annotation " + annotationType.getCanonicalName());
            Set<? extends Element> elements = roundEnv.getElementsAnnotatedWith(annotationType);
            logger.finer("Found " + elements.size() + " elements of " + annotationType.getCanonicalName());
            for (Element element : elements) {
                Annotation annotation = element.getAnnotation(annotationType);
                try {
                    switch(element.getKind()) {
                        case CLASS:
                            {
                                logger.finest("Annotation " + annotation + " found on class " + element);
                                Type t = new ApType((TypeElement) element, this.processingEnv.getTypeUtils(), this.processingEnv.getElementUtils());
                                checker.checkOnDeclared(new AnnotationLocation(element, annotationType), annotationType, t, annotation);
                            }
                            ;
                            break;
                        case CONSTRUCTOR:
                        case METHOD:
                            {
                                logger.finest("Annotation " + annotation + " found on behavior " + element);
                                Behavior b = new ApBehavior((ExecutableElement) element, this.processingEnv.getTypeUtils(), this.processingEnv.getElementUtils());
                                checker.checkOnBehavior(new AnnotationLocation(element, annotationType), annotationType, b, annotation);
                            }
                            ;
                            break;
                        case PARAMETER:
                            {
                                logger.finest("Annotation " + annotation + " found on parameter " + element + "of method " + element.getEnclosingElement() + " of class " + element.getEnclosingElement().getEnclosingElement());
                                Parameter p = new ApParameter((VariableElement) element, this.processingEnv.getTypeUtils(), this.processingEnv.getElementUtils());
                                checker.checkOnParameter(new AnnotationLocation(element, annotationType), annotationType, p, annotation);
                            }
                            ;
                            break;
                        case FIELD:
                            {
                                logger.finest("Annotation " + annotation + " found on field " + element);
                                Field f = new ApField((VariableElement) element, this.processingEnv.getTypeUtils(), this.processingEnv.getElementUtils());
                                checker.checkOnField(new AnnotationLocation(element, annotationType), annotationType, f, annotation);
                            }
                            ;
                            break;
                        default:
                            logger.severe("Annotation " + annotation + " found where not expected: " + element);
                    }
                } catch (Exception e) {
                    logger.severe("Error processing annotation " + annotation + ": " + e.getMessage() + ", class:" + e.getClass().getCanonicalName());
                    LoggingUtils.logException(logger, e);
                }
            }
        }
        if (dumperMclStream != null) {
            dumperMclStream.close();
        }
        logger.fine("Annotations processing ended");
        return true;
    }
}
