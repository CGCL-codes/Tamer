package br.com.fiapbank.mb.caixa;

import javax.annotation.PostConstruct;
import br.com.fiapbank.dominio.Conta;
import br.com.fiapbank.mb.BaseMB;
import br.com.fiapbank.negocio.NegocioException;
import br.com.fiapbank.negocio.caixa.CaixaNegocioFiapBank;
import br.com.fiapbank.negocio.caixa.CaixaNegocio;

/**
 * @author robson
 *
 */
public class CaixaMB extends BaseMB {

    private CaixaNegocio caixa;

    /**
	 * conta que sera realizada o saque / deposito ... 
	 */
    private Conta conta;

    /**
	 * valor do deposito/saque 
	 */
    private Double valor;

    /**
	 * quantidade talao/cartao
	 */
    private Integer quantidade;

    @PostConstruct
    public void init() {
        conta = new Conta();
        caixa = new CaixaNegocioFiapBank();
    }

    public void sacar() {
        try {
            caixa.sacar(conta, valor, conta.getSenha());
            setMsg("Saque realizado com sucesso");
        } catch (NegocioException e) {
            setMsg(e.getMessage());
        }
        clear();
    }

    public void depositar() {
        try {
            caixa.depositar(conta, valor, conta.getSenha());
            setMsg("Deposito realizado com sucesso");
        } catch (NegocioException e) {
            setMsg(e.getMessage());
        }
        clear();
    }

    public void retirarTalao() {
        try {
            caixa.retirarTalao(conta, quantidade, conta.getSenha());
            setMsg("Retirada Autorizada");
        } catch (NegocioException e) {
            setMsg(e.getMessage());
        }
        clear();
    }

    public void retirarCartao() {
        try {
            caixa.retirarCartao(conta, quantidade, conta.getSenha());
            setMsg("Retirada Autorizada");
        } catch (NegocioException e) {
            setMsg(e.getMessage());
        }
        clear();
    }

    public void clear() {
        conta = new Conta();
        valor = null;
        quantidade = null;
    }

    public void clearMsg() {
        setMsg("");
    }

    public Conta getConta() {
        return conta;
    }

    public void setConta(Conta conta) {
        this.conta = conta;
    }

    public Double getValor() {
        return valor;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }
}
