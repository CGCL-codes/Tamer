package test.oued.persistance.daopojo;

import java.util.*;

public class Category {

    private long id;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    private String categoryKey;

    public String getCategoryKey() {
        return categoryKey;
    }

    public void setCategoryKey(String categoryKey) {
        this.categoryKey = categoryKey;
    }

    private String intitule;

    public String getIntitule() {
        return intitule;
    }

    public void setIntitule(String intitule) {
        this.intitule = intitule;
    }

    private Set listEmployee;

    public Set getListEmployee() {
        return listEmployee;
    }

    public void setListEmployee(Set listEmployee) {
        this.listEmployee = listEmployee;
    }

    private Set listEmployeeFC;

    public Set getListEmployeeFC() {
        return listEmployeeFC;
    }

    public void setListEmployeeFC(Set listEmployeeFC) {
        this.listEmployeeFC = listEmployeeFC;
    }
}
