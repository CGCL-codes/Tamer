package com.yubuild.coreman.web.taglib;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.jsp.tagext.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ConstantsTei extends TagExtraInfo {

    private final Log log;

    public ConstantsTei() {
        log = LogFactory.getLog(com.yubuild.coreman.web.taglib.ConstantsTei.class);
    }

    public VariableInfo[] getVariableInfo(TagData data) {
        List vars = new ArrayList();
        try {
            String clazz = data.getAttributeString("className");
            if (clazz == null) clazz = com.yubuild.coreman.Constants.class.getName();
            Class c = Class.forName(clazz);
            if (data.getAttributeString("var") == null) {
                Field fields[] = c.getDeclaredFields();
                AccessibleObject.setAccessible(fields, true);
                for (int i = 0; i < fields.length; i++) vars.add(new VariableInfo(fields[i].getName(), "java.lang.String", true, 2));
            } else {
                String var = data.getAttributeString("var");
                vars.add(new VariableInfo(c.getField(var).getName(), "java.lang.String", true, 2));
            }
        } catch (Throwable cnf) {
            log.error(cnf.getMessage());
            cnf.printStackTrace();
        }
        return (VariableInfo[]) vars.toArray(new VariableInfo[0]);
    }
}
