package uk.ac.ebi.intact.confidence.plugin;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.*;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.project.MavenProject;
import uk.ac.ebi.intact.bridges.blast.BlastConfig;
import uk.ac.ebi.intact.bridges.blast.model.UniprotAc;
import uk.ac.ebi.intact.business.IntactTransactionException;
import uk.ac.ebi.intact.confidence.intact.IntactConfidenceCalculator;
import uk.ac.ebi.intact.confidence.intact.TrainModel;
import uk.ac.ebi.intact.confidence.intact.IntactScoreCalculator;
import uk.ac.ebi.intact.confidence.maxent.OpenNLPMaxEntClassifier;
import uk.ac.ebi.intact.confidence.utils.ParserUtils;
import uk.ac.ebi.intact.confidence.util.AttributeGetterException;
import uk.ac.ebi.intact.confidence.filter.FilterException;
import uk.ac.ebi.intact.context.IntactContext;
import uk.ac.ebi.intact.core.persister.PersisterHelper;
import uk.ac.ebi.intact.model.Confidence;
import uk.ac.ebi.intact.model.CvConfidenceType;
import uk.ac.ebi.intact.model.InteractionImpl;
import uk.ac.ebi.intact.model.util.CvObjectUtils;
import uk.ac.ebi.intact.persistence.dao.InteractionDao;
import uk.ac.ebi.intact.plugin.IntactAbstractMojo;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * Plugin for filling the IntAct database up with confidence values.
 *
 * @author Irina Armean (iarmean@ebi.ac.uk)
 * @version $Id$
 * @since 1.0
 * 
 * @goal assign
 * @phase install
 */
public class FillDbMojo extends IntactAbstractMojo {

    /**
     * Sets up a logger for that class.
     */
    public static final Log log = LogFactory.getLog(FillDbMojo.class);

    /**
     * Project instance
     *
     * @parameter expression="${project}"
     * @readonly
     */
    protected MavenProject project;

    /**
     * The path to the work directory
     *
     * @parameter expression="${workDirPath}" default-value="${project.build.outputDirectory}"
     */
    private String workDirPath;

    /**
     * The path to the blast archive directory
     *
     * @parameter expression="${blastArchivePath}" default-value="${project.build.outputDirectory}"
     */
    private String blastArchivePath;

    /**
     * The path to the blast database directory
     *
     * @parameter expression="${blastDbPath}" default-value="${project.build.outputDirectory}"
     */
    private String blastDbPath;

    /**
     * The email for the blast web service
     *
     * @parameter expression="${email}" default-value="${x@ebi.ac.uk}"
     */
    private String email;

    /**
     * The path to the high confidence set file.
     *
     * @parameter expression="${hcSetPath}" default-value="${project.build.outputDirectory}"
     */
    private String hcSetPath;

    /**
     * The path to the GO annotation file for IntAct.
     *
     * @parameter expression="${goaFilePath}" default-value="${workDirPath/gene_association.goa_intact}"
     */
    private String goaFilePath;

    /**
     * The path to the classifier file.
     *
     * @parameter expression="${gisModelPath}" default-value="${project.build.outputDirectory}"
     */
    private String gisModelPath;

    /**
     * The path to the hibernate config file.
     *
     * @parameter expression="${hibernateCfgFile}" default-value="${project.build.outputDirectory}"
     */
    private String hibernateCfgFile;

    /**
     * The path to the hibernate config file.
     *
     * @parameter expression="${override}" default-value="${false}"
     */
    private boolean override;

    protected Appender getLogAppender() throws IOException {
        File logFile = new File(getDirectory(), "log.out");
        getLog().info("Setting log4j in output: " + logFile.getAbsolutePath());
        Layout layout = getLogLayout();
        FileAppender appender = new FileAppender(layout, logFile.getAbsolutePath(), false);
        appender.setThreshold(Priority.INFO);
        return appender;
    }

    protected Layout getLogLayout() {
        String pattern = "%d [%t] %-5p (%C{1},%L) - %m%n";
        PatternLayout layout = new PatternLayout(pattern);
        return layout;
    }

    public MavenProject getProject() {
        return project;
    }

