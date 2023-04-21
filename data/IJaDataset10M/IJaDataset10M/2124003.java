package org.verus.ngl.web.beans.technicalprocessing;

import java.util.ArrayList;
import java.util.List;
import org.verus.ngl.utilities.NGLXMLUtility;
import org.verus.ngl.utilities.NGLUtility;

/**
 *
 * @author root
 */
public class AuthorityFile {

    private String id;

    private String select;

    private String display;

    private String authorityType;

    private String seeTerms;

    private String authorityXML;

    private String displayNew;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSelect() {
        return select;
    }

    public void setSelect(String select) {
        this.select = select;
    }

    public String getDisplay() {
        return display;
    }

    public void setDisplay(String display) {
        this.display = display;
    }

    public String getAuthorityType() {
        return authorityType;
    }

    public void setAuthorityType(String authorityType) {
        this.authorityType = authorityType;
    }

    public String getSeeTerms() {
        return seeTerms;
    }

    public void setSeeTerms(String seeTerms) {
        this.seeTerms = seeTerms;
    }

    public String getAuthorityXML() {
        return authorityXML;
    }

    public void setAuthorityXML(String authorityXML) {
        this.authorityXML = authorityXML;
    }

    public String getDisplayNew() {
        return displayNew;
    }

    public void setDisplayNew(String displayNew) {
        this.displayNew = displayNew;
    }
}
