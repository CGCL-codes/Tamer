package interfaz;

import java.awt.BorderLayout;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JFrame;
import java.awt.GridLayout;
import javax.swing.JLabel;
import javax.swing.JTextField;
import java.awt.GridBagLayout;
import javax.swing.JButton;
import negocio.Investigador;
import negocio.Organismo;
import negocio.Proyecto;
import org.hibernate.Session;
import org.hibernate.Transaction;
import persistencia.UtilidadHibernate;
import java.awt.GridBagConstraints;
import java.awt.Dimension;
import java.util.Iterator;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class NuevoOrganismo extends JDialog {

    private static final long serialVersionUID = 1L;

    private JPanel jContentPane = null;

    private JPanel organismoPanel = null;

    private JLabel jLabel = null;

    private JTextField jTextField = null;

    private JLabel jLabel1 = null;

    private JTextField jTextField1 = null;

    private JPanel confirmationPanel = null;

    private JButton aceptarButton = null;

    private JButton cancelarButton = null;

    private int idproyecto;

    private JPanel panelcentral = null;

    private JPanel jPanel = null;

    private JScrollPane jScrollPane = null;

    private JTable jTable = null;

    private JButton borrarButton = null;

    private DefaultTableModel modelo = null;

    /**
	 * This is the default constructor
	 */
    public NuevoOrganismo(int idproyecto) {
        super();
        initialize();
        this.idproyecto = idproyecto;
        try {
            Session session = null;
            session = UtilidadHibernate.getSessionFactory().openSession();
            Transaction tx = session.beginTransaction();
            Proyecto proy = (Proyecto) session.get(Proyecto.class, idproyecto);
            Iterator it = proy.getOrganismos().iterator();
            while (it.hasNext()) {
                Organismo aux = (Organismo) it.next();
                String[] fila = { new Integer(aux.getId()).toString(), aux.getNombre(), new Double(aux.getCuantia()).toString() };
                modelo.addRow(fila);
            }
        } catch (Exception e) {
        }
    }

    /**
	 * This method initializes this
	 * 
	 * @return void
	 */
    private void initialize() {
        this.setSize(587, 287);
        this.setContentPane(getJContentPane());
        this.setTitle("Nuevo Organismo");
    }

    /**
	 * This method initializes jContentPane	
	 * 	
	 * @return javax.swing.JPanel	
	 */
    private JPanel getJContentPane() {
        if (jContentPane == null) {
            jContentPane = new JPanel();
            jContentPane.setLayout(new BorderLayout());
            jContentPane.add(getConfirmationPanel(), BorderLayout.SOUTH);
            jContentPane.add(getPanelcentral(), BorderLayout.CENTER);
        }
        return jContentPane;
    }

    /**
	 * This method initializes organismoPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
    private JPanel getOrganismoPanel() {
        if (organismoPanel == null) {
            jLabel1 = new JLabel();
            jLabel1.setText("Cuant�a:");
            jLabel = new JLabel();
            jLabel.setText("Nombre Organismo:");
            GridLayout gridLayout11 = new GridLayout();
            gridLayout11.setRows(2);
            gridLayout11.setColumns(2);
            organismoPanel = new JPanel();
            organismoPanel.setLayout(gridLayout11);
            organismoPanel.add(jLabel, null);
            organismoPanel.add(getJTextField(), null);
            organismoPanel.add(jLabel1, null);
            organismoPanel.add(getJTextField1(), null);
        }
        return organismoPanel;
    }

    /**
	 * This method initializes jTextField	
	 * 	
	 * @return javax.swing.JTextField	
	 */
    private JTextField getJTextField() {
        if (jTextField == null) {
            jTextField = new JTextField();
        }
        return jTextField;
    }

    /**
	 * This method initializes jTextField1	
	 * 	
	 * @return javax.swing.JTextField	
	 */
    private JTextField getJTextField1() {
        if (jTextField1 == null) {
            jTextField1 = new JTextField();
        }
        return jTextField1;
    }

    /**
	 * This method initializes confirmationPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
    private JPanel getConfirmationPanel() {
        if (confirmationPanel == null) {
            confirmationPanel = new JPanel();
            confirmationPanel.setLayout(new GridBagLayout());
            confirmationPanel.add(getAceptarButton(), new GridBagConstraints());
            confirmationPanel.add(getCancelarButton(), new GridBagConstraints());
            confirmationPanel.add(getBorrarButton(), new GridBagConstraints());
        }
        return confirmationPanel;
    }

    /**
	 * This method initializes aceptarButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
    private JButton getAceptarButton() {
        if (aceptarButton == null) {
            aceptarButton = new JButton();
            aceptarButton.setText("Aceptar");
            aceptarButton.addActionListener(new java.awt.event.ActionListener() {

                public void actionPerformed(java.awt.event.ActionEvent e) {
                    try {
                        String nombre = jTextField.getText().trim();
                        double cuantia = Double.parseDouble(jTextField1.getText().trim());
                        if (nombre.length() == 0) throw new Exception("Hace falta indicar nombre");
                        Session session = null;
                        session = UtilidadHibernate.getSessionFactory().openSession();
                        Transaction tx = session.beginTransaction();
                        Organismo org = new Organismo();
                        Proyecto proy = (Proyecto) session.get(Proyecto.class, idproyecto);
                        session.save(org);
                        org.setCuantia(cuantia);
                        org.setNombre(nombre);
                        org.setProyecto(proy);
                        tx.commit();
                        JOptionPane.showMessageDialog(null, "La subvenci�n se ha a�adido correctamente", "Aviso", JOptionPane.INFORMATION_MESSAGE);
                        setVisible(false);
                    } catch (NumberFormatException ex1) {
                        JOptionPane.showMessageDialog(null, "La cuantia tiene que ser un n�mero", "Error", JOptionPane.ERROR_MESSAGE);
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            });
        }
        return aceptarButton;
    }

    /**
	 * This method initializes cancelarButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
    private JButton getCancelarButton() {
        if (cancelarButton == null) {
            cancelarButton = new JButton();
            cancelarButton.setText("Cancelar");
            cancelarButton.addActionListener(new java.awt.event.ActionListener() {

                public void actionPerformed(java.awt.event.ActionEvent e) {
                    setVisible(false);
                }
            });
        }
        return cancelarButton;
    }

    /**
	 * This method initializes panelcentral	
	 * 	
	 * @return javax.swing.JPanel	
	 */
    private JPanel getPanelcentral() {
        if (panelcentral == null) {
            GridLayout gridLayout = new GridLayout();
            gridLayout.setRows(2);
            gridLayout.setColumns(1);
            panelcentral = new JPanel();
            panelcentral.setLayout(gridLayout);
            panelcentral.add(getOrganismoPanel(), null);
            panelcentral.add(getJPanel(), null);
        }
        return panelcentral;
    }

    /**
	 * This method initializes jPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
    private JPanel getJPanel() {
        if (jPanel == null) {
            GridBagConstraints gridBagConstraints = new GridBagConstraints();
            gridBagConstraints.fill = GridBagConstraints.BOTH;
            gridBagConstraints.weighty = 1.0;
            gridBagConstraints.weightx = 1.0;
            jPanel = new JPanel();
            jPanel.setLayout(new GridBagLayout());
            jPanel.add(getJScrollPane(), gridBagConstraints);
        }
        return jPanel;
    }

    /**
	 * This method initializes jScrollPane	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */
    private JScrollPane getJScrollPane() {
        if (jScrollPane == null) {
            jScrollPane = new JScrollPane();
            jScrollPane.setViewportView(getJTable());
        }
        return jScrollPane;
    }

    /**
	 * This method initializes jTable	
	 * 	
	 * @return javax.swing.JTable	
	 */
    private JTable getJTable() {
        modelo = new DefaultTableModel();
        modelo.addColumn("id");
        modelo.addColumn("Nombre");
        modelo.addColumn("Cuantia");
        if (jTable == null) {
            jTable = new JTable(modelo);
        }
        return jTable;
    }

    /**
	 * This method initializes borrarButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
    private JButton getBorrarButton() {
        if (borrarButton == null) {
            borrarButton = new JButton();
            borrarButton.setText("Borrar");
            borrarButton.addActionListener(new java.awt.event.ActionListener() {

                public void actionPerformed(java.awt.event.ActionEvent e) {
                    try {
                        int fila = jTable.getSelectedRow();
                        int idorg = Integer.parseInt((String) modelo.getValueAt(fila, 0));
                        Session session = null;
                        session = UtilidadHibernate.getSessionFactory().openSession();
                        Transaction tx = session.beginTransaction();
                        Organismo org = (Organismo) session.get(Organismo.class, idorg);
                        session.delete(org);
                        tx.commit();
                        JOptionPane.showMessageDialog(null, "La subvencion se ha eliminador correctamente", "Aviso", JOptionPane.INFORMATION_MESSAGE);
                        modelo.removeRow(fila);
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            });
        }
        return borrarButton;
    }
}
