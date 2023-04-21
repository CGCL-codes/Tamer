package com.ericsson.nfc.signing.record;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import javax.microedition.contactless.ndef.NDEFRecord;
import javax.microedition.contactless.ndef.NDEFRecordType;

/**
 *
 * @author emarkki
 */
public class SignatureRecord {

    public static final NDEFRecordType RECORD_TYPE = new NDEFRecordType(NDEFRecordType.NFC_FORUM_RTD, "urn:nfc:wkt:Sig");

    public static SignatureRecord createMark() {
        return new SignatureRecord();
    }

    private int version;

    private SignatureField signatureField;

    CertificateField certificateField;

    private boolean marker;

    /**
     * Marker
     */
    public SignatureRecord() {
        this(SignatureField.createMark(), CertificateField.createEmpty());
        this.marker = true;
    }

    public SignatureRecord(SignatureField signatureField, CertificateField certificateField) {
        this.version = 0x01;
        this.signatureField = signatureField;
        this.certificateField = certificateField;
    }

    public SignatureRecord(byte[] bytes, int offset) throws InvalidSignatureRecord {
        decode(bytes, offset);
    }

    public boolean isMarker() {
        return marker;
    }

    public NDEFRecord toNDEFRecord() {
        return new NDEFRecord(RECORD_TYPE, new byte[0], encode());
    }

    public NDEFRecord toNDEFRecord(byte[] recordId) {
        return new NDEFRecord(RECORD_TYPE, recordId, encode());
    }

    public CertificateField getCertificateField() {
        return certificateField;
    }

    public SignatureField getSignatureField() {
        return signatureField;
    }

    public int getVersion() {
        return version;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("SigRec[");
        sb.append(getSignatureField());
        sb.append(", ");
        sb.append(getCertificateField());
        sb.append("]");
        return sb.toString();
    }

    private void decode(byte[] payload, int offset) throws InvalidSignatureRecord {
        try {
            ByteArrayInputStream in = new ByteArrayInputStream(payload);
            byte temp;
            in.skip(offset);
            version = read(in);
            if (version != 0x01) {
                throw new InvalidSignatureRecord(InvalidSignatureRecord.VERSION, "Unexpected version: " + version);
            }
            int signatureType;
            byte[] signature = null;
            URIRecord signatureUri = null;
            temp = (byte) read(in);
            boolean signatureUriPresent = ((temp & 0x80) != 0);
            signatureType = temp & 0x7f;
            if (signatureType >= 0x05) {
                throw new InvalidSignatureRecord(InvalidSignatureRecord.SIGNATURE_TYPE, "Unexpected signature type: " + signatureType);
            }
            marker = (signatureUriPresent == false && signatureType == 0);
            if (marker) {
                this.signatureField = SignatureField.createMark();
                this.certificateField = CertificateField.createEmpty();
            } else {
                int sigLen;
                {
                    int high = read(in) & 0xff;
                    int low = read(in) & 0xff;
                    high <<= 8;
                    sigLen = (high | low) & 0xffff;
                }
                byte[] sigURIField = new byte[sigLen];
                read(in, sigURIField);
                if (!signatureUriPresent) {
                    signature = sigURIField;
                } else {
                    NDEFRecord msg = new NDEFRecord(sigURIField, 0);
                    signatureUri = new URIRecord(msg.getPayload(), 0);
                }
                temp = read(in);
                boolean uriPresent = (temp & 0x80) != 0;
                int certificateFormat = (temp & 0x70) >> 4;
                if (certificateFormat >= 0x02) {
                    throw new InvalidSignatureRecord(InvalidSignatureRecord.CERTIFICATE_FORMAT, "Unexpected certificate format: " + certificateFormat);
                }
                int numCerts = temp & 0x0f;
                CertificateBytes[] certificates = new CertificateBytes[numCerts];
                for (int i = 0; i < numCerts; i++) {
                    int high = read(in) & 0xff;
                    int low = read(in) & 0xff;
                    high <<= 8;
                    int len = (high | low) & 0xffff;
                    byte[] bytes = new byte[len];
                    read(in, bytes);
                    certificates[i] = new CertificateBytes(bytes);
                }
                URIRecord certificateUri = null;
                if (uriPresent) {
                    NDEFRecord msg = new NDEFRecord(payload, payload.length - in.available());
                    certificateUri = new URIRecord(msg.getPayload(), 0);
                }
                if (signatureUri != null) {
                    this.signatureField = new SignatureField(signatureType, signatureUri);
                } else {
                    this.signatureField = new SignatureField(signatureType, signature);
                }
                this.certificateField = new CertificateField(certificateFormat, certificates, certificateUri);
            }
        } catch (IOException ex) {
            throw new InvalidSignatureRecord(ex);
        }
    }

