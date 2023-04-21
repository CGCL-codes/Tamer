package de.searchworkorange.indexcrawler.crawler.indexer.MultiFileIndexer;

import de.searchworkorange.lib.acl.NoACLCheckAvailableException;
import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import jcifs.smb.NtlmAuthenticator;
import jcifs.smb.SmbAuthException;
import jcifs.smb.SmbException;
import jcifs.smb.SmbFile;
import org.apache.log4j.Level;
import de.searchworkorange.indexcrawler.SmbShare;
import de.searchworkorange.indexcrawler.cifs.CifsAuthenticator;
import de.searchworkorange.indexcrawler.cifs.exception.NoDomainControllerDefinedException;
import de.searchworkorange.indexcrawler.configuration.ConfigurationCollection;
import de.searchworkorange.indexcrawler.configuration.StaticConfiguration;
import de.searchworkorange.indexcrawler.crawler.*;
import de.searchworkorange.indexcrawler.crawler.exceptions.NoIndexPathException;
import de.searchworkorange.indexcrawler.crawler.exceptions.NoIndexWriterAvailableException;
import de.searchworkorange.indexcrawler.crawler.indexer.extensions.UnknownIndexerException;
import de.searchworkorange.indexcrawler.crawler.statistic.PerShareStatistic;
import de.searchworkorange.indexcrawler.crawler.statistic.RunningThreadsReporter;
import de.searchworkorange.indexcrawler.fileDocument.exceptions.FileDocumentException;
import de.searchworkorange.indexcrawler.fileDocument.reader.exceptions.UnknownFileObjectException;
import de.searchworkorange.lib.acl.AclDirectoryDataWriter;
import de.searchworkorange.lib.acl.SmbACLCheck;
import de.searchworkorange.lib.cifsauth.MyNtlmAuthenticator;
import de.searchworkorange.lib.logger.LoggerCollection;
import java.io.File;

/**
 * 
 * @author Sascha Kriegesmann kriegesmann at vaxnet.de
 */
public class SmbServiceMultiFileIndexer extends MultiFileIndexer implements Runnable {

    private static final boolean CLASSDEBUG = false;

    private boolean recursive = true;

    private MyNtlmAuthenticator smbAuthenticator = null;

    private SmbACLCheck aclCheck = null;

    private AclDirectoryDataWriter aclDirectoryData = null;

    /**
     * 
     * @param loggerCol
     * @param config
     * @param reporter
     * @param tc
     * @throws NoDomainControllerDefinedException
     */
    public SmbServiceMultiFileIndexer(LoggerCollection loggerCol, ConfigurationCollection config, ThreadGroup parentThreadGroup, RunningThreadsReporter reporter, Crawler tc, SmbShare sshr) throws NoDomainControllerDefinedException {
        super(loggerCol, config, parentThreadGroup, reporter, tc, sshr);
        if (config.getShareList().size() > 0) {
            try {
                smbAuthenticator = new CifsAuthenticator(loggerCol, config);
                NtlmAuthenticator.setDefault(smbAuthenticator);
                aclCheck = new SmbACLCheck(loggerCol, smbAuthenticator);
            } catch (FileNotFoundException ex) {
                loggerCol.logException(CLASSDEBUG, this.getClass().getName(), Level.FATAL, ex);
            }
        }
    }

