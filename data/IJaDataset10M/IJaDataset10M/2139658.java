package kinesiologia;

import ClasesKinesiologia.Ficheros;
import ClasesKinesiologia.Paciente;
import ClasesKinesiologia.cbPro;
import ClasesKinesiologia.coneccion;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Adrian
 */
public class AgregarPaciente extends javax.swing.JFrame {

    /** Creates new form AgregarPaciente */
    private DefaultTableModel modelo;

    private cbPro proced;

    private Paciente actual;

    private Ficheros guardado;

    private DefaultListModel fichas;

    private estados estadoact;

    private coneccion conexion;

    private int borra;

    public AgregarPaciente(DefaultTableModel mod) {
        initComponents();
        estadoact = estados.alta;
        modelo = mod;
        this.btAbrir.setEnabled(false);
        this.btBorrar.setEnabled(false);
        fichas = new DefaultListModel();
        this.Lfichas.setModel(fichas);
        actual = null;
        conexion = new coneccion();
        proced = new cbPro();
        guardado = new Ficheros();
        this.setSize(450, 400);
        cargaCBOS();
        panelFicha.setVisible(false);
        this.setTitle("Paciente");
        cargaCBLocalidades();
    }

    private void vistaTotal() {
        this.setSize(700, 350);
        panelFicha.setVisible(true);
    }

    public enum estados {

        alta(1), modifica(2), guardado(3);

        private int num;

        estados(int e) {
            this.num = e;
        }

        public int getEstado() {
            return num;
        }
    }

