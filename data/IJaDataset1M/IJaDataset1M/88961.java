package org.ostion.siplacad2.session;

import org.ostion.siplacad2.entity.*;
import java.util.ArrayList;
import java.util.List;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

@Name("flujoHome")
public class FlujoHome extends EntityHome<Flujo> {

    @In(create = true)
    CarreraEspecializacionHome carreraEspecializacionHome;

    public void setFlujoId(Integer id) {
        setId(id);
    }

    public Integer getFlujoId() {
        return (Integer) getId();
    }

    @Override
    protected Flujo createInstance() {
        Flujo flujo = new Flujo();
        return flujo;
    }

    public void wire() {
        getInstance();
        CarreraEspecializacion carreraEspecializacion = carreraEspecializacionHome.getDefinedInstance();
        if (carreraEspecializacion != null) {
            getInstance().setCarreraEspecializacion(carreraEspecializacion);
        }
    }

    public boolean isWired() {
        if (getInstance().getCarreraEspecializacion() == null) return false;
        return true;
    }

    public Flujo getDefinedInstance() {
        return isIdDefined() ? getInstance() : null;
    }

    public List<FlujoMateria> getFlujoMaterias() {
        return getInstance() == null ? null : new ArrayList<FlujoMateria>(getInstance().getFlujoMaterias());
    }

    public List<CarreraEspecializacion> getCarreraEspecializacions() {
        return getInstance() == null ? null : new ArrayList<CarreraEspecializacion>(getInstance().getCarreraEspecializacions());
    }
}
