package howbuy.android.util;

import howbuy.android.palmfund.application.ApplicationParams;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.utils.URLEncodedUtils;
import android.app.Activity;
import android.app.Service;
import android.content.Context;

/**
 * ��������ַ�
 * 
 * @author yescpu
 * 
 */
public class UrlMatchUtil {

    public static final String basePath = "http://192.168.106.201:8082/hws/";

    public static String urlUtil(String path, Map<String, String> params, Context context) throws Exception {
        StringBuilder builder = new StringBuilder(basePath + path + "?");
        ApplicationParams aParams = (ApplicationParams) context.getApplicationContext();
        return builder.append(getParams(aParams.getPubNetParams()).append("&").append(getParams(params))).toString();
    }

    private static StringBuilder getParams(Map<String, String> map) throws Exception {
        StringBuilder pathBuilder = new StringBuilder();
        for (Map.Entry<String, String> entry : map.entrySet()) {
            pathBuilder.append(URLEncoder.encode(entry.getKey(), "UTF-8")).append('=').append((URLEncoder.encode(entry.getValue(), "UTF-8"))).append("&");
        }
        pathBuilder.deleteCharAt(pathBuilder.length() - 1);
        return pathBuilder;
    }
}
