package org.openqreg.openqregdemo.bean;

import org.openqreg.db.PrimaryKey;

/**
* This is the key class for the  evregistration table.
*/
public class EvregistrationKey implements PrimaryKey {

    private Long mceid;

    private String var;

    /**
* Constructor with arguments.
* @param mceid
* @param var
*/
    public EvregistrationKey(Long mceid, String var) {
        this.mceid = mceid;
        this.var = var;
    }

    /**
* @return Returns the Mceid.
*/
    public Long getMceid() {
        return mceid;
    }

    /**
* @param mceid The mceid to set.
*/
    public void setMceid(Long mceid) {
        this.mceid = mceid;
    }

    /**
* @return Returns the Var.
*/
    public String getVar() {
        return var;
    }

    /**
* @param var The var to set.
*/
    public void setVar(String var) {
        this.var = var;
    }

    /**
* @return Returns a string representation of evregistration.
*/
    @Override
    public String toString() {
        return new String("evregistration" + " Mceid: " + getMceid() + " Var: " + getVar());
    }
}
