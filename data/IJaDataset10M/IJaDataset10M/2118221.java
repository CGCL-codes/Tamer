package plp.imperative1.memory;

import plp.expressions2.expression.Valor;
import plp.expressions2.memory.AmbienteExecucao;
import plp.expressions2.memory.VariavelNaoDeclaradaException;
import plp.imperative2.expression.Identificavel;

public interface AmbienteExecucaoImperativa extends AmbienteExecucao {

    public void changeValor(Identificavel id, Valor valorId) throws VariavelNaoDeclaradaException;

    public Valor read() throws EntradaVaziaException;

    public void write(Valor v);

    public ListaValor getSaida();
}
