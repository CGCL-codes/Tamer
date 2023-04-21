package org.spockframework.runtime;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.spockframework.runtime.model.FeatureInfo;
import org.spockframework.util.GroovyRuntimeUtil;

/**
 * @author Peter Niederwieser
 */
public class UnrolledFeatureNameGenerator {

    private static final Pattern VARIABLE_PATTERN = Pattern.compile("#([a-zA-Z_\\$][\\w\\$]*)");

    private final FeatureInfo feature;

    private final Matcher variableMatcher;

    private final Map<String, Integer> parameterNameToPosition = new HashMap<String, Integer>();

    private int iterationCount;

    public UnrolledFeatureNameGenerator(FeatureInfo feature, String namePattern) {
        this.feature = feature;
        variableMatcher = VARIABLE_PATTERN.matcher(namePattern);
        int pos = 0;
        for (String name : feature.getParameterNames()) parameterNameToPosition.put(name, pos++);
    }

    public String nameFor(Object[] args) {
        StringBuffer result = new StringBuffer();
        variableMatcher.reset();
        while (variableMatcher.find()) {
            String variableName = variableMatcher.group(1);
            String value = getValue(variableName, args);
            variableMatcher.appendReplacement(result, Matcher.quoteReplacement(value));
        }
        variableMatcher.appendTail(result);
        iterationCount++;
        return result.toString();
    }

    private String getValue(String variableName, Object[] args) {
        if (variableName.equals("featureName")) return feature.getName();
        if (variableName.equals("iterationCount")) return String.valueOf(iterationCount);
        Integer pos = parameterNameToPosition.get(variableName);
        if (pos == null) return "#" + variableName;
        Object arg = args[pos];
        try {
            return GroovyRuntimeUtil.toString(arg);
        } catch (Throwable t) {
            return "#" + variableName;
        }
    }
}
