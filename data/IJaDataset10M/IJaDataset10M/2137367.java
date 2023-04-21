package net.sourceforge.hlm.xml.write;

import java.io.*;
import java.net.*;
import net.sourceforge.hlm.generic.*;
import net.sourceforge.hlm.library.*;
import net.sourceforge.hlm.util.*;
import net.sourceforge.hlm.util.xml.write.*;

public abstract class ObjectWriter {

    public ObjectWriter(MessageHandler messageHandler, SimpleXMLWriter writer) {
        this.messageHandler = messageHandler;
        this.writer = writer;
    }

    protected XMLDocument startDocument(File file, URI dtdURI, String type) throws Exception {
        if (this.messageHandler != null) {
            this.messageHandler.startFile(file);
        }
        XMLDocument document = this.writer.startDocument(file);
        XMLAttributes docType = document.addDocType(type);
        docType.addAttribute("SYSTEM", null);
        docType.addAttribute(null, dtdURI.resolve(type + ".dtd").toString());
        return document;
    }

    protected void finishDocument(XMLDocument document) throws Exception {
        document.finish();
        if (this.messageHandler != null) {
            this.messageHandler.endFile();
        }
    }

    protected XMLElement createRootElement(XMLDocument document, String type, String name) throws Exception {
        if (name == null) {
            throw new XMLWriteException(Translator.format("object name not set"));
        }
        document.addNewLine();
        XMLElement rootElement = document.addElement(type);
        rootElement.addAttribute("name", name);
        rootElement.addAttribute("xmlns", "http://hlm.sourceforge.net/xml/");
        return rootElement;
    }

    protected String getLibraryObjectPath(LibraryObject object, Section section) throws Exception {
        LibraryObject nextObject = section.getContainingObject(object);
        if (nextObject == null) {
            LibraryObject parent = section.getParent();
            if (parent == null) {
                throw new XMLWriteException(Translator.format("no path to referenced object found"));
            }
            return "../" + this.getLibraryObjectPath(object, (Section) parent);
        } else {
            String name = nextObject.getName().get();
            if (name == null) {
                throw new XMLWriteException(Translator.format("referenced object lacks a name"));
            }
            if (nextObject.equals(object)) {
                return name;
            } else {
                return name + "/" + this.getLibraryObjectPath(object, (Section) nextObject);
            }
        }
    }

    protected SimpleXMLWriter writer;

    protected MessageHandler messageHandler;

    public class XMLWriteException extends Exception {

        public XMLWriteException(String message) {
            super(message);
        }

        public XMLWriteException(Throwable cause) {
            super(cause);
        }
    }
}
