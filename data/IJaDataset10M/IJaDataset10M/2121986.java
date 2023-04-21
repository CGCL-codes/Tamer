package edu.ufasta.aplicativo.vista;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.RowFilter;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import java.awt.Rectangle;
import java.awt.Font;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.Dimension;
import javax.swing.JButton;
import java.awt.Color;
import javax.swing.BorderFactory;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import java.awt.Point;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JLabel;
import edu.ufasta.aplicativo.modelo.*;
import edu.ufasta.aplicativo.modelo.ModeloTablaEquipos;
import edu.ufasta.aplicativo.modelo.ModeloTablaWebs;
import edu.ufasta.aplicativo.modelo.ReporteCreador;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.TableRowSorter;
import javax.swing.ImageIcon;
import javax.swing.JToggleButton;

public class WPManagerPrincipal extends JFrame {

    private static final long serialVersionUID = 1L;

    private JMenuBar jJMenuBar = null;

    private JTabbedPane jTabbedPane = null;

    private JPanel panelEquipos = null;

    private JPanel panelRecursosWeb = null;

    private JMenu menuConfig = null;

    private JMenuItem itemPropiedades = null;

    private JMenu jMenuArchivo = null;

    private JMenuItem itemLogout = null;

    private JMenuItem itemSalir = null;

    private JPanel jPanel = null;

    private JPanel jPanel1 = null;

    private JButton jBotonHab = null;

    private JButton jBotonInhab = null;

    private JButton jBotonHabWeb = null;

    private JScrollPane jScrollPane = null;

    private JTable jTablaEquipos = null;

    private JButton jBotonModif = null;

    private JButton jBotonInhabWeb = null;

    private JButton jBotonModifWeb = null;

    private JLabel jLabel = null;

    private JLabel jLabel1 = null;

    private JTextField jTxtFiltro1 = null;

    private JLabel jLabel2 = null;

    private JScrollPane jScrollPane1 = null;

    private JTable jTablaWebs = null;

    private JLabel jLabel3 = null;

    private ModeloTablaEquipos equiposModel = new ModeloTablaEquipos();

    ;

    private ModeloTablaWebs websModel = new ModeloTablaWebs();

    private TableRowSorter<ModeloTablaEquipos> ordenador;

    private TableRowSorter<ModeloTablaWebs> ordenador2;

    private JMenu jMenuInformes = null;

    private JMenuItem itemReportesEquipos = null;

    private JMenuItem itemReportesSitios = null;

    private JMenu jMenuAcerca = null;

    private JMenuItem itemVersion = null;

    private JLabel jLabel21 = null;

    private JLabel jLabel4 = null;

    private JTextField jTxtFiltro2 = null;

    private JMenuItem itemLog = null;

    private JPanel panelReportes = null;

    private JLabel iconoReportes = null;

    private JLabel jLabel5 = null;

    private JToggleButton jToggleButton = null;

    private JToggleButton jToggleButton1 = null;

    private JLabel jLabel6 = null;

    private JLabel jLabel7 = null;

    private JLabel jLabel8 = null;

    private JToggleButton jToggleButton2 = null;

    private JLabel jLabel9 = null;

    private JToggleButton jToggleButton3 = null;

    private JLabel jLabel10 = null;

    private JLabel jLabel11 = null;

    private JLabel jLabel12 = null;

    private JLabel jLabel13 = null;

    private JLabel jLabel14 = null;

    private JLabel jLabel15 = null;

    private JLabel jLabel16 = null;

    private JLabel jLabel17 = null;

    /**
	 * Este metodo inicializa el menu principal de la aplicacion	
	 * 
	 * @author Carlos
	 * @return javax.swing.JMenuBar	
	 */
    private JMenuBar getJJMenuBar() {
        if (jJMenuBar == null) {
            jJMenuBar = new JMenuBar();
            jJMenuBar.add(getJMenuArchivo());
            jJMenuBar.add(getJMenuInformes());
            jJMenuBar.add(getMenuConfig());
            jJMenuBar.add(getJMenuAcerca());
        }
        return jJMenuBar;
    }

    /**
	 * Este metodo inicia los tabs visualizados en la ventana principal (Equipos y Sitios)	
	 * 
	 * @author Carlos
	 * @return javax.swing.JTabbedPane	
	 */
    private JTabbedPane getJTabbedPane() {
        if (jTabbedPane == null) {
            jTabbedPane = new JTabbedPane();
            jTabbedPane.setFont(new Font("Arial Narrow", Font.PLAIN, 24));
            jTabbedPane.setForeground(new Color(0, 51, 153));
            jTabbedPane.setBackground(new Color(102, 102, 102));
            jTabbedPane.setOpaque(false);
            jTabbedPane.addTab("   Equipos   ", null, getPanelEquipos(), null);
            jTabbedPane.addTab("   Recursos Web   ", null, getPanelRecursosWeb(), null);
            jTabbedPane.addTab("    Informes    ", null, getPanelReportes(), null);
        }
        return jTabbedPane;
    }

