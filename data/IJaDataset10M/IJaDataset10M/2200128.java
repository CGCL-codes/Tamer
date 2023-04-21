package org.virbo.autoplot;

import edu.uiowa.physics.pw.das.datum.Datum;
import edu.uiowa.physics.pw.das.datum.DatumUtil;
import edu.uiowa.physics.pw.das.datum.Units;
import edu.uiowa.physics.pw.das.util.CombinedTreeModel;
import org.das2.util.monitor.ProgressMonitor;
import org.das2.util.monitor.NullProgressMonitor;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.DecimalFormat;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.swing.SwingUtilities;
import javax.swing.tree.TreeModel;
import org.virbo.dataset.QDataSet;
import org.virbo.datasource.DataSource;
import org.virbo.datasource.MetadataModel;
import org.virbo.dsutil.PropertiesTreeModel;
import org.virbo.metatree.NameValueTreeModel;

/**
 *
 * @author  jbf
 */
public class MetaDataPanel extends javax.swing.JPanel {

    ApplicationModel applicationModel;

    CombinedTreeModel tree;

    /** Creates new form MetaDataPanel */
    public MetaDataPanel(ApplicationModel applicationModel) {
        this.applicationModel = applicationModel;
        initComponents();
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                metaDataTree.setModel(null);
            }
        });
        applicationModel.addPropertyChangeListener(this.appModelListener);
        update();
    }

    public void update() {
        tree = new CombinedTreeModel("metadata");
        try {
            DataSource dsrc = applicationModel.dataSource();
            if (dsrc != null) {
                ProgressMonitor mon = new NullProgressMonitor();
                Map<String, Object> meta = dsrc.getMetaData(mon);
                MetadataModel model = dsrc.getMetadataModel();
                String root = "Metadata (" + model.getLabel() + ")";
                final TreeModel dsrcMeta = NameValueTreeModel.create(root, meta);
                if (dsrcMeta != null) {
                    SwingUtilities.invokeLater(new Runnable() {

                        public void run() {
                            tree.mountTree(dsrcMeta);
                            metaDataTree.setModel(tree);
                        }
                    });
                }
            } else {
                SwingUtilities.invokeLater(new Runnable() {

                    public void run() {
                        metaDataTree.setModel(tree);
                    }
                });
            }
        } catch (Exception e) {
            applicationModel.application.getExceptionHandler().handle(e);
        }
    }

    PropertyChangeListener appModelListener = new PropertyChangeListener() {

        public void propertyChange(PropertyChangeEvent evt) {
            if (evt.getPropertyName().equals(ApplicationModel.PROPERTY_FILL)) {
                updateStatistics();
            }
        }
    };

    private String format(double d) {
        if (Math.abs(Math.log(d) / Math.log(10)) < 3) {
            DecimalFormat df1 = new DecimalFormat("0.00");
            return df1.format(d);
        } else {
            DecimalFormat df = new DecimalFormat("0.00E0");
            return df.format(d);
        }
    }

    boolean statisticsDirty;

    private void updateStatistics() {
        statisticsDirty = true;
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                if (statisticsDirty) {
                    updateStatisticsImmediately();
                    updateDataSetPropertiesView();
                }
            }
        });
    }

    private void updateDataSetPropertiesView() {
        if (applicationModel.dataset == null) {
            return;
        }
        final PropertiesTreeModel dsTree = new PropertiesTreeModel("dataset= ", applicationModel.dataset);
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                tree.mountTree(dsTree);
            }
        });
    }

    private synchronized void updateStatisticsImmediately() {
        QDataSet ds = applicationModel.fillDataset;
        if (ds == null) {
            return;
        }
        AutoplotUtil.MomentDescriptor moments = AutoplotUtil.moment(ds);
        final LinkedHashMap map = new LinkedHashMap();
        map.put("# invalid", String.valueOf(moments.invalidCount) + " of " + String.valueOf(moments.validCount + moments.invalidCount));
        String s;
        if (moments.validCount > 0) {
            s = format(moments.moment[0]);
        } else {
            s = "";
        }
        map.put("Mean", s);
        if (moments.validCount > 1) {
            s = format(moments.moment[1]);
        } else {
            s = "";
        }
        map.put("Std Dev", s);
        QDataSet dep0 = (QDataSet) ds.property(QDataSet.DEPEND_0);
        assert (dep0 != null);
        Double cadence = (Double) dep0.property(QDataSet.CADENCE);
        Units xunits = (Units) dep0.property(QDataSet.UNITS);
        if (xunits == null) {
            xunits = Units.dimensionless;
        }
        Datum d = DatumUtil.asOrderOneUnits(xunits.getOffsetUnits().createDatum(cadence));
        Units u = d.getUnits();
        map.put("Cadence", format(d.doubleValue(u)) + " " + u);
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                tree.mountTree(NameValueTreeModel.create("Statistics", map));
            }
        });
        statisticsDirty = false;
    }

    private void initComponents() {
        jScrollPane1 = new javax.swing.JScrollPane();
        metaDataTree = new javax.swing.JTree();
        metaDataTree.setShowsRootHandles(true);
        jScrollPane1.setViewportView(metaDataTree);
        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(layout.createSequentialGroup().addContainerGap().add(jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 380, Short.MAX_VALUE).addContainerGap()));
        layout.setVerticalGroup(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(layout.createSequentialGroup().addContainerGap().add(jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 295, Short.MAX_VALUE).addContainerGap()));
    }

    private javax.swing.JScrollPane jScrollPane1;

    private javax.swing.JTree metaDataTree;
}
