package org.infoeng.ofbiz.ltans.scep;

import org.bouncycastle.asn1.DERObjectIdentifier;

public interface SCEPIdentifiers {

    public static final DERObjectIdentifier id_VeriSign = new DERObjectIdentifier("2.16.840.1.113733");

    public static final DERObjectIdentifier id_pki = new DERObjectIdentifier(id_VeriSign + ".1");

    public static final DERObjectIdentifier id_attributes = new DERObjectIdentifier(id_pki + ".9");

    public static final DERObjectIdentifier id_messageType = new DERObjectIdentifier(id_attributes + ".2");

    public static final DERObjectIdentifier id_pkiStatus = new DERObjectIdentifier(id_attributes + ".3");

    public static final DERObjectIdentifier id_failInfo = new DERObjectIdentifier(id_attributes + ".4");

    public static final DERObjectIdentifier id_senderNonce = new DERObjectIdentifier(id_attributes + ".5");

    public static final DERObjectIdentifier id_recipientNonce = new DERObjectIdentifier(id_attributes + ".6");

    public static final DERObjectIdentifier id_transId = new DERObjectIdentifier(id_attributes + ".7");

    public static final DERObjectIdentifier id_extensionReq = new DERObjectIdentifier(id_attributes + ".8");
}
