package net.kano.joustsim.oscar.oscar.service.icbm.dim;

import net.kano.joustsim.oscar.oscar.service.icbm.ft.events.RvConnectionEvent;

public class ReceivingAttachmentEvent extends RvConnectionEvent {

    private final long totalpos;

    private final long totallen;

    private final long attachpos;

    private Attachment attachment;

    public ReceivingAttachmentEvent(long totalpos, long totallen, long attachpos, Attachment dest) {
        this.totalpos = totalpos;
        this.totallen = totallen;
        this.attachpos = attachpos;
        attachment = dest;
    }

    public long getTotalPosition() {
        return totalpos;
    }

    public long getTotalLength() {
        return totallen;
    }

    public long getAttachmentReceived() {
        return attachpos;
    }

    public Attachment getAttachmentDestination() {
        return attachment;
    }
}
