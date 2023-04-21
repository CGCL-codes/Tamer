package com.googlecode.routing.simulator;

/**
 * Represents the information of a link (edge) that is part of the network graph
 * 
 * @author Felipe Ribeiro
 * @author Michelly Guedes
 * @author Renato Miceli
 * 
 */
public class LinkInfo {

    /**
	 * Source router
	 */
    public long routerAID;

    /**
	 * Destination router
	 */
    public long routerBID;

    /**
	 * Cost of the link (i.e. weight of the edge)
	 */
    public double cost;
}
