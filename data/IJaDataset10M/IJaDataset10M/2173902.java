package org.jato.tags;

import java.util.*;
import org.jato.*;
import org.jdom.*;

public class InvokeParamScriptTag extends DefaultParamScriptTag {

    private String fMethodName;

    private String fClassName;

    private String fInVarName;

    public InvokeParamScriptTag() {
    }

    public InvokeParamScriptTag(String name) {
        fMethodName = name;
    }

    public InvokeParamScriptTag(String name, String className) {
        fMethodName = name;
        fClassName = className;
    }

    protected StringBuffer describeAttributes(StringBuffer buf) {
        buf = super.describeAttributes(buf);
        attr(buf, "invoke", fMethodName);
        if (fClassName != null) attr(buf, "class", fClassName);
        if (fInVarName != null) attr(buf, "var", fInVarName);
        return buf;
    }

    public String getMethodName() {
        return fMethodName;
    }

    public void setMethodName(String name) {
        fMethodName = name;
    }

    public String getClassName() {
        return fClassName;
    }

    public void setClassName(String name) {
        fClassName = name;
    }

    public String getInputVariableName() {
        return fInVarName;
    }

    public void setInputVariableName(String name) {
        fInVarName = name;
    }

    protected Parameter getParameter(Interpreter jato, Class thisClass, Object thisObj, Element xmlIn, Element xmlOut) throws JatoException {
        if (fClassName == null) {
            thisObj = getInvokingObject(jato, thisClass, thisObj, xmlIn, xmlOut);
            thisClass = (thisObj != null) ? thisObj.getClass() : null;
        }
        thisObj = getMethodValue(fMethodName, fClassName, jato, thisClass, thisObj, xmlIn, xmlOut);
        thisClass = (thisObj != null) ? thisObj.getClass() : null;
        return new Parameter(thisClass, thisObj);
    }

    protected Object getInvokingObject(Interpreter jato, Class thisClass, Object thisObj, Element xmlIn, Element xmlOut) throws JatoException {
        return (fInVarName == null) ? thisObj : jato.getVariable(fInVarName);
    }

    public static ScriptTag getTemplate() throws JatoException {
        ObjectScriptTag init = new ObjectScriptTag(InvokeParamScriptTag.class);
        init.setPublishable(true);
        init.setKey("child-tag");
        init.addChild(new PathPropertyScriptTag("methodName", "@invoke"));
        init.addChild(new PathPropertyScriptTag("className", "@class"));
        init.addChild(new PathPropertyScriptTag("inputVariableName", "@var"));
        init.addChild(getParamTypeScriptTag());
        init.addChild(getParamNameScriptTag());
        init.addChild(getPublishTemplate());
        InvokeParamScriptTag tag = new InvokeParamScriptTag();
        tag.setTemplateInfo("path-exists('@invoke')", init);
        return tag;
    }
}
