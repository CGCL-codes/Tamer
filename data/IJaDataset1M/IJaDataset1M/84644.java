package br.com.dotec.model;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;

@Entity
public class Caixa extends Elemento {

    @NotNull(message = "{validator.notEmpty}")
    @OneToMany(cascade = { CascadeType.ALL })
    private List<Movimentacao> movimentacoes = new ArrayList<Movimentacao>();

    private StatusDaCaixa status = StatusDaCaixa.DOTEC;

    public List<Movimentacao> getMovimentacoes() {
        return movimentacoes;
    }

    public void setMovimentacoes(List<Movimentacao> movimentacoes) {
        this.movimentacoes = movimentacoes;
    }

    public StatusDaCaixa getStatus() {
        return status;
    }

    public void setStatus(StatusDaCaixa status) {
        this.status = status;
    }

    @Override
    public TipoDeElemento getTipoDeElemento() {
        return TipoDeElemento.CAIXA;
    }
}
