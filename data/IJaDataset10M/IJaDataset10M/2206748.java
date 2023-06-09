package com.limegroup.gnutella.downloader.serial;

import java.io.File;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.limewire.collection.Range;
import com.limegroup.gnutella.URN;
import com.limegroup.gnutella.downloader.DownloaderType;

public class InNetworkDownloadMementoImpl implements InNetworkDownloadMemento, Serializable {

    private static final long serialVersionUID = -8013513174476895995L;

    private Map<String, Object> serialObjects = new HashMap<String, Object>();

    public int getDownloadAttempts() {
        Integer i = (Integer) serialObjects.get("downloadAttempts");
        if (i == null) return 0; else return i;
    }

    public long getStartTime() {
        Long l = (Long) serialObjects.get("startTime");
        if (l == null) return 0; else return l;
    }

    public String getTigerTreeRoot() {
        return (String) serialObjects.get("tigerTreeRoot");
    }

    public void setDownloadAttempts(int downloadAttempts) {
        serialObjects.put("downloadAttempts", downloadAttempts);
    }

    public void setStartTime(long startTime) {
        serialObjects.put("startTime", startTime);
    }

    public void setTigerTreeRoot(String tigerTreeRoot) {
        serialObjects.put("tigerTreeRoot", tigerTreeRoot);
    }

    @SuppressWarnings("unchecked")
    public Map<String, Object> getAttributes() {
        return (Map<String, Object>) serialObjects.get("attributes");
    }

    public long getContentLength() {
        Long l = (Long) serialObjects.get("contentLength");
        if (l == null) return -1; else return l;
    }

    public String getDefaultFileName() {
        return (String) serialObjects.get("defaultFileName");
    }

    public DownloaderType getDownloadType() {
        return (DownloaderType) serialObjects.get("downloadType");
    }

    public File getIncompleteFile() {
        return (File) serialObjects.get("incompleteFile");
    }

    @SuppressWarnings("unchecked")
    public Set<RemoteHostMemento> getRemoteHosts() {
        return (Set<RemoteHostMemento>) serialObjects.get("remoteHosts");
    }

    public File getSaveFile() {
        return (File) serialObjects.get("saveFile");
    }

    public URN getSha1Urn() {
        return (URN) serialObjects.get("sha1Urn");
    }

    public void setAttributes(Map<String, Object> attributes) {
        serialObjects.put("attributes", attributes);
    }

    public void setContentLength(long contentLength) {
        serialObjects.put("contentLength", contentLength);
    }

    public void setDefaultFileName(String defaultFileName) {
        serialObjects.put("defaultFileName", defaultFileName);
    }

    public void setDownloadType(DownloaderType downloaderType) {
        serialObjects.put("downloadType", downloaderType);
    }

    public void setIncompleteFile(File incompleteFile) {
        serialObjects.put("incompleteFile", incompleteFile);
    }

    public void setRemoteHosts(Set<RemoteHostMemento> remoteHosts) {
        serialObjects.put("remoteHosts", remoteHosts);
    }

    @SuppressWarnings("unchecked")
    public List<Range> getSavedBlocks() {
        return (List<Range>) serialObjects.get("savedBlocks");
    }

    public void setSavedBlocks(List<Range> serializableBlocks) {
        serialObjects.put("savedBlocks", serializableBlocks);
    }

    public void setSaveFile(File saveFile) {
        serialObjects.put("saveFile", saveFile);
    }

    public void setSha1Urn(URN sha1Urn) {
        serialObjects.put("sha1Urn", sha1Urn);
    }
}