    private void cargaCBLocalidades() {
        this.cbLocalidades.removeAllItems();
        cbLocalidades.addItem("");
        ResultSet pas = conexion.hacerConsultaSelect("select * from localidad");
        if (pas != null) {
            try {
                while (pas.next() == true) {
                    this.cbLocalidades.addItem(pas.getString("nombre_localidad"));
                }
            } catch (SQLException ex) {
                Logger.getLogger(AgregarPaciente.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                conexion.cerrarConeccion();
            }
        }
    }

    public void cambiaDatos(Paciente p, int ro) {
        estadoact = estados.modifica;
        actual = p;
        borra = ro;
        this.setSize(600, 350);
        panelFicha.setVisible(true);
        this.tbDoc.setText(p.getDoc());
        this.tbDom.setText(p.getDom());
        this.tbNom.setText(p.getApe());
        this.cbLocalidades.setSelectedItem(p.getLoc());
        this.tbTel.setText(p.getTel());
        this.datechoser.setDate(p.getFN());
        String aux = p.getOS();
        this.cbOS.setSelectedItem(aux);
        this.btFicha.setVisible(false);
        ResultSet fich = conexion.hacerConsultaSelect("select * from ficha where dni_Paciente=" + p.getDoc());
        if (fich != null) {
            try {
                while (fich.next() == true) {
                    fichas.addElement(fich.getString("nombre_localidad"));
                }
                conexion.cerrarConeccion();
            } catch (SQLException ex) {
                Logger.getLogger(AgregarPaciente.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private void cargaCBOS() {
        this.cbOS.removeAllItems();
        ResultSet pas = conexion.hacerConsultaSelect("select * from obraSocial");
        if (pas != null) {
            try {
                while (pas.next() == true) {
                    this.cbOS.addItem(pas.getString("nombre_obraSocial"));
                }
                conexion.cerrarConeccion();
            } catch (SQLException ex) {
                Logger.getLogger(AgregarPaciente.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {
        jButton2 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        tbNom = new javax.swing.JTextField();
        tbDoc = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        tbDom = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        tbTel = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox();
        jButton1 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jLabel7 = new javax.swing.JLabel();
        cbOS = new javax.swing.JComboBox();
        panelFicha = new javax.swing.JPanel();
        btAbrir = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        Lfichas = new javax.swing.JList();
        jButton5 = new javax.swing.JButton();
        jLabel8 = new javax.swing.JLabel();
        btBorrar = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JSeparator();
        btFicha = new javax.swing.JButton();
        cbLocalidades = new javax.swing.JComboBox();
        datechoser = new com.toedter.calendar.JDateChooser();
        jButton4 = new javax.swing.JButton();
        jButton2.setText("jButton2");
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        jLabel1.setText("Nombre y apellido:");
        tbNom.addKeyListener(new java.awt.event.KeyAdapter() {

            public void keyTyped(java.awt.event.KeyEvent evt) {
                tbNomKeyTyped(evt);
            }
        });
        tbDoc.addKeyListener(new java.awt.event.KeyAdapter() {

            public void keyTyped(java.awt.event.KeyEvent evt) {
                tbDocKeyTyped(evt);
            }
        });
        jLabel2.setText("Documento :");
        jLabel3.setText("Fecha Nacimiento :");
        jLabel4.setText("Localidad :");
        tbDom.addKeyListener(new java.awt.event.KeyAdapter() {

            public void keyTyped(java.awt.event.KeyEvent evt) {
                tbDomKeyTyped(evt);
            }
        });
        jLabel5.setText("Domicilio :");
        tbTel.addKeyListener(new java.awt.event.KeyAdapter() {

            public void keyTyped(java.awt.event.KeyEvent evt) {
                tbTelKeyTyped(evt);
            }
        });
        jLabel6.setText("Telefono :");
        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jButton1.setText("Aceptar");
        jButton1.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jButton3.setText("Cancelar");
        jButton3.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        jLabel7.setText("Obra Social :");
        cbOS.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cbOS.addItemListener(new java.awt.event.ItemListener() {

            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cbOSItemStateChanged(evt);
            }
        });
        cbOS.addKeyListener(new java.awt.event.KeyAdapter() {

            public void keyTyped(java.awt.event.KeyEvent evt) {
                cbOSKeyTyped(evt);
            }
        });
        panelFicha.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        btAbrir.setText("Abrir");
        btAbrir.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btAbrirActionPerformed(evt);
            }
        });
        Lfichas.setModel(new javax.swing.AbstractListModel() {

            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };

            public int getSize() {
                return strings.length;
            }

            public Object getElementAt(int i) {
                return strings[i];
            }
        });
        Lfichas.addMouseListener(new java.awt.event.MouseAdapter() {

            public void mouseClicked(java.awt.event.MouseEvent evt) {
                LfichasMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(Lfichas);
        jButton5.setText("Nueva Ficha");
        jButton5.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });
        jLabel8.setText("Ficha");
        btBorrar.setText("Borrar");
        btBorrar.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btBorrarActionPerformed(evt);
            }
        });
        javax.swing.GroupLayout panelFichaLayout = new javax.swing.GroupLayout(panelFicha);
        panelFicha.setLayout(panelFichaLayout);
        panelFichaLayout.setHorizontalGroup(panelFichaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(panelFichaLayout.createSequentialGroup().addContainerGap().addGroup(panelFichaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jLabel8).addComponent(jButton5).addGroup(panelFichaLayout.createSequentialGroup().addComponent(btAbrir).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(btBorrar)).addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)).addContainerGap(29, Short.MAX_VALUE)));
        panelFichaLayout.setVerticalGroup(panelFichaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelFichaLayout.createSequentialGroup().addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addGap(4, 4, 4).addComponent(jButton5).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addGap(18, 18, 18).addGroup(panelFichaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(btAbrir).addComponent(btBorrar)).addContainerGap()));
        jSeparator1.setOrientation(javax.swing.SwingConstants.VERTICAL);
        btFicha.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/ficha.gif")));
        btFicha.setToolTipText("Ver Fichas");
        btFicha.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btFichaActionPerformed(evt);
            }
        });
        cbLocalidades.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cbLocalidades.addItemListener(new java.awt.event.ItemListener() {

            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cbLocalidadesItemStateChanged(evt);
            }
        });
        cbLocalidades.addKeyListener(new java.awt.event.KeyAdapter() {

            public void keyTyped(java.awt.event.KeyEvent evt) {
                cbLocalidadesKeyTyped(evt);
            }
        });
        jButton4.setText("Agregar");
        jButton4.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING).addComponent(jLabel2).addComponent(jLabel5).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addGap(2, 2, 2).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING).addComponent(jLabel7).addComponent(jLabel6).addComponent(jLabel4).addComponent(jLabel1))).addComponent(jLabel3))).addGap(37, 37, 37).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(cbOS, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(tbTel, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE)).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addGap(32, 32, 32).addComponent(btFicha, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)).addGroup(layout.createSequentialGroup().addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)))).addComponent(tbNom, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(tbDoc, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(datechoser, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false).addComponent(cbLocalidades, javax.swing.GroupLayout.Alignment.LEADING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addComponent(tbDom, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 118, Short.MAX_VALUE))).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)).addGroup(layout.createSequentialGroup().addComponent(jButton1).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 156, Short.MAX_VALUE).addComponent(jButton3).addGap(27, 27, 27))).addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addComponent(panelFicha, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, javax.swing.GroupLayout.PREFERRED_SIZE).addContainerGap()));
        layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addGap(71, 71, 71).addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING).addComponent(jSeparator1, javax.swing.GroupLayout.DEFAULT_SIZE, 314, Short.MAX_VALUE).addGroup(layout.createSequentialGroup().addContainerGap().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addGap(22, 22, 22).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(tbNom, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jLabel1)).addGap(7, 7, 7).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(tbDoc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jLabel2)).addGap(11, 11, 11).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING).addComponent(datechoser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jLabel3)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel4).addComponent(cbLocalidades, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(tbDom, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jLabel5)).addGap(7, 7, 7).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(tbTel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jLabel6)).addGap(7, 7, 7).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(cbOS, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jLabel7).addComponent(jButton4))).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup().addComponent(btFicha).addGap(31, 31, 31))).addGap(18, 18, 18).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jButton1).addComponent(jButton3)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 64, Short.MAX_VALUE)).addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup().addContainerGap().addComponent(panelFicha, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))).addContainerGap()));
        jLabel2.getAccessibleContext().setAccessibleName("Documento : ");
        pack();
    }

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {
        if (this.estadoact == estados.alta) {
            int opc = JOptionPane.showConfirmDialog(this, "Desea Salir sin guardar?");
            if (opc == 0) {
                dispose();
            }
        } else {
            dispose();
        }
    }

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {
        if (estadoact != estados.guardado) {
            if (guardaPac() == true) {
                dispose();
            }
        } else {
            dispose();
        }
    }

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {
        String nom = JOptionPane.showInputDialog("Ingrese el nombre de la ficha");
        if (nom != null && nom.equals("") != true) {
            fichas.addElement(nom);
            AgregarFichero f = new AgregarFichero(actual);
            f.show();
            f.setTitle(nom);
        } else {
            if (nom != null) JOptionPane.showMessageDialog(rootPane, "No ha ingresado el nombre, por favor ingreselo");
        }
    }

    private void btFichaActionPerformed(java.awt.event.ActionEvent evt) {
        if (estadoact == estados.guardado) {
            vistaTotal();
        } else {
            if (this.guardaPac() == true) {
                vistaTotal();
            }
        }
    }

    private void btAbrirActionPerformed(java.awt.event.ActionEvent evt) {
        String nom = this.fichas.getElementAt(this.Lfichas.getSelectedIndex()).toString();
        AgregarFichero aux = new AgregarFichero(actual);
        aux.show();
        aux.cargaFic(nom);
    }

    private void btBorrarActionPerformed(java.awt.event.ActionEvent evt) {
        String nom = this.fichas.getElementAt(this.Lfichas.getSelectedIndex()).toString();
        conexion.hacerConsultas("select * from ficha where dni_Paciente=" + nom);
        fichas.removeElement(nom);
    }

    private void LfichasMouseClicked(java.awt.event.MouseEvent evt) {
        this.btAbrir.setEnabled(true);
        this.btBorrar.setEnabled(true);
    }

    private void tbDocKeyTyped(java.awt.event.KeyEvent evt) {
        char n;
        n = evt.getKeyChar();
        if (n != '0' && n != '1' && n != '2' && n != '3' && n != '4' && n != '5' && n != '6' && n != '7' && n != '8' && n != '9') evt.consume();
    }

    private void cbOSKeyTyped(java.awt.event.KeyEvent evt) {
        this.escriveCB(evt, cbOS);
    }

    private void cbLocalidadesKeyTyped(java.awt.event.KeyEvent evt) {
        this.escriveCB(evt, this.cbLocalidades);
    }

    private void tbNomKeyTyped(java.awt.event.KeyEvent evt) {
        if (estadoact != estados.alta) this.estadoact = estados.modifica;
    }

    private void datechoserKeyTyped(java.awt.event.KeyEvent evt) {
        if (estadoact != estados.alta) this.estadoact = estados.modifica;
    }

    private void cbLocalidadesItemStateChanged(java.awt.event.ItemEvent evt) {
        if (estadoact != estados.alta) this.estadoact = estados.modifica;
    }

    private void tbDomKeyTyped(java.awt.event.KeyEvent evt) {
        if (estadoact != estados.alta) this.estadoact = estados.modifica;
    }

    private void tbTelKeyTyped(java.awt.event.KeyEvent evt) {
        if (estadoact != estados.alta) this.estadoact = estados.modifica;
    }

    private void cbOSItemStateChanged(java.awt.event.ItemEvent evt) {
        if (estadoact != estados.alta) this.estadoact = estados.modifica;
    }

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {
        cbOS.removeAllItems();
        cbOS.addItem("");
        cbOS.setSelectedItem("");
    }

    private void escriveCB(java.awt.event.KeyEvent evt, javax.swing.JComboBox esc) {
        String p = esc.getSelectedItem().toString();
        if (evt.getKeyChar() != 8) {
            p = p + evt.getKeyChar();
        } else {
            if (p.length() != 0) {
                p = p.substring(0, (p.length() - 1));
            }
        }
        esc.removeAllItems();
        esc.addItem(p);
        esc.setSelectedItem(p);
    }

    private boolean buscaOS(String nombre) {
        coneccion conexion = new coneccion();
        ResultSet pas = conexion.hacerConsultaSelect("select * from obraSocial");
        try {
            pas.first();
            if (pas.getString("nombre_obraSocial").equals(nombre) == true) return true;
        } catch (SQLException ex) {
            Logger.getLogger(AgregarPaciente.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            conexion.cerrarConeccion();
        }
        return false;
    }

    private boolean buscaLoc(String nombre) {
        coneccion conexion = new coneccion();
        ResultSet pas = conexion.hacerConsultaSelect("select * from localidad");
        try {
            pas.first();
            if (pas.getString("nombre_localidad").equals(nombre) == true) return true;
        } catch (SQLException ex) {
            Logger.getLogger(AgregarPaciente.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            conexion.cerrarConeccion();
        }
        return false;
    }

    private boolean guardaPac() {
        boolean resp = false;
        coneccion conexion = new coneccion();
        String nombre = String.valueOf(cbOS.getSelectedItem());
        String locact = this.cbLocalidades.getSelectedItem().toString();
        String os = nombre;
        if (buscaOS(nombre) == false) {
            ResultSet pas = conexion.hacerConsultaSelect("select * from obraSocial");
            try {
                pas.moveToInsertRow();
                pas.updateString("nombre_obraSocial", nombre);
                pas.insertRow();
            } catch (SQLException ex) {
                Logger.getLogger(AgregarPaciente.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        if (buscaLoc(locact) == false) {
            ResultSet pas = conexion.hacerConsultaSelect("select * from localidad");
            try {
                pas.moveToInsertRow();
                pas.updateString("nombre_localidad", locact);
                pas.insertRow();
            } catch (SQLException ex) {
                Logger.getLogger(AgregarPaciente.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                conexion.cerrarConeccion();
            }
        }
        Calendar ac = this.datechoser.getCalendar();
        GregorianCalendar nac = (GregorianCalendar) ac;
        if (tbNom.getText().equals("") == false && tbNom.getText() != null && locact.equals("") == false && locact != null && tbDom.getText().equals("") == false && tbDom.getText() != null && tbTel.getText().equals("") == false && tbTel.getText() != null && os != null) {
            Paciente ne = new Paciente(tbNom.getText(), tbDoc.getText(), datechoser.getDate(), this.cbLocalidades.getSelectedItem().toString(), tbDom.getText(), tbTel.getText(), os);
            if (estadoact == estados.alta) {
                ResultSet pas = conexion.hacerConsultaSelect("select * from paciente");
                try {
                    pas.moveToInsertRow();
                    pas.updateString("nombre_paciente", ne.getApe());
                    pas.updateString("dni_paciente", ne.getDoc());
                    pas.updateDate("fecha_nacimiento", (java.sql.Date) ne.getFN());
                    pas.updateString("nombre_localidad", ne.getLoc());
                    pas.updateString("domicilio", ne.getDom());
                    pas.updateString("telefono", ne.getTel());
                    pas.updateString("nombre_obraSocial", ne.getOS());
                    pas.insertRow();
                } catch (SQLException ex) {
                    Logger.getLogger(AgregarPaciente.class.getName()).log(Level.SEVERE, null, ex);
                } finally {
                    conexion.cerrarConeccion();
                }
                this.agregaMod(ne);
            }
            if (estadoact == estados.modifica) {
                ResultSet pas = conexion.hacerConsultaSelect("select * from paciente where dni_paciente=" + ne.getDoc());
                try {
                    pas.first();
                    pas.updateString("nombre_paciente", ne.getApe());
                    pas.updateString("dni_paciente", ne.getDoc());
                    pas.updateDate("fecha_nacimiento", (java.sql.Date) ne.getFN());
                    pas.updateString("nombre_localidad", ne.getLoc());
                    pas.updateString("domicilio", ne.getDom());
                    pas.updateString("telefono", ne.getTel());
                    pas.updateString("nombre_obraSocial", ne.getOS());
                    pas.updateRow();
                } catch (SQLException ex) {
                    Logger.getLogger(AgregarPaciente.class.getName()).log(Level.SEVERE, null, ex);
                } finally {
                    conexion.cerrarConeccion();
                }
                modelo.removeRow(borra);
                agregaMod(ne);
            }
            estadoact = estados.guardado;
            return true;
        } else JOptionPane.showMessageDialog(rootPane, "Algun campo no ha sido llenado por favor verifiquelo");
        return false;
    }

    public void agregaMod(Paciente p) {
        String[] fila = new String[7];
        fila[0] = p.getApe();
        fila[1] = p.getDoc();
        fila[2] = "0";
        fila[3] = p.getLoc();
        fila[4] = p.getDom();
        fila[5] = p.getTel();
        fila[6] = p.getOS();
        modelo.addRow(fila);
    }

    private javax.swing.JList Lfichas;

    private javax.swing.JButton btAbrir;

    private javax.swing.JButton btBorrar;

    private javax.swing.JButton btFicha;

    private javax.swing.JComboBox cbLocalidades;

    private javax.swing.JComboBox cbOS;

    private com.toedter.calendar.JDateChooser datechoser;

    private javax.swing.JButton jButton1;

    private javax.swing.JButton jButton2;

    private javax.swing.JButton jButton3;

    private javax.swing.JButton jButton4;

    private javax.swing.JButton jButton5;

    private javax.swing.JComboBox jComboBox1;

    private javax.swing.JLabel jLabel1;

    private javax.swing.JLabel jLabel2;

    private javax.swing.JLabel jLabel3;

    private javax.swing.JLabel jLabel4;

    private javax.swing.JLabel jLabel5;

    private javax.swing.JLabel jLabel6;

    private javax.swing.JLabel jLabel7;

    private javax.swing.JLabel jLabel8;

    private javax.swing.JScrollPane jScrollPane1;

    private javax.swing.JSeparator jSeparator1;

    private javax.swing.JPanel panelFicha;

    private javax.swing.JTextField tbDoc;

    private javax.swing.JTextField tbDom;

    private javax.swing.JTextField tbNom;

    private javax.swing.JTextField tbTel;
}
