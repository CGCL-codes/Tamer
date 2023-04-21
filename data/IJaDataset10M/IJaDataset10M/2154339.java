package com.peterhi.persist.bean;

import java.io.Serializable;
import com.peterhi.State;

/**
 *
 * @author YUN TAO
 */
public class Member implements Serializable {

    public int id = -1;

    public String email;

    public String displayName;

    public State state;

    public Member() {
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Member other = (Member) obj;
        if (this.email != other.email && (this.email == null || !this.email.equals(other.email))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 43 * hash + (this.email != null ? this.email.hashCode() : 0);
        return hash;
    }
}
