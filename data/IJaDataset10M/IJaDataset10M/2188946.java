package de.eversync.sync;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.tmatesoft.sqljet.core.SqlJetException;
import de.eversync.localFileSystem.LocalFs;
import de.eversync.logging.Logger;
import de.eversync.netComm.NetComm;
import de.eversync.netComm.Request;
import de.eversync.netComm.WrongLoginException;
import de.eversync.remoteFs.RemoteFsLoader;
import de.eversync.remoteFs.RemoteFs;
import de.eversync.remoteFs.RemoteFsEntry;

public class SyncJob implements Runnable {

    private static final int cMODE_TO_SERVER = 0;

    private static final int cMODE_TO_CLIENT = 1;

    private NetComm mNetComm;

    private RemoteFsEntry mActRemoteFsEntry;

    private RemoteFs mRemoteFs;

    private LocalFs mLocalFs;

    private ExecutorService mExecuter;

    private String mRootFolder;

    public SyncJob(NetComm aNetComm, RemoteFs aRemoteFs, RemoteFsEntry aRemoteFsEntry, LocalFs aLocalFs, String aRootFolder) {
        this.setRootFolder(aRootFolder);
        this.setNetComm(aNetComm);
        this.setRemoteFs(aRemoteFs);
        this.setActRemoteFsEntry(aRemoteFsEntry);
        this.setLocalFs(aLocalFs);
        this.setExecuter(Executors.newCachedThreadPool());
        Logger.getInstance().add("Created Sync job" + this.getActRemoteFsEntry());
    }

    public SyncJob(NetComm aNetComm, LocalFs aLocalFs, String aRootFolder) {
        this(aNetComm, new RemoteFs(), null, aLocalFs, aRootFolder);
    }

