package net.sourceforge.hlm.impl.visual.templates.text;

import java.util.*;
import net.sourceforge.hlm.generic.annotations.*;
import net.sourceforge.hlm.impl.*;
import net.sourceforge.hlm.library.parameters.*;
import net.sourceforge.hlm.util.storage.*;
import net.sourceforge.hlm.visual.templates.text.*;

public abstract class TextTemplateItemImpl extends HLMObjectImpl implements TextTemplateItem {

    public TextTemplateItemImpl(StoredObject storedObject) {
        super(storedObject);
    }

    public static TextTemplateItemImpl createWrapper(StoredObject storedObject) {
        if (storedObject == null) {
            return null;
        }
        if (storedObject.getTypeID() == Id.VISUAL_TEMPLATE_ITEM) {
            switch(storedObject.getSubTypeID()) {
                case SubId.VisualTemplateItem.STRING:
                    return new TextTemplateStringImpl(storedObject);
                case SubId.VisualTemplateItem.PARAMETER:
                    return new TextTemplateParameterImpl(storedObject);
                case SubId.VisualTemplateItem.SYMBOL:
                    return new TextTemplateSymbolImpl(storedObject);
            }
        }
        throw new IllegalArgumentException();
    }

    protected Iterator<Parameter> getParameters() {
        return null;
    }
}
