package com.mgensystems.jarindexer.events;

import java.io.File;
import com.mgensystems.eventmanager.EMEvent;

public class FileIndexedStartedEvent extends EMEvent {

    private File artifact;

    public FileIndexedStartedEvent(File artifact) {
        super("fileIndexedStarted");
        this.artifact = artifact;
    }

    public File getArtifact() {
        return artifact;
    }

    public void setArtifact(File artifact) {
        this.artifact = artifact;
    }
}
