package gui.wizard;

import gui.HarvestPanel;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;
import javax.swing.border.BevelBorder;
import javax.swing.border.LineBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import model.HarvestType;
import service.HarvestService;

public class Wizard0 extends JPanel {

    private static final long serialVersionUID = 1L;

    private JLabel jLabel = null;

    private JPanel jPanel1 = null;

    private JPanel jPanelIzquierda = null;

    private JPanel jPanel4 = null;

    private JLabel jLabel11 = null;

    private JList jList = null;

    private JButton jButton = null;

    private JPanel jPanelCenterDetalles = null;

    private JScrollPane jScrollPane = null;

    private JPanel jPanel2 = null;

    private JPanel jPanel3 = null;

    private JPanel jPanel7 = null;

    private JButton jButtonEdit = null;

    private HarvestPanel padre;

    private JLabel jLabelDetalles = null;

    private HarvestService harvestService;

    /**
	 * This is the default constructor
	 */
    public Wizard0(HarvestPanel padre, HarvestService harvestService) {
        super();
        this.padre = padre;
        this.harvestService = harvestService;
        initialize();
    }

    /**
	 * This method initializes this
	 * 
	 * @return void
	 */
    private void initialize() {
        jLabel = new JLabel();
        jLabel.setText("<html>Seleccione un tipo de cultivo para editarlo o seleccione Crear Nuevo Tipo");
        jLabel.setLabelFor(jLabel);
        this.setLayout(new BorderLayout());
        this.setSize(539, 461);
        this.add(jLabel, BorderLayout.NORTH);
        this.add(getJPanel1(), BorderLayout.CENTER);
    }

    /**
	 * This method initializes jPanel1
	 * 
	 * @return javax.swing.JPanel
	 */
    private JPanel getJPanel1() {
        if (jPanel1 == null) {
            jPanel1 = new JPanel();
            jPanel1.setLayout(new BorderLayout());
            jPanel1.add(getJPanelIzquierda(), BorderLayout.WEST);
            jPanel1.add(getJPanel32(), BorderLayout.CENTER);
            jPanel1.add(getJPanel3(), BorderLayout.NORTH);
        }
        return jPanel1;
    }

    /**
	 * This method initializes jPanelIzquierda
	 * 
	 * @return javax.swing.JPanel
	 */
    private JPanel getJPanelIzquierda() {
        if (jPanelIzquierda == null) {
            jPanelIzquierda = new JPanel();
            jPanelIzquierda.setLayout(new BorderLayout());
            jPanelIzquierda.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
            jPanelIzquierda.add(getJPanel4(), BorderLayout.NORTH);
        }
        return jPanelIzquierda;
    }

    /**
	 * This method initializes jPanel4
	 * 
	 * @return javax.swing.JPanel
	 */
    private JPanel getJPanel4() {
        if (jPanel4 == null) {
            GridBagConstraints gridBagConstraints11 = new GridBagConstraints();
            gridBagConstraints11.fill = GridBagConstraints.BOTH;
            gridBagConstraints11.gridy = 1;
            gridBagConstraints11.ipadx = 107;
            gridBagConstraints11.ipady = 267;
            gridBagConstraints11.weightx = 1.0;
            gridBagConstraints11.weighty = 0.5;
            gridBagConstraints11.gridheight = 1;
            gridBagConstraints11.gridx = 0;
            GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
            gridBagConstraints3.insets = new Insets(14, 0, 0, 0);
            gridBagConstraints3.gridy = 0;
            gridBagConstraints3.gridx = 0;
            jLabel11 = new JLabel();
            jLabel11.setText("Tipo de Cultivo");
            jPanel4 = new JPanel();
            jPanel4.setLayout(new GridBagLayout());
            jPanel4.add(jLabel11, gridBagConstraints3);
            jPanel4.add(getJList(), gridBagConstraints11);
        }
        return jPanel4;
    }

    /**
	 * This method initializes jList
	 * 
	 * @return javax.swing.JList
	 */
    private JList getJList() {
        if (jList == null) {
            jList = new JList(harvestService.getHarvestTypesNames());
            jList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            jList.addListSelectionListener(new MyListSelectionListener2(this));
        }
        return jList;
    }

