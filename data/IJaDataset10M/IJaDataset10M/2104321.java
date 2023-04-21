package org.ziggurat.fenix.web.inicio;

import java.util.List;
import org.apache.wicket.markup.html.IHeaderContributor;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.panel.EmptyPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.ziggurat.fenix.common.database.hibernate.HibProveedorRepository;
import org.ziggurat.fenix.common.modelo.proveedores.Proveedor;
import org.ziggurat.fenix.web.AbstractFenixPage;

/**
 *
 * @author manuel
 */
public class Inicio extends AbstractFenixPage implements IHeaderContributor {

    public Inicio() {
        HibProveedorRepository repoProv = new HibProveedorRepository();
        List<Proveedor> proveedores = repoProv.getProveedoresConDeudaPorMasDeXDias(35);
        if (proveedores.size() != 0) {
            add(new DeudasConProveedoresPanel("deudas", proveedores));
        } else {
            add(new SinDeudasPanel("deudas"));
        }
    }

    @Override
    public Panel getSubMenuPanel() {
        return new EmptyPanel("submenu");
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        response.renderOnLoadJavascript("$('inicioTab').addClassName('current')");
    }
}
