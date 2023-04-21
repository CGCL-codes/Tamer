package fondefitco.Vista.Controlador;

import fondefitco.Controlador.ControlProductos;
import fondefitco.Controlador.ValidarorVistas;
import fondefitco.Modelo.Productos;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JComboBox;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Netbeans
 */
public class GestorVista_ConsultarProductos {

    ValidarorVistas validador = new ValidarorVistas();

    /**
     * Metodo que permite enviar los campos a la clase de validacion para verificar si se encuentran
     * vacios o contienen informacion.
     *
     */
    public boolean verificampo(JTextField... campos) {
        return validador.CamposVacios(campos);
    }

    /**
     * Metodo que me permite borrar toda la interfaz, y coloca el focus en la posicion inicial.
     * este metodo tiene en cuenta cual es el primer campo que se envia para poder entregar
     * el focus.
     */
    public void RetornaColor(JTextField... campos) {
        validador.RetornaColor(campos);
        validador.VaciarCampos(campos);
        campos[0].requestFocus();
    }

    public static void Limpiar(JComboBox s, DefaultTableModel dtm) {
        s.setSelectedIndex(0);
        dtm.setNumRows(0);
        ListarProductos(dtm);
    }

    public static Productos BuscarSeleccion(int seleccion, String campos) throws Exception {
        Productos prod = null;
        if (seleccion == 1) {
            prod = ControlProductos.Buscar(campos);
        } else {
            prod = ControlProductos.BuscarNombre(campos);
        }
        return prod;
    }

