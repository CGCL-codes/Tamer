package br.com.rnavarro.padroes.criacao.abstractfactory;

public class BarraDeRolagemWindows implements BarraDeRolagem {

    public void desenhar() {
        System.out.println("Barra de rolagem para Windows criada.");
    }

    public void rolar() {
        System.out.println("Rolando barra de rolagem do Windows.");
    }
}
