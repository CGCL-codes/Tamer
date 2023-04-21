package de.juwimm.cms.gui.admin;

import static de.juwimm.cms.common.Constants.*;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.apache.log4j.Logger;
import de.juwimm.cms.gui.admin.safeguard.AbstractSafeguardPanel;
import de.juwimm.cms.gui.admin.safeguard.PanJAASRealm;
import de.juwimm.cms.gui.admin.safeguard.PanLDAPRealm;
import de.juwimm.cms.gui.admin.safeguard.PanSqlDbRealm;
import de.juwimm.cms.gui.controls.ReloadablePanel;

/**
 * 
 * @author <a href="mailto:carsten.schalm@juwimm.com">Carsten Schalm</a>
 * company Juwi|MacMillan Group Gmbh, Walsrode, Germany
 * @version $Id: PanSafeguardRealmManager.java 8 2009-02-15 08:54:54Z skulawik $
 */
public final class PanSafeguardRealmManager extends JPanel implements ReloadablePanel {

    private static final long serialVersionUID = 1651984704473274824L;

    private static Logger log = Logger.getLogger(PanSafeguardRealmManager.class);

    private JTabbedPane jTabbedPaneRealms = null;

    private PanSqlDbRealm panJdbc = null;

    private PanJAASRealm panJaas = null;

    private PanLDAPRealm panLdap = null;

    /**
	 * This is the default constructor
	 */
    public PanSafeguardRealmManager() {
        super();
        initialize();
    }

    public void unload() {
        log.debug("unload ...");
    }

    public void reload() {
        log.debug("reload ...");
        this.panJdbc.fillRealmList();
    }

    public void save() {
        log.debug("save ...");
    }

    /**
	 * This method initializes this
	 */
    private void initialize() {
        this.setLayout(new GridBagLayout());
        this.setSize(636, 271);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridheight = 1;
        gbc.insets = new java.awt.Insets(10, 10, 0, 10);
        gbc.weighty = 1.0D;
        gbc.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gbc.weightx = 1.0D;
        gbc.gridwidth = 2;
        gbc.fill = java.awt.GridBagConstraints.BOTH;
        this.setPreferredSize(new java.awt.Dimension(636, 271));
        this.setMinimumSize(new java.awt.Dimension(636, 271));
        this.add(getJTabbedPaneRealms(), gbc);
    }

    /**
	 * This method initializes jTabbedPaneRealms	
	 * 	
	 * @return javax.swing.JTabbedPane	
	 */
    private JTabbedPane getJTabbedPaneRealms() {
        if (jTabbedPaneRealms == null) {
            jTabbedPaneRealms = new JTabbedPane();
            jTabbedPaneRealms.add(getPanJdbc(), rb.getString("panel.admin.tab.safeguard.jdbc"));
            jTabbedPaneRealms.add(getPanJaas(), rb.getString("panel.admin.tab.safeguard.jaas"));
            jTabbedPaneRealms.add(getPanLdap(), rb.getString("panel.admin.tab.safeguard.ldap"));
            getPanJdbc().fillRealmList();
            jTabbedPaneRealms.addChangeListener(new ChangeListener() {

                public void stateChanged(ChangeEvent e) {
                    AbstractSafeguardPanel currTab = (AbstractSafeguardPanel) jTabbedPaneRealms.getSelectedComponent();
                    currTab.fillRealmList();
                }
            });
        }
        return jTabbedPaneRealms;
    }

    private PanSqlDbRealm getPanJdbc() {
        if (panJdbc == null) {
            panJdbc = new PanSqlDbRealm();
        }
        return panJdbc;
    }

    private PanJAASRealm getPanJaas() {
        if (panJaas == null) {
            panJaas = new PanJAASRealm();
        }
        return panJaas;
    }

    private PanLDAPRealm getPanLdap() {
        if (panLdap == null) {
            panLdap = new PanLDAPRealm();
        }
        return panLdap;
    }
}
