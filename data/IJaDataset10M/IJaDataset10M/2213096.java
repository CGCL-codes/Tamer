package edu.ort.dcomp.fint.actions;

import edu.ort.dcomp.fint.engine.ServicioFacade;
import edu.ort.dcomp.fint.engine.UsuarioFacade;
import edu.ort.dcomp.fint.converter.ProveedorConverter;
import edu.ort.dcomp.fint.engine.Engine;
import edu.ort.dcomp.fint.engine.TransaccionFacade;
import edu.ort.dcomp.fint.jsf.JsfUtil;
import edu.ort.dcomp.fint.modelo.Proveedor;
import edu.ort.dcomp.fint.modelo.Servicio;
import edu.ort.dcomp.fint.modelo.Transaccion;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

/**
 *
 * @author migueldiab
 */
@ManagedBean
@SessionScoped
public class ServicioActions {

    @EJB
    private ServicioFacade servicioFacade;

    @EJB
    private Engine engine;

    @EJB
    private TransaccionFacade transaccionFacade;

    private Servicio servicio;

    private List<Servicio> listaServicios;

    private static String PATH = "/servicios/";

    public ServicioActions() {
    }

    @EJB
    private UsuarioFacade usuarioController;

    public Servicio getServicio() {
        if (null == servicio) {
            servicio = new Servicio();
        }
        return servicio;
    }

    public void nuevoServicio() {
        servicio = new Servicio();
    }

    public List<Servicio> getListaServicios() {
        return listaServicios;
    }

    public String buscarServicios() {
        String response = PATH + "lista";
        String id = JsfUtil.getRequestParameter("conectar:id");
        String password = JsfUtil.getRequestParameter("conectar:password");
        Proveedor proveedor = (Proveedor) JsfUtil.getObjectFromRequestParameter("conectar:proveedor", new ProveedorConverter(), null);
        List<Servicio> lista = null;
        try {
            lista = servicioFacade.listarCuentasProveedor(id, password, proveedor);
            if (lista.isEmpty()) {
                JsfUtil.addErrorMessage("El proveedor no tiene cuentas disponibles para esa ID. Consulte con su proveedor por activación de servicios en línea");
            } else {
                response = PATH + "conectar";
                listaServicios = lista;
                for (Servicio servicio1 : lista) {
                    System.out.println("servicio " + servicio1);
                }
            }
        } catch (Exception ex) {
            JsfUtil.addErrorMessage("Hubo un error al conectarse con el servicio del proveedor. Intente mas tarde");
        }
        return response;
    }

    public String guardar(Servicio unServicio) {
        String response;
        try {
            unServicio.setConectado(Boolean.FALSE);
            unServicio.setUsuario(usuarioController.getUsuario());
            usuarioController.guardarServicio(unServicio);
            response = PATH + "lista";
        } catch (Exception e) {
            String msg = "No se pudo crear el servicio";
            engine.errorLog(msg, e.toString());
            JsfUtil.addErrorMessage(msg);
            response = PATH + "crear";
        }
        return response;
    }

    public String conectarServicio(Servicio unServicio) {
        String response;
        try {
            servicio.setConectado(Boolean.TRUE);
            usuarioController.guardarServicio(unServicio);
            JsfUtil.addSuccessMessage("Se agregó el servicio!");
            response = PATH + "lista";
        } catch (Exception e) {
            String msg = "No se pudo crear automaticametne el servicio";
            engine.errorLog(msg, e.toString());
            JsfUtil.addErrorMessage(msg);
            response = PATH + "crear";
        }
        return response;
    }

    public String movimientosServicios(Servicio unServicio) {
        System.out.println("movimientosServicios");
        servicio = unServicio;
        return PATH + "estado";
    }

    public String proximosVencimientos() {
        System.out.println("proximosVencimientos");
        return PATH + "venicmientos";
    }

    public List<Transaccion> getTransacciones() {
        return transaccionFacade.obtenerPorServicioOrdenadoPorFecha(servicio);
    }

    public List<Transaccion> getVencimientos() {
        return transaccionFacade.obtenerPendientesPorUsuario(usuarioController.getUsuario());
    }

    public String borrarServicio(Servicio unServicio) {
        System.out.println("Borrando");
        usuarioController.borrarServicio(unServicio);
        return PATH + "borrada";
    }

    public List<Proveedor> getProveedores() {
        return servicioFacade.getProveedores();
    }

    public Proveedor getProveedorById(Integer idProveedor) {
        return servicioFacade.getProveedorById(idProveedor);
    }
}