    /**
	 * Este metodo inicializa el Panel de Equipos	
	 * 	
	 * @author Carlos
	 * @return javax.swing.JPanel	
	 */
    private JPanel getPanelEquipos() {
        if (panelEquipos == null) {
            jLabel2 = new JLabel();
            jLabel2.setBounds(new Rectangle(135, 15, 511, 61));
            jLabel2.setText("Esta seccion le permite visualizar los datos de los equipos habilitados por el Web Proxy.");
            jLabel1 = new JLabel();
            jLabel1.setBounds(new Rectangle(135, 105, 61, 16));
            jLabel1.setText("Nombre:");
            jLabel = new JLabel();
            jLabel.setLocation(new Point(15, 15));
            jLabel.setIcon(new ImageIcon(getClass().getResource("/media/iconos/IconoEquipoBig.png")));
            jLabel.setSize(new Dimension(112, 112));
            panelEquipos = new JPanel();
            panelEquipos.setLayout(null);
            panelEquipos.add(getJPanel(), null);
            panelEquipos.add(getJScrollPane(), null);
            panelEquipos.add(jLabel, null);
            panelEquipos.add(jLabel1, null);
            panelEquipos.add(getJTxtFiltro1(), null);
            panelEquipos.add(jLabel2, null);
        }
        return panelEquipos;
    }

    /**
	 * Este metodo inicializa el Panel de Sitios Web	
	 * 	
	 * @author Carlos
	 * @return javax.swing.JPanel	
	 */
    private JPanel getPanelRecursosWeb() {
        if (panelRecursosWeb == null) {
            jLabel4 = new JLabel();
            jLabel4.setBounds(new Rectangle(135, 105, 74, 16));
            jLabel4.setText("Sitio Web:");
            jLabel21 = new JLabel();
            jLabel21.setBounds(new Rectangle(135, 15, 511, 61));
            jLabel21.setText("<html>Esta seccion le permite visualizar los datos de los sitios web habilitados por el Web Proxy.Utilice la grilla para seleccionar el sitio web con el que desea trabajar y luego pulse en alguno de los botones del panel lateral para realizar alguna accion<html>");
            jLabel3 = new JLabel();
            jLabel3.setText("");
            jLabel3.setSize(new Dimension(112, 112));
            jLabel3.setIcon(new ImageIcon(getClass().getResource("/media/iconos/IconoSitioBig.png")));
            jLabel3.setLocation(new Point(15, 15));
            panelRecursosWeb = new JPanel();
            panelRecursosWeb.setLayout(null);
            panelRecursosWeb.add(getJPanel1(), null);
            panelRecursosWeb.add(getJScrollPane1(), null);
            panelRecursosWeb.add(jLabel3, null);
            panelRecursosWeb.add(jLabel21, null);
            panelRecursosWeb.add(jLabel4, null);
            panelRecursosWeb.add(getJTxtFiltro2(), null);
        }
        return panelRecursosWeb;
    }

    /**
	 * Este metodo inicializa la seccion Archivo de la barra de menu principal jMenuBar
	 * 	
	 * @return javax.swing.JMenu	
	 */
    private JMenu getJMenuArchivo() {
        if (jMenuArchivo == null) {
            jMenuArchivo = new JMenu();
            jMenuArchivo.setText("Archivo");
            jMenuArchivo.setFont(new Font("Dialog", Font.BOLD, 14));
            jMenuArchivo.setMargin(new Insets(4, 4, 4, 4));
            jMenuArchivo.add(getItemLogout());
            jMenuArchivo.add(getItemLog());
            jMenuArchivo.add(getItemSalir());
        }
        return jMenuArchivo;
    }

    /**
	 * Este metodo inicializa la seccion Informes de la barra de menu principal jMenuBar	
	 * 	
	 * @return javax.swing.JMenu	
	 */
    private JMenu getJMenuInformes() {
        if (jMenuInformes == null) {
            jMenuInformes = new JMenu();
            jMenuInformes.setText("Informes");
            jMenuInformes.setFont(new Font("Dialog", Font.BOLD, 14));
            jMenuInformes.add(getItemReportesEquipos());
            jMenuInformes.add(getItemReportesSitios());
        }
        return jMenuInformes;
    }

    /**
	 * Este metodo inicializa la seccion Configuracion de la barra de menu principal jMenuBar
	 * 	
	 * @author Carlos
	 * @return javax.swing.JMenu	
	 */
    private JMenu getMenuConfig() {
        if (menuConfig == null) {
            menuConfig = new JMenu();
            menuConfig.setName("");
            menuConfig.setText("Configuraci�n");
            menuConfig.setFont(new Font("Dialog", Font.BOLD, 14));
            menuConfig.add(getItemPropiedades());
        }
        return menuConfig;
    }

    /**
	 * This method initializes jMenuAcerca	
	 * 	
	 * @return javax.swing.JMenu	
	 */
    private JMenu getJMenuAcerca() {
        if (jMenuAcerca == null) {
            jMenuAcerca = new JMenu();
            jMenuAcerca.setText("Acerca de");
            jMenuAcerca.setFont(new Font("Dialog", Font.BOLD, 14));
            jMenuAcerca.add(getItemVersion());
        }
        return jMenuAcerca;
    }

    /**
	 * This method initializes itemPropiedades	
	 * 	
	 * @return javax.swing.JMenuItem	
	 */
    private JMenuItem getItemPropiedades() {
        if (itemPropiedades == null) {
            itemPropiedades = new JMenuItem();
            itemPropiedades.setText("Propiedades Web Proxy...");
            itemPropiedades.setMargin(new Insets(4, 4, 4, 4));
            itemPropiedades.setFont(new Font("Dialog", Font.PLAIN, 14));
            itemPropiedades.addActionListener(new java.awt.event.ActionListener() {

                public void actionPerformed(java.awt.event.ActionEvent e) {
                    VentanaConfiguracion dialogoConfig = new VentanaConfiguracion(WPManagerPrincipal.this);
                    dialogoConfig.setVisible(true);
                    dialogoConfig.prepararCampos();
                    System.out.println("actionPerformed()");
                }
            });
        }
        return itemPropiedades;
    }

