package fuse;

import fuse.util.Struct;

/**
 * An implementation of <code>FuseOpendirSetter</code> interface that is passed
 * as an argument to <code>fuse.Filesystem3.opendir()</code> callback method to
 * return filehandle and open options from it.
 */
public class FuseOpendir extends Struct implements FuseOpendirSetter {

    public Object fh;

    /**
	 * Callback for filehandle API
	 * <p/>
	 * 
	 * @param fh
	 *            the filehandle to return from <code>opendir()<code> method.
	 */
    public void setFh(Object fh) {
        this.fh = fh;
    }

    protected boolean appendAttributes(StringBuilder buff, boolean isPrefixed) {
        buff.append(super.appendAttributes(buff, isPrefixed) ? ", " : " ");
        buff.append("fh=").append(fh);
        return true;
    }
}
