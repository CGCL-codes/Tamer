package com.antares.sirius.filter;

import com.antares.commons.filter.Filter;
import com.antares.sirius.model.Regla;
import com.antares.sirius.model.Usuario;

/**
 * Filter para la endidad Regla.
 *
 * @version 1.0.0 Created 23/04/2011 by Julian Martinez
 * @author <a href:mailto:otakon@gmail.com> Julian Martinez </a>
 *
 * @param <T> clase de la entidad de modelo
 */
public class ReglaFilter extends Filter<Regla> {

    private Usuario usuario;

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
}
