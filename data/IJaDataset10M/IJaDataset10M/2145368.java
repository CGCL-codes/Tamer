package com.loribel.java.generator.template.bo;

import com.loribel.commons.exception.GB_TaskException;
import com.loribel.commons.util.CTools;
import com.loribel.commons.util.GB_TaskCheckTools;
import com.loribel.commons.util.STools;
import com.loribel.commons.util.filter.GB_StringFilterNull;
import com.loribel.java.abstraction.GB_JavaMethod;
import com.loribel.java.abstraction.GB_JavaParameter;
import com.loribel.java.impl.GB_JavaMethodImpl;
import com.loribel.java.impl.GB_JavaParameterImpl;

/**
 * Template to generate XXXBOVisitor class.
 */
public class GB_JavaClassBOVisitorAbstract extends GB_JavaClassBOVisitor {

    private String boClassName;

    public GB_JavaClassBOVisitorAbstract() {
        super();
    }

    @Override
    public GB_JavaMethod addMethod_visit(String a_boName) {
        if (STools.isNull(a_boName)) {
            return null;
        }
        String l_name = "visit" + a_boName;
        GB_JavaMethodImpl retour = new GB_JavaMethodImpl(l_name);
        this.addMethod(retour);
        GB_JavaParameter l_parameter = new GB_JavaParameterImpl(prefixClass + a_boName + suffixClass, "a_item");
        retour.addParameter(l_parameter);
        retour.setReturnType("Object");
        retour.getContent().addLine("return defaultVisit(a_item);");
        retour.getComments().addLine("Visits " + a_boName + ".");
        return retour;
    }

    public GB_JavaMethod addMethod_visitDefault() {
        String l_name = "defaultVisit";
        GB_JavaMethodImpl retour = new GB_JavaMethodImpl(l_name);
        this.addMethod(retour);
        GB_JavaParameter l_parameter = new GB_JavaParameterImpl(boClassName, "a_item");
        retour.addParameter(l_parameter);
        retour.setReturnType("Object");
        retour.setAbstract(true);
        retour.getComments().addLine("Overwrite this method to default implementation.");
        return retour;
    }

    public void setBoClassName(String a_boClassName) {
        boClassName = a_boClassName;
    }

    @Override
    public void validate() throws GB_TaskException {
        super.validate();
        CTools.removeAll(boNames, new GB_StringFilterNull());
        GB_TaskCheckTools.checkParameter(boClassName, boClassName);
        if (getName() == null) {
            this.setName(prefixClass + suffixClass + "VisitorAbstract");
        }
        this.setAbstract(true);
        this.addImplements(getVisitorNameInterface());
        addMethod_visitDefault();
        this.getComments().addLine("All methods visit call visitDefault()");
    }
}
