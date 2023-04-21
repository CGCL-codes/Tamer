package org.weras.portal.clientes.client.modulos.insejeccg.congregacao;

import org.weras.portal.clientes.client.comum.Formulario;
import org.weras.portal.clientes.client.comum.ui.combobox.ComboBoxDinamico;
import org.weras.portal.clientes.domain.igreja.Congregacao;
import org.weras.portal.clientes.domain.igreja.Congregacoes;

public class ComboBoxCongregacao extends ComboBoxDinamico<Congregacao> {

    public ComboBoxCongregacao(Formulario<?, ?> formulario, String idForm, String etiqueta, String codigo, boolean obrigatorio, String textoVazio) {
        super(formulario, idForm, null, etiqueta, codigo, obrigatorio, textoVazio, 10);
    }

    protected String[] converte(Congregacao congregacao) {
        return new String[] { congregacao.getId().toString(), congregacao.getUnidade().getNomeUnidade() };
    }

    protected String getNomeEstrategiaPesquisa() {
        return Congregacoes.ESTRATEGIA_PESQUISA;
    }
}