    /**
	 * This method initializes itemLogout	
	 * 	
	 * @return javax.swing.JMenuItem	
	 */
    private JMenuItem getItemLogout() {
        if (itemLogout == null) {
            itemLogout = new JMenuItem();
            itemLogout.setText("LogOut");
            itemLogout.setMargin(new Insets(4, 4, 4, 4));
            itemLogout.setFont(new Font("Dialog", Font.PLAIN, 14));
        }
        return itemLogout;
    }

    /**
	 * This method initializes itemSalir	
	 * 	
	 * @return javax.swing.JMenuItem	
	 */
    private JMenuItem getItemSalir() {
        if (itemSalir == null) {
            itemSalir = new JMenuItem();
            itemSalir.setText("Salir");
            itemSalir.setMargin(new Insets(4, 4, 4, 4));
            itemSalir.setFont(new Font("Dialog", Font.PLAIN, 14));
            itemSalir.addActionListener(new java.awt.event.ActionListener() {

                public void actionPerformed(java.awt.event.ActionEvent e) {
                    WPManagerPrincipal.this.dispose();
                    System.out.println("actionPerformed()");
                }
            });
        }
        return itemSalir;
    }

    /**
	 * This method initializes jPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
    private JPanel getJPanel() {
        if (jPanel == null) {
            jPanel = new JPanel();
            jPanel.setLayout(null);
            jPanel.setBounds(new Rectangle(662, 1, 121, 494));
            jPanel.setBorder(BorderFactory.createLineBorder(new Color(0, 0, 102), 5));
            jPanel.setBackground(new Color(204, 204, 204));
            jPanel.add(getJBotonHab(), null);
            jPanel.add(getJBotonInhab(), null);
            jPanel.add(getJBotonModif(), null);
        }
        return jPanel;
    }

    /**
	 * This method initializes jPanel1	
	 * 	
	 * @return javax.swing.JPanel	
	 */
    private JPanel getJPanel1() {
        if (jPanel1 == null) {
            jPanel1 = new JPanel();
            jPanel1.setLayout(null);
            jPanel1.setBounds(new Rectangle(662, 1, 121, 494));
            jPanel1.setBackground(new Color(204, 204, 204));
            jPanel1.setBorder(BorderFactory.createLineBorder(new Color(0, 0, 102), 5));
            jPanel1.add(getJBotonHabWeb(), null);
            jPanel1.add(getJBotonInhabWeb(), null);
            jPanel1.add(getJBotonModifWeb(), null);
        }
        return jPanel1;
    }

    /**
	 * This method initializes jBotonHab	
	 * 	
	 * @return javax.swing.JButton	
	 */
    private JButton getJBotonHab() {
        if (jBotonHab == null) {
            jBotonHab = new JButton();
            jBotonHab.setBounds(new Rectangle(13, 17, 95, 43));
            jBotonHab.setText("<html><center>Habilitar<br>Equipo</center></html>");
            jBotonHab.addActionListener(new java.awt.event.ActionListener() {

                public void actionPerformed(java.awt.event.ActionEvent e) {
                    VentanaDialogoEquipo dialogHabEquipo = new VentanaDialogoEquipo(WPManagerPrincipal.this, "", "", "", "", 0, 1);
                    ;
                    dialogHabEquipo.setModelE(equiposModel);
                    dialogHabEquipo.setVisible(true);
                }
            });
        }
        return jBotonHab;
    }

    /**
	 * This method initializes jBotonInhab	
	 * 	
	 * @return javax.swing.JButton	
	 */
    private JButton getJBotonInhab() {
        if (jBotonInhab == null) {
            jBotonInhab = new JButton();
            jBotonInhab.setPreferredSize(new Dimension(81, 42));
            jBotonInhab.setLocation(new Point(13, 76));
            jBotonInhab.setSize(new Dimension(95, 43));
            jBotonInhab.setText("<html><center>Deshabilitar<br>Equipo</center></html>");
            jBotonInhab.addActionListener(new java.awt.event.ActionListener() {

                public void actionPerformed(java.awt.event.ActionEvent e) {
                    Mensaje mensaje = new Mensaje();
                    mensaje.setTitulo("Deshabilitar Equipo");
                    mensaje.setCuerpo("�Confirma la deshabilitaci�n del equipo seleccionado?");
                    mensaje.setTipo(JOptionPane.QUESTION_MESSAGE);
                    mensaje.setBotones(JOptionPane.YES_NO_CANCEL_OPTION);
                    int opcion = mensaje.mostrarMensaje(jBotonInhab);
                    if (opcion == 0) {
                        try {
                            AdministraEquipo adminE = new AdministraEquipo();
                            String auxi = (String) jTablaEquipos.getValueAt(jTablaEquipos.getSelectedRow(), 0);
                            adminE.setIDEquipo(Integer.parseInt(auxi));
                            adminE.setDirIP((String) jTablaEquipos.getValueAt(jTablaEquipos.getSelectedRow(), 1));
                            adminE.setNombre((String) jTablaEquipos.getValueAt(jTablaEquipos.getSelectedRow(), 2));
                            adminE.setDescripcion((String) jTablaEquipos.getValueAt(jTablaEquipos.getSelectedRow(), 3));
                            adminE.setUltConexion((String) jTablaEquipos.getValueAt(jTablaEquipos.getSelectedRow(), 4));
                            adminE.borrar();
                            equiposModel.updateOrigen();
                            equiposModel.fireTableDataChanged();
                            jTablaEquipos.repaint();
                        } catch (Exception ex) {
                            mensaje.setTitulo(".: Web Proxy Manager :.");
                            mensaje.setCuerpo("Hubo un error en la conexi�n mySQL con la base de datos.\nLa operaci�n no pudo ser efectuada");
                            mensaje.setTipo(JOptionPane.ERROR_MESSAGE);
                            mensaje.setBotones(JOptionPane.DEFAULT_OPTION);
                            mensaje.mostrarMensaje(jBotonInhab);
                        }
                    }
                }
            });
        }
        return jBotonInhab;
    }

