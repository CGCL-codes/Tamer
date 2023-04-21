package org.dom4j.samples.performance;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.StringReader;
import org.dom4j.Document;
import org.dom4j.io.SAXReader;

/**
 * A simple parsing program that loops which makes it easier to profile
 * 
 * @author <a href="mailto:jstrachan@apache.org">James Strachan </a>
 * @version $Revision: 1.4 $
 */
public class ParseLoop {

    private static int bufferSize = 128 * 1024;

    public static void main(String[] args) throws Exception {
        if (args.length <= 0) {
            System.out.println("arguments: <XML file> [<loopCount>]");
            return;
        }
        String xmlFile = args[0];
        int loops = 40;
        if (args.length > 1) {
            loops = Integer.parseInt(args[1]);
        }
        StringBuffer buffer = new StringBuffer(64 * 1024);
        BufferedReader reader = new BufferedReader(new FileReader(xmlFile));
        while (true) {
            String text = reader.readLine();
            if (text == null) {
                break;
            }
            buffer.append(text);
            buffer.append("\n");
        }
        long start = System.currentTimeMillis();
        parse(buffer.toString(), loops);
        long elapsed = System.currentTimeMillis() - start;
        System.out.println("Parsed: " + xmlFile + " " + loops + " times in: " + elapsed + " (ms)");
    }

    protected static void parse(String text, int loops) throws Exception {
        SAXReader xmlReader = new SAXReader();
        for (int i = 0; i < loops; i++) {
            Document document = xmlReader.read(new StringReader(text));
        }
    }
}
