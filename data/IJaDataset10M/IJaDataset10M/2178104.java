package presentacion;

import java.awt.Color;
import java.awt.Rectangle;
import java.rmi.RemoteException;
import java.util.Iterator;
import java.util.LinkedList;
import javax.swing.JButton;
import javax.swing.JDesktopPane;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;
import estructuras.Tipo;
import rmi.Cliente;

public class ObtenerTiposDeInfraccionFrame extends JFrame {

    private static final long serialVersionUID = 1L;

    private JDesktopPane jDesktopPane = null;

    private JButton salir = null;

    private JButton seleccionarInfraccion = null;

    private JScrollPane jScrollPane = null;

    private JTable jTable = null;

    private JLabel jLabel = null;

    public static int idFinal;

    public String[] tipos;

    /**
	 * This is the default constructor
	 */
    public ObtenerTiposDeInfraccionFrame() {
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
        this.setLocation(160, 100);
        this.setContentPane(getJDesktopPane());
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
            jLabel.setBounds(new Rectangle(41, 11, 276, 30));
            jLabel.setText("Tipos Infracciones Activas: ");
            jLabel.setFont(new java.awt.Font("Cambria", 1, 20));
            jLabel.setForeground(Color.red);
            jDesktopPane = new JDesktopPane();
            jDesktopPane.add(getSalir(), null);
            jDesktopPane.add(getJScrollPane(), null);
            jDesktopPane.add(jLabel, null);
            jDesktopPane.add(getSeleccionarInfraccion(), null);
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
            salir.setBounds(new Rectangle(483, 492, 232, 48));
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
	 * This method initializes SeleccionarInfraccion	
	 * 	
	 * @return javax.swing.JButton	
	 */
    private JButton getSeleccionarInfraccion() {
        if (seleccionarInfraccion == null) {
            seleccionarInfraccion = new JButton();
            seleccionarInfraccion.setText("SELECCIONAR INFRACCION");
            seleccionarInfraccion.setBounds(new Rectangle(20, 493, 409, 48));
            seleccionarInfraccion.setFont(new java.awt.Font("CASTELLAR", 1, 20));
            seleccionarInfraccion.addMouseListener(new java.awt.event.MouseAdapter() {

                public void mouseClicked(java.awt.event.MouseEvent e) {
                    boolean identvalido1 = false;
                    String ident = " ";
                    while (!identvalido1) {
                        ident = JOptionPane.showInputDialog("Introduce identificador. Ej: 1");
                        if (IdentificarUsuarioFrame.isNumeric(ident) && Integer.parseInt(ident) < 9 && Integer.parseInt(ident) >= 0) {
                            int g = 0;
                            while (!identvalido1 && g < 30) {
                                if (ident.equals(tipos[g])) {
                                    identvalido1 = true;
                                } else {
                                    g++;
                                }
                            }
                            if (identvalido1) {
                            } else {
                                JOptionPane.showMessageDialog(null, "Identificador no v�lido. Inserte de nuevo.");
                            }
                        } else {
                            JOptionPane.showMessageDialog(null, "Identificador no v�lido. Inserte de nuevo.");
                        }
                    }
                    idFinal = Integer.parseInt(ident);
                    dispose();
                    RegistrarInfraccionFrame iiiframe = new RegistrarInfraccionFrame();
                    iiiframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                    iiiframe.setVisible(true);
                }
            });
        }
        return seleccionarInfraccion;
    }

    private JScrollPane getJScrollPane() {
        if (jScrollPane == null) {
            jScrollPane = new JScrollPane();
            jScrollPane.setBounds(new Rectangle(13, 50, 763, 417));
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
            String[] columnas = { "Identificador", "Descripci�n", "Gravedad", "Multa", "Puntos Negativos", "D�as Vigencias" };
            Object[][] datos = { { " ", " ", " ", " ", " ", " " }, { " ", " ", " ", " ", " ", " " }, { " ", " ", " ", " ", " ", " " }, { " ", " ", " ", " ", " ", " " }, { " ", " ", " ", " ", " ", " " }, { " ", " ", " ", " ", " ", " " }, { " ", " ", " ", " ", " ", " " }, { " ", " ", " ", " ", " ", " " }, { " ", " ", " ", " ", " ", " " }, { " ", " ", " ", " ", " ", " " }, { " ", " ", " ", " ", " ", " " }, { " ", " ", " ", " ", " ", " " }, { " ", " ", " ", " ", " ", " " }, { " ", " ", " ", " ", " ", " " }, { " ", " ", " ", " ", " ", " " }, { " ", " ", " ", " ", " ", " " }, { " ", " ", " ", " ", " ", " " }, { " ", " ", " ", " ", " ", " " }, { " ", " ", " ", " ", " ", " " }, { " ", " ", " ", " ", " ", " " }, { " ", " ", " ", " ", " ", " " }, { " ", " ", " ", " ", " ", " " } };
            if (Objetos.listaTiposInfraccion == null) {
                try {
                    Objetos.listaTiposInfraccion = Cliente.objetoLocal.obtenerTiposInfraccion();
                } catch (RemoteException e) {
                    System.out.println("Error en cliente.objetoLocal.obtenerTiposInfraccion()");
                    e.printStackTrace();
                }
            }
            meterDatos(datos, Objetos.listaTiposInfraccion);
            ponerDatosTablas(jTable, columnas, datos);
        }
        return jTable;
    }

    private void meterDatos(Object[][] datos, LinkedList<Tipo> tipoInfracciones) {
        Iterator<Tipo> iterador = tipoInfracciones.iterator();
        int i = 0;
        tipos = new String[30];
        Tipo aux = new Tipo();
        while (iterador.hasNext()) {
            int k = 0;
            aux = (Tipo) iterador.next();
            String auxTipo = aux.idTipo;
            datos[i][k] = auxTipo;
            tipos[i] = auxTipo;
            k++;
            String auxDescripcion = aux.Descripcion;
            datos[i][k] = auxDescripcion;
            k++;
            String auxGravedad = aux.Gravedad;
            datos[i][k] = auxGravedad;
            k++;
            int auxMulta = aux.Multa;
            datos[i][k] = String.valueOf(auxMulta);
            k++;
            int auxPuntosNeg = aux.PuntosNeg;
            datos[i][k] = String.valueOf(auxPuntosNeg);
            k++;
            int auxDiasVigencia = aux.DiasVigencia;
            datos[i][k] = String.valueOf(auxDiasVigencia);
            i++;
        }
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
