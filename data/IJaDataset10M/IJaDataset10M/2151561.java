package org.jazzteam.bpe.model.employee;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * This class contains personal information about employee.
 * 
 * @author skars
 * @version $Rev: $
 */
@Embeddable
public class PersonalInfo {

    /** The name of employee. */
    private String name;

    /** The second name of employee. */
    private String secondName;

    /**
	 * Constructs personal info.
	 */
    public PersonalInfo() {
        this("", "");
    }

    /**
	 * Constructs personal info.
	 * 
	 * @param name
	 *            The name of employee.
	 * @param secondName
	 *            The second name of employee.
	 */
    public PersonalInfo(String name, String secondName) {
        setName(name);
        setSecondName(secondName);
    }

    /**
	 * Gets name of employee.
	 * 
	 * @return Returns the name of employee.
	 */
    @Column
    public String getName() {
        return name;
    }

    /**
	 * Sets name of employee.
	 * 
	 * @param name
	 *            The name to set.
	 */
    public void setName(String name) {
        this.name = name;
    }

    /**
	 * Gets second name of employee.
	 * 
	 * @return Returns the second name of employee.
	 */
    @Column
    public String getSecondName() {
        return secondName;
    }

    /**
	 * Sets second name of employee.
	 * 
	 * @param secondName
	 *            The second name to set.
	 */
    public void setSecondName(String secondName) {
        this.secondName = secondName;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        PersonalInfo info = (PersonalInfo) obj;
        return (name.equals(info.getName()) & (secondName.equals(info.getSecondName())));
    }

    @Override
    public int hashCode() {
        return 16 * name.hashCode() + 16 * secondName.hashCode();
    }
}
