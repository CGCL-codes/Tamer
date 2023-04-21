package presentacion;

import java.awt.Color;
import java.awt.Rectangle;
import javax.swing.JButton;
import javax.swing.JDesktopPane;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;
import java.rmi.RemoteException;
import rmi.Cliente;

public class ObtenerVehiculoYConductorFrame extends JFrame {

    private static final long serialVersionUID = 1L;

    private JDesktopPane jDesktopPane = null;

    private JButton salir = null;

    private JButton atras = null;

    private JButton seleccionarConductor = null;

    private JButton introducirDni = null;

    private JButton consultarConductor = null;

    private JButton registrarInfraccion = null;

    private JScrollPane jScrollPane = null;

    private JTable jTable = null;

    private JLabel jLabel = null;

    private JLabel conductor = null;

    private JLabel dni = null;

    private JLabel consultar = null;

    private JLabel infraccion = null;

    /**
	 * This is the default constructor
	 */
    public ObtenerVehiculoYConductorFrame() {
        super();
        initialize();
        jDesktopPane.setBorder(new ImagenFondo());
    }

    /**
	 * This method initializes this
	 * 
	 * @return void
	 */
    private void initialize() {
        this.setSize(800, 600);
        this.setContentPane(getJDesktopPane());
        this.setLocation(160, 100);
        getContentPane().setBackground(new java.awt.Color(15, 90, 130));
        this.setTitle("PuntoMatik - Gesti�n integrada de las infracciones que cometen los conductores ");
        this.setResizable(false);
    }

    /**
	 * This method initializes jContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
    private JDesktopPane getJDesktopPane() {
        if (jDesktopPane == null) {
            jLabel = new JLabel();
            jLabel.setText("Vehiculo y Conductor habitual: ");
            jLabel.setFont(new java.awt.Font("Cambria", 1, 20));
            jLabel.setBounds(new Rectangle(15, 10, 290, 30));
            jLabel.setForeground(Color.red);
            conductor = new JLabel();
            conductor.setText("Si quieres seleccionar el conductor habitual: ");
            conductor.setFont(new java.awt.Font("Cambria", 1, 18));
            conductor.setBounds(new Rectangle(13, 178, 391, 30));
            conductor.setForeground(Color.red);
            dni = new JLabel();
            dni.setText("Si quieres seleccionar otro conductor: ");
            dni.setFont(new java.awt.Font("Cambria", 1, 18));
            dni.setBounds(new Rectangle(14, 261, 322, 30));
            dni.setForeground(Color.red);
            consultar = new JLabel();
            consultar.setText("Si quieres consultar un conductor: ");
            consultar.setFont(new java.awt.Font("Cambria", 1, 18));
            consultar.setBounds(new Rectangle(15, 345, 322, 30));
            consultar.setForeground(Color.red);
            infraccion = new JLabel();
            infraccion.setBounds(new Rectangle(15, 424, 322, 30));
            infraccion.setText("Una vez seleccionado el conductor: ");
            infraccion.setFont(new java.awt.Font("Cambria", 1, 18));
            infraccion.setForeground(Color.red);
            jDesktopPane = new JDesktopPane();
            jDesktopPane.add(getSalir(), null);
            jDesktopPane.add(getAtras(), null);
            jDesktopPane.add(getJScrollPane(), null);
            jDesktopPane.add(jLabel, null);
            jDesktopPane.add(getSeleccionarConductor(), null);
            jDesktopPane.add(getIntroducirDni(), null);
            jDesktopPane.add(getConsultarConductor(), null);
            jDesktopPane.add(getRegistrarInfraccion(), null);
            jDesktopPane.add(conductor, null);
            jDesktopPane.add(dni, null);
            jDesktopPane.add(infraccion, null);
            jDesktopPane.add(consultar, null);
        }
        return jDesktopPane;
    }

    /**
	 * This method initializes salir
	 * 
	 * @return javax.swing.JButton
	 */
    private JButton getSalir() {
        if (salir == null) {
            salir = new JButton();
            salir.setBounds(new Rectangle(187, 502, 131, 45));
            salir.setText("SALIR");
            salir.setFont(new java.awt.Font("CASTELLAR", 1, 20));
            salir.addMouseListener(new java.awt.event.MouseAdapter() {

                public void mouseClicked(java.awt.event.MouseEvent e) {
                    System.exit(0);
                }
            });
        }
        return salir;
    }

