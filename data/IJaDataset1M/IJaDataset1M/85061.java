package org.webdocwf.util.smime.cms;

import org.webdocwf.util.smime.exception.SMIMEException;
import org.webdocwf.util.smime.exception.ErrorStorage;
import org.webdocwf.util.smime.der.DERSequencePr;
import org.webdocwf.util.smime.der.DERObjectIdentifier;
import org.webdocwf.util.smime.util.DERLengthSearcher;
import org.webdocwf.util.smime.util.ByteArrayComparator;
import java.security.cert.X509Certificate;

/**
 * IssuerName class is DER encoded object represented in ASN.1 notation
 * according to RFC2630. It is used for representing information about issuer
 * of particular certificates. Detail information about ASN.1 notation of
 * this class can be found in description of ASN.1 notation of IssuerAndSerialNumber.
 */
public class IssuerName extends DERSequencePr {

    /**
     * Container for DN (set of distinguished names)
     */
    private byte[] dNames;

    /**
     * Enables/Disables function for particular adding of Relative Distinguished Name
     */
    private int enable = 0;

    /**
     * Construction with information got from specific X509Certificate or from .cer
     * file information which is extracted into instance of X509Certificate class
     * @param cert0 X509Certificate
     * @exception SMIMEException caused by non SMIMEException which is:
     * CertificateEncodingException. Also, it can be thrown by super class
     * constructor.
     */
    public IssuerName(X509Certificate cert0) throws SMIMEException {
        byte[] tbs = null;
        try {
            tbs = cert0.getTBSCertificate();
        } catch (Exception e) {
            throw SMIMEException.getInstance(this, e, "constructor");
        }
        dNames = findDNfromTBS(tbs);
    }

    /**
     * Finds area with Distinguish Names from TBS Certificate part of X509
     * certificate, represented as byte array
     * @param tbs0 TBS Certificate represented as byte array
     * @return Distinguish name as byte array
     */
    private byte[] findDNfromTBS(byte[] tbs0) {
        int start = 0;
        byte[] temp;
        DERLengthSearcher len = new DERLengthSearcher(start, tbs0);
        start = start + len.getLengthtDERLengthPart() + 1;
        len.newInitialization(start, tbs0);
        start = start + len.getLengthtDERLengthPart() + len.getLengthtDERContentPart() + 1;
        len.newInitialization(start, tbs0);
        start = start + len.getLengthtDERLengthPart() + len.getLengthtDERContentPart() + 1;
        len.newInitialization(start, tbs0);
        start = start + len.getLengthtDERLengthPart() + len.getLengthtDERContentPart() + 1;
        len.newInitialization(start, tbs0);
        start = start + len.getLengthtDERLengthPart() + 1;
        int stop = start + len.getLengthtDERContentPart() - 1;
        temp = new byte[stop - start + 1];
        for (int i = start; i <= stop; i++) temp[i - start] = tbs0[i];
        return temp;
    }

    /**
     * Adds all Relative Distinguish Names from certificate to IssuerName
     * @exception SMIMEException thrown from super class addContent method.
     */
    public void addAllRelativeDN() throws SMIMEException {
        super.addContent(dNames);
        enable = 1;
    }

    /**
     * Adds particular Relative Distinguish Name from certificate to IssuerName.
     * This method can be called many times, but never if method
     * addAllRelativeDN was called first
     * @param id_at_type0 object identifier name of desired Particular Distinguish
     * Name
     * @return Desired Particular Distinguish Name as byte array
     * @exception SMIMEException if method addAllRelativeDN was already performed.
     * Also it can be caused by non SMIMEException which is:
     * UnsupportedEncodingException.
     */
    public int addParticularRelativeDN(String id_at_type0) throws SMIMEException {
        if (enable == 1) throw new SMIMEException(this, 1021);
        byte[] temp = new DERObjectIdentifier(id_at_type0, "NAME_STRING").getDEREncoded();
        ByteArrayComparator bcomp = new ByteArrayComparator(temp, dNames);
        int positionFirst = bcomp.getMatchingIndex();
        if (positionFirst != -1) {
            positionFirst = positionFirst + temp.length;
            DERLengthSearcher len = new DERLengthSearcher(positionFirst, dNames);
            positionFirst = positionFirst + len.getLengthtDERLengthPart() + 1;
            int positionLast = positionFirst + len.getLengthtDERContentPart() - 1;
            byte[] name = new byte[positionLast - positionFirst + 1];
            for (int i = positionFirst; i <= positionLast; i++) name[i - positionFirst] = dNames[i];
            RelativeDistinguishedName rdn = null;
            try {
                rdn = new RelativeDistinguishedName(id_at_type0, "NAME_STRING", new String(name, "ISO-8859-1"));
            } catch (Exception e) {
                throw SMIMEException.getInstance(this, e, "addParticularRelativeDN");
            }
            super.addContent(rdn.getDEREncoded());
            return 0;
        } else return -1;
    }
}
