package dpgui.model;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import mil.army.usace.ehlschlaeger.rgik.util.FileUtil;
import org.supercsv.io.CsvListReader;
import org.supercsv.io.CsvListWriter;
import org.supercsv.io.ICsvListReader;
import org.supercsv.io.ICsvListWriter;
import org.supercsv.prefs.CsvPreference;

/**
 * Helper to manage the mapping of landu-use class numbers to names.
 * Use loadMapMetadata() to load the dictionary correponding to a map file,
 * and saveDict() to save a modified dictionary back to its file.
 *
 * @author William R. Zwicky
 */
public class LanduseDictionary {

    /** Path and name of file we last loaded. */
    protected File currentFile;

    /** Metadata giving names to each land-use code. */
    protected Map<Integer, String> classNames = new HashMap<Integer, String>();

    public LanduseDictionary() {
    }

    /**
     * Change dictionary file without trying to load it.
     * @param mapFile land-use map file, relative to which we will create
     *     the dictionary file
     */
    public void setMapFile(File mapFile) {
        currentFile = makeMetadataFile(mapFile);
    }

    /**
     * Change dictionary file without trying to load it.
     * @param dictFile exact path and name of file we will store dictionary in
     */
    public void setDictFile(File dictFile) {
        currentFile = dictFile;
    }

    /**
     * Add or change a land-use code/name mapping.
     *
     * @param classNumber
     * @param className
     */
    public void put(Integer classNumber, String className) {
        classNames.put(classNumber, className);
    }

    /**
     * @param classNumber
     * @return name for given land-use code, or null if none defined
     */
    public String get(Integer classNumber) {
        return classNames.get(classNumber);
    }

    /** Compute path and name of metadata file for our current map file. */
    public File makeMetadataFile(File mapFile) {
        if (mapFile == null) return null; else return FileUtil.resolve(mapFile.getParentFile(), mapFile.getName() + ".metadata.csv");
    }

    /**
     * Change dictionary file and load its contents, if possible.
     * Quietly ignores all errors.  If file is missing, will set file name
     * so that saveDict() will work.
     *
     * @param mapFile land-use map file, relative to which we will create
     *     the dictionary file.  Use null to clear dictionary and forget
     *     current file.
     */
    public void loadMapMetadata(File mapFile) {
        classNames.clear();
        currentFile = makeMetadataFile(mapFile);
        if (currentFile == null) return;
        try {
            ICsvListReader reader = new CsvListReader(new FileReader(currentFile), CsvPreference.STANDARD_PREFERENCE);
            try {
                reader.getCSVHeader(true);
                List<String> line;
                while ((line = reader.read()) != null) {
                    classNames.put(new Integer(line.get(0)), line.get(1));
                }
            } finally {
                reader.close();
            }
        } catch (IOException ex) {
        }
    }

    /**
     * Save current dictionary back to the last file we loaded.
     * Quietly does nothing if we haven't loaded a file.
     */
    public void saveDict() {
        if (currentFile == null) return;
        try {
            ICsvListWriter writer = new CsvListWriter(new FileWriter(currentFile), CsvPreference.STANDARD_PREFERENCE);
            try {
                writer.writeHeader("number", "name");
                List<Integer> keys = new ArrayList<Integer>(classNames.keySet());
                Collections.sort(keys);
                for (Integer key : keys) {
                    writer.write(key, classNames.get(key));
                }
            } finally {
                writer.close();
            }
        } catch (IOException ex) {
            Logger.getLogger(LanduseDictionary.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
