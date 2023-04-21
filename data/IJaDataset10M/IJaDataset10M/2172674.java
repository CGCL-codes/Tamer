package net.esle.sinadura.core.firma.timestamp;

import net.esle.sinadura.core.firma.exceptions.SinaduraCoreException;

/**
 * Time Stamp Authority client interface.
 * 
 * @author zylk.net
 */
public interface TSAClient {

    /**
	 * @return
	 */
    public int getTokenSizeEstimate();

    /**
	 * @param proxy
	 */
    public void setProxy(java.net.Proxy proxy);

    /**
	 * @param caller
	 * @param imprint
	 * @return
	 * @throws SinaduraCoreException
	 */
    public byte[] getTimeStampToken(TsaPdfPKCS7 caller, byte[] imprint) throws SinaduraCoreException;
}
