package com.acv.common.model.entity.impl;

import java.io.Serializable;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import com.acv.common.model.constants.PassengerType;
import com.acv.common.model.entity.Passenger;
import com.acv.common.model.entity.PassengerGroup;

/**
 * Simple group of Passenger objects. This structure is used by the website
 * to organize the passengers throught the different BookableService (ex: distribute
 * the passenger into rooms in the HotelCriterion).
 */
public class PassengerGroupImpl implements PassengerGroup, Serializable {

    private static final long serialVersionUID = -941162679142744387L;

    protected List<Passenger> passengers = new LinkedList<Passenger>();

    /**
     * Returns the number of Passenger that are of the given PassengerType in the current List.
     * @param type the PassengerType to check for.
     * @return the number of Passenger that are of the given PassengerType in the current List.
     */
    public int getNbOfType(PassengerType type) {
        int nbPaxOfType = 0;
        for (Passenger pax : passengers) {
            if (pax.getType() == type) nbPaxOfType++;
        }
        return nbPaxOfType;
    }

    public void add(Passenger passenger) {
        passengers.add(passenger);
    }

    public void append(PassengerGroup group) {
        if (group == null) {
            return;
        }
        List<Passenger> passengers = group.getPassengers();
        for (Passenger passenger : passengers) {
            add(passenger);
        }
    }

    public int getSize() {
        return passengers.size();
    }

    public List<Passenger> getPassengers() {
        return Collections.unmodifiableList(passengers);
    }

    public int getNbOfAge(int minAge, int maxAge) {
        int nbPaxOfAge = 0;
        for (Passenger pax : passengers) {
            if (pax.getAge() >= minAge && pax.getAge() <= maxAge) nbPaxOfAge++;
        }
        return nbPaxOfAge;
    }
}
