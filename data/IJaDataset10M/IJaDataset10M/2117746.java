package GUI;

import java.awt.HeadlessException;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JFrame;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.Toolkit;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import javax.swing.JSplitPane;
import javax.swing.BorderFactory;
import javax.swing.border.TitledBorder;
import javax.swing.border.BevelBorder;
import java.awt.Font;
import java.awt.Color;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EtchedBorder;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableColumn;
import java.awt.Point;
import java.awt.Dimension;
import javax.swing.ListSelectionModel;
import javax.swing.JLabel;
import java.awt.FlowLayout;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JTabbedPane;
import javax.swing.JList;
import modelo.Restaurante;
import modelo.gestionCarta.Producto;
import modelo.gestionCarta.Seccion;
import modelo.gestionPedidos.Consumicion;
import modelo.gestionPedidos.Pedido;
import aplicaciones.Cocina;

/**

 * @author Quique, Ana Bel√©n Pelegrina Ortiz

 *

 */
public class CocinaGrafica extends JFrame implements MouseListener, ChangeListener {

    /**

	 * 

	 */
    private static final long serialVersionUID = 1L;

    private JPanel jContentPane = null;

    private JSplitPane jSPCocina = null;

    private JPanel jPColaPlatos = null;

    private JScrollPane jSPColaPlatos = null;

    private JTable jTColaPlatos = null;

    private JPanel jPLeyenda1 = null;

    private JLabel jLSolicitado = null;

    private JLabel jLEnPreparacion = null;

    private JLabel jLListo = null;

    private JPanel right = null;

    private JPanel jPLeyenda2 = null;

    private JLabel jLDisponible = null;

    private JLabel jLNoDisponible = null;

    private JTabbedPane jTabbedPane = null;

    private JList jLSecciones = null;

    private JScrollPane jSPProductos = null;

    private JTable jTProductos = null;

    private DefaultListModel defaultListModel = null;

