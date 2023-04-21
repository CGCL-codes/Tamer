package com.prolix.editor.oics.data.lom;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import com.prolix.editor.oics.oicsSend.OICSConnection;
import org.jdom.Element;
import com.prolix.editor.oics.data.lom.datatypes.OICSLOMTaxonPath;
import com.prolix.editor.oics.get.OICSGetData;

public class OICSLOMClassification implements OICSLOMItem {

    private String purpose;

    private String description;

    private String keyword;

    private List taxonPathes;

    /**
	 * 
	 */
    public OICSLOMClassification() {
        super();
        taxonPathes = new ArrayList();
    }

    public boolean isValid() {
        return true;
    }

    public void readJDOM(Element root) {
        Element element = root.getChild("purpose", OICSConnection.lomNS);
        if (element != null) {
            setPurpose(element.getChildText("value", OICSConnection.lomNS));
        }
        element = root.getChild("description", OICSConnection.lomNS);
        if (element != null) {
            setDescription(element.getChildText("string", OICSConnection.lomNS));
        }
        element = root.getChild("keyword", OICSConnection.lomNS);
        if (element != null) {
            setKeyword(element.getChildText("string", OICSConnection.lomNS));
        }
        Iterator it = root.getChildren("taxonPath", OICSConnection.lomNS).iterator();
        while (it.hasNext()) {
            Element tax = (Element) it.next();
            OICSLOMTaxonPath taxonPath = new OICSLOMTaxonPath();
            taxonPath.readJDOM(tax);
            addTaxonPath(taxonPath);
        }
    }

    public void writeJDOM(Element parent) {
        Element classification = new Element("classification", OICSConnection.lomNS);
        parent.addContent(classification);
        Element element;
        Element element2;
        if (getPurpose() != null) {
            element = new Element("purpose", OICSConnection.lomNS);
            classification.addContent(element);
            element2 = new Element("source", OICSConnection.lomNS);
            element.addContent(element2);
            if (getPurpose().equals("group size") || getPurpose().equals("educational setting")) element2.setText(OICSConnection.icoperSource); else element2.setText(OICSConnection.lomSource);
            element2 = new Element("value", OICSConnection.lomNS);
            element.addContent(element2);
            element2.setText(getPurpose());
        }
        if (getDescription() != null) {
            element = new Element("description", OICSConnection.lomNS);
            classification.addContent(element);
            element2 = new Element("string", OICSConnection.lomNS);
            element.addContent(element2);
            element2.setText(getDescription());
        }
        if (getKeyword() != null) {
            element = new Element("keyword", OICSConnection.lomNS);
            classification.addContent(element);
            element2 = new Element("string", OICSConnection.lomNS);
            element.addContent(element2);
            element2.setText(getKeyword());
        }
        Iterator it = taxonPathes.iterator();
        while (it.hasNext()) {
            ((OICSLOMTaxonPath) it.next()).writeJDOM(classification);
        }
    }

    /**
	 * @return the purpose
	 */
    public String getPurpose() {
        return purpose;
    }

    /**
	 * @param purpose
	 *           the purpose to set
	 */
    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    /**
	 * @return the description
	 */
    public String getDescription() {
        return description;
    }

    /**
	 * @param description
	 *           the description to set
	 */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
	 * @return the keyword
	 */
    public String getKeyword() {
        return keyword;
    }

    /**
	 * @param keyword
	 *           the keyword to set
	 */
    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    /**
	 * @return the taxonPathes
	 */
    public List getTaxonPathes() {
        return taxonPathes;
    }

    public boolean addTaxonPath(OICSLOMTaxonPath path) {
        if (path == null || !path.isValid()) {
            return false;
        }
        if (taxonPathes.contains(path)) {
            return false;
        }
        taxonPathes.add(path);
        return true;
    }
}
