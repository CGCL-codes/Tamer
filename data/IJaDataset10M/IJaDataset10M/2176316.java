package net.sf.maple.resources.local;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import net.sf.maple.resources.Fragment;
import net.sf.maple.resources.InLocation;
import net.sf.maple.resources.ResourceError;
import net.sf.maple.resources.protocols.Protocol;

public abstract class AbstractLocationImpl {

    protected final Protocol protocol;

    protected final Fragment path;

    protected AbstractLocationImpl(Protocol prot, Fragment path) {
        this.protocol = prot;
        try {
            this.path = prot.adjustPath(path);
        } catch (Exception e) {
            throw new ResourceError(e);
        }
    }

    public URL toUrl() {
        try {
            return protocol.toUrl(path);
        } catch (Exception e) {
            throw new ResourceError(e);
        }
    }

    protected InputStream openInput() {
        try {
            Fragment actual = path.getActualPath();
            return protocol.openInput(actual);
        } catch (Exception e) {
            throw new ResourceError(e);
        }
    }

    protected OutputStream openOutput() {
        try {
            Fragment actual = path.getActualPath();
            return protocol.openOutput(actual);
        } catch (Exception e) {
            throw new ResourceError(e);
        }
    }

    @Override
    public String toString() {
        return toUrl().toString();
    }

    public Fragment[] list() {
        return protocol.list(path);
    }

    public Fragment getPath() {
        return path;
    }

    public Protocol getProtocol() {
        return protocol;
    }

    public int compareTo(InLocation o) {
        Protocol pl = this.getProtocol();
        Protocol pr = o.getProtocol();
        int comp = pl.compareTo(pr);
        if (comp != 0) return comp;
        int result = this.getPath().compareTo(o.getPath());
        return result;
    }
}
