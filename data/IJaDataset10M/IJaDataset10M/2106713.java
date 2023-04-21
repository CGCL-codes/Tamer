package mx.uacam.balam.simulacion.registrocic.ui;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import mx.uacam.balam.simulacion.registrocic.data.HSQLServer;
import mx.uacam.balam.simulacion.registrocic.data.HibernateUtil;
import mx.uacam.balam.simulacion.registrocic.data.dao.AlumnoDAO;
import mx.uacam.balam.simulacion.registrocic.data.dao.ComputadoraDAO;
import mx.uacam.balam.simulacion.registrocic.data.dao.SoftwareDAO;
import mx.uacam.balam.simulacion.registrocic.data.entities.Alumno;
import mx.uacam.balam.simulacion.registrocic.data.entities.Computadora;
import mx.uacam.balam.simulacion.registrocic.data.entities.EstadoPc;
import mx.uacam.balam.simulacion.registrocic.data.entities.PrestamoPc;
import mx.uacam.balam.simulacion.registrocic.data.entities.PrestamoPcId;
import mx.uacam.balam.simulacion.registrocic.data.entities.Software;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import static mx.uacam.balam.simulacion.registrocic.util.OperationConstants.factory;

/**
 *
 * @author Freddy
 */
public class VentanaAcceso extends javax.swing.JFrame {

    /**
     * Contructor de la clase por defecto
     */
    private VentanaAcceso() {
        server = new HSQLServer();
        server.start();
        alumnoDAO = factory.getAlumnoDAO();
        computadoraDAO = factory.getComputadoraDAO();
        softwareDAO = factory.getSoftwareDAO();
        initComponents();
        Dimension pantalla = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension ventana = getSize();
        setLocation((pantalla.width - ventana.width) / 2, (pantalla.height - ventana.height) / 2);
    }

    public static VentanaAcceso getInstance() {
        return SingletonHolder.instance;
    }

    private static class SingletonHolder {

        private static final VentanaAcceso instance = new VentanaAcceso();
    }