    /**
	 * This method initializes jBotonHabWeb	
	 * 	
	 * @return javax.swing.JButton	
	 */
    private JButton getJBotonHabWeb() {
        if (jBotonHabWeb == null) {
            jBotonHabWeb = new JButton();
            jBotonHabWeb.setBounds(new Rectangle(13, 17, 95, 43));
            jBotonHabWeb.setText("<html><center>Registrar<br>Sitio Web</center></html>");
            jBotonHabWeb.addActionListener(new java.awt.event.ActionListener() {

                public void actionPerformed(java.awt.event.ActionEvent e) {
                    VentanaDialogoSitioWeb dialogRegSitio = new VentanaDialogoSitioWeb(WPManagerPrincipal.this, 0, "", "", 1);
                    dialogRegSitio.setModeloWeb(websModel);
                    dialogRegSitio.setVisible(true);
                }
            });
        }
        return jBotonHabWeb;
    }

    /**
	 * This method initializes jScrollPane	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */
    private JScrollPane getJScrollPane() {
        if (jScrollPane == null) {
            jScrollPane = new JScrollPane();
            jScrollPane.setBounds(new Rectangle(15, 135, 631, 346));
            jScrollPane.setOpaque(false);
            jScrollPane.setViewportView(getJTablaEquipos());
        }
        return jScrollPane;
    }

    /**
	 * This method initializes jTablaEquipos	
	 * 	
	 * @return javax.swing.JTable	
	 */
    private JTable getJTablaEquipos() {
        if (jTablaEquipos == null) {
            jTablaEquipos = new JTable(equiposModel);
            ordenador = new TableRowSorter<ModeloTablaEquipos>(equiposModel);
            jTablaEquipos.setRowSorter(ordenador);
            jTablaEquipos.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        }
        return jTablaEquipos;
    }

    /**
	 * This method initializes jBotonModif	
	 * 	
	 * @return javax.swing.JButton	
	 */
    private JButton getJBotonModif() {
        if (jBotonModif == null) {
            jBotonModif = new JButton();
            jBotonModif.setBounds(new Rectangle(13, 137, 95, 43));
            jBotonModif.setText("<html><center>Modificar<br>Equipo</center></html>");
            jBotonModif.addActionListener(new java.awt.event.ActionListener() {

                public void actionPerformed(java.awt.event.ActionEvent e) {
                    String aux = (String) jTablaEquipos.getValueAt(jTablaEquipos.getSelectedRow(), 0);
                    int myID = Integer.parseInt(aux);
                    String myIP = (String) jTablaEquipos.getValueAt(jTablaEquipos.getSelectedRow(), 1);
                    String myNombre = (String) jTablaEquipos.getValueAt(jTablaEquipos.getSelectedRow(), 2);
                    String myDesc = (String) jTablaEquipos.getValueAt(jTablaEquipos.getSelectedRow(), 3);
                    Date myConn = (Date) jTablaEquipos.getValueAt(jTablaEquipos.getSelectedRow(), 4);
                    SimpleDateFormat ff = new SimpleDateFormat();
                    String auxFecha;
                    if (myConn == null) {
                        auxFecha = "";
                    } else {
                        auxFecha = ff.format(myConn);
                    }
                    VentanaDialogoEquipo modEquipo = new VentanaDialogoEquipo(WPManagerPrincipal.this, myIP, myNombre, myDesc, auxFecha, myID, 2);
                    modEquipo.setModelE(equiposModel);
                    modEquipo.setVisible(true);
                }
            });
        }
        return jBotonModif;
    }

