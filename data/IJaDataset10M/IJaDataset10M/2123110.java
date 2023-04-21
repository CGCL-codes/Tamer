package net.msgexperts.gwapi;

import java.util.ArrayList;
import com.novell.groupwise.ws.Folder;
import com.novell.groupwise.ws.FolderType;
import com.novell.groupwise.ws.SystemFolder;

public class FolderData {

    final Folder f;

    final ArrayList<FolderData> children;

    public FolderData(Folder f) {
        this.f = f;
        children = new ArrayList<FolderData>();
    }

    public void addChild(Folder f) {
        children.add(new FolderData(f));
    }

    public void addChild(FolderData fd) {
        children.add(fd);
    }

    public String getId() {
        return f.getId();
    }

    public String getName() {
        return f.getName();
    }

    public String getParent() {
        return f.getParent();
    }

    public String getURL() {
        return f.getURL();
    }

    public ArrayList<FolderData> getChildren() {
        return children;
    }

    @Override
    public String toString() {
        return getName();
    }

    public FolderType getType() {
        if (f instanceof SystemFolder) {
            SystemFolder sys = (SystemFolder) f;
            return sys.getFolderType();
        } else return FolderType.NORMAL;
    }
}
