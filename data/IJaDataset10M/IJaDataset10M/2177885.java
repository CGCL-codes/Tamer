package org.blueoxygen.cimande.template.actions;

import org.blueoxygen.cimande.descriptors.Descriptor;

/**
 *@author fauzan prasetyo�
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Name and Comments
 */
public class EditTemplate extends ViewTemplate {

    protected Descriptor templateDescriptor;

    private String descriptor_id = "";

    public String execute() {
        String result = super.execute();
        return SUCCESS;
    }

    public Descriptor getTemplateDescriptor() {
        return templateDescriptor;
    }

    public void setTemplateDescriptor(Descriptor templateDescriptor) {
        this.templateDescriptor = templateDescriptor;
    }

    public String getDescriptor_id() {
        return descriptor_id;
    }

    public void setDescriptor_id(String descriptor_id) {
        this.descriptor_id = descriptor_id;
    }
}
