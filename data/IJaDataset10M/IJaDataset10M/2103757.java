package person.liusy.lsyjb.tree.impl;

import java.util.ArrayList;
import java.util.List;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.IEditorInput;
import person.liusy.lsyjb.tree.ItreeEntry;

public class NavigatorEntry implements ItreeEntry {

    private String name;

    private ItreeEntry parentEntry;

    private List children = new ArrayList();

    private Image image;

    private Object data;

    private IEditorInput editorInput;

    private String editorId;

    public IEditorInput getEditorInput() {
        return editorInput;
    }

    public void setEditorInput(IEditorInput editorInput) {
        this.editorInput = editorInput;
    }

    public String getEditorId() {
        return editorId;
    }

    public void setEditorId(String editorId) {
        this.editorId = editorId;
    }

    public NavigatorEntry(String name) {
        this.name = name;
    }

    public void addChild(ItreeEntry entry) {
        this.children.add(entry);
    }

    public List getChildren() {
        return this.children;
    }

    public String getName() {
        return name;
    }

    public ItreeEntry getParentEntry() {
        return parentEntry;
    }

    public boolean hasChild() {
        return children.size() > 0;
    }

    public void setChildren(List children) {
        this.children = children;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setParentEntry(ItreeEntry parentEntry) {
        this.parentEntry = parentEntry;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
