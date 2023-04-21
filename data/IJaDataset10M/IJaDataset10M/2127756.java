package org.weras.portal.clientes.client.comum.ui.grupos;

import org.weras.portal.clientes.client.comum.util.StringUtils;
import com.gwtext.client.widgets.Component;
import com.gwtext.client.widgets.form.FieldSet;

public class GrupoUI extends FieldSet {

    public GrupoUI(String labelGrupo) {
        super(labelGrupo);
        setWidth(780);
    }

    protected static boolean existe(String idCampo) {
        return !StringUtils.isEmpty(idCampo);
    }

    protected void adicionar(String idCampo, Component component) {
        if (existe(idCampo)) {
            add(component);
        }
    }
}
