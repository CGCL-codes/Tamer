package org.sidora.strata.explorer;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.Serializable;
import java.util.Iterator;
import java.util.List;
import java.util.TreeSet;
import java.util.logging.Level;
import javax.swing.DefaultListModel;
import javax.swing.UIManager;
import org.openide.ErrorManager;
import org.openide.util.NbBundle;
import org.openide.util.RequestProcessor;
import org.openide.util.Utilities;
import org.openide.windows.TopComponent;
import org.openide.windows.WindowManager;
import org.sidora.core.Sector;
import org.sidora.core.Site;
import org.sidora.core.Zone;
import org.sidora.core.util.LoggerProvider;
import org.sidora.core.util.ParameterCollector;
import org.sidora.strata.SidoraTools;
import org.sidora.strata.Storage;

/**
 * Sector explorer form
 * @author Enric Tartera, Juan Manuel Gimeno, Roger Masgoret
 * @version 1.0
 */
final class SectorExplorerTopComponent extends TopComponent {

    private static SectorExplorerTopComponent instance;

    private static final String PREFERRED_ID = "SectorExplorerTopComponent";

    private SectorExplorerTopComponent() {
        System.setProperty("Quaqua.tabLayoutPolicy", "wrap");
        try {
            UIManager.setLookAndFeel("ch.randelshofer.quaqua.QuaquaLookAndFeel");
        } catch (Exception e) {
        }
        initComponents();
        setName(NbBundle.getMessage(SectorExplorerTopComponent.class, "CTL_SectorExplorerTopComponent"));
        setToolTipText(NbBundle.getMessage(SectorExplorerTopComponent.class, "HINT_SectorExplorerTopComponent"));
    }

    private void initComponents() {
        jButtonDelSector = new javax.swing.JButton();
        jButtonDelSector.putClientProperty("Quaqua.Button.style", "square");
        jButtonAddSector = new javax.swing.JButton();
        jButtonAddSector.putClientProperty("Quaqua.Button.style", "square");
        jScrollPane1 = new javax.swing.JScrollPane();
        jListSector = new javax.swing.JList();
        jListSector.putClientProperty("Quaqua.List.style", "striped");
        org.openide.awt.Mnemonics.setLocalizedText(jButtonDelSector, "-");
        jButtonDelSector.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonDelSectorActionPerformed(evt);
            }
        });
        org.openide.awt.Mnemonics.setLocalizedText(jButtonAddSector, "+");
        jButtonAddSector.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAddSectorActionPerformed(evt);
            }
        });
        jListSector.addListSelectionListener(new javax.swing.event.ListSelectionListener() {

            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                jListSectorValueChanged(evt);
            }
        });
        jScrollPane1.setViewportView(jListSector);
        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(jScrollPane1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE).add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup().addContainerGap(40, Short.MAX_VALUE).add(jButtonAddSector).addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED).add(jButtonDelSector)));
        layout.linkSize(new java.awt.Component[] { jButtonAddSector, jButtonDelSector }, org.jdesktop.layout.GroupLayout.HORIZONTAL);
        layout.setVerticalGroup(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup().add(jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 73, Short.MAX_VALUE).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE).add(jButtonDelSector).add(jButtonAddSector))));
        layout.linkSize(new java.awt.Component[] { jButtonAddSector, jButtonDelSector }, org.jdesktop.layout.GroupLayout.VERTICAL);
    }

    private void jListSectorValueChanged(javax.swing.event.ListSelectionEvent evt) {
        SidoraTools.showElement();
        Storage.setSector((Sector) jListSector.getSelectedValue());
        Storage.setFeature(null);
        Storage.getFeatureExplorerAction().fillFeatureItems();
        Storage.getSectorAction().actionPerformed();
    }

    private void jButtonAddSectorActionPerformed(java.awt.event.ActionEvent evt) {
        addSector();
        fillSectorItems();
    }

    private void jButtonDelSectorActionPerformed(java.awt.event.ActionEvent evt) {
    }

    private javax.swing.JButton jButtonAddSector;

    private javax.swing.JButton jButtonDelSector;

    private javax.swing.JList jListSector;

    private javax.swing.JScrollPane jScrollPane1;

    /**
     * Gets default instance. Do not use directly: reserved for *.settings files only,
     * i.e. deserialization routines; otherwise you could get a non-deserialized instance.
     * To obtain the singleton instance, use {@link findInstance}.
     */
    public static synchronized SectorExplorerTopComponent getDefault() {
        if (instance == null) {
            instance = new SectorExplorerTopComponent();
        }
        return instance;
    }

    /**
     * Obtain the SectorExplorerTopComponent instance. Never call {@link #getDefault} directly!
     */
    public static synchronized SectorExplorerTopComponent findInstance() {
        TopComponent win = WindowManager.getDefault().findTopComponent(PREFERRED_ID);
        if (win == null) {
            ErrorManager.getDefault().log(ErrorManager.WARNING, "Cannot find SectorExplorer component. It will not be located properly in the window system.");
            return getDefault();
        }
        if (win instanceof SectorExplorerTopComponent) {
            return (SectorExplorerTopComponent) win;
        }
        ErrorManager.getDefault().log(ErrorManager.WARNING, "There seem to be multiple components with the '" + PREFERRED_ID + "' ID. That is a potential source of errors and unexpected behavior.");
        return getDefault();
    }

    public int getPersistenceType() {
        return TopComponent.PERSISTENCE_ALWAYS;
    }

    public void componentOpened() {
        this.setDisplayName("Sector");
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
            return SectorExplorerTopComponent.getDefault();
        }
    }

    private static void addSector() {
        Zone zone = Storage.getZone();
        if (zone != null) {
            ParameterCollector param = new ParameterCollector();
            Sector.Inventory si = new Sector.Inventory();
            param.put("zone", zone);
            Sector sector = null;
            try {
                sector = si.getNewObject(param);
                sector.setClosed(false);
                si.setObject(sector);
            } catch (Exception e1) {
                LoggerProvider.getInstance().log(Level.WARNING, "Exception", e1);
            }
        } else {
            LoggerProvider.getInstance().fine("No hay un Site seleccionado.");
        }
    }

    public void fillSectorItems() {
        Zone zone = Storage.getZone();
        if (zone != null) {
            DefaultListModel sectorModel = new DefaultListModel();
            try {
                List list = Sector.Inventory.getList(zone);
                TreeSet<Sector> sectors = new TreeSet<Sector>();
                Iterator iter2 = list.iterator();
                while (iter2.hasNext()) {
                    Sector sector = (Sector) iter2.next();
                    sectors.add(sector);
                }
                Iterator iter = sectors.iterator();
                while (iter.hasNext()) {
                    sectorModel.addElement(iter.next());
                }
            } catch (Exception e1) {
                LoggerProvider.getInstance().log(Level.WARNING, "Exception", e1);
            }
            jListSector.setModel(sectorModel);
        } else {
            jListSector.setModel(new DefaultListModel());
        }
    }
}
