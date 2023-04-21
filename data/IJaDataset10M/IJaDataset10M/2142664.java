package net.sf.dropboxmq.dropboxsupport;

import java.io.File;
import java.io.FileFilter;
import java.util.StringTokenizer;
import javax.jms.Destination;
import javax.jms.JMSException;
import net.sf.dropboxmq.Configuration;
import net.sf.dropboxmq.DropboxMQJMSException;
import net.sf.dropboxmq.DropboxTransaction;
import net.sf.dropboxmq.FileSystem;
import net.sf.dropboxmq.LogHelper;
import net.sf.dropboxmq.destinations.DestinationImpl;
import net.sf.dropboxmq.destinations.QueueImpl;
import net.sf.dropboxmq.messagetranscoders.DefaultMessageTranscoder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Created: 15 Nov 2008
 *
 * @author <a href="mailto:dwayne@schultz.net">Dwayne Schultz</a>
 * @version $Revision: 235 $, $Date: 2011-08-27 00:55:14 -0400 (Sat, 27 Aug 2011) $
 */
public class DirectoryStructure {

    private static final Log log = LogFactory.getLog(DirectoryStructure.class);

    public static final String TOPIC_ROOT_SUB_DIR = "topic-root";

    public static final String DROPBOXMQ_SUB_DIR = "dropboxmq";

    public static final String CLIENTS_SUB_DIR = DROPBOXMQ_SUB_DIR + File.separator + "clients";

    public static final String PROCESSING_DESTINATION_SUFFIX = "~PROCESSING";

    public static final String EXPIRED_DESTINATION_SUFFIX = "~EXPIRED";

    public static final String RECOVER_ERROR_DESTINATION_SUFFIX = "~RECOVER-ERROR";

    public static final String REJECT_ERROR_DESTINATION_SUFFIX = "~REJECT-ERROR";

    private static final String[] DESTINATION_SUFFIXES = { PROCESSING_DESTINATION_SUFFIX, EXPIRED_DESTINATION_SUFFIX, RECOVER_ERROR_DESTINATION_SUFFIX, REJECT_ERROR_DESTINATION_SUFFIX };

    public static final int FILENAME_MAX_LENGTH = 255;

    public static final String WORKING_DIR_NAME = "working";

    public static final String TARGET_DIR_NAME = "target";

    public static final String PROCESSING_DIR_NAME = "processing";

    public static final String PROCESSED_DIR_NAME = "processed";

    public static final String ERROR_DIR_NAME = "error";

    public static final String EXPIRED_DIR_NAME = "expired";

    public static final String SUBSCRIPTIONS_DIR_NAME = ".subscriptions";

    public static final String TOPIC_INCOMING_DIR_NAME = ".incoming";

    public static final String DELETED_NAME_PREFIX = ".deleted";

    public static class InOutStructure {

        private final File inOutDir;

        private final File workingDir;

        private final File targetDir;

        private final File processingDir;

        private final File processedDir;

        private final File expiredDir;

        private final File errorDir;

        private InOutStructure(final File inOutDir, final File workingDir, final File targetDir, final File processingDir, final File processedDir, final File expiredDir, final File errorDir) {
            this.inOutDir = inOutDir;
            this.workingDir = workingDir;
            this.targetDir = targetDir;
            this.processingDir = processingDir;
            this.processedDir = processedDir;
            this.expiredDir = expiredDir;
            this.errorDir = errorDir;
        }

        public File getInOutDir() {
            return inOutDir;
        }

        public File getWorkingDir() {
            return workingDir;
        }

        public File getTargetDir() {
            return targetDir;
        }

        public File getProcessingDir() {
            return processingDir;
        }

        public File getProcessedDir() {
            return processedDir;
        }

        public File getExpiredDir() {
            return expiredDir;
        }

        public File getErrorDir() {
            return errorDir;
        }
    }

    private final Configuration configuration;

