package net.ep.db4o.collections;

import java.util.List;
import net.ep.db4o.annotations.Persist;
import com.db4o.collections.ArrayList4;

public class TreeItem {

    private String name;

    private List<TreeItem> childs = null;

    private TreeItem parent = null;

    @Persist
    public void addChild(TreeItem it) {
        if (childs == null) childs = new ArrayList4<TreeItem>();
        childs.add(it);
    }

    @Persist
    void setName(String name) {
        this.name = name;
    }

    String getName() {
        return name;
    }

    @Persist
    void setParent(TreeItem parent) {
        this.parent = parent;
    }

    private TreeItem getParent() {
        return parent;
    }
}
