package com.griddynamics.openspaces.convergence.monitor.coherence.events;

import com.griddynamics.openspaces.convergence.monitor.coherence.CoherenceMemberInfo;

public class MemberLeftEvent extends CoherenceMemberEvent {

    public MemberLeftEvent(Object source, CoherenceMemberInfo subject) {
        super(source, subject);
    }
}
