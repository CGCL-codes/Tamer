package org.josso.tooling.gshell.install.provider.maven2;

import java.util.Collection;
import org.apache.commons.vfs.FileName;
import org.apache.commons.vfs.FileObject;
import org.apache.commons.vfs.FileSystem;
import org.apache.commons.vfs.FileSystemException;
import org.apache.commons.vfs.FileSystemOptions;
import org.apache.commons.vfs.provider.AbstractFileSystem;

public class MavenFileSystem extends AbstractFileSystem implements FileSystem {

    protected MavenFileSystem(FileName rootName, FileSystemOptions fileSystemOptions) {
        super(rootName, null, fileSystemOptions);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void addCapabilities(Collection caps) {
        caps.addAll(MavenProvider.capabilities);
    }

    @Override
    protected FileObject createFile(FileName fileName) throws Exception {
        return new MavenFileObject(fileName, this);
    }
}
