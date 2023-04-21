package net.sourceforge.websnaptool.hr.util;

import java.net.MalformedURLException;
import java.net.URL;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *  WebUtil:
 *  
 * User: Muwang Zheng
 * Date: 2010-10-5
 * Time: 下午08:10:09
 * 
 */
public class WebUtil {

    /**
	 * log
	 */
    private static final Log LOG = LogFactory.getLog(WebUtil.class);

    /**
	 * 获取url的域名
	 * @param url
	 * @return 域名
	 */
    public static String getDomain(String url) {
        if (isURL(url)) {
            try {
                URL u = new URL(url);
                String host = u.getHost();
                int pos = 0;
                int lastPos = 0;
                lastPos = host.lastIndexOf('.');
                pos = host.substring(0, lastPos).lastIndexOf('.');
                return host.substring(pos + 1);
            } catch (MalformedURLException e) {
                LOG.error(e.getMessage(), e);
            }
        }
        return null;
    }

    public static boolean isURL(String url) {
        if (null != url) {
            return url.startsWith("http");
        }
        return false;
    }

    /**
	 * @param args
	 */
    public static void main(String[] args) {
        System.out.println(getDomain("http://www.163.com"));
        System.out.println(getDomain("http://www.163.com/dd"));
        System.out.println(getDomain("http://www.163.com/dd?dd=d"));
        System.out.println(getDomain("http://www.163.com/"));
        System.out.println(getDomain("http://163.com/"));
        System.out.println(getDomain("http://1.2.1.32/"));
    }
}
