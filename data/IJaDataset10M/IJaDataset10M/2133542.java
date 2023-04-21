package net.sourceforge.hlm.impl.visual.templates.text;

import net.sourceforge.hlm.impl.generic.*;
import net.sourceforge.hlm.util.storage.*;
import net.sourceforge.hlm.visual.templates.text.*;

public class TextTemplateStringImpl extends TextTemplateItemImpl implements TextTemplateString {

    public TextTemplateStringImpl(StoredObject storedObject) {
        super(storedObject);
    }

    public StringReferenceImpl getString() {
        if (this.string == null) {
            this.string = new StringReferenceImpl(this.storedObject, 0);
        }
        return this.string;
    }

    private StringReferenceImpl string;
}
