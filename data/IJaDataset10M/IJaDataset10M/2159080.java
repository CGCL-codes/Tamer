package com.jspx.network.download.impl;

import com.jspx.network.download.HttpDownloadThread;
import com.jspx.core.sign.DownStateType;
import com.jspx.utils.NumberUtil;
import com.jspx.utils.FileUtil;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;
import java.net.URL;
import java.net.HttpURLConnection;
import java.io.*;
import java.util.Date;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Created by IntelliJ IDEA.
 * User:chenYuan (mail:cayurain@21cn.com)
 * Date: 2006-8-4
 * Time: 21:47:32
 */
public class MultiThreadImpl extends Thread implements HttpDownloadThread {

    private static final Log log = LogFactory.getLog(MultiThreadImpl.class);

    private SiteInfoBean siteInfoBean = null;

    private long[] nStartPos;

    private long[] nEndPos;

    private long[] nCompleted;

    private long downLoadFileSize = 0;

    private int stateType = DownStateType.WAITING;

    private File saveFile;

    private String downStateId = "";

    private int splitter = 1;

    private URL url;

    private Date createDate = new Date();

    public MultiThreadImpl() {
    }

    public String getDownStateId() {
        return downStateId;
    }

    public void setDownStateId(String downStateId) {
        this.downStateId = downStateId;
    }

    public int getSplitter() {
        return splitter;
    }

    public void setSplitter(int splitter) {
        this.splitter = splitter;
    }

    public File getSaveFile() {
        return saveFile;
    }

    public void setSaveFile(File saveFile) {
        this.saveFile = saveFile;
    }

    public URL getURL() {
        return url;
    }

    public void setURL(URL url) {
        this.url = url;
    }

