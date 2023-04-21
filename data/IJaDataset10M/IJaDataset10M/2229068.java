package com.armatiek.infofuze.stream.filesystem;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.io.input.XmlStreamReader;
import com.armatiek.infofuze.error.InfofuzeException;

/**
 * @author Maarten Kroon
 *
 */
public class PrologSkippingXMLStreamReader extends Reader {

    private static final int BUFF_SIZE = 1024;

    protected BufferedReader reader;

    private static final Pattern rootElemPattern = Pattern.compile("<[^!?]", Pattern.MULTILINE);

    private static final Pattern commentPattern = Pattern.compile("<!--.*?(-->|\\z)", Pattern.DOTALL | Pattern.MULTILINE);

    public PrologSkippingXMLStreamReader(InputStream is) throws IOException {
        reader = new BufferedReader(new XmlStreamReader(is));
        StringBuilder xmlBuff;
        Matcher rootElemMatcher;
        boolean found;
        int counter = 0;
        int numRead = -1;
        char[] charBuff = new char[BUFF_SIZE];
        do {
            counter++;
            int buffSize = counter * BUFF_SIZE;
            reader.mark(buffSize);
            int totalRead = 0;
            xmlBuff = new StringBuilder();
            do {
                numRead = reader.read(charBuff);
                xmlBuff.append(charBuff);
                totalRead += numRead;
            } while (numRead != -1 && totalRead < buffSize);
            reader.reset();
            String xml = xmlBuff.toString();
            rootElemMatcher = rootElemPattern.matcher(xml);
            Matcher commentMatcher = null;
            while (found = rootElemMatcher.find()) {
                int start = rootElemMatcher.start();
                if (commentMatcher == null) {
                    commentMatcher = commentPattern.matcher(xml);
                } else {
                    commentMatcher.reset();
                }
                boolean withinComment = false;
                if (commentMatcher.find()) {
                    do {
                        if (start > commentMatcher.start() && start < commentMatcher.end()) {
                            withinComment = true;
                            break;
                        }
                    } while (commentMatcher.find());
                }
                if (!withinComment) {
                    break;
                }
            }
        } while (!found && numRead != -1);
        if (!found) {
            throw new InfofuzeException("Could not find start element in XML stream");
        }
        reader.skip(rootElemMatcher.start());
    }

    @Override
    public void close() throws IOException {
        reader.close();
    }

    @Override
    public int read(char[] cbuf, int off, int len) throws IOException {
        return reader.read(cbuf, off, len);
    }
}
