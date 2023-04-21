package com.ecmdeveloper.plugin.model;

import java.util.Collection;
import com.ecmdeveloper.plugin.core.model.ICodeModule;
import com.ecmdeveloper.plugin.core.model.IObjectStoreItem;
import com.filenet.api.constants.PropertyNames;
import com.filenet.api.core.Document;
import com.filenet.api.core.IndependentlyPersistableObject;
import com.filenet.api.core.VersionSeries;

/**
 * 
 * @author Ricardo Belfor
 *
 */
public class CodeModule extends ObjectStoreItem implements ICodeModule {

    protected VersionSeries versionSeries;

    protected Document document;

    public CodeModule(Object versionSeries, IObjectStoreItem parent, ObjectStore objectStore) {
        super(parent, objectStore);
        this.versionSeries = (VersionSeries) versionSeries;
        refresh();
    }

    @Override
    public IndependentlyPersistableObject getObjectStoreObject() {
        return document;
    }

    @Override
    public Collection<IObjectStoreItem> getChildren() {
        return null;
    }

    @Override
    public void refresh() {
        versionSeries.refresh(new String[] { PropertyNames.NAME, PropertyNames.ID, PropertyNames.CURRENT_VERSION });
        name = ((Document) versionSeries.get_CurrentVersion()).get_Name();
        id = versionSeries.get_Id().toString();
        versionSeries.fetchProperties(new String[] { PropertyNames.CURRENT_VERSION });
        document = (Document) versionSeries.get_CurrentVersion();
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Object getAdapter(Class adapter) {
        if (adapter.equals(Document.class)) {
            versionSeries.fetchProperties(new String[] { PropertyNames.CURRENT_VERSION });
            return versionSeries.get_CurrentVersion();
        }
        return super.getAdapter(adapter);
    }

    @Override
    public String getClassName() {
        if (document != null) {
            return document.getClassName();
        }
        return null;
    }
}
