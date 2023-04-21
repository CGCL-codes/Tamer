package br.ufmg.lcc.pcollecta.model;

import java.util.Map;
import br.ufmg.lcc.arangi.commons.BasicException;

public abstract class ScriptExecuter {

    public abstract void addMethod(String method) throws BasicException;

    public abstract void fillVariables(Map<String, Object> registers) throws BasicException;

    public abstract void execute(String methodName) throws BasicException;

    public abstract Object get(String variableName) throws BasicException;

    public abstract void set(String variableName, Object value) throws BasicException;
}