    /**
	 * This method initializes jBotonInhabWeb	
	 * 	
	 * @return javax.swing.JButton	
	 */
    private JButton getJBotonInhabWeb() {
        if (jBotonInhabWeb == null) {
            jBotonInhabWeb = new JButton();
            jBotonInhabWeb.setBounds(new Rectangle(13, 76, 95, 43));
            jBotonInhabWeb.setText("<html><center>Eliminar<br>Sitio Web</center></html>");
            jBotonInhabWeb.addActionListener(new java.awt.event.ActionListener() {

                public void actionPerformed(java.awt.event.ActionEvent e) {
                    Mensaje mensaje = new Mensaje();
                    mensaje.setTitulo("Eliminar Sitio Web");
                    mensaje.setCuerpo("�Desea eliminar el sitio web seleccionado?");
                    mensaje.setTipo(JOptionPane.QUESTION_MESSAGE);
                    mensaje.setBotones(JOptionPane.YES_NO_CANCEL_OPTION);
                    int opcion = mensaje.mostrarMensaje(jBotonInhabWeb);
                    if (opcion == 0) {
                        try {
                            AdministraSitioWeb adminW = new AdministraSitioWeb();
                            int auxID = Integer.parseInt((String) jTablaWebs.getValueAt(jTablaWebs.getSelectedRow(), 0));
                            adminW.setIDSitio(auxID);
                            adminW.setURLSitio((String) jTablaWebs.getValueAt(jTablaWebs.getSelectedRow(), 1));
                            adminW.setDescripcion((String) jTablaWebs.getValueAt(jTablaWebs.getSelectedRow(), 2));
                            adminW.borrar();
                            websModel.updateOrigen();
                            websModel.fireTableDataChanged();
                            jTablaWebs.repaint();
                        } catch (Exception ex) {
                            mensaje.setTitulo(".: Web Proxy Manager :.");
                            mensaje.setCuerpo("Hubo un error en la conexi�n mySQL con la base de datos.\nLa operaci�n no pudo ser efectuada");
                            mensaje.setTipo(JOptionPane.ERROR_MESSAGE);
                            mensaje.setBotones(JOptionPane.DEFAULT_OPTION);
                            mensaje.mostrarMensaje(jBotonInhabWeb);
                        }
                    }
                }
            });
        }
        return jBotonInhabWeb;
    }

    /**
	 * This method initializes jBotonModifWeb	
	 * 	
	 * @return javax.swing.JButton	
	 */
    private JButton getJBotonModifWeb() {
        if (jBotonModifWeb == null) {
            jBotonModifWeb = new JButton();
            jBotonModifWeb.setBounds(new Rectangle(13, 137, 95, 43));
            jBotonModifWeb.setText("<html><center>Modificar<br>Sitio Web</center></html>");
            jBotonModifWeb.addActionListener(new java.awt.event.ActionListener() {

                public void actionPerformed(java.awt.event.ActionEvent e) {
                    int IDSitioweb = Integer.parseInt((String) jTablaWebs.getValueAt(jTablaWebs.getSelectedRow(), 0));
                    String URLSitio = (String) jTablaWebs.getValueAt(jTablaWebs.getSelectedRow(), 1);
                    String descripcionSitio = (String) jTablaWebs.getValueAt(jTablaWebs.getSelectedRow(), 2);
                    VentanaDialogoSitioWeb modSitio = new VentanaDialogoSitioWeb(WPManagerPrincipal.this, IDSitioweb, URLSitio, descripcionSitio, 2);
                    modSitio.setModeloWeb(websModel);
                    modSitio.setVisible(true);
                }
            });
        }
        return jBotonModifWeb;
    }

    /**
	 * This method initializes jTxtFiltro1	
	 * 	
	 * @return javax.swing.JTextField	
	 */
    private JTextField getJTxtFiltro1() {
        if (jTxtFiltro1 == null) {
            jTxtFiltro1 = new JTextField();
            jTxtFiltro1.setBounds(new Rectangle(210, 105, 196, 20));
            jTxtFiltro1.getDocument().addDocumentListener(new DocumentListener() {

                public void changedUpdate(DocumentEvent e) {
                    nuevoFiltro(1);
                }

                public void insertUpdate(DocumentEvent e) {
                    nuevoFiltro(1);
                }

                public void removeUpdate(DocumentEvent e) {
                    nuevoFiltro(1);
                }
            });
        }
        return jTxtFiltro1;
    }

    /**
	 * This method initializes jScrollPane1	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */
    private JScrollPane getJScrollPane1() {
        if (jScrollPane1 == null) {
            jScrollPane1 = new JScrollPane();
            jScrollPane1.setBounds(new Rectangle(15, 135, 631, 346));
            jScrollPane1.setOpaque(false);
            jScrollPane1.setViewportView(getJTablaWebs());
        }
        return jScrollPane1;
    }

    /**
	 * This method initializes jTablaWebs	
	 * 	
	 * @return javax.swing.JTable	
	 */
    private JTable getJTablaWebs() {
        if (jTablaWebs == null) {
            jTablaWebs = new JTable(websModel);
            ordenador2 = new TableRowSorter<ModeloTablaWebs>(websModel);
            jTablaWebs.setRowSorter(ordenador2);
            jTablaWebs.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
            jTablaWebs.setOpaque(false);
        }
        return jTablaWebs;
    }

    /**
	 * This method initializes itemReportesEquipos	
	 * 	
	 * @return javax.swing.JMenuItem	
	 */
    private JMenuItem getItemReportesEquipos() {
        if (itemReportesEquipos == null) {
            itemReportesEquipos = new JMenuItem();
            itemReportesEquipos.setText("Reportes de Equipos");
            itemReportesEquipos.setMargin(new Insets(4, 4, 4, 4));
            itemReportesEquipos.setFont(new Font("Dialog", Font.PLAIN, 14));
            itemReportesEquipos.addActionListener(new java.awt.event.ActionListener() {

                public void actionPerformed(java.awt.event.ActionEvent e) {
                }
            });
        }
        return itemReportesEquipos;
    }

