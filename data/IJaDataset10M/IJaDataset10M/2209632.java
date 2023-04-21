package org.sidora.strata.explorer;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;
import java.util.TreeSet;
import java.util.logging.Level;
import javax.swing.DefaultListModel;
import javax.swing.UIManager;
import org.openide.ErrorManager;
import org.openide.util.NbBundle;
import org.openide.windows.TopComponent;
import org.openide.windows.WindowManager;
import org.sidora.core.Site;
import org.sidora.core.Zone;
import org.sidora.core.util.LoggerProvider;
import org.sidora.core.util.ParameterCollector;
import org.sidora.strata.SidoraTools;
import org.sidora.strata.Storage;

/**
 * Zone Explorer Form
 * @author Enric Tartera, Juan Manuel Gimeno, Roger Masgoret
 * @version 1.0
 */
final class ZoneExplorerTopComponent extends TopComponent {

    private static ZoneExplorerTopComponent instance;

    private static final String PREFERRED_ID = "ZoneExplorerTopComponent";

    private ZoneExplorerTopComponent() {
        System.setProperty("Quaqua.tabLayoutPolicy", "wrap");
        try {
            UIManager.setLookAndFeel("ch.randelshofer.quaqua.QuaquaLookAndFeel");
        } catch (Exception e) {
        }
        initComponents();
        setName(NbBundle.getMessage(ZoneExplorerTopComponent.class, "CTL_ZoneExplorerTopComponent"));
        setToolTipText(NbBundle.getMessage(ZoneExplorerTopComponent.class, "HINT_ZoneExplorerTopComponent"));
    }

    private void initComponents() {
        jScrollPane1 = new javax.swing.JScrollPane();
        jListZone = new javax.swing.JList();
        jListZone.putClientProperty("Quaqua.List.style", "striped");
        jButton1 = new javax.swing.JButton();
        jButton1.putClientProperty("Quaqua.Button.style", "square");
        jButton2 = new javax.swing.JButton();
        jButton2.putClientProperty("Quaqua.Button.style", "square");
        jListZone.addListSelectionListener(new javax.swing.event.ListSelectionListener() {

            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                jListZoneValueChanged(evt);
            }
        });
        jScrollPane1.setViewportView(jListZone);
        org.openide.awt.Mnemonics.setLocalizedText(jButton1, "-");
        org.openide.awt.Mnemonics.setLocalizedText(jButton2, "+");
        jButton2.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup().add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING).add(layout.createSequentialGroup().addContainerGap().add(jButton2).addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED).add(jButton1)).add(jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 184, Short.MAX_VALUE)).add(0, 0, 0)));
        layout.linkSize(new java.awt.Component[] { jButton1, jButton2 }, org.jdesktop.layout.GroupLayout.HORIZONTAL);
        layout.setVerticalGroup(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup().add(jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 122, Short.MAX_VALUE).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE).add(jButton1).add(jButton2))));
        layout.linkSize(new java.awt.Component[] { jButton1, jButton2 }, org.jdesktop.layout.GroupLayout.VERTICAL);
    }

    private void jListZoneValueChanged(javax.swing.event.ListSelectionEvent evt) {
        System.out.println("123");
        SidoraTools.showElement();
        Storage.setZone((Zone) jListZone.getSelectedValue());
        Storage.getSectorExplorerAction().fillSectorItems();
    }

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {
        addZone();
        fillZoneItems();
    }

    private javax.swing.JButton jButton1;

    private javax.swing.JButton jButton2;

    private javax.swing.JList jListZone;

    private javax.swing.JScrollPane jScrollPane1;

    /**
     * Gets default instance. Do not use directly: reserved for *.settings files only,
     * i.e. deserialization routines; otherwise you could get a non-deserialized instance.
     * To obtain the singleton instance, use {@link findInstance}.
     */
    public static synchronized ZoneExplorerTopComponent getDefault() {
        if (instance == null) {
            instance = new ZoneExplorerTopComponent();
        }
        return instance;
    }

    /**
     * Obtain the ZoneExplorerTopComponent instance. Never call {@link #getDefault} directly!
     */
    public static synchronized ZoneExplorerTopComponent findInstance() {
        TopComponent win = WindowManager.getDefault().findTopComponent(PREFERRED_ID);
        if (win == null) {
            ErrorManager.getDefault().log(ErrorManager.WARNING, "Cannot find ZoneExplorer component. It will not be located properly in the window system.");
            return getDefault();
        }
        if (win instanceof ZoneExplorerTopComponent) {
            return (ZoneExplorerTopComponent) win;
        }
        ErrorManager.getDefault().log(ErrorManager.WARNING, "There seem to be multiple components with the '" + PREFERRED_ID + "' ID. That is a potential source of errors and unexpected behavior.");
        return getDefault();
    }

    public int getPersistenceType() {
        return TopComponent.PERSISTENCE_ALWAYS;
    }

    public void componentOpened() {
        this.setDisplayName("Zone");
    }

    public void componentClosed() {
    }

    /** replaces this in object stream */
    public Object writeReplace() {
        return new ResolvableHelper();
    }

    protected String preferredID() {
        return PREFERRED_ID;
    }

    static final class ResolvableHelper implements Serializable {

        private static final long serialVersionUID = 1L;

        public Object readResolve() {
            return ZoneExplorerTopComponent.getDefault();
        }
    }

    private static void addZone() {
        Site site = Storage.getSite();
        if (site != null) {
            ParameterCollector param = new ParameterCollector();
            Zone.Inventory zi = new Zone.Inventory();
            param.put("site", site);
            Zone zone = null;
            try {
                zone = zi.getNewObject(param);
                zone.setDescription("New Zone");
                zi.setObject(zone);
            } catch (Exception e1) {
                LoggerProvider.getInstance().log(Level.WARNING, "Exception", e1);
            }
        } else {
            LoggerProvider.getInstance().fine("No hay un Site seleccionado.");
        }
    }

    public void fillZoneItems() {
        Site site = Storage.getSite();
        if (site != null) {
            DefaultListModel zoneModel = new DefaultListModel();
            try {
                List list = Zone.Inventory.getList(site);
                TreeSet<Zone> zones = new TreeSet<Zone>();
                Iterator iter2 = list.iterator();
                while (iter2.hasNext()) {
                    Zone zone = (Zone) iter2.next();
                    zones.add(zone);
                }
                Iterator iter = zones.iterator();
                while (iter.hasNext()) {
                    zoneModel.addElement(iter.next());
                }
            } catch (Exception e1) {
                LoggerProvider.getInstance().log(Level.WARNING, "Exception", e1);
            }
            jListZone.setModel(zoneModel);
        } else {
            jListZone.setModel(new DefaultListModel());
        }
    }
}
