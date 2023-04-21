package com.basemovil.vc.view.settings;

import bm.core.io.SerializerOutputStream;
import bm.core.io.SerializationException;
import com.basemovil.vc.ViewCompilerException;
import com.basemovil.vc.ViewCompiler;

/**
 * A setting in a settings view.
 *
 * @author <a href="mailto:narciso@elondra.com">Narciso Cerezo</a>
 * @version $Revision$
 */
public class Setting {

    private String title;

    private String key;

    private boolean defaultValue;

    private String onlyIf;

    private String unless;

    public String getTitle() {
        return title;
    }

    public void setTitle(final String title) {
        this.title = title;
    }

    public String getKey() {
        return key;
    }

    public void setKey(final String key) {
        this.key = key;
    }

    public boolean isDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(final boolean defaultValue) {
        this.defaultValue = defaultValue;
    }

    public String getOnlyIf() {
        return onlyIf;
    }

    public void setOnlyIf(final String onlyIf) {
        this.onlyIf = onlyIf;
    }

    public String getUnless() {
        return unless;
    }

    public void setUnless(final String unless) {
        this.unless = unless;
    }

    public void store(final SerializerOutputStream out) throws ViewCompilerException {
        try {
            out.writeByte((byte) 1);
            out.writeString(ViewCompiler.escape(title));
            out.writeString(key);
            out.writeBoolean(defaultValue);
            out.writeNullableString(onlyIf);
            out.writeNullableString(unless);
        } catch (SerializationException e) {
            throw new ViewCompilerException(e);
        }
    }
}
