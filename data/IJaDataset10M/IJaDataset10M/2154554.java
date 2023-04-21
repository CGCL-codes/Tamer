package ejb.bprocess.opac;

/**
 *
 * @author  administrator
 */
public class AuthorCreatorBrowseXMLParser {

    /** Creates a new instance of AuthorCreatorBrowseXMLParser */
    public AuthorCreatorBrowseXMLParser() {
    }

    public String personalNameDisplayString(String xmlStr) {
        String numeration = "", titles = "", titlework = "", dateAsso = "", fullname = "", str = "";
        try {
            if (xmlStr != null && !xmlStr.trim().equals("")) {
                org.jdom.input.SAXBuilder sax = new org.jdom.input.SAXBuilder();
                org.jdom.Document doc = sax.build(new java.io.StringReader(xmlStr));
                org.jdom.Element root = doc.getRootElement();
                java.util.List childList = root.getChildren();
                java.util.List dataList = ((org.jdom.Element) childList.get(0)).getChildren();
                for (int i = 0; i < dataList.size(); i++) {
                    org.jdom.Element sf = (org.jdom.Element) dataList.get(i);
                    if (sf.getAttributeValue("code").equals("b")) numeration = sf.getText();
                    if (sf.getAttributeValue("code").equals("c")) titles = titles + " " + sf.getText() + ",";
                    if (sf.getAttributeValue("code").equals("t")) titlework = sf.getText();
                    if (sf.getAttributeValue("code").equals("d")) dateAsso = sf.getText();
                    if (sf.getAttributeValue("code").equals("q")) fullname = sf.getText();
                }
                if (!titles.equals("")) titles = "," + titles;
                if (!fullname.equals("")) fullname = "(" + " " + fullname + " " + ")";
                if (!dateAsso.equals("")) dateAsso = " " + "," + dateAsso;
                if (!titlework.equals("")) titlework = "." + " " + titlework;
                str = titles + fullname + dateAsso + titlework;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return str;
    }

    public String corporateNameDisplayString(String xmlStr) {
        String subord = "", loc = "", titlework = "", dateAsso = "", str = "";
        try {
            if (xmlStr != null && !xmlStr.trim().equals("")) {
                org.jdom.input.SAXBuilder sax = new org.jdom.input.SAXBuilder();
                org.jdom.Document doc = sax.build(new java.io.StringReader(xmlStr));
                org.jdom.Element root = doc.getRootElement();
                java.util.List childList = root.getChildren();
                java.util.List dataList = ((org.jdom.Element) childList.get(0)).getChildren();
                for (int i = 0; i < dataList.size(); i++) {
                    org.jdom.Element sf = (org.jdom.Element) dataList.get(i);
                    if (sf.getAttributeValue("code").equals("b")) subord = subord + sf.getText() + ",";
                    if (sf.getAttributeValue("code").equals("c")) loc = sf.getText();
                    if (sf.getAttributeValue("code").equals("t")) titlework = sf.getText();
                    if (sf.getAttributeValue("code").equals("d")) dateAsso = dateAsso + sf.getText() + ",";
                }
                if (!subord.equals("")) subord = "--" + subord;
                if (!loc.equals("")) loc = "--" + loc;
                if (!dateAsso.equals("")) dateAsso = "--" + dateAsso;
                if (!titlework.equals("")) titlework = "." + " " + titlework;
                str = subord + loc + dateAsso + titlework;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return str;
    }

    public String meetingNameDisplayString(String xmlStr) {
        String loc = "", titlework = "", dateAsso = "", str = "";
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
                    if (sf.getAttributeValue("code").equals("t")) titlework = sf.getText();
                    if (sf.getAttributeValue("code").equals("d")) dateAsso = sf.getText();
                }
                if (!loc.equals("")) loc = "--" + loc;
                if (!dateAsso.equals("")) dateAsso = "--" + dateAsso;
                if (!titlework.equals("")) titlework = "." + " " + titlework;
                str = loc + dateAsso + titlework;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return str;
    }

    public String uniformTitleDisplayString(String xmlStr) {
        String datework = "", lang = "", namepart = "", str = "", ver = "";
        ejb.bprocess.util.Utility utility = ejb.bprocess.util.Utility.getInstance();
        try {
            if (xmlStr != null && !xmlStr.trim().equals("")) {
                org.jdom.input.SAXBuilder sax = new org.jdom.input.SAXBuilder();
                org.jdom.Document doc = sax.build(new java.io.StringReader(xmlStr));
                org.jdom.Element root = doc.getRootElement();
                java.util.List childList = root.getChildren();
                java.util.List dataList = ((org.jdom.Element) childList.get(0)).getChildren();
                for (int i = 0; i < dataList.size(); i++) {
                    org.jdom.Element sf = (org.jdom.Element) dataList.get(i);
                    if (sf.getAttributeValue("code").equals("f")) datework = utility.getTestedString(sf.getText());
                    if (sf.getAttributeValue("code").equals("l")) lang = utility.getTestedString(sf.getText());
                    if (sf.getAttributeValue("code").equals("s")) ver = utility.getTestedString(sf.getText());
                    if (sf.getAttributeValue("code").equals("p")) namepart = namepart + utility.getTestedString(sf.getText());
                }
                if (!datework.equals("")) datework = "--" + datework;
                if (!lang.equals("")) lang = "--" + lang;
                if (!ver.equals("")) ver = "." + " " + ver;
                if (!namepart.equals("")) namepart = "." + " " + namepart;
                str = datework + lang + ver + namepart;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return str;
    }

    public String subDivisionDisplayString(String xmlStr) {
        String form = "", gen = "", chron = "", geo = "", str = "";
        ejb.bprocess.util.Utility utility = ejb.bprocess.util.Utility.getInstance();
        try {
            if (xmlStr != null && !xmlStr.trim().equals("")) {
                org.jdom.input.SAXBuilder sax = new org.jdom.input.SAXBuilder();
                org.jdom.Document doc = sax.build(new java.io.StringReader(xmlStr));
                org.jdom.Element root = doc.getRootElement();
                java.util.List childList = root.getChildren();
                java.util.List dataList = ((org.jdom.Element) childList.get(0)).getChildren();
                for (int i = 0; i < dataList.size(); i++) {
                    org.jdom.Element sf = (org.jdom.Element) dataList.get(i);
                    if (sf.getAttributeValue("code").equals("v")) form = form + utility.getTestedString(sf.getText()) + ",";
                    if (sf.getAttributeValue("code").equals("x")) gen = gen + utility.getTestedString(sf.getText()) + ",";
                    if (sf.getAttributeValue("code").equals("y")) chron = chron + utility.getTestedString(sf.getText()) + ",";
                    if (sf.getAttributeValue("code").equals("z")) geo = geo + utility.getTestedString(sf.getText()) + ",";
                }
                if (!form.equals("")) form = "--" + form;
                if (!gen.equals("")) gen = "--" + gen;
                if (!chron.equals("")) chron = "--" + chron;
                if (!geo.equals("")) geo = "--" + geo;
                str = form + gen + chron + geo;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return str;
    }
}
