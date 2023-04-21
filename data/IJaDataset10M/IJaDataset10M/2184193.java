package br.padroes.gof.estrutural.proxy;

import java.util.ArrayList;
import java.util.List;

public class ClienteProxy {

    public static void main(String[] args) {
        List<Pessoa> pessoas = new ArrayList<Pessoa>();
        pessoas.add(new ProxyPessoa("A"));
        pessoas.add(new ProxyPessoa("B"));
        pessoas.add(new ProxyPessoa("C"));
        System.out.println("Nome: " + pessoas.get(0).getNome());
        System.out.println("Nome: " + pessoas.get(1).getNome());
        System.out.println("Nome: " + pessoas.get(0).getNome());
        System.out.println("Id da 3� - " + pessoas.get(2).getId());
    }
}