    public ImageIcon flagRoja = new ImageIcon(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/iconos/tag_red.png")));

    public ImageIcon flagVerde = new ImageIcon(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/iconos/tag_green.png")));

    public ImageIcon flagAmarillo = new ImageIcon(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/iconos/tag_yellow.png")));

    public ImageIcon disponible = new ImageIcon(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/iconos/thumb_up.png")));

    public ImageIcon no_disponible = new ImageIcon(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/iconos/thumb_down.png")));

    private Seccion seccionActual = null;

    /**

	 * @throws HeadlessException

	 */
    public CocinaGrafica() throws HeadlessException {
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setExtendedState(MAXIMIZED_BOTH);
        initialize();
    }

    /**

	 * This method initialize			

	 * 

	 */
    private void initialize() {
        this.setTitle("Subsistema de gesti√≥n de Cocina");
        this.setMinimumSize(new Dimension(800, 600));
        this.setMaximumSize(new Dimension(800, 600));
        this.setLocation(new Point(50, 50));
        this.setContentPane(getJContentPane());
        this.setVisible(true);
        this.setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/iconos/cake.png")));
        HebraActualizadora ha = new HebraActualizadora(jTColaPlatos);
        ha.start();
    }

    public void mouseClicked(MouseEvent e) {
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }

    public void mousePressed(MouseEvent e) {
    }

    public void mouseReleased(MouseEvent e) {
    }

    public void stateChanged(ChangeEvent e) {
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
            jContentPane.add(getJSPCocina(), BorderLayout.CENTER);
        }
        return jContentPane;
    }

    /**

	 * This method initializes jSPCocina	

	 * 	

	 * @return javax.swing.JSplitPane	

	 */
    private JSplitPane getJSPCocina() {
        if (jSPCocina == null) {
            jSPCocina = new JSplitPane();
            jSPCocina.setDividerSize(10);
            jSPCocina.setLeftComponent(getJPColaPlatos());
            jSPCocina.setRightComponent(getRight());
            jSPCocina.setDividerLocation(400);
        }
        return jSPCocina;
    }

    /**

	 * This method initializes jPColaPlatos	

	 * 	

	 * @return javax.swing.JPanel	

	 */
    private JPanel getJPColaPlatos() {
        if (jPColaPlatos == null) {
            jPColaPlatos = new JPanel();
            jPColaPlatos.setLayout(new BorderLayout());
            jPColaPlatos.setBorder(BorderFactory.createTitledBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED), "COLA DE PLATOS SOLICITADOS:", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.ABOVE_TOP, new Font("Dialog", Font.BOLD, 12), new Color(51, 51, 51)));
            jPColaPlatos.add(getJSPColaPlatos(), BorderLayout.CENTER);
            jPColaPlatos.add(getJPLeyenda1(), BorderLayout.SOUTH);
        }
        return jPColaPlatos;
    }

    /**

	 * This method initializes jSPColaPlatos	

	 * 	

	 * @return javax.swing.JScrollPane	

	 */
    private JScrollPane getJSPColaPlatos() {
        if (jSPColaPlatos == null) {
            jSPColaPlatos = new JScrollPane();
            jSPColaPlatos.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
            jSPColaPlatos.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
            jSPColaPlatos.setViewportBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
            jSPColaPlatos.setViewportView(getJTColaPlatos());
        }
        return jSPColaPlatos;
    }

    /**

	 * This method initializes jTColaPlatos	

	 * 	

	 * @return javax.swing.JTable	

	 */
    private JTable getJTColaPlatos() {
        if (jTColaPlatos == null) {
            MyEstadoTableModel metm = new MyEstadoTableModel();
            jTColaPlatos = new JTable(metm);
            jTColaPlatos.setAutoCreateColumnsFromModel(true);
            jTColaPlatos.setShowGrid(true);
            jTColaPlatos.setRowHeight(45);
            jTColaPlatos.setCellSelectionEnabled(false);
            jTColaPlatos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            jTColaPlatos.setRowSelectionAllowed(true);
            jTColaPlatos.setFont(new Font("Arial", Font.BOLD, 16));
            TableColumn column = null;
            for (int i = 0; i < 3; i++) {
                column = jTColaPlatos.getColumnModel().getColumn(i);
                switch(i) {
                    case 0:
                        column.setPreferredWidth(65);
                        break;
                    case 1:
                        column.setPreferredWidth(230);
                        break;
                    case 2:
                        column.setPreferredWidth(100);
                        break;
                    case 3:
                        column.setPreferredWidth(70);
                        break;
                }
            }
            jTColaPlatos.addMouseListener(new MouseListener() {

                public void mouseClicked(MouseEvent arg0) {
                    int columna = jTColaPlatos.getSelectedRow();
                    java.awt.Image imagenCelda = ((ImageIcon) jTColaPlatos.getValueAt(columna, 3)).getImage();
                    MyEstadoTableModel modelo = (MyEstadoTableModel) jTColaPlatos.getModel();
                    List<Consumicion> consumiciones = modelo.getConsumicones();
                    Consumicion c = null;
                    int id = Integer.parseInt(jTColaPlatos.getValueAt(jTColaPlatos.getSelectedRow(), 0).toString());
                    for (int i = 0; i < consumiciones.size() && c == null; ++i) {
                        if (consumiciones.get(i).getCodConsumicion() == id) {
                            c = consumiciones.get(i);
                        }
                    }
                    if (flagRoja.getImage().equals(imagenCelda)) {
                        jTColaPlatos.setValueAt(flagAmarillo, columna, 3);
                        if (c != null) {
                            c.setEstado(2);
                            c.actualizarConsumicion();
                        }
                    } else if (flagAmarillo.getImage().equals(imagenCelda)) {
                        jTColaPlatos.setValueAt(flagVerde, columna, 3);
                        if (c != null) {
                            c.setEstado(3);
                            c.actualizarConsumicion();
                        }
                    } else if (flagVerde.getImage().equals(imagenCelda)) {
                        jTColaPlatos.setValueAt(flagRoja, columna, 3);
                        if (c != null) {
                            c.setEstado(1);
                            c.actualizarConsumicion();
                        }
                    } else {
                        System.out.println("no cuela");
                    }
                }

                public void mouseEntered(MouseEvent arg0) {
                }

                public void mouseExited(MouseEvent arg0) {
                }

                public void mousePressed(MouseEvent arg0) {
                }

                public void mouseReleased(MouseEvent arg0) {
                }
            });
        }
        return jTColaPlatos;
    }

    /**

	 * This method initializes jPLeyenda1	

	 * 	

	 * @return javax.swing.JPanel	

	 */
    private JPanel getJPLeyenda1() {
        if (jPLeyenda1 == null) {
            jLListo = new JLabel();
            jLListo.setText("Listo");
            jLListo.setIcon(new ImageIcon(getClass().getResource("/iconos/tag_green.png")));
            jLEnPreparacion = new JLabel();
            jLEnPreparacion.setText("En preparaci√≥n");
            jLEnPreparacion.setIcon(new ImageIcon(getClass().getResource("/iconos/tag_yellow.png")));
            jLSolicitado = new JLabel();
            jLSolicitado.setText("Solicitado");
            jLSolicitado.setIcon(new ImageIcon(getClass().getResource("/iconos/tag_red.png")));
            jPLeyenda1 = new JPanel();
            jPLeyenda1.setLayout(new FlowLayout());
            jPLeyenda1.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED), "LEYENDA:", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.ABOVE_TOP, new Font("Dialog", Font.BOLD, 12), new Color(51, 51, 51)));
            jPLeyenda1.add(jLSolicitado, null);
            jPLeyenda1.add(jLEnPreparacion, null);
            jPLeyenda1.add(jLListo, null);
        }
        return jPLeyenda1;
    }

    /**

	 * This method initializes right	

	 * 	

	 * @return javax.swing.JPanel	

	 */
    private JPanel getRight() {
        if (right == null) {
            right = new JPanel();
            right.setLayout(new BorderLayout());
            right.setPreferredSize(new Dimension(400, 600));
            right.setBorder(BorderFactory.createTitledBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED), "DISPONIBILIDAD DE LOS PRODUCTOS:", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.ABOVE_TOP, new Font("Dialog", Font.BOLD, 12), new Color(51, 51, 51)));
            right.add(getJTabbedPane(), BorderLayout.CENTER);
            right.add(getJPLeyenda2(), BorderLayout.SOUTH);
        }
        return right;
    }

    /**

	 * This method initializes jPLeyenda2	

	 * 	

	 * @return javax.swing.JPanel	

	 */
    private JPanel getJPLeyenda2() {
        if (jPLeyenda2 == null) {
            jLNoDisponible = new JLabel();
            jLNoDisponible.setIcon(new ImageIcon(getClass().getResource("/iconos/thumb_down.png")));
            jLNoDisponible.setText("No disponible");
            jLDisponible = new JLabel();
            jLDisponible.setIcon(new ImageIcon(getClass().getResource("/iconos/thumb_up.png")));
            jLDisponible.setText("Disponible");
            jPLeyenda2 = new JPanel();
            jPLeyenda2.setLayout(new FlowLayout());
            jPLeyenda2.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED), "LEYENDA:", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.ABOVE_TOP, new Font("Dialog", Font.BOLD, 12), new Color(51, 51, 51)));
            jPLeyenda2.add(jLDisponible, null);
            jPLeyenda2.add(jLNoDisponible, null);
        }
        return jPLeyenda2;
    }

    /**

	 * This method initializes jTabbedPane	

	 * 	

	 * @return javax.swing.JTabbedPane	

	 */
    private JTabbedPane getJTabbedPane() {
        if (jTabbedPane == null) {
            jTabbedPane = new JTabbedPane();
            jTabbedPane.setPreferredSize(new Dimension(0, 0));
            jTabbedPane.addTab("SECCIONES", new ImageIcon(getClass().getResource("/iconos/table_edit.png")), getJLSecciones(), null);
            jTabbedPane.addTab("PRODUCTOS", new ImageIcon(getClass().getResource("/iconos/cup.png")), getJSPProductos(), null);
        }
        return jTabbedPane;
    }

    /**

	 * This method initializes jLSecciones	

	 * 	

	 * @return javax.swing.JList	

	 */
    private JList getJLSecciones() {
        if (jLSecciones == null) {
            jLSecciones = new JList(this.getDefaultListModel());
            jLSecciones.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            jLSecciones.setFont(new Font("Dialog", Font.BOLD, 20));
            jLSecciones.setSelectedIndex(0);
            jLSecciones.addListSelectionListener(new ListSelectionListener() {

                public void valueChanged(ListSelectionEvent arg0) {
                    Seccion s[] = Cocina.m_Restaurante.getCarta().getSecciones();
                    Seccion secc = null;
                    for (int i = 0; i < s.length && secc == null; ++i) if (s[i].getNombreSeccion().equals(jLSecciones.getSelectedValue())) secc = s[i];
                    MyDisponibilidadTableModel modelo = (MyDisponibilidadTableModel) jTProductos.getModel();
                    seccionActual = secc;
                    modelo.actualizarProductos(secc);
                }
            });
        }
        return jLSecciones;
    }

    /**

	 * This method initializes jSPProductos	

	 * 	

	 * @return javax.swing.JScrollPane	

	 */
    private JScrollPane getJSPProductos() {
        if (jSPProductos == null) {
            jSPProductos = new JScrollPane();
            jSPProductos.setViewportView(getJTProductos());
        }
        return jSPProductos;
    }

    /**

	 * This method initializes jTProductos	

	 * 	

	 * @return javax.swing.JTable	

	 */
    private JTable getJTProductos() {
        if (jTProductos == null) {
            MyDisponibilidadTableModel mdtm = new MyDisponibilidadTableModel(seccionActual);
            jTProductos = new JTable(mdtm);
            jTProductos.setAutoCreateColumnsFromModel(true);
            jTProductos.setShowGrid(true);
            jTProductos.setRowHeight(45);
            jTProductos.setCellSelectionEnabled(false);
            jTProductos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            jTProductos.setRowSelectionAllowed(true);
            jTProductos.setFont(new Font("Arial", Font.BOLD, 16));
            TableColumn column = null;
            for (int i = 0; i < 2; i++) {
                column = jTProductos.getColumnModel().getColumn(i);
                switch(i) {
                    case 0:
                        column.setPreferredWidth(330);
                        break;
                    case 1:
                        column.setPreferredWidth(70);
                        break;
                }
            }
            jTProductos.addMouseListener(new MouseListener() {

                public void mouseClicked(MouseEvent arg0) {
                    int columna = jTProductos.getSelectedRow();
                    String estado_producto = "";
                    java.awt.Image imagenCelda = ((ImageIcon) jTProductos.getValueAt(columna, 1)).getImage();
                    if (disponible.getImage().equals(imagenCelda)) {
                        jTProductos.setValueAt(no_disponible, columna, 1);
                        estado_producto = "0";
                    } else if (no_disponible.getImage().equals(imagenCelda)) {
                        jTProductos.setValueAt(disponible, columna, 1);
                        estado_producto = "1";
                    } else {
                        System.out.println("no cuela");
                    }
                    String nombre_producto = jTProductos.getValueAt(columna, 0).toString();
                    Producto p = null;
                    for (int i = 0; i < seccionActual.getProductos().length; ++i) {
                        if (nombre_producto.equals(seccionActual.getProductos()[i].toString())) {
                            p = seccionActual.getProductos()[i];
                            p.setEstado(estado_producto);
                        }
                    }
                }

                public void mouseEntered(MouseEvent arg0) {
                }

                public void mouseExited(MouseEvent arg0) {
                }

                public void mousePressed(MouseEvent arg0) {
                }

                public void mouseReleased(MouseEvent arg0) {
                }
            });
        }
        return jTProductos;
    }

    /**

	 * This method initializes defaultListModel	

	 * 	

	 * @return javax.swing.DefaultListModel	

	 */
    private DefaultListModel getDefaultListModel() {
        if (defaultListModel == null) {
            defaultListModel = new DefaultListModel();
            Restaurante ds = Cocina.m_Restaurante;
            Seccion sccs[] = ds.getCarta().getSecciones();
            seccionActual = sccs[0];
            for (int i = 0; i < sccs.length; ++i) {
                defaultListModel.addElement(sccs[i].getNombreSeccion());
            }
        }
        return defaultListModel;
    }
}

