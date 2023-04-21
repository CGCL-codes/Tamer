package org.mitre.rt.client.ui.profile.selectrule;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import org.mitre.rt.client.core.MetaManager;
import org.mitre.rt.rtclient.ApplicationType;
import org.mitre.rt.rtclient.ProfileType;

/**
 *
 * @author  JWINSTON
 */
public class SelectRuleAddDialog extends javax.swing.JDialog {

    SelectRuleAddPanel panel = null;

    PropertyChangeListener windowClose = null;

    /**
   * 
   * @param parent    --  GUI parent Frame object
   * @param modal     --  is this window modal?
   * @param app       --  Application element containing this profile
   * @param xmlParent --  XML container element for comments ( SelectRuleRefs )
   * @param propChange
   */
    public SelectRuleAddDialog(java.awt.Frame parent, boolean modal, ApplicationType app, ProfileType profile) {
        super(parent, modal);
        initComponents();
        setWindowClose();
        setPanel(app, profile);
        setWindowLocation();
    }

    private void setWindowLocation() {
        this.setLocationRelativeTo(MetaManager.getMainWindow());
    }

    @Override
    public void addPropertyChangeListener(String propName, PropertyChangeListener propListener) {
        if (panel != null) this.panel.addPropertyChangeListener(propName, propListener);
    }

    private void setWindowClose() {
        this.windowClose = new PropertyChangeListener() {

            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                closeWindow();
            }
        };
    }

    private void setPanel(ApplicationType app, ProfileType profile) {
        panel = new SelectRuleAddPanel(app, profile);
        panel.addPropertyChangeListener("window_close", this.windowClose);
        add(panel);
        pack();
        repaint();
    }

    private void closeWindow() {
        this.dispose();
    }

    private void initComponents() {
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Add New Select Rule");
        setModal(true);
        getAccessibleContext().setAccessibleName("Add New Rules");
        pack();
    }
}
