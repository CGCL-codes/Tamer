package AdaptadorSistemaReportes;

import DTO.DTOOrden;
import DTO.DTOReserva;
import java.util.ArrayList;
import java.util.List;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;

/**
 *
 * @author diego
 * Contiene la informacion que se pasa cuando se genera un nuevo reporte
 */
public class OrdenesReparacionDataSource implements JRDataSource {

    private int indicadorOrdenActual = -1;

    private List<DTOOrden> listaOrdenes;

    private FallasTecnicasDataSource fallasDataSource;

    private SemaforoDataSource semaforosDataSource;

    private EquipamientoDataSource equipamientoDataSource;

    private RepuestoDataSource repuestoDataSource;

    public OrdenesReparacionDataSource() {
    }

    public OrdenesReparacionDataSource(List<DTOOrden> nuevaListaDtoOrdenes) {
        listaOrdenes = new ArrayList<DTOOrden>();
        listaOrdenes.addAll(listaOrdenes);
        fallasDataSource = new FallasTecnicasDataSource();
        semaforosDataSource = new SemaforoDataSource();
    }

    public void addOrden(DTOOrden nuevaoden) {
        if (listaOrdenes == null) {
            listaOrdenes = new ArrayList<DTOOrden>();
        }
        listaOrdenes.add(nuevaoden);
    }

    public void addAllOrden(List<DTOOrden> nuevaLista) {
        if (listaOrdenes == null) {
            listaOrdenes = new ArrayList<DTOOrden>();
        }
        listaOrdenes.addAll(nuevaLista);
    }

    public boolean next() throws JRException {
        ++indicadorOrdenActual;
        if (indicadorOrdenActual < listaOrdenes.size()) {
            return true;
        } else {
            return false;
        }
    }

    public Object getFieldValue(JRField jrf) throws JRException {
        Object valor = null;
        if (jrf.getName().equals("nroOrden")) {
            valor = listaOrdenes.get(indicadorOrdenActual).getNroOrden();
        } else if (jrf.getName().equals("fecha")) {
            valor = listaOrdenes.get(indicadorOrdenActual).getFechaInicioPlanificada().toString();
        } else if (jrf.getName().equals("equipo")) {
            valor = listaOrdenes.get(indicadorOrdenActual).getNombreEquipo();
        } else if (jrf.getName().equals("calle1")) {
            valor = listaOrdenes.get(indicadorOrdenActual).getUbicacion().getCalle1();
        } else if (jrf.getName().equals("calle2")) {
            valor = listaOrdenes.get(indicadorOrdenActual).getUbicacion().getCalle2();
        } else if (jrf.getName().equals("falla")) {
            fallasDataSource = new FallasTecnicasDataSource(listaOrdenes.get(indicadorOrdenActual).getListaFalla());
            valor = fallasDataSource;
        } else if (jrf.getName().equals("semaforos")) {
            semaforosDataSource = new SemaforoDataSource(listaOrdenes.get(indicadorOrdenActual).getListaSemaforos());
            valor = semaforosDataSource;
        } else if (jrf.getName().equals("equipamientos")) {
            equipamientoDataSource = new EquipamientoDataSource();
            for (DTOReserva reserva : listaOrdenes.get(indicadorOrdenActual).getListaReservas()) {
                equipamientoDataSource.addAllEquipamiento(reserva.getListaEquipamiento());
            }
            valor = equipamientoDataSource;
        } else if (jrf.getName().equals("repuestos")) {
            repuestoDataSource = new RepuestoDataSource();
            for (DTOReserva reserva : listaOrdenes.get(indicadorOrdenActual).getListaReservas()) {
                repuestoDataSource.addAllRepuestos(reserva.getListaRepuesto());
            }
            valor = repuestoDataSource;
        } else if (jrf.getName().equals("numCaso")) {
            valor = listaOrdenes.get(indicadorOrdenActual).getNroCaso();
        } else if (jrf.getName().equals("altura")) {
            if (listaOrdenes.get(indicadorOrdenActual).getUbicacion().getAltura() == null) {
                valor = "";
            } else {
                valor = listaOrdenes.get(indicadorOrdenActual).getUbicacion().getAltura();
            }
        }
        return valor;
    }

    /**
     * @return the fallasDataSource
     */
    public FallasTecnicasDataSource getFallasDataSource() {
        return fallasDataSource;
    }

    /**
     * @param fallasDataSource the fallasDataSource to set
     */
    public void setFallasDataSource(FallasTecnicasDataSource fallasDataSource) {
        this.fallasDataSource = fallasDataSource;
    }

    public boolean isVacio() {
        return (listaOrdenes == null) ? true : false;
    }
}
