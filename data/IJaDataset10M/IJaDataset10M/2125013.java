package ca.petersens.gwt.databinding.rebind;

import static com.google.gwt.core.ext.TreeLogger.ERROR;
import static com.google.gwt.core.ext.TreeLogger.INFO;
import static java.util.Collections.unmodifiableList;
import ca.petersens.gwt.databinding.client.annotation.Calculate;
import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.core.ext.UnableToCompleteException;
import com.google.gwt.core.ext.typeinfo.JMethod;
import com.google.gwt.user.rebind.SourceWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JCalculatedFieldMethod extends JReadableFieldMethod {

    private static Pattern pattern;

    private static Pattern getPattern() {
        if (pattern == null) {
            pattern = Pattern.compile("(?<!\\$)\\$\\{([^}]+)}");
        }
        return pattern;
    }

    private Map<String, List<JAttribute>> expressionMap;

    public JCalculatedFieldMethod(TreeLogger logger, JFormType formType, JMethod method) throws UnableToCompleteException {
        super(logger, formType, method);
    }

    private Map<String, List<JAttribute>> computeExpressionMap(TreeLogger logger) throws UnableToCompleteException {
        Calculate calculate = getCalculateAnnotation();
        Matcher m = getMatcher();
        Map<String, List<JAttribute>> ret = new LinkedHashMap<String, List<JAttribute>>();
        Class<?> rootType = getBeanTypeAsJavaLangClass(logger);
        while (m.find()) {
            String expression = m.group(1);
            ret.put(expression, PropertyDescriptorChain.parse(logger, rootType, expression).getRequiredAttributes());
        }
        if (ret.isEmpty()) {
            logger.log(ERROR, calculate.value() + " has no ${}-delimited sub-sequences");
            throw new UnableToCompleteException();
        } else if (logger.isLoggable(INFO)) {
            logger = logger.branch(INFO, calculate.value() + " has the following ${}-delimited sub-sequences:");
            for (String str : ret.keySet()) {
                logger.log(INFO, str);
            }
        }
        return ret;
    }

    private void generateAutomaticCalculate(TreeLogger logger, SourceWriter sw, Map<String, Integer> attributeIndexes) throws UnableToCompleteException {
        for (String expression : getExpressionMap(logger).keySet()) {
            generateDeclAndReturnIfNull(logger, sw, expression, attributeIndexes.get(expression));
        }
        Matcher m = getMatcher();
        StringBuffer sb = new StringBuffer("return ");
        while (m.find()) {
            m.appendReplacement(sb, "attr" + attributeIndexes.get(m.group(1)));
        }
        m.appendTail(sb);
        sw.print(sb.toString());
        sw.println(";");
    }

    private void generateDeclAndReturnIfNull(TreeLogger logger, SourceWriter sw, String expression, Integer index) throws UnableToCompleteException {
        List<JAttribute> attributeList = getExpressionMap(logger).get(expression);
        JAttribute lastAttribute = attributeList.get(attributeList.size() - 1);
        sw.print(lastAttribute.getPropertyTypeName());
        sw.print(" attr");
        sw.print(index.toString());
        sw.print(" = ");
        sw.print(getTypeCastAttributeAccess(logger, expression, index));
        sw.println(";");
        sw.println();
        sw.print("if (attr");
        sw.print(index.toString());
        sw.println(" == null) {");
        sw.indentln("return null;");
        sw.println("}");
        sw.println();
    }

    private void generateManualCalculate(TreeLogger logger, SourceWriter sw, Map<String, Integer> attributeIndexes) throws UnableToCompleteException {
        Matcher m = getMatcher();
        StringBuffer sb = new StringBuffer("return ");
        while (m.find()) {
            String expression = m.group(1);
            Integer index = attributeIndexes.get(expression);
            m.appendReplacement(sb, getTypeCastAttributeAccess(logger, expression, index));
        }
        m.appendTail(sb);
        sw.print(sb.toString());
        sw.println(";");
    }

    private Calculate getCalculateAnnotation() {
        Calculate ret = getMethod().getAnnotation(Calculate.class);
        assert ret != null;
        return ret;
    }

    private Map<String, List<JAttribute>> getExpressionMap(TreeLogger logger) throws UnableToCompleteException {
        if (expressionMap == null) {
            expressionMap = computeExpressionMap(logger);
        }
        return expressionMap;
    }

    private String getExpressionTypeName(TreeLogger logger, String expression) throws UnableToCompleteException {
        List<JAttribute> attributeList = getExpressionMap(logger).get(expression);
        JAttribute lastAttribute = attributeList.get(attributeList.size() - 1);
        return lastAttribute.getPropertyTypeName();
    }

    private Matcher getMatcher() {
        Calculate calculate = getCalculateAnnotation();
        return getPattern().matcher(calculate.value());
    }

    private String getTypeCastAttributeAccess(TreeLogger logger, String expression, Integer index) throws UnableToCompleteException {
        return "get(bean, " + getExpressionTypeName(logger, expression) + ".class, " + index + ")";
    }

    @Override
    protected List<JAttribute> computeRequiredAttributes(TreeLogger logger) throws UnableToCompleteException {
        Set<JAttribute> ret = new LinkedHashSet<JAttribute>();
        for (List<JAttribute> list : getExpressionMap(logger).values()) {
            ret.addAll(list);
        }
        return unmodifiableList(new ArrayList<JAttribute>(ret));
    }

    @Override
    protected void generateAttributeReference(TreeLogger logger, SourceWriter sw) throws UnableToCompleteException {
        sw.print("calculate(new Calculator<");
        sw.print(getBeanSourceName());
        sw.print(", ");
        sw.print(getValueTypeName());
        sw.print(">(\"");
        sw.print(getName());
        sw.print("\"");
        for (List<JAttribute> attributes : getExpressionMap(logger).values()) {
            sw.print(", ");
            generateConsInvocation(logger, sw, attributes);
        }
        sw.println(") {");
        sw.indent();
        sw.println();
        sw.println("@Override");
        sw.print("public ");
        sw.print(getValueTypeName());
        sw.print(" calculate(");
        sw.print(getBeanSourceName());
        sw.println(" bean) {");
        sw.indent();
        Map<String, Integer> attributeIndexes = new HashMap<String, Integer>();
        int index = 0;
        for (String expression : getExpressionMap(logger).keySet()) {
            attributeIndexes.put(expression, Integer.valueOf(index++));
        }
        switch(getCalculateAnnotation().policy()) {
            case AUTOMATIC:
                generateAutomaticCalculate(logger, sw, attributeIndexes);
                break;
            case MANUAL:
                generateManualCalculate(logger, sw, attributeIndexes);
                break;
            default:
                logger.log(ERROR, "Unexpected IntermediateNullPolicy on " + getMethod() + ": " + getCalculateAnnotation().policy());
                throw new UnableToCompleteException();
        }
        sw.outdent();
        sw.println("}");
        sw.outdent();
        sw.print("})");
    }
}
