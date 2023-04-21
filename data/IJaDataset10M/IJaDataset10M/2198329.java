package de.juwimm.cms.gui.admin;

import static de.juwimm.cms.common.Constants.*;
import java.awt.BorderLayout;
import java.awt.Cursor;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.apache.log4j.Logger;
import de.juwimm.cms.gui.controls.ReloadablePanel;
import de.juwimm.cms.gui.controls.UnloadablePanel;
import de.juwimm.cms.util.ActionHub;

/**
 * <p>Title: juwimm cms</p>
 * <p>Description: content management system</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: juwi macmillan group gmbh</p>
 * @author Dirk Bogun
 * @version $Id: PanAdministrationAdmin.java 8 2009-02-15 08:54:54Z skulawik $
 */
public class PanAdministrationAdmin extends JPanel implements UnloadablePanel {

    private static Logger log = Logger.getLogger(PanAdministrationAdmin.class);

    private JTabbedPane panTab = new JTabbedPane();

    private PanUser panUser;

    private PanUnitGroupPerUser panUserUnits = null;

    public PanAdministrationAdmin() {
        try {
            jbInit();
        } catch (Exception exe) {
            log.error("Initialization Error", exe);
        }
    }

    void jbInit() throws Exception {
        this.setLayout(new BorderLayout());
        panUser = new PanUser();
        if (panUserUnits != null) {
            ActionHub.removeActionListener(panUserUnits);
        }
        panUserUnits = new PanUnitGroupPerUser();
        ActionHub.addActionListener(panUserUnits);
        panTab.add(panUser, rb.getString("panel.admin.tab.user"));
        panTab.add(panUserUnits, rb.getString("panel.admin.tab.unitUser"));
        panTab.setSelectedIndex(panTab.indexOfTab(rb.getString("panel.admin.tab.user")));
        panTab.addChangeListener(new ChangeListener() {

            private int lastIndex = panTab.indexOfTab(rb.getString("panel.admin.tab.user"));

            public void stateChanged(ChangeEvent e) {
                SwingUtilities.invokeLater(new Runnable() {

                    public void run() {
                        setCursor(new Cursor(Cursor.WAIT_CURSOR));
                        String strTabName = panTab.getTitleAt(panTab.getSelectedIndex());
                        if (strTabName.equals(rb.getString("panel.admin.tab.user"))) {
                            if (lastIndex == panTab.indexOfTab(rb.getString("panel.admin.tab.unitUser"))) {
                                panUserUnits.unload();
                            }
                            panUser.reload();
                            lastIndex = panTab.indexOfTab(rb.getString("panel.admin.tab.user"));
                        } else if (strTabName.equals(rb.getString("panel.admin.tab.unitUser"))) {
                            if (lastIndex == panTab.indexOfTab(rb.getString("panel.admin.tab.user"))) {
                                panUser.unload();
                            }
                            panUserUnits.reload();
                            lastIndex = panTab.indexOfTab(rb.getString("panel.admin.tab.unitUser"));
                        }
                        setCursor(Cursor.getDefaultCursor());
                    }
                });
            }
        });
        this.add(panTab, BorderLayout.CENTER);
    }

    public void reload() throws Exception {
        ReloadablePanel rpan = (ReloadablePanel) panTab.getSelectedComponent();
        rpan.reload();
    }

    public void unload() {
        panUser.unload();
        panUserUnits.unload();
    }
}
