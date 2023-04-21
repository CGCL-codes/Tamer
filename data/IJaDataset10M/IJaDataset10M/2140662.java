package org.radsimplified.genapp.metadata;

import java.util.List;
import org.jopendocument.dom.spreadsheet.SpreadSheet;

public class Operation {

    protected BizEntity entity;

    protected Short slNo;

    protected String name;

    protected String label;

    protected Boolean visibleInList = true;

    public void setEntity(BizEntity entity) {
        this.entity = entity;
    }

    public BizEntity getEntity() {
        return entity;
    }

    public void setSlNo(Short slNo) {
        this.slNo = slNo;
    }

    public Short getSlNo() {
        return slNo;
    }

    public void setName(String name) {
        this.name = name != null && name.equals("") ? null : name;
    }

    public String getName() {
        return name;
    }

    public void setLabel(String label) {
        this.label = label != null && label.equals("") ? null : label;
    }

    public String getLabel() {
        return label;
    }

    public void setVisibleInList(Boolean visibleInList) {
        this.visibleInList = visibleInList;
    }

    public Boolean getVisibleInList() {
        return visibleInList;
    }

    public static List<Operation> getOperations(SpreadSheet metadata, String name2) {
        return null;
    }
}
