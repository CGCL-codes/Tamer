package org.silicolife.util.xls2sql.data;

import java.util.Collection;
import java.util.HashMap;
import java.util.Set;

public class ExcelFileRepository {

    private HashMap<String, ExcelFile> fileMap = new HashMap<String, ExcelFile>();

    public void put(ExcelFile file) {
        fileMap.put(file.getKey(), file);
    }

    public ExcelFile get(String key) {
        ExcelFile file = fileMap.get(key);
        if (file == null) {
        }
        return file;
    }

    public void del(String key) {
        fileMap.remove(key);
    }

    public int size() {
        return fileMap.size();
    }

    public Set<String> keySet() {
        return fileMap.keySet();
    }

    public Collection<ExcelFile> values() {
        return fileMap.values();
    }
}
