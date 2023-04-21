package com.myapp.tools.media.renamer.model.naming.impl;

import com.myapp.tools.media.renamer.model.IRenamable;
import com.myapp.tools.media.renamer.model.naming.AbstractNamePart;

/**
 * the part of the new file name representing the Beschreibung
 * 
 * @author andre
 * 
 */
public class BeschreibungNamePart extends AbstractNamePart {

    @Override
    protected String getFormattedStringImpl(IRenamable f) {
        builder.setLength(0);
        return builder.append(cfg.getBeschreibungPrefix()).append(getRawValue(f)).append(cfg.getBeschreibungSuffix()).toString();
    }

    @Override
    public Object getRawValue(IRenamable file) {
        String beschr = file.getBeschreibung();
        return beschr == null ? cfg.getDefaultBeschreibung() : beschr;
    }

    @Override
    public boolean isEditable() {
        return true;
    }

    @Override
    public boolean setRawValue(Object value, IRenamable file) {
        assert value != null;
        if (value instanceof String) {
            file.setBeschreibung((String) value);
            return true;
        }
        return false;
    }

    @Override
    public String getPropertyName() {
        return COLUMN_NAME_BESCHREIBUNG;
    }
}
