package it.polito.appeal.traci;

import it.polito.appeal.traci.TraCIException.UnexpectedDatatype;
import it.polito.appeal.traci.protocol.Constants;
import de.uniluebeck.itm.tcpip.Storage;

public class Phase {

    private final int duration;

    private final TLState state;

    Phase(Storage content) throws UnexpectedDatatype {
        Utils.checkType(content, Constants.TYPE_INTEGER);
        duration = content.readInt();
        Utils.checkType(content, Constants.TYPE_INTEGER);
        content.readInt();
        Utils.checkType(content, Constants.TYPE_INTEGER);
        content.readInt();
        Utils.checkType(content, Constants.TYPE_STRING);
        state = new TLState(content.readStringASCII());
    }

    public Phase(final int duration, final TLState state) {
        this.duration = duration;
        this.state = state;
    }

    public int getDuration() {
        return duration;
    }

    public TLState getState() {
        return state;
    }
}
