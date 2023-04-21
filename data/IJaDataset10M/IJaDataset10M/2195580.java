package org.dcm4chee.xds.common.store;

public class StoredDocument implements XDSDocumentIdentifier {

    private String docUid;

    private String desc;

    private String hashString;

    private long size;

    public StoredDocument(String docUid, long size, byte[] hash, String desc) {
        this.docUid = docUid;
        this.size = size;
        this.hashString = toHexString(hash);
        this.desc = desc;
    }

    public StoredDocument(String docUid, long size, String hash, String desc) {
        this.docUid = docUid;
        this.size = size;
        this.hashString = hash;
        this.desc = desc;
    }

    private String toHexString(byte[] hash) {
        StringBuffer sb = new StringBuffer();
        String h;
        for (int i = 0; i < hash.length; i++) {
            h = Integer.toHexString(hash[i] & 0xff);
            if (h.length() == 1) h = "0" + h;
            sb.append(h);
        }
        return sb.toString();
    }

    /** Unique ID of this Stored XDS Document (XDSDocumentEntry.uniqueId) */
    public String getDocumentUID() {
        return docUid;
    }

    /** Description of this Stored XDS Document */
    public String getDescription() {
        return desc;
    }

    /** Size of the Document */
    public long getSize() {
        return size;
    }

    /** Hash of this Document (SHA1 MessageDigest) */
    public String getHash() {
        return hashString;
    }
}
