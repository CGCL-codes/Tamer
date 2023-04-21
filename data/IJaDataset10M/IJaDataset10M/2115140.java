package br.com.rnavarro.padroes.comportamental.observer;

public class Chefe implements Observador {

    public void atualizar(String operacao, String registro) {
        System.out.println("Chefe notou que um " + operacao + " foi realizado na " + registro);
    }
}
