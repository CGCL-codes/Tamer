package org.cake.game.io;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * A class defining some sort of resource and methods to access it.
 * @author Aaron
 */
public interface iResource {

    public boolean exists();

    public String getLocation();

    public String getExtension();

    public InputStream openRead();

    public OutputStream openWrite();

    public OutputStream openAppend();

    public boolean delete();

    public boolean canRead();

    public boolean canWrite();

    public long lastModified();

    public long contentLength();
}
