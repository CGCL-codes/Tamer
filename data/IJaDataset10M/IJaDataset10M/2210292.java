package net.sf.fileexchange.api.snapshot.events;

import net.sf.fileexchange.api.snapshot.VirtualFolderSnapshot.Child;

public class RenameVirtualFolderChildEvent implements StorageEvent<Child> {

    private final String newValue;

    public RenameVirtualFolderChildEvent(String newValue) {
        this.newValue = newValue;
    }

    @Override
    public void updateSnapshot(Child snapshot) {
        snapshot.setName(newValue);
    }
}
