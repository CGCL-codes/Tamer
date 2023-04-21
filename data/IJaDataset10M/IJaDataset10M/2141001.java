package org.jiopi.ibean.kernel.repository;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.log4j.Logger;
import org.jiopi.framework.core.Config;
import org.jiopi.ibean.kernel.util.FileUtil;
import org.jiopi.ibean.kernel.util.ResourceUtil;
import org.jiopi.ibean.share.ShareConstants;

/**
 * 
 * 远程文件管理器
 * 
 * 使用远程文件获取器获取远程文件,首先会检查本地Cache
 * 
 * 如果配置了每次使用时检查更新(ibean.autoupdate.interval=0)
 * 则在每次获取Cache前比对远程文件变更情况,如果有变更,则更新原文件
 * 如果更新失败则返回原文件
 * 
 * 特别注意:此管理器返回的本地缓存文件名不会与远程相同，以避免重名问题
 * 
 * @since iBean0.1 2010.4.25
 * @version 0.1
 *
 */
public class RemoteFileManager {

    private static Logger logger = Logger.getLogger(RemoteFileManager.class);

    private static boolean checkOnGet = "0".equals(Config.getJiopiProperties().getProperty(ShareConstants.IBEAN_AUTOUPDATE_INTERVAL, ShareConstants.IBEAN_AUTOUPDATE_INTERVAL_DEFAULT));

    /**
	 * 得到指定远程地址的本地缓存文件夹的文件名
	 * 
	 * @param remoteFileURL
	 * @param cacheDir
	 * @return
	 */
    public static File getRemoteCacheFile(URL remoteFileURL, File cacheDir) {
        String remoteFilePath = remoteFileURL.toString();
        String localFileName = "";
        localFileName = localFileName + FileUtil.getFileName(remoteFilePath, true);
        return new File(cacheDir, localFileName);
    }

    /**
	 * 获取远程文件的本地缓存
	 * 如果是本地文件,直接返回,而不使用cache
	 * 如果为远程地址,则cacheDir必须为一个 Directory
	 * 
	 * v0.1: 只支持file和http协议
	 * @param remoteFileURL
	 * @param cacheDir
	 * @return 本地文件对象,不保证一定存在
	 * @since 0.1
	 */
    public static File getRemoteFile(URL remoteFileURL, File cacheDir, UsernamePasswordCredentials creds) {
        if (remoteFileURL == null) return null;
        String protocol = remoteFileURL.getProtocol();
        if ("file".equals(protocol)) {
            return new File(remoteFileURL.getFile());
        } else if ("http".equals(protocol)) {
            if (cacheDir == null || !cacheDir.isDirectory()) return null;
            String remoteFilePath = remoteFileURL.toString();
            File localFile = getRemoteCacheFile(remoteFileURL, cacheDir);
            if (logger.isDebugEnabled()) logger.debug("get http file " + remoteFilePath + " local file is " + localFile.getAbsolutePath());
            boolean needDownload = false;
            if (localFile.isFile()) {
                if (checkOnGet) {
                    try {
                        needDownload = !ResourceUtil.isSameHttpContent(remoteFilePath, localFile, creds);
                        logger.info("check for update:" + remoteFilePath + " " + needDownload);
                    } catch (IOException e) {
                        needDownload = false;
                        logger.error(e);
                    }
                }
            } else {
                needDownload = true;
            }
            if (needDownload) {
                try {
                    logger.info("downloading... " + remoteFilePath);
                    ResourceUtil.copyHttpContent(remoteFilePath, localFile, creds);
                    logger.info("saved at " + localFile);
                } catch (IOException e) {
                    logger.error(e);
                }
            }
            return localFile;
        }
        return null;
    }
}
