package de.mpiwg.vspace.java.transformation.ids;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.ecore.EObject;

public class ElementIDSetter {

    private Map<String, IIdSetter> idSetters;

    public ElementIDSetter() {
        idSetters = new HashMap<String, IIdSetter>();
        IConfigurationElement[] configs = Platform.getExtensionRegistry().getConfigurationElementsFor(de.mpiwg.vspace.java.transformation.internal.ExtensionPointIDs.EOBJECT_IDSETTER);
        for (IConfigurationElement e : configs) {
            Object o = null;
            try {
                o = e.createExecutableExtension("class");
            } catch (CoreException e1) {
                e1.printStackTrace();
                continue;
            }
            if (o != null) {
                IIdSetter setter = (IIdSetter) o;
                idSetters.put(setter.getRegisteredClass(), setter);
            }
        }
    }

    public void setIds(EObject startObject, String parentId) {
        IIdSetter setter = idSetters.get(startObject.eClass().getName());
        if (setter == null) return;
        String id = setter.setId(startObject, parentId);
        List<? extends EObject> children = setter.getChildren(startObject);
        if (children != null) {
            for (EObject child : children) {
                setIds(child, id);
            }
        }
    }
}
