package com.tysanclan.site.projectewok.beans;

import com.tysanclan.site.projectewok.entities.SenateElection;

/**
 * Functionality wrapper for several method calls
 * 
 * @author Jeroen Steenbeeke
 */
public interface WrapperService {

    public int countElectionWinner(SenateElection senateElection);
}
