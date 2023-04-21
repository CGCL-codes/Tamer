package com.proyecto.tropero.gui.interfaces;

import java.util.List;
import com.proyecto.tropero.core.domain.Permiso;
import com.proyecto.tropero.core.domain.TipoUsuario;

public interface IVentanaABMTiposUsuario {

    public void setListTiposUsuario(List<TipoUsuario> listTiposUsuario);

    public TipoUsuario getTipoUsuarioSeleccionado();

    public List<Permiso> getListPermisosDentro();

    public void setListPermisosDentro(List<Permiso> listPermisosDentro);

    public List<Permiso> getListPermisosFuera();

    public void setListPermisosFuera(List<Permiso> listPermisosFuera);

    public String getDescripcionTipo();
}
