/*
 * Desenvolvido para a disciplina Programacao 1
 * Curso de Bacharelado em Ciência da Computação
 * Departamento de Sistemas e Computação
 * Universidade Federal da Paraíba
 *
 * Copyright (C) 1999 Universidade Federal da Paraíba.
 * Não redistribuir sem permissão.
 */
package p1.aplic.banco;

import java.util.*;
import p1.util.*;
import p1.aplic.geral.*;

/**
 * Classe que implementa um extrato.
 * Um extrato é um subconjunto das transações de um movimento
 * que ocorreram entre duas datas.
 *
 * @author   Jacques Philippe Sauvé, jacques@dsc.ufpb.br
 * @version 1.1
 * <br>
 * Copyright (C) 1999 Universidade Federal da Paraíba.
 */
public class Extrato {
	protected Data		dataInicial;
	protected Data		dataFinal;
	protected Movimento	movimento;

    /**
     * Constroi um Extrato.
     * @param dataInicial A data inicial desejada para o extrato.
     * @param dataFinal A data final desejada para o extrato.
     */
	public Extrato(Data dataInicial, Data dataFinal, Movimento movimento) {
		this.dataInicial = dataInicial;
		this.dataFinal = dataFinal;
		this.movimento = movimento;
	}

	/**
	 * Formata o movimento como string.
	 * @return O movimento formatado como string, pronto para impressão.
	 */
	public String formatar() {
		String newLine = System.getProperty("line.separator");
		Formata f1 = new Formata("%-20.20s");
		Formata f2 = new Formata("%7.7d");
		Formata f3 = new Formata("%7.7s");
		Formata f4 = new Formata("%15.15s");
		Formata f5 = new Formata("%s");
		String resultado = "Extrato de conta entre " + dataInicial.DDMMAAAAHHMM() +
				" e " + dataFinal.DDMMAAAAHHMM() + newLine;
		resultado += f1.form("Data") +
				" " + f3.form("Debito") +
				" " + f3.form("Credito") +
				" " + f4.form("Valor") +
				" " + f5.form("Descricao") +
				newLine;
		Iterator it = getTransações();
		while(it.hasNext()) {
			Transacao t = (Transacao)it.next();
			resultado += f1.form(t.getData().DDMMAAAAHHMM()) +
					" " + f2.form(t.getContaDébito().getNúmero()) +
					" " + f2.form(t.getContaCrédito().getNúmero()) +
					" " + f4.form(t.getValorMonetário().toString()) +
					" " + f5.form(t.getDescrição()) +
					newLine;
		}
		// alterar no futuro para que um extrato possa dar um saldo?
		// lembra que tem movimento total da agencia a considerar
		// e isso nao envolve uma conta
		return resultado;
	}


	/**
	 * Fornece um Iterator para varrer as transações do extrato por data.
	 * @return O Iterator.
	 */
	public Iterator getTransações() {
		// um iterador de extrato deve dar a visão de uma parte do iterador
		// do movimento (a parte entre duas datas)
		return new Iterator() {
			Iterator it = movimento.getTransações();
			Object lookAhead = null;

			public boolean hasNext() {
				while(lookAhead == null && it.hasNext()) {
					Transacao la = (Transacao)it.next();
					Data dt = la.getData();
					if(dt.compareTo(dataInicial) < 0) {
						continue; // estamos antes da data inicial
					}
					if(dt.compareTo(dataFinal) > 0) {
						break; // estamos depois da data final
					}
					lookAhead = la; // bingo
				}
				return lookAhead != null;
			}

			public Object next() {
				if(lookAhead == null) {
					hasNext();
				}
				if( lookAhead == null) {
					throw new NoSuchElementException();
				} else {
					Object ret = lookAhead;
					lookAhead = null;
					return ret;
				}
			}

			public void remove() {
				throw new UnsupportedOperationException();
			}
		};
	}
}