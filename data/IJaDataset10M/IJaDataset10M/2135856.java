package org.epoline.phoenix.dossiernotepad.shared;

import org.epoline.phoenix.common.shared.PhoenixException;

/**
 * Happens if dossier wan't found in both systems: Legacy (DSS) and DMS.
 *
 */
public class DossierNotFoundInLegacyAndDMS extends PhoenixException {

    public DossierNotFoundInLegacyAndDMS(String s) {
        super(s);
    }
}
