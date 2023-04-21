package com.germinus.xpression.cms.contents.binary;

import java.io.File;
import java.util.Random;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.germinus.xpression.cms.CMSRuntimeException;
import com.germinus.xpression.cms.CmsConfig;
import com.germinus.xpression.cms.web.TemporaryFilesHandler;

/**
 * This class helps to manage the binary fields.
 *
 * @author <a href="luish@germinus.com">Luis Miguel Hernanz</a>
 * @version $Revision$
 */
public class BinaryDataHelper {

    private static Log log = LogFactory.getLog(BinaryDataHelper.class);

    private static BinaryDataHelper instance;

    /**
     * Creates a new <code>BinaryContentHelper</code> instance.
     *
     * @exception CMSRuntimeException if an error occurs
     */
    private BinaryDataHelper() throws CMSRuntimeException {
    }

    public static BinaryDataHelper getInstance() {
        if (null == instance) instance = new BinaryDataHelper();
        return instance;
    }

    /**
     * Get the path to the binary contents repository.
     *
     * @return a <code>String</code> value
     */
    public String getRepositoryPath(Class contentClass) {
        String className = getClassName(contentClass);
        String repositoryPath = CmsConfig.getRepositoryPath(className);
        if (StringUtils.isEmpty(repositoryPath)) {
            repositoryPath = CmsConfig.getDefaultRepositoryPath();
        }
        if (!repositoryPath.endsWith("/")) {
            repositoryPath += "/";
        }
        return repositoryPath;
    }

    public String getPublicRepositoryPath(Class contentClass) {
        String className = getClassName(contentClass);
        String repositoryPath = CmsConfig.getPublicRepositoryPath(className);
        if (StringUtils.isNotEmpty(repositoryPath)) {
            return repositoryPath;
        } else {
            return getRepositoryPath(contentClass);
        }
    }

    public String getURLPrefix(Class contentClass) {
        String className = getClassName(contentClass);
        String repositoryPath = CmsConfig.getUrlPrefix(className);
        if (StringUtils.isNotEmpty(repositoryPath)) {
            return repositoryPath;
        } else {
            return CmsConfig.getDefaultUrlPrefix();
        }
    }

    /**
     * Get the maximum number of directories to use in the CMS
     * system.
     *
     * @return an <code>int</code> value
     */
    public static int getMaximumNumberOfDirectories() {
        return CmsConfig.getMaxNumOfDirectories();
    }

    /**
     * The maximum number of attemps to find a suitable path to store
     * the content.
     *
     * @return an <code>int</code> value
     */
    public int getMaximumDirectoriesAttemps() {
        return 30;
    }

    public String getNewPath(BinaryDataReference bc) {
        String destinationDir;
        String destinationFile;
        File destination;
        String correctedFileName = bc.getFileName();
        if (StringUtils.isEmpty(correctedFileName)) {
            correctedFileName = "NoName";
        }
        do {
            destinationDir = getDestinationPath(bc.getClass());
            destinationFile = destinationDir + correctedFileName;
            destination = new File(destinationFile);
            TemporaryFilesHandler.register(null, destination);
            log.debug("Trying to store the content " + destinationFile);
        } while (destination.exists());
        return destinationFile;
    }

    public String getStreamingURL() {
        return CmsConfig.getStreamingBinaryContentUrl();
    }

    public String getDBDateFormat() {
        return CmsConfig.getDbDateFormat();
    }

    public String getDbType() {
        return CmsConfig.getDbType();
    }

    /**
     * Returns the path of the directory where a new content should be
     * stored. The directory is guaranteed to exist.
     *
     * @return a <code>String</code> value
     * @exception CMSRuntimeException if an error occurs
     */
    private String getDestinationPath(Class contentClass) throws CMSRuntimeException {
        return getDestinationPath(contentClass, 0);
    }

    /**
     * Returns the path of the directory where a new content should be
     * stored. The directory is guaranteed to exist.
     *
     * @param depth the number of times thie method has been called.
     * @return a <code>String</code> value
     * @exception CMSRuntimeException if an error occurs
     */
    private String getDestinationPath(Class contentClass, int depth) throws CMSRuntimeException {
        String repositoryPath = getRepositoryPath(contentClass);
        Random random = new Random(System.currentTimeMillis());
        String destinationPath = repositoryPath + random.nextInt(getMaximumNumberOfDirectories());
        File destinationDir = new File(destinationPath);
        TemporaryFilesHandler.register(null, destinationDir);
        if (!destinationDir.isDirectory()) {
            if (!destinationDir.mkdirs()) {
                log.debug("The directory " + destinationDir + " could not be created. Retrying.");
                if (depth < getMaximumDirectoriesAttemps()) {
                    return getDestinationPath(contentClass, depth + 1);
                } else {
                    String errorMessage = "Unable to find a suitable directory to store the content";
                    log.error(errorMessage);
                    throw new CMSRuntimeException(errorMessage);
                }
            }
        }
        return destinationPath + "/";
    }

    private String getClassName(Class contentClass) {
        int lastDotIndex = contentClass.getName().lastIndexOf(".");
        if (lastDotIndex == -1) {
            return contentClass.getName();
        } else {
            return contentClass.getName().substring(contentClass.getName().lastIndexOf(".") + 1);
        }
    }

    /**
     * Return the file extension
     * @param fileName the file name
     * @return the file extension or an empty string
     */
    private String getFileExtension(String fileName) {
        int lastDotIndex = fileName.lastIndexOf(".");
        if (lastDotIndex == -1) {
            return "";
        } else {
            return fileName.substring(lastDotIndex + 1);
        }
    }
}
