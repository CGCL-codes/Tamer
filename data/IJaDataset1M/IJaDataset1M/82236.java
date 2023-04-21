package net.sourceforge.javabits.tool.copier;

import java.io.File;
import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;
import net.sourceforge.javabits.tool.DefaultToolData;

/**
 * @author Jochen Kuhnle
 */
public class CopierData extends DefaultToolData {

    private Map<File, File> fileMap = new TreeMap<File, File>();

    public void setFileMap(Map<File, File> fileMap) {
        this.fileMap = new TreeMap<File, File>(fileMap);
    }

    public Map<File, File> getFileMap() {
        return Collections.unmodifiableMap(this.fileMap);
    }
}
