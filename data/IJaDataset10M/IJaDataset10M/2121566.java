package com.sun.midp.installer;

import java.io.IOException;
import com.sun.midp.io.j2me.storage.RandomAccessStream;

/**
 * Interface that must be implemented by any class that is used to verify
 * a signature of the midlet suite.
 */
public interface Verifier {

    /**
     * MIDlet property for the application signature
     */
    public static final String SIG_PROP = "MIDlet-Jar-RSA-SHA1";

    /**
     * MIDlet property for the content provider certificates
     */
    public static final String CERT_PROP = "MIDlet-Certificate-";

    /**
     * Checks to see if the JAD has a signature, but does not verify the
     * signature.
     *
     * @return true if the JAD has a signature
     */
    public boolean isJadSigned();

    /**
     * Gets the security domain name for this MIDlet Suite from storage.
     *
     * @param ca CA of an installed suite
     *
     * @return name of the security domain for the MIDlet Suite
     */
    public String getSecurityDomainName(String ca);

    /**
     * Verifies a Jar. On success set the name of the domain owner in the
     * install state. Post any error back to the server.
     *
     * @param jarStorage System store for applications
     * @param jarFilename name of the jar to read.
     *
     * @exception IOException if any error prevents the reading
     *   of the JAR
     * @exception InvalidJadException if the JAR is not valid or the
     *   provider certificate is missing
     */
    public String[] verifyJar(RandomAccessStream jarStorage, String jarFilename) throws IOException, InvalidJadException;
}
