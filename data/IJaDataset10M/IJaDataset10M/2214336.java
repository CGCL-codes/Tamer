package com.primosync.store;

import javax.microedition.rms.RecordFilter;

/**
 * @author Thomas Oldervoll, thomas@zenior.no
 * @author $Author$
 * @version $Rev: 1 $
 * @date $Date$
 */
public class RecordTypeFilter implements RecordFilter {

    public byte recordType;

    public boolean matches(byte[] bytes) {
        return (bytes.length > 0) && (bytes[0] == recordType);
    }
}
