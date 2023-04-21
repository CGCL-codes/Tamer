package it.gestioneimmobili;

import it.gestioneimmobili.hibernate.bean.AnagVisionaImmobile;
import it.gestioneimmobili.hibernate.bean.Anagrafica;
import it.gestioneimmobili.hibernate.bean.Immobile;
import it.gestioneimmobili.process.ProcessAnagVisiona;
import it.gestioneimmobili.process.ProcessAnagrafica;
import it.gestioneimmobili.process.ProcessImmobile;
import it.gestioneimmobili.util.Util;
import java.awt.Frame;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseEvent;
import java.util.Set;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author  __USER__
 */
public class VisualizzaImmobiliVisionatiUI extends javax.swing.JDialog {

    /**
	 * 
	 */
    private static final long serialVersionUID = 5977830310198142492L;

    private Set<AnagVisionaImmobile> listaImmobili;

    private Integer idAnagrafica;

    /** Creates new form VisualizzaImmobiliAssociatiUI */
    public VisualizzaImmobiliVisionatiUI(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        this.setLocationRelativeTo(parent);
        this.listaImmobili = null;
    }

    public VisualizzaImmobiliVisionatiUI(java.awt.Frame parent, boolean modal, Integer idAnagrafica) {
        this(parent, modal);
        this.idAnagrafica = idAnagrafica;
        ProcessAnagrafica proc = new ProcessAnagrafica();
        this.listaImmobili = proc.searchImmobiliVisionati(idAnagrafica);
        if (!Util.isEmptyCollection(this.listaImmobili)) {
            DefaultTableModel m = (DefaultTableModel) this.jTable1.getModel();
            for (AnagVisionaImmobile v : this.listaImmobili) {
                Object[] o = new Object[7];
                o[0] = v.getId();
                o[1] = v.getImmobile().getId();
                o[2] = Util.toDataFormattata(v.getDataVisione());
                o[3] = v.getImmobile().getZona();
                o[4] = v.getImmobile().getIndirizzo();
                o[5] = v.getImmobile().getIncaricoEsclusivo() ? "SI" : "NO";
                o[6] = (v.getImmobile().getFitto() == null) ? "Vendita" : "Fitto";
                m.addRow(o);
            }
        }
    }

    public void refresh() {
        ProcessAnagrafica proc = new ProcessAnagrafica();
        this.listaImmobili = proc.searchImmobiliVisionati(this.idAnagrafica);
        if (!Util.isEmptyCollection(this.listaImmobili)) {
            DefaultTableModel m = (DefaultTableModel) this.jTable1.getModel();
            m.getDataVector().removeAllElements();
            for (AnagVisionaImmobile v : this.listaImmobili) {
                Object[] o = new Object[6];
                o[0] = v.getId();
                o[1] = Util.toDataFormattata(v.getDataVisione());
                o[2] = v.getImmobile().getZona();
                o[3] = v.getImmobile().getIndirizzo();
                o[4] = v.getImmobile().getIncaricoEsclusivo();
                o[5] = (v.getImmobile().getFitto() == null) ? "Vendita" : "Fitto";
                m.addRow(o);
            }
        }
    }

    private void initComponents() {
        jButtonInsert = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jButtonDelete = new javax.swing.JButton();
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Immobili Visionati");
        setModal(true);
        setName("ImmobiliAssociati");
        setResizable(false);
        jButtonInsert.setText("Inserisci");
        jButtonInsert.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonInsertActionPerformed(evt);
            }
        });
        jTable1.setModel(new javax.swing.table.DefaultTableModel(new Object[][] {}, new String[] { "Id", "Id Immobile", "Data Visione", "Zona", "Indirizzo", "Incarico", "Tipologia" }) {

            Class[] types = new Class[] { java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class };

            boolean[] canEdit = new boolean[] { false, false, false, false, false, false, false };

            public Class getColumnClass(int columnIndex) {
                return types[columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit[columnIndex];
            }
        });
        jTable1.addMouseListener(new java.awt.event.MouseAdapter() {

            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable1MouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(jTable1);
        jButtonDelete.setText("Elimina");
        jButtonDelete.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonDeleteActionPerformed(evt);
            }
        });
        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(layout.createSequentialGroup().add(jButtonInsert).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(jButtonDelete).addContainerGap(278, Short.MAX_VALUE)).add(org.jdesktop.layout.GroupLayout.TRAILING, jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 420, Short.MAX_VALUE));
        layout.setVerticalGroup(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup().add(jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 293, Short.MAX_VALUE).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE).add(jButtonInsert).add(jButtonDelete))));
        pack();
    }

    private void jButtonDeleteActionPerformed(java.awt.event.ActionEvent evt) {
        int index = jTable1.getSelectedRow();
        if (index >= 0) {
            Integer idAnagVis = (Integer) jTable1.getModel().getValueAt(index, 0);
            ProcessAnagVisiona proc = new ProcessAnagVisiona();
            AnagVisionaImmobile an = proc.selectPk(idAnagVis);
            try {
                proc.delete(an);
                this.refresh();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void jButtonInsertActionPerformed(java.awt.event.ActionEvent evt) {
        ProcessAnagrafica proc = new ProcessAnagrafica();
        Anagrafica aaa = proc.selectPk(idAnagrafica);
        CercaImmobileFittoForVisoneUI fov = new CercaImmobileFittoForVisoneUI((Frame) this.getParent(), true, aaa);
        fov.setVisible(true);
        fov.addComponentListener(new ComponentListener() {

            @Override
            public void componentHidden(ComponentEvent e) {
                refresh();
            }

            @Override
            public void componentResized(ComponentEvent e) {
            }

            @Override
            public void componentShown(ComponentEvent e) {
            }

            @Override
            public void componentMoved(ComponentEvent e) {
            }
        });
    }

    private void jTable1MouseClicked(java.awt.event.MouseEvent evt) {
        if (evt.getButton() == MouseEvent.BUTTON1 && evt.getClickCount() == 2) {
            int index = jTable1.getSelectedRow();
            Integer idImmobile = (Integer) jTable1.getModel().getValueAt(index, 1);
            ProcessImmobile proc = new ProcessImmobile();
            Immobile imm = proc.selectPk(idImmobile);
            if (imm.getVendita() != null) {
                ImmobileVenditaUpdateUI dett2 = new ImmobileVenditaUpdateUI((Frame) this.getParent(), true, imm);
                dett2.setVisible(true);
            } else {
                ImmobileFittoUpdateUI dett2 = new ImmobileFittoUpdateUI((Frame) this.getParent(), true, imm);
                dett2.setVisible(true);
            }
        }
    }

    private javax.swing.JButton jButtonDelete;

    private javax.swing.JButton jButtonInsert;

    private javax.swing.JScrollPane jScrollPane1;

    private javax.swing.JTable jTable1;

    public Set<AnagVisionaImmobile> getListaImmobili() {
        return listaImmobili;
    }

    public void setListaImmobili(Set<AnagVisionaImmobile> listaImmobili) {
        this.listaImmobili = listaImmobili;
    }

    public Integer getIdAnagrafica() {
        return idAnagrafica;
    }

    public void setIdAnagrafica(Integer idAnagrafica) {
        this.idAnagrafica = idAnagrafica;
    }
}
