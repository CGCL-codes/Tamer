package org.furthurnet.xmlparser;

import org.furthurnet.furi.Res;
import org.furthurnet.furi.ServiceManager;

public class SearchParamSet {

    public String networkType = null;

    public String networkVersion = null;

    public String furthurVersion = null;

    public boolean firewall = false;

    public String objectID = null;

    public String toString() {
        String str = "\tnetworkType: " + networkType + "\n" + "\tnetworkVersion: " + networkVersion + "\n" + "\tfurthurVersion: " + furthurVersion + "\n" + "\tfirewall: " + firewall + "\n" + "\tobjectID: " + objectID + "\n";
        return str;
    }

    public SearchParam[] param = new SearchParam[NetworkSpec.MAX_SEARCH_PARAMS];

    public int numParams = 0;

    public SearchParamSet(XmlObject obj) throws XmlException {
        if (!obj.getName().equals(XmlTags.QUERY_SPEC)) {
            throw new XmlException("Could not read element " + XmlTags.QUERY_SPEC);
        } else {
            for (int i = 0; i < obj.numAttributes(); i++) {
                if (i >= NetworkSpec.MAX_SEARCH_PARAMS) throw new XmlException("There is a maximum of " + NetworkSpec.MAX_SEARCH_PARAMS + " search parameters");
                XmlObject attr = obj.getAttribute(i);
                if (attr.getName().equals(XmlTags.QUERY_PARAM)) {
                    param[numParams] = new SearchParam();
                    param[numParams].setName(attr.getAttribute(XmlTags.QUERY_NAME).getValue());
                    param[numParams].setValue(attr.getAttribute(XmlTags.QUERY_VALUE).getValue());
                    numParams++;
                } else if (attr.getName().equals(XmlTags.NETWORK_TYPE)) networkType = attr.getValue(); else if (attr.getName().equals(XmlTags.NETWORK_VERSION)) networkVersion = attr.getValue(); else if (attr.getName().equals(XmlTags.FURTHUR_VERSION)) furthurVersion = attr.getValue(); else if (attr.getName().equals(XmlTags.FIREWALL)) firewall = (attr.getValue().equals("YES")); else if (attr.getName().equals(XmlTags.ID)) objectID = attr.getValue();
            }
            if ((networkType == null) || (networkType.length() == 0)) throw new XmlException("Could not read element " + XmlTags.NETWORK_TYPE);
        }
    }

    public static AttributeSet generateAttributeSet(String attrXml) {
        try {
            XmlObject obj = (XmlObject) XmlObject.parse(attrXml).elementAt(0);
            return new AttributeSet(obj);
        } catch (Exception e) {
        }
        return null;
    }

    public boolean isBandSearch() {
        for (int i = 0; i < numParams; i++) {
            if (param[i].getName().matches("Band Name")) {
                return true;
            }
        }
        return false;
    }

    public boolean matchesAttributes(AttributeSet attrSet, NetworkSpec ns) {
        try {
            boolean matched = false;
            for (int i = 0; i < numParams; i++) if (param[i].numTerms > 0) {
                if (param[i].matchesAttributes(attrSet, ns)) matched = true; else return false;
            }
            return matched;
        } catch (Exception e) {
            return false;
        }
    }

    public static String generateExactMatchSearchXml(String id, NetworkSpec ns) {
        try {
            String firewallStr = "NO";
            if (ServiceManager.getManager().getMainFrame().isFirewall()) firewallStr = "YES";
            return "<" + XmlTags.QUERY_SPEC + " " + XmlTags.NETWORK_TYPE + "=\"" + ns.getNetworkType() + "\"" + " " + XmlTags.FURTHUR_VERSION + "=\"" + Res.getStr("Program.Version") + "\"" + " " + XmlTags.ID + "=\"" + id + "\"" + " " + XmlTags.FIREWALL + "=\"" + firewallStr + "\"/>";
        } catch (Exception e) {
            return null;
        }
    }
}
