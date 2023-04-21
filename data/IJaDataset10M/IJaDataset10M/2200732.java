package org.tolven.app.entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author mohammed
 *
 */
@Entity
@Table(name = "states_names", schema = "app")
public class StateNames implements Serializable {

    /**
	 * Auto generated Serial Version ID
	 */
    private static final long serialVersionUID = -6963816710407554337L;

    /**
	 * Default Constructor
	 */
    public StateNames() {
        super();
    }

    @Id
    @Column(name = "state_code")
    private String stateCode;

    @Column(name = "state_desc")
    private String stateName;

    /**
	 * @return the stateCode
	 */
    public String getStateCode() {
        return stateCode;
    }

    /**
	 * @param stateCode the stateCode to set
	 */
    public void setStateCode(String stateCode) {
        this.stateCode = stateCode;
    }

    /**
	 * @return the stateName
	 */
    public String getStateName() {
        return stateName;
    }

    /**
	 * @param stateName the stateName to set
	 */
    public void setStateName(String stateName) {
        this.stateName = stateName;
    }
}