    private final FileSystem fileSystem;

    private final InOutStructure incomingStructure;

    private final InOutStructure outgoingStructure;

    private final File subscriptionDir;

    public static File getDropboxDir(final Destination destination, final Configuration configuration) {
        LogHelper.logMethod(log, null, "getDropboxDir(), destination = " + destination + ", configuration = " + configuration);
        if (!(destination instanceof DestinationImpl)) {
            throw new RuntimeException("destination must be an instance of net.sf.dropboxmq.destinations.DestinationImpl");
        }
        final DestinationImpl destinationImpl = (DestinationImpl) destination;
        String destinationName = destinationImpl.getName();
        boolean foundSuffix = false;
        for (int i = 0; i < DESTINATION_SUFFIXES.length && !foundSuffix; i++) {
            final String destinationSuffix = DESTINATION_SUFFIXES[i];
            if (destinationName.endsWith(destinationSuffix)) {
                destinationName = destinationName.substring(0, destinationName.length() - destinationSuffix.length());
                foundSuffix = true;
            }
        }
        final File rootDir = configuration.getRootDir();
        File dropboxDir;
        if (destination instanceof QueueImpl) {
            dropboxDir = new File(rootDir, destinationName);
        } else {
            dropboxDir = new File(rootDir, TOPIC_ROOT_SUB_DIR);
            final StringTokenizer tokenizer = new StringTokenizer(destinationName, ".");
            while (tokenizer.hasMoreTokens()) {
                dropboxDir = new File(dropboxDir, tokenizer.nextToken());
            }
        }
        return dropboxDir;
    }

    public static void createDropbox(final DestinationImpl destination, final Configuration configuration) throws JMSException {
        LogHelper.logMethod(log, null, "createDropbox(), destination = " + destination + ", configuration = " + configuration);
        try {
            new DirectoryStructure(null, destination, null, false, configuration).createDropbox();
        } catch (FileSystem.FileSystemException e) {
            throw new DropboxMQJMSException(e);
        }
    }

    public static boolean doesDropboxExist(final Destination destination, final Configuration configuration) {
        return configuration.getFileSystem().exists(getDropboxDir(destination, configuration));
    }

    public static void deleteDropbox(final Destination destination, final Configuration configuration) throws JMSException {
        LogHelper.logMethod(log, null, "deleteDropbox(), destination = " + destination + ", configuration = " + configuration);
        final File dropboxDir = getDropboxDir(destination, configuration);
        deleteDropboxDirectory(dropboxDir, configuration);
    }

    public static void deleteDropboxDirectory(final File file, final Configuration configuration) throws JMSException {
        LogHelper.logMethod(log, null, "deleteDropboxDirectory(), file = " + file + ", configuration = " + configuration);
        final FileSystem fileSystem = configuration.getFileSystem();
        try {
            if (configuration.isPreserveDeletedDropboxes()) {
                final String deleteFilename = file.getName() + DELETED_NAME_PREFIX + "-" + System.currentTimeMillis();
                fileSystem.move(file, new File(file.getParentFile(), deleteFilename));
            } else {
                fileSystem.delete(file);
            }
        } catch (FileSystem.FileSystemException e) {
            throw new DropboxMQJMSException(e);
        }
    }

    public static File findDurableSubscriptionDir(final String subscriptionName, final String clientId, final Configuration configuration) throws JMSException {
        LogHelper.logMethod(log, null, "findDurableSubscriptionDir(), subscriptionName = " + subscriptionName + ", clientId = " + clientId + ", configuration = " + configuration);
        final File topicRootDir = new File(configuration.getRootDir(), TOPIC_ROOT_SUB_DIR);
        final String subscriptionId = getSubscriptionId(subscriptionName, clientId, false);
        final FileSystem fileSystem = configuration.getFileSystem();
        File subscriptionDir = null;
        if (fileSystem.exists(topicRootDir)) {
            subscriptionDir = findDurableSubscriptionDir(subscriptionId, topicRootDir, fileSystem);
        }
        return subscriptionDir;
    }

