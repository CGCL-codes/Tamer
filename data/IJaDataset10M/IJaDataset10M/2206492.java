package org.ufacekit.ui.example.model;

import java.util.List;
import org.ufacekit.core.ubean.UArrayBean;

/**
 *
 */
public class Country extends UArrayBean {

    /**
	 *
	 */
    public static final int ID = 1;

    /**
	 *
	 */
    public static final int NAME = 2;

    /**
	 *
	 */
    public static final int FEDERAL_STATES = 3;

    /**
	 * @param id
	 * @param name
	 */
    public Country(int id, String name) {
        setId(id);
        setName(name);
    }

    @SuppressWarnings("unchecked")
    public <T extends Object> T getValueType(int featureId) {
        switch(featureId) {
            case ID:
                return (T) int.class;
            case NAME:
                return (T) String.class;
            case FEDERAL_STATES:
                return (T) List.class;
        }
        return null;
    }

    /**
	 * @param id
	 */
    public void setId(int id) {
        set(ID, Integer.valueOf(id));
    }

    /**
	 * @return value
	 */
    public int getId() {
        return ((Integer) get(ID)).intValue();
    }

    /**
	 * @param name
	 */
    public void setName(String name) {
        set(NAME, name);
    }

    /**
	 * @return value
	 */
    public String getName() {
        return (String) get(NAME);
    }

    /**
	 * @param federalState
	 */
    public void addFederalState(FederalState federalState) {
        add(FEDERAL_STATES, federalState);
    }

    /**
	 * @param federalState
	 */
    public void removeFederalState(FederalState federalState) {
        remove(FEDERAL_STATES, federalState);
    }

    /**
	 * @return value
	 */
    @SuppressWarnings("unchecked")
    public List<FederalState> getFederalStates() {
        return (List<FederalState>) get(FEDERAL_STATES);
    }
}
