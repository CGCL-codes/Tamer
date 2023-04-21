package br.usp.ime.protoc.neuroimagem;

import com.google.gwt.user.client.rpc.IsSerializable;

public class TipoProcessamento implements IsSerializable {

    private String descricao;

    public String getDescricao() {
        return this.descricao;
    }

    public void setDescricao(String value) {
        if (this.descricao != value) {
            this.descricao = value;
        }
    }
}
