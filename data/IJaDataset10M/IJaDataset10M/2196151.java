package org.gruposp2p.controldatosgob.model.adaptadorxml;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import org.gruposp2p.controldatosgob.modelo.Etiqueta;

/**
 *
 * @author jj
 */
public class AdaptadorConsejeria extends XmlAdapter<String, Etiqueta> {

    @Override
    public String marshal(Etiqueta etiqueta) throws Exception {
        return String.valueOf(etiqueta.getId());
    }

    @Override
    public Etiqueta unmarshal(String id) throws Exception {
        Etiqueta etiqueta = new Etiqueta();
        etiqueta.setId(Integer.valueOf(id));
        return etiqueta;
    }
}
