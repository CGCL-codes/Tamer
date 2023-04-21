package org.dmd.features.extgwt.examples.modules.shared.features.security.generated.types.adapters;

import org.dmd.dmc.presentation.DmcAdapterIF;
import org.dmd.dmc.DmcAttribute;
import org.dmd.dmc.DmcAttributeInfo;
import org.dmd.dms.generated.types.DmcTypeModifierMV;
import org.dmd.features.extgwt.examples.modules.shared.features.security.generated.types.DmcTypeUserGroupREFMV;

@SuppressWarnings("serial")
public class UserGroupREFMVAdapter extends DmcTypeUserGroupREFMV implements DmcAdapterIF {

    transient DmcTypeUserGroupREFMV existingValue;

    public UserGroupREFMVAdapter(DmcAttributeInfo ai) {
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
        if (existingValue == null) value = null; else value = existingValue.getMVCopy();
    }

    @Override
    public void setExisting(DmcAttribute<?> attr) {
        existingValue = (DmcTypeUserGroupREFMV) attr;
        if (existingValue != null) value = existingValue.getMVCopy();
    }

    @Override
    public boolean valueChanged() {
        return (valueChangedMV(existingValue, this));
    }

    @Override
    public void addMods(DmcTypeModifierMV mods) {
        addModsMV(mods, existingValue, this);
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
