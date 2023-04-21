package org.pescuma.jfg.gui.swt;

import org.pescuma.jfg.Attribute;

public class SWTTextWithOptionsBuilder implements SWTWidgetBuilder {

    @Override
    public boolean accept(Attribute attrib) {
        Object type = attrib.getType();
        return type == String.class || "text_with_options".equals(type);
    }

    @Override
    public SWTGuiWidget build(Attribute attrib, JfgFormData data, InnerBuilder innerBuilder) {
        return new TextWithOptionsSWTWidget(attrib, data);
    }
}
