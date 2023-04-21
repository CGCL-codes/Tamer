package org.localstorm.mcc.ejb.people.dao;

import org.localstorm.mcc.ejb.people.entity.Person;

/**
 *
 * @author localstorm
 */
public class PersonWrapper extends Person {

    private int remains;

    private static final long serialVersionUID = -1550857252928371785L;

    public PersonWrapper(Integer pid) {
        super(pid);
    }

    public int getRemains() {
        return remains;
    }

    public void setRemains(int remains) {
        this.remains = remains;
    }
}
