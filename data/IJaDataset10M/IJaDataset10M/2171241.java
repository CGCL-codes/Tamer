package com.germinus.xpression.cms.taglib;

import javax.servlet.jsp.tagext.TagExtraInfo;
import javax.servlet.jsp.tagext.VariableInfo;
import javax.servlet.jsp.tagext.TagData;

/**
 * Used to declare the property value as a JSP scripting variable
 * 
 * @author acheca
 */
public class GetContentsByTypeTei extends TagExtraInfo {

    /**
     * Return information about the scripting variables to be created.
     */
    public VariableInfo[] getVariableInfo(TagData data) {
        String type = "java.util.List";
        return new VariableInfo[] { new VariableInfo(data.getAttributeString("var"), type, true, VariableInfo.AT_END) };
    }
}
