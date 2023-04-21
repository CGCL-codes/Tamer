package com.jedifact.catalog.test;

import java.io.File;
import java.io.FilenameFilter;
import java.io.StringReader;
import junit.framework.TestCase;
import com.jedifact.common.MessageType;
import com.jedifact.common.SyntaxVersion;
import com.jedifact.parser.EdifactParser;
import com.jedifact.parser.EdifactParserFactory;

public class TestAllMessageTypes extends TestCase {

    @Override
    protected void setUp() throws Exception {
    }

    public void testAllMessageTypesInCatalogs() {
        File catalogDir = new File("src/main/catalog");
        for (File subdir : catalogDir.listFiles(filter("\\d\\d[AB]"))) {
            String dir = subdir.getName();
            for (File specfile : subdir.listFiles(filter(".*\\." + dir))) {
                String messageType = specfile.getName().substring(0, 6).toUpperCase();
                parseMessageType(messageType, dir);
            }
        }
    }

    private void parseMessageType(String messageTypeName, String catalog) {
        System.out.println("Testing " + catalog + "/" + messageTypeName);
        MessageType messageType = new MessageType(messageTypeName, catalog, SyntaxVersion._4);
        EdifactParserFactory factory = new EdifactParserFactory().withSupportFor(messageType);
        EdifactParser parser = factory.createParser(new StringReader(""));
    }

    private FilenameFilter filter(final String regex) {
        return new FilenameFilter() {

            public boolean accept(File dir, String name) {
                return name.matches(regex);
            }
        };
    }
}
