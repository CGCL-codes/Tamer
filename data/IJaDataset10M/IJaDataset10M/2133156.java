package org.iosgi.outpost.operations;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import org.iosgi.outpost.Operation;

/**
 * @author Sven Schulz
 */
public class MkDir implements Operation<Void>, Serializable {

    private static final long serialVersionUID = -2910501327159742057L;

    private final File dir;

    public MkDir(final File dir) {
        this.dir = dir;
    }

    @Override
    public Void perform() throws Exception {
        boolean success = dir.mkdirs();
        if (!success) {
            throw new IOException("failed to create directory");
        }
        return null;
    }
}
