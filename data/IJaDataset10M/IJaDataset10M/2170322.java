package org.javaldap.ldapv3;

import java.io.PrintStream;
import com.ibm.util.*;
import com.ibm.asn1.*;

/** This class represents the ASN.1 SEQUENCE type <tt>ModifyDNRequest</tt>.
  * For each sequence member, sequence classes contain a
  * public member variable of the corresponding Java type.
  * @author Snacc for Java
  * @version Fri Jul  2 18:01:43 1999
 
  */
public class ModifyDNRequest implements LDAPv3 {

    /** member variable representing the sequence member entry of type byte[] */
    public byte[] entry;

    /** member variable representing the sequence member newrdn of type byte[] */
    public byte[] newrdn;

    /** member variable representing the sequence member deleteoldrdn of type boolean */
    public boolean deleteoldrdn;

    /** member variable representing the sequence member newSuperior of type byte[] */
    public byte[] newSuperior = null;

    /** default constructor */
    public ModifyDNRequest() {
    }

    /** copy constructor */
    public ModifyDNRequest(ModifyDNRequest arg) {
        entry = new byte[arg.entry.length];
        System.arraycopy(arg.entry, 0, entry, 0, arg.entry.length);
        newrdn = new byte[arg.newrdn.length];
        System.arraycopy(arg.newrdn, 0, newrdn, 0, arg.newrdn.length);
        deleteoldrdn = arg.deleteoldrdn;
        newSuperior = new byte[arg.newSuperior.length];
        System.arraycopy(arg.newSuperior, 0, newSuperior, 0, arg.newSuperior.length);
    }

    /** decoding method.
	* @param dec
	*        decoder object derived from com.ibm.asn1.ASN1Decoder
	* @exception com.ibm.asn1.ASN1Exception 
	*            decoding error
	*/
    public void decode(ASN1Decoder dec) throws ASN1Exception {
        dec.nextIsImplicit(dec.makeTag(dec.APPLICATION_TAG_CLASS, 12));
        int seq_nr = dec.decodeSequence();
        entry = dec.decodeOctetString();
        newrdn = dec.decodeOctetString();
        deleteoldrdn = dec.decodeBoolean();
        if (!dec.nextIsOptional(dec.makeTag(dec.CONTEXT_TAG_CLASS, 0))) {
            dec.nextIsImplicit(dec.makeTag(dec.CONTEXT_TAG_CLASS, 0));
            newSuperior = dec.decodeOctetString();
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
        enc.nextIsImplicit(enc.makeTag(enc.APPLICATION_TAG_CLASS, 12));
        int seq_nr = enc.encodeSequence();
        enc.encodeOctetString(entry);
        enc.encodeOctetString(newrdn);
        enc.encodeBoolean(deleteoldrdn);
        if (newSuperior != null) {
            enc.nextIsImplicit(enc.makeTag(enc.CONTEXT_TAG_CLASS, 0));
            enc.encodeOctetString(newSuperior);
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
        os.print("entry = ");
        try {
            (new HexOutputStream(os)).write(entry);
        } catch (java.io.IOException ex) {
            os.print("( unprintable OCTET STRING value )");
        }
        os.println(',');
        for (int ii = 0; ii < indent + 2; ii++) os.print(' ');
        os.print("newrdn = ");
        try {
            (new HexOutputStream(os)).write(newrdn);
        } catch (java.io.IOException ex) {
            os.print("( unprintable OCTET STRING value )");
        }
        os.println(',');
        for (int ii = 0; ii < indent + 2; ii++) os.print(' ');
        os.print("deleteoldrdn = ");
        os.print(deleteoldrdn);
        os.println(',');
        if (newSuperior != null) {
            for (int ii = 0; ii < indent + 2; ii++) os.print(' ');
            os.print("newSuperior = ");
            try {
                (new HexOutputStream(os)).write(newSuperior);
            } catch (java.io.IOException ex) {
                os.print("( unprintable OCTET STRING value )");
            }
        }
        os.println();
        for (int ii = 0; ii < indent; ii++) os.print(' ');
        os.print('}');
    }
}
