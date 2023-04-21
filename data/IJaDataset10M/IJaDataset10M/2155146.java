package cn._2dland.uploader.xina;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;

/**
 * 上传监听线程
 * @author deadblue
 */
public class HandleThread extends Thread {

    private String HANDLE_URL = "http://vupload.you.video.sina.com.cn/cgi-bin/handle.cgi";

    /** 同步锁 */
    private UploadLock lock = null;

    private HttpClient client = null;

    private String hach = null;

    public HandleThread(UploadLock lock, HttpClient client, String hach) {
        this.lock = lock;
        this.hach = hach;
        this.client = new HttpClient(client.getParams());
    }

    public void run() {
        synchronized (lock) {
            lock.setDone(false);
            try {
                StringBuffer buf = new StringBuffer(HANDLE_URL).append("?hach=").append(hach).append("&random=").append(Math.random());
                GetMethod handleGet = new GetMethod(buf.toString());
                handleGet.addRequestHeader("Referer", "http://vupload.you.video.sina.com.cn/u.php?m=2");
                int respCode = client.executeMethod(handleGet);
                if (respCode != 200) throw new Exception("Http Error: " + respCode);
                InputStream is = handleGet.getResponseBodyAsStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(is));
                for (String line; (line = reader.readLine()) != null; ) {
                    if (!line.startsWith("<script language=\"javascript\">showOK('")) continue;
                    String result = line.substring(38, line.length() - 12);
                    lock.setResult(result);
                }
                is.close();
                handleGet.releaseConnection();
            } catch (Exception e) {
                e.printStackTrace();
            }
            lock.setDone(true);
            lock.notify();
        }
    }
}
