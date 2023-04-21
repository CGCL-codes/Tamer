package de.psisystems.dmachinery.io.protocols.bytecache;

import de.psisystems.dmachinery.io.URL2ByteCache;
import java.net.URLConnection;
import java.net.URL;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.File;

/**
 * Created by IntelliJ IDEA.
 * User: stefanpudig
 * Date: Jul 31, 2009
 * Time: 2:00:52 AM
 * To change this template use File | Settings | File Templates.
 */
public class URL2ByteCacheConnection extends URLConnection {

    InputStream is;

    OutputStream os;

    File file;

    protected URL2ByteCacheConnection(URL u) {
        super(u);
    }

    public void connect() {
        if (!connected) {
            if (getDoOutput()) {
                os = URL2ByteCache.getInstance().getOutputStream(url);
            } else {
                is = URL2ByteCache.getInstance().getInputStream(url);
            }
            connected = true;
        }
    }

    public synchronized InputStream getInputStream() {
        connect();
        return is;
    }

    public synchronized OutputStream getOutputStream() {
        connect();
        return os;
    }
}
