package org.wltea.expression.test;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.wltea.expression.ExpressionEvaluator;
import org.wltea.expression.PreparedExpression;
import org.wltea.expression.datameta.Variable;
import org.wltea.expression.datameta.BaseDataMeta.DataType;
import junit.framework.TestCase;

public class VariableTest extends TestCase {

    /**
	 * 简单测试带变量的表达式操作符
	 * @throws Exception
	 */
    public void testOperators() throws Exception {
        System.out.println("testOperators");
        ArrayList<String> expressions = new ArrayList<String>();
        expressions.add("vInt + 2 - 3 * 4 / 5 % 6");
        expressions.add("vString <= \"223\"");
        expressions.add("vDate >= [2008-01-01]");
        expressions.add("223 == vDouble");
        expressions.add("223 == vNull");
        expressions.add("vBoolean != false");
        expressions.add("vNull != null");
        expressions.add("vNull != \"a string\"");
        expressions.add("true && vBoolean");
        expressions.add("vBoolean || false");
        expressions.add("!vBoolean");
        expressions.add("vDate + vBoolean + vInt + vString + vNull  + vDouble + vBoolean");
        expressions.add("false ? true ? vString_p1 : vString_p3 : vBoolean ? vString_p3 : vString_p4 ");
        expressions.add("vString # vBoolean # vInt # vDate # vNull");
        ArrayList<Variable> variables = new ArrayList<Variable>();
        variables.add(Variable.createVariable("vInt", new Integer(-1)));
        variables.add(Variable.createVariable("vString", "12345"));
        variables.add(Variable.createVariable("vDate", new Date()));
        variables.add(Variable.createVariable("vDouble", new Double(223.0)));
        variables.add(Variable.createVariable("vBoolean", new Boolean(true)));
        variables.add(Variable.createVariable("vNull", null));
        variables.add(Variable.createVariable("vString_p1", "路径1"));
        variables.add(Variable.createVariable("vString_p2", "路径2"));
        variables.add(Variable.createVariable("vString_p3", "路径3"));
        variables.add(Variable.createVariable("vString_p4", "路径4"));
        for (String expression : expressions) {
            System.out.println("expression : " + expression);
            Object result = ExpressionEvaluator.evaluate(expression, variables);
            System.out.println("result = " + result);
            System.out.println();
        }
        System.out.println("----------------------------------------------------");
        System.out.println("----------------testOperators over------------------");
        System.out.println("----------------------------------------------------");
    }

    /**
	 * 测试带变量的内部函数
	 * @throws Exception
	 */
    public void testInnerFunctions() throws Exception {
        System.out.println("testInnerFunctions");
        List<String> expressions = new ArrayList<String>();
        expressions.add("$CONTAINS(vString1 ,\"abc\")");
        expressions.add("$CONTAINS(vString2 ,\"abc\")");
        expressions.add("$STARTSWITH(vString2 ,\"abc\")");
        expressions.add("$STARTSWITH(vString3 ,\"abc\")");
        expressions.add("$ENDSWITH(vString2 ,\"abc\")");
        expressions.add("$ENDSWITH(vString3 ,\"bcc\")");
        expressions.add("$CALCDATE(vDate,1,1,1,1,1,1)");
        expressions.add("$CALCDATE(vDate,0,0,0,0,0,0)");
        expressions.add("$CALCDATE(vDate,-1,-1,-1,-1,-1,-1)");
        expressions.add("$CALCDATE(vDate,0,0,0,0,0,60)");
        expressions.add("$CALCDATE(vDate,0,0,0,0,60,0)");
        expressions.add("$CALCDATE(vDate,0,0,0,24,0,0)");
        expressions.add("$CALCDATE(vDate,0,0,31,0,0,0)");
        expressions.add("$CALCDATE(vDate,0,12,0,0,0,0)");
        expressions.add("$DAYEQUALS(vDate,[2008-01-01])");
        List<Variable> variables = new ArrayList<Variable>();
        variables.add(Variable.createVariable("vString1", "aabbcc"));
        variables.add(Variable.createVariable("vString2", "aabcbcc"));
        variables.add(Variable.createVariable("vString3", "abccbcc"));
        variables.add(Variable.createVariable("vDate", new Date()));
        for (String expression : expressions) {
            System.out.println("expression : " + expression);
            Object result = ExpressionEvaluator.evaluate(expression, variables);
            System.out.println("result = " + result);
            System.out.println();
        }
        System.out.println("----------------------------------------------------");
        System.out.println("--------------testInnerFunctions over---------------");
        System.out.println("----------------------------------------------------");
    }

    /**
	 * Hello World Example
	 * @param args
	 */
    public static void main(String[] args) {
        String expression = "$问好(数字类型)";
        List<Variable> variables = new ArrayList<Variable>();
        variables.add(new Variable("数字类型", DataType.DATATYPE_DOUBLE, new Integer(0)));
        PreparedExpression pe = ExpressionEvaluator.preparedCompile(expression, variables);
        System.out.println("Result = " + pe.execute());
        pe.setArgument("数字类型", new Float(100));
        System.out.println("Result = " + pe.execute());
        pe.setArgument("数字类型", new Double(100));
        System.out.println("Result = " + pe.execute());
        Object result = null;
        result = ExpressionEvaluator.evaluate("$问好(1.0)");
        System.out.println("Result = " + result);
        result = ExpressionEvaluator.evaluate("$问好(1)");
        System.out.println("Result = " + result);
        Map<String, Object> vars = new HashMap<String, Object>();
        System.out.println("-----------IK Expression");
        variables = new ArrayList<Variable>();
        Object[] keys = vars.keySet().toArray();
        for (int i = 0; keys != null && i < keys.length; i++) {
            Object key = keys[i];
            variables.add(Variable.createVariable(key.toString(), vars.get(key)));
        }
        result = ExpressionEvaluator.evaluate("mobile!=null", variables);
        Boolean b = (Boolean) result;
        System.out.println(b.booleanValue());
    }
}