    public void execute() throws MojoExecutionException, MojoFailureException {
        enableLogging();
        if (log.isDebugEnabled()) {
            log.debug("Debug active.");
        }
        if (log.isInfoEnabled()) {
            log.info("Info active.");
        }
        if (log.isWarnEnabled()) {
            log.warn("Warn active.");
        }
        if (log.isErrorEnabled()) {
            log.error("Error active.");
        }
        if (log.isFatalEnabled()) {
            log.fatal("Fatal active.");
        }
        if (log.isInfoEnabled()) {
            log.info("mem: " + (Runtime.getRuntime().maxMemory()) / (1024 * 1024));
            log.info("DB config file: " + hibernateCfgFile);
            log.info("override: " + override);
            log.info("workDirPath: " + workDirPath);
            log.info("blastArchivePath: " + blastArchivePath);
            log.info("blastDbPath: " + blastDbPath);
            log.info("email: " + email);
            log.info("hcSetPath: " + hcSetPath);
            log.info("gisModel File: " + gisModelPath);
            log.info("GOA File: " + goaFilePath);
        }
        File workDir = new File(workDirPath);
        File dbFolder = new File(blastDbPath);
        dbFolder.mkdir();
        File blastArchiveDir = new File(blastArchivePath);
        blastArchiveDir.mkdir();
        BlastConfig config = new BlastConfig(email);
        config.setBlastArchiveDir(blastArchiveDir);
        config.setDatabaseDir(dbFolder);
        File gisModelFile = new File(gisModelPath);
        File pgConfigFile = new File(hibernateCfgFile);
        File goaFile = new File(goaFilePath);
        IntactContext.initStandaloneContext(pgConfigFile);
        try {
            fillDb(workDir, config, new File(hcSetPath), gisModelFile, goaFile);
        } catch (IOException e) {
            log.error("exception", e);
        } catch (IntactTransactionException e) {
            log.error("exception", e);
        } catch (AttributeGetterException e) {
            log.error("exception", e);
        } catch (FilterException e) {
            log.error("exception", e);
        }
    }

    private void fillDb(File workDir, BlastConfig config, File hcSet, File gisModelFile, File goaFile) throws IOException, IntactTransactionException, AttributeGetterException, FilterException {
        log.info("goaFile: " + goaFile.getPath());
        OpenNLPMaxEntClassifier classifier = new OpenNLPMaxEntClassifier(gisModelFile);
        Set<UniprotAc> againstProts = ParserUtils.parseProteins(hcSet);
        IntactScoreCalculator ic = new IntactConfidenceCalculator(classifier, config, againstProts, goaFile, workDir);
        CvConfidenceType cvConfidence = (CvConfidenceType) IntactContext.getCurrentInstance().getDataContext().getDaoFactory().getCvObjectDao().getByShortLabel("intact confidence");
        if (cvConfidence == null) {
            cvConfidence = CvObjectUtils.createCvObject(IntactContext.getCurrentInstance().getInstitution(), CvConfidenceType.class, null, "intact confidence");
            PersisterHelper.saveOrUpdate(cvConfidence);
        }
        InteractionDao interactionDao = IntactContext.getCurrentInstance().getDataContext().getDaoFactory().getInteractionDao();
        int totalNr = interactionDao.countAll();
        System.out.println("total: " + totalNr);
        if (log.isInfoEnabled()) {
            log.info(" total Nr of interactions: " + totalNr);
        }
        int firstResult = 0;
        int maxResults = 50;
        boolean firstTime = true;
        List<InteractionImpl> interactions = null;
        for (int i = 0; i < totalNr; i += maxResults) {
            IntactContext.getCurrentInstance().getDataContext().beginTransaction();
            interactions = interactionDao.getAll(firstResult, maxResults);
            ic.calculate(interactions, override);
            saveInteractionsToDb(interactions);
            if (firstTime) {
                firstTime = false;
            }
            if (log.isInfoEnabled()) {
                int processed = firstResult + interactions.size();
                if (firstResult != processed) {
                    log.info("\t\tProcessed " + (firstResult + interactions.size() + " out of " + totalNr));
                }
            }
            firstResult = firstResult + maxResults;
            IntactContext.getCurrentInstance().getDataContext().commitTransaction();
        }
        if (log.isInfoEnabled()) {
            log.info("Processed " + totalNr + " IntAct interactions.");
        }
    }

    private void saveInteractionsToDb(List<InteractionImpl> interactions) throws IntactTransactionException {
        CvConfidenceType cvConfidenceType = (CvConfidenceType) IntactContext.getCurrentInstance().getDataContext().getDaoFactory().getCvObjectDao().getByShortLabel("intact confidence");
        for (Iterator<InteractionImpl> iter = interactions.iterator(); iter.hasNext(); ) {
            InteractionImpl interaction = iter.next();
            if (interaction.getConfidences().size() != 0) {
                Confidence confidence = interaction.getConfidences().iterator().next();
                confidence.setCvConfidenceType(cvConfidenceType);
                IntactContext.getCurrentInstance().getDataContext().getDaoFactory().getInteractionDao().update(interaction);
                IntactContext.getCurrentInstance().getDataContext().getDaoFactory().getConfidenceDao().persist(confidence);
            }
        }
        IntactContext.getCurrentInstance().getDataContext().commitTransaction();
    }
}
