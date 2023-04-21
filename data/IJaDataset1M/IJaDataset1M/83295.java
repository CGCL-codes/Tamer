package idlcompiler.files;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 *
 * @author angr
 */
public class TopicConfigFile implements TextFile {

    private File file;

    private String text = "";

    /** Creates a new instance of TopicConfigFile */
    public TopicConfigFile(File file) throws IOException {
        this.file = file;
        try {
            file.createNewFile();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        try {
            FileInputStream fIn = new FileInputStream(file);
            byte[] b = null;
            try {
                b = new byte[fIn.available()];
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            try {
                fIn.read(b);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            setText(new String(b));
            fIn.close();
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        }
    }

    public void save() {
        FileOutputStream fOut = null;
        try {
            fOut = new FileOutputStream(getFile());
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        }
        try {
            fOut.write(getText().getBytes());
            fOut.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getName() {
        return file.getName();
    }

    public String toString() {
        return getName();
    }
}