    private static File findDurableSubscriptionDir(final String subscriptionId, final File topicDir, final FileSystem fileSystem) {
        LogHelper.logMethod(log, null, "findDurableSubscriptionDir(), subscriptionId = " + subscriptionId + ", topicDir = " + topicDir);
        final File[] subDirs = topicDir.listFiles(new FileFilter() {

            public boolean accept(final File pathname) {
                return pathname.isDirectory() && pathname.getName().indexOf(DELETED_NAME_PREFIX) == -1 && !pathname.getName().equals(TOPIC_INCOMING_DIR_NAME);
            }
        });
        log.trace("found " + subDirs.length + " subdirectories");
        File subscriptionDir = null;
        for (int i = 0; i < subDirs.length && subscriptionDir == null; i++) {
            final File subDir = subDirs[i];
            log.trace("analyzing " + subDir);
            if (subDir.isDirectory() && subDir.getName().indexOf(DELETED_NAME_PREFIX) == -1 && !subDir.getName().equals(TOPIC_INCOMING_DIR_NAME)) {
                if (subDir.getName().equals(SUBSCRIPTIONS_DIR_NAME)) {
                    final File potentialSubDir = new File(subDir, subscriptionId);
                    if (fileSystem.exists(potentialSubDir)) {
                        subscriptionDir = potentialSubDir;
                    } else {
                        final File[] subscriptionDirs = subDir.listFiles();
                        for (int j = 0; j < subscriptionDirs.length; j++) {
                            final File potentialNoLocalSubDir = subscriptionDirs[j];
                            if (potentialNoLocalSubDir.getName().startsWith(subscriptionId + ".")) {
                                subscriptionDir = potentialNoLocalSubDir;
                            }
                        }
                    }
                } else {
                    subscriptionDir = findDurableSubscriptionDir(subscriptionId, subDir, fileSystem);
                }
            }
        }
        return subscriptionDir;
    }

    private static String getSubscriptionId(final String subscriptionName, final String clientId, final boolean noLocal) throws JMSException {
        return DefaultMessageTranscoder.encode(subscriptionName) + "." + DefaultMessageTranscoder.encode(clientId == null ? "" : clientId) + (noLocal ? ".no-local" : "");
    }

    public DirectoryStructure(final String clientId, final DestinationImpl destination, final String subscriptionName, final boolean noLocal, final Configuration configuration) throws JMSException {
        this.configuration = configuration;
        fileSystem = configuration.getFileSystem();
        final File dropboxDir = getDropboxDir(destination, configuration);
        final String incomingDirName = configuration.getIncomingDirectoryName();
        final String outgoingDirName = configuration.getOutgoingDirectoryName();
        final boolean hasInLevel = incomingDirName != null && incomingDirName.length() > 0;
        final boolean hasOutLevel = outgoingDirName != null && outgoingDirName.length() > 0;
        if (hasInLevel && !hasOutLevel || !hasInLevel && hasOutLevel) {
            throw new JMSException("Disagreement between incomingDirectoryName and outgoingDirectoryName. If one is " + "undefined, both must be undefined");
        }
        final File incomingDir;
        if (destination instanceof QueueImpl) {
            incomingDir = hasInLevel ? new File(dropboxDir, incomingDirName) : dropboxDir;
        } else {
            final File topIncomingDir = new File(dropboxDir, TOPIC_INCOMING_DIR_NAME);
            if (subscriptionName == null) {
                incomingDir = topIncomingDir;
            } else {
                final File subscriptionsDir = new File(dropboxDir, SUBSCRIPTIONS_DIR_NAME);
                final String subscriptionId = getSubscriptionId(subscriptionName, clientId, noLocal);
                incomingDir = new File(subscriptionsDir, subscriptionId);
            }
        }
        final String name = destination.getName();
        incomingStructure = newInOutStructure(incomingDir, name);
        if (!hasOutLevel || incomingDirName.equals(outgoingDirName)) {
            outgoingStructure = incomingStructure;
        } else {
            outgoingStructure = newInOutStructure(new File(dropboxDir, outgoingDirName), name);
        }
        if (subscriptionName == null) {
            subscriptionDir = null;
        } else {
            try {
                createDropbox();
            } catch (FileSystem.FileSystemException e) {
                throw new DropboxMQJMSException(e);
            }
            subscriptionDir = incomingDir;
        }
    }

