package com.netx.ut.bl.R1.core;

import com.netx.bl.R1.core.*;

public class Interests extends HolderEntity<InterestsMetaData, Interest> {

    public static Interests getInstance() {
        return Seller.getInterests();
    }

    Interests() {
        super(new InterestsMetaData());
    }

    public void create(Connection c, Interest interest, AssociationMap<InterestedParty> parties) throws BLException, ValidationException {
        synchronized (c) {
            c.startTransaction();
            insert(c, interest);
            if (parties != null) {
                InterestedParties.getInstance().save(c, parties);
            }
            c.commit();
        }
    }

    public int clear(Connection c) throws BLException {
        return deleteAll(c);
    }
}
