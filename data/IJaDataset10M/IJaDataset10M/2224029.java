package org.endeavour.mgmt.model;

import java.util.List;
import java.util.Map;
import org.endeavour.mgmt.model.persistence.PersistenceManager;
import org.endeavour.mgmt.view.IViewConstants;

public class Defect extends WorkProduct {

    public static final String LABEL = "Defect";

    public Defect() {
        super.setLabel(LABEL);
    }

    public Defect(Project aProject) {
        this();
        super.setProject(aProject);
    }

    public void save(Map<String, Object> aData) {
        super.save(aData);
        String theStatus = (String) aData.get(STATUS);
        if (theStatus != null) {
            this.setStatus(theStatus);
        }
        super.getProject().addWorkProduct(this);
        PersistenceManager.getInstance().save(this);
    }

    public Map<String, Object> getData() {
        Map<String, Object> theData = super.getData();
        return theData;
    }

    public List<String> validate(Map<String, Object> aData) {
        List<String> theErrors = super.validate(aData);
        return theErrors;
    }

    public String getElementType() {
        return IViewConstants.RB.getString("defect.lbl");
    }

    public boolean equals(Object anObj) {
        boolean isEquals = false;
        if (anObj != null && anObj instanceof Defect) {
            Defect theDefect = (Defect) anObj;
            if (this.getId() != null) {
                isEquals = this.getId().equals(theDefect.getId());
            }
        }
        return isEquals;
    }
}
