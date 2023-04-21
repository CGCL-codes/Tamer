package br.farn.web3;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Executar aplica��o b�sica para exemplificar as diversas formas de resolver
 * depend�ncias entre objetos, servindo como introdu��o ao framework Spring
 * 
 * @author Daniel Siqueira
 */
public class Main {

    public static void main(String[] args) {
        IComputador computador;
        ApplicationContext ac = new ClassPathXmlApplicationContext("br/farn/web3/applicationContext.xml");
        ApplicationContext acAnnot = new ClassPathXmlApplicationContext("br/farn/web3/applicationContext_annotations.xml");
        Computador.howToResolveDependecy instanciaDireta = Computador.howToResolveDependecy.NEW;
        Computador.howToResolveDependecy instanciaFactory = Computador.howToResolveDependecy.FACTORY;
        computador = new Computador(instanciaDireta);
        computador.ligar();
        computador = new Computador(instanciaFactory);
        computador.ligar();
        computador = (ComputadorSpring) ac.getBean("computador");
        computador.ligar();
        computador = (ComputadorSpringAnnotations) acAnnot.getBean("computadorSpringAnnotations");
        computador.ligar();
    }
}
