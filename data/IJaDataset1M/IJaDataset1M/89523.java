package org.blueoxygen.mantra.register.detail;

import java.util.ArrayList;
import java.util.List;
import org.blueoxygen.cimande.CimandeAction;
import org.blueoxygen.cimande.security.User;
import org.blueoxygen.cimande.gx.entity.GxDroplistName;
import org.blueoxygen.cimande.gx.entity.GxDroplistValue;
import org.blueoxygen.mantra.entity.Register;

public class FormRegisterDetail extends CimandeAction {

    private String dropList;

    private GxDroplistValue droplistValue = new GxDroplistValue();

    private List<GxDroplistValue> droplistValues = new ArrayList<GxDroplistValue>();

    private Register register = new Register();

    private List<Register> reristers = new ArrayList<Register>();

    private User user = new User();

    private List<User> users = new ArrayList<User>();

    public String Execute() {
        if (getRegister().getId() != null && !"".equalsIgnoreCase(getRegister().getId().trim())) {
            setRegister((Register) manager.findAllSorted(Register.class, getRegister().getId()));
        }
        setDroplistValues(manager.getList("SELECT w FROM " + GxDroplistValue.class.getName() + " w WHERE w.name.id='402881881f7e7602011f7e77ebb80002'", null, null));
        return SUCCESS;
    }

    public Register getRegister() {
        return register;
    }

    public void setRegister(Register register) {
        this.register = register;
    }

    public List<Register> getReristers() {
        return reristers;
    }

    public void setReristers(List<Register> reristers) {
        this.reristers = reristers;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public String getDropList() {
        return dropList;
    }

    public void setDropList(String dropList) {
        this.dropList = dropList;
    }

    public GxDroplistValue getDroplistValue() {
        return droplistValue;
    }

    public void setDroplistValue(GxDroplistValue droplistValue) {
        this.droplistValue = droplistValue;
    }

    public List<GxDroplistValue> getDroplistValues() {
        return droplistValues;
    }

    public void setDroplistValues(List<GxDroplistValue> droplistValues) {
        this.droplistValues = droplistValues;
    }
}
