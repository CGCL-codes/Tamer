package au.gov.naa.digipres.xena.core;

public class XenaEventHandler {

    private Xena xena;

    public static XenaEventHandler getEventHandler(Xena xena) {
        return xena.getXenaEventHandler();
    }

    XenaEventHandler(Xena xena) {
        this.xena = xena;
    }

    /**
	 * Fire a warning event to all Xena listeners.
	 * @param warning
	 */
    public void fireWarning(String warning) {
        xena.fireWarning(warning);
    }
}
