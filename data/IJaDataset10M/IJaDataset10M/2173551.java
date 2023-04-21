package ProjectClasses;

import java.io.Serializable;

/**
 *
 * @author frhu2799
 */
public class ProjectSettings implements Serializable {

    private String mProjectName;

    private java.util.Date mCreationDate;

    private String mCreator;

    public java.util.Date getCreationDate() {
        return mCreationDate;
    }

    public void setCreationDate(java.util.Date val) {
        this.mCreationDate = val;
    }

    public String getCreator() {
        return mCreator;
    }

    public void setCreator(String val) {
        this.mCreator = val;
    }

    public String getProjectName() {
        return mProjectName;
    }

    public void setProjectName(String val) {
        this.mProjectName = val;
    }
}
