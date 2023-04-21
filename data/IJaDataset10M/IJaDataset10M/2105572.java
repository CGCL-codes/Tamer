package com.aelitis.net.upnp;

/**
 * @author parg
 *
 */
public interface UPnPLogListener {

    public static final int TYPE_ALWAYS = 1;

    public static final int TYPE_ONCE_PER_SESSION = 2;

    public static final int TYPE_ONCE_EVER = 3;

    public void log(String str);

    public void logAlert(String str, boolean error, int type);
}