    /**
	 * This method initializes itemReportesSitios	
	 * 	
	 * @return javax.swing.JMenuItem	
	 */
    private JMenuItem getItemReportesSitios() {
        if (itemReportesSitios == null) {
            itemReportesSitios = new JMenuItem();
            itemReportesSitios.setText("Reportes de Sitios Webs");
            itemReportesSitios.setMargin(new Insets(4, 4, 4, 4));
            itemReportesSitios.setFont(new Font("Dialog", Font.PLAIN, 14));
        }
        return itemReportesSitios;
    }

    /**
	 * This method initializes itemVersion	
	 * 	
	 * @return javax.swing.JMenuItem	
	 */
    private JMenuItem getItemVersion() {
        if (itemVersion == null) {
            itemVersion = new JMenuItem();
            itemVersion.setText("Version Web Proxy");
            itemVersion.setMargin(new Insets(4, 4, 4, 4));
            itemVersion.setFont(new Font("Dialog", Font.PLAIN, 14));
            itemVersion.addActionListener(new java.awt.event.ActionListener() {

                public void actionPerformed(java.awt.event.ActionEvent e) {
                    Mensaje mensaje = new Mensaje();
                    mensaje.setTitulo("Web Proxy FASTA");
                    mensaje.setCuerpo("Web Proxy FASTA version 1.0.0\nCreado por Juan Pablo Ruiz y Carlos Daniel Gaspar");
                    mensaje.setTipo(JOptionPane.INFORMATION_MESSAGE);
                    mensaje.setBotones(JOptionPane.DEFAULT_OPTION);
                    mensaje.mostrarMensaje(itemVersion);
                }
            });
        }
        return itemVersion;
    }

    /**
	 * This method initializes jTxtFiltro2	
	 * 	
	 * @return javax.swing.JTextField	
	 */
    private JTextField getJTxtFiltro2() {
        if (jTxtFiltro2 == null) {
            jTxtFiltro2 = new JTextField();
            jTxtFiltro2.setBounds(new Rectangle(210, 105, 349, 20));
            jTxtFiltro2.setText("http://");
            jTxtFiltro2.getDocument().addDocumentListener(new DocumentListener() {

                public void changedUpdate(DocumentEvent e) {
                    nuevoFiltro(2);
                }

                public void insertUpdate(DocumentEvent e) {
                    nuevoFiltro(2);
                }

                public void removeUpdate(DocumentEvent e) {
                    nuevoFiltro(2);
                }
            });
        }
        return jTxtFiltro2;
    }

    /**
	 * This method initializes itemLog	
	 * 	
	 * @return javax.swing.JMenuItem	
	 */
    private JMenuItem getItemLog() {
        if (itemLog == null) {
            itemLog = new JMenuItem();
            itemLog.setText("Ver Log Web Proxy");
            itemLog.setMargin(new Insets(4, 4, 4, 4));
            itemLog.setFont(new Font("Dialog", Font.PLAIN, 14));
            itemLog.addActionListener(new java.awt.event.ActionListener() {

                public void actionPerformed(java.awt.event.ActionEvent e) {
                    VentanaLog log = new VentanaLog(WPManagerPrincipal.this);
                    log.setVisible(true);
                }
            });
        }
        return itemLog;
    }

