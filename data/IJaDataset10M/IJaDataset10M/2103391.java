package es.devel.opentrats.booking.gui;

import es.devel.opentrats.booking.beans.Customer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.swing.table.TableColumn;
import javax.swing.SpinnerNumberModel;
import java.awt.event.*;
import java.awt.Cursor;
import es.devel.opentrats.booking.util.OpenTratsBookingUtil;
import es.devel.opentrats.booking.componentes.NonEditableTableModel;
import es.devel.opentrats.booking.service.dao.ICustomerDao;
import es.devel.opentrats.booking.service.dao.impl.CustomerDaoImpl;
import org.apache.log4j.Logger;

/**
 *
 * @author  Fran Serrano
 */
public class frmBusquedaClientes extends javax.swing.JFrame {

    private static final long serialVersionUID = -2952500637016173987L;

    private NonEditableTableModel tm_clientes;

    private SpinnerNumberModel modelo_sp;

    private frmCita padre;

    private ICustomerDao customerDao;

    /**
     * Creates new form frm_busqueda 
     * @param papa 
     */
    public frmBusquedaClientes(frmCita papa) {
        init();
        this.padre = papa;
        initComponents();
        Object[] Cabecera = new Object[] { "Cod.", "Apellidos", "Nombre", "Tfno" };
        tm_clientes = new NonEditableTableModel(Cabecera, 0);
        dg_clientes.setModel(tm_clientes);
        TableColumn columnaCodigo = dg_clientes.getColumnModel().getColumn(0);
        columnaCodigo.setPreferredWidth(40);
        TableColumn columnaApellidos = dg_clientes.getColumnModel().getColumn(1);
        columnaApellidos.setPreferredWidth(200);
        TableColumn columnaNombre = dg_clientes.getColumnModel().getColumn(2);
        columnaNombre.setPreferredWidth(110);
        TableColumn columnaTfno = dg_clientes.getColumnModel().getColumn(3);
        columnaTfno.setPreferredWidth(70);
        dg_clientes.setCursor(java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.HAND_CURSOR));
        this.setLocation(0, 0);
        this.setSize(330, 270);
        this.setVisible(true);
        txt_filtro.grabFocus();
    }

    private void init() {
        customerDao = new CustomerDaoImpl();
    }

    private void MostrarClientes(List clientes) {
        try {
            this.setCursor(java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.WAIT_CURSOR));
            Iterator it = clientes.iterator();
            Object[] Fila;
            tm_clientes.setRowCount(0);
            Customer c = null;
            while (it.hasNext()) {
                c = (Customer) it.next();
                Fila = new Object[] { c.getCodcliente(), c.getApellidos(), c.getNombre(), c.getTfno() };
                tm_clientes.addRow(Fila);
                dg_clientes.setModel(tm_clientes);
                dg_clientes.grabFocus();
            }
            dg_clientes.changeSelection(0, 0, false, false);
            this.setCursor(java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.DEFAULT_CURSOR));
        } catch (Exception e) {
            OpenTratsBookingUtil.Mensaje("Error mostrando clientes en el grid: " + e.getMessage(), "Error mostrando clientes...", "error");
            Logger.getRootLogger().error(e.toString());
            this.setCursor(java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.DEFAULT_CURSOR));
        }
    }

    private void BuscarCliente(String filtro) {
        if (txt_filtro.getText().length() == 0) {
            OpenTratsBookingUtil.Mensaje("Debe filtrar mínimamente para poder encontrar algo", "Escriba algo en el filtro...", "exclamacion");
            txt_filtro.grabFocus();
            txt_filtro.selectAll();
        } else {
            try {
                List clientes = null;
                filtro = filtro.trim();
                if (chk_codigo.isSelected() == false && chk_tfno.isSelected() == false) {
                    clientes = getCustomerDao().findByApellidosONombre(filtro, 300);
                } else {
                    if (chk_codigo.isSelected() == true) {
                        Customer c = getCustomerDao().Load(Integer.parseInt(filtro));
                        clientes = new ArrayList();
                        clientes.add(c);
                    }
                    if (chk_tfno.isSelected() == true) {
                        clientes = getCustomerDao().findByTfnoOMovil(filtro, 300);
                    }
                }
                if (clientes.isEmpty() == false) {
                    this.MostrarClientes(clientes);
                } else {
                    tm_clientes.setRowCount(0);
                }
            } catch (Exception e) {
                OpenTratsBookingUtil.Mensaje("Error SQL durante la búsqueda de clientes: " + e.getMessage(), "Error en la búsqueda...", "error");
                Logger.getRootLogger().error(e.toString());
            }
        }
    }

    private void Vaciar_cajas() {
        try {
            txt_filtro.setText("");
        } catch (Exception e) {
            OpenTratsBookingUtil.Mensaje("Error durante la búsqueda de clientes: " + e.getMessage(), "Error en la búsqueda...", "error");
            Logger.getRootLogger().error(e.toString());
        }
    }

    private void AceptarCliente() {
        try {
            String codcliente = dg_clientes.getValueAt(dg_clientes.getSelectedRow(), 0).toString();
            this.padre.setRefCliente(codcliente);
            this.dispose();
        } catch (Exception e) {
            Logger.getRootLogger().error(e.toString());
        }
    }

    private void initComponents() {
        jsp_clientes = new javax.swing.JScrollPane();
        dg_clientes = new javax.swing.JTable();
        txt_filtro = new javax.swing.JTextField();
        lbl_filtro = new javax.swing.JLabel();
        chk_codigo = new javax.swing.JCheckBox();
        chk_tfno = new javax.swing.JCheckBox();
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("OpenTratsBooking.- Gestión de clientes");
        setResizable(false);
        getContentPane().setLayout(null);
        jsp_clientes.setBackground(new java.awt.Color(153, 153, 255));
        jsp_clientes.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        jsp_clientes.setAutoscrolls(true);
        jsp_clientes.setFocusable(false);
        dg_clientes.setFont(new java.awt.Font("MS Sans Serif", 0, 10));
        dg_clientes.setForeground(new java.awt.Color(0, 51, 153));
        dg_clientes.setModel(new javax.swing.table.DefaultTableModel(new Object[][] { { null, null, null, null }, { null, null, null, null }, { null, null, null, null }, { null, null, null, null } }, new String[] { "Title 1", "Title 2", "Title 3", "Title 4" }));
        dg_clientes.setToolTipText("<html>\n<ul>\n<il>Haz doble click, presiona intro o presiona la barra de espacio para seleccionar el cliente...</il>\n</ul>\n</html>");
        dg_clientes.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        dg_clientes.setGridColor(new java.awt.Color(0, 102, 204));
        dg_clientes.addKeyListener(new java.awt.event.KeyAdapter() {

            public void keyPressed(java.awt.event.KeyEvent evt) {
                dg_clientesKeyPressed(evt);
            }
        });
        dg_clientes.addMouseListener(new java.awt.event.MouseAdapter() {

            public void mouseClicked(java.awt.event.MouseEvent evt) {
                dg_clientesMouseClicked(evt);
            }

            public void mouseEntered(java.awt.event.MouseEvent evt) {
                dg_clientesMouseEntered(evt);
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                dg_clientesMouseExited(evt);
            }
        });
        jsp_clientes.setViewportView(dg_clientes);
        getContentPane().add(jsp_clientes);
        jsp_clientes.setBounds(5, 50, 305, 196);
        txt_filtro.setFont(new java.awt.Font("Arial", 1, 10));
        txt_filtro.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        txt_filtro.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 153), 1, true));
        txt_filtro.setNextFocusableComponent(dg_clientes);
        txt_filtro.addKeyListener(new java.awt.event.KeyAdapter() {

            public void keyPressed(java.awt.event.KeyEvent evt) {
                txt_filtroKeyPressed(evt);
            }
        });
        getContentPane().add(txt_filtro);
        txt_filtro.setBounds(5, 30, 305, 20);
        lbl_filtro.setFont(new java.awt.Font("Arial", 1, 12));
        lbl_filtro.setForeground(new java.awt.Color(0, 51, 153));
        lbl_filtro.setText("Filtro:");
        getContentPane().add(lbl_filtro);
        lbl_filtro.setBounds(5, 15, 40, 16);
        chk_codigo.setForeground(new java.awt.Color(0, 102, 153));
        chk_codigo.setText("Código");
        chk_codigo.setToolTipText("<html> Marque esta casilla para realizar la búsqueda por <b>c&oacute;digo de cliente</b></html>");
        chk_codigo.addItemListener(new java.awt.event.ItemListener() {

            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                chk_codigoItemStateChanged(evt);
            }
        });
        getContentPane().add(chk_codigo);
        chk_codigo.setBounds(160, 10, 72, 22);
        chk_tfno.setForeground(new java.awt.Color(0, 102, 153));
        chk_tfno.setText("Teléfonos");
        chk_tfno.setToolTipText("<html>Marque esta casilla para realizar la busqueda por <b>tel&eacute;fono o m&oacute;vil del cliente</b></hml>");
        chk_tfno.addItemListener(new java.awt.event.ItemListener() {

            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                chk_tfnoItemStateChanged(evt);
            }
        });
        getContentPane().add(chk_tfno);
        chk_tfno.setBounds(235, 10, 75, 22);
        pack();
    }

    private void chk_codigoItemStateChanged(java.awt.event.ItemEvent evt) {
        if (chk_codigo.isSelected() == true) {
            chk_tfno.setSelected(false);
            txt_filtro.setText("");
            txt_filtro.grabFocus();
        } else {
            txt_filtro.setText("");
            txt_filtro.grabFocus();
        }
    }

    private void chk_tfnoItemStateChanged(java.awt.event.ItemEvent evt) {
        if (chk_tfno.isSelected() == true) {
            chk_codigo.setSelected(false);
            txt_filtro.setText("");
            txt_filtro.grabFocus();
        } else {
            txt_filtro.setText("");
            txt_filtro.grabFocus();
        }
    }

    private void dg_clientesKeyPressed(java.awt.event.KeyEvent evt) {
        if (evt.getKeyCode() == KeyEvent.VK_SPACE || evt.getKeyCode() == KeyEvent.VK_ENTER) {
            AceptarCliente();
        }
    }

    private void dg_clientesMouseClicked(java.awt.event.MouseEvent evt) {
        int codcliente = Integer.parseInt(dg_clientes.getValueAt(dg_clientes.getSelectedRow(), 0).toString());
        if (evt.getButton() == MouseEvent.BUTTON1 && evt.getClickCount() >= 2) {
            AceptarCliente();
        }
    }

    private void dg_clientesMouseExited(java.awt.event.MouseEvent evt) {
        dg_clientes.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
    }

    private void dg_clientesMouseEntered(java.awt.event.MouseEvent evt) {
        dg_clientes.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }

    private void txt_filtroKeyPressed(java.awt.event.KeyEvent evt) {
        try {
            if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
                this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                this.BuscarCliente(txt_filtro.getText().trim());
                txt_filtro.grabFocus();
                txt_filtro.selectAll();
                this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
            }
            if (chk_codigo.isSelected() == true || chk_tfno.isSelected() == true) {
                if (OpenTratsBookingUtil.esNumero(evt) == false && evt.getKeyCode() != KeyEvent.VK_ENTER) {
                    OpenTratsBookingUtil.Mensaje("Sólo se aceptan números", "Sólo números...", "w");
                    txt_filtro.grabFocus();
                    txt_filtro.selectAll();
                }
            }
        } catch (Exception e) {
            OpenTratsBookingUtil.Mensaje("Error en el filtrado de clientes..." + e.getMessage(), "Error de filtrado", "error");
            Logger.getRootLogger().error(e.toString());
            this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        }
    }

    private javax.swing.JCheckBox chk_codigo;

    private javax.swing.JCheckBox chk_tfno;

    private javax.swing.JTable dg_clientes;

    private javax.swing.JScrollPane jsp_clientes;

    private javax.swing.JLabel lbl_filtro;

    private javax.swing.JTextField txt_filtro;

    public ICustomerDao getCustomerDao() {
        return customerDao;
    }

    public void setCustomerDao(ICustomerDao customerDao) {
        this.customerDao = customerDao;
    }
}
