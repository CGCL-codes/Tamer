package org.xmlcml.cml.converters.docx;

import java.util.zip.ZipEntry;

public class WordComponent extends DOCXComponent {

    public WordComponent(ZipEntry zipEntry, DOCXProcessor processor) {
        super(zipEntry, processor);
        process();
    }

    private void process() {
        if (names[1] == null) {
            throw new RuntimeException("expected second name: " + name);
        } else if (names[1].equals(_RELS)) {
            processRels();
        } else if (names[1].equals(EMBEDDINGS)) {
            processEmbeddings();
        } else if (names[1].equals(FONTS)) {
            processFonts();
        } else if (names[1].equals(GLOSSARY)) {
            processGlossary();
        } else if (names[1].equals(MEDIA)) {
            processMedia();
        } else if (names[1].equals(THEME)) {
            processTheme();
        } else if (names[1].equals(DOCUMENT_XML)) {
            type = names[1];
        } else if (names[1].equals(ENDNOTES_XML)) {
            type = names[1];
        } else if (names[1].equals(HEADER1_XML)) {
            type = names[1];
        } else if (names[1].equals(FOOTER1_XML)) {
            type = names[1];
        } else if (names[1].equals(FONT_TABLE_XML)) {
            type = names[1];
        } else if (names[1].equals(FOOTER2_XML)) {
            type = names[1];
        } else if (names[1].equals(FOOTNOTES_XML)) {
            type = names[1];
        } else if (names[1].equals(NUMBERING_XML)) {
            type = names[1];
        } else if (names[1].equals(SETTINGS_XML)) {
        } else if (names[1].equals(STYLES_XML)) {
        } else if (names[1].equals(WEB_SETTINGS_XML)) {
        } else {
            throw new RuntimeException("bad name in Word " + name);
        }
    }

    private void processRels() {
        if (names[2] == null) {
            throw new RuntimeException("expected third name: " + name);
        } else if (names[2].equals(DOCUMENT_XML_RELS)) {
            type = names[2];
        } else if (names[2].equals(SETTINGS_XML_RELS)) {
            type = names[2];
        } else if (names[2].equals(FONT_TABLE_XML_RELS)) {
            type = names[2];
        } else {
            throw new RuntimeException("bad name in Word rels " + name);
        }
    }

    private void processFonts() {
        if (names[2] == null) {
            throw new RuntimeException("expected third name: " + name);
        } else if (names[2].startsWith(FONT)) {
            type = FONT;
        } else {
            throw new RuntimeException("bad name in Word fonts " + name);
        }
    }

    private void processGlossary() {
        if (names[2] == null) {
            throw new RuntimeException("expected third name: " + name);
        } else if (names[2].equals(_RELS)) {
            processGlossaryRels();
        } else if (names[2].equals(DOCUMENT_XML_RELS)) {
        } else if (names[2].equals(DOCUMENT_XML)) {
        } else if (names[2].equals(FONT_TABLE_XML)) {
        } else if (names[2].equals(SETTINGS_XML)) {
        } else if (names[2].equals(STYLES_XML)) {
        } else if (names[2].equals(WEB_SETTINGS_XML)) {
        } else {
            throw new RuntimeException("bad name in Word glossary " + name);
        }
    }

    private void processGlossaryRels() {
        if (names[3] == null) {
            throw new RuntimeException("expected fourth name: " + name);
        } else if (names[3].equals(DOCUMENT_XML_RELS)) {
        } else {
            throw new RuntimeException("bad name in Word glossary Rels " + name);
        }
    }

    private void processMedia() {
        if (names[2] == null) {
            throw new RuntimeException("expected third name: " + name);
        } else if (names[2].startsWith(IMAGE)) {
            type = IMAGE;
        } else {
            throw new RuntimeException("bad name in Word media " + name);
        }
    }

    private void processEmbeddings() {
        if (names[2] == null) {
            throw new RuntimeException("expected third name: " + name);
        } else if (names[2].startsWith(OLE_OBJECT)) {
            type = OLE_OBJECT;
        } else {
            throw new RuntimeException("bad name in Word embeddings " + name);
        }
    }

    private void processTheme() {
        if (names[2] == null) {
            throw new RuntimeException("expected third name: " + name);
        } else if (names[2].startsWith(THEME)) {
            type = THEME;
        } else {
            throw new RuntimeException("bad name in Word theme " + name);
        }
    }
}
