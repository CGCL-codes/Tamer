package org.gruposp2p.controldatosgob.model.adaptadorxml;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import org.gruposp2p.controldatosgob.modelo.ConjuntoDeDatos;

/**
 *
 * @author jj
 */
public class AdaptadorComunidad extends XmlAdapter<String, ConjuntoDeDatos> {

    @Override
    public String marshal(ConjuntoDeDatos conjuntoDeDatos) throws Exception {
        return String.valueOf(conjuntoDeDatos.getId());
    }

    @Override
    public ConjuntoDeDatos unmarshal(String id) throws Exception {
        ConjuntoDeDatos conjuntoDeDatos = new ConjuntoDeDatos();
        conjuntoDeDatos.setId(Integer.valueOf(id));
        return conjuntoDeDatos;
    }
}
