package org.pescuma.jfg.gui.swt;

import java.util.List;
import org.pescuma.jfg.Attribute;

public class SWTInlineObjectListBuilder implements SWTWidgetBuilder {

    @Override
    public boolean accept(Attribute attrib) {
        if (attrib.canWrite()) return false;
        Object type = attrib.getType();
        return type == List.class || "inline_obj_list".equals(type);
    }

    @Override
    public SWTGuiWidget build(Attribute attrib, JfgFormData data, InnerBuilder innerBuilder) {
        if (!innerBuilder.canBuildInnerAttribute()) return null;
        if (attrib.canWrite()) System.out.println("[JFG] Creating GUI for read/write object. " + "I'll only change the object in place and will not check for changes in it!");
        return new InlineObjectListSWTWidget(attrib, data);
    }
}
