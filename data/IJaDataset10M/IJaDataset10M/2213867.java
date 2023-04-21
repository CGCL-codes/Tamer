package ejb.bprocess.administration;

import org.jdom.*;
import org.jdom.output.*;
import java.util.Vector;

/**
 *
 * @author  Administrator
 */
public class MeetingNameXMLGenerator {

    /** Creates a new instance of MeetingNameXMLGenerator */
    public MeetingNameXMLGenerator() {
    }

    public String getNamesSearchAsExactPhrase(Vector vret) {
        Element retroot = new Element("Response");
        Document retdoc = new Document(retroot);
        for (int i = 0, j = 1; i < vret.size(); i += 6, j++) {
            Element recid = new Element("Record");
            recid.setAttribute("no", String.valueOf(j));
            Element elenew = new Element("MeetingNameId");
            elenew.setText(vret.elementAt(i).toString().trim());
            recid.addContent(elenew);
            elenew = new Element("LibraryId");
            elenew.setText(vret.elementAt(i + 1).toString().trim());
            recid.addContent(elenew);
            elenew = new Element("Name");
            elenew.setText(vret.elementAt(i + 2).toString().trim());
            recid.addContent(elenew);
            elenew = new Element("Location");
            if (vret.elementAt(i + 3) != null) {
                elenew.setText(vret.elementAt(i + 3).toString().trim());
                recid.addContent(elenew);
            }
            elenew = new Element("DateOfMeeting");
            if (vret.elementAt(i + 4) != null) {
                elenew.setText(vret.elementAt(i + 4).toString());
                recid.addContent(elenew);
            }
            Vector vpartnos = (Vector) vret.elementAt(i + 5);
            for (int x = 0; x < vpartnos.size(); x++) {
                elenew = new Element("PartNumber");
                elenew.setText(vpartnos.elementAt(x).toString().trim());
                recid.addContent(elenew);
            }
            try {
                retroot.addContent(recid);
            } catch (Exception jdome) {
                System.out.println(jdome);
            }
        }
        return (new org.jdom.output.XMLOutputter()).outputString(retdoc);
    }

    public String getDisplayString(String xmlStr) {
        String str = "", loc = "", dateofmeet = "", titlework = "";
        try {
            if (xmlStr != null && !xmlStr.trim().equals("")) {
                org.jdom.input.SAXBuilder sax = new org.jdom.input.SAXBuilder();
                org.jdom.Document doc = sax.build(new java.io.StringReader(xmlStr));
                org.jdom.Element root = doc.getRootElement();
                java.util.List childList = root.getChildren();
                java.util.List dataList = ((org.jdom.Element) childList.get(0)).getChildren();
                for (int i = 0; i < dataList.size(); i++) {
                    org.jdom.Element sf = (org.jdom.Element) dataList.get(i);
                    if (sf.getAttributeValue("code").equals("c")) loc = sf.getText();
                    if (sf.getAttributeValue("code").equals("d")) dateofmeet = sf.getText();
                    if (sf.getAttributeValue("code").equals("t")) titlework = sf.getText();
                }
                if (!loc.equals("")) loc = "--" + loc;
                if (!dateofmeet.equals("")) dateofmeet = "--" + dateofmeet;
                if (!titlework.equals("")) titlework = "--" + titlework;
                str = loc + dateofmeet + titlework;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return str;
    }

    public String searchSeeTermXML(java.util.Vector vec) throws Exception {
        java.util.Vector vecObj = new java.util.Vector(1, 1);
        for (int j = 0; j < vec.size(); j += 6) {
            Object obj[] = new Object[6];
            obj[0] = obj[1] = obj[2] = obj[3] = obj[4] = obj[5] = "";
            obj[0] = (String) vec.get(j);
            obj[1] = (String) vec.get(j + 1);
            obj[2] = (String) vec.get(j + 2);
            obj[3] = (String) vec.get(j + 3);
            obj[4] = (String) vec.get(j + 4);
            obj[5] = (String) vec.get(j + 5);
            vecObj.addElement(obj);
        }
        java.util.Collections.sort(vecObj, (new ejb.bprocess.administration.AuthorityFileComparator()));
        Element sres = new Element("SearchResponse");
        for (int i = 0; i < vecObj.size(); i++) {
            Object[] obj = (Object[]) vecObj.elementAt(i);
            Element srec = new Element("SearchRecord");
            Element pid = new Element("MeetingNameID");
            Element libid = new Element("libid");
            Element sid = new Element("SeeID");
            Element sterm = new Element("SeeTerm");
            Element relation = new Element("Relation");
            Element note = new Element("Note");
            sres.addContent(srec);
            pid.setText((String) obj[0]);
            libid.setText((String) obj[1]);
            sid.setText((String) obj[2]);
            sterm.setText((String) obj[3]);
            relation.setText((String) obj[4]);
            note.setText((String) obj[5]);
            srec.addContent(pid);
            srec.addContent(libid);
            srec.addContent(sid);
            srec.addContent(sterm);
            srec.addContent(relation);
            srec.addContent(note);
        }
        Document doc = new Document(sres);
        XMLOutputter out = new XMLOutputter();
        String xml = "";
        xml = out.outputString(doc);
        return xml;
    }

    public String noteXMLGenerator(org.jdom.Element rec) {
        String recXml = "";
        try {
            org.jdom.Document recDoc = new org.jdom.Document(rec);
            org.jdom.output.XMLOutputter recOut = new org.jdom.output.XMLOutputter();
            recXml = recOut.outputString(recDoc);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return recXml;
    }

    public String getMatchedDetails(java.util.Vector vmatched) {
        Element rootele = new Element("Response");
        rootele.setAttribute("MATCH", "Y");
        Document doc = new Document(rootele);
        Element ele = new Element("MeetingNameId");
        ele.setText(vmatched.elementAt(0).toString());
        rootele.addContent(ele);
        ele = new Element("LibraryId");
        ele.setText(vmatched.elementAt(1).toString());
        rootele.addContent(ele);
        ele = new Element("Name");
        ele.setText(vmatched.elementAt(2).toString());
        rootele.addContent(ele);
        ele = new Element("Location");
        ele.setText(vmatched.elementAt(3).toString());
        rootele.addContent(ele);
        ele = new Element("DateOfMeeting");
        ele.setText(vmatched.elementAt(4).toString());
        rootele.addContent(ele);
        java.util.HashSet hs = (java.util.HashSet) vmatched.elementAt(5);
        Object[] obr = hs.toArray();
        for (int i = 0; i < obr.length; i++) {
            ele = new Element("PartNumber");
            ele.setText(obr[i].toString());
            rootele.addContent(ele);
        }
        return (new org.jdom.output.XMLOutputter()).outputString(doc);
    }
}
