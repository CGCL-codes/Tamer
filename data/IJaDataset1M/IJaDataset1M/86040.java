package uk.ac.ebi.intact.plugins.dbtest.xmlimport;

import java.util.List;
import java.util.ArrayList;

/**
 * Contains xml filesets
 *
 * @author Bruno Aranda (baranda@ebi.ac.uk)
 * @version $Id: Imports.java 6741 2006-11-16 11:20:38Z baranda $
 */
public class Imports {

    private List<XmlFileset> xmlFilesets;

    public Imports() {
        this.xmlFilesets = new ArrayList<XmlFileset>();
    }

    public List<XmlFileset> getXmlFilesets() {
        return xmlFilesets;
    }

    public void setXmlFilesets(List<XmlFileset> xmlFilesets) {
        this.xmlFilesets = xmlFilesets;
    }
}
