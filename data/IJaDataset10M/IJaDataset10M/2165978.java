package com.loribel.java.generator.template.repository;

import com.loribel.commons.exception.GB_TaskException;
import com.loribel.java.abstraction.GB_JavaBlock;
import com.loribel.java.abstraction.GB_JavaConstructor;
import com.loribel.java.abstraction.GB_JavaParameter;
import com.loribel.java.generator.template.GB_JavaClassTemplate;
import com.loribel.java.impl.GB_JavaConstructorImpl;
import com.loribel.java.impl.GB_JavaParameterImpl;

/**
 * Template to generate RepositoryOwner.
 * 
 * @author Gregory Borelli
 */
public class GB_JavaClassBOAbstract extends GB_JavaClassTemplate {

    private GB_JavaRepositoryOptions options;

    public GB_JavaClassBOAbstract() {
        super();
        setOverwrite(false);
    }

    public void addConfiguredOptions(GB_JavaRepositoryOptions a_options) {
        options = a_options;
    }

    public GB_JavaRepositoryOptions getOptions() {
        if (options == null) {
            options = new GB_JavaRepositoryOptions();
        }
        return options;
    }

    public void setOptions(GB_JavaRepositoryOptions a_options) {
        options = a_options;
    }

    public void validate() throws GB_TaskException {
        super.validate();
        getOptions();
        this.getComments().addLine("Abstract BusinessObject.");
        this.setName(prefixClass + "BOAbstract");
        this.setAbstract(true);
        this.addImplements(prefixClass + "BusinessObject");
        this.setExtends("GB_BusinessObjectDefault");
        this.addImport("com.loribel.commons.business", true);
        this.addConstructor_String();
    }

    private GB_JavaConstructor addConstructor_String() {
        GB_JavaConstructor retour = new GB_JavaConstructorImpl();
        GB_JavaParameter l_param = new GB_JavaParameterImpl("String", "a_boName");
        retour.addParameter(l_param);
        GB_JavaBlock c = retour.getContent();
        c.addLine("super(a_boName);");
        this.addConstructor(retour);
        return retour;
    }
}
