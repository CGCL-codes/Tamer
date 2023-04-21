package dk.i2m.converge.plugins.joomla.client;

/**
 * Exception thrown when an incompatible Joomla plug-in is encountered.
 *
 * @author Allan Lykke Christensen
 */
public class IncompatibleJoomlaPluginException extends JoomlaException {

    public IncompatibleJoomlaPluginException(Throwable thrwbl) {
        super(thrwbl);
    }

    public IncompatibleJoomlaPluginException(String string, Throwable thrwbl) {
        super(string, thrwbl);
    }

    public IncompatibleJoomlaPluginException(String string) {
        super(string);
    }

    public IncompatibleJoomlaPluginException() {
        super();
    }
}
