package org.openscience.cdk.applications.art2aClassification;

/**
 * Fingerprint Item<br>
 * This class is a ported C# struct.
 * 
 * @author original C# code: Stefan Neumann, Gesellschaft fuer naturwissenschaftliche Informatik, stefan.neumann@gnwi.de<br>
 *         porting to Java: Christian Geiger, University of Applied Sciences Gelsenkirchen, 2007
 * @author Thomas Kuhn
 */
public class FingerprintItem {

    /**
	 * Fingerprint vector
	 */
    public double[] fingerprintVector;

    /**
	 * Corresponding object from which fingerprint vector was derived
	 */
    public Object correspondingObject;

    /**
	 * Constructor
	 */
    public FingerprintItem() {
    }

    /**
	 * Constructor
	 * 
	 * @param fingerprintVector
	 *            Fingerprint vector
	 * @param correspondingObject
	 *            Corresponding object from which fingerprint vector was derived
	 */
    public FingerprintItem(double[] fingerprintVector, Object correspondingObject) {
        this.fingerprintVector = fingerprintVector;
        this.correspondingObject = correspondingObject;
    }
}
