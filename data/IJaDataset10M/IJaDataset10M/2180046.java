package codec.x509.extensions;

import java.io.IOException;
import java.security.cert.CertificateEncodingException;
import codec.asn1.ASN1Exception;
import codec.asn1.ASN1ObjectIdentifier;
import codec.asn1.ASN1Sequence;
import codec.asn1.ASN1SequenceOf;
import codec.asn1.ConstraintException;
import codec.asn1.Decoder;
import codec.x509.GeneralName;
import codec.x509.X509Extension;

/**
 * Class (in combination with other classes) implements the Admission Extension
 * of the ISIS MTT SigG_Core_Spec_V_1_0_2.
 * 
 * id-isismtt-at-admission OBJECT IDENTIFIER ::= {id-isismtt-at 3}
 * id-isismtt-at-namingAuthorities OBJECT IDENTIFIER ::= {id-isismtt-at 11}
 * 
 * AdmissionSyntax ::= SEQUENCE { admissionAuthority GeneralName OPTIONAL,
 * contentsOfAdmissions SEQUENCE OF Admissions }
 * 
 * Admissions ::= SEQUENCE { admissionAuthority [0] EXPLICIT GeneralName
 * OPTIONAL, namingAuthority [1] EXPLICIT NamingAuthority OPTIONAL,
 * professionInfos SEQUENCE OF ProfessionInfo }
 * 
 * NamingAuthority ::= SEQUENCE { namingAuthorityId OBJECT IDENTIFIER OPTIONAL,
 * namingAuthorityUrl IA5String OPTIONAL, namingAuthorityText
 * DirectoryString(SIZE(1..128)) OPTIONAL}
 * 
 * ProfessionInfo ::= SEQUENCE { namingAuthority [0] EXPLICIT NamingAuthority
 * OPTIONAL, professionItems SEQUENCE OF DirectoryString (SIZE(1..128)),
 * professionOIDs SEQUENCE OF OBJECT IDENTIFIER OPTIONAL, registrationNumber
 * PrintableString(SIZE(1..128)) OPTIONAL, addProfessionInfo OCTET STRING
 * OPTIONAL }
 * 
 * @author Christian Valentin
 */
public class AdmissionExtension extends X509Extension {

    /**
     * Generalname of the first Admissionauthority
     */
    private GeneralName admissionAuthority = null;

    /**
     * holds the Admissions, at least one, up to many.
     */
    private ASN1Sequence admissions = new ASN1SequenceOf(Admissions.class);

    /**
     * OID : id-isismtt-at 3
     */
    public static final String EXTENSION_OID = "1.3.36.8.3.3";

    /**
     * Sequence containing the AdmissionSyntax
     */
    private ASN1Sequence admissionSyntax = null;

    /**
     * constructor, using the Generalname of the first Admissionauthority as
     * Parameter.
     */
    public AdmissionExtension(GeneralName adAuth) {
        admissionSyntax = new ASN1Sequence();
        admissionAuthority = adAuth;
        setCritical(false);
        try {
            setOID(new ASN1ObjectIdentifier(EXTENSION_OID));
            admissionSyntax.add(admissionAuthority);
            setValue(admissionSyntax);
        } catch (ConstraintException ce) {
            ce.printStackTrace();
        } catch (CertificateEncodingException cee) {
            cee.printStackTrace();
        }
    }

    /**
     * constructor, using an Admission as Parameter
     */
    public AdmissionExtension(ASN1Sequence ad) {
        admissionSyntax = new ASN1Sequence();
        setCritical(false);
        admissions = ad;
        admissionSyntax.add(admissions);
        try {
            setOID(new ASN1ObjectIdentifier(EXTENSION_OID));
            setValue(admissionSyntax);
        } catch (ConstraintException ce) {
            ce.printStackTrace();
        } catch (CertificateEncodingException cee) {
            cee.printStackTrace();
        }
    }

    /**
     * adds the Sequence holding the Admissions to the extension
     */
    public void addAdmission(ASN1Sequence admissions_) {
        admissions = admissions_;
        admissionSyntax.add(admissions);
        try {
            setValue(admissionSyntax);
        } catch (CertificateEncodingException cee) {
            cee.printStackTrace();
        }
    }

    public void decode(Decoder dec) throws ASN1Exception, IOException {
        super.decode(dec);
        super.decodeExtensionValue(admissionAuthority);
        super.decodeExtensionValue(admissions);
    }

    public String toString() {
        String result;
        result = "Extension OID : " + EXTENSION_OID;
        if (this.admissionAuthority != null) {
            result = result + "\n" + this.admissionAuthority;
        }
        result = result + "\n" + this.admissionSyntax;
        return result;
    }
}
