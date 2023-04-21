package org.dmd.features.extgwt.client;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import com.extjs.gxt.ui.client.core.FastMap;
import com.extjs.gxt.ui.client.core.FastSet;
import com.extjs.gxt.ui.client.data.ChangeEvent;
import com.extjs.gxt.ui.client.data.ChangeEventSource;
import com.extjs.gxt.ui.client.data.ChangeEventSupport;
import com.extjs.gxt.ui.client.data.ChangeListener;
import com.extjs.gxt.ui.client.data.Model;
import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.data.PropertyChangeEvent;
import com.extjs.gxt.ui.client.util.Util;
import org.dmd.dmc.DmcAttribute;
import org.dmd.dmc.DmcAttributeChangeListenerIF;
import org.dmd.dmc.DmcContainerIF;
import org.dmd.dmc.DmcListenerManagerIF;
import org.dmd.dmc.DmcObject;
import org.dmd.dmc.DmcValueException;
import org.dmd.dmc.DmcValueExceptionSet;
import org.dmd.dmc.types.DmcTypeModifier;
import org.dmd.dmc.types.Modifier;
import org.dmd.dmp.shared.generated.dmo.DMPEventDMO;
import org.dmd.dmp.shared.generated.dmo.DmpDMSAG;
import org.dmd.dms.generated.dmo.MetaDMSAG;
import org.dmd.dms.generated.types.DmcTypeModifierMV;

/**
 * The DmoExtGWTWrapperBase provides a common base class for auto generated Dark Matter
 * Wrapper (DMW) objects that allow Dark MAtter Objects (DMOs) to be used ExtGWT data
 * models.
 * @param <DMO> 
 */
public class DmoExtGWTWrapperBase<DMO extends DmcObject> implements Model, ModelData, DmcContainerIF {

    protected DMO core;

    protected transient ChangeEventSupport changeEventSupport;

    boolean deleted;

    public DmoExtGWTWrapperBase() {
        deleted = false;
    }

    @SuppressWarnings("unchecked")
    protected DmoExtGWTWrapperBase(DmcObject obj) {
        core = (DMO) obj;
    }

    /**
	 * Sets the object's deleted flag to true.
	 */
    public void setDeleted() {
        deleted = true;
        fireUpdateEvent();
    }

    /**
	 * @return The object's deleted flag.
	 */
    public boolean getDeleted() {
        return (deleted);
    }

    public void wrap(DMO obj) {
        core = obj;
    }

    public DMO getDmcObject() {
        return (core);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <X> X get(String property) {
        DmcAttribute attr = core.get(property);
        if (attr == null) return (null);
        return (X) (attr.getSV());
    }

    public String toOIF() {
        if (core != null) return (core.toOIF());
        return ("");
    }

    public String toOIF(int padding) {
        if (core != null) return (core.toOIF(padding));
        return ("");
    }

    public void setModifier(DmcTypeModifierMV mods) {
        core.setModifier(mods);
    }

    public DmcTypeModifier getModifier() {
        return (core.getModifier());
    }

    public void applyModifier(DmcTypeModifier mods) throws DmcValueExceptionSet, DmcValueException {
        core.applyModifier(mods);
        Iterator<Modifier> it = mods.getMV();
        while (it.hasNext()) {
            Modifier mod = it.next();
            if (mod.getAttribute().getSV() != null) {
                notifyPropertyChanged(mod.getAttributeName(), mod.getAttribute().getSV().toString(), "");
            }
        }
        fireUpdateEvent();
    }

    public void applyModifierFromEvent(DMPEventDMO event) throws DmcValueExceptionSet, DmcValueException {
        DmcTypeModifier mods = (DmcTypeModifier) event.get(MetaDMSAG.__modify);
        if (mods != null) core.applyModifier(mods);
    }

    /**
	 * This is a convenience function to allow notification that something has changed
	 * on this object. This is required because we don't actually use the set() mechanisms
	 * provided by the Model framework.
	 */
    protected void fireUpdateEvent() {
        notify(new ChangeEvent(ChangeEventSource.Update, this));
    }

    @Override
    public void addChangeListener(ChangeListener... listener) {
        if (changeEventSupport == null) changeEventSupport = new ChangeEventSupport();
        changeEventSupport.addChangeListener(listener);
    }

    @Override
    public void notify(ChangeEvent event) {
        if (changeEventSupport != null) changeEventSupport.notify(event);
    }

    @Override
    public void removeChangeListener(ChangeListener... listener) {
        if (changeEventSupport != null) changeEventSupport.removeChangeListener(listener);
    }

    @Override
    public void removeChangeListeners() {
        if (changeEventSupport != null) changeEventSupport.removeChangeListeners();
    }

    @Override
    public void setSilent(boolean silent) {
        if (changeEventSupport != null) changeEventSupport.setSilent(silent);
    }

    public void addChangeListener(List<ChangeListener> listeners) {
        for (ChangeListener listener : listeners) {
            changeEventSupport.addChangeListener(listener);
        }
    }

    public boolean isSilent() {
        return changeEventSupport.isSilent();
    }

    protected void fireEvent(int type) {
        notify(new ChangeEvent(type, this));
    }

    protected void fireEvent(int type, Model item) {
        notify(new ChangeEvent(type, this, item));
    }

    protected void notifyPropertyChanged(String name, Object value, Object oldValue) {
        if (!Util.equalWithNull(value, oldValue)) {
            notify(new PropertyChangeEvent(Update, this, name, oldValue, value));
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public Map<String, Object> getProperties() {
        Map<String, Object> newMap = new FastMap<Object>();
        if (core.getAttributes() != null) {
            for (DmcAttribute attr : core.getAttributes().values()) {
                newMap.put(attr.getName(), attr);
            }
        }
        return newMap;
    }

    @Override
    public Collection<String> getPropertyNames() {
        Set<String> set = new FastSet();
        if (core.getAttributes() != null) {
            set.addAll(core.getAttributeNames(true));
        }
        return set;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <X> X remove(String property) {
        return (X) (core.rem(property));
    }

    @Override
    public <X> X set(String property, X value) {
        return null;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void setDmcObject(DmcObject obj) {
        core = (DMO) obj;
        obj.setContainer(this);
    }
}
