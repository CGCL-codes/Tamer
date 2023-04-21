package gov.sandia.ccaffeine.dc.distributed;

interface OutOfBandListener extends java.util.EventListener {

    /** Process the out of band message sent to a Client.  The String
	cmd does not include the out of band token that originally
	indicated the presence of the out of band command. */
    public void processOutOfBand(OutOfBandEvent evt);
}
