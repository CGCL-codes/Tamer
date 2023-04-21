package org.put.netbeans.dcs_modeler;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;
import java.util.logging.Logger;
import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import org.openide.util.NbBundle;
import org.openide.windows.TopComponent;
import org.openide.windows.WindowManager;
import org.openide.util.Utilities;
import org.openide.windows.CloneableTopComponent;

/**
 * Top component which displays something.
 */
public class ComputingObjectDefinitionEditorTopComponent extends CloneableTopComponent {

    private JComboBox entityTypeCombo = new JComboBox();

    private static ComputingObjectDefinitionEditorTopComponent instance;

    /** path to the icon used by the component and its open action */
    static final String ICON_PATH = "org/put/netbeans/dcs_modeler/palette/images/workflow_simple_case_16x16.png";

    private static final String PREFERRED_ID = "ComputingObjectDefinitionEditorTopComponent";

    public ComputingObjectDefinitionEditorTopComponent() {
        initComponents();
        setName(NbBundle.getMessage(ComputingObjectDefinitionEditorTopComponent.class, "CTL_ComputingObjectDefinitionEditorTopComponent"));
        setToolTipText(NbBundle.getMessage(ComputingObjectDefinitionEditorTopComponent.class, "HINT_ComputingObjectDefinitionEditorTopComponent"));
        setIcon(Utilities.loadImage(ICON_PATH, true));
        entityTypeCombo.addItem("jjj");
        entityTypeCombo.addItem("jjyytty");
        tableIn.getColumnModel().getColumn(1).setCellEditor(new DefaultCellEditor(entityTypeCombo));
        entityTypeCombo.addItem("ppfpfpfpf");
    }

    public Vector<Vector> getPortInVector() {
        return ((javax.swing.table.DefaultTableModel) tableIn.getModel()).getDataVector();
    }

    public Vector<Vector> getPortOutVector() {
        return ((javax.swing.table.DefaultTableModel) tableOut.getModel()).getDataVector();
    }

    public void addEntityType(String entityName) {
        entityTypeCombo.addItem(entityName);
    }

    public void addPortIn(String name, String type) {
        String[] rowArray = new String[2];
        rowArray[0] = name;
        rowArray[1] = type;
        ((javax.swing.table.DefaultTableModel) tableIn.getModel()).addRow(rowArray);
    }

    public void addPortOut(String name, String type) {
        String[] rowArray = new String[2];
        rowArray[0] = name;
        rowArray[1] = type;
        ((javax.swing.table.DefaultTableModel) tableOut.getModel()).addRow(rowArray);
    }

    private void initComponents() {
        jToolBar1 = new javax.swing.JToolBar();
        jScrollPane1 = new javax.swing.JScrollPane();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tableIn = new javax.swing.JTable();
        addIn = new javax.swing.JButton();
        jScrollPane3 = new javax.swing.JScrollPane();
        tableOut = new javax.swing.JTable();
        addOut = new javax.swing.JButton();
        jToolBar1.setRollover(true);
        tableIn.setModel(new javax.swing.table.DefaultTableModel(new Object[][] { { null, null } }, new String[] { "Nazwa", "Typ" }));
        jScrollPane2.setViewportView(tableIn);
        org.openide.awt.Mnemonics.setLocalizedText(addIn, "Dodaj sygnał wejściowy");
        tableOut.setModel(new javax.swing.table.DefaultTableModel(new Object[][] { { null, null } }, new String[] { "Nazwa", "Typ" }) {

            Class[] types = new Class[] { java.lang.String.class, java.lang.String.class };

            boolean[] canEdit = new boolean[] { false, false };

            public Class getColumnClass(int columnIndex) {
                return types[columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit[columnIndex];
            }
        });
        jScrollPane3.setViewportView(tableOut);
        org.openide.awt.Mnemonics.setLocalizedText(addOut, "Dodaj sygnał wyjściowy");
        org.jdesktop.layout.GroupLayout jPanel1Layout = new org.jdesktop.layout.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel1Layout.createSequentialGroup().addContainerGap().add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING).add(addOut).add(org.jdesktop.layout.GroupLayout.LEADING, jScrollPane3, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 615, Short.MAX_VALUE).add(org.jdesktop.layout.GroupLayout.LEADING, jScrollPane2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 615, Short.MAX_VALUE).add(addIn)).addContainerGap()));
        jPanel1Layout.setVerticalGroup(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(jPanel1Layout.createSequentialGroup().add(jScrollPane2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 122, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(addIn).addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED).add(jScrollPane3, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 127, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(addOut).addContainerGap(213, Short.MAX_VALUE)));
        jScrollPane1.setViewportView(jPanel1);
        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(jToolBar1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 654, Short.MAX_VALUE).add(jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 654, Short.MAX_VALUE));
        layout.setVerticalGroup(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(layout.createSequentialGroup().add(jToolBar1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 19, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 333, Short.MAX_VALUE)));
    }

    private javax.swing.JButton addIn;

    private javax.swing.JButton addOut;

    private javax.swing.JPanel jPanel1;

    private javax.swing.JScrollPane jScrollPane1;

    private javax.swing.JScrollPane jScrollPane2;

    private javax.swing.JScrollPane jScrollPane3;

    private javax.swing.JToolBar jToolBar1;

    private javax.swing.JTable tableIn;

    private javax.swing.JTable tableOut;

    /**
     * Gets default instance. Do not use directly: reserved for *.settings files only,
     * i.e. deserialization routines; otherwise you could get a non-deserialized instance.
     * To obtain the singleton instance, use {@link findInstance}.
     */
    public static synchronized ComputingObjectDefinitionEditorTopComponent getDefault() {
        if (instance == null) {
            instance = new ComputingObjectDefinitionEditorTopComponent();
        }
        return instance;
    }

    /**
     * Obtain the ComputingObjectDefinitionEditorTopComponent instance. Never call {@link #getDefault} directly!
     */
    public static synchronized ComputingObjectDefinitionEditorTopComponent findInstance() {
        TopComponent win = WindowManager.getDefault().findTopComponent(PREFERRED_ID);
        if (win == null) {
            Logger.getLogger(ComputingObjectDefinitionEditorTopComponent.class.getName()).warning("Cannot find " + PREFERRED_ID + " component. It will not be located properly in the window system.");
            return getDefault();
        }
        if (win instanceof ComputingObjectDefinitionEditorTopComponent) {
            return (ComputingObjectDefinitionEditorTopComponent) win;
        }
        Logger.getLogger(ComputingObjectDefinitionEditorTopComponent.class.getName()).warning("There seem to be multiple components with the '" + PREFERRED_ID + "' ID. That is a potential source of errors and unexpected behavior.");
        return getDefault();
    }

    @Override
    public int getPersistenceType() {
        return TopComponent.PERSISTENCE_ALWAYS;
    }

    @Override
    public void componentOpened() {
    }

    @Override
    public void componentClosed() {
    }

    /** replaces this in object stream */
    @Override
    public Object writeReplace() {
        return new ResolvableHelper();
    }

    @Override
    protected String preferredID() {
        return PREFERRED_ID;
    }

    static final class ResolvableHelper implements Serializable {

        private static final long serialVersionUID = 1L;

        public Object readResolve() {
            return ComputingObjectDefinitionEditorTopComponent.getDefault();
        }
    }

    private static class HashMapImpl extends HashMap<String, Object> {

        public HashMapImpl() {
        }
    }
}
