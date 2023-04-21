package org.makumba.view.jsptaglib;

import java.util.Vector;
import javax.servlet.jsp.tagext.TagData;
import javax.servlet.jsp.tagext.TagExtraInfo;
import javax.servlet.jsp.tagext.VariableInfo;

public class AttrTEI extends TagExtraInfo {

    public VariableInfo[] getVariableInfo(TagData data) {
        Vector v = new Vector();
        String var = data.getAttributeString("var");
        if (var != null) v.addElement(new VariableInfo(var, "java.lang.Object", true, VariableInfo.AT_BEGIN));
        var = data.getAttributeString("exceptionVar");
        if (var != null) v.addElement(new VariableInfo(var, "java.lang.Throwable", true, VariableInfo.AT_BEGIN));
        return CountTEI.vector2VarInfo(v);
    }
}
