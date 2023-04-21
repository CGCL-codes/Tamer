package org.opensih.Actions;

import java.text.SimpleDateFormat;
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
import org.opensih.Modelo.Calificacion;
import org.opensih.Modelo.Ciap2;
import org.opensih.Modelo.Desarrollo;
import org.opensih.Modelo.Ingreso;
import org.opensih.Modelo.Paciente;
import org.opensih.Modelo.UnidadEjecutora;
import org.opensih.Seguridad.IUtils;
import org.opensih.Utils.Constantes.EstadoVoc;
import org.opensih.Utils.Converters.UEConverter;

@Stateful
@Name("ingreso")
@Cache(org.jboss.ejb3.cache.NoPassivationCache.class)
public class IngresoAction implements IIngreso {

    @PersistenceContext(type = PersistenceContextType.EXTENDED)
    private EntityManager em;

    @EJB
    IUtils utils;

    @EJB
    IMantenimiento logica;

    @In(required = false)
    @Out(required = false)
    Desarrollo desarrollo;

    Ingreso ingr;

    @In(required = false)
    Paciente paciente;

    boolean confirm;

    String cod;

    String descr;

    List<Ciap2> codigos = new LinkedList<Ciap2>();

    UnidadEjecutora ue;

    UnidadEjecutora centro;

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
        Date fecha = new Date();
        desarrollo = new Desarrollo();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        desarrollo.setID(sdf.format(fecha));
        desarrollo.setEstado(EstadoVoc.Espera);
        desarrollo.setUe(ue);
        ingr = new Ingreso();
        ingr.setFechaIngr(fecha);
        confirm = false;
        return "ok";
    }

    public String guardar() {
        if (paciente == null) {
            FacesMessages.instance().add("Debe ingresar un Paciente.");
            return null;
        }
        if (!confirm) {
            FacesMessages.instance().add("Debe confirmar los datos del Paciente ingresado.");
            return null;
        }
        if (codigos.isEmpty()) {
            FacesMessages.instance().add("Debe agregar un motivo de ingreso.");
            return null;
        }
        if (ingr.getOrigen() == null) {
            FacesMessages.instance().add("Debe seleccionar el orgien del Paciente.");
            return null;
        }
        if (ingr.getMedio() == null) {
            FacesMessages.instance().add("Debe seleccionar el medio de ingreso del Paciente.");
            return null;
        }
        if (centro == null) {
            FacesMessages.instance().add("Debe seleccionar el centro de procedencia del Paciente.");
            return null;
        }
        Paciente pac = em.find(Paciente.class, paciente.getRoot_extension());
        if (pac != null) {
            pac.setSexo(paciente.getSexo());
            pac.setFnac(paciente.getFnac());
            pac.setNombre(paciente.getNombre());
            pac.setApellido(paciente.getApellido());
            desarrollo.setPaciente(pac);
            em.merge(pac);
        } else desarrollo.setPaciente(paciente);
        ingr.setCentro(centro.getNombre());
        String mot = "<motivo>";
        for (Ciap2 c : codigos) {
            mot += c.toText();
        }
        mot += "</motivo>";
        ingr.setMotivo(mot);
        desarrollo.setIngreso(ingr);
        desarrollo.setCalificaciones(new LinkedList<Calificacion>());
        em.persist(desarrollo);
        return "nueva";
    }

    public void agregarCodigo() {
        if (cod.compareTo("") != 0) {
            Ciap2 c = new Ciap2();
            c.setMConCIAP(cod);
            c.setMConDsc(descr);
            codigos.add(c);
            cod = descr = "";
        }
    }

    public void eliminarCodigo() {
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
    public boolean isConfirm() {
        return confirm;
    }

    public void setConfirm(boolean confirm) {
        this.confirm = confirm;
    }

    public UnidadEjecutora getUe() {
        return ue;
    }

    public void setUe(UnidadEjecutora ue) {
        this.ue = ue;
    }

    public UnidadEjecutora getCentro() {
        return centro;
    }

    public void setCentro(UnidadEjecutora centro) {
        this.centro = centro;
    }

    public Ingreso getIngr() {
        return ingr;
    }

    public void setIngr(Ingreso ingr) {
        this.ingr = ingr;
    }

    public List<Ciap2> getCodigos() {
        return codigos;
    }

    public void setCodigos(List<Ciap2> codigos) {
        this.codigos = codigos;
    }

    public String getCod() {
        return cod;
    }

    public void setCod(String cod) {
        this.cod = cod;
    }

    public String getDescr() {
        return descr;
    }

    public void setDescr(String descr) {
        this.descr = descr;
    }
}
