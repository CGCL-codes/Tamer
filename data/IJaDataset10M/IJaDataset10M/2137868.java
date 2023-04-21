package com.loribel.commons.gui.bo;

import java.awt.Component;
import java.awt.Container;
import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import com.loribel.commons.abstraction.GB_EnabledSet;
import com.loribel.commons.abstraction.GB_MyPropertyChangeListener;
import com.loribel.commons.abstraction.GB_MyPropertyOwner;
import com.loribel.commons.abstraction.GB_Unregisterable;
import com.loribel.commons.business.abstraction.GB_SimpleBusinessObject;
import com.loribel.commons.util.CTools;

/**
 * Class to enabled / disabled component according values of a property.
 *
 * @author Gregory Borelli
 */
public class GB_BOCompEnabledMgr implements GB_MyPropertyChangeListener, GB_Unregisterable {

    private GB_SimpleBusinessObject bo;

    private String propertyName;

    private Collection valuesToEnabled;

    /** List of Component and/or GB_EnabledSet */
    private Collection compToEnabled;

    /** List of Component and/or GB_EnabledSet */
    private Collection compToDisabled;

    public GB_BOCompEnabledMgr(GB_SimpleBusinessObject a_bo, String a_propertyName, Object[] a_values) {
        bo = a_bo;
        propertyName = a_propertyName;
        valuesToEnabled = CTools.toList(a_values);
        init();
    }

    public GB_BOCompEnabledMgr(GB_SimpleBusinessObject a_bo, String a_propertyName, Object a_value) {
        bo = a_bo;
        propertyName = a_propertyName;
        valuesToEnabled = new ArrayList(1);
        valuesToEnabled.add(a_value);
        init();
    }

    private void init() {
        compToEnabled = new ArrayList();
        compToDisabled = new ArrayList();
        ((GB_MyPropertyOwner) bo).addMyPropertyChangeListener(this);
    }

    public void addPropertyToEnabled(Container a_container, String a_propertyName) {
        Component[] l_comps = GB_BOPanelTools.getComponents(a_container, a_propertyName);
        CTools.addAll(compToEnabled, l_comps);
    }

    public void addPropertyToDisabled(Container a_container, String a_propertyName) {
        Component[] l_comps = GB_BOPanelTools.getComponents(a_container, a_propertyName);
        CTools.addAll(compToDisabled, l_comps);
    }

    public void addComponentToEnabled(Component a_comp) {
        compToEnabled.add(a_comp);
    }

    public void addElementToEnabled(GB_EnabledSet a_comp) {
        compToEnabled.add(a_comp);
    }

    public void addComponentToDisabled(Component a_comp) {
        compToDisabled.add(a_comp);
    }

    public void addElementToDisabled(GB_EnabledSet a_comp) {
        compToDisabled.add(a_comp);
    }

    public boolean unregister() {
        ((GB_MyPropertyOwner) bo).removeMyPropertyChangeListener(this);
        return false;
    }

    /**
     * When the value change, update the components (enabled / disabled)
     */
    public void myPropertyChange(PropertyChangeEvent a_event) {
        updateComponents();
    }

    public void updateComponents() {
        Object l_value = bo.getPropertyValue(propertyName);
        boolean l_enabled = false;
        if (valuesToEnabled.contains(l_value)) {
            l_enabled = true;
        }
        enabledComponents(compToEnabled, l_enabled);
        enabledComponents(compToDisabled, !l_enabled);
    }

    private void enabledComponents(Collection a_comps, boolean a_enabled) {
        if (a_comps == null) {
            return;
        }
        Iterator it = a_comps.iterator();
        while (it.hasNext()) {
            Object l_item = it.next();
            if (l_item instanceof GB_EnabledSet) {
                ((GB_EnabledSet) l_item).setEnabled(a_enabled);
            } else {
                ((Component) l_item).setEnabled(a_enabled);
            }
        }
    }
}
