package org.jtools.iofs.db;

import java.io.IOException;
import java.net.URI;
import java.util.HashSet;
import java.util.Set;
import org.jtools.io.IOFile;
import org.jtools.io.IOFileFactory;
import org.jtools.io.IOFileUtils;

public class DatabaseFileFactory implements IOFileFactory {

    private static final Set<String> installedIOProtocols = new HashSet<String>();

    private static final DatabaseFileFactory singleton = new DatabaseFileFactory();

    public static DatabaseFileFactory getInstance() {
        return singleton;
    }

    public IOFile newIOFile(URI uri) {
        try {
            return valueOf(uri);
        } catch (IOException e) {
            return null;
        }
    }

    public static DatabaseFile valueOf(URI uri) throws IOException {
        DBFileSystem tfs = DBFS.getFileSystem(uri);
        if (tfs == null) throw new IOException("unknown database filesystem: " + uri);
        return tfs.get(uri);
    }

    static void install(String protocol) {
        if (installedIOProtocols.add(protocol)) IOFileUtils.register(protocol, DatabaseFileFactory.getInstance(), true);
    }
}
