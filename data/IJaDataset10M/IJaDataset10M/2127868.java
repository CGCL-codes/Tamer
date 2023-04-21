package net.sf.borg.model.beans;

import java.util.Date;
import net.sf.borg.common.PrefName;
import net.sf.borg.common.Prefs;

public class Project extends KeyedBean<Project> implements CalendarBean, java.io.Serializable {

    private static final long serialVersionUID = -3250115693306817331L;

    private Integer Id_;

    public Integer getId() {
        return (Id_);
    }

    public void setId(Integer xx) {
        Id_ = xx;
    }

    private java.util.Date StartDate_;

    public java.util.Date getStartDate() {
        return (StartDate_);
    }

    public void setStartDate(java.util.Date xx) {
        StartDate_ = xx;
    }

    private java.util.Date DueDate_;

    public java.util.Date getDueDate() {
        return (DueDate_);
    }

    public void setDueDate(java.util.Date xx) {
        DueDate_ = xx;
    }

    private String Description_;

    public String getDescription() {
        return (Description_);
    }

    public void setDescription(String xx) {
        Description_ = xx;
    }

    private String Category_;

    public String getCategory() {
        return (Category_);
    }

    public void setCategory(String xx) {
        Category_ = xx;
    }

    private String Status_;

    public String getStatus() {
        return (Status_);
    }

    public void setStatus(String xx) {
        Status_ = xx;
    }

    private Integer parent_;

    public Integer getParent() {
        return parent_;
    }

    public void setParent(Integer parent) {
        this.parent_ = parent;
    }

    protected Project clone() {
        Project dst = new Project();
        dst.setKey(getKey());
        dst.setId(getId());
        dst.setStartDate(getStartDate());
        dst.setDueDate(getDueDate());
        dst.setDescription(getDescription());
        dst.setCategory(getCategory());
        dst.setStatus(getStatus());
        dst.setParent(getParent());
        return (dst);
    }

    public String getColor() {
        return "navy";
    }

    public Integer getDuration() {
        return new Integer(0);
    }

    public Date getDate() {
        return getDueDate();
    }

    public boolean getTodo() {
        return true;
    }

    public Date getNextTodo() {
        return null;
    }

    public String getText() {
        String show_abb = Prefs.getPref(PrefName.TASK_SHOW_ABBREV);
        String abb = "";
        if (show_abb.equals("true")) abb = "PR" + getId().toString() + " ";
        String de = abb + getDescription();
        String tx = de.replace('\n', ' ');
        return tx;
    }
}