    private static byte read(ByteArrayInputStream in) throws IOException {
        int b = in.read();
        if (b == -1) {
            throw new IOException("Unexpected end in record");
        }
        return (byte) (b & 0xff);
    }

    private static byte read(ByteArrayInputStream in, byte[] bytes) throws IOException {
        int b = in.read(bytes);
        if (b != bytes.length) {
            throw new IOException("Unexpected end in record");
        }
        return (byte) (b & 0xff);
    }

    private byte[] encode() {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte temp;
        out.write((byte) version);
        temp = (byte) signatureField.getType();
        if (signatureField.getUri() != null) {
            temp |= 0x80;
        }
        out.write(temp);
        if (!isMarker()) {
            byte[] sigUriField;
            if (signatureField.getUri() != null) {
                sigUriField = signatureField.getUri().toNDEFRecord().toByteArray();
            } else {
                sigUriField = signatureField.getSignature();
            }
            if (sigUriField.length > 0xffff) {
                throw new IllegalArgumentException("Signature/URI too long: " + sigUriField.length);
            }
            byte high = (byte) (sigUriField.length >> 8);
            byte low = (byte) (sigUriField.length & 0xff);
            out.write(high);
            out.write(low);
            out.write(sigUriField, 0, sigUriField.length);
        }
        if (!isMarker()) {
            boolean certUriPresent = certificateField.getUri() != null;
            temp = 0;
            if (certUriPresent) {
                temp = (byte) 0x80;
            }
            temp |= certificateField.getFormat() << 4;
            if (certificateField.getChain().length > 15) {
                throw new IllegalArgumentException("Too many certificates: " + certificateField.getChain().length);
            }
            temp |= certificateField.getChain().length;
            out.write(temp);
            for (int i = 0; i < certificateField.getChain().length; i++) {
                byte[] bytes = certificateField.getChain()[i].getBytes();
                if (bytes.length > 0xffff) {
                    throw new IllegalArgumentException("Certificate " + i + " too long: " + bytes.length);
                }
                byte high = (byte) (bytes.length >> 8);
                byte low = (byte) (bytes.length & 0xff);
                out.write(high);
                out.write(low);
                out.write(bytes, 0, bytes.length);
            }
            if (certUriPresent) {
                byte[] b = certificateField.getUri().toNDEFRecord().toByteArray();
                out.write(b, 0, b.length);
            }
        }
        return out.toByteArray();
    }

    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final SignatureRecord other = (SignatureRecord) obj;
        if (this.version != other.version) {
            return false;
        }
        if (this.signatureField != other.signatureField && (this.signatureField == null || !this.signatureField.equals(other.signatureField))) {
            return false;
        }
        if (this.certificateField != other.certificateField && (this.certificateField == null || !this.certificateField.equals(other.certificateField))) {
            return false;
        }
        if (this.marker != other.marker) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        int hash = 3;
        hash = 29 * hash + this.version;
        hash = 29 * hash + (this.signatureField != null ? this.signatureField.hashCode() : 0);
        hash = 29 * hash + (this.certificateField != null ? this.certificateField.hashCode() : 0);
        hash = 29 * hash + (this.marker ? 1 : 0);
        return hash;
    }
}
