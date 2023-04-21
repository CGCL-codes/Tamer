package com.renangomes.fluxo.utils;

import com.renangomes.fluxo.utils.Linguagem.Token;

/**
 * Esta Classe representa o Linha
 * @since 09/09/2011
 * @author Renan Gomes Barreto
 */
public class Linha {

    /** A string encontrada no codigo*/
    public String token;

    /** O Tipo de Token */
    public Token tipo;

    /** o numero da linha que o token se encontra */
    public int numeroDaLinha;

    public int caractereInicial = 0;

    /** O contrutor padrão da classe Linha */
    public Linha(String l, Token t, int n, int ci) {
        token = l;
        tipo = t;
        numeroDaLinha = n;
        caractereInicial = ci;
    }

    /**
     * Tranforma a linha em uma string bem espaçada
     * (Essa função é usada apenas para mostrar na tela a tabela de linhas)
     * @return Uma linha com tamanho fixo e com os dados da linha
     */
    @Override
    public String toString() {
        String saida = Integer.toString(numeroDaLinha);
        for (int i = saida.length(); i <= 10; i++) {
            saida += ' ';
        }
        saida += token;
        for (int i = saida.length(); i <= 30; i++) {
            saida += ' ';
        }
        saida += tipo;
        for (int i = saida.length(); i <= 50; i++) {
            saida += ' ';
        }
        return saida;
    }
}
