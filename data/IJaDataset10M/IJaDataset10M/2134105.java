package org.matsim.core.config.groups;

import java.util.TreeMap;
import org.matsim.core.config.Module;

/**
 * Config group for households
 * @author dgrether
 */
public class HouseholdsConfigGroup extends Module {

    private static final long serialVersionUID = 1L;

    public static final String GROUP_NAME = "households";

    private static final String INPUT_FILE = "inputFile";

    private String inputFile = null;

    public HouseholdsConfigGroup() {
        super(GROUP_NAME);
    }

    @Override
    public String getValue(final String key) {
        if (INPUT_FILE.equals(key)) {
            return getInputFile();
        }
        throw new IllegalArgumentException(key);
    }

    @Override
    public void addParam(final String key, final String value) {
        if (INPUT_FILE.equals(key)) {
            setInputFile(value.replace('\\', '/'));
        } else {
            throw new IllegalArgumentException(key);
        }
    }

    @Override
    public final TreeMap<String, String> getParams() {
        TreeMap<String, String> map = new TreeMap<String, String>();
        addParameterToMap(map, INPUT_FILE);
        return map;
    }

    public String getInputFile() {
        return this.inputFile;
    }

    public void setInputFile(final String inputFile) {
        this.inputFile = inputFile;
    }
}
