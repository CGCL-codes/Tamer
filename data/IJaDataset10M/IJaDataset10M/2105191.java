package graphlab.ui.components.gbody;

import javax.swing.JPanel;

/**
 * this is an interface which is used to give a JPanel,
 * in graphlab the Graph implements this.
 * the program should put a JPanel in the Body of the GFrame,
 * so it finds the JPanel via the getPanel method of the class that specified in the body tag of XML.
 * @author azin azadi
 */
public interface GBodyPane {

    public JPanel getPanel();
}
