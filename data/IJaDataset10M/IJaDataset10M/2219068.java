package plp.imperative2.command;

import plp.expressions1.util.Tipo;
import plp.expressions2.expression.Expressao;
import plp.expressions2.expression.Id;
import plp.expressions2.memory.IdentificadorJaDeclaradoException;
import plp.expressions2.memory.IdentificadorNaoDeclaradoException;
import plp.expressions2.memory.VariavelJaDeclaradaException;
import plp.expressions2.memory.VariavelNaoDeclaradaException;
import plp.imperative1.command.Comando;
import plp.imperative1.memory.AmbienteCompilacaoImperativa;
import plp.imperative1.memory.AmbienteExecucaoImperativa;
import plp.imperative1.memory.EntradaVaziaException;
import plp.imperative1.memory.ListaValor;
import plp.imperative2.declaration.DefProcedimento;
import plp.imperative2.declaration.ListaDeclaracaoParametro;
import plp.imperative2.expression.ValorListaEncadeada;
import plp.imperative2.memory.AmbienteExecucaoImperativa2;
import plp.imperative2.util.Nodo;
import plp.imperative2.util.TipoProcedimento;

public class InsertLista implements Comando {

    private Id idLista;

    private Expressao exp1;

    public InsertLista(Id nomeProcedimento, Expressao parametrosReais) {
        this.idLista = nomeProcedimento;
        this.exp1 = parametrosReais;
    }

    /**
	 * 
	 */
    public AmbienteExecucaoImperativa executar(AmbienteExecucaoImperativa amb) throws IdentificadorNaoDeclaradoException, IdentificadorJaDeclaradoException, EntradaVaziaException {
        AmbienteExecucaoImperativa2 ambiente = (AmbienteExecucaoImperativa2) amb;
        ValorListaEncadeada valorLista = (ValorListaEncadeada) ambiente.get(idLista);
        valorLista.valor().addLast(new Nodo(exp1.avaliar(ambiente)));
        return ambiente;
    }

    /**
	 * Adota-se que a ListaEncadeada é heterogênea
	 * 
	 * @param ambiente
	 *            o ambiente que contem o mapeamento entre identificadores e
	 *            tipos.
	 * @return <code>true</code> se a chamada de procedimeno est� bem tipada;
	 *         <code>false</code> caso contrario.
	 */
    public boolean checaTipo(AmbienteCompilacaoImperativa amb) throws IdentificadorJaDeclaradoException, IdentificadorNaoDeclaradoException {
        Tipo tipo = amb.get(this.idLista);
        return tipo.eList();
    }
}
