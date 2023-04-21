package com.dotmarketing.webdav;

import com.bradmcevoy.http.Resource;
import com.bradmcevoy.http.ResourceFactory;
import com.dotmarketing.cache.HostCache;
import com.dotmarketing.portlets.files.model.File;
import com.dotmarketing.portlets.folders.model.Folder;
import com.dotmarketing.util.Logger;

/**
 * @author Jason Tesser
 *
 */
public class ResourceFactorytImpl implements ResourceFactory {

    private DotWebdavHelper dotDavHelper;

    private static final String AUTOPUB_PATH = "/webdav/autopub";

    private static final String NONPUB_PATH = "/webdav/nonpub";

    public ResourceFactorytImpl() {
        super();
        dotDavHelper = new DotWebdavHelper();
    }

    public Resource getResource(String host, String url) {
        Logger.debug(this, "WebDav ResourceFactory: Host is " + host + " and the url is " + url);
        try {
            boolean isFolder = false;
            boolean isResource = false;
            boolean isWebDavRoot = url.equals(AUTOPUB_PATH) || url.equals(NONPUB_PATH) || url.equals(AUTOPUB_PATH + "/") || url.equals(NONPUB_PATH + "/");
            boolean autoPub = url.startsWith(AUTOPUB_PATH);
            boolean nonPub = url.startsWith(NONPUB_PATH);
            if (isWebDavRoot) {
                WebdavRootResourceImpl wr = new WebdavRootResourceImpl();
                return wr;
            } else {
                String actualPath;
                if (autoPub) {
                    actualPath = url.replaceAll(AUTOPUB_PATH, "");
                    if (actualPath.startsWith("/")) {
                        actualPath = actualPath.substring(1);
                    }
                } else if (nonPub) {
                    actualPath = url.replaceAll(NONPUB_PATH, "");
                    if (actualPath.startsWith("/")) {
                        actualPath = actualPath.substring(1);
                    }
                } else {
                    return null;
                }
                String[] splitPath = actualPath.split("/");
                if (splitPath != null && splitPath.length == 1) {
                    HostResourceImpl hr = new HostResourceImpl(url, HostCache.getFromCache(splitPath[0]));
                    return hr;
                }
            }
            if (dotDavHelper.isTempResource(url)) {
                java.io.File tempFile = dotDavHelper.loadTempFile(url);
                if (tempFile == null || !tempFile.exists()) {
                    return null;
                } else if (tempFile.isDirectory()) {
                    TempFolderResourceImpl tr = new TempFolderResourceImpl(url, tempFile, dotDavHelper.isAutoPub(url));
                    return tr;
                } else {
                    TempFileResourceImpl tr = new TempFileResourceImpl(tempFile, url, dotDavHelper.isAutoPub(url));
                    return tr;
                }
            }
            if (dotDavHelper.isResource(url)) {
                isResource = true;
            } else if (dotDavHelper.isFolder(url)) {
                isFolder = true;
            }
            if (!isFolder && !isResource) {
                return null;
            }
            if (isResource) {
                File file = dotDavHelper.loadFile(url);
                if (file == null || file.getInode() < 1) {
                    Logger.debug(this, "The file for url " + url + " returned null or not in db");
                    return null;
                }
                FileResourceImpl fr = new FileResourceImpl(file, url);
                return fr;
            } else {
                Folder folder = dotDavHelper.loadFolder(url);
                if (folder == null || folder.getInode() < 1) {
                    Logger.debug(this, "The folder for url " + url + " returned null or not in db");
                    return null;
                }
                FolderResourceImpl fr = new FolderResourceImpl(folder, url);
                return fr;
            }
        } catch (Exception e) {
            Logger.error(this, e.getMessage(), e);
            return null;
        }
    }

    public String getSupportedLevels() {
        return "1,2";
    }
}
