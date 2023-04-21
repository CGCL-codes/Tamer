package org.domain.siplacad5.session;

import org.domain.siplacad5.entity.*;
import java.util.ArrayList;
import java.util.List;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

@Name("usuarioHome")
public class UsuarioHome extends EntityHome<Usuario> {

    @In(create = true)
    AutenticacionHome autenticacionHome;

    public void setUsuarioId(Integer id) {
        setId(id);
    }

    public Integer getUsuarioId() {
        return (Integer) getId();
    }

    @Override
    protected Usuario createInstance() {
        Usuario usuario = new Usuario();
        return usuario;
    }

    public void wire() {
        getInstance();
        Autenticacion autenticacion = autenticacionHome.getDefinedInstance();
        if (autenticacion != null) {
            getInstance().setAutenticacion(autenticacion);
        }
    }

    public boolean isWired() {
        if (getInstance().getAutenticacion() == null) return false;
        return true;
    }

    public Usuario getDefinedInstance() {
        return isIdDefined() ? getInstance() : null;
    }

    public List<RolUsuario> getRolUsuarios() {
        return getInstance() == null ? null : new ArrayList<RolUsuario>(getInstance().getRolUsuarios());
    }
}
