package com.jspx.test.jtxml;

import com.jspx.jtxml.tag.AElement;
import com.jspx.jtxml.impl.TxElement;
import com.jspx.jtxml.impl.TxEngineImpl;
import com.jspx.jtxml.TxmlEngine;
import com.jspx.io.AutoReadTextFile;
import com.jspx.io.AbstractRead;
import com.jspx.txweb.tag.ActionElement;
import com.jspx.txweb.support.ActionSupport;
import com.jspx.txweb.util.TXWebUtil;
import com.jspx.utils.StringUtil;
import com.jspx.utils.BeanUtil;
import com.jspx.sioc.tag.BeanElement;
import java.util.List;
import java.io.IOException;
import org.htmlparser.Parser;
import org.htmlparser.tags.LinkTag;
import org.htmlparser.filters.NodeClassFilter;
import org.htmlparser.util.NodeList;

/**
 * Created by IntelliJ IDEA.
 * User:chenYuan (mail:cayurain@21cn.com)
 * Date: 2006-12-31
 * Time: 21:05:04
 *
 */
public class Testxml {

    public static String testTsingleEngine() throws Exception {
        AbstractRead abstractRead = new AutoReadTextFile();
        abstractRead.setFile("D:\\jcmsstd\\webapps\\jcms\\test.xml");
        String xx = abstractRead.getContent();
        long start = System.currentTimeMillis();
        TxEngineImpl te = new TxEngineImpl();
        te.setStringValue(xx);
        te.setElementFilter(new AElement());
        te.getFilterString();
        List<TxElement> resultLists = te.getResultList();
        for (TxElement el : resultLists) {
            AElement ae = (AElement) el;
            System.out.println(ae.getElementString());
        }
        return resultLists.size() + "-----testTsingleEngine-----" + (System.currentTimeMillis() - start);
    }

    public static String testHtmlparser() throws Exception {
        AbstractRead abstractRead = new AutoReadTextFile();
        abstractRead.setFile("D:\\jcmsstd\\webapps\\jcms\\test.xml");
        String xx = abstractRead.getContent();
        long start = System.currentTimeMillis();
        Parser parser = new org.htmlparser.Parser();
        parser.setInputHTML(xx);
        NodeList nodeList = parser.extractAllNodesThatMatch(new NodeClassFilter(LinkTag.class));
        for (int i = 0; i < nodeList.size(); i++) {
            LinkTag node = (LinkTag) nodeList.elementAt(i);
            System.out.println(node.toHtml());
        }
        return nodeList.size() + "----testHtmlparser------" + (System.currentTimeMillis() - start);
    }

    public static String testActionParser() throws Exception {
        AbstractRead ar = new AutoReadTextFile();
        ar.setEncode("UTF-8");
        ar.setFile("r:\\jspx.net.xml");
        TxmlEngine xmltEngine = new TxEngineImpl();
        xmltEngine.setStringValue(ar.getContent());
        xmltEngine.setElementFilter(new BeanElement());
        xmltEngine.setCurrentPage(2);
        xmltEngine.setTotalCount(2);
        xmltEngine.getFilterString();
        List<TxElement> lists = xmltEngine.getResultList();
        for (TxElement xte : lists) {
            BeanElement actionElement = (BeanElement) xte;
            System.out.println("------------actionElement=" + actionElement.getElementString());
        }
        return "";
    }

    public static void main(String args[]) throws Exception {
        System.out.println(testActionParser());
    }
}
