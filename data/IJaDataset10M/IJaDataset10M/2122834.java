package wilos.business.webservices.transfertobject;

import java.io.Serializable;
import wilos.model.spem2.section.Section;

public class SectionTO extends Section implements Serializable {

    /**
	 * 
	 */
    private static final long serialVersionUID = -3478890824306139556L;

    public SectionTO() {
    }

    public SectionTO(Section mySection) {
        this.setName(mySection.getName());
        this.setDescription(mySection.getDescription());
        this.setGuid(mySection.getGuid());
        this.setMainDescription(mySection.getMainDescription());
        this.setKeyConsiderations(mySection.getKeyConsiderations());
    }
}
