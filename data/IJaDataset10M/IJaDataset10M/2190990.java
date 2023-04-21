package br.com.rnavarro.padroes.criacao.builder;

public abstract class MontadorDePizza {

    protected Pizza pizza;

    public Pizza getPizza() {
        return pizza;
    }

    public void prapararMassa() {
        pizza = new Pizza();
    }

    public abstract void montarRecheio();

    public abstract void montarBorda();
}
