package com.prolix.editor.resourcemanager.model;

import com.prolix.editor.resourcemanager.commands.RemoveResourceItemCommand;
import com.prolix.editor.resourcemanager.zip.LearningDesignDataModel;
import uk.ac.reload.straker.datamodel.DataComponent;

/**
 * <<class description>>
 * 
 * @author Susanne Neumann, Stefan Zander, Philipp Prenner
 */
public class ResourceTreeInternalFile extends ResourceTreeItem {

    public static final String internal = "internal";

    /**
	 * @param parent
	 */
    public ResourceTreeInternalFile(ResourceTreeCategory parent, String id) {
        super(parent);
        setId(id);
    }

    public Object getResource() {
        return null;
    }

    public String getResourceIdentifier() {
        return getId();
    }

    public String getType() {
        return internal;
    }

    public void componentAdded(DataComponent component) {
    }

    public void componentChanged(DataComponent component) {
    }

    public void componentMoved(DataComponent component) {
    }

    public void componentRemoved(DataComponent component) {
    }

    public void removeSelf() {
        new RemoveResourceItemCommand(this).execute();
    }

    public ResourceTreeItem copyForOtherLD(LearningDesignDataModel lddm) {
        throw new IllegalAccessError("not suitable for internal files");
    }
}
