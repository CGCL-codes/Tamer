package br.org.eteg.curso.javaoo.capitulo09.colecoes;

import java.util.Stack;

public class ExemploStack {

    /**
	 * @param args
	 */
    public static void main(String[] args) {
        Stack<String> pilha = new Stack<String>();
        pilha.push("10 centavos");
        pilha.push("5 centavos");
        pilha.push("1 centavo");
        pilha.push("25 centavos");
        pilha.push("50 centavos");
        while (!pilha.isEmpty()) {
            System.out.println(pilha.pop());
        }
    }
}