    /**
	 * This method initializes panelReportes	
	 * 	
	 * @return javax.swing.JPanel	
	 */
    private JPanel getPanelReportes() {
        if (panelReportes == null) {
            jLabel17 = new JLabel();
            jLabel17.setText("<html>Este informe muestra un listado con todos los equipos registrados en el sistema, y para cada uno de ellos un sublistado con los sitios a los que se accedio desde el mismo.</html>");
            jLabel17.setSize(new Dimension(676, 26));
            jLabel17.setLocation(new Point(90, 444));
            jLabel16 = new JLabel();
            jLabel16.setText("<html>Este informe muestra un listado con todos los Sitios Web registrados en el sistema, y para cada uno de ellos un sublistado con los equipos que visitaron el sitio en las ultimas 24 horas</html>");
            jLabel16.setSize(new Dimension(676, 26));
            jLabel16.setLocation(new Point(90, 354));
            jLabel15 = new JLabel();
            jLabel15.setText("<html>Este informe muestra un ranking de los 10 sitios mas solicitados en las utlimas 24 horas a traves de la red. Se muestra la cantidad de accesos realizada a las p�ginas que forman parte del sitio</html>");
            jLabel15.setSize(new Dimension(676, 30));
            jLabel15.setLocation(new Point(90, 264));
            jLabel14 = new JLabel();
            jLabel14.setBounds(new Rectangle(90, 420, 256, 16));
            jLabel14.setText("Detalle de Equipos registrados");
            jLabel13 = new JLabel();
            jLabel13.setBounds(new Rectangle(90, 330, 256, 16));
            jLabel13.setText("Detalle de Sitios Web registrados");
            jLabel12 = new JLabel();
            jLabel12.setBounds(new Rectangle(90, 240, 256, 16));
            jLabel12.setText("Sitios Web m�s visitados");
            jLabel11 = new JLabel();
            jLabel11.setText("");
            jLabel11.setSize(new Dimension(51, 51));
            jLabel11.setBorder(BorderFactory.createLineBorder(Color.black, 3));
            jLabel11.setIcon(new ImageIcon(getClass().getResource("/media/iconos/reportThumb.gif")));
            jLabel11.setLocation(new Point(30, 420));
            jLabel10 = new JLabel();
            jLabel10.setText("");
            jLabel10.setSize(new Dimension(51, 51));
            jLabel10.setBorder(BorderFactory.createLineBorder(Color.black, 3));
            jLabel10.setIcon(new ImageIcon(getClass().getResource("/media/iconos/reportThumb.gif")));
            jLabel10.setLocation(new Point(30, 330));
            jLabel9 = new JLabel();
            jLabel9.setBounds(new Rectangle(30, 148, 51, 51));
            jLabel9.setIcon(new ImageIcon(getClass().getResource("/media/iconos/reportThumb.gif")));
            jLabel9.setBorder(BorderFactory.createLineBorder(Color.black, 3));
            jLabel9.setIconTextGap(0);
            jLabel9.setText("");
            jLabel8 = new JLabel();
            jLabel8.setText("");
            jLabel8.setSize(new Dimension(51, 51));
            jLabel8.setBorder(BorderFactory.createLineBorder(Color.black, 3));
            jLabel8.setIcon(new ImageIcon(getClass().getResource("/media/iconos/reportThumb.gif")));
            jLabel8.setLocation(new Point(30, 240));
            jLabel7 = new JLabel();
            jLabel7.setText("<html>Este informe genera un listado con los equipos registrados en el sistema. Para cada uno de ellos se muestra la cantidad de bytes transferidos en las utlimas 24 horas<html>");
            jLabel7.setSize(new Dimension(676, 30));
            jLabel7.setLocation(new Point(90, 171));
            jLabel6 = new JLabel();
            jLabel6.setBounds(new Rectangle(90, 150, 256, 16));
            jLabel6.setText("Informe de Acceso de Equipos");
            jLabel5 = new JLabel();
            jLabel5.setBounds(new Rectangle(135, 15, 646, 61));
            jLabel5.setText("<html>En esta seccion usted puede obtener reportes que reflejan el comportamiento y uso del web proxy. Haga click en cualquiera de los de los botones para generar un reporte y visualizar su vista previa antes de imprimirlo.<hmtl>");
            iconoReportes = new JLabel();
            iconoReportes.setText("");
            iconoReportes.setSize(new Dimension(112, 112));
            iconoReportes.setIcon(new ImageIcon(getClass().getResource("/media/iconos/IconoReportesBig.png")));
            iconoReportes.setLocation(new Point(15, 15));
            panelReportes = new JPanel();
            panelReportes.setLayout(null);
            panelReportes.add(iconoReportes, null);
            panelReportes.add(jLabel5, null);
            panelReportes.add(jLabel6, null);
            panelReportes.add(jLabel12, null);
            panelReportes.add(jLabel13, null);
            panelReportes.add(jLabel14, null);
            panelReportes.add(jLabel7, null);
            panelReportes.add(jLabel15, null);
            panelReportes.add(jLabel16, null);
            panelReportes.add(jLabel17, null);
            panelReportes.add(jLabel9, null);
            panelReportes.add(jLabel8, null);
            panelReportes.add(jLabel10, null);
            panelReportes.add(jLabel11, null);
            panelReportes.add(getJToggleButton(), null);
            panelReportes.add(getJToggleButton1(), null);
            panelReportes.add(getJToggleButton2(), null);
            panelReportes.add(getJToggleButton3(), null);
        }
        return panelReportes;
    }

    /**
	 * This method initializes jToggleButton	
	 * 	
	 * @return javax.swing.JToggleButton	
	 */
    private JToggleButton getJToggleButton() {
        if (jToggleButton == null) {
            jToggleButton = new JToggleButton();
            jToggleButton.setBounds(new Rectangle(15, 135, 766, 76));
            jToggleButton.setBorderPainted(false);
            jToggleButton.setBorder(null);
            jToggleButton.addActionListener(new java.awt.event.ActionListener() {

                public void actionPerformed(java.awt.event.ActionEvent e) {
                    ReporteCreador reporte = new ReporteCreador();
                    int val = reporte.buildReporte1();
                    if (val < 0) {
                        Mensaje menError = new Mensaje();
                        menError.setTitulo(".: Web Proxy Manager :.");
                        menError.setCuerpo(reporte.getCadenaError());
                        menError.setTipo(JOptionPane.ERROR_MESSAGE);
                        menError.setBotones(JOptionPane.DEFAULT_OPTION);
                        menError.mostrarMensaje(null);
                    }
                    WPManagerPrincipal.this.jToggleButton.setSelected(false);
                }
            });
        }
        return jToggleButton;
    }

    /**
	 * This method initializes jToggleButton1	
	 * 	
	 * @return javax.swing.JToggleButton	
	 */
    private JToggleButton getJToggleButton1() {
        if (jToggleButton1 == null) {
            jToggleButton1 = new JToggleButton();
            jToggleButton1.setBounds(new Rectangle(15, 225, 766, 76));
            jToggleButton1.setBorderPainted(false);
            jToggleButton1.addActionListener(new java.awt.event.ActionListener() {

                public void actionPerformed(java.awt.event.ActionEvent e) {
                    ReporteCreador reporte = new ReporteCreador();
                    int val = reporte.buildReporte2();
                    if (val < 0) {
                        Mensaje menError = new Mensaje();
                        menError.setTitulo(".: Web Proxy Manager :.");
                        menError.setCuerpo(reporte.getCadenaError());
                        menError.setTipo(JOptionPane.ERROR_MESSAGE);
                        menError.setBotones(JOptionPane.DEFAULT_OPTION);
                        menError.mostrarMensaje(null);
                    }
                    WPManagerPrincipal.this.jToggleButton1.setSelected(false);
                }
            });
        }
        return jToggleButton1;
    }

