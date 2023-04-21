package net.sourceforge.cridremote;

public interface IOsdListener {

    /**
	 * Informiert dar�ber, dass sich das OSD ge�ndert hat.
	 * 
	 * @param modEvent
	 */
    void osdModified(OsdEvent modEvent);

    /**
	 * Informiert dar�ber, dass ein neues OSD vollst�ndig gelesen wurde.
	 */
    void osdReadComplete();

    /**
	 * Informiert dar�ber, dass die Connection weg ist.
	 */
    void osdConnectionLost(OsdEvent e);

    /**
	 * @param event
	 */
    void wrongVersion(ErrorEvent event);
}
