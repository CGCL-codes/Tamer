package org.xmldap.asn1;

import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.ASN1TaggedObject;
import org.bouncycastle.asn1.DERObject;
import org.bouncycastle.asn1.DERObjectIdentifier;
import org.bouncycastle.asn1.DERSequence;

public class OtherLogotypeInfo extends ASN1Encodable {

    DERObjectIdentifier logotypeType = null;

    ASN1TaggedObject logotypeInfo = null;

    public static OtherLogotypeInfo getInstance(ASN1Sequence obj) {
        if (obj.size() == 2) {
            DERObjectIdentifier logotypeType = DERObjectIdentifier.getInstance(obj.getObjectAt(0));
            ASN1TaggedObject logotypeInfo = (ASN1TaggedObject) obj.getObjectAt(1);
            return new OtherLogotypeInfo(logotypeType, logotypeInfo);
        }
        throw new IllegalArgumentException("sequence must have length 2");
    }

    public OtherLogotypeInfo(DERObjectIdentifier logotypeType, ASN1TaggedObject logotypeInfo) {
        this.logotypeType = logotypeType;
        this.logotypeInfo = logotypeInfo;
    }

    public LogotypeInfo getLogotypeInfo() {
        return LogotypeInfo.getInstance(logotypeInfo);
    }

    @Override
    public DERObject toASN1Object() {
        ASN1EncodableVector v = new ASN1EncodableVector();
        v.add(logotypeType);
        v.add(logotypeInfo);
        return new DERSequence(v);
    }
}
