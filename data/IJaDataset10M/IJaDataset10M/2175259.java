package jacg;

import jacg.model.ModelClass;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

/**
 * Speichert den Inhalt einer Datei in einem String
 * 
 * @author                       Carsten Spr�ner
 */
public class FileTemplate implements Template {

    /**
     * Name der Datei
     */
    private String fileName = null;

    /**
     * Inhalt der Datei
     */
    private String myContent = null;

    /**
     * Das Verzeichnis, in dem die Datei zu finden ist.
     */
    private String dir = null;

    public FileTemplate(String dir, String fileName) {
        this.fileName = fileName;
        this.dir = dir;
    }

    /**
     * Erzeugt ein neues Objekt
     * 
     * @param fileName
     *            String
     */
    public FileTemplate(String fileName) {
        this.fileName = fileName;
    }

    /**
     * Leere Implementierung
     * 
     * @param c
     *            Constraint
     */
    public void addConstraint(Constraint c) {
    }

    /**
     * Keine Implementierung
     * 
     * @param mc
     *            ModelClass
     * @return false
     */
    public boolean doesModelClassMatch(ModelClass mc) {
        return false;
    }

    /**
     * Name der Datei
     * 
     * @return String
     */
    public String getFileName() {
        return fileName;
    }

    public String getContent() throws FileNotFoundException, IOException {
        if (myContent == null) {
            InputStream is = null;
            if (Configuration.isFromClasspath()) {
                String resourceName = Configuration.getJTPrefix() + "/" + fileName;
                is = getClass().getResourceAsStream(resourceName);
                if (is == null) {
                    System.out.println("could not load resource " + resourceName);
                }
            } else {
                is = new FileInputStream(dir + "/" + fileName);
            }
            myContent = new String(FileUtil.streamToByteArray(is));
        }
        return myContent;
    }
}
