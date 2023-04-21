package org.blueoxygen.cimande.section.actions;

import org.blueoxygen.cimande.commons.CimandeAction;
import org.blueoxygen.cimande.section.Section;

/**
 * @author dwi miyanto [mee_andto@yahoo.com]
 *
 *         TODO To change the template for this generated type comment go to
 *         Window - Preferences - Java - Code Generation - Code and Comments
 */
public class ViewSection extends CimandeAction {

    private String id = "";

    protected Section section;

    public String execute() {
        if (!id.equalsIgnoreCase("")) {
            section = (Section) manager.getById(Section.class, getId());
            modelMap.put("section", section);
            return SUCCESS;
        } else {
            addActionError("Section not found");
            return ERROR;
        }
    }

    public Section getSection() {
        return section;
    }

    public void setSection(Section section) {
        this.section = section;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
