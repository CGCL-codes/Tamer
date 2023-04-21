package org.activebpel.rt.bpel.def;

import java.util.Iterator;

/**
 * Constructs that can be the parent of a 'partnerLinks' container def should implement this
 * interface.
 */
public interface IAePartnerLinksParentDef {

    /**
    * Gets the partner links container def.
    */
    public AePartnerLinksDef getPartnerLinksDef();

    /**
    * Sets the partner links container def.
    *
    * @param aDef
    */
    public void setPartnerLinksDef(AePartnerLinksDef aDef);

    /**
    * Gets the partner link with the given name.
    *
    * @param aPartnerLinkName
    */
    public AePartnerLinkDef getPartnerLinkDef(String aPartnerLinkName);

    /**
    * Gets the partner link defs.
    */
    public Iterator getPartnerLinkDefs();
}
