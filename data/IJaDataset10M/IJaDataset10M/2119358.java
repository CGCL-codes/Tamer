package org.grobid.core.sax;

import java.io.*;
import org.xml.sax.*;
import org.xml.sax.helpers.*;
import java.util.*;
import org.grobid.core.data.PatentItem;
import org.grobid.core.data.BibDataSet;
import org.grobid.core.utilities.TextUtilities;
import org.grobid.core.exceptions.GrobidException;

/**
 *  This SAX parser miror the input XML document, and add as extra annotation identified reference to 
 *  patent and NPL. The possible tags within the chunk are removed to avoid hierarchical invalid documents.
 * 
 *  @author Patrice Lopez
 */
public class PatentAnnotationSaxParser extends DefaultHandler {

    StringBuffer accumulator = new StringBuffer();

    private Writer writer = null;

    private int offset = -1;

    private boolean counting = false;

    private ArrayList<PatentItem> patents = null;

    private ArrayList<BibDataSet> articles = null;

    private int currentPatentIndex = 0;

    private int currentArticleIndex = 0;

    private static String delimiters = " \n\t" + TextUtilities.fullPunctuations;

    public PatentAnnotationSaxParser() {
    }

    public void characters(char[] buffer, int start, int length) {
        accumulator.append(buffer, start, length);
    }

    public void setWriter(Writer writer) {
        this.writer = writer;
    }

    public void setPatents(ArrayList<PatentItem> patents) {
        this.patents = patents;
    }

    public void setArticles(ArrayList<BibDataSet> articles) {
        this.articles = articles;
    }

    public String getText() {
        String text = accumulator.toString();
        if (text.trim().length() == 0) {
            return "";
        }
        text = text.replace("\n", " ");
        text = text.replace("  ", " ");
        if (counting) {
            StringTokenizer st = new StringTokenizer(text, delimiters, true);
            int count = 0;
            while (st.hasMoreTokens()) {
                String token = st.nextToken().trim();
                if (token.length() == 0) {
                    continue;
                }
                count++;
            }
            int i = currentPatentIndex;
            while (i < patents.size()) {
                PatentItem currentPatent = patents.get(i);
                if (currentPatent != null) {
                    int startOffset = currentPatent.getOffsetBegin();
                    int endOffset = currentPatent.getOffsetEnd();
                    if ((startOffset >= offset) && (startOffset <= offset + count) && (endOffset <= offset + count)) {
                        String context = currentPatent.getContext();
                        String target = "<ref type=\"patent\">" + context + "</ref>";
                        text = text.replace(context, target);
                        currentPatentIndex = i;
                    }
                }
                i++;
            }
            i = currentArticleIndex;
            while (i < articles.size()) {
                BibDataSet currentArticle = articles.get(i);
                if (currentArticle != null) {
                    List<Integer> offsets = currentArticle.getOffsets();
                    int startOffset = -1;
                    int endOffset = -1;
                    String context = currentArticle.getRawBib();
                    if (offsets.size() > 0) {
                        if (offsets.get(0) != null) {
                            startOffset = offsets.get(0).intValue();
                            StringTokenizer stt = new StringTokenizer(context, delimiters, true);
                            int count2 = 0;
                            while (stt.hasMoreTokens()) {
                                String token2 = stt.nextToken().trim();
                                if (token2.length() == 0) {
                                    continue;
                                }
                                count2++;
                            }
                            endOffset = startOffset + count2;
                        }
                    }
                    if ((startOffset >= offset) && (startOffset <= offset + count) && (endOffset <= offset + count)) {
                        String target = " <ref type=\"npl\">" + context + "</ref> ";
                        text = text.replace(context, target);
                        currentArticleIndex = i;
                    }
                }
                i++;
            }
            offset += count;
        }
        return text;
    }

    public void endElement(java.lang.String uri, java.lang.String localName, java.lang.String qName) throws SAXException {
        try {
            if (qName.equals("p") || qName.equals("description")) {
                writer.write(getText());
                accumulator.setLength(0);
            }
            if (qName.equals("description")) {
                counting = false;
            }
            if (!counting) {
                writer.write(getText());
                accumulator.setLength(0);
                writer.write("</" + qName + ">\n");
            } else {
                if (qName.equals("row")) {
                    accumulator.append(" ");
                }
                if (qName.equals("p")) {
                    writer.write("\n");
                    accumulator.append(" ");
                }
            }
        } catch (Exception e) {
            throw new GrobidException("An exception occured while running Grobid.", e);
        }
    }

    public void startElement(String namespaceURI, String localName, String qName, Attributes atts) throws SAXException {
        try {
            if (!counting) {
                writer.write(getText());
                accumulator.setLength(0);
            }
            if (!counting) {
                writer.write("<" + qName);
                int length = atts.getLength();
                for (int i = 0; i < length; i++) {
                    String name = atts.getQName(i);
                    String value = atts.getValue(i);
                    if ((name != null) && (value != null)) {
                        writer.write(" " + name + "=\"" + value + "\"");
                    }
                }
                writer.write(">");
            }
            if (qName.equals("description")) {
                offset = 0;
                counting = true;
            } else if (qName.equals("patent-document")) {
                counting = false;
            }
        } catch (Exception e) {
            throw new GrobidException("An exception occured while running Grobid.", e);
        }
    }
}
