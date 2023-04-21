package uk.ac.lkl.common.util.datafile;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * A file in the local file system.
 * 
 * @author sergut
 */
public class StandaloneDataFile extends DataFile {

    private File file;

    public StandaloneDataFile(String name, File file) {
        super(name);
        this.file = file;
    }

    @Override
    public String readContents() {
        try {
            String contents = "";
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;
            while ((line = reader.readLine()) != null) {
                contents += line + "\n";
            }
            reader.close();
            return contents;
        } catch (FileNotFoundException e) {
            return null;
        } catch (IOException e) {
            return "";
        }
    }

    @Override
    public boolean writeContents(String contents) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            writer.write(contents, 0, contents.length());
            writer.close();
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    @Override
    public boolean isReadOnly() {
        return false;
    }

    /**
     * Returns the file 
     * 
     * @return the file
     */
    public File getFile() {
        return file;
    }
}
