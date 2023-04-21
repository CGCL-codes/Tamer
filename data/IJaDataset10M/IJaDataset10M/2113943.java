package net.sf.fileexchange.api;

import java.io.File;
import java.io.InputStream;
import java.util.List;
import net.sf.fileexchange.api.snapshot.ResourceTreeSnapshot;
import net.sf.fileexchange.api.snapshot.events.SetFileLinkTargetEvent;
import net.sf.fileexchange.api.snapshot.events.StorageEvent;
import net.sf.fileexchange.util.http.FileResource;
import net.sf.fileexchange.util.http.RequestHeader;
import net.sf.fileexchange.util.http.Resource;

public final class FileLink extends FileOrDirectoryLink implements ResourceTree {

    public FileLink(File target) {
        super(target);
    }

    @Override
    public String getMetaData() {
        final File target = getTarget();
        if (target == null) {
            return "coming soon";
        }
        if (!target.isFile()) {
            return "broken link";
        }
        final long size = target.length();
        return ByteRepresenation.format(size);
    }

    @Override
    public Resource getResource(List<String> path, RequestHeader header, InputStream inputStream) throws InterruptedException {
        final File target = getTarget();
        if (path.size() > 0) return null;
        if (target == null) return null;
        return FileResource.create(target);
    }

    @Override
    StorageEvent<ResourceTreeSnapshot> createSetTargetEvent(File target) {
        return new SetFileLinkTargetEvent(target);
    }

    @Override
    public boolean isDirectoryLike() {
        return false;
    }
}
