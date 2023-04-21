package org.uasd.jalgor.model;

import org.uasd.jalgor.business.AlgorSintaxException;
import org.uasd.jalgor.business.InterpreterError;
import org.uasd.jalgor.business.JalgorInterpreter;
import org.uasd.jalgor.business.JalgorInterpreter.AnalizadorLexico;
import org.uasd.jalgor.business.JalgorInterpreter.AnalizadorSemantico;

/**
 *
 * @author Edwin Bratini <edwin.bratini@gmail.com>
 */
public class LeeStatement extends Statement {

    public LeeStatement() throws AlgorSintaxException {
        super(Keyword.LEE);
    }

    public LeeStatement(Keyword tipoSatement, JalgorInterpreter ji) throws AlgorSintaxException {
        super(tipoSatement, ji);
        parseMe();
    }

    private void parseMe() throws AlgorSintaxException {
        JalgorInterpreter ji = getJi();
        AnalizadorLexico al = ji.getAs().getAl();
        AnalizadorSemantico asem = ji.getAs().getAsem();
        Token token = al.getNextToken();
        Token nxtToken = al.getNextToken();
        if (!(token instanceof VariableId)) {
            String msjError = "Identificador esperado";
            al.getCodeLine().addError(new InterpreterError(msjError));
            throw new AlgorSintaxException(msjError);
        }
        if (!asem.variableExiste(token.getValue())) {
            String msjError = "Variable [" + token.getValue() + "] no ha sido declarada";
            al.getCodeLine().addError(new InterpreterError(msjError));
            throw new AlgorSintaxException(msjError);
        }
        if (!(nxtToken instanceof SignoPuntuacion) || (nxtToken instanceof SignoPuntuacion && (!((SignoPuntuacion) nxtToken).getValue().equals(";")))) {
            String msjError = "(;) esperado";
            al.getCodeLine().addError(new InterpreterError(msjError));
            throw new AlgorSintaxException(msjError);
        }
        addTokenStatement(token);
        addTokenStatement(nxtToken);
        setParsedValue(parse());
        if (getParsedValue().indexOf(';') != getParsedValue().lastIndexOf(';')) {
            String msjError = "Fin de linea invalido. Mas de un identificador de fin de linea encontrado.";
            al.getCodeLine().addError(new InterpreterError(msjError));
            throw new AlgorSintaxException(msjError);
        }
    }
}
