package Pantallas.Cliente;

import gestores.GestorCliente;
import gestores.MyDefaultTableModel;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;
import negocio.cliente.Cliente;
import com.swtdesigner.SwingResourceManager;

public class ConsultarCliente extends JDialog {

    private JButton salirButton;

    private JButton aceptarButton;

    private JTable table_1;

    private JScrollPane scrollPane_1;

    private JButton buscarButton;

    private JLabel label_2;

    private JTextField textField;

    private JLabel label_1;

    private JSeparator separator_1;

    private JLabel consultarEmpleadoLabel;

    private MyDefaultTableModel modelo;

    private GestorCliente gestor;

    private Cliente cliente;

    /**
	 * Launch the application
	 * @param args
	 */
    public static void main(String args[]) {
        try {
            ConsultarCliente dialog = new ConsultarCliente(GestorCliente.getInstance());
            dialog.addWindowListener(new WindowAdapter() {

                public void windowClosing(WindowEvent e) {
                    System.exit(0);
                }
            });
            dialog.setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
	 * Create the dialog
	 */
    public ConsultarCliente(GestorCliente gestor) {
        super();
        setModal(true);
        setIconImage(SwingResourceManager.getImage(ConsultarCliente.class, "/Iconos/viewmag-000.png"));
        setTitle("Consultar Cliente");
        getContentPane().setLayout(null);
        setBounds(100, 100, 651, 354);
        this.gestor = gestor;
        consultarEmpleadoLabel = new JLabel();
        consultarEmpleadoLabel.setFont(new Font("Dialog", Font.BOLD | Font.ITALIC, 14));
        consultarEmpleadoLabel.setText("Consultar Cliente");
        consultarEmpleadoLabel.setBounds(10, 25, 151, 18);
        getContentPane().add(consultarEmpleadoLabel);
        separator_1 = new JSeparator();
        separator_1.setBounds(10, 65, 623, 11);
        getContentPane().add(separator_1);
        label_1 = new JLabel();
        label_1.setIcon(SwingResourceManager.getIcon(ConsultarCliente.class, "/Iconos/viewmag-002.png"));
        label_1.setBounds(205, 15, 115, 35);
        getContentPane().add(label_1);
        textField = new JTextField();
        textField.setBounds(20, 100, 204, 20);
        getContentPane().add(textField);
        buscarButton = new JButton();
        buscarButton.setIcon(SwingResourceManager.getIcon(ConsultarCliente.class, "/Iconos/viewmag-001.png"));
        buscarButton.setBounds(243, 95, 57, 33);
        getContentPane().add(buscarButton);
        scrollPane_1 = new JScrollPane();
        scrollPane_1.setBounds(10, 156, 623, 113);
        getContentPane().add(scrollPane_1);
        modelo = new MyDefaultTableModel();
        modelo.setColumnIdentifiers(new String[] { "Raz�n Social", "Domicilio" });
        final List<Cliente> clientes = gestor.getClientes();
        for (Cliente c : clientes) {
            modelo.addRow(new String[] { c.getRazonSocial(), c.getDomicilioCompleto() });
        }
        table_1 = new JTable(modelo);
        table_1.setAutoCreateRowSorter(true);
        table_1.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table_1.getColumn("Domicilio").setPreferredWidth(370);
        scrollPane_1.setViewportView(table_1);
        aceptarButton = new JButton();
        aceptarButton.addActionListener(new ActionListener() {

            public void actionPerformed(final ActionEvent e) {
                if (table_1.getSelectedRow() != -1) {
                    cliente = clientes.get(table_1.getSelectedRow());
                    ConsultarCliente.this.dispose();
                } else {
                    JOptionPane.showMessageDialog(aceptarButton, "Debe seleccionar un cliente", "ERROR", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        aceptarButton.setIcon(SwingResourceManager.getIcon(ConsultarCliente.class, "/Iconos/button_ok-001.png"));
        aceptarButton.setFont(new Font("Dialog", Font.BOLD, 12));
        aceptarButton.setBounds(303, 275, 52, 32);
        getContentPane().add(aceptarButton);
        label_2 = new JLabel();
        label_2.setIcon(SwingResourceManager.getIcon(ConsultarCliente.class, "/Iconos/Client-2-256x256-002.png"));
        label_2.setBounds(167, 0, 71, 55);
        getContentPane().add(label_2);
        salirButton = new JButton();
        salirButton.addActionListener(new ActionListener() {

            public void actionPerformed(final ActionEvent arg0) {
                ConsultarCliente.this.dispose();
            }
        });
        salirButton.setIcon(SwingResourceManager.getIcon(ConsultarCliente.class, "/Iconos/back-001.png"));
        salirButton.setBounds(581, 275, 52, 32);
        getContentPane().add(salirButton);
    }

    public Cliente getClienteSeleccionado() {
        return cliente;
    }
}
