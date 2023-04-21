package org.dmd.dms.generated.types.adapters;

import org.dmd.dmc.presentation.DmcAdapterIF;
import org.dmd.dmc.DmcAttribute;
import org.dmd.dmc.DmcAttributeInfo;
import org.dmd.dms.generated.types.DmcTypeModifierMV;
import org.dmd.dms.generated.types.DmcTypeClassDefinitionREFSV;

@SuppressWarnings("serial")
public class ClassDefinitionREFSVAdapter extends DmcTypeClassDefinitionREFSV implements DmcAdapterIF {

    transient DmcTypeClassDefinitionREFSV existingValue;

    public ClassDefinitionREFSVAdapter(DmcAttributeInfo ai) {
        attrInfo = ai;
    }

    @Override
    public void setEmpty() {
        value = null;
    }

    @Override
    public boolean hasValue() {
        if (value == null) return (false);
        return (true);
    }

    @Override
    public void resetToExisting() {
        if (existingValue == null) value = null; else value = existingValue.getSVCopy();
    }

    @Override
    public void setExisting(DmcAttribute<?> attr) {
        existingValue = (DmcTypeClassDefinitionREFSV) attr;
        if (existingValue != null) value = existingValue.getSVCopy();
    }

    @Override
    public boolean valueChanged() {
        return (valueChangedSV(existingValue, this));
    }

    @Override
    public void addMods(DmcTypeModifierMV mods) {
        addModsSV(mods, existingValue, this);
    }

    @Override
    public DmcAttribute<?> getExisting() {
        return (existingValue);
    }

    @Override
    public Object getValue() {
        return (value);
    }
}