    private static InOutStructure newInOutStructure(final File inOutDir, final String name) {
        File workingDir = new File(inOutDir, WORKING_DIR_NAME);
        File targetDir = new File(inOutDir, TARGET_DIR_NAME);
        File processingDir = new File(inOutDir, PROCESSING_DIR_NAME);
        File processedDir = new File(inOutDir, PROCESSED_DIR_NAME);
        File expiredDir = new File(inOutDir, EXPIRED_DIR_NAME);
        File errorDir = new File(inOutDir, ERROR_DIR_NAME);
        if (name.endsWith(RECOVER_ERROR_DESTINATION_SUFFIX)) {
            targetDir = new File(inOutDir, ERROR_DIR_NAME);
            processedDir = new File(inOutDir, TARGET_DIR_NAME);
            expiredDir = null;
        } else if (name.endsWith(REJECT_ERROR_DESTINATION_SUFFIX)) {
            targetDir = new File(inOutDir, ERROR_DIR_NAME);
            expiredDir = null;
        } else if (name.endsWith(EXPIRED_DESTINATION_SUFFIX)) {
            targetDir = new File(inOutDir, EXPIRED_DIR_NAME);
            expiredDir = null;
        } else if (name.endsWith(PROCESSING_DESTINATION_SUFFIX)) {
            workingDir = null;
            targetDir = new File(inOutDir, PROCESSING_DIR_NAME);
            processingDir = null;
            processedDir = null;
            expiredDir = null;
            errorDir = null;
        }
        return new InOutStructure(inOutDir, workingDir, targetDir, processingDir, processedDir, expiredDir, errorDir);
    }

    public final void createDropbox() throws FileSystem.FileSystemException {
        LogHelper.logMethod(log, toObjectString(), "createDropbox()");
        createInOutStructure(incomingStructure);
        if (!incomingStructure.equals(outgoingStructure)) {
            createInOutStructure(outgoingStructure);
        }
    }

    private void createInOutStructure(final InOutStructure inOutStructure) throws FileSystem.FileSystemException {
        if (inOutStructure.workingDir != null) {
            fileSystem.mkdirs(inOutStructure.workingDir);
        }
        if (inOutStructure.targetDir != null) {
            fileSystem.mkdirs(inOutStructure.targetDir);
        }
        if (inOutStructure.processingDir != null) {
            fileSystem.mkdirs(inOutStructure.processingDir);
        }
        if (inOutStructure.processedDir != null) {
            fileSystem.mkdirs(inOutStructure.processedDir);
        }
        if (inOutStructure.expiredDir != null) {
            fileSystem.mkdirs(inOutStructure.expiredDir);
        }
        if (inOutStructure.errorDir != null) {
            fileSystem.mkdirs(inOutStructure.errorDir);
        }
        fileSystem.mkdirs(new File(configuration.getRootDir(), CLIENTS_SUB_DIR));
        DropboxTransaction.createTransactionDirs(configuration);
    }

    public InOutStructure getIncomingStructure() {
        return incomingStructure;
    }

    public InOutStructure getOutgoingStructure() {
        return outgoingStructure;
    }

    public File getSubscriptionDir() {
        return subscriptionDir;
    }

    protected final String toObjectString() {
        return super.toString();
    }
}
