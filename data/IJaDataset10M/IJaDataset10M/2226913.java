package playground.marcel.config.groups;

import java.util.LinkedHashSet;
import java.util.Set;
import playground.marcel.config.ConfigGroupI;
import playground.marcel.config.ConfigListI;

public abstract class AbstractFileIOConfigGroup implements ConfigGroupI {

    public static final String INPUT_FILE = "inputFile";

    public static final String OUTPUT_FILE = "outputFile";

    public static final String OUTPUT_FORMAT = "outputVersion";

    private String inputFile = "";

    private String outputFile = "";

    private String outputFormat = "";

    protected Set<String> keyset = null;

    public AbstractFileIOConfigGroup() {
        this.keyset = new LinkedHashSet<String>();
        this.keyset.add(INPUT_FILE);
        this.keyset.add(OUTPUT_FILE);
        this.keyset.add(OUTPUT_FORMAT);
    }

    public abstract String getName();

    public String getValue(String key) {
        if (key.equals(INPUT_FILE)) {
            return getInputFile();
        } else if (key.equals(OUTPUT_FILE)) {
            return getOutputFile();
        } else if (key.equals(OUTPUT_FORMAT)) {
            return getOutputFormat();
        } else {
            throw new IllegalArgumentException(key);
        }
    }

    public void setValue(String key, String value) {
        if (key.equals(INPUT_FILE)) {
            setInputFile(value);
        } else if (key.equals(OUTPUT_FILE)) {
            setOutputFile(value);
        } else if (key.equals(OUTPUT_FORMAT)) {
            setOutputFormat(value);
        } else {
            throw new IllegalArgumentException(key);
        }
    }

    public Set<String> paramKeySet() {
        return keyset;
    }

    public Set<String> listKeySet() {
        return EMPTY_LIST_SET;
    }

    public ConfigListI getList(String key) {
        throw new UnsupportedOperationException();
    }

    public String getInputFile() {
        return this.inputFile;
    }

    public void setInputFile(String filename) {
        this.inputFile = filename;
    }

    public String getOutputFile() {
        return this.outputFile;
    }

    public void setOutputFile(String filename) {
        this.outputFile = filename;
    }

    public String getOutputFormat() {
        return this.outputFormat;
    }

    public void setOutputFormat(String format) {
        this.outputFormat = format;
    }
}
