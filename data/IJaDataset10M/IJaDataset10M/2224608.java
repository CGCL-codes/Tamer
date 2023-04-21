package net.sourceforge.ondex.webservice2.result;

import net.sourceforge.ondex.core.CV;

/**
 * The controlled vocabulary (CV) for the created Concepts and Relations. It has
 * a mandatory name and an optional description field for additional
 * information.
 * 
 * @author David Withers
 */
public class WSCV extends WSMetaData {

    public WSCV() {
    }

    public WSCV(CV cv) {
        super(cv);
    }

    /*********************************************************************/
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
