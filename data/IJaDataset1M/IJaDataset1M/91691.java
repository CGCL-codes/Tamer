package com.loribel.java.generator.template.repository;

import com.loribel.commons.exception.GB_TaskException;
import com.loribel.java.generator.template.GB_JavaInterfaceTemplate;

/**
 * Template to generate interface.
 * 
 * @author Gregory Borelli
 */
public class GB_JavaInterfaceCreator extends GB_JavaInterfaceTemplate {

    private GB_JavaRepositoryOptions options;

    public GB_JavaInterfaceCreator() {
        super();
        setOverwrite(true);
    }

    public GB_JavaRepositoryOptions getOptions() {
        if (options == null) {
            options = new GB_JavaRepositoryOptions();
        }
        return options;
    }

    public void addConfiguredOptions(GB_JavaRepositoryOptions a_options) {
        options = a_options;
    }

    public void setOptions(GB_JavaRepositoryOptions a_options) {
        options = a_options;
    }

    public void validate() throws GB_TaskException {
        super.validate();
        getOptions();
        if (getName() == null) {
            this.setName(prefixClass + "BOCreator");
        }
        this.getComments().addLine("Interface creator.");
        this.addExtends(prefixClass + "BOCreatorGen");
        this.addImport("com.loribel.commons.exception", true);
    }
}
