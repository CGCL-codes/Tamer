package org.dwgsoftware.raistlin.composition.model.impl;

import org.apache.avalon.framework.context.ContextException;
import org.dwgsoftware.raistlin.composition.model.ContainmentModel;
import org.dwgsoftware.raistlin.composition.model.ModelException;
import org.dwgsoftware.raistlin.composition.provider.ComponentContext;
import org.dwgsoftware.raistlin.meta.info.EntryDescriptor;
import org.dwgsoftware.raistlin.util.i18n.ResourceManager;
import org.dwgsoftware.raistlin.util.i18n.Resources;

/**
 * Default implementation of a the context entry import model.
 *
 * @author <a href="mailto:dev@avalon.apache.org">Avalon Development Team</a>
 * @version $Revision: 1.1 $ $Date: 2005/09/06 00:58:18 $
 */
public class DefaultImportModel extends DefaultEntryModel {

    private static final Resources REZ = ResourceManager.getPackageResources(DefaultImportModel.class);

    private final EntryDescriptor m_descriptor;

    private final String m_key;

    private final ComponentContext m_context;

    private Object m_value;

    /**
    * Creation of a new context entry import model.
    *
    * @param descriptor the context entry descriptor
    * @param key the container scoped key
    * @param context the containment context
    */
    public DefaultImportModel(EntryDescriptor descriptor, String key, ComponentContext context) throws ModelException {
        super(descriptor);
        if (key == null) {
            throw new NullPointerException("key");
        }
        if (context == null) {
            throw new NullPointerException("context");
        }
        m_key = key;
        m_context = context;
        m_descriptor = descriptor;
        if (!DefaultContextModel.isaStandardKey(key)) {
            final String error = REZ.getString("context.non-standard-key.error", key);
            throw new ModelException(error);
        }
        if (!descriptor.isVolatile()) {
            m_value = getValue();
        }
    }

    /**
    * Return the context entry value.
    * 
    * @return the context entry value
    */
    public Object getValue() throws ModelException {
        if (m_value != null) return m_value;
        return getStandardEntry(m_key);
    }

    private Object getStandardEntry(String key) {
        if (key.startsWith("urn:raistlin:")) {
            return getStandardAvalonEntry(key);
        } else if (key.startsWith("urn:composition:")) {
            return getStandardCompositionEntry(key);
        } else {
            final String error = "Unknown key [" + key + "]";
            throw new IllegalArgumentException(error);
        }
    }

    private Object getStandardAvalonEntry(String key) {
        if (key.equals(ComponentContext.NAME_KEY)) {
            return m_context.getName();
        } else if (key.equals(ComponentContext.PARTITION_KEY)) {
            return m_context.getPartitionName();
        } else if (key.equals(ComponentContext.CLASSLOADER_KEY)) {
            return m_context.getClassLoader();
        } else if (key.equals(ComponentContext.HOME_KEY)) {
            return m_context.getHomeDirectory();
        } else if (key.equals(ComponentContext.TEMP_KEY)) {
            return m_context.getTempDirectory();
        }
        return null;
    }

    private Object getStandardCompositionEntry(String key) {
        if (key.equals(ContainmentModel.KEY)) {
            return m_context.getContainmentModel();
        } else {
            try {
                return m_context.getSystemContext().get(key);
            } catch (ContextException e) {
                return null;
            }
        }
    }
}
