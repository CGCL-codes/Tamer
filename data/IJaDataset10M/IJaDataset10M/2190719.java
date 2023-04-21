package net.sourceforge.hlm.impl.visual.templates;

import java.util.*;
import net.sourceforge.hlm.impl.*;
import net.sourceforge.hlm.impl.generic.*;
import net.sourceforge.hlm.library.parameters.*;
import net.sourceforge.hlm.util.storage.*;
import net.sourceforge.hlm.visual.templates.*;

public abstract class VisualTemplateImpl extends HLMObjectImpl implements VisualTemplate {

    public VisualTemplateImpl(StoredObject storedObject) {
        super(storedObject);
    }

    public ParenthesesStyleImpl getParentheses() {
        if (this.parentheses == null) {
            this.parentheses = new ParenthesesStyleImpl(this.storedObject, 0);
        }
        return this.parentheses;
    }

    public StringReferenceImpl getElementName() {
        if (this.elementName == null) {
            this.elementName = new StringReferenceImpl(this.storedObject, 0);
        }
        return this.elementName;
    }

    public void setShow(boolean show) {
        this.storedObject.setIntBit(1, 0, show);
    }

    public boolean getShow() {
        return this.storedObject.getIntBit(1, 0);
    }

    public abstract Iterator<Parameter> getParameters();

    private ParenthesesStyleImpl parentheses;

    private StringReferenceImpl elementName;
}
