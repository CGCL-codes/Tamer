package com.wgo.precise.client.ui.view.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource;
import org.eclipse.ui.views.properties.PropertyDescriptor;

/**
 * @author petterei
 *
 * @version $Id: ListPropertySource.java,v 1.1 2006-01-23 19:58:37 petterei Exp $
 */
public class ListPropertySource implements IPropertySource {

    private String label = "";

    private Collection<IAdaptable> items = null;

    private Map<IAdaptable, IPropertySource> sources = new Hashtable<IAdaptable, IPropertySource>();

    public ListPropertySource(Collection<IAdaptable> items, String... label) {
        if (0 < label.length) {
            this.label = label[0];
        }
        this.items = items;
    }

    public Object getEditableValue() {
        return this;
    }

    public IPropertyDescriptor[] getPropertyDescriptors() {
        List<IPropertyDescriptor> descriptors = new ArrayList<IPropertyDescriptor>(items.size());
        for (IAdaptable adaptable : items) {
            IPropertySource source = (IPropertySource) adaptable.getAdapter(IPropertySource.class);
            if (null != source) {
                IPropertyDescriptor descriptor = new PropertyDescriptor(adaptable, "");
                descriptors.add(descriptor);
                sources.put(adaptable, source);
            }
        }
        IPropertyDescriptor[] descriptorArray = new IPropertyDescriptor[descriptors.size()];
        int i = 0;
        for (IPropertyDescriptor descriptor : descriptors) {
            descriptorArray[i] = descriptor;
            i++;
        }
        return descriptorArray;
    }

    public Object getPropertyValue(Object id) {
        return sources.get(id);
    }

    public boolean isPropertySet(Object id) {
        return sources.containsKey(id);
    }

    public void resetPropertyValue(Object id) {
    }

    public void setPropertyValue(Object id, Object value) {
    }

    @Override
    public String toString() {
        return label;
    }

    public Collection<IAdaptable> getItems() {
        return items;
    }
}
