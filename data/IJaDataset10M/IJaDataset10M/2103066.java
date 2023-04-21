package org.trdf.trdf4jena;

/**
 * Manages GUI labels for trust values.
 * Labels for trust values must not be used inside the application. They may be
 * used for the user interface, i.e. to present trust values to human users.
 *
 * @author Olaf Hartig
 */
public interface TrustValueLabelizer {

    /**
	 * Returns a GUI label for the given trust value.
	 */
    public String getLabel(TrustValue t);

    /**
	 * Returns a representing trust value for the given GUI label.
	 */
    public TrustValue getTrustValue(String label);
}
