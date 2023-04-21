package de.searchworkorange.indexcrawler.crawler.indexer.MultiFileIndexer;

import java.io.File;
import org.apache.log4j.Level;
import de.searchworkorange.indexcrawler.Command;
import de.searchworkorange.indexcrawler.Document.ZipObject;
import de.searchworkorange.indexcrawler.LocalShare;
import de.searchworkorange.indexcrawler.configuration.ConfigurationCollection;
import de.searchworkorange.indexcrawler.crawler.AllFilesIndexCrawler;
import de.searchworkorange.indexcrawler.crawler.Crawler;
import de.searchworkorange.indexcrawler.crawler.MyIndexWriter;
import de.searchworkorange.indexcrawler.crawler.SingleFileIndexCrawler;
import de.searchworkorange.indexcrawler.crawler.exceptions.CommandIsNotAllowedWithThisCrawlerException;
import de.searchworkorange.indexcrawler.crawler.indexer.extensions.UnknownIndexerException;
import de.searchworkorange.indexcrawler.crawler.statistic.PerShareStatistic;
import de.searchworkorange.indexcrawler.crawler.statistic.RunningThreadsReporter;
import de.searchworkorange.indexcrawler.fileDocument.exceptions.FileDocumentException;
import de.searchworkorange.lib.logger.LoggerCollection;

/**
 * 
 * @author Sascha Kriegesmann kriegesmann at vaxnet.de
 */
public class TmpZipFileServiceMultiFileIndexer extends MultiFileIndexer implements Runnable {

    private static final boolean CLASSDEBUG = false;

    private Command command = null;

    private boolean recursive = true;

    private MyIndexWriter indexWriter = null;

    /**
     * 
     * @param loggerCol
     * @param config
     * @param reporter
     * @param crawler
     */
    public TmpZipFileServiceMultiFileIndexer(LoggerCollection loggerCol, ConfigurationCollection config, ThreadGroup parentThreadGroup, RunningThreadsReporter reporter, Crawler crawler, LocalShare lshr, MyIndexWriter indexWriter) {
        super(loggerCol, config, parentThreadGroup, reporter, crawler, lshr, indexWriter);
        super.createNewIndex = true;
        if (indexWriter == null) {
            throw new IllegalArgumentException();
        } else {
            this.indexWriter = indexWriter;
            if (crawler instanceof SingleFileIndexCrawler) {
                crawler = (SingleFileIndexCrawler) crawler;
                try {
                    command = crawler.getCommand();
                } catch (CommandIsNotAllowedWithThisCrawlerException ex) {
                    loggerCol.logException(CLASSDEBUG, this.getClass().getName(), Level.FATAL, ex);
                }
            }
        }
    }

    @Override
    public void run() {
        long testTime = System.currentTimeMillis();
        for (ZipObject zipObject : zipObjectList) {
            super.zipObjectInUse = zipObject;
            String toIndexingPath = zipObject.getExtractionPath();
            File toIndexingFile = new File(toIndexingPath);
            if (toIndexingFile.exists() && toIndexingFile.canRead()) {
                loggerCol.logDebug(CLASSDEBUG, this.getClass().getName(), Level.DEBUG, "PKZIP CALCULATING FILES:" + zipObject.getZipFileName());
                PerShareStatistic perShareStats = new PerShareStatistic(loggerCol, reporter.getStatsModel(), toIndexingPath);
                if (crawler instanceof AllFilesIndexCrawler) {
                    perShareStats.setCalculating(true);
                }
                try {
                    boolean addToIndex = false;
                    if (command != null) {
                        if (command.getCommand().equals("WF")) {
                            addToIndex = true;
                        } else if ((command.getCommand().equals("RM")) || (command.getCommand().equals("RMD"))) {
                            addToIndex = false;
                        }
                    } else {
                        localFileToIndex(toIndexingFile, perShareStats, true, "ZIP");
                        addToIndex = true;
                    }
                    if (addToIndex) {
                        if (crawler instanceof AllFilesIndexCrawler) {
                            perShareStats.setCalculating(false);
                        }
                        canStat = true;
                        loggerCol.logDebug(CLASSDEBUG, this.getClass().getName(), Level.DEBUG, "PKZIP INDEXING FILES:" + zipObject.getZipFileName());
                        localFileToIndex(toIndexingFile, perShareStats, false, "ZIP", zipObject);
                    }
                } catch (FileDocumentException ex) {
                    loggerCol.logException(CLASSDEBUG, this.getClass().getName(), Level.FATAL, ex);
                } catch (UnknownIndexerException ex) {
                    loggerCol.logException(CLASSDEBUG, this.getClass().getName(), Level.FATAL, ex);
                }
                if (perShareStats != null) {
                    perShareStats.removeMeFromStatisticModel();
                }
            } else {
                loggerCol.logStd(CLASSDEBUG, this.getClass().getName(), Level.WARN, "FILE NOT FOUND:" + toIndexingFile.getAbsolutePath());
            }
            shareCounter = 0;
        }
        super.waitTillAllThreadsAreDone();
        super.setIndexWriterForZipFiles(null);
    }
}
