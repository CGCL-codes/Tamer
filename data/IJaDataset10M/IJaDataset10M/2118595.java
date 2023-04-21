package pl.edu.agh.iosr.gamblingzone.action;

import org.richfaces.model.TreeNodeImpl;

public class GroupsTreeNodeImpl extends TreeNodeImpl {

    private String type = "";

    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
