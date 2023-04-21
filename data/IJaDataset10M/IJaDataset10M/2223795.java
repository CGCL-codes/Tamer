package edu.ucdavis.genomics.metabolomics.sjp.handler;

import edu.ucdavis.genomics.metabolomics.sjp.ParserHandler;
import edu.ucdavis.genomics.metabolomics.sjp.exception.ParserException;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Date;
import java.util.Properties;

/**
 * @author Gert Wohlgemuth
 *
 * kann verwendet werden um dateien aus dem internet herunterzuladen. Dabei
 * muss in den properties der parameter URL gesetzt sein und diese den Ausdruck
 * (ID) aufweisen. Dieser ausdruck wird dann durch das entsprechende element
 * ersetzt welches geparst wurde und die datei wird dann im angegebenen
 * ordner gespeichert.
 *
 *
 * example
 * <p>
 * http://webbook.nist.gov/cgi/cbook.cgi?Name=(ID)&Units=SI&cMS=on
 * </p>
 * des weiteres wird der parameter DIR f?r das verzeichnis und EXTENSION f?r
 * die dateiendung ben?tigt
 *
 */
public class FileDownloadHandler implements ParserHandler {

    /**
     * identifier f?r ein directory
     */
    public static final String DIR = "DIR";

    /**
     * identifier f?r die url
     */
    public static final String URL = "URL";

    /**
     * identifier f?r das gew?nschte attribute
     */
    public static final String ATTRIBUTE = "ATTRIBUTE";

    /**
     * extension f?r die heruntergeladenen dateien
     */
    public static final String EXTENSION = "EXTENSION";

    /**
     * DOCUMENT ME!
     */
    private File dir;

    /**
     * DOCUMENT ME!
     */
    private String ext;

    /**
     * DOCUMENT ME!
     */
    private String field;

    /**
     * DOCUMENT ME!
     */
    private String url;

    /**
     * @return Returns the dir.
     *
     * @uml.property name="dir"
     */
    public File getDir() {
        return dir;
    }

    /**
     * @return Returns the ext.
     *
     * @uml.property name="ext"
     */
    public String getExt() {
        return ext;
    }

    /**
     * @return Returns the field.
     *
     * @uml.property name="field"
     */
    public String getField() {
        return field;
    }

    /**
     * @see edu.ucdavis.genomics.metabolomics.binbase.parser.ParserHandler#setProperties(java.util.Properties)
     */
    public void setProperties(Properties p) {
        dir = new File(p.getProperty(DIR));
        if (dir.exists() == false) {
            dir.mkdirs();
        }
        ext = p.getProperty(EXTENSION);
        url = p.getProperty(URL);
        field = p.getProperty(ATTRIBUTE);
    }

    /**
     * @return Returns the url.
     *
     * @uml.property name="url"
     */
    public String getUrl() {
        return url;
    }

    /**
     * @see edu.ucdavis.genomics.metabolomics.sjp.ParserHandler#endAttribute(java.lang.String, java.lang.String)
     */
    public void endAttribute(String element, String name) {
    }

    /**
     * @see edu.ucdavis.genomics.metabolomics.binbase.parser.ParserHandler#endDataSet()
     */
    public void endDataSet() {
    }

    /**
     * @see edu.ucdavis.genomics.metabolomics.binbase.parser.ParserHandler#endDocument()
     */
    public void endDocument() {
    }

    /**
     * @see edu.ucdavis.genomics.metabolomics.sjp.ParserHandler#endElement(java.lang.String)
     */
    public void endElement(String name) {
    }

    /**
     * @see edu.ucdavis.genomics.metabolomics.sjp.ParserHandler#startAttribute(java.lang.String, java.lang.String, java.lang.String)
     */
    public void startAttribute(String element, String name, String value) {
    }

    /**
     * @see edu.ucdavis.genomics.metabolomics.binbase.parser.ParserHandler#startDataSet()
     */
    public void startDataSet() {
    }

    /**
     * @see edu.ucdavis.genomics.metabolomics.binbase.parser.ParserHandler#startDocument()
     */
    public void startDocument() {
    }

    /**
     * @see edu.ucdavis.genomics.metabolomics.binbase.parser.ParserHandler#foundElement(java.lang.String,
     *      java.lang.String)
     */
    public void startElement(String name, String value) throws ParserException {
        try {
            if (name.equals(field) == true) {
                value = URLEncoder.encode(value);
                String temp = url;
                temp = temp.replaceFirst("\\(ID\\)", value);
                System.err.println("found url - " + temp);
                URL url = new URL(temp);
                handleUrl(url);
            }
        } catch (Exception e) {
            throw new ParserException(e);
        }
    }

    /**
     * macht was mit der url
     */
    protected void handleUrl(URL url) throws Exception {
        File file = new File(dir.getAbsolutePath() + "/" + new Date().getTime() + "." + this.ext);
        FileWriter writer = new FileWriter(file);
        BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
        String s;
        while ((s = in.readLine()) != null) {
            writer.write(s + "\n");
        }
        in.close();
        writer.close();
    }
}
