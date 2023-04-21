package com.cntinker.util;

import java.io.FileNotFoundException;
import java.io.IOException;
import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.filters.AndFilter;
import org.htmlparser.filters.HasAttributeFilter;
import org.htmlparser.filters.TagNameFilter;
import org.htmlparser.tags.Div;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;

/**
 * @autohr: bin_liu
 *
 */
public class HtmlParse {

    private String url;

    private Parser parser;

    public HtmlParse(String url, String encoding) throws FileNotFoundException, IOException, ParserException {
        this.url = url;
        this.parser = new Parser(url);
        this.parser.setEncoding(encoding);
    }

    public HtmlParse(String stringInput) throws ParserException {
        this.parser = new Parser(stringInput);
    }

    /**
     * �õ��ڵ����
     * 
     * @param nodeFilter
     * @return NodeList
     * @throws ParserException
     */
    public NodeList getNodeList(NodeFilter nodeFilter) throws ParserException {
        this.parser.reset();
        NodeList nList = (NodeList) parser.parse(nodeFilter);
        return nList;
    }

    /**
     * �õ�DIV��HTML
     * 
     * @param nodeFilter
     * @return String
     * @throws ParserException
     */
    public String getDivHtml(NodeFilter nodeFilter) throws ParserException {
        NodeList nList = getNodeList(nodeFilter);
        StringBuffer str = new StringBuffer();
        for (int i = 0; i < nList.size(); i++) {
            Div div = (Div) nList.elementAt(i);
            str.append(div.toHtml());
        }
        return str.toString();
    }

    public static void main(String[] args) throws Exception {
        HtmlParse f = new HtmlParse("http://www.wenming.cn", "UTF-8");
        NodeFilter nFilter = new AndFilter(new TagNameFilter("div"), new HasAttributeFilter("id", "main_6th"));
        NodeList nList = f.getNodeList(nFilter);
        for (int i = 0; i < nList.size(); i++) {
            Div div = (Div) nList.elementAt(i);
            System.out.println(div.toHtml());
        }
        System.out.println("======================");
    }
}
