package de.herberlin.pss.model;

public class Directory extends BasicVO<Directory.Key> {

    private static final long serialVersionUID = 1L;

    public static enum Key {

        iDirId, cDirName, iParentDirId
    }

    ;

    public Directory() {
    }

    @Override
    public String getTableName() {
        return "dirTable";
    }

    @Override
    public String getPrimaryKeyName() {
        return Key.iDirId.name();
    }
}
