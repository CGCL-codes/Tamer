package org.hmaciel.sisingr.ejb.datatypes;

/**
 * @author pclavijo
 */
public class TELBean extends ANYBean {

    String tel;

    int id_tel;

    public TELBean() {
    }

    public int getId_tel() {
        return id_tel;
    }

    public void setId_tel(int id_tel) {
        this.id_tel = id_tel;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }
}
