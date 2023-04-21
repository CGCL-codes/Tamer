package edu.mit.csail.pag.amock.trace;

import edu.mit.csail.pag.amock.util.*;

public class ClinitEntry extends ClinitEvent {

    public ClinitEntry(int callId, ClassName className) {
        super(callId, className);
    }
}
