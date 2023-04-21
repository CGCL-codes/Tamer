package org.apache.harmony.auth.internal.kerberos.v5;

import java.io.IOException;
import java.util.Date;
import org.apache.harmony.security.asn1.ASN1Any;
import org.apache.harmony.security.asn1.ASN1Constants;
import org.apache.harmony.security.asn1.ASN1Explicit;
import org.apache.harmony.security.asn1.ASN1Integer;
import org.apache.harmony.security.asn1.ASN1OctetString;
import org.apache.harmony.security.asn1.ASN1Sequence;
import org.apache.harmony.security.asn1.ASN1StringType;
import org.apache.harmony.security.asn1.ASN1Type;
import org.apache.harmony.security.asn1.BerInputStream;
import org.apache.harmony.security.asn1.DerInputStream;
import org.apache.harmony.auth.internal.nls.Messages;

/**
 * Kerberos Error Message type.
 * 
 * @see http://www.ietf.org/rfc/rfc4120.txt
 */
public class KerberosErrorMessage {

    private Date ctime;

    private int cusec;

    private Date stime;

    private int susec;

    private int errorCode;

    private String crealm;

    private PrincipalName cname;

    private String realm;

    private PrincipalName sname;

    private String etext;

    public KerberosErrorMessage() {
    }

    public static KerberosErrorMessage decode(DerInputStream in) throws IOException {
        return (KerberosErrorMessage) ASN1.decode(in);
    }

    public Date getCtime() {
        return ctime;
    }

    public int getCusec() {
        return cusec;
    }

    public Date getStime() {
        return stime;
    }

    public int getSusec() {
        return susec;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public String getCrealm() {
        return crealm;
    }

    public PrincipalName getCname() {
        return cname;
    }

    public String getRealm() {
        return realm;
    }

    public PrincipalName getSname() {
        return sname;
    }

    public String getEtext() {
        return etext;
    }

    private static final ASN1Sequence KRB_ERROR = new ASN1Sequence(new ASN1Type[] { new ASN1Explicit(0, ASN1Any.getInstance()), new ASN1Explicit(1, ASN1Any.getInstance()), new ASN1Explicit(2, KerberosTime.getASN1()), new ASN1Explicit(3, ASN1Integer.getInstance()), new ASN1Explicit(4, KerberosTime.getASN1()), new ASN1Explicit(5, ASN1Integer.getInstance()), new ASN1Explicit(6, ASN1Integer.getInstance()), new ASN1Explicit(7, ASN1StringType.GENERALSTRING), new ASN1Explicit(8, PrincipalName.ASN1), new ASN1Explicit(9, ASN1StringType.GENERALSTRING), new ASN1Explicit(10, PrincipalName.ASN1), new ASN1Explicit(11, ASN1StringType.GENERALSTRING), new ASN1Explicit(12, ASN1OctetString.getInstance()) }) {

        {
            setOptional(2);
            setOptional(3);
            setOptional(7);
            setOptional(8);
            setOptional(11);
            setOptional(12);
        }

        @Override
        protected Object getDecodedObject(BerInputStream in) throws IOException {
            Object[] values = (Object[]) in.content;
            KerberosErrorMessage message = new KerberosErrorMessage();
            message.ctime = (Date) values[2];
            if (values[3] != null) {
                message.cusec = ASN1Integer.toIntValue(values[3]);
            }
            message.stime = (Date) values[4];
            message.susec = ASN1Integer.toIntValue(values[5]);
            message.errorCode = ASN1Integer.toIntValue(values[6]);
            message.crealm = (String) values[7];
            message.cname = (PrincipalName) values[8];
            message.realm = (String) values[9];
            message.sname = (PrincipalName) values[10];
            message.etext = (String) values[11];
            return message;
        }

        @Override
        protected void getValues(Object object, Object[] values) {
            throw new RuntimeException(Messages.getString("auth.64"));
        }
    };

    public static final ASN1Explicit ASN1 = new ASN1Explicit(ASN1Constants.CLASS_APPLICATION, 30, KRB_ERROR);
}
