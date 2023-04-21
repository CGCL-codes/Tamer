package se.ucr.openqregdemo.bean;

import se.ucr.db.PrimaryKey;

/**
* This is the key class for the  discharge table.
*/
public class DischargeKey implements PrimaryKey {

    private Long mceid;

    /**
* Constructor with arguments.
* @param mceid
*/
    public DischargeKey(Long mceid) {
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
* @return Returns a string representation of discharge.
*/
    @Override
    public String toString() {
        return new String("discharge" + " Mceid: " + getMceid());
    }
}
