package gestores;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Vector;
import javax.swing.JOptionPane;
import negocio.busqueda.Perfil;
import negocio.cliente.Cliente;
import negocio.cliente.SolicitudRRHH;
import negocio.cv.Puesto;
import persistencia.SingletonDACBase;
import persistencia.busqueda.DACPerfil;
import persistencia.busqueda.DACSolicitudRRHH;
import persistencia.cliente.DACCliente;
import persistencia.cliente.DACPuesto;
import Excepciones.ExccepcionFormeteoDeFechas;
import Pantallas.ActualizarSolicitudDeRRHH;
import Pantallas.PantallaPerfil;
import Pantallas.Seguimiento;
import com.swtdesigner.SwingResourceManager;

public class GestorSolicitudRRHH {

    private SolicitudRRHH solicitudAux;

    private SolicitudRRHH solicitudPerfil;

    private ArrayList<SolicitudRRHH> solicitudes;

    private ArrayList<SolicitudRRHH> solicAEliminar;

    private Vector<Cliente> listaClientes;

    private Vector<Puesto> listaPuestos;

    private ActualizarSolicitudDeRRHH pantalla;

    private DACSolicitudRRHH dacSolicitud;

    private DACCliente dacCliente;

    private DACPuesto dacPuesto;

    public GestorSolicitudRRHH(ActualizarSolicitudDeRRHH pantalla) {
        this.pantalla = pantalla;
        solicitudes = new ArrayList<SolicitudRRHH>();
        solicAEliminar = new ArrayList<SolicitudRRHH>();
        dacSolicitud = new DACSolicitudRRHH(SingletonDACBase.tomarDACBase());
        dacCliente = new DACCliente(SingletonDACBase.tomarDACBase());
        dacPuesto = new DACPuesto(SingletonDACBase.tomarDACBase());
        listaClientes = new Vector<Cliente>(dacCliente.consultarClientes());
        listaPuestos = new Vector<Puesto>(dacPuesto.consultarPuestos());
    }

    public void tomarOpcionActualizarSolicitudRRHH() {
        try {
            solicitudes = new ArrayList<SolicitudRRHH>(dacSolicitud.consultaSolicitudesPendientes());
        } catch (ExccepcionFormeteoDeFechas e) {
            JOptionPane.showMessageDialog(null, "Error Formeteo Fechas Consultar Solicitudes Pendientes", e.getMensaje(), JOptionPane.ERROR_MESSAGE);
        }
        inicializarComponentes();
    }

    /**
	 * si es una solicitud ya cargada no hago nada y e inicializo los componentes
	 * pero si es una nuava la agrego a la lista e inicializo los componentes 
	 * 
	 */
    public void tomarParametrosSolicitud() {
        SolicitudRRHH solicitud = new SolicitudRRHH();
        if (solicitudAux != null) {
            solicitud.setIdSolicitudRRHH(solicitudAux.getIdSolicitudRRHH());
            solicitud.setEstado(solicitudAux.getEstado());
            solicitudAux = null;
        } else {
            solicitud.setEstado("nueva");
        }
        int posicion = pantalla.getComboBoxCliente().getSelectedIndex();
        Cliente c = listaClientes.get(posicion);
        posicion = pantalla.getComboBoxPuesto().getSelectedIndex();
        Puesto p = listaPuestos.get(posicion);
        solicitud.setFechaRecepcion(pantalla.getCalendarioRecepcion().getDate());
        solicitud.setCantEmpleadosSolictados(Integer.parseInt(pantalla.getCantidadSpinner().getValue().toString()));
        solicitud.setFechaPrevistaSolucion(pantalla.getCalendarioPrevista().getDate());
        solicitud.setRutaArchivo(pantalla.getRutaTextField().getText());
        solicitud.setObservacion(pantalla.getTextAreaObservacion().getText());
        solicitud.setCliente(c);
        solicitud.setPuesto(p);
        solicitudes.add(solicitud);
        inicializarComponentes();
    }

