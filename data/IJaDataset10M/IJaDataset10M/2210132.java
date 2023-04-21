package com.akjava.wiki.client.core;

import java.util.List;
import java.util.Vector;
import com.akjava.wiki.client.modules.LineFolder;
import com.akjava.wiki.client.modules.Text;
import com.akjava.wiki.client.util.StringUtils;

/**
 * 
 *
 */
public class DocumentBuilder {

    public RootDocument createDocument(String path, String texts) throws WikiException {
        String lines[] = splitLine(texts, false);
        RootDocument document = createDocument(lines);
        document.addAttribute(path);
        return document;
    }

    public String[] splitLine(String text, boolean bool) {
        text = StringUtils.replace(text, "\r\n", "\n");
        text = StringUtils.replace(text, "\r", "\n");
        return text.split("\n");
    }

    public RootDocument createDocument(String text) throws WikiException {
        return createDocument(splitLine(text, false));
    }

    public RootDocument createDocument(String[] lines) throws WikiException {
        RootDocument document = new RootDocument();
        Element element = document;
        for (int i = 0; i < lines.length; i++) {
            element = element.breakUp(element, lines[i]);
            LineParser[] parsers = element.getLineParses();
            boolean doParse = false;
            PARSER: for (int j = 0; j < parsers.length; j++) {
                if (parsers[j].canParse(lines[i])) {
                    doParse = true;
                    element = parsers[j].parse(element, lines[i]);
                    break PARSER;
                }
            }
            if (!doParse) {
                Element parent = element;
                element = new LineFolder();
                parent.addNode(element);
                String text = "";
                String line = lines[i];
                StringParser[] stringParsers = element.getStringParsers();
                while (line.length() > 0) {
                    boolean isMutch = false;
                    for (int j = 0; j < stringParsers.length; j++) {
                        if (stringParsers[j].canParse(line)) {
                            if (text.length() > 0) {
                                element.addNode(new Text(text));
                                text = "";
                            }
                            isMutch = true;
                            line = stringParsers[j].parse(element, line);
                        }
                    }
                    if (isMutch == false) {
                        if (line.length() > 0) {
                            text += line.charAt(0);
                            line = line.substring(1);
                        }
                    }
                }
                if (text.length() > 0) element.addNode(new Text(text));
                element = parent;
            }
        }
        return document;
    }
}
