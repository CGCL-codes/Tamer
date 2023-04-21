package br.com.srv.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class RotaTO implements Serializable {

    private static final long serialVersionUID = 1216235334661856051L;

    private Integer id;

    private String descricao;

    private String codigo;

    private String nome;

    private Integer clienteId;

    private Set<PontoTO> pontos;

    private Set<PontoTO> pontosFiscalizacao;

    private Set<VeiculoTO> veiculos;

    private Boolean status;

    private Short tolerancia;

    public Short getTolerancia() {
        return tolerancia;
    }

    public void setTolerancia(Short tolerancia) {
        this.tolerancia = tolerancia;
    }

    public Set<PontoTO> getPontosFiscalizacao() {
        return pontosFiscalizacao;
    }

    public void setPontosFiscalizacao(Set<PontoTO> pontosFiscalizacao) {
        this.pontosFiscalizacao = pontosFiscalizacao;
    }

    public void addPontoFiscalizacao(PontoTO ponto) {
        if (pontosFiscalizacao == null) {
            pontosFiscalizacao = new HashSet<PontoTO>();
        }
        pontosFiscalizacao.add(ponto);
    }

    public Integer getClienteId() {
        return clienteId;
    }

    public void setClienteId(Integer clienteId) {
        this.clienteId = clienteId;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Set<PontoTO> getPontos() {
        return pontos;
    }

    public void setPontos(Set<PontoTO> pontos) {
        this.pontos = pontos;
    }

    public void addPonto(PontoTO ponto) {
        if (pontos == null) {
            pontos = new HashSet<PontoTO>();
        }
        pontos.add(ponto);
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public Set<VeiculoTO> getVeiculos() {
        return veiculos;
    }

    public void setVeiculos(Set<VeiculoTO> veiculos) {
        this.veiculos = veiculos;
    }

    public void addVeiculos(VeiculoTO veiculoTO) {
        if (veiculos == null) {
            veiculos = new HashSet<VeiculoTO>();
        }
        veiculos.add(veiculoTO);
    }
}
