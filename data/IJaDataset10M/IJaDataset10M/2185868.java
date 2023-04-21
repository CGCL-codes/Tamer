package se.sics.isl.db.file;

import java.io.File;
import java.io.FilenameFilter;

public class NameFilter implements FilenameFilter {

    private final String tableName;

    public NameFilter(String tableName) {
        this.tableName = tableName + '.';
    }

    public boolean accept(File dir, String name) {
        return name.startsWith(tableName);
    }
}
