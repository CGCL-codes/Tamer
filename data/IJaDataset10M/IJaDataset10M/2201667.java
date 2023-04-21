package it.dangelo.domjson.impl.path.rules;

import it.dangelo.domjson.JSONElement;
import it.dangelo.domjson.JSONPrincipalElement;

public class PathContext {

    private JSONElement actualElment;

    private JSONPrincipalElement startElement;

    private Object results;

    public PathContext(JSONPrincipalElement principalElement) {
        this.startElement = principalElement;
    }

    public JSONPrincipalElement getStartElement() {
        return startElement;
    }

    public JSONElement getActualElment() {
        return actualElment;
    }

    public void setActualElment(JSONElement actualElment) {
        this.actualElment = actualElment;
    }

    public Object getResults() {
        return results;
    }

    public void setResults(Object results) {
        this.results = results;
    }
}
