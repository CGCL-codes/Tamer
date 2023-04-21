package org.deft.repository;

import java.io.File;
import java.util.LinkedList;
import java.util.List;
import org.deft.repository.ast.decoration.Format;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.helpers.DefaultHandler;

public class XmlToFormatContentConverter extends XmlToRepositoryContentConverter {

    private XfsrFormatManager formatManager;

    private boolean readAsDefaultFormats;

    private String parserID;

    private static String formatSchemaLocation = new File("resources/formats.xsd").getAbsolutePath();

    public XmlToFormatContentConverter(XmlFileSystemRepository rep) {
        super(rep, formatSchemaLocation);
        this.formatManager = XfsrFormatManager.getInstance();
        this.parserID = "java";
    }

    public ContentHandler getContentHandler() {
        return new ReadXmlFormatContentHandler();
    }

    /**
     * The content handler for algorithm definition files.
     */
    private class ReadXmlFormatContentHandler extends DefaultHandler {

        private Format currentFormat;

        private List<String> currentHideList;

        private List<String> currentHideExceptList;

        private String currentReplace;

        private String currentTarget;

        private String snippetId;

        private boolean readContent;

        String contentRead = "";

        public void startElement(String uri, String localName, String qName, Attributes attributes) {
            if (localName.equals("format")) {
                String name = attributes.getValue("name");
                currentFormat = new Format(name);
                String sDisplay = attributes.getValue("display");
                if (sDisplay != null) {
                    String[] displays = sDisplay.split("\\|");
                    for (String display : displays) {
                        currentFormat.addDisplay(display);
                    }
                }
                snippetId = attributes.getValue("snippetid");
            } else if (localName.equals("target")) {
                currentTarget = attributes.getValue("type");
            } else if (localName.equals("replacegroup")) {
                currentHideList = new LinkedList<String>();
                currentHideExceptList = new LinkedList<String>();
            } else if (localName.equals("hide")) {
                String path = attributes.getValue("path");
                currentHideList.add(path);
            } else if (localName.equals("hideexcept")) {
                String path = attributes.getValue("path");
                currentHideExceptList.add(path);
            } else if (localName.equals("replace")) {
                readContent = true;
            }
        }

        public void endElement(String uri, String localName, String qName) {
            if (localName.equals("format")) {
                formatManager.addDefaultFormat(parserID, currentFormat);
                currentFormat = null;
                snippetId = null;
            } else if (localName.equals("target")) {
                if (currentHideList == null) {
                    currentFormat.addTarget(currentTarget);
                }
                currentTarget = null;
            } else if (localName.equals("replacegroup")) {
                if (!currentHideList.isEmpty()) {
                    currentFormat.addReplaceAll(currentTarget, currentHideList, currentReplace);
                }
                if (!currentHideExceptList.isEmpty()) {
                    currentFormat.addReplaceExcept(currentTarget, currentHideExceptList, currentReplace);
                }
                currentHideList = null;
                currentHideExceptList = null;
                currentReplace = null;
            } else if (localName.equals("replace")) {
                currentReplace = contentRead;
                readContent = false;
                contentRead = "";
            }
        }

        public void characters(char[] ch, int start, int length) {
            if (readContent) {
                contentRead += String.copyValueOf(ch, start, length);
            }
        }
    }
}