    /**
	 * This method initializes jToggleButton2	
	 * 	
	 * @return javax.swing.JToggleButton	
	 */
    private JToggleButton getJToggleButton2() {
        if (jToggleButton2 == null) {
            jToggleButton2 = new JToggleButton();
            jToggleButton2.setBounds(new Rectangle(15, 315, 766, 76));
            jToggleButton2.setBorderPainted(false);
            jToggleButton2.addActionListener(new java.awt.event.ActionListener() {

                public void actionPerformed(java.awt.event.ActionEvent e) {
                    ReporteCreador reporte = new ReporteCreador();
                    int val = reporte.buildReporte3();
                    if (val < 0) {
                        Mensaje menError = new Mensaje();
                        menError.setTitulo(".: Web Proxy Manager :.");
                        menError.setCuerpo("Hubo un error en la operacion.");
                        menError.setTipo(JOptionPane.ERROR_MESSAGE);
                        menError.setBotones(JOptionPane.DEFAULT_OPTION);
                        menError.mostrarMensaje(null);
                    }
                    WPManagerPrincipal.this.jToggleButton2.setSelected(false);
                }
            });
        }
        return jToggleButton2;
    }

    /**
	 * This method initializes jToggleButton3	
	 * 	
	 * @return javax.swing.JToggleButton	
	 */
    private JToggleButton getJToggleButton3() {
        if (jToggleButton3 == null) {
            jToggleButton3 = new JToggleButton();
            jToggleButton3.setBounds(new Rectangle(15, 405, 766, 76));
            jToggleButton3.setBorderPainted(false);
            jToggleButton3.addActionListener(new java.awt.event.ActionListener() {

                public void actionPerformed(java.awt.event.ActionEvent e) {
                    ReporteCreador reporte = new ReporteCreador();
                    int val = reporte.buildReporte4();
                    if (val < 0) {
                        Mensaje menError = new Mensaje();
                        menError.setTitulo(".: Web Proxy Manager :.");
                        menError.setCuerpo("Hubo un error en la operacion.");
                        menError.setTipo(JOptionPane.ERROR_MESSAGE);
                        menError.setBotones(JOptionPane.DEFAULT_OPTION);
                        menError.mostrarMensaje(null);
                    }
                    WPManagerPrincipal.this.jToggleButton3.setSelected(false);
                }
            });
        }
        return jToggleButton3;
    }

    /**
	 * @param args
	 */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                try {
                    UIManager.setLookAndFeel("org.jvnet.substance.skin.SubstanceBusinessBlackSteelLookAndFeel");
                } catch (UnsupportedLookAndFeelException ulafe) {
                    System.out.println("Substance failed to set");
                } catch (ClassNotFoundException cnfe) {
                    System.out.println("Substance not found");
                } catch (InstantiationException ie) {
                    System.out.println("Substance failed to instantiate");
                } catch (IllegalAccessException iae) {
                    System.out.println("Access denied");
                }
                try {
                    WPManagerPrincipal thisClass = new WPManagerPrincipal();
                    thisClass.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                    thisClass.setVisible(true);
                } catch (Exception e) {
                    Mensaje menError = new Mensaje();
                    menError.setTitulo(".: Web Proxy Manager :.");
                    menError.setCuerpo("Hubo un error en la conexi�n mySQL con la base de datos.\nLa aplicaci�n no puede inicializarse");
                    menError.setTipo(JOptionPane.ERROR_MESSAGE);
                    menError.setBotones(JOptionPane.DEFAULT_OPTION);
                    menError.mostrarMensaje(null);
                    System.exit(0);
                }
            }
        });
    }

    /**
	 * This is the default constructor
	 */
    public WPManagerPrincipal() {
        super();
        initialize();
    }

    /**
	 * This method initializes this
	 * 
	 * @return void
	 */
    private void initialize() {
        this.setContentPane(getJTabbedPane());
        this.setJMenuBar(getJJMenuBar());
        this.setTitle(".: Wep Proxy Manager :.");
        this.setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/media/iconos/mainLogo.png")));
        this.setBounds(new Rectangle(0, 0, 800, 600));
        this.setLocationRelativeTo(null);
    }

    private void nuevoFiltro(int categoria) {
        if (categoria == 1) {
            RowFilter<ModeloTablaEquipos, Object> rf = null;
            try {
                rf = RowFilter.regexFilter("(?i)" + jTxtFiltro1.getText().toLowerCase(), 2);
            } catch (java.util.regex.PatternSyntaxException e) {
                return;
            }
            ordenador.setRowFilter(rf);
        } else {
            RowFilter<ModeloTablaWebs, Object> rf = null;
            try {
                rf = RowFilter.regexFilter("(?i)" + jTxtFiltro2.getText(), 1);
            } catch (java.util.regex.PatternSyntaxException e) {
                return;
            }
            ordenador2.setRowFilter(rf);
        }
    }
}
