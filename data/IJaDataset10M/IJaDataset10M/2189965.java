package org.webical.plugin.classloading;

/**
 * RunTimeException to throw when the ClassResolver could not load a Class
 * @author ivo
 *
 */
public class PluginClassNotFoundRuntimeException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public PluginClassNotFoundRuntimeException() {
    }

    public PluginClassNotFoundRuntimeException(String message) {
        super(message);
    }

    public PluginClassNotFoundRuntimeException(String message, Throwable throwable) {
        super(message, throwable);
    }

    public PluginClassNotFoundRuntimeException(Throwable throwable) {
        super(throwable);
    }
}
