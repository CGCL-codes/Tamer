package com.jme3.gde.core.filters.impl;

import com.jme3.gde.core.filters.AbstractFilterNode;
import com.jme3.gde.core.filters.FilterNode;
import com.jme3.math.Vector3f;
import com.jme3.post.filters.LightScatteringFilter;
import org.openide.loaders.DataObject;
import org.openide.nodes.Node;
import org.openide.nodes.Sheet;

/**
 *
 * @author normenhansen
 */
@org.openide.util.lookup.ServiceProvider(service = FilterNode.class)
public class JmeLightScatteringFilter extends AbstractFilterNode {

    public JmeLightScatteringFilter() {
    }

    public JmeLightScatteringFilter(LightScatteringFilter filter, DataObject object, boolean readOnly) {
        super(filter);
        this.dataObject = object;
        this.readOnly = readOnly;
    }

    @Override
    protected Sheet createSheet() {
        Sheet sheet = super.createSheet();
        Sheet.Set set = Sheet.createPropertiesSet();
        set.setDisplayName("Blur");
        set.setName(Node.class.getName());
        LightScatteringFilter obj = (LightScatteringFilter) filter;
        if (obj == null) {
            return sheet;
        }
        createFields(LightScatteringFilter.class, set, obj);
        sheet.put(set);
        return sheet;
    }

    @Override
    public Class<?> getExplorerObjectClass() {
        return LightScatteringFilter.class;
    }

    @Override
    public Node[] createNodes(Object key, DataObject dataObject, boolean readOnly) {
        return new Node[] { new JmeLightScatteringFilter((LightScatteringFilter) key, dataObject, readOnly) };
    }
}
