package org.opensih.comunes.Actions;

import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Remove;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.jboss.seam.annotations.Begin;
import org.jboss.seam.annotations.Create;
import org.jboss.seam.annotations.Destroy;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.faces.FacesMessages;
import org.opensih.comunes.Controladores.IMantenimiento;
import org.opensih.comunes.Modelo.UnidadEjecutora;

@Stateful
@Name("unidadEjecutora")
public class UnidadEjecutoraAction implements IUnidadEjecutora {

    @PersistenceContext
    private EntityManager em;

    @EJB
    IMantenimiento logica;

    String nombre;

    String codigo;

    String codUE;

    String ips;

    int zona;

    String gdq;

    UnidadEjecutora e;

    @Create
    @Begin(join = true)
    public void nuevo() {
        nombre = codigo = ips = codUE = "";
        e = null;
    }

    public String alta() {
        if (nombre.equals("") || codigo.equals("") || ips.equals("") || codUE.equals("") || zona == 0 || gdq.equals("-")) {
            FacesMessages.instance().add("Debe completar todos los campos.");
            return null;
        }
        if (!logica.existeUE(codigo)) {
            boolean uso_gdq = false;
            if (gdq.equals("SI")) {
                uso_gdq = true;
            }
            logica.nuevaUE(codigo, codUE, nombre, ips, zona, uso_gdq);
            nombre = codigo = ips = codUE = "";
            zona = 0;
            FacesMessages.instance().add("Se ha ingresado la informaci�n exitosamente");
            return "success";
        } else {
            FacesMessages.instance().add("Existe un Unidad Ejecutora con ese codigo.");
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    public String buscar() {
        List<UnidadEjecutora> Ues = null;
        Ues = em.createQuery("SELECT e FROM UnidadEjecutora e WHERE e.nombre =:nom").setParameter("nom", nombre).getResultList();
        if (Ues.size() == 1) {
            e = Ues.get(0);
            ips = e.getRangoIps();
            codigo = e.getCodigo();
            codUE = e.getCodUE();
            nombre = e.getNombre();
            zona = e.getZona();
            if (e.isUso_gdq()) gdq = "SI"; else gdq = "NO";
        } else {
            if (Ues.size() == 0) {
                FacesMessages.instance().add("No existe ninguna UE con los parametros ingresados");
            } else {
                FacesMessages.instance().add("Existen mas de una UE con los parametros ingresados");
                Ues = null;
            }
        }
        return null;
    }

    public String modificar() {
        if (nombre.equals("") || codigo.equals("") || ips.equals("") || codUE.equals("") || zona == 0 || gdq.equals("-")) {
            FacesMessages.instance().add("Debe completar todos los campos.");
            return null;
        }
        if (e == null) {
            FacesMessages.instance().add("Primero debe buscar la Unidad Ejecutora.");
            return null;
        }
        boolean uso_gdq = false;
        if (gdq.equals("SI")) {
            uso_gdq = true;
        }
        e.setRangoIps(ips);
        e.setNombre(nombre);
        e.setCodUE(codUE);
        e.setZona(zona);
        e.setUso_gdq(uso_gdq);
        em.merge(e);
        nombre = codigo = ips = codUE = "";
        zona = 0;
        e = null;
        FacesMessages.instance().add("Se ha modificado correctamente la IP de acceso.");
        return null;
    }

    @Destroy
    @Remove
    public void destroy() {
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getCodUE() {
        return codUE;
    }

    public void setCodUE(String codUE) {
        this.codUE = codUE;
    }

    public String getIps() {
        return ips;
    }

    public void setIps(String ips) {
        this.ips = ips;
    }

    public int getZona() {
        return zona;
    }

    public void setZona(int zona) {
        this.zona = zona;
    }

    public String getGdq() {
        return gdq;
    }

    public void setGdq(String gdq) {
        this.gdq = gdq;
    }
}
