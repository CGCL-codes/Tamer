package net.sf.clairv.p2p.search.util;

public class Doc {

    public int docid;

    public float tf;

    public Doc(int docid, float tf) {
        super();
        this.docid = docid;
        this.tf = tf;
    }

    public String toString() {
        StringBuffer buf = new StringBuffer();
        return buf.append("Doc <").append(docid).append(", ").append(tf).append(">").toString();
    }
}
