package com.google.code.sagetvaddons.sagealert.server.events;

import gkusnick.sagetv.api.MediaFileAPI;
import com.google.code.sagetvaddons.sagealert.server.CoreEventsManager;
import com.google.code.sagetvaddons.sagealert.shared.SageAlertEventMetadata;

/**
 * @author dbattams
 *
 */
public final class RecordingStartedEvent extends RecordingEvent {

    public RecordingStartedEvent(MediaFileAPI.MediaFile mf, SageAlertEventMetadata data) {
        super(mf, data);
    }

    @Override
    public String getSource() {
        return CoreEventsManager.REC_STARTED;
    }
}
