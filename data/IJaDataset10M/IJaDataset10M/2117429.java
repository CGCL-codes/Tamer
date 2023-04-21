package org.makumba.list.tags;

import java.util.Vector;
import javax.servlet.jsp.tagext.TagData;
import javax.servlet.jsp.tagext.TagExtraInfo;
import javax.servlet.jsp.tagext.VariableInfo;

/**
 * Extra information for the attributes
 * 
 * @author Cristian Bogdan
 */
public class AttrTEI extends TagExtraInfo {

    @Override
    public VariableInfo[] getVariableInfo(TagData data) {
        Vector<VariableInfo> v = new Vector<VariableInfo>();
        String var = data.getAttributeString("var");
        if (var != null) {
            v.addElement(new VariableInfo(var, "java.lang.Object", true, VariableInfo.AT_BEGIN));
        }
        var = data.getAttributeString("exceptionVar");
        if (var != null) {
            v.addElement(new VariableInfo(var, "java.lang.Throwable", true, VariableInfo.AT_BEGIN));
        }
        return v.toArray(new VariableInfo[v.size()]);
    }
}