    private void initComponents() {
        jPanel1 = new javax.swing.JPanel();
        backgroundJPanel1 = new mx.neocs.beans.panel.BackgroundJPanel();
        fondoJPanel1 = new mx.neocs.beans.panel.FondoJPanel();
        jLabel1 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jLabel2 = new javax.swing.JLabel();
        jFormattedTextField1 = new javax.swing.JFormattedTextField();
        jLabel3 = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jXStatusBar1 = new org.jdesktop.swingx.JXStatusBar();
        statusJLabel = new javax.swing.JLabel();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        jMenuItem2 = new javax.swing.JMenuItem();
        jMenu3 = new javax.swing.JMenu();
        jMenuItem3 = new javax.swing.JMenuItem();
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Prestamo de computadora");
        setMinimumSize(new java.awt.Dimension(464, 400));
        setName("VentanaAcceso");
        addWindowListener(new java.awt.event.WindowAdapter() {

            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });
        jPanel1.setBackground(new java.awt.Color(30, 64, 100));
        backgroundJPanel1.setFondoImagenIcon(new javax.swing.ImageIcon(getClass().getResource("/mx/uacam/balam/simulacion/registrocic/resources/imagenes/banner_uac_avanza.png")));
        backgroundJPanel1.setMinimumSize(new java.awt.Dimension(10, 150));
        fondoJPanel1.setBackground(java.awt.Color.white);
        fondoJPanel1.setAlpha(255);
        fondoJPanel1.setRedondes(25);
        jLabel1.setFont(new java.awt.Font("Bookman Old Style", 1, 26));
        jLabel1.setForeground(new java.awt.Color(0, 51, 102));
        jLabel1.setText("Bienvenido");
        jLabel2.setText("Matricula:");
        jFormattedTextField1.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0"))));
        jFormattedTextField1.setToolTipText("");
        jLabel3.setText("Software:");
        jComboBox1.addItemListener(new java.awt.event.ItemListener() {

            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBox1ItemStateChanged(evt);
            }
        });
        jComboBox1.addFocusListener(new java.awt.event.FocusAdapter() {

            public void focusGained(java.awt.event.FocusEvent evt) {
                jComboBox1FocusGained(evt);
            }
        });
        jButton1.setText("Usar equipo");
        jButton1.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                usarEquipo(evt);
            }
        });
        jButton2.setText("Liberar equipo");
        jButton2.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                liberarEquipo(evt);
            }
        });
        javax.swing.GroupLayout fondoJPanel1Layout = new javax.swing.GroupLayout(fondoJPanel1);
        fondoJPanel1.setLayout(fondoJPanel1Layout);
        fondoJPanel1Layout.setHorizontalGroup(fondoJPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(fondoJPanel1Layout.createSequentialGroup().addGroup(fondoJPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(fondoJPanel1Layout.createSequentialGroup().addContainerGap().addComponent(jLabel1).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jSeparator1, javax.swing.GroupLayout.DEFAULT_SIZE, 282, Short.MAX_VALUE)).addGroup(fondoJPanel1Layout.createSequentialGroup().addGap(57, 57, 57).addGroup(fondoJPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jLabel3, javax.swing.GroupLayout.Alignment.TRAILING).addComponent(jLabel2, javax.swing.GroupLayout.Alignment.TRAILING)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addGroup(fondoJPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false).addComponent(jComboBox1, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addComponent(jFormattedTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 221, javax.swing.GroupLayout.PREFERRED_SIZE).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, fondoJPanel1Layout.createSequentialGroup().addComponent(jButton1).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jButton2))))).addContainerGap()));
        fondoJPanel1Layout.setVerticalGroup(fondoJPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(fondoJPanel1Layout.createSequentialGroup().addContainerGap().addGroup(fondoJPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING).addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jLabel1)).addGap(35, 35, 35).addGroup(fondoJPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel2).addComponent(jFormattedTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(fondoJPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel3).addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addGap(18, 18, 18).addGroup(fondoJPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jButton1).addComponent(jButton2)).addContainerGap(48, Short.MAX_VALUE)));
        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup().addContainerGap().addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING).addComponent(fondoJPanel1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addComponent(backgroundJPanel1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 458, Short.MAX_VALUE)).addContainerGap()));
        jPanel1Layout.setVerticalGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel1Layout.createSequentialGroup().addContainerGap().addComponent(backgroundJPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addComponent(fondoJPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addContainerGap()));
        statusJLabel.setText("Listo");
        jXStatusBar1.add(statusJLabel);
        jMenu1.setText("Archivo");
        jMenuItem1.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F4, java.awt.event.InputEvent.ALT_MASK));
        jMenuItem1.setText("Salir");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                salir(evt);
            }
        });
        jMenu1.add(jMenuItem1);
        jMenuBar1.add(jMenu1);
        jMenu2.setText("Edición");
        jMenuItem2.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_D, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem2.setText("Administrar");
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                iniciarAdministrador(evt);
            }
        });
        jMenu2.add(jMenuItem2);
        jMenuBar1.add(jMenu2);
        jMenu3.setText("Ayuda");
        jMenuItem3.setText("Acerca de");
        jMenuItem3.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mostrarAcercaDe(evt);
            }
        });
        jMenu3.add(jMenuItem3);
        jMenuBar1.add(jMenu3);
        setJMenuBar(jMenuBar1);
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jXStatusBar1, javax.swing.GroupLayout.DEFAULT_SIZE, 478, Short.MAX_VALUE).addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE));
        layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup().addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jXStatusBar1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)));
    }

    private void usarEquipo(java.awt.event.ActionEvent evt) {
    }

    private void iniciarAdministrador(java.awt.event.ActionEvent evt) {
        statusJLabel.setText("Cargando ventana de administrador...");
        Thread t = new Thread() {

            @Override
            public void run() {
                try {
                    sleep(2000);
                    statusJLabel.setText("Listo");
                } catch (InterruptedException ex) {
                    Logger.getLogger(VentanaAcceso.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        };
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                v = VentanaAdministracion.getInstance();
                v.setVisible(true);
            }
        });
        t.start();
    }

    private void jComboBox1FocusGained(java.awt.event.FocusEvent evt) {
        List<Software> softwareDisponible = softwareDAO.findAll();
        if (softwareDisponible.isEmpty()) {
            softwareDisponible.add(new Software("No se ha encontrado alguna especialidad"));
        }
        jComboBox1.setModel(new DefaultComboBoxModel(softwareDisponible.toArray()));
    }

    private void mostrarAcercaDe(java.awt.event.ActionEvent evt) {
        new JDialogAcercaDe(this, true).setVisible(true);
    }

    private void jComboBox1ItemStateChanged(java.awt.event.ItemEvent evt) {
        seleccionoSoftware = true;
    }

    private void liberarEquipo(java.awt.event.ActionEvent evt) {
        Session session = null;
        Transaction transaction = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            transaction = session.beginTransaction();
            Query consulta = session.createQuery("from PrestamoPc as pres where pres.alumno.matricula = '" + jFormattedTextField1.getText() + "'and pres.horaFin = null");
            PrestamoPc prestamoPc = (PrestamoPc) consulta.list().get(0);
            prestamoPc.setHoraFin(Calendar.getInstance().getTime());
            EstadoPc estadoPc = (EstadoPc) session.createQuery("from EstadoPc as estado where estado.nombre = \'disponible\'").list().get(0);
            prestamoPc.getComputadora().setEstadoPc(estadoPc);
            session.update(prestamoPc);
            JOptionPane.showMessageDialog(null, "El equipo se ha liberado correctamente");
        } catch (HibernateException ex) {
        } finally {
            transaction.commit();
            session.close();
        }
    }

    private void salir(java.awt.event.ActionEvent evt) {
        if (v != null) {
            v.setVisible(false);
            v = null;
        }
        setVisible(false);
        server.shutdown();
        System.exit(0);
    }

    private void formWindowClosing(java.awt.event.WindowEvent evt) {
        if (v != null) {
            v.setVisible(false);
            v = null;
        }
        setVisible(false);
        server.shutdown();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        try {
            if (System.getProperty("os.name").contains("Windows")) {
                UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
            } else {
                UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(VentanaAcceso.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            Logger.getLogger(VentanaAcceso.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(VentanaAcceso.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnsupportedLookAndFeelException ex) {
            Logger.getLogger(VentanaAcceso.class.getName()).log(Level.SEVERE, null, ex);
        }
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                VentanaAcceso.getInstance().setVisible(true);
            }
        });
    }

    private boolean seleccionoSoftware = false;

    private static final long serialVersionUID = -8764302563354031649L;

    private AlumnoDAO alumnoDAO;

    private ComputadoraDAO computadoraDAO;

    private SoftwareDAO softwareDAO;

    private HSQLServer server;

    private VentanaAdministracion v;

    private mx.neocs.beans.panel.BackgroundJPanel backgroundJPanel1;

    private mx.neocs.beans.panel.FondoJPanel fondoJPanel1;

    private javax.swing.JButton jButton1;

    private javax.swing.JButton jButton2;

    private javax.swing.JComboBox jComboBox1;

    private javax.swing.JFormattedTextField jFormattedTextField1;

    private javax.swing.JLabel jLabel1;

    private javax.swing.JLabel jLabel2;

    private javax.swing.JLabel jLabel3;

    private javax.swing.JMenu jMenu1;

    private javax.swing.JMenu jMenu2;

    private javax.swing.JMenu jMenu3;

    private javax.swing.JMenuBar jMenuBar1;

    private javax.swing.JMenuItem jMenuItem1;

    private javax.swing.JMenuItem jMenuItem2;

    private javax.swing.JMenuItem jMenuItem3;

    private javax.swing.JPanel jPanel1;

    private javax.swing.JSeparator jSeparator1;

    private org.jdesktop.swingx.JXStatusBar jXStatusBar1;

    private javax.swing.JLabel statusJLabel;
}
