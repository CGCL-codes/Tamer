package org.openbbs.blackboard.persistence.prevalence;

import org.apache.commons.lang.Validate;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.openbbs.blackboard.Zone;

class CreateZoneCommand implements PrevalenceCommand {

    private final Zone zone;

    public CreateZoneCommand(Zone zone) {
        Validate.notNull(zone, "zone must not be null");
        this.zone = zone;
    }

    public String toString() {
        return new ToStringBuilder(this).append("zone", zone).toString();
    }

    public void playback(PlaybackDelegate playbackDelegate) {
        Validate.notNull(playbackDelegate, "no playbackDelegate");
        playbackDelegate.createZone(this.zone);
    }

    private static final long serialVersionUID = -3352770516329017736L;
}