    /**
	 * Si selecciono una solicitud para subir, se la asigno al atributo solicitud
	 * que me sirve para saber si los datos de los componentes son de una 
	 * solicitud una ya cargada (no null) o de una nueva (null)
	 *
	 */
    public void subirSolicitudSeleccionada() {
        int seleccion = pantalla.getTable().getSelectedRow();
        if (seleccion != -1) {
            if (solicitudAux == null) {
                pantalla.getTableModel().removeRow(seleccion);
                solicitudAux = solicitudes.get(seleccion);
                solicitudes.remove(seleccion);
                pantalla.getCalendarioRecepcion().setDate(solicitudAux.getFechaRecepcion());
                pantalla.getCalendarioPrevista().setDate(solicitudAux.getFechaPrevistaSolucion());
                pantalla.getRutaTextField().setText(solicitudAux.getRutaArchivo());
                pantalla.getCantidadSpinner().setValue(solicitudAux.getCantEmpleadosSolictados());
                pantalla.getTextAreaObservacion().setText(solicitudAux.getObservacion());
                pantalla.getComboBoxCliente().setSelectedItem(solicitudAux.getCliente().getRazonSocial());
                pantalla.getComboBoxPuesto().setSelectedItem(solicitudAux.getPuesto().getNombre());
            } else {
                JOptionPane.showMessageDialog(null, "Hay una pendiente de modificar", "Informacion", JOptionPane.INFORMATION_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(null, "Tiene que seleccionar una", "Informacion", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    public void guardarSolicitudes() {
        int opcion = JOptionPane.showConfirmDialog(null, "Confirma la actualizacion de los datos?", "Confirmacion", JOptionPane.OK_CANCEL_OPTION);
        if (opcion == JOptionPane.OK_OPTION) {
            dacSolicitud = new DACSolicitudRRHH(SingletonDACBase.tomarDACBase());
            dacSolicitud.altaSolicitudRRHH(solicitudes);
            dacSolicitud.eliminarSolicitudes(solicAEliminar);
            JOptionPane.showMessageDialog(null, "Los datos se actualizaron correctamente", "Positivo", JOptionPane.INFORMATION_MESSAGE);
            tomarOpcionActualizarSolicitudRRHH();
        }
    }

    /**
	 * inicializa los elementos de la pantalla ActualizarSolicitudRRHH
	 * menos los combos que estan siempre cargados
	 *
	 */
    public void inicializarComponentes() {
        pantalla.getTableModel().getDataVector().clear();
        pantalla.getTable().updateUI();
        for (SolicitudRRHH solicitud : solicitudes) {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            pantalla.getTableModel().addRow(new Object[] { String.valueOf(solicitud.getIdSolicitudRRHH()), solicitud.getEstado(), solicitud.getCliente().getRazonSocial(), solicitud.getPuesto().getNombre(), sdf.format(solicitud.getFechaPrevistaSolucion()), solicitud.getCantEmpleadosSolictados() });
        }
        pantalla.getCalendarioRecepcion().setDate(new Date());
        pantalla.getCalendarioPrevista().setDate(new Date());
        pantalla.getRutaTextField().setText("");
        pantalla.getCantidadSpinner().setValue(1);
        pantalla.getTextAreaObservacion().setText("");
    }

    /**
	 * cancela todas a quellas solicitudes a guardar cargadas por el
	 * operador (con estado "nueva")
	 *
	 */
    public void operacionCancelar() {
        int opcion = JOptionPane.showConfirmDialog(null, "Descartar los datos a guardar?", "Confirmacion", JOptionPane.OK_CANCEL_OPTION);
        if (opcion == JOptionPane.OK_OPTION) {
            for (int i = 0; i < solicitudes.size(); i++) {
                if (solicitudes.get(i).getEstado().equals("nueva")) {
                    solicitudes.remove(i);
                }
            }
            JOptionPane.showMessageDialog(null, "Los datos a guardar fueron descartados", "Positivo", JOptionPane.INFORMATION_MESSAGE);
        }
        tomarOpcionActualizarSolicitudRRHH();
    }

    /**
	 * M�todo que inicia la b�squeda y Selecci�n a partir de la presenta solicitud
	 * Si la solicitud esta guardada y tiene un perfil asocido
	 */
    public void tomarOpcionIniciarBusqueda(int seleccion) {
        solicitudPerfil = solicitudes.get(seleccion);
        if (solicitudPerfil.getIdSolicitudRRHH() != 0) {
            DACPerfil dacPerfil = new DACPerfil(SingletonDACBase.tomarDACBase());
            Perfil p = dacPerfil.consultarPerfil(solicitudPerfil.getIdSolicitudRRHH());
            if (p != null) {
                new GestorBusqueda(this, p);
            } else {
                JOptionPane.showMessageDialog(null, "Primero asocie un perfil a la solicitud", "Informacion", JOptionPane.INFORMATION_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(null, "Primero guarde los datos de la solicitud", "Informacion", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    public void actualizarSolicitudEnGestion() {
    }

    public void tomarOpcionEliminarSolictitud(int seleccion) {
        int opcion = JOptionPane.showOptionDialog(null, "Seguro desea eliminar la solicitud", "Confirmar", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, SwingResourceManager.getIcon(Seguimiento.class, "/Iconos/edit_remove-000.png"), null, 0);
        if (opcion == JOptionPane.OK_OPTION) {
            solicAEliminar.add(solicitudes.get(seleccion));
            solicitudes.remove(seleccion);
            inicializarComponentes();
        }
    }

    public void tomarOpcionEliminarTodos() {
        if (solicitudes.size() != 0) {
            int opcion = JOptionPane.showOptionDialog(null, "Seguro desea eliminar TODAS las Solicitudes", "Confirmar", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, SwingResourceManager.getIcon(Seguimiento.class, "/Iconos/removeAll-001.png"), null, 0);
            if (opcion == JOptionPane.OK_OPTION) {
                solicAEliminar.addAll(solicitudes);
                solicitudes.clear();
                inicializarComponentes();
            }
        } else {
            JOptionPane.showMessageDialog(null, "No hay seguimientos parea eliminar", "Informacion", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    public SolicitudRRHH getSolicitudPerfil() {
        return solicitudPerfil;
    }

    public Vector<Cliente> getListaClientes() {
        return listaClientes;
    }

    public Vector<Puesto> getListaPuestos() {
        return listaPuestos;
    }

    public void tomarOpcionActualizarPerfil(int seleccion) {
        solicitudPerfil = solicitudes.get(seleccion);
        if (solicitudPerfil.getIdSolicitudRRHH() != 0) {
            PantallaPerfil p = new PantallaPerfil(this);
            p.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(null, "Primero guarde los datos de la solicitud", "Informacion", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    public void cerrarSolicitud() {
        solicitudPerfil.cerrar();
        ArrayList<SolicitudRRHH> aGuardar = new ArrayList<SolicitudRRHH>();
        aGuardar.add(solicitudPerfil);
        dacSolicitud.altaSolicitudRRHH(aGuardar);
    }
}
