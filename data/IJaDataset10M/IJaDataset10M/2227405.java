package net.wgen.op.db;

import net.wgen.op.Op;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author feuerpa
 * @version $Id$
 */
public class NamedParamCallBuilder {

    public static final String REGEX = "(:\\w+[^ :=,)(])";

    private final Map<String, List<Integer>> _nameToParamIdx;

    private final Map<String, Parameter> _nameToParameter;

    private final DatabaseCall _call;

    private final String _originalNamedParameterText;

    private final Parameter[] _parameters;

    public NamedParamCallBuilder(String callText, Op owner, int callType) {
        _originalNamedParameterText = callText;
        _nameToParameter = new LinkedHashMap<String, Parameter>();
        _nameToParamIdx = new LinkedHashMap<String, List<Integer>>();
        StringBuilder qmarkCallText = new StringBuilder(callText.length());
        int numParameters = convertToQMark(callText, qmarkCallText, _nameToParamIdx);
        _call = new DatabaseCall(qmarkCallText.toString(), owner, callType);
        _parameters = new Parameter[numParameters];
    }

    public void registerParameter(String name, Object value) {
        List<Integer> indecies = _nameToParamIdx.get(name);
        if (indecies == null) {
            throw new NullPointerException("indecies not found for parameter named \"" + name + "\" in call " + _originalNamedParameterText);
        }
        Parameter p = (value instanceof Parameter) ? (Parameter) value : new Parameter(value);
        _nameToParameter.put(name, p);
        for (Integer paramIdx : indecies) {
            _parameters[paramIdx - 1] = p;
        }
    }

    public void setParameters() {
        _call.setParameters(_parameters);
    }

    public Parameter getParameter(String name) {
        Parameter p = _nameToParameter.get(name);
        if (p == null) {
            throw new NullPointerException("parameter named \"" + name + "\" not registered yet, or not found in call " + _originalNamedParameterText);
        }
        return p;
    }

    public DatabaseCall getCall() {
        return _call;
    }

    public static int convertToQMark(String namedParamText, StringBuilder qmarkCallText, Map<String, List<Integer>> nameToIdx) {
        qmarkCallText.ensureCapacity(namedParamText.length());
        Matcher m = Pattern.compile(REGEX).matcher(namedParamText);
        int prevE = 0;
        int paramIdx = 0;
        while (m.find()) {
            int s = m.start();
            int e = m.end();
            paramIdx++;
            String skipped = namedParamText.substring(prevE, s);
            qmarkCallText.append(skipped).append('?');
            String paramName = m.group().substring(1);
            List<Integer> indecies = nameToIdx.get(paramName);
            if (indecies == null) nameToIdx.put(paramName, indecies = new ArrayList<Integer>());
            indecies.add(paramIdx);
            prevE = e;
        }
        String tail = namedParamText.substring(prevE);
        qmarkCallText.append(tail);
        return paramIdx;
    }
}
