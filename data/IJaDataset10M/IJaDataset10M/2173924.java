package org.eclipse.mylyn.internal.trac.core;

import java.io.Serializable;
import java.util.List;
import org.eclipse.mylyn.internal.trac.core.model.TracComponent;
import org.eclipse.mylyn.internal.trac.core.model.TracTicketField;
import org.eclipse.mylyn.internal.trac.core.model.TracMilestone;
import org.eclipse.mylyn.internal.trac.core.model.TracPriority;
import org.eclipse.mylyn.internal.trac.core.model.TracSeverity;
import org.eclipse.mylyn.internal.trac.core.model.TracTicketResolution;
import org.eclipse.mylyn.internal.trac.core.model.TracTicketStatus;
import org.eclipse.mylyn.internal.trac.core.model.TracTicketType;
import org.eclipse.mylyn.internal.trac.core.model.TracVersion;

public class TracClientData implements Serializable {

    private static final long serialVersionUID = 6891961984245981675L;

    List<TracComponent> components;

    List<TracMilestone> milestones;

    List<TracPriority> priorities;

    List<TracSeverity> severities;

    List<TracTicketField> ticketFields;

    List<TracTicketResolution> ticketResolutions;

    List<TracTicketStatus> ticketStatus;

    List<TracTicketType> ticketTypes;

    List<TracVersion> versions;

    long lastUpdate;
}
