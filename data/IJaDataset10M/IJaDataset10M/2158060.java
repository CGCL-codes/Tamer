package com.google.appengine.datanucleus.test;

import com.google.appengine.datanucleus.Utils;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.jdo.annotations.Element;
import javax.jdo.annotations.Order;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

/**
 * @author Max Ross <maxr@google.com>
 */
@PersistenceCapable(detachable = "true")
public class HasOneToManyUnencodedStringPkListJDO implements HasOneToManyUnencodedStringPkJDO {

    @PrimaryKey
    private String key;

    @Element(dependent = "true")
    @Order(column = "flights_INTEGER_IDX_unencodedstringpk")
    private List<Flight> flights = Utils.newArrayList();

    @Persistent(mappedBy = "parent")
    @Element(dependent = "true")
    private List<BidirectionalChildUnencodedStringPkListJDO> bidirChildren = new ArrayList<BidirectionalChildUnencodedStringPkListJDO>();

    public void addFlight(Flight flight) {
        flights.add(flight);
    }

    public Collection<Flight> getFlights() {
        return flights;
    }

    public String getId() {
        return key;
    }

    public void setId(String key) {
        this.key = key;
    }

    public void addBidirChild(BidirectionalChildUnencodedStringPkJDO child) {
        bidirChildren.add((BidirectionalChildUnencodedStringPkListJDO) child);
    }

    public Collection<BidirectionalChildUnencodedStringPkJDO> getBidirChildren() {
        return (List) bidirChildren;
    }

    public boolean removeFlights(Collection<Flight> flights) {
        return this.flights.removeAll(flights);
    }

    public boolean removeBidirChildren(Collection<BidirectionalChildUnencodedStringPkJDO> bidirChildren) {
        return this.bidirChildren.removeAll(bidirChildren);
    }
}
