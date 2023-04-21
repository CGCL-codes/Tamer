package org.opensih.Actions;

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
import org.jboss.annotation.ejb.cache.Cache;
import org.jboss.seam.annotations.Begin;
import org.jboss.seam.annotations.Create;
import org.jboss.seam.annotations.Destroy;
import org.jboss.seam.annotations.End;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Out;
import org.jboss.seam.faces.FacesMessages;
import org.opensih.ControladoresCU.IMantenimiento;
import org.opensih.Modelo.Desarrollo;
import org.opensih.Modelo.Resolver;
import org.opensih.Modelo.UnidadEjecutora;
import org.opensih.Seguridad.IUtils;
import org.opensih.Utils.Constantes.EstadoVoc;
import org.opensih.Utils.Converters.UEConverter;

@Stateful
@Name("resuelto")
@Cache(org.jboss.ejb3.cache.NoPassivationCache.class)
public class ResueltoAction implements IResuelto {

    @PersistenceContext(type = PersistenceContextType.EXTENDED)
    private EntityManager em;

    @EJB
    IUtils utils;

    @EJB
    IMantenimiento logica;

    @In(required = false)
    @Out(required = false)
    Desarrollo desarrollo;

    Resolver res;

    UnidadEjecutora ue;

    List<UnidadEjecutora> ues;

    Map<String, UnidadEjecutora> uesMap;

    @Create
    @Begin(join = true)
    public String inicial() {
        ue = utils.devolverUE();
        ues = logica.listarUnidades();
        if (ue == null) ue = ues.get(0);
        Map<String, UnidadEjecutora> results = new TreeMap<String, UnidadEjecutora>();
        for (UnidadEjecutora p : ues) {
            String nom = p.toString();
            results.put(nom, p);
        }
        uesMap = results;
        res = new Resolver();
        return "ok";
    }

    public String guardar() {
        if (res.getTipo() == null) {
            FacesMessages.instance().add("Debe seleccionar el tipo de alta del Paciente.");
            return null;
        }
        if (res.getDestino() == null) {
            FacesMessages.instance().add("Debe seleccionar el destino del Paciente.");
            return null;
        }
        if (res.getAtencion() == null) {
            FacesMessages.instance().add("Debe seleccionar la atencion del Paciente.");
            return null;
        }
        desarrollo.setEstado(EstadoVoc.Resuelto);
        desarrollo.setResuelto(res);
        em.merge(desarrollo);
        return "nueva";
    }

    public Converter getConverterUe() {
        return new UEConverter(ues);
    }

    public Map<String, UnidadEjecutora> getUes() {
        return uesMap;
    }

    @End
    public String cancel() {
        return "ok";
    }

    public void seteo() {
    }

    @Destroy
    @Remove
    public void destroy() {
    }

    /*********************GETS Y SETS********************************/
    public UnidadEjecutora getUe() {
        return ue;
    }

    public void setUe(UnidadEjecutora ue) {
        this.ue = ue;
    }

    public Resolver getRes() {
        return res;
    }

    public void setRes(Resolver res) {
        this.res = res;
    }
}
