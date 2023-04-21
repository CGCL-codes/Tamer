package gov.lanl.RAD.Impl;

import nu.xom.*;
import java.util.Hashtable;

/**
 * Maintains a mapping from Id to corresponding policyname
 * @author Srikanth Kotha
 */
public class PolicyReader {

    Hashtable<String, String> policyName;

    private static org.apache.log4j.Logger cat = org.apache.log4j.Logger.getLogger(PolicyReader.class.getName());

    /**
     * Creates a new HashTable and reads the id-policyname pairs into it from policyname.xml
     */
    public PolicyReader(String policynameFile) {
        policyName = new Hashtable<String, String>(30, (float) 0.75);
        readPolicies(policynameFile);
    }

    /**
     * Traverse the Node and put id-policyname pairs into Hashtable
     */
    private void traverseTX(Element node) {
        String Id;
        Element el = (Element) node;
        if (el.getLocalName().equals("RESOURCENAME")) {
            Id = el.getAttribute("Id").getValue();
            policyName.put(Id, el.getValue());
        } else if (el.getLocalName().equals("ROOT")) {
            Elements nl = node.getChildElements();
            int size = nl.size();
            for (int i = 0; i < size; i++) {
                traverseTX(nl.get(i));
            }
        }
    }

    /**
     * Opens the policyname XML file and passes it to TraverseTX for traversal
     */
    public void readPolicies(String fileName) {
        cat.debug("Entering readPolicies");
        if (fileName != null) {
            cat.debug("readPolicies: " + fileName);
            Element root = null;
            try {
                Builder docBuilder = new Builder();
                Document doc = docBuilder.build(fileName);
                root = (Element) doc.getRootElement();
            } catch (Exception e) {
                cat.error("readPolicies failed to read: " + fileName + " " + e);
            }
            traverseTX(root);
        }
    }

    /**
     * get the policy name associated with the input indentifier (ResourceName)
     * @param Id is used to return the policyname
     * @return the policyname corresponding to the Id
     */
    public String getPolicyName(String Id) {
        String polOb = policyName.get(Id);
        if (polOb == null) {
            polOb = policyName.get("Default");
        }
        return polOb;
    }
}
