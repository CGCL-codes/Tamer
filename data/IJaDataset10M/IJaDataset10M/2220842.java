package com.oreilly.javaxslt.util;

import java.io.*;
import java.net.URL;
import org.xml.sax.*;
import org.xml.sax.helpers.*;

/**
 * A utility class that parses a Comma Separated Values (CSV) file
 * and outputs its contents using SAX2 events. The format of CSV that
 * this class reads is identical to the export format for Microsoft
 * Excel. For simple values, the CSV file may look like this:
 * <pre>
 * a,b,c
 * d,e,f
 * </pre>
 * Quotes are used as delimiters when the values contain commas:
 * <pre>
 * a,"b,c",d
 * e,"f,g","h,i"
 * </pre>
 * And double quotes are used when the values contain quotes. This parser
 * is smart enough to trim spaces around commas, as well.
 *
 * @author Eric M. Burke
 */
public class CSVXMLReader extends AbstractXMLReader {

    private static final Attributes EMPTY_ATTR = new AttributesImpl();

    /**
     * Parse a CSV file. SAX events are delivered to the ContentHandler
     * that was registered via <code>setContentHandler</code>.
     *
     * @param input the comma separated values file to parse.
     */
    public void parse(InputSource input) throws IOException, SAXException {
        ContentHandler ch = getContentHandler();
        if (ch == null) {
            return;
        }
        BufferedReader br = null;
        if (input.getCharacterStream() != null) {
            br = new BufferedReader(input.getCharacterStream());
        } else if (input.getByteStream() != null) {
            br = new BufferedReader(new InputStreamReader(input.getByteStream()));
        } else if (input.getSystemId() != null) {
            java.net.URL url = new URL(input.getSystemId());
            br = new BufferedReader(new InputStreamReader(url.openStream()));
        } else {
            throw new SAXException("Invalid InputSource object");
        }
        ch.startDocument();
        ch.startElement("", "", "csvFile", EMPTY_ATTR);
        String curLine = null;
        while ((curLine = br.readLine()) != null) {
            curLine = curLine.trim();
            if (curLine.length() > 0) {
                ch.startElement("", "", "line", EMPTY_ATTR);
                parseLine(curLine, ch);
                ch.endElement("", "", "line");
            }
        }
        ch.endElement("", "", "csvFile");
        ch.endDocument();
    }

    private void parseLine(String curLine, ContentHandler ch) throws IOException, SAXException {
        String firstToken = null;
        String remainderOfLine = null;
        int commaIndex = locateFirstDelimiter(curLine);
        if (commaIndex > -1) {
            firstToken = curLine.substring(0, commaIndex).trim();
            remainderOfLine = curLine.substring(commaIndex + 1).trim();
        } else {
            firstToken = curLine;
        }
        firstToken = cleanupQuotes(firstToken);
        ch.startElement("", "", "value", EMPTY_ATTR);
        ch.characters(firstToken.toCharArray(), 0, firstToken.length());
        ch.endElement("", "", "value");
        if (remainderOfLine != null) {
            parseLine(remainderOfLine, ch);
        }
    }

    private int locateFirstDelimiter(String curLine) {
        if (curLine.startsWith("\"")) {
            boolean inQuote = true;
            int numChars = curLine.length();
            for (int i = 1; i < numChars; i++) {
                char curChar = curLine.charAt(i);
                if (curChar == '"') {
                    inQuote = !inQuote;
                } else if (curChar == ',' && !inQuote) {
                    return i;
                }
            }
            return -1;
        } else {
            return curLine.indexOf(',');
        }
    }

    private String cleanupQuotes(String token) {
        StringBuffer buf = new StringBuffer();
        int length = token.length();
        int curIndex = 0;
        if (token.startsWith("\"") && token.endsWith("\"")) {
            curIndex = 1;
            length--;
        }
        boolean oneQuoteFound = false;
        boolean twoQuotesFound = false;
        while (curIndex < length) {
            char curChar = token.charAt(curIndex);
            if (curChar == '"') {
                twoQuotesFound = (oneQuoteFound) ? true : false;
                oneQuoteFound = true;
            } else {
                oneQuoteFound = false;
                twoQuotesFound = false;
            }
            if (twoQuotesFound) {
                twoQuotesFound = false;
                oneQuoteFound = false;
                curIndex++;
                continue;
            }
            buf.append(curChar);
            curIndex++;
        }
        return buf.toString();
    }
}
