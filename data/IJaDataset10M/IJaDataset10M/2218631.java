package org.xmlcml.cml.converters.docx;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import org.xmlcml.cml.base.CMLConstants;

public class DOCXComponent {

    public static final String _RELS = "_rels";

    public static final String CONTENT_TYPES_XML = "[Content_Types].xml";

    public static final String CUSTOM_XML = "customXml";

    public static final String DOC_PROPS = "docProps";

    public static final String DOCUMENT_XML_RELS = "document.xml.rels";

    public static final String DOCUMENT_XML = "document.xml";

    public static final String DOT_RELS = ".rels";

    public static final String EMBEDDINGS = "embeddings";

    public static final String ENDNOTES_XML = "endnotes.xml";

    public static final String FONT = "font";

    public static final String FONTS = "fonts";

    public static final String FONT_TABLE_XML_RELS = "fontTable.xml.rels";

    public static final String FONT_TABLE_XML = "fontTable.xml";

    public static final String FOOTER1_XML = "footer1.xml";

    public static final String FOOTER2_XML = "footer2.xml";

    public static final String FOOTNOTES_XML = "footnotes.xml";

    public static final String GLOSSARY = "glossary";

    public static final String HEADER1_XML = "header1.xml";

    public static final String IMAGE = "image";

    public static final String ITEM_PROPS = "itemProps";

    public static final String ITEM = "item";

    public static final String MEDIA = "media";

    public static final String NUMBERING_XML = "numbering.xml";

    public static final String OLE_OBJECT = "oleObject";

    public static final String SETTINGS_XML = "settings.xml";

    public static final String SETTINGS_XML_RELS = "settings.xml.rels";

    public static final String STYLES_XML = "styles.xml";

    public static final String THEME = "theme";

    public static final String WEB_SETTINGS_XML = "webSettings.xml";

    public static final String WORD = "word";

    protected DOCXProcessor processor;

    protected ZipEntry zipEntry;

    protected String name;

    protected String[] names;

    protected String type;

    byte[] bytes;

    protected DOCXComponent(ZipEntry zipEntry, DOCXProcessor processor) {
        this.processor = processor;
        this.zipEntry = zipEntry;
        name = zipEntry.getName();
        names = name.split(CMLConstants.S_SLASH);
    }

    public static DOCXComponent createComponent(ZipEntry zipEntry, DOCXProcessor processor) {
        DOCXComponent component = null;
        DOCXComponent comp = new DOCXComponent(zipEntry, processor);
        if (comp.names[0] == null) {
            throw new RuntimeException("null first name");
        } else if (comp.names[0].equals(_RELS)) {
            component = new RelsComponent(zipEntry, processor);
        } else if (comp.names[0].equals(WORD)) {
            component = new WordComponent(zipEntry, processor);
        } else if (comp.names[0].equals(CUSTOM_XML)) {
            component = new CustomXMLComponent(zipEntry, processor);
        } else if (comp.names[0].equals(DOC_PROPS)) {
            component = new DocPropsComponent(zipEntry, processor);
        } else if (comp.names[0].equals(CONTENT_TYPES_XML)) {
            component = new ContentTypesComponent(zipEntry, processor);
            component.type = CONTENT_TYPES_XML;
        } else {
            throw new RuntimeException("unknown first name: " + comp.name);
        }
        return component;
    }

    public void setBytes(byte[] bb) {
        this.bytes = new byte[bb.length];
        System.arraycopy(bb, 0, bytes, 0, bb.length);
    }

    void writeFile() {
        String entryName = null;
        try {
            String filename = makeFilename(this.name);
            File file = new File(processor.outputDir, filename);
            new File(file.getParent()).mkdirs();
            System.out.println(file.getAbsolutePath());
            FileOutputStream fos = new FileOutputStream(file);
            entryName = this.zipEntry.getName();
            fos.write(this.bytes);
            fos.close();
        } catch (Exception e) {
            throw new RuntimeException("cannot write file: " + entryName, e);
        }
    }

    private String makeFilename(String name) {
        name = name.replace(CMLConstants.S_LSQUARE, CMLConstants.S_UNDER);
        name = name.replace(CMLConstants.S_RSQUARE, CMLConstants.S_UNDER);
        return name;
    }
}
