package br.faimg.pomar.modelo.pojo;

/**
 *
 * @author flavio
 */
public class RotaEstacao {

    private Estacao estacaoAnterior = null;

    private Estacao estacaoProxima = null;

    private RotaProduto rotaProduto = null;

    private Integer squencia = null;

    public RotaProduto getRotaProduto() {
        return rotaProduto;
    }

    public void setRotaProduto(RotaProduto rotaProduto) {
        this.rotaProduto = rotaProduto;
    }

    public RotaEstacao() {
        estacaoAnterior = new Estacao();
        estacaoProxima = new Estacao();
    }

    public Estacao getEstacaoAnterior() {
        return estacaoAnterior;
    }

    public void setEstacaoAnterior(Estacao estacaoAnterior) {
        this.estacaoAnterior = estacaoAnterior;
    }

    public Estacao getEstacaoProxima() {
        return estacaoProxima;
    }

    public void setEstacaoProxima(Estacao estacaoProxima) {
        this.estacaoProxima = estacaoProxima;
    }

    public Integer getSquencia() {
        return squencia;
    }

    public void setSquencia(Integer squencia) {
        this.squencia = squencia;
    }
}
