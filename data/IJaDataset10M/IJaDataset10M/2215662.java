package edu.upmc.opi.caBIG.caTIES.installer.pipes;

import java.text.DecimalFormat;
import org.apache.log4j.Logger;
import org.hibernate.Session;
import edu.upmc.opi.caBIG.caTIES.common.CaTIES_Constants;
import edu.upmc.opi.caBIG.caTIES.connector.CaTIES_DataSourceManager;
import edu.upmc.opi.caBIG.caTIES.middletier.CaTIES_DataMiddleTierImpl;
import edu.upmc.opi.caBIG.caTIES.server.index.CaTIES_Indexer;
import edu.upmc.opi.caBIG.caTIES.server.index.CaTIES_Sequencer;

public class CaTIES_IndexPipeController implements Runnable {

    private static final Logger logger = Logger.getLogger(CaTIES_IndexPipeController.class);

    private String hibernatePublicConfiguration;

    private int indexParameterBatchSize = 1;

    private int indexParameterSleepSize = 10;

    private String indexParameterAncestoryIndex = "";

    private String indexParameterLuceneIndex = "";

    private String indexParameterPubDriver = "";

    private String indexParameterPubUrl = "";

    private String indexParameterPubUserName = "";

    private String indexParameterPubPassword = "";

    CaTIES_DataMiddleTierImpl middleTier = new CaTIES_DataMiddleTierImpl();

    private String tomcatHomePath;

    public static void main(String[] args) {
    }

    public void runStandAlone() {
        try {
            initialize();
            CaTIES_DataSourceManager publicDataSourceManager = new CaTIES_DataSourceManager();
            Session publicSession = publicDataSourceManager.getMySQLSession(getIndexParameterPubUrl(), getIndexParameterPubUserName(), getIndexParameterPubPassword(), "update");
            middleTier.setSession(publicSession);
            if (runSequencer()) {
                runIndexer();
            }
            middleTier.setSession(null);
            publicDataSourceManager.destroy();
        } catch (Exception x) {
            x.printStackTrace();
        }
    }

    public void run() {
        initialize();
        while (true) {
            try {
                CaTIES_DataSourceManager publicDataSourceManager = new CaTIES_DataSourceManager();
                Session publicSession = publicDataSourceManager.getMySQLSession(getIndexParameterPubUrl(), getIndexParameterPubUserName(), getIndexParameterPubPassword(), "update");
                middleTier.setSession(publicSession);
                if (runSequencer()) runIndexer();
                middleTier.setSession(null);
                publicDataSourceManager.destroy();
                logger.info("Work completed. Going to sleep...");
                sleep(indexParameterSleepSize);
                break;
            } catch (Exception x) {
                x.printStackTrace();
            }
        }
    }

    public void initialize() {
        setSleepDuration();
        setBatchSize();
        logPipeInfo();
    }

    private void setSleepDuration() {
        String constant = System.getProperty(CaTIES_Constants.PROPERTY_KEY_INDEXER_SLEEPSIZE);
        if (constant == null) indexParameterSleepSize = 300; else indexParameterSleepSize = Integer.parseInt(constant.trim());
    }

    private void setBatchSize() {
        String constant = System.getProperty(CaTIES_Constants.PROPERTY_KEY_INDEXER_BATCHSIZE);
        if (constant == null) indexParameterBatchSize = 100; else indexParameterBatchSize = Integer.parseInt(constant.trim());
    }

    private void logPipeInfo() {
        logger.info("-------------------------");
        DecimalFormat format = new DecimalFormat();
        logger.info("Free Memory: " + format.format(Runtime.getRuntime().freeMemory()) + " MB");
        logger.info("Max Memory: " + format.format(Runtime.getRuntime().maxMemory()) + " MB");
        logger.info("Total Memory: " + format.format(Runtime.getRuntime().freeMemory()) + " MB");
        logger.info("-------------------------");
        logger.info("Using the following properties:");
        logger.info("   Report Index Location: " + indexParameterLuceneIndex);
        logger.info("   Ancestry Index Location: " + indexParameterAncestoryIndex);
        logger.info("   Sleep Duration: " + (new Long(indexParameterSleepSize / 60000)).intValue() + " min.");
        logger.info("   Batch Size: " + indexParameterBatchSize);
    }

    private boolean runSequencer() {
        logger.info("Starting Sequencer...");
        try {
            CaTIES_Sequencer sequencer = new CaTIES_Sequencer(middleTier);
            sequencer.setBatchSize(indexParameterBatchSize);
            sequencer.run();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
            return false;
        }
    }

    private void runIndexer() {
        logger.info("Starting Indexer...");
        try {
            CaTIES_Indexer indexer = new CaTIES_Indexer(middleTier, indexParameterLuceneIndex, indexParameterAncestoryIndex);
            indexer.setBatchSize(indexParameterBatchSize);
            indexer.run();
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        }
    }

    private void sleep(long milliSeconds) {
        try {
            Thread.sleep(milliSeconds);
        } catch (InterruptedException ie) {
        }
    }

    public void shutdown() {
        logger.info("Shutting down...");
        middleTier.destroy();
    }

    public int getIndexParameterSleepSize() {
        return indexParameterSleepSize;
    }

    public void setIndexParameterSleepSize(int indexParameterSleepSize) {
        this.indexParameterSleepSize = indexParameterSleepSize;
    }

    public int getIndexParameterBatchSize() {
        return indexParameterBatchSize;
    }

    public void setIndexParameterBatchSize(int indexParameterBatchSize) {
        this.indexParameterBatchSize = indexParameterBatchSize;
    }

    public String getIndexParameterAncestoryIndex() {
        return indexParameterAncestoryIndex;
    }

    public void setIndexParameterAncestoryIndex(String indexParameterAncestoryIndex) {
        this.indexParameterAncestoryIndex = indexParameterAncestoryIndex;
        System.out.println("The ancestory index is " + this.indexParameterAncestoryIndex);
    }

    public String getIndexParameterLuceneIndex() {
        return indexParameterLuceneIndex;
    }

    public void setIndexParameterLuceneIndex(String indexParameterLuceneIndex) {
        this.indexParameterLuceneIndex = indexParameterLuceneIndex;
    }

    public String getIndexParameterPubDriver() {
        return indexParameterPubDriver;
    }

    public void setIndexParameterPubDriver(String indexParameterPubDriver) {
        this.indexParameterPubDriver = indexParameterPubDriver;
    }

    public String getIndexParameterPubUrl() {
        return indexParameterPubUrl;
    }

    public void setIndexParameterPubUrl(String indexParameterPubUrl) {
        this.indexParameterPubUrl = indexParameterPubUrl;
    }

    public String getIndexParameterPubUserName() {
        return indexParameterPubUserName;
    }

    public void setIndexParameterPubUserName(String indexParameterPubUserName) {
        this.indexParameterPubUserName = indexParameterPubUserName;
    }

    public String getIndexParameterPubPassword() {
        return indexParameterPubPassword;
    }

    public void setIndexParameterPubPassword(String indexParameterPubPassword) {
        this.indexParameterPubPassword = indexParameterPubPassword;
    }

    public String getTomcatHomePath() {
        return tomcatHomePath;
    }

    public void setTomcatHomePath(String tomcatHomePath) {
        this.tomcatHomePath = tomcatHomePath;
    }
}
