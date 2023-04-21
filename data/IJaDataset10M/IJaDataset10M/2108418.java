package br.com.fiap.prova.bean.prova;

import java.util.ArrayList;

/**
 *
 * @author Vinicius
 */
public class Questao {

    private String enunciado = "";

    private ArrayList<Alternativa> alternativas = new ArrayList<Alternativa>();

    /**
     * Define quanto essa questão vale na prova.
     */
    private double peso;

    public ArrayList<Alternativa> getAlternativas() {
        return alternativas;
    }

    public void setAlternativas(ArrayList<Alternativa> alternativas) {
        this.alternativas = alternativas;
    }

    public String getEnunciado() {
        return enunciado;
    }

    public void setEnunciado(String enunciado) {
        this.enunciado = enunciado;
    }

    public double getPeso() {
        return peso;
    }

    public void setPeso(double peso) {
        this.peso = peso;
    }
}
