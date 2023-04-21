package com.vayoodoot.cache;

import com.vayoodoot.file.VDFile;
import com.vayoodoot.file.FileReceiver;
import com.vayoodoot.message.DirectoryItem;
import com.vayoodoot.util.StringUtil;
import com.vayoodoot.ui.explorer.Message2UIAdapter;
import com.vayoodoot.partner.Buddy;
import com.vayoodoot.properties.VDProperties;
import com.vayoodoot.local.LocalManager;
import com.vayoodoot.local.UserLocalSettings;
import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.Date;
import org.apache.log4j.Logger;

/**
 * Created by IntelliJ IDEA.
 * User: Sachin Shetty
 * Date: Jul 13, 2007
 * Time: 8:50:32 PM
 * To change this template use File | Settings | File Templates.
 */
public class CacheManager {

    private static String cacheLocation = UserLocalSettings.getCacheBasePath();

    private static Logger logger = Logger.getLogger(CacheManager.class);

    private static ExecutorService threadPool = Executors.newFixedThreadPool(1);

    public static void processVDCacheFileItem(final Message2UIAdapter uiAdapter, final Buddy buddy, final DirectoryItem item) {
        File f1 = getFileFromCache(buddy.getBuddyName(), item.getFullPath());
        if (f1 != null && f1.exists()) {
            Date lastModified = new Date(f1.lastModified());
            logger.info("Comparing: " + lastModified + ":" + item.getLastModified());
            if (lastModified.compareTo(StringUtil.getDateFromString(item.getLastModified())) < 0) {
                logger.info(("Cache file ie newer that what I have, need to download"));
                threadPool.execute(new Runnable() {

                    public void run() {
                        try {
                            uiAdapter.getFile(buddy, getLocalFilePath(buddy.getBuddyName(), item.getFullPath()), item.getFullPath());
                        } catch (Exception e) {
                            logger.fatal("Could not get the new file:" + item.getFullPath() + e, e);
                        }
                    }
                });
            } else {
                logger.info("My file is newer, skipping: " + item.getFullPath());
            }
        } else {
            logger.info("getting file for the first time: " + item.getFullPath());
            threadPool.execute(new Runnable() {

                public void run() {
                    try {
                        uiAdapter.getFile(buddy, getLocalFilePath(buddy.getBuddyName(), item.getFullPath()), item.getFullPath());
                    } catch (Exception e) {
                        logger.fatal("Could not get the new file:" + item.getFullPath() + e, e);
                    }
                }
            });
        }
    }

    public static File getFileFromCache(String userName, String remoteFileName) {
        String actualFile = getTranslatedLocalPath(userName, remoteFileName);
        File f1 = new File(actualFile);
        if (f1.exists()) {
            return f1;
        } else {
            return null;
        }
    }

    public static String getLocalFilePath(String userName, String remoteFileName) {
        return getTranslatedLocalPath(userName, remoteFileName);
    }

    private static String getTranslatedLocalPath(String userName, String remoteFileName) {
        logger.info("Get Tranlated path for:" + userName + ":" + remoteFileName);
        String actualFile = cacheLocation + VDFile.LOCAL_FILE_SEPARATOR + userName + VDFile.LOCAL_FILE_SEPARATOR + remoteFileName.replace(VDFile.VD_FILE_SEPARATOR, VDFile.LOCAL_FILE_SEPARATOR);
        return actualFile;
    }
}