    /**
	 * This method initializes jButton
	 * 
	 * @return javax.swing.JButton
	 */
    private JButton getJButton() {
        if (jButton == null) {
            jButton = new JButton();
            jButton.setText("Crear Nuevo Tipo");
            jButton.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    HarvestPanel p = (HarvestPanel) padre;
                    p.crearTipo();
                }
            });
        }
        return jButton;
    }

    /**
	 * This method initializes jPanel3
	 * 
	 * @return javax.swing.JPanel
	 */
    private JPanel getJPanel32() {
        if (jPanelCenterDetalles == null) {
            jPanelCenterDetalles = new JPanel();
            jPanelCenterDetalles.setLayout(new BorderLayout());
            jPanelCenterDetalles.add(getJScrollPane(), BorderLayout.CENTER);
            jPanelCenterDetalles.add(getJPanel7(), BorderLayout.SOUTH);
        }
        return jPanelCenterDetalles;
    }

    /**
	 * This method initializes jScrollPane
	 * 
	 * @return javax.swing.JScrollPane
	 */
    private JScrollPane getJScrollPane() {
        if (jScrollPane == null) {
            jScrollPane = new JScrollPane();
            jScrollPane.setViewportView(getJPanel2());
        }
        return jScrollPane;
    }

    /**
	 * This method initializes jPanel2
	 * 
	 * @return javax.swing.JPanel
	 */
    private JPanel getJPanel2() {
        if (jPanel2 == null) {
            jLabelDetalles = new JLabel();
            jLabelDetalles.setText("");
            LineBorder b = new LineBorder(UIManager.getDefaults().getColor("Panel.background"), 10);
            jPanel2 = new JPanel();
            jPanel2.setBorder(b);
            jPanel2.setLayout(new GridLayout(15, 1));
            jPanel2.add(jLabelDetalles, null);
        }
        return jPanel2;
    }

    /**
	 * This method initializes jPanel3
	 * 
	 * @return javax.swing.JPanel
	 */
    private JPanel getJPanel3() {
        if (jPanel3 == null) {
            GridLayout gridLayout = new GridLayout();
            gridLayout.setRows(2);
            gridLayout.setColumns(2);
            jPanel3 = new JPanel();
            jPanel3.setLayout(gridLayout);
        }
        return jPanel3;
    }

    /**
	 * This method initializes jPanel7
	 * 
	 * @return javax.swing.JPanel
	 */
    private JPanel getJPanel7() {
        if (jPanel7 == null) {
            GridBagConstraints gridBagConstraints = new GridBagConstraints();
            gridBagConstraints.gridx = 0;
            gridBagConstraints.gridy = 0;
            GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
            gridBagConstraints2.anchor = GridBagConstraints.SOUTH;
            gridBagConstraints2.gridy = -1;
            gridBagConstraints2.weightx = 0.0;
            gridBagConstraints2.weighty = 0.5;
            gridBagConstraints2.gridx = 1;
            jPanel7 = new JPanel();
            jPanel7.setLayout(new GridBagLayout());
            jPanel7.setSize(new Dimension(188, 29));
            jPanel7.add(getJButton(), gridBagConstraints2);
            jPanel7.add(getJButtonEdit(), gridBagConstraints);
        }
        return jPanel7;
    }

    /**
	 * This method initializes jButtonEdit
	 * 
	 * @return javax.swing.JButton
	 */
    private JButton getJButtonEdit() {
        if (jButtonEdit == null) {
            jButtonEdit = new JButton();
            jButtonEdit.setText("Editar tipo");
            jButtonEdit.setEnabled(false);
            jButtonEdit.addActionListener(new java.awt.event.ActionListener() {

                public void actionPerformed(java.awt.event.ActionEvent e) {
                    editarTipo();
                }
            });
        }
        return jButtonEdit;
    }

    private void editarTipo() {
        int selected = jList.getSelectedIndex();
        HarvestType h = (harvestService.getHarvestTypes().get(selected));
        padre.editarTipo(h);
    }

    private void showItemDetails() {
        int selected = jList.getSelectedIndex();
        HarvestType h = (harvestService.getHarvestTypes().get(selected));
        jLabelDetalles.setText("<html>");
        jLabelDetalles.setText(jLabelDetalles.getText() + "<b><u>NOMBRE: </u></b><br>");
        jLabelDetalles.setText(jLabelDetalles.getText() + h.getNombre() + " <br><br>");
        jLabelDetalles.setText(jLabelDetalles.getText() + " <b><u>DESCRIPCION: </u></b><br>");
        jLabelDetalles.setText(jLabelDetalles.getText() + h.getDescripcion() + "<br> <br>");
        jLabelDetalles.setText(jLabelDetalles.getText() + " <b><u>INSUMOS: </u></b><br>");
        jLabelDetalles.setText(jLabelDetalles.getText() + h.getInsumos().size() + " insumos<br> <br>");
        jLabelDetalles.setText(jLabelDetalles.getText() + " <b><u>IMPUESTOS: </u></b><br>");
        jLabelDetalles.setText(jLabelDetalles.getText() + h.getImpuestos().size() + " impuestos<br> <br>");
        jLabelDetalles.setText(jLabelDetalles.getText() + "</html>");
    }

    public void itemSelected(int index) {
        System.out.println("Mostrar los detalles de " + index);
        showItemDetails();
        jButtonEdit.setEnabled(true);
    }

    public void updateList() {
        jList.setListData(harvestService.getHarvestTypesNames());
    }
}

class MyListSelectionListener2 implements ListSelectionListener {

    private Wizard0 wizard;

    public MyListSelectionListener2(Wizard0 wizard) {
        super();
        this.wizard = wizard;
    }

    public void valueChanged(ListSelectionEvent evt) {
        if (!evt.getValueIsAdjusting()) {
            JList list = (JList) evt.getSource();
            Object[] selected = list.getSelectedValues();
            for (int i = 0; i < selected.length; i++) {
                wizard.itemSelected(list.getSelectedIndex());
            }
        }
    }
}
