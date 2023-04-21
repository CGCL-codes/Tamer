package org.openqreg.openqregdemo.bean;

import org.openqreg.db.PrimaryKey;

/**
* This is the key class for the  followup table.
*/
public class FollowupKey implements PrimaryKey {

    private Long mceid;

    /**
* Constructor with arguments.
* @param mceid
*/
    public FollowupKey(Long mceid) {
        this.mceid = mceid;
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
* @return Returns a string representation of followup.
*/
    @Override
    public String toString() {
        return new String("followup" + " Mceid: " + getMceid());
    }
}
