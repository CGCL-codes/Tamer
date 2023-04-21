package org.snmp4j.smi;

import java.io.IOException;
import java.io.OutputStream;
import org.snmp4j.asn1.BERInputStream;

@SuppressWarnings("serial")
public class DisplayString extends AbstractVariable {

    public OctetString octet;

    public DisplayString(String s) {
        if (s == null) throw new IllegalArgumentException("string cannot be null.");
        octet = new OctetString(s);
    }

    public DisplayString(OctetString octet) {
        this.octet = octet;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) return false;
        if (!(o instanceof DisplayString)) return false;
        DisplayString ds = (DisplayString) o;
        return this.octet.equals(ds.octet);
    }

    @Override
    public int compareTo(Object o) {
        DisplayString ds = (DisplayString) o;
        return octet.compareTo(ds.octet);
    }

    @Override
    public int hashCode() {
        return octet.hashCode();
    }

    @Override
    public int getBERLength() {
        return octet.getBERLength();
    }

    @Override
    public void decodeBER(BERInputStream inputStream) throws IOException {
        octet.decodeBER(inputStream);
    }

    @Override
    public void encodeBER(OutputStream outputStream) throws IOException {
        octet.encodeBER(outputStream);
    }

    @Override
    public int getSyntax() {
        return octet.getSyntax();
    }

    @Override
    public String toString() {
        return octet.toString();
    }

    @Override
    public Object clone() {
        return new DisplayString(new OctetString(octet));
    }

    public String getValue() {
        return new String(octet.getValue());
    }

    public void setValue(String s) {
        this.octet = new OctetString(s);
    }

    @Override
    public void fromSubIndex(OID subIndex, boolean impliedLength) {
        octet.fromSubIndex(subIndex, impliedLength);
    }

    @Override
    public int toInt() {
        return octet.toInt();
    }

    @Override
    public long toLong() {
        return octet.toLong();
    }

    @Override
    public OID toSubIndex(boolean impliedLength) {
        return octet.toSubIndex(impliedLength);
    }
}
