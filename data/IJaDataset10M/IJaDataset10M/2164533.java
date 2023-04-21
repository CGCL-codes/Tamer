package com.electionpredictor.instance;

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;

/**
 * Handles parties - prevents duplicate creation
 * 
 * @author Niels Stchedroff
 */
public class PartyStore {

    private Map<PartyID, Party> mParties;

    /**
	 * Hidden so that it can only be accessed via the instance
	 */
    protected PartyStore() {
        super();
    }

    /**
	 * Build/get the party
	 * 
	 * @param partyID
	 *            The party id
	 * @return The party
	 */
    public Party getParty(final PartyID partyID) {
        if (!getParties().containsKey(partyID)) {
            getParties().put(partyID, new Party(partyID));
        }
        return getParties().get(partyID);
    }

    /**
	 * Get all the currently defined parties as a list
	 * 
	 * @return The list of parties
	 */
    public Party[] getPartyList() {
        final Collection<Party> parties = getParties().values();
        final Party[] partyArray = parties.toArray(new Party[parties.size()]);
        ;
        Arrays.sort(partyArray);
        return partyArray;
    }

    /**
	 * @return the mParties
	 */
    private Map<PartyID, Party> getParties() {
        if (mParties == null) {
            mParties = new TreeMap<PartyID, Party>();
        }
        return mParties;
    }
}
