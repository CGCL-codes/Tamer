package com.primosync.store.factory;

import com.primosync.store.Storable;
import com.primosync.store.StorableFactory;
import com.primosync.cal.Timestamps;

/**
 * @author Thomas Oldervoll, thomas@zenior.no
 * @author $Author$
 * @version $Rev: 1 $
 * @date $Date$
 */
public class TimestampsFactory implements StorableFactory {

    public Storable create() {
        return new Timestamps();
    }
}
