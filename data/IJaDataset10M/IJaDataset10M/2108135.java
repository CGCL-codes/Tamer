package edu.fudan.cse.medlab.event.moresite;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import edu.fudan.cse.medlab.event.crawler.Client;

public class Google {

    public static URL[] SearchByKeyword(String[] keyword, int num) throws Exception {
        String s = URLEncoder.encode(keyword[0], "GBK");
        if (keyword.length != 1) for (int i = 1; i < keyword.length; i++) s += "+" + URLEncoder.encode(keyword[i], "GBK");
        String urlstring = "http://www.google.cn/search?num=" + num + "&hl=zh-CN&q=" + s;
        urlstring = urlstring.replace(" ", "+");
        urlstring = urlstring.replace("\t", "+");
        System.out.println(urlstring);
        String html = Client.getPage(urlstring, null, null);
        if (html == null) return null;
        return Google.getURL(html);
    }

    public static URL[] getURL(String s) {
        String urlstring = new String();
        URL[] urltemp = new URL[100];
        URL[] urls = null;
        int k = 0;
        String regex = "<a href=\"[^\"]*\" target=_blank class=l";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(s);
        ;
        try {
            while (k < 100 && m.find()) {
                String st = m.group();
                st = st.substring(st.indexOf("<a href"));
                st = st.substring(st.indexOf("href=\"") + 6);
                urlstring = st.substring(0, st.indexOf("\" "));
                if (urlstring.contains("/interstitial?url=")) urlstring = urlstring.substring(urlstring.indexOf("/interstitial?url=") + 18);
                try {
                    URL url = new URL(urlstring);
                    urltemp[k++] = url;
                } catch (MalformedURLException murle) {
                    murle.printStackTrace();
                }
            }
            if (k != 100) {
                urls = new URL[k];
                for (int i = 0; i < urls.length; i++) urls[i] = urltemp[i];
            } else urls = urltemp;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return urls;
        }
    }

    public static void main(String[] args) {
        String s1 = "\"2008?8?1\"";
        String s2 = "\"�Ϻ�\"";
        String[] s = { s1, s2 };
        try {
            URL[] urls = Google.SearchByKeyword(s, 100);
            for (int i = 0; i < urls.length; i++) {
                System.out.println(i + ":   " + urls[i]);
                System.out.println(urls[i].getHost());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
