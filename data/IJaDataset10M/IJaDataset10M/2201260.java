package org.weras.portal.clientes.client.modulos.acampamento;

import org.weras.portal.clientes.client.comum.PesquisaPaginada;
import org.weras.portal.clientes.domain.app.acampamento.Quarto;

public class PesquisaQuarto extends PesquisaPaginada {

    private static final long serialVersionUID = 1L;

    private Quarto quarto;

    public Quarto getQuarto() {
        return quarto;
    }

    public void setQuarto(Quarto quarto) {
        this.quarto = quarto;
    }
}
