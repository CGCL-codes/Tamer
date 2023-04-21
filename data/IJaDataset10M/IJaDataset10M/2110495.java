package cn.myapps.core.shortmessage.runtime;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Http传输类,可以直接POST数据到指定URL,并获取服务器端返回字串.
 * @author Chris
 *
 */
public class HttpHandler {

    /**
	 * POST数据到指定URL,并获取服务器端返回字串.
	 * @param urlString URL字符串
	 * @param bytes byte数组,待传输数据字节
	 * @return 获取服务器端返回字串.
	 * @throws Exception
	 */
    public String post(String urlString, byte[] bytes) throws Exception {
        HttpURLConnection conn = null;
        OutputStream os = null;
        InputStream is = null;
        StringBuffer responseMessage = new StringBuffer();
        try {
            URL url = new URL(urlString);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setRequestProperty("charset", "UTF-8");
            conn.setRequestProperty("Content-Length", Integer.toString(bytes != null ? bytes.length : 0));
            conn.setDoOutput(true);
            conn.setDoInput(true);
            os = conn.getOutputStream();
            os.write(bytes);
            is = conn.getInputStream();
            bytes = new byte[256];
            int r = 0;
            while ((r = is.read(bytes)) != -1) {
                responseMessage.append(new String(bytes, 0, r, "UTF-8"));
            }
        } catch (Exception e) {
            throw e;
        } finally {
            if (os != null) os.close();
            if (is != null) is.close();
            if (conn != null) conn = null;
        }
        return responseMessage.toString();
    }
}
