package plp.imperative2.expression;

import plp.expressions2.expression.Expressao;
import plp.expressions2.expression.Id;

public interface Identificavel extends Expressao {

    public Id getIdentificador();
}
