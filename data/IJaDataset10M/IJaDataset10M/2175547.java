package fi.pyramus.plugin;

public class PluginManagerException extends RuntimeException {

    private static final long serialVersionUID = -5174694440768819025L;

    public PluginManagerException(Exception e) {
        super(e);
    }

    public PluginManagerException(String message) {
        super(message);
    }

    public PluginManagerException(Exception e, String message) {
        super(message, e);
    }
}
