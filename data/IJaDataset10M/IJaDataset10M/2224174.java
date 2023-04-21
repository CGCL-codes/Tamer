package pl.kwiecienm.jcomet.model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author kwiecienm
 */
public final class TextFile {

    /** */
    public static final String FILE_NAME = "file_name";

    /** */
    public static final String FILE_FULL_PATH = "file_full_name";

    /** */
    private String _fullPath;

    /** */
    private String _name;

    /** */
    private String _extension;

    /** */
    private String[] _data;

    /** 
     */
    public TextFile(File inFile) {
        readFile(inFile);
    }

    /**
     * @return the fullPath
     */
    public String getFullPath() {
        return this._fullPath;
    }

    /**
     * @return the name
     */
    public String getName() {
        return this._name;
    }

    /**
     * @return the extension
     */
    public String getExtension() {
        return this._extension;
    }

    /**
     * @return the data
     */
    public String[] getData() {
        return this._data;
    }

    /**
     * @param inFile
     */
    private void readFile(File inFile) {
        List<String> data = new ArrayList<String>();
        BufferedReader in = null;
        try {
            this._fullPath = inFile.getCanonicalPath();
            getNameAndExtension(inFile);
            in = new BufferedReader(new FileReader(inFile));
            String line;
            while ((line = in.readLine()) != null) {
                data.add(line);
            }
            in.close();
        } catch (IOException e) {
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                }
            }
            this._data = data.toArray(new String[data.size()]);
        }
    }

    /**
     * @param inFile
     */
    private void getNameAndExtension(File inFile) {
        String filename = inFile.getName();
        int index = filename.lastIndexOf(".");
        if (index != -1) {
            this._name = filename.substring(0, index);
            this._extension = filename.substring(index + 1, filename.length());
        } else {
            this._name = filename;
            this._extension = "";
        }
    }
}
