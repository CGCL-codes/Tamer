package de.juwimm.cms.content.modules;

import static de.juwimm.cms.common.Constants.*;
import java.util.Properties;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import de.juwimm.cms.content.ContentManager;
import de.juwimm.cms.content.event.EditpaneFiredEvent;
import de.juwimm.cms.content.panel.PanOnlyButton;
import de.juwimm.util.XercesHelper;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author <a href="mailto:s.kulawik@juwimm.com">Sascha-Matthias Kulawik</a>
 * @version $Id: Anchor.java 8 2009-02-15 08:54:54Z skulawik $
 */
public class Anchor extends AbstractModule {

    private boolean imEnabled = true;

    private String anchor = "";

    private PanOnlyButton panBtn;

    public void setCustomProperties(String methodname, Properties parameters) {
        super.setCustomProperties(methodname, parameters);
    }

    /**
	 * We haven't implemented the isValid Method here, because this Module should only be used inside a WYSIWYG.<br>
	 * Therefor we also can open a JOptionPane.
	 *
	 * @return Everytime true here
	 */
    public boolean isValid() {
        if (isMandatory()) {
            boolean retVal = true;
            setValidationError("");
            if (anchor == null || anchor.equalsIgnoreCase("")) {
                appendValidationError(rb.getString("exception.AnchorRequired"));
                retVal = false;
            }
            return retVal;
        }
        return true;
    }

    public JDialog viewModalUI(boolean modal) {
        Thread t = new Thread(new ShowOptionPane(this));
        if (modal) {
            t.run();
        } else {
            t.setPriority(Thread.NORM_PRIORITY);
            t.start();
        }
        return null;
    }

    /**
	 * 
	 */
    private class ShowOptionPane implements Runnable {

        private Module module = null;

        public ShowOptionPane(Module module) {
            this.module = module;
        }

        public void run() {
            String newAnchor = JOptionPane.showInputDialog(rb.getString("content.modules.anchor.insertAnchorName"), anchor);
            if (newAnchor != null && !newAnchor.equals("")) {
                anchor = newAnchor;
                EditpaneFiredEvent efe = new EditpaneFiredEvent(module);
                runEditpaneFiredEvent(efe);
                setSaveable(true);
            } else if (newAnchor == null) {
                EditpaneFiredEvent efe = new EditpaneFiredEvent(module);
                runEditpaneCancelEvent(efe);
                setSaveable(false);
            }
        }
    }

    public JPanel viewPanelUI() {
        panBtn = new PanOnlyButton(this, true);
        panBtn.setEnabled(imEnabled);
        return panBtn;
    }

    public void load() {
    }

    public Node getProperties() {
        Element root = ContentManager.getDomDoc().createElement("root");
        Element elm = ContentManager.getDomDoc().createElement("a");
        elm.setAttribute("name", getURLEncoded(anchor));
        elm.setAttribute("type", "anchor");
        setDescription(anchor);
        root.appendChild(elm);
        return root;
    }

    public void setProperties(Node node) {
        if (node != null) {
            try {
                anchor = ((Element) XercesHelper.findNode(node, "./a")).getAttribute("name");
                anchor = AbstractModule.getURLDecoded(anchor);
            } catch (Exception exe) {
            }
        }
    }

    public String getPaneImage() {
        return "16_komp_anchor.gif";
    }

    public String getIconImage() {
        return "16_komp_anchor.gif";
    }

    public void setEnabled(boolean enabling) {
        if (panBtn != null) panBtn.setEnabled(enabling);
        imEnabled = enabling;
    }

    public void recycle() {
    }
}
