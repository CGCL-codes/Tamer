package org.middleheaven.ui.web.tags;

import javax.servlet.jsp.tagext.TagData;
import javax.servlet.jsp.tagext.TagExtraInfo;
import javax.servlet.jsp.tagext.VariableInfo;

public class InvalidationReasonsListTagExtraInfo extends TagExtraInfo {

    public VariableInfo[] getVariableInfo(TagData data) {
        VariableInfo[] rc = new VariableInfo[1];
        String exportedName = (String) data.getAttribute("var");
        rc[0] = new VariableInfo(exportedName, "java.lang.Object", true, VariableInfo.NESTED);
        return rc;
    }
}
