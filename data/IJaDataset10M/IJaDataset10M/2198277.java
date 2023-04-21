package InterfacesGraficas;

import DTO.DTOCaso;
import DTO.DTOInformeReparacion;
import DTO.DTOOrden;
import Excepciones.ExcepcionCampoInvalido;
import Excepciones.ExcepcionObjetoNoEncontrado;
import Expertos.ExpertoConsultarAvanceDeReclamo;
import Fabricas.FabricaExpertos;
import InterfacesGraficas.ModelosTablas.ModeloTablaConsultarAvanceReclamo;
import InterfacesGraficas.ModelosTablas.ModeloTablaFallas;
import InterfacesGraficas.ModelosTablas.ModeloTablaOrdenesTrabajo;
import InterfacesGraficas.ModelosTablas.ModeloTablaSemaforosDenunciadosDTO;
import Persistencia.Entidades.Operador;
import java.util.List;
import javax.swing.JOptionPane;

/**
 *
 * @author LEIVA
 */
public class ControladorConsultarAvanceDeReclamo implements Controlador {

    public static final int COD_CASO_VACIO = 1;

    public static final int BUSQUEDA_VACIA = 2;

    public static final int CHUCK = 1;

    public static final int OTRO = 2;

    ExpertoConsultarAvanceDeReclamo experto;

    private PantallaConsultarAvanceDeReclamo pantalla;

    private PantallaDetalleOrden pantallaDetalleOrden;

    ModeloTablaConsultarAvanceReclamo modeloEstados;

    ModeloTablaOrdenesTrabajo modeloOrdenes;

    ModeloTablaFallas modeloFallas;

    ModeloTablaSemaforosDenunciadosDTO modeloSemaforos;

    private ChuckNorrisControlador chuck;

    public ControladorConsultarAvanceDeReclamo() {
        experto = (ExpertoConsultarAvanceDeReclamo) FabricaExpertos.getInstance().getExperto("ConsultarAvanceDeReclamo");
        pantalla = new PantallaConsultarAvanceDeReclamo(this);
        modeloEstados = new ModeloTablaConsultarAvanceReclamo();
        modeloOrdenes = new ModeloTablaOrdenesTrabajo();
        modeloFallas = new ModeloTablaFallas();
        modeloSemaforos = new ModeloTablaSemaforosDenunciadosDTO();
        pantalla.getTablaConsultarAvanceReclamo().setModel(modeloEstados);
        pantalla.getTblOrdenReparacion().setModel(modeloOrdenes);
        pantalla.getTblFallas().setModel(modeloFallas);
        pantalla.getTblSemaforos().setModel(modeloSemaforos);
    }

    public ControladorConsultarAvanceDeReclamo(ChuckNorrisControlador chuckCont) {
        chuck = chuckCont;
        experto = (ExpertoConsultarAvanceDeReclamo) FabricaExpertos.getInstance().getExperto("ConsultarAvanceDeReclamo");
        pantalla = new PantallaConsultarAvanceDeReclamo(this);
        modeloEstados = new ModeloTablaConsultarAvanceReclamo();
        modeloOrdenes = new ModeloTablaOrdenesTrabajo();
        modeloFallas = new ModeloTablaFallas();
        pantalla.getTablaConsultarAvanceReclamo().setModel(modeloEstados);
        pantalla.getTblOrdenReparacion().setModel(modeloOrdenes);
        pantalla.getTblFallas().setModel(modeloFallas);
    }

    public void iniciar() {
        getPantalla().setTitle("Consultar Avance de Reclamo");
        getPantalla().setLocationRelativeTo(null);
        getPantalla().setVisible(true);
        getPantalla().setRetorno(CHUCK);
    }

    public void iniciar(String numCaso) {
        pantalla.setTitle("Consultar Avance de Reclamo");
        pantalla.setLocationRelativeTo(null);
        pantalla.setVisible(true);
        pantalla.setRetorno(OTRO);
        pantalla.getTxtNumeroCaso().setText(numCaso);
        pantalla.getTxtNumeroCaso().setEnabled(false);
        pantalla.getRadioBtnDenuncia().setEnabled(false);
        pantalla.getRadioBtnReclamo().setEnabled(false);
        pantalla.getBotonConsultar().setEnabled(false);
        pantalla.getBtnSalir().setText("Regresar");
        pantalla.getBtnSalir().setIcon(new javax.swing.ImageIcon(getClass().getResource("/Utilidades/Imagenes/iconos/dialog-apply.png")));
        ConsultarEstadoCaso(numCaso, ExpertoConsultarAvanceDeReclamo.DENUNCIA);
    }

