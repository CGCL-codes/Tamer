package org.matsim.plans.filters;

/**
 * this interface offers the basic functions for
 * org.matsim.playground.filters.filter.Filter und its subclasses.
 * 
 * @author ychen
 * 
 */
public interface FilterI {

    /**
	 * Counts, how many persons (org.matsim.demandmodeling.plans.Person) or
	 * events(org.matsim.demandmodeling.events.BasicEvent) were selected
	 */
    void count();

    /**
	 * Returns how many persons (org.matsim.demandmodeling.plans.Person) or
	 * events(org.matsim.demandmodeling.events.BasicEvent) were selected
	 * @return how many persons (org.matsim.demandmodeling.plans.Person) or
	 * events(org.matsim.demandmodeling.events.BasicEvent) were selected
	 */
    int getCount();
}
