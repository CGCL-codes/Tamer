package au.gov.nla.aons.format.util;

import au.gov.nla.aons.registry.domain.RegistryFormat;

public interface RegistryFormatFieldWriter {

    /**
	 * Copies all changeable fields (not version or name) across from the source
	 * to the destination.
	 */
    public void copyRegistryFormatDataFields(RegistryFormat source, RegistryFormat dest);
}
