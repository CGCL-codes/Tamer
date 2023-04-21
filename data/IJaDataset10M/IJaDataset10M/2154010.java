package com.googlecode.psiprobe.tools.logging.commons;

import com.googlecode.psiprobe.tools.logging.DefaultAccessor;
import com.googlecode.psiprobe.tools.logging.LogDestination;
import java.util.List;

public class CommonsLoggerAccessor extends DefaultAccessor {

    public List getDestinations() {
        GetAllDestinationsVisitor v = new GetAllDestinationsVisitor();
        v.setTarget(getTarget());
        v.setApplication(getApplication());
        v.visit();
        return v.getDestinations();
    }

    public LogDestination getDestination(String logIndex) {
        GetSingleDestinationVisitor v = new GetSingleDestinationVisitor(logIndex);
        v.setTarget(getTarget());
        v.setApplication(getApplication());
        v.visit();
        return v.getDestination();
    }
}