    @Override
    public void run() {
        String smbSharePathName = shr.getPathName();
        try {
            SmbFile toIndexingSmbFile = new SmbFile(smbSharePathName);
            if (toIndexingSmbFile.exists() && toIndexingSmbFile.canRead()) {
                if (config.isCheckACL()) {
                    if (toIndexingSmbFile.isDirectory()) {
                        String aclDirectoryOutputFileName = super.shr.getIndexPathName();
                        if (!aclDirectoryOutputFileName.endsWith("/")) {
                            aclDirectoryOutputFileName += "/";
                        }
                        aclDirectoryOutputFileName += StaticConfiguration.getACL_DIRECTORY_FILE();
                        if (!toIndexingSmbFile.getCanonicalPath().endsWith(java.io.File.separator)) {
                            try {
                                toIndexingSmbFile = new SmbFile(toIndexingSmbFile.getCanonicalPath() + java.io.File.separator);
                            } catch (MalformedURLException ex) {
                                loggerCol.logException(CLASSDEBUG, this.getClass().getName(), Level.FATAL, ex);
                            }
                        }
                        loggerCol.logStd(CLASSDEBUG, this.getClass().getName(), Level.INFO, "SMB CRAWL RECURSIVE DIRECTORIES FROM " + toIndexingSmbFile.getCanonicalPath() + " AND WRITE ACLS TO: " + aclDirectoryOutputFileName);
                        aclDirectoryData = new AclDirectoryDataWriter(super.loggerCol, aclCheck);
                        aclDirectoryData.crawlData(toIndexingSmbFile);
                        aclDirectoryData.writeToLocalFile(new File(aclDirectoryOutputFileName));
                    }
                }
                loggerCol.logStd(CLASSDEBUG, this.getClass().getName(), Level.INFO, "SMB CALCULATING FILES:" + toIndexingSmbFile.getCanonicalPath());
                PerShareStatistic perShareStats = new PerShareStatistic(loggerCol, reporter.getStatsModel(), smbSharePathName);
                perShareStats.setCalculating(true);
                try {
                    localSmbFileToIndex(toIndexingSmbFile, perShareStats, true, "SMB");
                    perShareStats.setCalculating(false);
                    if (!super.createNewIndex) {
                        semaphore.v();
                        mutex.p();
                        deleting = true;
                        loggerCol.logStd(CLASSDEBUG, this.getClass().getName(), Level.INFO, "SMB INDEXING AND DELETING STALE DOC FILES: " + toIndexingSmbFile.getCanonicalPath());
                        rootFileObjectToIndex(toIndexingSmbFile, perShareStats, false, "SMB");
                        mutex.v();
                    }
                    while (checkIndexInUse(super.indexPathName)) {
                        semaphore.p();
                    }
                    mutex.p();
                    try {
                        super.touchIndexWriter();
                    } catch (NoIndexWriterAvailableException ex) {
                        loggerCol.logException(CLASSDEBUG, this.getClass().getName(), Level.FATAL, ex);
                    }
                    canStat = true;
                    loggerCol.logDebug(CLASSDEBUG, this.getClass().getName(), Level.DEBUG, "SMB INDEXING FILES:" + toIndexingSmbFile.getCanonicalPath());
                    rootFileObjectToIndex(toIndexingSmbFile, perShareStats, false, "SMB");
                    mutex.v();
                } catch (NoIndexPathException ex) {
                    loggerCol.logException(CLASSDEBUG, this.getClass().getName(), Level.FATAL, ex);
                } catch (FileDocumentException ex) {
                    loggerCol.logException(CLASSDEBUG, this.getClass().getName(), Level.FATAL, ex);
                } catch (UnknownIndexerException ex) {
                    loggerCol.logException(CLASSDEBUG, this.getClass().getName(), Level.FATAL, ex);
                }
            } else {
                loggerCol.logStd(CLASSDEBUG, this.getClass().getName(), Level.INFO, "SmbFile NOT FOUND:" + toIndexingSmbFile.getCanonicalPath());
                super.fileNotFound = true;
            }
        } catch (NoACLCheckAvailableException ex) {
            loggerCol.logException(CLASSDEBUG, this.getClass().getName(), Level.FATAL, ex);
        } catch (UnknownFileObjectException ex) {
            loggerCol.logException(CLASSDEBUG, this.getClass().getName(), Level.FATAL, ex);
        } catch (SmbAuthException ex) {
            loggerCol.logException(CLASSDEBUG, this.getClass().getName(), Level.FATAL, "toIndexingSmbPath:" + smbSharePathName);
            loggerCol.logException(CLASSDEBUG, this.getClass().getName(), Level.FATAL, ex);
        } catch (SmbException ex) {
            loggerCol.logException(CLASSDEBUG, this.getClass().getName(), Level.FATAL, ex);
        } catch (MalformedURLException ex) {
            loggerCol.logException(CLASSDEBUG, this.getClass().getName(), Level.FATAL, ex);
        }
    }

    /**
     * 
     * @return boolean
     */
    @Override
    public boolean isFileNotFound() {
        return super.isFileNotFound();
    }

    /**
     * 
     * @return SmbACLCheck
     */
    public synchronized SmbACLCheck getACLCheck() {
        return aclCheck;
    }
}
