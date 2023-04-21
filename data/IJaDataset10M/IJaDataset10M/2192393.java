package de.juwimm.cms.content.modules;

import java.util.ArrayList;
import java.util.Properties;
import javax.swing.JDialog;
import javax.swing.JPanel;
import org.w3c.dom.Node;
import de.juwimm.cms.content.frame.DlgModalModule;
import de.juwimm.cms.content.panel.PanCheckBox;
import de.juwimm.util.XercesHelper;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author Sabarinath Ayyappan
 * @version $Id: CheckBoxModule.java 8 2009-02-15 08:54:54Z skulawik $
 */
public class CheckBoxModule extends AbstractModule {

    private PanCheckBox pan = new PanCheckBox();

    private ArrayList<Properties> vec = new ArrayList<Properties>();

    public String getPaneImage() {
        return "";
    }

    public String getIconImage() {
        return "";
    }

    public Object clone() {
        CheckBoxModule module = (CheckBoxModule) super.clone(false);
        return module;
    }

    public void setCustomProperties(String methodname, Properties parameters) {
        super.setCustomProperties(methodname, parameters);
        pan.setCustomProperties(methodname, parameters);
        vec.add(parameters);
    }

    public boolean isValid() {
        return true;
    }

    public JPanel viewPanelUI() {
        return pan;
    }

    public JDialog viewModalUI(boolean modal) {
        DlgModalModule frm = new DlgModalModule(this, pan, 150, 450, modal);
        frm.setVisible(true);
        return frm;
    }

    public void load() {
    }

    public void setProperties(Node node) {
        setDescription(XercesHelper.getNodeValue(node.getFirstChild()));
        pan.setProperties(node);
    }

    public Node getProperties() {
        Node node = pan.getProperties();
        try {
            setDescription(XercesHelper.getNodeValue(node.getFirstChild()));
        } catch (Exception exe) {
        }
        return node;
    }

    /**
	 * Function which enables the toolbar button
	 * @param enabling
	 */
    public void setEnabled(boolean enabling) {
        pan.setEnabled(enabling);
    }

    public void recycle() {
        pan.clear();
    }
}
