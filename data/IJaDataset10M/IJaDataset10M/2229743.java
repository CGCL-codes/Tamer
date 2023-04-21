package test.oued.persistance.daopojo;

import java.util.*;

public class Service {

    private long id;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    private String serviceKey;

    public String getServiceKey() {
        return serviceKey;
    }

    public void setServiceKey(String serviceKey) {
        this.serviceKey = serviceKey;
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
}
