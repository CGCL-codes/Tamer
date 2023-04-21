package com.gcalsync.cal;

import com.gcalsync.store.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * @author Thomas Oldervoll, thomas@zenior.no
 * @author $Author$
 * @version $Rev: 1 $
 * @date $Date$
 */
public class Timestamps extends Storable {

    public long lastSync;

    public Timestamps() {
        super(RecordTypes.TIMESTAMPS);
    }

    public void readRecord(DataInputStream in) throws IOException {
        lastSync = in.readLong();
    }

    public void writeRecord(DataOutputStream out) throws IOException {
        out.writeLong(lastSync);
    }
}
