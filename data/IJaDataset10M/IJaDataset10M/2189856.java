package org.j2eebuilder.view;

import javax.servlet.jsp.tagext.TagExtraInfo;
import javax.servlet.jsp.tagext.VariableInfo;
import javax.servlet.jsp.tagext.TagData;
import org.j2eebuilder.util.LogManager;

/**
 * @author Sandeep Dixit
 * @version 1.0 $date 2004/03/12
 * This tag defines variables available to JSPs using any tag
 * extending ComponentTag
 */
public class ComponentTagExtraInfo extends TagExtraInfo {

    public VariableInfo[] getVariableInfo(TagData tagData) {
        return new VariableInfo[] { new VariableInfo(org.j2eebuilder.BuilderHelperBean.getCurrentInstance().TAG_CONTEXT_ATTRIBUTE_ID_OF_COMPONENT_DEFINITION, "org.j2eebuilder.ComponentDefinition", true, VariableInfo.NESTED) };
    }
}
