package org.ziggurat.fenix.web.cheques;

import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;
import org.ziggurat.fenix.common.modelo.transacciones.EstadoDeCheque;

public class SubMenuPanelCheques extends Panel {

    public SubMenuPanelCheques(String id) {
        super(id);
        add(new Link("nuevoCheque") {

            @Override
            public void onClick() {
                setResponsePage(NuevoCheque.class);
            }
        });
        add(new Link("listaChequesEnPosesion") {

            @Override
            public void onClick() {
                setResponsePage(ListadoDeCheques.class);
            }
        });
        add(new Link("listaChequesEntregados") {

            @Override
            public void onClick() {
                setResponsePage(new ListadoDeCheques(EstadoDeCheque.ENTREGADO_A_PROVEEDOR));
            }
        });
        add(new Link("listaChequesDepositados") {

            @Override
            public void onClick() {
                setResponsePage(new ListadoDeCheques(EstadoDeCheque.DEPOSITADO));
            }
        });
    }
}
