package marten.aoe.fileio;

import java.util.ArrayList;
import java.util.List;

public final class DataTree {

    private final String value;

    private final ArrayList<DataTree> branches = new ArrayList<DataTree>();

    public DataTree(String value) {
        this.value = value;
    }

    public void addBranch(DataTree branch) {
        this.branches.add(branch);
    }

    public void addBranch(String value) {
        this.branches.add(new DataTree(value));
    }

    public void addBranch(String key, String value) {
        DataTree keyValue = new DataTree("KEYVALUE");
        keyValue.addBranch(key);
        keyValue.addBranch(value);
        this.branches.add(keyValue);
    }

    public String value() {
        return this.value;
    }

    public List<DataTree> branches() {
        return this.branches;
    }
}
