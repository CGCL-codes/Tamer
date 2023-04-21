package org.sourceforge.zlang.ui;

import java.awt.event.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import javax.swing.*;
import javax.swing.JOptionPane;
import org.sourceforge.zlang.model.IndentedWriter;
import org.sourceforge.zlang.model.XMLCreator;
import org.sourceforge.zlang.model.XMLSerializer;
import org.sourceforge.zlang.model.ZClass;
import org.sourceforge.zlang.model.ZElement;
import org.sourceforge.zlang.model.ZFile;
import org.xml.sax.SAXException;

/**
 * Changes package for a file.
 * 
 * @author <a href="Tim.Lebedkov@web.de">Tim Lebedkov</a>
 * @version $Id: ExportToXMLAction.java,v 1.2 2002/12/04 22:49:05 hilt2 Exp $
 */
public class ExportToXMLAction extends NodeAction {

    /**
     * Constructor for ChangeClassNameAction.
     *
     * @param tree Zlang tree component
     */
    public ExportToXMLAction(ZlangTree tree) {
        super("Export to XML", tree);
    }

    public void update(ZElement el) {
        setEnabled(el instanceof ZFile);
    }

    public void actionPerformed(ZElement el) {
        ZFile c = (ZFile) el;
        File f = c.getFile();
        String n = f.getName();
        n = n.substring(0, n.lastIndexOf('.')) + ".zl";
        f = new File(f.getParent(), n);
        if (f.exists()) {
            int ret = JOptionPane.showConfirmDialog(MainFrame.getInstance(), "File " + f + " already exists.\n" + "Would you like to overwrite it?", "Overwrite?", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            if (ret == JOptionPane.NO_OPTION) return;
        }
        try {
            IndentedWriter w = new IndentedWriter(new BufferedWriter(new FileWriter(f)));
            XMLSerializer xmls = new XMLSerializer(w);
            xmls.setPublicID(ZFile.PUBLIC_ID);
            xmls.setSystemID("zlang.dtd");
            c.printXML(new XMLCreator(xmls));
            w.close();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(MainFrame.getInstance(), "Error writing file " + f + " " + e.getMessage());
        }
    }
}
