package interfaz;

import java.awt.BorderLayout;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JFrame;
import java.awt.GridBagLayout;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import java.awt.GridBagConstraints;
import javax.swing.JButton;
import negocio.Grupo;
import negocio.Profesor;
import org.hibernate.Session;
import org.hibernate.Transaction;
import persistencia.UtilidadHibernate;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.List;

public class ListaLider extends JDialog {

    private static final long serialVersionUID = 1L;

    private JPanel jContentPane = null;

    private JPanel tablaPanel = null;

    private JScrollPane tablaScrollPane = null;

    private JTable tablaTable = null;

    private DefaultTableModel modelo = null;

    private JPanel SeleccionarPanel = null;

    private JButton seleccionarButton = null;

    private JButton cancelarButton = null;

    private int idgrupo;

    private String lider;

    private int idprofesor;

    public String getLider() {
        return lider;
    }

    /**
	 * This is the default constructor
	 */
    public ListaLider() {
        super();
        initialize();
        this.idgrupo = -1;
    }

    public ListaLider(int idgrupo) {
        super();
        this.idgrupo = idgrupo;
        initialize();
        this.idgrupo = idgrupo;
    }

    /**
	 * This method initializes this
	 * 
	 * @return void
	 */
    private void initialize() {
        this.setSize(671, 494);
        this.setContentPane(getJContentPane());
        this.addWindowListener(new java.awt.event.WindowAdapter() {

            public void windowOpened(java.awt.event.WindowEvent e) {
                tablaTable = getTablaTable();
            }
        });
        this.setTitle("JFrame");
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
            jContentPane.add(getTablaPanel(), BorderLayout.NORTH);
            jContentPane.add(getSeleccionarPanel(), BorderLayout.SOUTH);
        }
        return jContentPane;
    }

    /**
	 * This method initializes tablaPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
    private JPanel getTablaPanel() {
        if (tablaPanel == null) {
            GridBagConstraints gridBagConstraints = new GridBagConstraints();
            gridBagConstraints.fill = GridBagConstraints.BOTH;
            gridBagConstraints.weighty = 1.0;
            gridBagConstraints.weightx = 1.0;
            tablaPanel = new JPanel();
            tablaPanel.setLayout(new GridBagLayout());
            tablaPanel.add(getTablaScrollPane(), gridBagConstraints);
        }
        return tablaPanel;
    }

    /**
	 * This method initializes tablaScrollPane	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */
    private JScrollPane getTablaScrollPane() {
        if (tablaScrollPane == null) {
            tablaScrollPane = new JScrollPane();
            tablaScrollPane.setViewportView(getTablaTable());
        }
        return tablaScrollPane;
    }

    /**
	 * This method initializes tablaTable	
	 * 	
	 * @return javax.swing.JTable	
	 */
    private JTable getTablaTable() {
        modelo = new DefaultTableModel();
        modelo.addColumn("Id");
        modelo.addColumn("Nombre");
        if (tablaTable == null) {
            tablaTable = new JTable(modelo);
        }
        Session session = null;
        session = UtilidadHibernate.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        String query = "from Profesor";
        List result = session.createQuery(query).list();
        if (result.size() > 0) {
            for (int i = 0; i < result.size(); i++) {
                Profesor p = (Profesor) result.get(i);
                if (p.getGrupo() == null) {
                    String[] fila = { new Integer(p.getId()).toString(), p.getNombre() };
                    modelo.addRow(fila);
                } else {
                    if (p.getGrupo().getId() == idgrupo) {
                        String[] fila = { new Integer(p.getId()).toString(), p.getNombre() };
                        modelo.addRow(fila);
                    }
                }
            }
        }
        return tablaTable;
    }

    /**
	 * This method initializes SeleccionarPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
    private JPanel getSeleccionarPanel() {
        if (SeleccionarPanel == null) {
            GridLayout gridLayout = new GridLayout();
            gridLayout.setRows(1);
            gridLayout.setColumns(2);
            SeleccionarPanel = new JPanel();
            SeleccionarPanel.setLayout(gridLayout);
            SeleccionarPanel.add(getSeleccionarButton(), null);
            SeleccionarPanel.add(getCancelarButton(), null);
        }
        return SeleccionarPanel;
    }

    /**
	 * This method initializes seleccionarButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
    private JButton getSeleccionarButton() {
        if (seleccionarButton == null) {
            seleccionarButton = new JButton();
            seleccionarButton.setText("Seleccionar");
            seleccionarButton.addActionListener(new java.awt.event.ActionListener() {

                public void actionPerformed(java.awt.event.ActionEvent e) {
                    if (tablaTable.getSelectedRowCount() != 1) JOptionPane.showMessageDialog(jContentPane, "Debes tener seleccionada una fila", "Error", JOptionPane.ERROR_MESSAGE); else {
                        int fila = tablaTable.getSelectedRow();
                        lider = (String) modelo.getValueAt(fila, 1);
                        idprofesor = Integer.parseInt((String) modelo.getValueAt(fila, 0));
                        setVisible(false);
                    }
                }
            });
        }
        return seleccionarButton;
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

    public int getIdprofesor() {
        return idprofesor;
    }
}
