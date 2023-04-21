package net.sf.lavabeans.testbean.pubmed;

import net.sf.lavabeans.annotations.Node;
import net.sf.lavabeans.annotations.NodeType;

/**
 * <pre>
&lt;!ELEMENT PubMedPubDate (%normal.date;)>
&lt;!ATTLIST PubMedPubDate
	PubStatus %pub.status; #REQUIRED
>
 * </pre>
 */
public class PubMedPubDate extends NormalDate {

    private String pubStatus;

    @Node(type = NodeType.Attribute)
    public String getPubStatus() {
        return pubStatus;
    }

    public void setPubStatus(String pubStatus) {
        this.pubStatus = pubStatus;
    }

    public String toString() {
        return super.toString() + " status=" + pubStatus;
    }
}
