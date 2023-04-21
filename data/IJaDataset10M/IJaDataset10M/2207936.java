package main.actions;

import javax.ejb.EJB;
import javax.ejb.Remove;
import javax.ejb.Stateful;
import main.controladores.IIngresoSalaController;
import main.rim.entity.PlaceBean;
import org.jboss.seam.annotations.Create;
import org.jboss.seam.annotations.Destroy;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.log.Log;

@Stateful
@Name("altaSala")
public class IngresoSalaAction implements IIngresoSalaAction {

    @Logger
    private Log log;

    String nombre;

    @EJB
    private IIngresoSalaController logica;

    @Create
    public String nuevo() {
        log.info("IngresoSalaAction NUEVO");
        return "ok";
    }

    public String alta() {
        if (nombre.compareTo("") == 0) {
            FacesMessages.instance().add("Debe ingresar todos los datos");
            return null;
        }
        boolean existe = logica.existeSala(nombre);
        if (existe) {
            FacesMessages.instance().add("Existe en el sistema una Sala con el nombre ingresado");
            return null;
        }
        PlaceBean place = new PlaceBean();
        place.setName(nombre);
        place.setIi_extension("extension_" + nombre);
        logica.persistirSala(place);
        FacesMessages.instance().add("Se ha ingresado la informaci�n exitosamente");
        nombre = "";
        return "ok";
    }

    @Destroy
    @Remove
    public void destroy() {
        log.info("IngresoSalaAction ELIMINADO");
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