    public void ConsultarEstadoCaso(String numcaso, int seleccion) {
        limpiarPantalla();
        try {
            DTOCaso dtoDenuncia = experto.ConsultarEstadoCaso(numcaso, seleccion, chuck.getOperadorEncontrado());
            modeloEstados.addAllRow(dtoDenuncia.getListaEstados());
            if (dtoDenuncia.getOrdenesRep() != null) {
                modeloOrdenes.addAllRow(dtoDenuncia.getOrdenesRep());
            }
            if (dtoDenuncia.getListaFallas() != null) {
                modeloFallas.addAllRow(dtoDenuncia.getListaFallas());
            }
            pantalla.getLblCantReclamos().setText(pantalla.getLblCantReclamos().getText() + " " + dtoDenuncia.getCantidadReclamos() + ".");
            pantalla.getLblNroCaso().setText(pantalla.getLblNroCaso().getText() + numcaso);
            String ubicacion;
            if (dtoDenuncia.getUbicacion().getTipo().equalsIgnoreCase("INTERSECCION")) {
                ubicacion = dtoDenuncia.getUbicacion().getCalle1() + " y " + dtoDenuncia.getUbicacion().getCalle2();
            } else {
                ubicacion = dtoDenuncia.getUbicacion().getCalle1() + " " + dtoDenuncia.getUbicacion().getAltura();
            }
            pantalla.getLblUbicacion().setText(pantalla.getLblUbicacion().getText() + " " + ubicacion);
            if (dtoDenuncia.getSemaforosDenunciados() != null) {
                modeloSemaforos.addAllRow(dtoDenuncia.getSemaforosDenunciados());
            }
            pantalla.getBtnMostrarProblemas().setEnabled(true);
        } catch (ExcepcionCampoInvalido ex) {
            mostrarMensaje(COD_CASO_VACIO, ex.getMessage());
        } catch (ExcepcionObjetoNoEncontrado ex) {
            mostrarMensaje(BUSQUEDA_VACIA, ex.getMessage());
            pantalla.getTxtNumeroCaso().setText("");
        }
    }

    public void mostrarMensaje(int seleccion, String mensaje) {
        switch(seleccion) {
            case COD_CASO_VACIO:
                JOptionPane.showMessageDialog(getPantalla(), mensaje, "ATENCIÓN!", JOptionPane.INFORMATION_MESSAGE);
                System.out.println(mensaje);
                break;
            case BUSQUEDA_VACIA:
                JOptionPane.showMessageDialog(getPantalla(), mensaje, "ATENCIÓN!", JOptionPane.INFORMATION_MESSAGE);
                System.out.println(mensaje);
                break;
        }
    }

    public void mostrarDetalleOrden(DTOInformeReparacion informe) {
        pantallaDetalleOrden = new PantallaDetalleOrden(this);
        pantallaDetalleOrden.setTitle("Detalle Orden Reparación");
        pantallaDetalleOrden.setLocationRelativeTo(null);
        pantallaDetalleOrden.setVisible(true);
        pantallaDetalleOrden.setInformeReparacion(informe);
    }

    public void cerrar(int opcionSalida) {
        getPantalla().setVisible(false);
        getPantalla().dispose();
        if (opcionSalida == CHUCK) {
            chuck.iniciar();
        }
    }

    /**
     * @return the pantalla
     */
    public PantallaConsultarAvanceDeReclamo getPantalla() {
        return pantalla;
    }

    /**
     * @param chuck the chuck to set
     */
    public void setChuck(ChuckNorrisControlador chuck) {
        this.chuck = chuck;
    }

    public void habilitarBotonDetalleOrden(DTOOrden dtoOrdenRep) {
        boolean habilitar = false;
        if (dtoOrdenRep != null) {
            habilitar = experto.habilitarBotonDetalleOrden(dtoOrdenRep);
        }
        if (habilitar) {
            pantalla.getBtnDetalleOrden().setEnabled(true);
        } else {
            pantalla.getBtnDetalleOrden().setEnabled(false);
        }
    }

    public void limpiarPantalla() {
        modeloEstados.clear();
        modeloOrdenes.clear();
        modeloFallas.clear();
        modeloSemaforos.clear();
        pantalla.getLblCantReclamos().setText("Cantidad de Reclamos Caso:");
        pantalla.getLblNroCaso().setText("Caso Nº: ");
        pantalla.getLblUbicacion().setText("Ubicación:");
        pantalla.getBtnMostrarProblemas().setEnabled(false);
        habilitarBotonDetalleOrden(null);
    }

    public void cerrarPantallaDetalle() {
        pantallaDetalleOrden = null;
    }

    /**
     * @return the operadorEncontrado
     */
    public Operador getOperadorEncontrado() {
        return chuck.getOperadorEncontrado();
    }

    public List<String> getProblemasDenunciados() {
        return experto.getProblemasDenunciados();
    }
}
