package com.starlight.locale;

import com.starlight.IOKit;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Locale;

/**
 * A localized string.
 */
public class TextResourceKey extends ResourceKey<String> {

    static final long serialVersionUID = 0L;

    private String default_text;

    /**
	 * FOR EXTERNALIZATION ONLY!!!
	 */
    public TextResourceKey() {
        super(true);
        assert IOKit.isBeingDeserialized();
    }

    public TextResourceKey(String default_text) {
        this(default_text, false);
    }

    protected TextResourceKey(String default_text, boolean ignore_resource_list_class) {
        super(ignore_resource_list_class);
        if (default_text == null) {
            throw new IllegalArgumentException("Default text cannot be null");
        }
        this.default_text = default_text;
    }

    @Override
    public String toString() {
        return getValue();
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        TextResourceKey that = (TextResourceKey) o;
        if (!default_text.equals(that.default_text)) return false;
        return true;
    }

    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + default_text.hashCode();
        return result;
    }

    @Override
    protected String convertToValue(String string, Locale locale) {
        return string;
    }

    String getDefaultValue() {
        return default_text;
    }

    public void writeExternal(ObjectOutput out) throws IOException {
        super.writeExternal(out);
        out.writeByte(0);
        IOKit.writeString(out, default_text);
    }

    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        super.readExternal(in);
        in.readByte();
        default_text = IOKit.readString(in);
    }
}