    public void setBufferSize(int size) {
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void startDownload() {
        if (saveFile == null) {
            stateType = DownStateType.ERROR;
            setQuit(true);
            return;
        }
        if (url == null) {
            stateType = DownStateType.ERROR;
            setQuit(true);
            return;
        }
        if (!FileUtil.makeDirectory(saveFile.getParent())) {
            stateType = DownStateType.ERROR;
            setQuit(true);
            return;
        }
        stateType = DownStateType.INITIALIZE;
        siteInfoBean = new SiteInfoBean();
        siteInfoBean.setUrl(url);
        siteInfoBean.setTempFilePath(saveFile.getParent());
        siteInfoBean.setSaveFile(saveFile.getName());
        siteInfoBean.setSplitter(splitter);
        tmpFile = new File(siteInfoBean.getTempFilePath() + File.separator + siteInfoBean.getSaveFile() + ".info");
        if (tmpFile.exists()) {
            bFirst = false;
            readPos();
        } else {
            nStartPos = new long[siteInfoBean.getSplitter()];
            nEndPos = new long[siteInfoBean.getSplitter()];
            nCompleted = new long[siteInfoBean.getSplitter()];
        }
        start();
    }

    public int getStateType() {
        if (nStartPos == null) return stateType;
        if (DownStateType.FINISH == stateType) return DownStateType.FINISH;
        if (DownStateType.INITIALIZE == stateType) return DownStateType.INITIALIZE;
        for (int i = 0; i < nStartPos.length; i++) {
            if (DownStateType.INITIALIZE == fileSplitterFetch[i].getStateType()) return DownStateType.INITIALIZE;
            if (DownStateType.WAITING == fileSplitterFetch[i].getStateType()) return DownStateType.WAITING;
            if (DownStateType.ERROR == fileSplitterFetch[i].getStateType()) return DownStateType.ERROR;
            if (DownStateType.DOWNLOADING == fileSplitterFetch[i].getStateType()) return DownStateType.DOWNLOADING;
            if (DownStateType.PAUSE == fileSplitterFetch[i].getStateType()) return DownStateType.PAUSE;
        }
        return stateType;
    }

    public void setStateType(int stateType) {
        for (int i = 0; i < nStartPos.length; i++) fileSplitterFetch[i].setStateType(stateType);
    }

    private FileSplitterFetch[] fileSplitterFetch;

    private boolean bFirst = true;

    private boolean flagQuit = false;

    private File tmpFile;

    public void run() {
        downLoadFileSize = getFileSize();
        if (downLoadFileSize < 0) {
            stateType = DownStateType.ERROR;
            setQuit(true);
            return;
        }
        if (bFirst) {
            for (int i = 0; i < nStartPos.length; i++) {
                nStartPos[i] = (i * (downLoadFileSize / nStartPos.length));
            }
            System.arraycopy(nStartPos, 1, nEndPos, 0, nEndPos.length - 1);
            nEndPos[nEndPos.length - 1] = downLoadFileSize;
        }
        stateType = DownStateType.DOWNLOADING;
        try {
            fileSplitterFetch = new FileSplitterFetch[nStartPos.length];
            for (int i = 0; i < nStartPos.length; i++) {
                fileSplitterFetch[i] = new FileSplitterFetch(siteInfoBean.getUrl(), siteInfoBean.getTempFilePath() + File.separator + siteInfoBean.getSaveFile(), nStartPos[i], nEndPos[i], i);
                fileSplitterFetch[i].setCompleted(nCompleted[i]);
                log.debug("Thread " + i + " , nStartPos = " + nStartPos[i] + ", nEndPos = " + nEndPos[i]);
                fileSplitterFetch[i].start();
            }
            boolean breakWhile;
            while (!flagQuit && !interrupted()) {
                write_nPos();
                try {
                    sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                breakWhile = true;
                for (int i = 0; i < nStartPos.length; i++) {
                    if (!fileSplitterFetch[i].isbDownOver()) {
                        breakWhile = false;
                        break;
                    }
                }
                if (breakWhile) break;
            }
            stateType = DownStateType.FINISH;
            if (!isInterrupted()) interrupt();
        } catch (Exception e) {
            stateType = DownStateType.ERROR;
            e.printStackTrace();
        } finally {
            setQuit(true);
        }
    }

    private long getFileSize() {
        if (url == null) return -1;
        int nFileLength = -1;
        try {
            HttpURLConnection uc = (HttpURLConnection) url.openConnection();
            uc.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.00; Windows 98)");
            uc.setRequestProperty("JCache-Controlt", "no-cache");
            if (uc instanceof HttpsURLConnection) {
                HttpsURLConnection httpsConnection = (HttpsURLConnection) uc;
                httpsConnection.setHostnameVerifier(new HostnameVerifier() {

                    public boolean verify(String host, SSLSession session) {
                        return true;
                    }
                });
            }
            int responseCode = uc.getResponseCode();
            if (responseCode >= 400) {
                return -2;
            }
            String sHeader;
            for (int i = 1; ; i++) {
                sHeader = uc.getHeaderFieldKey(i);
                if (sHeader != null) {
                    if (sHeader.equals("Content-Length")) {
                        nFileLength = Integer.parseInt(uc.getHeaderField(sHeader));
                        break;
                    }
                } else break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return nFileLength;
    }

    private void write_nPos() {
        try {
            DataOutputStream output = new DataOutputStream(new FileOutputStream(tmpFile));
            output.writeInt(nStartPos.length);
            for (int i = 0; i < nStartPos.length; i++) {
                output.writeLong(fileSplitterFetch[i].getStartPos());
                output.writeLong(fileSplitterFetch[i].getEndPos());
                output.writeLong(fileSplitterFetch[i].getCompleted());
            }
            output.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void readPos() {
        try {
            DataInputStream input = new DataInputStream(new FileInputStream(tmpFile));
            int nCount = input.readInt();
            nStartPos = new long[nCount];
            nEndPos = new long[nCount];
            nCompleted = new long[nCount];
            for (int i = 0; i < nStartPos.length; i++) {
                nStartPos[i] = input.readLong();
                nEndPos[i] = input.readLong();
                nCompleted[i] = input.readLong();
            }
            input.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean getQuit() {
        return flagQuit;
    }

    public void setQuit(boolean flagQuit) {
        this.flagQuit = flagQuit;
        if (flagQuit) {
            for (int i = 0; i < nStartPos.length; i++) fileSplitterFetch[i].splitterStop();
            if (DownStateType.FINISH == stateType) tmpFile.deleteOnExit();
        }
    }

    public long getCompleted() {
        long completed = 0;
        for (int i = 0; i < nStartPos.length; i++) completed = completed + fileSplitterFetch[i].getCompleted();
        return completed;
    }

    public String getPercent() {
        if (downLoadFileSize <= 0) return "00%";
        return NumberUtil.mul(NumberUtil.div((double) getCompleted(), (double) downLoadFileSize, 2).doubleValue(), 100) + "%";
    }
}