    /**
	 * This method initializes atras
	 * 
	 * @return javax.swing.JButton
	 */
    private JButton getAtras() {
        if (atras == null) {
            atras = new JButton();
            atras.setBounds(new Rectangle(459, 504, 138, 40));
            atras.setText("ATRAS");
            atras.setFont(new java.awt.Font("CASTELLAR", 1, 20));
            atras.addMouseListener(new java.awt.event.MouseAdapter() {

                public void mouseClicked(java.awt.event.MouseEvent e) {
                    Objetos.dniIntroducido = false;
                    dispose();
                    RegistrarInfraccionFrame riframe = new RegistrarInfraccionFrame();
                    riframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                    riframe.setVisible(true);
                }
            });
        }
        return atras;
    }

    /**
	 * This method initializes SeleccionarConductor
	 * 
	 * @return javax.swing.JButton
	 */
    private JButton getSeleccionarConductor() {
        if (seleccionarConductor == null) {
            seleccionarConductor = new JButton();
            seleccionarConductor.setText("SELECCIONAR CONDUCTOR");
            seleccionarConductor.setBounds(new Rectangle(413, 175, 360, 35));
            seleccionarConductor.setFont(new java.awt.Font("CASTELLAR", 1, 20));
            seleccionarConductor.addMouseListener(new java.awt.event.MouseAdapter() {

                public void mouseClicked(java.awt.event.MouseEvent e) {
                    Objetos.dniFinal = auxDNI;
                    JOptionPane.showMessageDialog(null, "DNI introducido con �xito: " + Objetos.dniFinal);
                    Objetos.dniIntroducido = true;
                }
            });
        }
        return seleccionarConductor;
    }

    /**
	 * This method initializes IntroducirDni
	 * 
	 * @return javax.swing.JButton
	 */
    private JButton getIntroducirDni() {
        if (introducirDni == null) {
            introducirDni = new JButton();
            introducirDni.setText("INTRODUCIR DNI");
            introducirDni.setBounds(new Rectangle(413, 256, 360, 35));
            introducirDni.setFont(new java.awt.Font("CASTELLAR", 1, 20));
            introducirDni.addMouseListener(new java.awt.event.MouseAdapter() {

                public void mouseClicked(java.awt.event.MouseEvent e) {
                    introducirDNI();
                }
            });
        }
        return introducirDni;
    }

    /**
	 * This method initializes ConsultarConductor
	 * 
	 * @return javax.swing.JButton
	 */
    private JButton getConsultarConductor() {
        if (consultarConductor == null) {
            consultarConductor = new JButton();
            consultarConductor.setText("CONSULTAR CONDUCTOR");
            consultarConductor.setBounds(new Rectangle(413, 339, 360, 35));
            consultarConductor.setFont(new java.awt.Font("CASTELLAR", 1, 20));
            consultarConductor.addMouseListener(new java.awt.event.MouseAdapter() {

                public void mouseClicked(java.awt.event.MouseEvent e) {
                    if (Objetos.dniIntroducido) {
                        dispose();
                        ConsultarInformacionConductorFrame ccframe = new ConsultarInformacionConductorFrame();
                        ccframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                        ccframe.setVisible(true);
                    } else {
                        JOptionPane.showMessageDialog(null, "Todav�a no has elegido un DNI");
                    }
                }
            });
        }
        return consultarConductor;
    }

    /**
	 * This method initializes registrarInfraccion
	 * 
	 * @return javax.swing.JButton
	 */
    private JButton getRegistrarInfraccion() {
        if (registrarInfraccion == null) {
            registrarInfraccion = new JButton();
            registrarInfraccion.setBounds(new Rectangle(413, 420, 360, 35));
            registrarInfraccion.setText("REGISTRAR INFRACCI�N");
            registrarInfraccion.setFont(new java.awt.Font("CASTELLAR", 1, 20));
            registrarInfraccion.addMouseListener(new java.awt.event.MouseAdapter() {

                public void mouseClicked(java.awt.event.MouseEvent e) {
                    if (Objetos.dniIntroducido) {
                        int seleccion = JOptionPane.showOptionDialog(introducirDni, "�Est� seguro que quiere confirmar?", null, JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, new Object[] { "Confirmar", "Eliminar" }, null);
                        if (seleccion == 1) {
                            JOptionPane.showMessageDialog(null, "Registro ANULADO.");
                            dispose();
                            ObtenerTiposDeInfraccionFrame ccframe = new ObtenerTiposDeInfraccionFrame();
                            ccframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                            ccframe.setVisible(true);
                        } else {
                            int codigo = 0;
                            try {
                                codigo = Cliente.objetoLocal.registrarInfraccion(Objetos.dniFinal, String.valueOf(ObtenerTiposDeInfraccionFrame.idFinal), RegistrarInfraccionFrame.lugarFinal, RegistrarInfraccionFrame.coordenadasFinal);
                            } catch (RemoteException e1) {
                                e1.printStackTrace();
                            }
                            if (codigo == 0) {
                                Objetos.dniIntroducido = false;
                                JOptionPane.showMessageDialog(null, "Registro realizado Satisfactoriamente.");
                            } else {
                                JOptionPane.showMessageDialog(null, "Registro no realizado. Intentelo de nuevo.");
                            }
                        }
                    } else {
                        JOptionPane.showMessageDialog(null, "Introduzca primero el DNI del infractor. Selecciona conductor o Introduzca DNI");
                    }
                }
            });
        }
        return registrarInfraccion;
    }

