package com.tesisutn.restsoft.persistencia.implementaciones.dominio;

import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import com.tesisutn.restsoft.dominio.direccion.Provincia;
import com.tesisutn.restsoft.persistencia.implementaciones.base.PersistenciaJPA;
import com.tesisutn.restsoft.persistencia.interfaces.dominio.PersistenciaProvincia;

@Repository
public class PersistenciaProvinciaImp extends PersistenciaJPA<Provincia> implements PersistenciaProvincia {

    public PersistenciaProvinciaImp() {
        super(Provincia.class);
    }
}