    @Override
    public void run() {
        if (this.getRemoteFs().getEntries().size() == 0) {
            new RemoteFsLoader(this.getNetComm(), this.getRemoteFs(), this.getRootFolder() + "/").loadFs();
            if (this.getRemoteFs().getEntries().size() == 0) {
                return;
            }
            this.setActRemoteFsEntry(this.getRemoteFs().getRootEntry());
        }
        if (this.getActRemoteFsEntry() == null) {
            this.setActRemoteFsEntry(this.getRemoteFs().getRootEntry());
        }
        try {
            this.sync();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (SqlJetException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (WrongLoginException e) {
            e.printStackTrace();
        }
        this.getExecuter().shutdown();
        while (!this.getExecuter().isTerminated()) {
        }
    }

    private void sync() throws ClientProtocolException, IOException, InterruptedException, ParseException, SqlJetException, URISyntaxException, WrongLoginException {
        File localFile = new File(this.getLocalFs().getWatchPath() + this.getActRemoteFsEntry().getFullPath());
        if (this.getActRemoteFsEntry().getLastChange() > localFile.lastModified() / 1000) {
            Logger.getInstance().add("Standard Get " + this.getActRemoteFsEntry().getFullPath());
            switch(this.getActRemoteFsEntry().getType()) {
                case RemoteFsEntry.cTYPE_FILE:
                    if (localFile.lastModified() / 1000 > this.getLocalFs().getFileUpdateCacher().getLastUpdate(this.getActRemoteFsEntry().getFullPath()) / 1000) this.createFileBackup(new File(localFile.getPath()));
                    this.downloadFile();
                    this.getLocalFs().getFileUpdateCacher().setLastUpdate(this.getActRemoteFsEntry().getFullPath(), System.currentTimeMillis());
                    localFile.setLastModified(this.getActRemoteFsEntry().getLastChange() * 1000);
                    break;
                case RemoteFsEntry.cTYPE_FOLDER:
                    if (localFile.exists()) {
                        this.compareFolderEntries(localFile, SyncJob.cMODE_TO_CLIENT);
                    } else {
                        localFile.mkdir();
                        this.downloadFolder();
                        this.getLocalFs().getFileUpdateCacher().setLastUpdate(this.getActRemoteFsEntry().getFullPath(), System.currentTimeMillis());
                        localFile.setLastModified(this.getActRemoteFsEntry().getLastChange() * 1000);
                    }
                    break;
                default:
                    assert (false);
            }
        } else if (this.getActRemoteFsEntry().getLastChange() < localFile.lastModified() / 1000) {
            Logger.getInstance().add("Local newer " + this.getActRemoteFsEntry().getFullPath() + " LocalFile: " + localFile.getPath());
            switch(this.getActRemoteFsEntry().getType()) {
                case RemoteFsEntry.cTYPE_FILE:
                    if (this.getActRemoteFsEntry().getLastChange() < this.getLocalFs().getFileUpdateCacher().getLastUpdate(this.getActRemoteFsEntry().getFullPath()) / 1000) {
                        this.updateFile(localFile);
                    } else {
                        this.createFileBackup(localFile);
                        this.downloadFile();
                    }
                    this.getLocalFs().getFileUpdateCacher().setLastUpdate(this.getActRemoteFsEntry().getFullPath(), System.currentTimeMillis());
                    break;
                case RemoteFsEntry.cTYPE_FOLDER:
                    this.compareFolderEntries(localFile, SyncJob.cMODE_TO_SERVER);
                    this.updateFsEntry(localFile);
                    break;
                default:
                    assert (false);
            }
        } else {
            Logger.getInstance().add("Equal " + this.getActRemoteFsEntry().getFullPath());
            if (this.getActRemoteFsEntry().getType() == RemoteFsEntry.cTYPE_FOLDER) {
                if (localFile.exists()) {
                    Logger.getInstance().add("Compare local with remote entries");
                    this.compareFolderEntries(localFile, SyncJob.cMODE_TO_CLIENT);
                } else {
                    Logger.getInstance().add("Get Complete folder" + this.getActRemoteFsEntry().getFullPath());
                    localFile.mkdir();
                    this.downloadFolder();
                }
            }
            this.getLocalFs().getFileUpdateCacher().setLastUpdate(this.getActRemoteFsEntry().getFullPath(), System.currentTimeMillis());
        }
    }

    private void compareFolderEntries(File aFolder, int aMode) throws ClientProtocolException, IOException, SqlJetException, ParseException, URISyntaxException, WrongLoginException {
        File watchFolder = new File(this.getLocalFs().getWatchPath());
        ArrayList<RemoteFsEntry> subEntries = this.getRemoteFs().getSubEntries(this.getActRemoteFsEntry());
        if (subEntries == null) {
            Logger.getInstance().add("Sub-entries == null");
            return;
        }
        if (!aFolder.exists()) {
            Logger.getInstance().add("Folder doesn't exist");
            return;
        }
        for (File file : aFolder.listFiles()) {
            String path = file.getPath().replace(watchFolder.getName(), "");
            Logger.getInstance().add("Try to find remote:" + path);
            boolean found = false;
            for (RemoteFsEntry entry : subEntries) {
                if (entry.getFullPath().equals(path)) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                if (aMode == SyncJob.cMODE_TO_SERVER) {
                    this.addRemoteNewFsEntry(file);
                    if (file.isDirectory()) {
                        this.recAddFolder(file);
                    }
                } else {
                    if (file.isFile()) {
                        file.delete();
                    } else {
                        this.getLocalFs().recDeleteFolder(file);
                    }
                }
            } else {
                Logger.getInstance().add("Found");
            }
        }
        if (aMode == SyncJob.cMODE_TO_SERVER) {
            for (RemoteFsEntry entry : subEntries) {
                boolean exists = false;
                for (File file : aFolder.listFiles()) {
                    String path = file.getPath().replace(watchFolder.getName(), "");
                    if (entry.getFullPath().equals(path)) {
                        exists = true;
                        break;
                    }
                }
                if (!exists) {
                    Logger.getInstance().add("Dep: " + entry.getFullPath());
                    this.deleteRemoteFsEntry(entry);
                    entry.setDeleted(true);
                }
            }
        }
        for (RemoteFsEntry entry : subEntries) {
            if (!entry.isDeleted()) {
                this.getExecuter().execute(new SyncJob(this.getNetComm(), this.getRemoteFs(), entry, this.getLocalFs(), this.getRootFolder()));
            }
        }
    }

    private void recAddFolder(File aFolder) throws ClientProtocolException, IOException, SqlJetException, ParseException, URISyntaxException, WrongLoginException {
        Logger.getInstance().add("Rec add folder" + aFolder.getPath() + " Subentries: " + aFolder.listFiles().length);
        for (File file : aFolder.listFiles()) {
            Logger.getInstance().add("Found: " + file.getName());
            this.addRemoteNewFsEntry(file);
            if (file.isDirectory()) {
                this.recAddFolder(file);
            }
        }
    }

    private void downloadFile() throws ParseException, IOException, SqlJetException, URISyntaxException, WrongLoginException {
        Logger.getInstance().add("Download file " + this.getActRemoteFsEntry().getFullPath());
        ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("action", "getFile"));
        params.add(new BasicNameValuePair("path", this.getActRemoteFsEntry().getPath()));
        params.add(new BasicNameValuePair("name", this.getActRemoteFsEntry().getName()));
        String path = this.getLocalFs().getWatchPath() + this.getActRemoteFsEntry().getFullPath();
        this.getLocalFs().saveFile(path, this.doRequestByteArray(this.getNetComm().makeUrl(params)));
        Logger.getInstance().add("Path: " + path);
        new File(path).setLastModified(this.getActRemoteFsEntry().getLastChange());
        this.getLocalFs().getFileUpdateCacher().setLastUpdate(this.getActRemoteFsEntry().getFullPath(), System.currentTimeMillis());
    }

    private void createFileBackup(File aFile) throws ClientProtocolException, IOException, SqlJetException, ParseException, URISyntaxException, WrongLoginException {
        Logger.getInstance().add("Create backup from: " + aFile.getPath());
        long changeTime = aFile.lastModified();
        File backup = new File(aFile.getParent() + "/" + System.currentTimeMillis() + "_backup_of_" + aFile.getName());
        aFile.renameTo(backup);
        backup.setLastModified(changeTime);
        this.addRemoteNewFsEntry(backup);
    }

    private void addRemoteNewFsEntry(File aFile) throws ClientProtocolException, IOException, SqlJetException, ParseException, URISyntaxException, WrongLoginException {
        Logger.getInstance().add("Add new FsEntry: " + aFile.getPath());
        File watchFolder = new File(this.getLocalFs().getWatchPath());
        String path = aFile.getParent().replace(watchFolder.getName(), "");
        path += "/";
        String name = aFile.getName();
        int type = (aFile.isFile() ? RemoteFsEntry.cTYPE_FILE : RemoteFsEntry.cTYPE_FOLDER);
        if (type == RemoteFsEntry.cTYPE_FILE) {
            ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("action", "addFsEntry"));
            params.add(new BasicNameValuePair("path", path));
            params.add(new BasicNameValuePair("name", name));
            params.add(new BasicNameValuePair("type", "" + type));
            params.add(new BasicNameValuePair("last_change", "" + aFile.lastModified() / 1000));
            HttpPost httpPost = new HttpPost(this.getNetComm().makeUrl(params));
            MultipartEntity entity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
            FileBody fileBody = new FileBody(aFile, "text/plain");
            entity.addPart("file", fileBody);
            httpPost.setEntity(entity);
            Request req = new Request(this.getNetComm().getHttpClient());
            req.addRequest(httpPost);
            req.performRequest();
            httpPost.abort();
        } else {
            ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("action", "addFsEntry"));
            params.add(new BasicNameValuePair("path", path));
            params.add(new BasicNameValuePair("name", name));
            params.add(new BasicNameValuePair("type", "" + type));
            params.add(new BasicNameValuePair("last_change", "" + aFile.lastModified() / 1000));
            this.doRequestString(this.getNetComm().makeUrl(params));
        }
        if (aFile.isFile()) {
            this.getLocalFs().getFileUpdateCacher().setLastUpdate(path + aFile.getName(), System.currentTimeMillis());
        }
    }

    private void updateFile(File aFile) throws ClientProtocolException, IOException, URISyntaxException, WrongLoginException {
        Logger.getInstance().add("Update File:" + aFile);
        File watchFolder = new File(this.getLocalFs().getWatchPath());
        String path = aFile.getParent().replace(watchFolder.getName(), "");
        path += "/";
        String name = aFile.getName();
        ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("action", "updateFile"));
        params.add(new BasicNameValuePair("path", path));
        params.add(new BasicNameValuePair("name", name));
        params.add(new BasicNameValuePair("last_change", "" + aFile.lastModified() / 1000));
        HttpPost httpPost = new HttpPost(this.getNetComm().makeUrl(params));
        MultipartEntity entity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
        FileBody fileBody = new FileBody(aFile, "text/plain");
        entity.addPart("file", fileBody);
        httpPost.setEntity(entity);
        Request req = new Request(this.getNetComm().getHttpClient());
        req.addRequest(httpPost);
        HttpEntity answerEntity = req.performRequest();
        String respStr = EntityUtils.toString(answerEntity);
        if (respStr.length() > 0) {
            Logger.getInstance().add("Update msg: " + respStr);
        }
        httpPost.abort();
    }

    private void deleteRemoteFsEntry(RemoteFsEntry aEntry) throws ParseException, IOException, SqlJetException, URISyntaxException, WrongLoginException {
        Logger.getInstance().add("Delete fs entry " + aEntry.getFullPath());
        ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("action", "deleteFsEntry"));
        params.add(new BasicNameValuePair("path", aEntry.getPath()));
        params.add(new BasicNameValuePair("name", aEntry.getName()));
        this.doRequestString(this.getNetComm().makeUrl(params));
        if (aEntry.getType() == RemoteFsEntry.cTYPE_FILE) {
            this.getLocalFs().getFileUpdateCacher().deleteCacheEntry(this.getActRemoteFsEntry().getFullPath());
        }
    }

    private void updateFsEntry(File aFile) throws ClientProtocolException, IOException, ParseException, URISyntaxException, WrongLoginException {
        Logger.getInstance().add("Update fs entry  " + this.getActRemoteFsEntry().getFullPath());
        ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("action", "updateFsEntry"));
        params.add(new BasicNameValuePair("path", this.getActRemoteFsEntry().getPath()));
        params.add(new BasicNameValuePair("name", this.getActRemoteFsEntry().getName()));
        params.add(new BasicNameValuePair("last_change", "" + aFile.lastModified() / 1000));
        this.doRequestString(this.getNetComm().makeUrl(params));
    }

    private HttpEntity doRequest(HttpUriRequest reqUri) throws ParseException, IOException, WrongLoginException {
        Request req = new Request(this.getNetComm().getHttpClient());
        req.addRequest(reqUri);
        HttpEntity entity = req.performRequest();
        return entity;
    }

    private String doRequestString(URI aUri) throws ParseException, IOException, WrongLoginException {
        HttpUriRequest reqUri = new HttpGet(aUri);
        HttpEntity entity = this.doRequest(reqUri);
        String re = EntityUtils.toString(entity);
        reqUri.abort();
        return re;
    }

    private byte[] doRequestByteArray(URI aUri) throws ParseException, IOException, WrongLoginException {
        HttpUriRequest reqUri = new HttpGet(aUri);
        HttpEntity entity = this.doRequest(reqUri);
        byte[] reByte = EntityUtils.toByteArray(entity);
        reqUri.abort();
        return reByte;
    }

    private void downloadFolder() {
        ArrayList<RemoteFsEntry> subEntries = this.getRemoteFs().getSubEntries(this.getActRemoteFsEntry());
        ExecutorService exService = Executors.newCachedThreadPool();
        for (RemoteFsEntry entry : subEntries) {
            exService.execute(new Thread(new SyncJob(this.getNetComm(), this.getRemoteFs(), entry, this.getLocalFs(), this.getRootFolder())));
        }
        exService.shutdown();
        while (!exService.isTerminated()) {
        }
    }

    private void setExecuter(ExecutorService mExecuter) {
        this.mExecuter = mExecuter;
    }

    private ExecutorService getExecuter() {
        return mExecuter;
    }

    private void setNetComm(NetComm mNetComm) {
        this.mNetComm = mNetComm;
    }

    private NetComm getNetComm() {
        return mNetComm;
    }

    private void setRemoteFs(RemoteFs mRemoteFs) {
        this.mRemoteFs = mRemoteFs;
    }

    private RemoteFs getRemoteFs() {
        return mRemoteFs;
    }

    public RemoteFsEntry getActRemoteFsEntry() {
        return mActRemoteFsEntry;
    }

    private void setActRemoteFsEntry(RemoteFsEntry mActRemoteFsEntry) {
        this.mActRemoteFsEntry = mActRemoteFsEntry;
    }

    private void setLocalFs(LocalFs mLocalFs) {
        this.mLocalFs = mLocalFs;
    }

    private LocalFs getLocalFs() {
        return mLocalFs;
    }

    private void setRootFolder(String mRootFolder) {
        this.mRootFolder = mRootFolder;
    }

    private String getRootFolder() {
        return mRootFolder;
    }
}
