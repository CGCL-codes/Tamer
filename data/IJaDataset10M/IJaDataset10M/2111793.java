package jhelpgenerator.utils;

import java.io.*;
import oracle.xml.parser.v2.*;

/**
 *
 * @author v3r5_u5
 */
public abstract class XMLFile {

    protected XMLDocument XMLDoc;

    protected String fileName;

    public XMLFile() {
    }

    public XMLFile(XMLDocument XMLDoc) {
        this.XMLDoc = XMLDoc;
    }

    public XMLFile(String fileName) {
        this.fileName = fileName;
    }

    public abstract void createEmptyDoc();

    public void saveXML() throws IOException {
        FileOutputStream fout = new FileOutputStream(fileName);
        XMLDoc.print(fout);
        fout.flush();
        fout.close();
    }

    /**
     * Метод,  читающий <code>XMLDocument</code> из файла.
     * @throws IOException
     */
    public void readXML() {
        DOMParser parser = new DOMParser();
        parser.setPreserveWhitespace(false);
        parser.setValidationMode(DOMParser.NONVALIDATING);
        parser.setDebugMode(true);
        try {
            parser.parse(new FileInputStream(fileName));
            XMLDoc = parser.getDocument();
        } catch (Exception ex) {
            System.out.println(ex.getLocalizedMessage());
            createEmptyDoc();
        }
    }

    public XMLDocument getXMLDoc() {
        return XMLDoc;
    }

    public void setXMLDoc(XMLDocument XMLDoc) {
        this.XMLDoc = XMLDoc;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
