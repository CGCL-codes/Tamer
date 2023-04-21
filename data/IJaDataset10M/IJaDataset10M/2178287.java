package android.pim.vcard;

import java.util.ArrayList;

/**
 * Previously used in main vCard handling code but now exists only for testing.
 */
public class VNode {

    public String VName;

    public ArrayList<PropertyNode> propList = new ArrayList<PropertyNode>();

    /** 0:parse over. 1:parsing. */
    public int parseStatus = 1;
}
