package grobid.impl.sax;

import java.io.*;
import java.util.*;
import org.xml.sax.*;
import org.xml.sax.helpers.*;
import javax.xml.parsers.*;
import java.sql.*;
import javax.naming.*;
import javax.sql.*;
import grobid.impl.utilities.TextUtilities;

/**
 *  SAX parser for the TEI format for fulltext data. Normally all training data should be in this unique format. 
 *  The segmentation of tokens must be identical as the one from pdf2xml files so that
 *  training and online input tokens are aligned.  
 *  
 */
public class TEIEbookSaxParser extends DefaultHandler {

    private StringBuffer accumulator = null;

    private String output = null;

    private Stack<String> currentTags = null;

    private ArrayList<String> labeled = null;

    public TEIEbookSaxParser() {
        labeled = new ArrayList<String>();
        currentTags = new Stack<String>();
        accumulator = new StringBuffer();
    }

    public void characters(char[] buffer, int start, int length) {
        accumulator.append(buffer, start, length);
        System.out.println(accumulator.toString());
    }

    public String getText() {
        if (accumulator != null) {
            return accumulator.toString().trim();
        } else {
            return null;
        }
    }

    public ArrayList<String> getLabeledResult() {
        return labeled;
    }

    public void endElement(java.lang.String uri, java.lang.String localName, java.lang.String qName) throws SAXException {
        if ((!qName.equals("lb")) & (!qName.equals("pb"))) {
            writeData(qName, true);
        }
    }

    public void startElement(String namespaceURI, String localName, String qName, Attributes atts) throws SAXException {
        if (qName.equals("lb")) {
            accumulator.append(" +L+ ");
        } else if (qName.equals("pb")) {
            accumulator.append(" +PAGE+ ");
        } else {
            String text = getText();
            if (text != null) {
                if (text.length() > 0) {
                    writeData(qName, false);
                }
            }
            accumulator.setLength(0);
            if (qName.equals("header")) {
                currentTags.push("<header>");
            } else if (qName.equals("other")) {
                currentTags.push("<other>");
            } else if (qName.equals("page_header")) {
                currentTags.push("<page_header>");
            } else if (qName.equals("page_footnote")) {
                currentTags.push("<page_footnote>");
            } else if (qName.equals("page") | qName.equals("pages")) {
                currentTags.push("<page>");
            } else if (qName.equals("reference")) {
                currentTags.push("<reference>");
            } else if (qName.equals("toc")) {
                currentTags.push("<toc>");
            } else if (qName.equals("index")) {
                currentTags.push("<index>");
            } else if (qName.equals("section")) {
                currentTags.push("<section>");
            }
        }
    }

    private void writeData(String qName, boolean pop) {
        if ((qName.equals("header")) | (qName.equals("other")) | (qName.equals("page_header")) | (qName.equals("page_footnote")) | (qName.equals("page")) | (qName.equals("pages")) | (qName.equals("reference")) | (qName.equals("toc")) | (qName.equals("index")) | (qName.equals("section"))) {
            String currentTag = null;
            if (pop) {
                currentTag = currentTags.pop();
            } else {
                currentTag = currentTags.peek();
            }
            String text = getText();
            StringTokenizer st = new StringTokenizer(text, " \n\t" + TextUtilities.fullPunctuations, true);
            boolean begin = true;
            while (st.hasMoreTokens()) {
                String tok = st.nextToken().trim();
                if (tok.length() == 0) continue;
                if (tok.equals("+L+")) {
                    labeled.add("@newline\n");
                } else if (tok.equals("+PAGE+")) {
                    labeled.add("@newpage\n");
                } else {
                    String content = tok;
                    int i = 0;
                    if (content.length() > 0) {
                        if (begin) {
                            labeled.add(content + " I-" + currentTag + "\n");
                            begin = false;
                        } else {
                            labeled.add(content + " " + currentTag + "\n");
                        }
                    }
                }
                begin = false;
            }
            accumulator.setLength(0);
        }
    }
}
