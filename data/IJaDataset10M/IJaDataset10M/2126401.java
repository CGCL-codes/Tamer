package org.hibernate.test.annotations.onetomany;

import java.io.Serializable;
import javax.persistence.Embeddable;

/**
 * @author Emmanuel Bernard
 */
@Embeddable
public class ParentPk implements Serializable {

    String firstName;

    String lastName;

    boolean isMale;

    public int hashCode() {
        return firstName.hashCode() + lastName.hashCode() + (isMale ? 0 : 1);
    }

    public boolean equals(Object obj) {
        if (obj != null && obj instanceof ParentPk) {
            ParentPk other = (ParentPk) obj;
            return firstName.equals(other.firstName) && lastName.equals(other.lastName) && isMale == other.isMale;
        } else {
            return false;
        }
    }
}