class MyEstadoTableModel extends AbstractTableModel {

    /**

	 * 

	 */
    private static final long serialVersionUID = 1L;

    String[] columnNames = { "COD", "PLATO", "OBSERVACIONES", "ESTADO" };

    Object[][] data = null;

    List<Consumicion> consumicion = new ArrayList<Consumicion>();

    public MyEstadoTableModel() {
        actualizar();
    }

    public List<Consumicion> getConsumicones() {
        return consumicion;
    }

    public void setRowCount(int rows) {
        data = new Object[rows][getColumnCount()];
    }

    public int getColumnCount() {
        return columnNames.length;
    }

    public int getRowCount() {
        return data.length;
    }

    public String getColumnName(int col) {
        return columnNames[col];
    }

    public Object getValueAt(int row, int col) {
        return data[row][col];
    }

    @SuppressWarnings("unchecked")
    public Class getColumnClass(int c) {
        return getValueAt(0, c).getClass();
    }

    public boolean isCellEditable(int row, int col) {
        return false;
    }

    public void setValueAt(Object value, int row, int col) {
        data[row][col] = value;
        fireTableCellUpdated(row, col);
    }

    public void actualizar() {
        Restaurante dondeSea = Cocina.m_Restaurante;
        List<Pedido> pedidos = dondeSea.recuperarPedidos();
        consumicion.clear();
        for (int i = 0; i < pedidos.size(); ++i) {
            for (int j = 0; j < pedidos.get(i).getConsumiciones().size(); ++j) {
                if (pedidos.get(i).getConsumiciones().get(j).getEstado() > 0 && pedidos.get(i).getConsumiciones().get(j).getEstado() < 4) {
                    consumicion.add(pedidos.get(i).getConsumiciones().get(j));
                }
            }
        }
        setRowCount(consumicion.size());
        for (int i = 0; i < getRowCount(); ++i) {
            setValueAt(consumicion.get(i).getCodConsumicion(), i, 0);
            if (consumicion.get(i).getProducto() != null) setValueAt(consumicion.get(i).getProducto().toString(), i, 1); else setValueAt("", i, 1);
            String observaciones = consumicion.get(i).getObservaciones();
            if (observaciones == null) observaciones = "";
            setValueAt(observaciones, i, 2);
            switch(consumicion.get(i).getEstado()) {
                case 1:
                    setValueAt(new ImageIcon(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/iconos/tag_red.png"))), i, 3);
                    break;
                case 2:
                    setValueAt(new ImageIcon(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/iconos/tag_yellow.png"))), i, 3);
                    break;
                case 3:
                    setValueAt(new ImageIcon(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/iconos/tag_green.png"))), i, 3);
                    break;
            }
        }
    }
}

class MyDisponibilidadTableModel extends AbstractTableModel {

    /**

	 * 

	 */
    private static final long serialVersionUID = 1L;

    String[] columnNames = { "PRODUCTO", "DISPONIBILIDAD" };

    Object[][] data = null;

    public MyDisponibilidadTableModel(Seccion s) {
        if (s != null) actualizarProductos(s);
    }

    public int getColumnCount() {
        return columnNames.length;
    }

    public int getRowCount() {
        return data.length;
    }

    public void setRowCount(int rows) {
        data = new Object[rows][this.getColumnCount()];
    }

    public String getColumnName(int col) {
        return columnNames[col];
    }

    public Object getValueAt(int row, int col) {
        return data[row][col];
    }

    @SuppressWarnings("unchecked")
    public Class getColumnClass(int c) {
        return getValueAt(0, c).getClass();
    }

    public boolean isCellEditable(int row, int col) {
        return false;
    }

    public void setValueAt(Object value, int row, int col) {
        data[row][col] = value;
        fireTableCellUpdated(row, col);
    }

    public void actualizarProductos(Seccion s) {
        Producto productos[] = s.getProductos();
        this.setRowCount(productos.length);
        for (int i = 0; i < productos.length; ++i) {
            setValueAt(productos[i].toString(), i, 0);
            if (productos[i].consultarEstado().equals("1")) {
                setValueAt(new ImageIcon(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/iconos/thumb_up.png"))), i, 1);
            } else setValueAt(new ImageIcon(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/iconos/thumb_down.png"))), i, 1);
        }
    }
}

class HebraActualizadora extends Thread {

    JTable tabla = null;

    public HebraActualizadora(JTable t) {
        tabla = t;
    }

    public void run() {
        while (true) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            ((MyEstadoTableModel) tabla.getModel()).actualizar();
            tabla.setVisible(false);
            tabla.setVisible(true);
        }
    }
}
