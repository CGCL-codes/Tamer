package es.gva.cit.catalog.csw.messages;

import es.gva.cit.catalog.csw.drivers.profiles.CSWAbstractProfile;
import es.gva.cit.catalog.csw.parsers.CSWConstants;

/**
 * @author Jorge Piera LLodr� (jorge.piera@iver.es)
 */
public class CSWMessages2_0_0 extends CSWAbstractMessages {

    public CSWMessages2_0_0(CSWAbstractProfile profile) {
        super(profile);
    }

    protected String getContraintVersion() {
        return CSWConstants.CONSTRAINT_VERSION_2_0_0;
    }
}
