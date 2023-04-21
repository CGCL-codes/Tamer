package issrg.policytester;

import issrg.editor2.PEApplication;
import issrg.utils.gui.xml.XMLChangeEvent;
import issrg.utils.gui.xml.XMLChangeListener;
import java.util.Vector;
import javax.swing.JComboBox;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 * @author Christian Azzopardi
 */
public class EnvironmentParametersDropDownList extends JComboBox implements XMLChangeListener {

    /**
     * String Array that stores, in correct sequence, the type of variable.
     * This can be one of five types: String/Integer/Time/Boolean/Real.
     */
    public String[] types;

    Vector itemTypes;

    /** Creates a new instance of EnvironmentParametersDropDownList */
    public EnvironmentParametersDropDownList() {
        Vector items = new Vector();
        itemTypes = new Vector();
        PTApplication.getConfig().addXMLChangeListener(this);
        refreshList();
    }

    public void refreshList() {
        this.removeAllItems();
        for (int i = 0; i < getEnvironmentVariables().length; i++) {
            this.addItem(getEnvironmentVariables()[i]);
        }
    }

    public String[] getEnvironmentVariables() {
        if (PTApplication.getConfig() == null) return null;
        String envNames[] = null;
        if (getEnvironmentParentNode() != null) {
            NodeList nlist = ((Element) getEnvironmentParentNode()).getElementsByTagName("EnvironmentVariable");
            Node n;
            envNames = new String[nlist.getLength()];
            itemTypes = new Vector();
            for (int i = 0; i < nlist.getLength(); i++) {
                n = nlist.item(i);
                envNames[i] = (((Element) n).getAttribute("Name"));
                itemTypes.add(((Element) n).getAttribute("Type"));
            }
        }
        return envNames;
    }

    /**
     * Returns a String array Variable, with the vector contents passed as
     * a parameter.
     *
     * @param v   A vector that contains the data to be transformed to an array.
     *
     * @return  a String array containing the vector elements.
     */
    public String[] fillStringArray(Vector v) {
        String[] tmpArray = new String[v.size()];
        for (int i = 0; i < v.size(); i++) {
            tmpArray[i] = (String) v.get(i);
        }
        return tmpArray;
    }

    /**
     * Obtains and returns the <EnvironmentParameters> Node.
     *
     * @return   The <EnvironmentParameters> Node in the pe.cfg file.
     */
    public Node getEnvironmentParentNode() {
        return PTApplication.getConfig().DOM.getElementsByTagName("EnvironmentParameters").item(0);
    }

    public String[] getTypes() {
        return fillStringArray(itemTypes);
    }

    public void XMLChanged(XMLChangeEvent e) {
        refreshList();
    }
}
