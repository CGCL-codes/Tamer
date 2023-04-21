package com.loribel.java.generator.template.bo;

import com.loribel.commons.exception.GB_TaskException;
import com.loribel.commons.util.CTools;
import com.loribel.commons.util.GB_TaskCheckTools;
import com.loribel.commons.util.STools;
import com.loribel.java.abstraction.GB_JavaAttribute;
import com.loribel.java.abstraction.GB_JavaMethod;
import com.loribel.java.generator.template.GB_JavaClassBOsTemplate;
import com.loribel.java.impl.GB_JavaAttributeImpl;
import com.loribel.java.impl.GB_JavaMethodImpl;
import com.loribel.java.tools.GB_JavaMethodTools;

/**
 * Template to generate XXXMyFactory class.
 * 
 * Interface simple avec des m�thodes newXXX()
 */
public class GB_JavaClassBOsFactory extends GB_JavaClassBOsTemplate {

    private String factoryName;

    private String nameForClass;

    private String nameForReturn;

    private boolean useFinal;

    public GB_JavaClassBOsFactory() {
        super();
    }

    protected GB_JavaMethod addMethod_new(String a_boName) {
        String l_nameForClass = STools.replace(nameForClass, a_boName);
        String l_nameForReturn = STools.replace(nameForReturn, a_boName);
        String l_name = "new" + a_boName;
        GB_JavaMethodImpl retour = new GB_JavaMethodImpl(l_name);
        retour.setFinal(useFinal);
        this.addMethod(retour);
        retour.setReturnType(l_nameForReturn);
        String l_line = "return new " + l_nameForClass + "();";
        retour.getContent().addLine(l_line);
        return retour;
    }

    public void setFactoryName(String a_factoryName) {
        factoryName = a_factoryName;
    }

    public void setNameForClass(String a_nameForClass) {
        nameForClass = a_nameForClass;
    }

    public void setNameForReturn(String a_nameForReturn) {
        nameForReturn = a_nameForReturn;
    }

    public void setUseFinal(boolean useFinal) {
        this.useFinal = useFinal;
    }

    public void validate() throws GB_TaskException {
        super.validate();
        GB_TaskCheckTools.checkParameter("name", getName());
        GB_TaskCheckTools.checkParameter("nameForClass", nameForClass);
        GB_TaskCheckTools.checkParameter("nameForReturn", nameForReturn);
        this.getComments().addLine("Simple factory.");
        if (STools.isNull(factoryName)) {
            factoryName = STools.removeEnd(getName(), "Gen");
        }
        GB_JavaAttribute l_attribute = new GB_JavaAttributeImpl(factoryName, "instance");
        l_attribute.setInitValue("new " + factoryName + "()");
        l_attribute.setStatic(true);
        l_attribute.setFinal(true);
        GB_JavaMethodTools.addMethod_get(this, l_attribute);
        this.addAttribute(l_attribute);
        int len = CTools.getSize(boNames);
        for (int i = 0; i < len; i++) {
            String l_boName = (String) boNames.get(i);
            addMethod_new(l_boName);
        }
    }
}
