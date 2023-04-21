package com.tesisutn.restsoft.persistencia.implementaciones.dominio;

import org.springframework.stereotype.Repository;
import com.tesisutn.restsoft.dominio.articulo.UnidadDeMedida;
import com.tesisutn.restsoft.persistencia.implementaciones.base.PersistenciaJPA;
import com.tesisutn.restsoft.persistencia.interfaces.dominio.PersistenciaUnidadDeMedida;

@Repository
public class PersistenciaUnidadDeMedidaImp extends PersistenciaJPA<UnidadDeMedida> implements PersistenciaUnidadDeMedida {

    public PersistenciaUnidadDeMedidaImp() {
        super(UnidadDeMedida.class);
    }
}
