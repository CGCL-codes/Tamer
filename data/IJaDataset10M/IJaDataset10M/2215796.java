package br.com.srv.web.relatorio;

import java.io.Serializable;

public class VelocidadeMediaTO implements Serializable {

    private static final long serialVersionUID = 334354479903830075L;

    private String rotaId;

    private String pontoFisInicial;

    private String pontoFisFinal;

    private String dataInicial;

    private String dataFinal;

    private String horaInicial;

    private String horaFinal;

    private Double distanciaPercorrida;

    private Double tempoPercurso;

    private String descricaoVeiculo;

    private String velocidadeMediaVeiculo;

    public Double getDistanciaPercorrida() {
        return distanciaPercorrida;
    }

    public void setDistanciaPercorrida(Double distanciaPercorrida) {
        this.distanciaPercorrida = distanciaPercorrida;
    }

    public Double getTempoPercurso() {
        return tempoPercurso;
    }

    public void setTempoPercurso(Double tempoPercurso) {
        this.tempoPercurso = tempoPercurso;
    }

    public String getDescricaoVeiculo() {
        return descricaoVeiculo;
    }

    public void setDescricaoVeiculo(String descricaoVeiculo) {
        this.descricaoVeiculo = descricaoVeiculo;
    }

    public String getVelocidadeMediaVeiculo() {
        return velocidadeMediaVeiculo;
    }

    public void setVelocidadeMediaVeiculo(String velocidadeMediaVeiculo) {
        this.velocidadeMediaVeiculo = velocidadeMediaVeiculo;
    }

    public String getDataFinal() {
        return dataFinal;
    }

    public void setDataFinal(String dataFinal) {
        this.dataFinal = dataFinal;
    }

    public String getDataInicial() {
        return dataInicial;
    }

    public void setDataInicial(String dataInicial) {
        this.dataInicial = dataInicial;
    }

    public String getHoraFinal() {
        return horaFinal;
    }

    public void setHoraFinal(String horaFinal) {
        this.horaFinal = horaFinal;
    }

    public String getHoraInicial() {
        return horaInicial;
    }

    public void setHoraInicial(String horaInicial) {
        this.horaInicial = horaInicial;
    }

    public String getPontoFisFinal() {
        return pontoFisFinal;
    }

    public void setPontoFisFinal(String pontoFisFinal) {
        this.pontoFisFinal = pontoFisFinal;
    }

    public String getPontoFisInicial() {
        return pontoFisInicial;
    }

    public void setPontoFisInicial(String pontoFisInicial) {
        this.pontoFisInicial = pontoFisInicial;
    }

    public String getRotaId() {
        return rotaId;
    }

    public void setRotaId(String rotaId) {
        this.rotaId = rotaId;
    }
}