    private void introducirDNI() {
        String ident = JOptionPane.showInputDialog("Introduce DNI. Ej: 44567123");
        if (ident != null) {
            if (ident.length() != 8) {
                JOptionPane.showMessageDialog(null, "El DNI tiene que tener 8 caracteres");
                introducirDNI();
            } else if (!IdentificarUsuarioFrame.isNumeric(ident)) {
                JOptionPane.showMessageDialog(null, "El usuario debe ser un DNI: XXXXXXXX");
                introducirDNI();
            } else {
                try {
                    Objetos.InfoConductorI = Cliente.objetoLocal.obtenerInformacionConductor(ident);
                } catch (RemoteException e) {
                    System.out.println("Error al introducir dni no v�lido.");
                    e.printStackTrace();
                }
                if (Objetos.InfoConductorI == null) {
                    JOptionPane.showMessageDialog(null, "El usuario NO EXISTE en la base de datos");
                    introducirDNI();
                } else {
                    Objetos.dniFinal = ident;
                    Objetos.dniIntroducido = true;
                    JOptionPane.showMessageDialog(null, "DNI introducido con �xito");
                }
            }
        }
    }

    private JScrollPane getJScrollPane() {
        if (jScrollPane == null) {
            jScrollPane = new JScrollPane();
            jScrollPane.setBounds(new Rectangle(13, 50, 763, 40));
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
        if (jTable == null) {
            jTable = new JTable();
            jTable.setVisible(true);
            jTable.setForeground(Color.black);
            jTable.setBackground(Color.lightGray);
            String[] columnas = { "Matr�cula", "Tipo", "Potencia", "Fabricante", "Modelo", "Color", "Dni Conductor", "Nombre" };
            Object[][] datos = { { " ", " ", " ", " ", " ", " ", " ", " " } };
            if (Objetos.VehiculoYConductorHabitualI == null) {
                try {
                    Objetos.VehiculoYConductorHabitualI = Cliente.objetoLocal.obtenerVehiculoYConductorHabitual(RegistrarInfraccionFrame.matriculaFinal);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
            meterDatos(datos, Objetos.VehiculoYConductorHabitualI);
            ponerDatosTablas(jTable, columnas, datos);
        }
        return jTable;
    }

    public String auxDNI = " ";

    private void meterDatos(Object[][] datos, estructuras.VehiculoYConductorHabitual aux1) {
        int k = 0;
        int i = 0;
        String auxMatricula = RegistrarInfraccionFrame.matriculaFinal;
        datos[i][k] = auxMatricula;
        k++;
        String auxTipo = aux1.TipoVehiculo;
        datos[i][k] = auxTipo;
        k++;
        String auxPotencia = aux1.Potencia;
        datos[i][k] = auxPotencia;
        k++;
        String auxFabricante = aux1.Fabricante;
        datos[i][k] = auxFabricante;
        k++;
        String auxModelo = aux1.Modelo;
        datos[i][k] = auxModelo;
        k++;
        String auxColor = aux1.Color;
        datos[i][k] = auxColor;
        k++;
        auxDNI = aux1.dni;
        datos[i][k] = auxDNI;
        k++;
        String auxNombre = aux1.Nombre;
        datos[i][k] = auxNombre;
    }

    public void ponerDatosTablas(JTable tabla, final String[] nombres, final Object[][] datos) {
        TableModel modeloDatos = new AbstractTableModel() {

            private static final long serialVersionUID = 1L;

            public int getColumnCount() {
                return nombres.length;
            }

            public int getRowCount() {
                return datos.length;
            }

            public Object getValueAt(int fil, int col) {
                return datos[fil][col];
            }

            public String getColumnName(int columnas) {
                return nombres[columnas];
            }

            public boolean isCellEditable(int fil, int col) {
                return false;
            }

            public void setValueAt(Object aValue, int fil, int col) {
                datos[fil][col] = aValue;
            }
        };
        tabla.setModel(modeloDatos);
        tabla.setRowHeight(20);
        tabla.setColumnSelectionAllowed(true);
        tabla.repaint();
    }
}
