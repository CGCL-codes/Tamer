package org.gvsig.remoteClient.wfs.edition;

import java.util.ArrayList;

/**
 * @author Jorge Piera LLodr� (jorge.piera@iver.es)
 */
public class WFSTTransaction_1_1_0 extends WFSTTransaction {

    public WFSTTransaction_1_1_0(String typename, String namespaceprefix, String namespace, ArrayList featuresLocked) {
        super(typename, namespaceprefix, namespace, featuresLocked);
    }

    public WFSTTransaction_1_1_0() {
        super();
    }

    protected String getSchemaLocation() {
        return "../wfs/1.1.0/WFS-transaction.xsd";
    }

    protected String getVersion() {
        return "1.1.0";
    }
}
