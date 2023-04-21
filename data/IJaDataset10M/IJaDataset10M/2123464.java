package net.sourceforge.hlm.impl.visual.templates.layout;

import java.util.*;
import net.sourceforge.hlm.generic.*;
import net.sourceforge.hlm.generic.annotations.*;
import net.sourceforge.hlm.impl.*;
import net.sourceforge.hlm.library.parameters.*;
import net.sourceforge.hlm.util.iterator.*;
import net.sourceforge.hlm.util.storage.*;
import net.sourceforge.hlm.visual.templates.layout.*;

public abstract class LayoutImpl extends HLMObjectImpl implements Layout {

    public LayoutImpl(StoredObject storedObject) {
        super(storedObject);
    }

    public void setSize(int size) {
        this.storedObject.setInt(0, size);
    }

    public int getSize() {
        return this.storedObject.getInt(0);
    }

    public void setAlignment(int alignment) {
        this.storedObject.setInt(1, alignment);
    }

    public int getAlignment() {
        return this.storedObject.getInt(1);
    }

    protected Iterator<Parameter> getParameters() {
        return new ParameterIteratorImpl();
    }

    protected Iterator<SelectablePlaceholder<Layout>> getSubLayouts() {
        return null;
    }

    public static final int FIRST_CUSTOM_INT = 2;

    public static final int FIRST_CUSTOM_STRING = 0;

    public static final int FIRST_CUSTOM_CHILD = 0;

    public static final int FIRST_CUSTOM_REFERENCE = 0;

    public static LayoutImpl createWrapper(StoredObject storedObject) {
        if (storedObject == null) {
            return null;
        }
        if (storedObject.getTypeID() == Id.VISUAL_LAYOUT) {
            switch(storedObject.getSubTypeID()) {
                case SubId.VisualLayout.STRING:
                    return new StringLayoutImpl(storedObject);
                case SubId.VisualLayout.PARAMETER:
                    return new ParameterLayoutImpl(storedObject);
                case SubId.VisualLayout.CONCATENATE:
                    return new ConcatenateLayoutImpl(storedObject);
                case SubId.VisualLayout.SUB_SUP:
                    return new SubSupLayoutImpl(storedObject);
                case SubId.VisualLayout.UNDER_OVER:
                    return new UnderOverLayoutImpl(storedObject);
                case SubId.VisualLayout.GRID:
                    return new GridLayoutImpl(storedObject);
                case SubId.VisualLayout.FRAME:
                    return new FrameLayoutImpl(storedObject);
                case SubId.VisualLayout.FRACTION:
                    return new FractionLayoutImpl(storedObject);
                case SubId.VisualLayout.RADICAL:
                    return new RadicalLayoutImpl(storedObject);
            }
        }
        throw new IllegalArgumentException();
    }

    class ParameterIteratorImpl extends NestedIterator<SelectablePlaceholder<Layout>, Parameter> {

        public ParameterIteratorImpl() {
            super(LayoutImpl.this.getSubLayouts());
        }

        @Override
        protected Iterator<Parameter> getInnerIterator(SelectablePlaceholder<Layout> outerItem) {
            Layout layout = outerItem.get();
            if (layout == null) {
                return null;
            } else {
                return ((LayoutImpl) layout).getParameters();
            }
        }
    }
}
