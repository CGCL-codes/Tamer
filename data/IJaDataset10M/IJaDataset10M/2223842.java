package com.volantis.mcs.xml.schema.impl.model;

import com.volantis.mcs.xml.schema.model.ContentModel;
import com.volantis.mcs.xml.schema.impl.model.ElementReferenceImpl;
import com.volantis.mcs.xml.schema.model.ElementSchema;
import com.volantis.mcs.xml.schema.model.ElementType;
import com.volantis.mcs.xml.schema.model.ElementReference;

/**
 * Identifies an element within the validation mechanism.
 */
public class ElementSchemaImpl extends ElementReferenceImpl implements ElementSchema {

    private final ElementType elementType;

    private ContentModel model;

    private boolean useAnywhere;

    private boolean transparent;

    public ElementSchemaImpl(ElementType type) {
        this.elementType = type;
    }

    public ElementSchemaImpl(ElementType type, ContentModel model) {
        this(type);
        this.model = model;
    }

    public void setContentModel(ContentModel model) {
        this.model = model;
    }

    public ContentModel getContentModel() {
        return model;
    }

    public ElementType getElementType() {
        return elementType;
    }

    public boolean getUseAnywhere() {
        return useAnywhere;
    }

    public void setUseAnywhere(boolean useAnywhere) {
        this.useAnywhere = useAnywhere;
    }

    public boolean isTransparent() {
        return transparent;
    }

    public void setTransparent(boolean transparent) {
        this.transparent = transparent;
    }

    public ElementReference getElementReference() {
        return this;
    }
}
