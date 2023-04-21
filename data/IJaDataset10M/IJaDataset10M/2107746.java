package net.etherstorm.jopenrpg.swing;

import javax.swing.*;
import org.jdom.Element;
import java.awt.BorderLayout;
import org.jdom.output.XMLOutputter;

/**
 * 
 * 
 * @author $Author: tedberg $
 * @version $Revision: 1.6 $
 * $Date: 2002/05/06 05:00:33 $
 */
public class JXMLViewPanel extends JPanel {

    String originalXML;

    /**
	 *
	 */
    public JXMLViewPanel(Element myElement) {
        super(new BorderLayout());
        XMLOutputter xout = new XMLOutputter();
        originalXML = xout.outputString(myElement);
        xout.setIndent("  ");
        xout.setNewlines(true);
        xout.setLineSeparator("\n");
        xout.setTextNormalize(true);
        JTextArea f = new JTextArea(xout.outputString(myElement));
        f.setFont(new java.awt.Font("monospaced", java.awt.Font.PLAIN, 12));
        f.setEditable(false);
        add(new JScrollPane(f));
    }

    public String getXMLText() {
        return originalXML;
    }
}
