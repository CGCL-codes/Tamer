package org.wikiup.romulan.gom.imp.rc;

import org.wikiup.core.inf.Document;
import org.wikiup.core.util.Documents;
import org.wikiup.romulan.gom.inf.NamedModelInf;

public class CargoSpace extends ResourceContainer implements NamedModelInf {

    private int capability;

    public int getCapability() {
        return capability;
    }

    @Override
    public void aware(Document desc) {
        capability = Documents.getAttributeIntegerValue(desc, "capability", 0);
    }

    public String getName() {
        return "cargo";
    }
}
