package model;

import java.util.ArrayList;

public class FichaDeInscricao {

    private int id;

    private double cre;

    private String instituicao_de_origem;

    private String curso;

    private double poscomp;

    private boolean tempo_integral;

    private int ano_conclusao;

    private ArrayList cartas;

    private int publicacoes;

    private int monitoria;

    private boolean especializacao;

    private boolean pet;

    public int getAno_conclusao() {
        return ano_conclusao;
    }

    public void setAno_conclusao(int ano_conclusao) {
        this.ano_conclusao = ano_conclusao;
    }

    public ArrayList getCartas() {
        return cartas;
    }

    public void setCartas(ArrayList cartas) {
        this.cartas = cartas;
    }

    public double getCre() {
        return cre;
    }

    public void setCre(double cre) {
        this.cre = cre;
    }

    public String getCurso() {
        return curso;
    }

    public void setCurso(String curso) {
        this.curso = curso;
    }

    public boolean isEspecializacao() {
        return especializacao;
    }

    public void setEspecializacao(boolean especializacao) {
        this.especializacao = especializacao;
    }

    public String getInstituicao_de_origem() {
        return instituicao_de_origem;
    }

    public void setInstituicao_de_origem(String instituicao_de_origem) {
        this.instituicao_de_origem = instituicao_de_origem;
    }

    public int getMonitoria() {
        return monitoria;
    }

    public void setMonitoria(int monitoria) {
        this.monitoria = monitoria;
    }

    public boolean isPet() {
        return pet;
    }

    public void setPet(boolean pet) {
        this.pet = pet;
    }

    public double getPoscomp() {
        return poscomp;
    }

    public void setPoscomp(double poscomp) {
        this.poscomp = poscomp;
    }

    public int getPublicacoes() {
        return publicacoes;
    }

    public void setPublicacoes(int publicacoes) {
        this.publicacoes = publicacoes;
    }

    public boolean isTempo_integral() {
        return tempo_integral;
    }

    public void setTempo_integral(boolean tempo_integral) {
        this.tempo_integral = tempo_integral;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
