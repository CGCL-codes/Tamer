package net.sourceforge.ondex.plugin.introspect;

/**
 * Something has gone wrong during the introspection of a plugin.
 *
 * @author Matthew Pocock
 */
public class PluginIntrospectionException extends Exception {

    public PluginIntrospectionException() {
    }

    public PluginIntrospectionException(String s) {
        super(s);
    }

    public PluginIntrospectionException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public PluginIntrospectionException(Throwable throwable) {
        super(throwable);
    }
}
