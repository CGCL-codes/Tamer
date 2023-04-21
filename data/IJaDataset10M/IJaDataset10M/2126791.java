package com.electionpredictor.seats.mapper;

import com.electionpredictor.instance.Election;
import com.electionpredictor.instance.Seat;

/**
 * interface for mapping seat fields
 * 
 * @author nNiels Stchedroff
 */
public interface SeatFieldMapper {

    /**
	 * Add given data to a field in the given parliamentary seat
	 * 
	 * @param seat
	 *            The parliamentary seat
	 * @param election
	 *            The election in question
	 * @param data
	 *            The data
	 */
    public void addField(Seat seat, Election election, String data);
}
