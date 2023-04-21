package org.opensih.gdq.Actions;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import javax.ejb.EJB;
import javax.ejb.Remove;
import javax.ejb.Stateful;
import javax.faces.convert.Converter;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import org.jboss.seam.annotations.Begin;
import org.jboss.seam.annotations.Create;
import org.jboss.seam.annotations.Destroy;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.security.Identity;
import org.opensih.comunes.Controladores.IMantenimiento;
import org.opensih.comunes.Modelo.Servicio;
import org.opensih.comunes.Modelo.UnidadEjecutora;
import org.opensih.comunes.Seguridad.IUtils;
import org.opensih.comunes.Utils.Constantes.RolesVoc;
import org.opensih.comunes.Utils.Converters.UEConverter;
import org.opensih.gdq.Controladores.ICUBusqueda;
import org.opensih.gdq.Modelo.Intervencion;
import org.opensih.gdq.Utils.BusquedaCriterio;
import org.opensih.gdq.Utils.CriterioBusqueda;
import org.opensih.gdq.Utils.OrdenCriterio;
import org.opensih.gdq.Utils.Constantes.EstadoVoc;

@Stateful
@Name("controlGDQ")
public class ControlListaAction implements IControlLista {

    @PersistenceContext(type = PersistenceContextType.EXTENDED)
    EntityManager em;

    @EJB
    ICUBusqueda cu_busq;

    @EJB
    IUtils utils;

    @EJB
    IMantenimiento logica;

    boolean mostrar;

    UnidadEjecutora ue;

    List<UnidadEjecutora> ues;

    Map<String, UnidadEjecutora> uesMap;

    List<Intervencion> lista1 = new LinkedList<Intervencion>();

    List<Intervencion> lista2 = new LinkedList<Intervencion>();

    long cantidad1;

    long cantidad2;

    String ids;

    @In
    org.jboss.seam.security.Credentials credentials;

    @In
    Identity identity;

    @Create
    @Begin(join = true)
    public String create() {
        ue = utils.devolverUE();
        ues = logica.listarUnidades();
        if (ue == null) ue = ues.get(0);
        Map<String, UnidadEjecutora> results = new TreeMap<String, UnidadEjecutora>();
        for (UnidadEjecutora p : ues) {
            String nom = p.toString();
            results.put(nom, p);
        }
        uesMap = results;
        if (identity.hasRole(RolesVoc.CoordServ.getName())) {
            List<Servicio> orgs = logica.getServiciosCoordinador(credentials.getUsername(), ue.getCodigo());
            ids = "";
            for (Servicio s : orgs) {
                ids += s.getId() + ",";
            }
            if (ids.length() > 0) ids = ids.substring(0, ids.length() - 1);
        }
        buscarCoordinados();
        buscarEnOportunidad();
        mostrar = (!lista1.isEmpty()) || (!lista2.isEmpty());
        return "true";
    }

    public String buscarCoordinados() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        List<CriterioBusqueda> criterios = new LinkedList<CriterioBusqueda>();
        if (ue != null) {
            criterios.add(new CriterioBusqueda(ue.getCodigo(), "", BusquedaCriterio.Id_UnidadEjecutora));
        }
        if (identity.hasRole(RolesVoc.CoordServ.getName())) {
            criterios.add(new CriterioBusqueda(ids, "", BusquedaCriterio.ID_Conjunto_Servicio));
        }
        criterios.add(new CriterioBusqueda(EstadoVoc.Coordinado.getDisplayName(), "", BusquedaCriterio.Estado_Intevencion));
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.DATE, -1);
        String f2 = sdf.format(cal.getTime());
        criterios.add(new CriterioBusqueda(f2, "", BusquedaCriterio.Fecha_Coordinacion_2));
        lista1 = cu_busq.realizarConsulta(criterios, em, 1000, OrdenCriterio.Orden_Fecha_Solicitud);
        cantidad1 = cu_busq.realizarConsultaCantidad(criterios, em);
        return null;
    }

    public String buscarEnOportunidad() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        List<CriterioBusqueda> criterios1 = new LinkedList<CriterioBusqueda>();
        if (ue != null) {
            criterios1.add(new CriterioBusqueda(ue.getCodigo(), "", BusquedaCriterio.Id_UnidadEjecutora));
        }
        if (identity.hasRole(RolesVoc.CoordServ.getName())) {
            criterios1.add(new CriterioBusqueda(ids, "", BusquedaCriterio.ID_Conjunto_Servicio));
        }
        criterios1.add(new CriterioBusqueda(EstadoVoc.En_Oportunidad.getDisplayName(), "", BusquedaCriterio.Estado_Intevencion));
        criterios1.add(new CriterioBusqueda("SI", "", BusquedaCriterio.Categoria));
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.DATE, -180);
        String f2 = sdf.format(cal.getTime());
        criterios1.add(new CriterioBusqueda(f2, "", BusquedaCriterio.Fecha_Solicitud_2));
        lista2 = cu_busq.realizarConsulta(criterios1, em, 1000, OrdenCriterio.Orden_Fecha_Solicitud);
        cantidad2 = cu_busq.realizarConsultaCantidad(criterios1, em);
        List<CriterioBusqueda> criterios2 = new LinkedList<CriterioBusqueda>();
        if (ue != null) {
            criterios2.add(new CriterioBusqueda(ue.getCodigo(), "", BusquedaCriterio.Id_UnidadEjecutora));
        }
        if (identity.hasRole(RolesVoc.CoordServ.getName())) {
            criterios2.add(new CriterioBusqueda(ids, "", BusquedaCriterio.ID_Conjunto_Servicio));
        }
        criterios2.add(new CriterioBusqueda(EstadoVoc.En_Oportunidad.getDisplayName(), "", BusquedaCriterio.Estado_Intevencion));
        criterios2.add(new CriterioBusqueda("NO", "", BusquedaCriterio.Categoria));
        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(new Date());
        cal2.add(Calendar.DATE, -30);
        f2 = sdf.format(cal2.getTime());
        criterios2.add(new CriterioBusqueda(f2, "", BusquedaCriterio.Fecha_Solicitud_2));
        lista2.addAll(cu_busq.realizarConsulta(criterios2, em, 1000, OrdenCriterio.Orden_Fecha_Solicitud));
        cantidad2 += cu_busq.realizarConsultaCantidad(criterios2, em);
        return null;
    }

    public void seteo() {
    }

    public Map<String, UnidadEjecutora> getUes() {
        return uesMap;
    }

    public Converter getConverterUe() {
        return new UEConverter(ues);
    }

    @Destroy
    @Remove
    public void destroy() {
    }

    public List<Intervencion> getLista1() {
        return lista1;
    }

    public void setLista1(List<Intervencion> lista1) {
        this.lista1 = lista1;
    }

    public List<Intervencion> getLista2() {
        return lista2;
    }

    public void setLista2(List<Intervencion> lista2) {
        this.lista2 = lista2;
    }

    public UnidadEjecutora getUe() {
        return ue;
    }

    public void setUe(UnidadEjecutora ue) {
        this.ue = ue;
    }

    public long getCantidad1() {
        return cantidad1;
    }

    public void setCantidad1(long cantidad1) {
        this.cantidad1 = cantidad1;
    }

    public long getCantidad2() {
        return cantidad2;
    }

    public void setCantidad2(long cantidad2) {
        this.cantidad2 = cantidad2;
    }

    public boolean isMostrar() {
        return mostrar;
    }

    public void setMostrar(boolean mostrar) {
        this.mostrar = mostrar;
    }
}
