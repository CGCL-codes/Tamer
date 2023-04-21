package org.javaldap.ldapv3;

import java.io.PrintStream;
import com.ibm.util.*;
import com.ibm.asn1.*;

/** This class represents the ASN.1 SEQUENCE type <tt>ExtendedResponse</tt>.
  * For each sequence member, sequence classes contain a
  * public member variable of the corresponding Java type.
  * @author Snacc for Java
  * @version Fri Jul  2 18:01:43 1999
 
  */
public class ExtendedResponse implements LDAPv3 {

    /** member variable representing the sequence member resultCode of type LDAPResultEnum */
    public LDAPResultEnum resultCode = new LDAPResultEnum();

    /** member variable representing the sequence member matchedDN of type byte[] */
    public byte[] matchedDN;

    /** member variable representing the sequence member errorMessage of type byte[] */
    public byte[] errorMessage;

    /** member variable representing the sequence member referral of type Referral */
    public Referral referral = null;

    /** member variable representing the sequence member responseName of type byte[] */
    public byte[] responseName = null;

    /** member variable representing the sequence member response of type byte[] */
    public byte[] response = null;

    /** default constructor */
    public ExtendedResponse() {
    }

    /** copy constructor */
    public ExtendedResponse(ExtendedResponse arg) {
        resultCode = new LDAPResultEnum(arg.resultCode);
        matchedDN = new byte[arg.matchedDN.length];
        System.arraycopy(arg.matchedDN, 0, matchedDN, 0, arg.matchedDN.length);
        errorMessage = new byte[arg.errorMessage.length];
        System.arraycopy(arg.errorMessage, 0, errorMessage, 0, arg.errorMessage.length);
        referral = new Referral(arg.referral);
        responseName = new byte[arg.responseName.length];
        System.arraycopy(arg.responseName, 0, responseName, 0, arg.responseName.length);
        response = new byte[arg.response.length];
        System.arraycopy(arg.response, 0, response, 0, arg.response.length);
    }

    /** decoding method.
	* @param dec
	*        decoder object derived from com.ibm.asn1.ASN1Decoder
	* @exception com.ibm.asn1.ASN1Exception 
	*            decoding error
	*/
    public void decode(ASN1Decoder dec) throws ASN1Exception {
        dec.nextIsImplicit(dec.makeTag(dec.APPLICATION_TAG_CLASS, 24));
        int seq_nr = dec.decodeSequence();
        resultCode.decode(dec);
        matchedDN = dec.decodeOctetString();
        errorMessage = dec.decodeOctetString();
        if (!dec.nextIsOptional(dec.makeTag(dec.CONTEXT_TAG_CLASS, 3))) {
            referral = new Referral();
            dec.nextIsImplicit(dec.makeTag(dec.CONTEXT_TAG_CLASS, 3));
            referral.decode(dec);
        }
        if (!dec.nextIsOptional(dec.makeTag(dec.CONTEXT_TAG_CLASS, 10))) {
            dec.nextIsImplicit(dec.makeTag(dec.CONTEXT_TAG_CLASS, 10));
            responseName = dec.decodeOctetString();
        }
        if (!dec.nextIsOptional(dec.makeTag(dec.CONTEXT_TAG_CLASS, 11))) {
            dec.nextIsImplicit(dec.makeTag(dec.CONTEXT_TAG_CLASS, 11));
            response = dec.decodeOctetString();
        }
        dec.endOf(seq_nr);
    }

    /** encoding method.
	* @param enc
	*        encoder object derived from com.ibm.asn1.ASN1Encoder
	* @exception com.ibm.asn1.ASN1Exception 
	*            encoding error
	*/
    public void encode(ASN1Encoder enc) throws ASN1Exception {
        enc.nextIsImplicit(enc.makeTag(enc.APPLICATION_TAG_CLASS, 24));
        int seq_nr = enc.encodeSequence();
        resultCode.encode(enc);
        enc.encodeOctetString(matchedDN);
        enc.encodeOctetString(errorMessage);
        if (referral != null) {
            enc.nextIsImplicit(enc.makeTag(enc.CONTEXT_TAG_CLASS, 3));
            referral.encode(enc);
        }
        if (responseName != null) {
            enc.nextIsImplicit(enc.makeTag(enc.CONTEXT_TAG_CLASS, 10));
            enc.encodeOctetString(responseName);
        }
        if (response != null) {
            enc.nextIsImplicit(enc.makeTag(enc.CONTEXT_TAG_CLASS, 11));
            enc.encodeOctetString(response);
        }
        enc.endOf(seq_nr);
    }

    /** default print method (variable indentation)
	* @param os
	*        PrintStream representing the print destination (file, etc)
	*/
    public void print(PrintStream os) {
        print(os, 0);
    }

    /** print method (variable indentation)
	* @param os
	*        PrintStream representing the print destination (file, etc)
	* @param indent
	*        number of blanks that preceed each output line.
	*/
    public void print(PrintStream os, int indent) {
        os.println("{ -- SEQUENCE --");
        for (int ii = 0; ii < indent + 2; ii++) os.print(' ');
        os.print("resultCode = ");
        resultCode.print(os, indent + 2);
        os.println(',');
        for (int ii = 0; ii < indent + 2; ii++) os.print(' ');
        os.print("matchedDN = ");
        try {
            (new HexOutputStream(os)).write(matchedDN);
        } catch (java.io.IOException ex) {
            os.print("( unprintable OCTET STRING value )");
        }
        os.println(',');
        for (int ii = 0; ii < indent + 2; ii++) os.print(' ');
        os.print("errorMessage = ");
        try {
            (new HexOutputStream(os)).write(errorMessage);
        } catch (java.io.IOException ex) {
            os.print("( unprintable OCTET STRING value )");
        }
        os.println(',');
        if (referral != null) {
            for (int ii = 0; ii < indent + 2; ii++) os.print(' ');
            os.print("referral = ");
            referral.print(os, indent + 2);
        }
        os.println(',');
        if (responseName != null) {
            for (int ii = 0; ii < indent + 2; ii++) os.print(' ');
            os.print("responseName = ");
            try {
                (new HexOutputStream(os)).write(responseName);
            } catch (java.io.IOException ex) {
                os.print("( unprintable OCTET STRING value )");
            }
        }
        os.println(',');
        if (response != null) {
            for (int ii = 0; ii < indent + 2; ii++) os.print(' ');
            os.print("response = ");
            try {
                (new HexOutputStream(os)).write(response);
            } catch (java.io.IOException ex) {
                os.print("( unprintable OCTET STRING value )");
            }
        }
        os.println();
        for (int ii = 0; ii < indent; ii++) os.print(' ');
        os.print('}');
    }
}