    public static String Listar(DefaultTableModel dtm) {
        String listar = null;
        dtm.setNumRows(0);
        try {
            Productos prod = new Productos();
            ControlProductos.listar();
            Iterator it = ControlProductos.getDetallePedido().iterator();
            while (it.hasNext()) {
                prod = (Productos) it.next();
                listar += prod.getCodigo() + " " + prod.getNombre() + " " + prod.getTipo() + " " + prod.getPrecio() + " " + prod.getStockMax() + " " + prod.getStockMin() + " " + prod.getCantidad() + " " + prod.getFecharegistro() + " " + prod.getVelocidadconsumo() + " " + prod.getBodegaCodigo();
                String fila[] = { prod.getCodigo() + "", prod.getNombre(), prod.getDescripcion(), prod.getTipo(), prod.getPrecio() + "", prod.getStockMax() + "", prod.getStockMin() + "", prod.getCantidad() + "", prod.getFecharegistro() + "", prod.getVelocidadconsumo() + "", " " + prod.getBodegaCodigo(), prod.getProveedor() };
                dtm.addRow(fila);
            }
        } catch (SQLException ex) {
            Logger.getLogger(GestorVista_ConsultarProductos.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(GestorVista_ConsultarProductos.class.getName()).log(Level.SEVERE, null, ex);
        }
        return listar;
    }

    public static String ListarProductos(DefaultTableModel dtm) {
        String listar = null;
        try {
            Productos prod = new Productos();
            ControlProductos.listar();
            Iterator it = ControlProductos.getDetallePedido().iterator();
            while (it.hasNext()) {
                prod = (Productos) it.next();
                listar += prod.getCodigo() + " " + prod.getNombre() + " " + prod.getTipo() + " " + prod.getPrecio() + " " + prod.getStockMax() + " " + prod.getStockMin() + " " + prod.getCantidad() + " " + prod.getFecharegistro() + " " + prod.getVelocidadconsumo() + " " + prod.getBodegaCodigo() + " " + prod.getProveedor();
                String fila[] = { prod.getCodigo() + "", prod.getNombre(), prod.getDescripcion(), prod.getTipo(), prod.getPrecio() + "", prod.getStockMax() + "", prod.getStockMin() + "", prod.getCantidad() + "", prod.getFecharegistro() + "", prod.getVelocidadconsumo() + "", " " + prod.getBodegaCodigo(), prod.getProveedor() };
                dtm.addRow(fila);
            }
        } catch (SQLException ex) {
            Logger.getLogger(GestorVista_ConsultarProductos.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(GestorVista_ConsultarProductos.class.getName()).log(Level.SEVERE, null, ex);
        }
        return listar;
    }

    public static void busquedaRapida(String inf_db, String dato_busq, DefaultTableModel dtm, int seleccion) {
        String[] palabras_db = inf_db.split("");
        String[] palabras_busqueda = dato_busq.split("");
        boolean encontrado = false;
        if (palabras_busqueda.length > 0) {
            String primera = palabras_busqueda[0];
            for (int i = 0; i < palabras_db.length; i++) {
                if (palabras_db[i].equals(primera)) {
                    encontrado = true;
                    int j = 0;
                    if (palabras_busqueda.length < 1) {
                        for (j = 1; j < palabras_busqueda.length; j++) if (palabras_db[i + j].equals(palabras_busqueda[j])) encontrado = false;
                    }
                    if (encontrado) {
                        System.out.println("Productos: " + Integer.toString(Palabras(dato_busq, inf_db)));
                        System.out.println(palabras_busqueda[j]);
                        if (seleccion == 1) {
                            if (ControlProductos.Buscar(dato_busq) != null) {
                                dtm.setNumRows(0);
                                Productos prod = ControlProductos.Buscar(dato_busq);
                                String fila[] = { prod.getCodigo() + "", prod.getNombre(), prod.getDescripcion(), prod.getTipo(), prod.getPrecio() + "", prod.getStockMax() + "", prod.getStockMin() + "", prod.getCantidad() + "", prod.getFecharegistro() + "", prod.getVelocidadconsumo() + "", " " + prod.getBodegaCodigo(), prod.getProveedor() };
                                dtm.addRow(fila);
                            } else {
                                dtm.setNumRows(0);
                            }
                        } else if (seleccion == 2) {
                            if (ControlProductos.BuscarNombre(dato_busq) != null) {
                                try {
                                    dtm.setNumRows(0);
                                    Productos prod = ControlProductos.BuscarNombre(dato_busq);
                                    listarNombres(dato_busq, dtm);
                                } catch (SQLException ex) {
                                    Logger.getLogger(GestorVista_ConsultarProductos.class.getName()).log(Level.SEVERE, null, ex);
                                } catch (Exception ex) {
                                    Logger.getLogger(GestorVista_ConsultarProductos.class.getName()).log(Level.SEVERE, null, ex);
                                }
                            } else {
                                dtm.setNumRows(0);
                            }
                        }
                    } else {
                        System.out.println("");
                    }
                }
            }
        }
    }

    public static int Palabras(String Palabra, String Parrafo) {
        if (!(Parrafo.compareTo("") == 0) && (!(Palabra.compareTo("") == 0))) {
            int Ocurrencias = 0;
            for (int i = 0; i < (Parrafo.length() - Palabra.length()); i++) {
                if (Parrafo.substring(i, (i + Palabra.length())).compareTo(Palabra) == 0) {
                    Ocurrencias++;
                }
            }
            return Ocurrencias;
        }
        return 0;
    }

    public static void listarNombres(String nombre, DefaultTableModel dtm) throws SQLException, Exception {
        dtm.setNumRows(0);
        Productos prod = new Productos();
        ControlProductos.listar();
        Iterator it = ControlProductos.getDetallePedido().iterator();
        while (it.hasNext()) {
            prod = (Productos) it.next();
            if (prod.getNombre().equals(nombre)) {
                String fila[] = { prod.getCodigo() + "", prod.getNombre(), prod.getDescripcion(), prod.getTipo(), prod.getPrecio() + "", prod.getStockMax() + "", prod.getStockMin() + "", prod.getCantidad() + "", prod.getFecharegistro() + "", prod.getVelocidadconsumo() + "", "" + prod.getBodegaCodigo(), prod.getProveedor() };
                dtm.addRow(fila);
            } else {
                dtm.setNumRows(0);
            }
        }
    }
}
