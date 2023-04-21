package org.domain.siplacad5.session;

import org.domain.siplacad5.entity.*;
import java.util.ArrayList;
import java.util.List;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

@Name("areaHome")
public class AreaHome extends EntityHome<Area> {

    @In(create = true)
    RolHome rolHome;

    @In(create = true)
    UnidadHome unidadHome;

    public void setAreaId(Integer id) {
        setId(id);
    }

    public Integer getAreaId() {
        return (Integer) getId();
    }

    @Override
    protected Area createInstance() {
        Area area = new Area();
        return area;
    }

    public void wire() {
        getInstance();
        Rol rol = rolHome.getDefinedInstance();
        if (rol != null) {
            getInstance().setRol(rol);
        }
        Unidad unidad = unidadHome.getDefinedInstance();
        if (unidad != null) {
            getInstance().setUnidad(unidad);
        }
    }

    public boolean isWired() {
        if (getInstance().getRol() == null) return false;
        if (getInstance().getUnidad() == null) return false;
        return true;
    }

    public Area getDefinedInstance() {
        return isIdDefined() ? getInstance() : null;
    }

    public List<AreaMateria> getAreaMaterias() {
        return getInstance() == null ? null : new ArrayList<AreaMateria>(getInstance().getAreaMaterias());
    }
}
