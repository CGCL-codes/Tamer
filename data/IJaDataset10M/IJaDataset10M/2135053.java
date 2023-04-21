package net.sf.fileexchange.api.snapshot.events;

import java.io.File;
import net.sf.fileexchange.api.snapshot.FileLinkSnapshot;
import net.sf.fileexchange.api.snapshot.ResourceTreeSnapshot;

public class SetFileLinkTargetEvent implements StorageEvent<ResourceTreeSnapshot> {

    private final File target;

    public SetFileLinkTargetEvent(File target) {
        this.target = target;
    }

    @Override
    public void updateSnapshot(ResourceTreeSnapshot snapshot) {
        ((FileLinkSnapshot) snapshot).setTarget(target);
    }
}
