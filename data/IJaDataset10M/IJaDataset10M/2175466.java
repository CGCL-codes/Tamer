package it.dangelo.domjson.impl.path.rules;

import it.dangelo.domjson.JSONArray;
import it.dangelo.domjson.JSONObject;
import it.dangelo.domjson.path.JsonPathException;

public class FunctionRule extends AbstractRule {

    private String functionName;

    public FunctionRule(String functionName) {
        super();
        this.functionName = functionName;
    }

    @Override
    protected PathContext execute(PathContext context) throws JsonPathException {
        if (this.functionName.equals("length")) this._length(context);
        return context;
    }

    private void _length(PathContext context) throws JsonPathException {
        Object result = context.getResults();
        if (result instanceof JSONObject) {
            JSONObject object = (JSONObject) result;
            result = object.gatAttributeNames().length;
        } else if (result instanceof JSONArray) {
            JSONArray array = (JSONArray) result;
            result = array.size();
        }
        context.setResults(result);
    }
}
