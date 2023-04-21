package org.eclipse.wst.xml.security.ui.preferences;

/**
 * <p>
 * This interface contains some preference values for the XML Security Tools preference pages. All other values (like
 * algorithms) are defined in the <code>Algorithms</code> class in the
 * <code>org.eclipse.wst.xml.security.core.utils</code> package.
 * </p>
 *
 * @author Dominik Schadow
 * @version 0.5.0
 */
public interface IPreferenceValues {

    /** Canonicalization types. */
    String[][] CANON_TYPES = { { "&Exclusive", "exclusive" }, { "&Inclusive", "inclusive" } };

    /** Canonicalization targets. */
    String[][] CANON_TARGETS = { { "&Same Document", "internal" }, { "&New Document", "external" } };

    /** Signature types. */
    String[][] SIGNATURE_TYPES = { { "Enveloping", "enveloping" }, { "Enveloped", "enveloped" } };

    /** Encryption types. */
    String[][] ENCRYPTION_TYPES = { { "Enveloping", "enveloping" } };
}
