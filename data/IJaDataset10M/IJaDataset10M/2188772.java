package com.sitescape.team.module.definition.ws;

import org.dom4j.Element;
import com.sitescape.team.remoting.ws.model.CustomLongArrayField;
import com.sitescape.util.StringUtil;

public class ElementBuilderCommaSeparatedLong extends AbstractElementBuilder {

    protected boolean build(Element element, com.sitescape.team.remoting.ws.model.DefinableEntity entityModel, Object obj, String dataElemType, String dataElemName) {
        if (obj != null) {
            String value = obj.toString();
            if (element != null) element.setText(value);
            if (entityModel != null) {
                String[] strings = StringUtil.split(value);
                Long[] longs = new Long[strings.length];
                for (int i = 0; i < longs.length; i++) longs[i] = Long.parseLong(strings[i]);
                entityModel.addCustomLongArrayField(new CustomLongArrayField(dataElemName, dataElemType, longs));
            }
        }
        return true;
    }
}
