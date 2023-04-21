package com.wojiao.htmlparser;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class JsoupTest {

    /**
	 * @param args
	 */
    public static void main(String[] args) throws IOException {
        Document doc = Jsoup.parse(new File("D:\\code1\\lucene\\src\\com\\wojiao\\htmlparser\\1.html"), "UTF-8", "D:\\code1\\lucene\\src\\com\\wojiao\\htmlparser\\1.html");
        Elements eles = doc.select("meta[http-equiv=Content-Type]");
        Iterator<Element> itor = eles.iterator();
        while (itor.hasNext()) {
            System.out.println(itor.next().text());
        }
    }
}
