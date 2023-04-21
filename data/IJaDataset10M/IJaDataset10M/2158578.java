package org.gudy.azureus2.plugins;

/**
 *
 * @author parg
 * @since 2.0.7.0
 */
public class PluginException extends Exception {

    public PluginException(String str) {
        super(str);
    }

    public PluginException(String str, Throwable e) {
        super(str, e);
    }

    public PluginException(Throwable e) {
        super(e);
    }
}
