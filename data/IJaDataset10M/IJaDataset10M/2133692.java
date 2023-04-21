package com.google.appengine.datanucleus.test;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.datanucleus.Utils;
import java.util.Collection;
import java.util.List;
import javax.jdo.annotations.Element;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.Order;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

/**
 * @author Max Ross <maxr@google.com>
 */
@PersistenceCapable(detachable = "true")
public class HasOneToManyKeyPkListJDO implements HasOneToManyKeyPkJDO {

    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Key key;

    @Element(dependent = "true")
    @Order(column = "flights_INTEGER_IDX_keypk")
    private List<Flight> flights = Utils.newArrayList();

    public void addFlight(Flight flight) {
        flights.add(flight);
    }

    public Collection<Flight> getFlights() {
        return flights;
    }

    public Key getId() {
        return key;
    }
}
