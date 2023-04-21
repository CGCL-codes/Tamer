package org.ms150hams.trackem.network;

import java.util.Date;
import org.ms150hams.trackem.model.*;

public class ReplicationRequest extends Event {

    public ReplicationRequest(Station theStation, Date theTimestamp) {
        super(theStation, theTimestamp);
    }
}
