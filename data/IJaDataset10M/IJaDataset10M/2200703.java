package pl.xperios.rtfcompiler;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author Praca
 */
public class CompileRTF {

    public static int zamkniety = 1;

    public static int otwarty = 2;

    private XMLElement xmlElement;

    public void compile(File inputRtf, XMLElement xmlElement, File outputRtf) {
        if (outputRtf == null) {
            return;
        }
        try {
            compile(inputRtf, xmlElement, new FileWriter(outputRtf));
        } catch (IOException ex) {
            System.err.println("Błąd przy tworzeniu pliku wynikowego: " + outputRtf.getAbsolutePath());
        }
    }

    public void compile(File inputRtf, XMLElement xmlElement, Writer outputStream) {
        this.xmlElement = xmlElement;
        xmlElement.print(0);
        FileInputStream inputStream = null;
        if (!inputRtf.exists()) {
            System.err.println("Błąd przy odczycie pliku wejściowego: " + inputRtf.getAbsolutePath());
            return;
        }
        try {
            inputStream = new FileInputStream(inputRtf);
            while (inputStream.available() > 0) {
                readContent(inputStream, outputStream);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException ex) {
                    Logger.getLogger(CompileRTF.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    private void readContent(FileInputStream inputStream, Writer outputStream) throws IOException {
        Character current = (char) inputStream.read();
        if (current.equals('<')) {
            current = (char) inputStream.read();
            if (current.equals('$')) {
                readTag(inputStream, outputStream, current, null).getCompiledContent(outputStream, null);
            }
        } else {
            outputStream.write(current);
        }
    }

    private TagFiller readTag(FileInputStream inputStream, Writer outputStream, Character current, TagFiller tagFillerParent) throws IOException {
        TagFiller tagFiller = new TagFiller(xmlElement);
        tagFiller.addParent(tagFillerParent);
        if (tagFillerParent != null) {
            tagFillerParent.addChildFiller(tagFiller);
        }
        Character nazwa;
        current = (char) inputStream.read();
        nazwa = current;
        current = (char) inputStream.read();
        if (!current.equals('%')) {
            return null;
        }
        String param = "";
        while (inputStream.available() > 0) {
            current = (char) inputStream.read();
            if (current.equals('$')) {
                current = (char) inputStream.read();
                if (current.equals(nazwa)) {
                    current = (char) inputStream.read();
                    if (current.equals('>')) {
                        tagFiller.setParameters(param, 0);
                        if (tagFillerParent == null) {
                        }
                        break;
                    }
                }
            }
            if (current.equals('>')) {
                tagFiller.setParameters(param, 1);
                while (inputStream.available() > 0) {
                    current = (char) inputStream.read();
                    if (current.equals('<')) {
                        current = (char) inputStream.read();
                        if (current.equals('/')) {
                            current = (char) inputStream.read();
                            if (current.equals('$')) {
                                current = (char) inputStream.read();
                                if (current.equals(nazwa)) {
                                    current = (char) inputStream.read();
                                    if (current.equals('>')) {
                                        if (tagFillerParent == null) {
                                        }
                                        break;
                                    }
                                }
                            }
                        }
                        if (current.equals('$')) {
                            tagFiller.addValue(readTag(inputStream, outputStream, current, tagFiller));
                        } else {
                            tagFiller.addValue(current);
                        }
                    } else {
                        tagFiller.addValue(current);
                    }
                }
                break;
            }
            param += current;
        }
        return tagFiller;
    }
}
