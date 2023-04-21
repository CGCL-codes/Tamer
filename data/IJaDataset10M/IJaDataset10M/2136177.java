package org.jsmg.pmd.rules;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertNull;
import java.io.FileNotFoundException;
import java.io.Reader;
import java.io.StringReader;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import net.sourceforge.pmd.PMD;
import net.sourceforge.pmd.PMDException;
import net.sourceforge.pmd.RuleContext;
import net.sourceforge.pmd.RuleSet;
import net.sourceforge.pmd.SourceType;
import net.sourceforge.pmd.util.designer.DFAGraphRule;
import org.jsmg.model.ExecutionPath;
import org.jsmg.model.ExecutionPathNode;
import org.jsmg.model.Field;
import org.jsmg.model.JavaSourceClassModel;
import org.jsmg.model.Literal;
import org.jsmg.model.Method;
import org.jsmg.model.MethodInvocation;
import org.jsmg.model.Scope;
import org.jsmg.model.UnresolvedArgument;
import org.jsmg.model.ValueHolder;
import org.jsmg.model.Variable;
import org.junit.Before;
import org.junit.Test;

public class TestJavaSourceModelGeneration {

    /**
	 * Class to be tested.
	 */
    private TestClassGenerationRule testClassGenerationRule;

    /**
	 * Pmd engine.
	 */
    private PMD pmd;

    /**
	 * Rule set used in test.
	 */
    private RuleSet ruleSet;

    /**
	 * Rule context.
	 */
    private RuleContext ruleContext;

    /**
	 * Prepares for next test.
	 */
    @Before
    public void setUp() {
        testClassGenerationRule = new TestClassGenerationRule();
        pmd = new PMD();
        pmd.setJavaVersion(SourceType.JAVA_16);
        ruleSet = new RuleSet();
        ruleSet.addRule(getFlowAnalysisRule());
        ruleSet.addRule(testClassGenerationRule);
        ruleContext = new RuleContext();
        ruleContext.setSourceCodeFilename("[no filename]");
    }

    /**
	 * Returns rule responsible for flow analysis.
	 * @return rule
	 */
    private DFAGraphRule getFlowAnalysisRule() {
        final DFAGraphRule dfaGraphRule = new DFAGraphRule();
        return dfaGraphRule;
    }

    /**
	 * Code Sample:
		public class ClassSource {
		    private Type fieldName;
		}
	 * Tests if the field is correctly retrieved.
	 * @throws PMDException error verifying rule
	 * @throws FileNotFoundException error
	 */
    @Test
    public void testClassField() throws PMDException, FileNotFoundException {
        final String sourceCode = "public class ClassSource { " + "private Type fieldName; " + "} ";
        processSourceCode(sourceCode);
        final JavaSourceClassModel javaSourceClassModel = testClassGenerationRule.getJavaSourceClassModel();
        assertEquals(1, javaSourceClassModel.getFields().size());
        final Field field = javaSourceClassModel.getField("fieldName");
        assertEquals("fieldName", field.getVariableName());
        assertEquals("Type", field.getFieldType());
    }

    /**
	 * Tests generics type are correcty identified.
	 * @throws PMDException error verifying rule
	 * @throws FileNotFoundException error
	 */
    @Test
    public void testGenericType() throws PMDException, FileNotFoundException {
        final String sourceCode = "public class ClassSource { " + "private Map<String, Integer> list; " + "public List<NameType> getNames(){return null;}" + "} ";
        processSourceCode(sourceCode);
        final JavaSourceClassModel javaSourceClassModel = testClassGenerationRule.getJavaSourceClassModel();
        assertEquals(1, javaSourceClassModel.getFields().size());
        final Field field = javaSourceClassModel.getField("list");
        assertEquals("list", field.getVariableName());
        assertEquals("Map<String, Integer>", field.getFieldType());
        final Method method = javaSourceClassModel.getMethod("getNames");
        assertEquals("List<NameType>", method.getReturnType());
    }

    /**
	 * Tests generics type are correcty identified in parameters.
	 * @throws PMDException error verifying rule
	 * @throws FileNotFoundException error
	 */
    @Test
    public void testGenericsInArguments() throws PMDException, FileNotFoundException {
        final String sourceCode = "public class Teste {" + "public void excluirReservas(Set<ReservaSala> reservasSala) { " + "reservaLocalProvaBusiness.excluirReservas(reservasSala); " + "} " + "}";
        processSourceCode(sourceCode);
        final JavaSourceClassModel javaSourceClassModel = testClassGenerationRule.getJavaSourceClassModel();
        final Method method = javaSourceClassModel.getMethod("excluirReservas");
        assertEquals(1, method.getExecutionsPath().size());
        final ExecutionPath executionPath = method.getExecutionsPath().iterator().next();
        assertEquals(1, executionPath.getReadVariables().size());
        final Variable readVariable = executionPath.getReadVariables().iterator().next();
        assertEquals("Set<ReservaSala>", readVariable.getType());
        assertEquals("reservasSala", readVariable.getVariableId());
    }

    /**
	 * Tests if the vars are correctly identified as being read on the execution flow.
	 * @throws PMDException error verifying rule
	 * @throws FileNotFoundException error
	 */
    @Test
    public void testGetVariablesReadInConditionalExpressions() throws PMDException, FileNotFoundException {
        final String sourceCode = "public class ClassSource {\n" + "boolean classVariableIf = false;\n" + "public void isInConditionalExpression() {\n" + "boolean localVariableWhile = false;\n" + "if(this.classVariableIf){}\n" + "while(localVariableWhile){}\n" + "for(int localVariableFor=0; localVariableFor < 10; localVariableFor++){}\n" + "List<String> listForEach;\n" + "for(String localVariableForEach : listForEach){}\n" + "}" + "};";
        System.out.println(sourceCode);
        final Set<Variable> expectedVariablesRead = new HashSet<Variable>(3);
        final Variable localVariableIf = new Variable();
        localVariableIf.setScope(Scope.CLASS_SCOPE);
        localVariableIf.setType("boolean");
        localVariableIf.setVariableId("classVariableIf");
        expectedVariablesRead.add(localVariableIf);
        final Variable localVariableWhile = new Variable();
        localVariableWhile.setScope(Scope.LOCAL_SCOPE);
        localVariableWhile.setType("boolean");
        localVariableWhile.setVariableId("localVariableWhile");
        expectedVariablesRead.add(localVariableWhile);
        final Variable localVariableFor = new Variable();
        localVariableFor.setScope(Scope.LOCAL_SCOPE);
        localVariableFor.setType("int");
        localVariableFor.setVariableId("localVariableFor");
        expectedVariablesRead.add(localVariableFor);
        final Variable listForEach = new Variable();
        listForEach.setScope(Scope.LOCAL_SCOPE);
        listForEach.setType("List<String>");
        listForEach.setVariableId("listForEach");
        expectedVariablesRead.add(listForEach);
        processSourceCode(sourceCode);
        final JavaSourceClassModel javaSourceClassModel = testClassGenerationRule.getJavaSourceClassModel();
        final Method method = javaSourceClassModel.getMethod("isInConditionalExpression");
        final ExecutionPath executionPath = method.getExecutionsPath().iterator().next();
        final Set<Variable> readVariables = executionPath.getReadVariables();
        assertEquals(expectedVariablesRead.size(), readVariables.size());
        assertEquals(expectedVariablesRead, readVariables);
    }

    /**
	 * Tests if the vars read in return statements are correctly identified as being read on the execution flow.
	 * @throws PMDException error verifying rule
	 * @throws FileNotFoundException error
	 */
    @Test
    public void testGetVariablesReadInArguments() throws PMDException, FileNotFoundException {
        final String sourceCode = "public class ClassSource {" + "private Field field;" + "private Field fieldTwo;" + "public void varsReadInArguments(Set<Type> param) {" + "fieldTwo.someMethod(param + 1, param, this.field);" + "}" + "};";
        final Set<Variable> expectedVariablesRead = new HashSet<Variable>(3);
        final Variable paramVariable = new Variable();
        paramVariable.setScope(Scope.METHOD_SCOPE);
        paramVariable.setType("Set<Type>");
        paramVariable.setVariableId("param");
        expectedVariablesRead.add(paramVariable);
        final Variable fieldVariable = new Variable();
        fieldVariable.setScope(Scope.CLASS_SCOPE);
        fieldVariable.setType("Field");
        fieldVariable.setVariableId("field");
        expectedVariablesRead.add(fieldVariable);
        final Variable fieldTwoVariable = new Variable();
        fieldTwoVariable.setScope(Scope.CLASS_SCOPE);
        fieldTwoVariable.setType("Field");
        fieldTwoVariable.setVariableId("fieldTwo");
        expectedVariablesRead.add(fieldTwoVariable);
        processSourceCode(sourceCode);
        final JavaSourceClassModel javaSourceClassModel = testClassGenerationRule.getJavaSourceClassModel();
        final Method method = javaSourceClassModel.getMethod("varsReadInArguments");
        final ExecutionPath executionPath = method.getExecutionsPath().iterator().next();
        final Set<Variable> readVariables = executionPath.getReadVariables();
        final List<MethodInvocation> methodInvocations = executionPath.getInternalMethodInvocations();
        assertEquals(1, methodInvocations.size());
        final List<ValueHolder> argumentsList = methodInvocations.get(0).getArgumentsList();
        assertEquals(3, argumentsList.size());
        assertTrue(argumentsList.get(0) instanceof UnresolvedArgument);
        assertEquals(paramVariable, argumentsList.get(1));
        assertEquals(fieldVariable, argumentsList.get(2));
        assertEquals(3, readVariables.size());
        assertEquals(expectedVariablesRead, readVariables);
    }

    private void processSourceCode(final String sourceCode) throws PMDException {
        final Reader testFile = new StringReader(sourceCode);
        pmd.processFile(testFile, ruleSet, ruleContext);
    }

    /**
	 * Code Sample:
		public class ClassSource {
			private Var fieldVariable;
		    public boolean isTrue(String methodVariable) {
		    	boolean localVariable = false;
		    	return fieldVariable != null || methodVariable == null &&  !localVariable;
		    }
		}
	 * Tests if the vars read in return statements are correctly identified as being read on the execution flow.
	 * @throws PMDException error verifying rule
	 * @throws FileNotFoundException error
	 */
    @Test
    public void testGetVariablesReadInReturnStatements() throws PMDException, FileNotFoundException {
        final String sourceCode = "public class ClassSource {" + "private Var fieldVariable;" + "public boolean isTrue(String methodVariable) {" + "boolean localVariable = false;" + "return this.fieldVariable != null || methodVariable == null &&  !localVariable;" + "}" + "};";
        final Set<Variable> expectedVariablesRead = new HashSet<Variable>(3);
        final Variable fieldVariable = new Variable();
        fieldVariable.setScope(Scope.CLASS_SCOPE);
        fieldVariable.setType("Var");
        fieldVariable.setVariableId("fieldVariable");
        expectedVariablesRead.add(fieldVariable);
        final Variable methodVariable = new Variable();
        methodVariable.setScope(Scope.METHOD_SCOPE);
        methodVariable.setType("String");
        methodVariable.setVariableId("methodVariable");
        expectedVariablesRead.add(methodVariable);
        final Variable localVariable = new Variable();
        localVariable.setScope(Scope.LOCAL_SCOPE);
        localVariable.setType("boolean");
        localVariable.setVariableId("localVariable");
        expectedVariablesRead.add(localVariable);
        processSourceCode(sourceCode);
        final JavaSourceClassModel javaSourceClassModel = testClassGenerationRule.getJavaSourceClassModel();
        final Method method = javaSourceClassModel.getMethod("isTrue");
        final ExecutionPath executionPath = method.getExecutionsPath().iterator().next();
        final Set<Variable> readVariables = executionPath.getReadVariables();
        assertEquals(3, readVariables.size());
        assertEquals(expectedVariablesRead, readVariables);
    }

    /**
	 * Tests if "this.fieldName" is properly identified as a class scope
	 * variable and "fieldName", which is a parameter, as a method scope var.
	 * @throws PMDException error verifying rule
	 * @throws FileNotFoundException error
	 */
    @Test
    public void testAssigningParameterSameFieldName() throws PMDException, FileNotFoundException {
        final String sourceCode = "public class ClassSource { " + "private Type fieldName; " + "public void setUfSelecionada(Type fieldName) { " + "this.fieldName = fieldName; " + "} " + "} ";
        processSourceCode(sourceCode);
        final JavaSourceClassModel javaSourceClassModel = testClassGenerationRule.getJavaSourceClassModel();
        final Method method = javaSourceClassModel.getMethod("setUfSelecionada");
        final ExecutionPath executionPath = method.getExecutionsPath().get(0);
        final Variable variableRead = executionPath.getReadVariables().iterator().next();
        final Variable fieldVariableWritten = executionPath.getVariablesWritten().iterator().next();
        assertEquals("fieldName", variableRead.getVariableId());
        assertEquals(Scope.METHOD_SCOPE, variableRead.getScope());
        assertEquals("fieldName", fieldVariableWritten.getVariableId());
        assertEquals(Scope.CLASS_SCOPE, fieldVariableWritten.getScope());
    }

    /**
	 * Tests if "varOneName = varTwoName;" is properly identified written vars and read vars.
	 * @throws PMDException error verifying rule
	 * @throws FileNotFoundException error
	 */
    @Test
    public void testSimpleVariablesAssignments() throws PMDException, FileNotFoundException {
        final String sourceCode = "public class ClassSource { " + "private int type; " + "public void update(int newType) { " + "type = newType; " + "} " + "} ";
        processSourceCode(sourceCode);
        final JavaSourceClassModel javaSourceClassModel = testClassGenerationRule.getJavaSourceClassModel();
        final Method method = javaSourceClassModel.getMethod("update");
        final ExecutionPath executionPath = method.getExecutionsPath().get(0);
        final Variable variableRead = executionPath.getReadVariables().iterator().next();
        final Variable fieldVariableWritten = executionPath.getVariablesWritten().iterator().next();
        assertEquals("newType", variableRead.getVariableId());
        assertEquals(Scope.METHOD_SCOPE, variableRead.getScope());
        assertEquals("type", fieldVariableWritten.getVariableId());
        assertEquals(Scope.CLASS_SCOPE, fieldVariableWritten.getScope());
    }

    /**
	 * Tests if "varOneName = varTwoName;" is properly identified written vars and read vars.
	 * @throws PMDException error verifying rule
	 * @throws FileNotFoundException error
	 */
    @Test
    public void testAssignedVariableUsingThisModifier() throws PMDException, FileNotFoundException {
        final String sourceCode = "public class ClassSource { " + "private EngineType defaultEngine; " + "public void mountCar() { " + "EngineType engine = null;" + "engine = this.defaultEngine; " + "} " + "} ";
        processSourceCode(sourceCode);
        final JavaSourceClassModel javaSourceClassModel = testClassGenerationRule.getJavaSourceClassModel();
        final Method method = javaSourceClassModel.getMethod("mountCar");
        final ExecutionPath executionPath = method.getExecutionsPath().get(0);
        final Variable variableRead = executionPath.getReadVariables().iterator().next();
        final Variable fieldVariableWritten = executionPath.getVariablesWritten().iterator().next();
        assertEquals("defaultEngine", variableRead.getVariableId());
        assertEquals(Scope.CLASS_SCOPE, variableRead.getScope());
        assertEquals("engine", fieldVariableWritten.getVariableId());
        assertEquals(Scope.LOCAL_SCOPE, fieldVariableWritten.getScope());
    }

    /**
	 * Tests if "varOneName = varTwoName;" is properly identified written vars and read vars.
	 * @throws PMDException error verifying rule
	 * @throws FileNotFoundException error
	 */
    @Test
    public void testMethodInvocationOwnClassNoArguments() throws PMDException, FileNotFoundException {
        final String sourceCode = "public class ClassSource { " + "private Field field;" + "public void selectCar(Car car) { " + "onChangeCar();" + "field.onChangeCar();" + "} " + "public void onChangeCar() { }" + "} ";
        processSourceCode(sourceCode);
        final JavaSourceClassModel javaSourceClassModel = testClassGenerationRule.getJavaSourceClassModel();
        assertEquals(2, javaSourceClassModel.getMethods().size());
        final Method method = javaSourceClassModel.getMethod("selectCar");
        final ExecutionPath executionPath = method.getExecutionsPath().get(0);
        final List<MethodInvocation> methodInvocations = executionPath.getInternalMethodInvocations();
        assertEquals(2, methodInvocations.size());
        MethodInvocation methodInvocation = methodInvocations.get(0);
        assertEquals(null, methodInvocation.getInvokerVariable());
        assertEquals("onChangeCar", methodInvocation.getMethodInvoked());
        methodInvocation = methodInvocations.get(1);
        assertEquals("field", methodInvocation.getInvokerVariable().getVariableId());
        assertEquals("onChangeCar", methodInvocation.getMethodInvoked());
    }

    /**
	 * Tests if:
	 * variableName.someMethod(args)
	 * is correctl eidentified as method invocation.
	 * @throws PMDException error verifying rule
	 * @throws FileNotFoundException error
	 */
    @Test
    public void testMethodInvocationFromSimpleVariableWithArgs() throws PMDException, FileNotFoundException {
        final String sourceCode = "public class ClassSource { " + "public void method() { " + "Type<Object> var = new Type<Object>();" + "var.someMethod(var);" + "} " + "} ";
        processSourceCode(sourceCode);
        final JavaSourceClassModel javaSourceClassModel = testClassGenerationRule.getJavaSourceClassModel();
        final Method method = javaSourceClassModel.getMethod("method");
        final ExecutionPath executionPath = method.getExecutionsPath().get(0);
        final List<MethodInvocation> methodInvocations = executionPath.getInternalMethodInvocations();
        assertEquals(1, methodInvocations.size());
        final MethodInvocation methodInvocation = methodInvocations.get(0);
        final Variable variableInvoked = new Variable();
        variableInvoked.setVariableId("var");
        variableInvoked.setScope(Scope.LOCAL_SCOPE);
        variableInvoked.setType("Type<Object>");
        assertEquals(variableInvoked, methodInvocation.getInvokerVariable());
        assertEquals("someMethod", methodInvocation.getMethodInvoked());
        assertEquals(1, methodInvocation.getArgumentsList().size());
        assertEquals(variableInvoked, methodInvocation.getArgumentsList().get(0));
    }

    /**
	 * Tests if:
	 * this.variableName.someMethod(args)
	 * is correctl eidentified as method invocation.
	 * @throws PMDException error verifying rule
	 * @throws FileNotFoundException error
	 */
    @Test
    public void testMethodInvocationFromThisDotVariableWithArgs() throws PMDException, FileNotFoundException {
        final String sourceCode = "public class ClassSource { " + "Type var = new Type();" + "public void method() { " + "this.var.someMethod(this.var);" + "} " + "} ";
        processSourceCode(sourceCode);
        final JavaSourceClassModel javaSourceClassModel = testClassGenerationRule.getJavaSourceClassModel();
        final Method method = javaSourceClassModel.getMethod("method");
        final ExecutionPath executionPath = method.getExecutionsPath().get(0);
        final List<MethodInvocation> methodInvocations = executionPath.getInternalMethodInvocations();
        assertEquals(1, methodInvocations.size());
        final MethodInvocation methodInvocation = methodInvocations.get(0);
        final Variable variableInvoked = new Variable();
        variableInvoked.setVariableId("var");
        variableInvoked.setScope(Scope.CLASS_SCOPE);
        variableInvoked.setType("Type");
        assertEquals(variableInvoked, methodInvocation.getInvokerVariable());
        assertEquals("someMethod", methodInvocation.getMethodInvoked());
        assertEquals(1, methodInvocation.getArgumentsList().size());
        assertEquals(variableInvoked, methodInvocation.getArgumentsList().get(0));
    }

    /**
	 * Tests if:
	 * variableName.someMethod(literal)
	 * is correctl eidentified as method invocation.
	 * @throws PMDException error verifying rule
	 * @throws FileNotFoundException error
	 */
    @Test
    public void testMethodInvocationWithArgsLiteral() throws PMDException, FileNotFoundException {
        final String sourceCode = "public class ClassSource { " + "Type var = new Type();" + "public void method() { " + "this.var.someMethod(\"string\", true, 10);" + "} " + "} ";
        processSourceCode(sourceCode);
        final JavaSourceClassModel javaSourceClassModel = testClassGenerationRule.getJavaSourceClassModel();
        final Method method = javaSourceClassModel.getMethod("method");
        final ExecutionPath executionPath = method.getExecutionsPath().get(0);
        final List<MethodInvocation> methodInvocations = executionPath.getInternalMethodInvocations();
        assertEquals(1, methodInvocations.size());
        final MethodInvocation methodInvocation = methodInvocations.get(0);
        final Variable variableInvoked = new Variable();
        variableInvoked.setVariableId("var");
        variableInvoked.setScope(Scope.CLASS_SCOPE);
        variableInvoked.setType("Type");
        assertEquals(variableInvoked, methodInvocation.getInvokerVariable());
        assertEquals("someMethod", methodInvocation.getMethodInvoked());
        assertEquals(3, methodInvocation.getArgumentsList().size());
        final Literal literalString = new Literal();
        literalString.setScope(null);
        literalString.setType("String");
        literalString.setValue("string");
        assertEquals(literalString, methodInvocation.getArgumentsList().get(0));
        final Literal literalBoolean = new Literal();
        literalBoolean.setValue("true");
        literalBoolean.setType("boolean");
        assertEquals(literalBoolean, methodInvocation.getArgumentsList().get(1));
        final Literal literalInt = new Literal();
        literalInt.setType("int");
        literalInt.setValue("10");
        assertEquals(literalInt, methodInvocation.getArgumentsList().get(2));
    }

    /**
	 * Tests if the method invocation was returned by the method.
	 * @throws PMDException error verifying rule
	 * @throws FileNotFoundException error
	 */
    @Test
    public void testReturnedMethodInvocation() throws PMDException, FileNotFoundException {
        final String sourceCode = "public class ClassSource {" + "private Var fieldVariable;" + "public Var returnedMethods(Type param) {" + "return fieldVariable.someMethod(param);" + "}" + "};";
        final Set<Variable> expectedVariablesRead = new HashSet<Variable>(3);
        final Variable fieldVariable = new Variable();
        fieldVariable.setScope(Scope.METHOD_SCOPE);
        fieldVariable.setType("Type");
        fieldVariable.setVariableId("param");
        expectedVariablesRead.add(fieldVariable);
        processSourceCode(sourceCode);
        final JavaSourceClassModel javaSourceClassModel = testClassGenerationRule.getJavaSourceClassModel();
        final Method method = javaSourceClassModel.getMethod("returnedMethods");
        final ExecutionPath executionPath = method.getExecutionsPath().iterator().next();
        final List<MethodInvocation> invokedMethods = executionPath.getInternalMethodInvocations();
        assertEquals(1, invokedMethods.size());
        assertEquals("someMethod", invokedMethods.get(0).getMethodInvoked());
        assertTrue(invokedMethods.get(0).isReturnedInvocation());
    }

    /**
	 * Tests if the method invocation was returned by the method.
	 * @throws PMDException error verifying rule
	 * @throws FileNotFoundException error
	 */
    @Test
    public void testReturnedMethodInvocationFalse() throws PMDException, FileNotFoundException {
        final String sourceCode = "public class ClassSource {" + "private Var fieldVariable;" + "public Var returnedMethods(Type param) {" + "return fieldVariable.someMethod(param) || !fieldVariable;" + "}" + "};";
        final Set<Variable> expectedVariablesRead = new HashSet<Variable>(3);
        final Variable fieldVariable = new Variable();
        fieldVariable.setScope(Scope.METHOD_SCOPE);
        fieldVariable.setType("Type");
        fieldVariable.setVariableId("param");
        expectedVariablesRead.add(fieldVariable);
        processSourceCode(sourceCode);
        final JavaSourceClassModel javaSourceClassModel = testClassGenerationRule.getJavaSourceClassModel();
        final Method method = javaSourceClassModel.getMethod("returnedMethods");
        final ExecutionPath executionPath = method.getExecutionsPath().iterator().next();
        final List<MethodInvocation> invokedMethods = executionPath.getInternalMethodInvocations();
        assertEquals(1, invokedMethods.size());
        assertEquals("someMethod", invokedMethods.get(0).getMethodInvoked());
        assertFalse(invokedMethods.get(0).isReturnedInvocation());
    }

    /**
	 * test Field Modifiers.
	 * @throws PMDException error verifying rule
	 * @throws FileNotFoundException error
	 */
    @Test
    public void testFieldModifiers() throws PMDException, FileNotFoundException {
        final String sourceCode = "public class ClassSource {" + "private Var privateField;" + "public Var publicField;" + "protected Var protectedField;" + "static Var staticField;" + "};";
        final Set<Variable> expectedVariablesRead = new HashSet<Variable>(3);
        final Variable fieldVariable = new Variable();
        fieldVariable.setScope(Scope.METHOD_SCOPE);
        fieldVariable.setType("Type");
        fieldVariable.setVariableId("param");
        expectedVariablesRead.add(fieldVariable);
        processSourceCode(sourceCode);
        final JavaSourceClassModel javaSourceClassModel = testClassGenerationRule.getJavaSourceClassModel();
        assertEquals(4, javaSourceClassModel.getFields().size());
        final Field privateField = javaSourceClassModel.getField("privateField");
        assertTrue(privateField.isPrivate());
        assertFalse(privateField.isProtected() || privateField.isPublic() || privateField.isStatic());
        final Field protectedField = javaSourceClassModel.getField("protectedField");
        assertTrue(protectedField.isProtected());
        assertFalse(protectedField.isPrivate() || protectedField.isPublic() || protectedField.isStatic());
        final Field publicField = javaSourceClassModel.getField("publicField");
        assertTrue(publicField.isPublic());
        assertFalse(publicField.isProtected() || publicField.isPrivate() || publicField.isStatic());
        final Field staticField = javaSourceClassModel.getField("staticField");
        assertTrue(staticField.isStatic());
        assertFalse(staticField.isProtected() || staticField.isPrivate() || staticField.isPublic());
    }

    /**
	 * test Field Modifiers.
	 * @throws PMDException error verifying rule
	 * @throws FileNotFoundException error
	 */
    @Test
    public void testAnnotations() throws PMDException, FileNotFoundException {
        final String sourceCode = "public class ClassSource {" + "@EJB " + "private Var ejbField;" + "@In " + "public Var inField;" + "};";
        final Set<Variable> expectedVariablesRead = new HashSet<Variable>(3);
        final Variable fieldVariable = new Variable();
        fieldVariable.setScope(Scope.METHOD_SCOPE);
        fieldVariable.setType("Type");
        fieldVariable.setVariableId("param");
        expectedVariablesRead.add(fieldVariable);
        processSourceCode(sourceCode);
        final JavaSourceClassModel javaSourceClassModel = testClassGenerationRule.getJavaSourceClassModel();
        assertEquals(2, javaSourceClassModel.getFields().size());
        final Field ejbField = javaSourceClassModel.getField("ejbField");
        assertEquals(1, ejbField.getAnnotations().size());
        assertEquals("EJB", ejbField.getAnnotations().get(0).getName());
        final Field inField = javaSourceClassModel.getField("inField");
        assertEquals(1, inField.getAnnotations().size());
        assertEquals("In", inField.getAnnotations().get(0).getName());
    }

    /**
	 * test Field Modifiers.
	 * @throws PMDException error verifying rule
	 * @throws FileNotFoundException error
	 */
    @Test
    public void testAssignedVariableDeclarationMethodInvocation() throws PMDException, FileNotFoundException {
        final String sourceCode = "public class ClassSource {" + "public void assignedVariableMethodInvocation(Type param) {" + "Var fieldVariable = param.calculateSomeThing();" + "}" + "};";
        final Variable assignedVariable = new Variable();
        assignedVariable.setScope(Scope.LOCAL_SCOPE);
        assignedVariable.setType("Var");
        assignedVariable.setVariableId("fieldVariable");
        processSourceCode(sourceCode);
        final JavaSourceClassModel javaSourceClassModel = testClassGenerationRule.getJavaSourceClassModel();
        final Method method = javaSourceClassModel.getMethod("assignedVariableMethodInvocation");
        assertEquals(1, method.getExecutionsPath().size());
        final ExecutionPath executionPath = method.getExecutionsPath().get(0);
        assertEquals(1, executionPath.getInternalMethodInvocations().size());
        final MethodInvocation methodInvocation = executionPath.getInternalMethodInvocations().get(0);
        assertEquals("Var", methodInvocation.getReturnedType());
        assertEquals(assignedVariable, methodInvocation.getAssignedVariable());
    }

    /**
	 * test Field Modifiers.
	 * @throws PMDException error verifying rule
	 * @throws FileNotFoundException error
	 */
    @Test
    public void testAssignedVariableMethodInvocation() throws PMDException, FileNotFoundException {
        final String sourceCode = "public class ClassSource {" + "Var fieldVariable;" + "public void assignedVariableMethodInvocation(Type param) {" + "fieldVariable = param.calculateSomeThing();" + "}" + "};";
        final Variable assignedVariable = new Variable();
        assignedVariable.setScope(Scope.CLASS_SCOPE);
        assignedVariable.setType("Var");
        assignedVariable.setVariableId("fieldVariable");
        processSourceCode(sourceCode);
        final JavaSourceClassModel javaSourceClassModel = testClassGenerationRule.getJavaSourceClassModel();
        final Method method = javaSourceClassModel.getMethod("assignedVariableMethodInvocation");
        assertEquals(1, method.getExecutionsPath().size());
        final ExecutionPath executionPath = method.getExecutionsPath().get(0);
        assertEquals(1, executionPath.getInternalMethodInvocations().size());
        final MethodInvocation methodInvocation = executionPath.getInternalMethodInvocations().get(0);
        assertEquals("Var", methodInvocation.getReturnedType());
        assertEquals(assignedVariable, methodInvocation.getAssignedVariable());
    }

    /**
	 * Tests if "this.variable = methodInvocation()" gets correctly identified.
	 * @throws PMDException error verifying rule
	 * @throws FileNotFoundException error
	 */
    @Test
    public void testAssignedThisVariableMethodInvocation() throws PMDException, FileNotFoundException {
        final String sourceCode = "public class ClassSource {\n" + "Var fieldVariable;\n" + "public void assignedThisVariableMethodInvocation(Type param) {\n" + "this.fieldVariable = param.calculateSomeThing();\n" + "}\n" + "}";
        final Variable assignedVariable = new Variable();
        assignedVariable.setScope(Scope.CLASS_SCOPE);
        assignedVariable.setType("Var");
        assignedVariable.setVariableId("fieldVariable");
        processSourceCode(sourceCode);
        final JavaSourceClassModel javaSourceClassModel = testClassGenerationRule.getJavaSourceClassModel();
        final Method method = javaSourceClassModel.getMethod("assignedThisVariableMethodInvocation");
        assertEquals(1, method.getExecutionsPath().size());
        final ExecutionPath executionPath = method.getExecutionsPath().get(0);
        assertEquals(1, executionPath.getInternalMethodInvocations().size());
        final MethodInvocation methodInvocation = executionPath.getInternalMethodInvocations().get(0);
        assertEquals("Var", methodInvocation.getReturnedType());
        assertEquals(assignedVariable, methodInvocation.getAssignedVariable());
    }

    /**
	 * Tests if the nodes in execution flow are unique.
	 * Obs: when flow enters for statements, pmd returns data flows with repeated nodes inside.
	 * @throws PMDException error verifying rule
	 * @throws FileNotFoundException error
	 */
    @Test
    public void testUniqueNodesInExecutionFlow() throws PMDException, FileNotFoundException {
        final String sourceCode = "public class ClassSource {" + "private Var fieldVariable;" + "public void testForDuplication(Type param) {" + "for(int i=0; i<10; i++) {" + "param.recalculate();" + "}" + "fieldVariable.someMethod(param);" + "}" + "};";
        processSourceCode(sourceCode);
        final JavaSourceClassModel javaSourceClassModel = testClassGenerationRule.getJavaSourceClassModel();
        final Method method = javaSourceClassModel.getMethod("testForDuplication");
        for (final ExecutionPath executionPath : method.getExecutionsPath()) {
            assertEquals(1, executionPath.getForExecutionNodes().size());
        }
    }

    /**
	 * Tests if the nodes in execution flow are unique.
	 * Obs: when flow enters for statements, pmd returns data flows with repeated nodes inside.
	 * @throws PMDException error verifying rule
	 * @throws FileNotFoundException error
	 */
    @Test
    public void testUniqueNodesInExecutionFlowNestedIf() throws PMDException, FileNotFoundException {
        final String sourceCode = "public class ClassSource {\n" + "private Var fieldVariable;\n" + "public void testForDuplication(Type param) {\n" + "for(int i=0; i<10; i++) {\n" + "if(i == 0) {\n" + "param.recalculate();\n" + "}\n" + "}\n" + "fieldVariable.someMethod(param);\n" + "}\n" + "};";
        System.out.println(sourceCode);
        processSourceCode(sourceCode);
        final JavaSourceClassModel javaSourceClassModel = testClassGenerationRule.getJavaSourceClassModel();
        final Method method = javaSourceClassModel.getMethod("testForDuplication");
        assertEquals(3, method.getExecutionsPath().size());
        final ExecutionPath executionPathOne = method.getExecutionsPath().get(0);
        assertEquals(1, executionPathOne.getForExecutionNodes().size());
        assertTrue(executionPathOne.getForExecutionNodes().iterator().next().isEntersConditionalExpression());
        assertEquals(1, executionPathOne.getIfExecutionNodes().size());
        assertTrue(executionPathOne.getIfExecutionNodes().iterator().next().isEntersConditionalExpression());
        final ExecutionPath executionPathTwo = method.getExecutionsPath().get(1);
        assertEquals(1, executionPathTwo.getForExecutionNodes().size());
        assertTrue(executionPathTwo.getForExecutionNodes().iterator().next().isEntersConditionalExpression());
        assertEquals(1, executionPathTwo.getIfExecutionNodes().size());
        assertFalse(executionPathTwo.getIfExecutionNodes().iterator().next().isEntersConditionalExpression());
        final ExecutionPath executionPathThree = method.getExecutionsPath().get(2);
        assertEquals(1, executionPathThree.getForExecutionNodes().size());
        assertFalse(executionPathThree.getForExecutionNodes().iterator().next().isEntersConditionalExpression());
        assertEquals(0, executionPathThree.getIfExecutionNodes().size());
    }

    /**
	 * Tests if the nodes in execution flow are unique.
	 * Obs: when flow enters for statements, pmd returns data flows with repeated nodes inside.
	 * @throws PMDException error verifying rule
	 * @throws FileNotFoundException error
	 */
    @Test
    public void testUniqueNodesInExecutionFlowNestedFor() throws PMDException, FileNotFoundException {
        final String sourceCode = "public class ClassSource {\n" + "private Var fieldVariable;\n" + "public void testForDuplication(Type param) {\n" + "for(int i=0; i<10; i++) {\n" + "for(int i=0; i<10; i++) {\n" + "param.recalculate();\n" + "}\n" + "}\n" + "fieldVariable.someMethod(param);\n" + "}\n" + "};";
        System.out.println(sourceCode);
        processSourceCode(sourceCode);
        final JavaSourceClassModel javaSourceClassModel = testClassGenerationRule.getJavaSourceClassModel();
        final Method method = javaSourceClassModel.getMethod("testForDuplication");
        assertEquals(3, method.getExecutionsPath().size());
        final ExecutionPath executionPathOne = method.getExecutionsPath().get(0);
        assertEquals(2, executionPathOne.getForExecutionNodes().size());
        assertTrue(executionPathOne.getForExecutionNodes().get(0).isEntersConditionalExpression());
        assertTrue(executionPathOne.getForExecutionNodes().get(1).isEntersConditionalExpression());
        final ExecutionPath executionPathTwo = method.getExecutionsPath().get(1);
        assertEquals(2, executionPathTwo.getForExecutionNodes().size());
        assertTrue(executionPathTwo.getForExecutionNodes().get(0).isEntersConditionalExpression());
        assertFalse(executionPathTwo.getForExecutionNodes().get(1).isEntersConditionalExpression());
        final ExecutionPath executionPathThree = method.getExecutionsPath().get(2);
        assertEquals(1, executionPathThree.getForExecutionNodes().size());
        assertFalse(executionPathThree.getForExecutionNodes().iterator().next().isEntersConditionalExpression());
    }

    /**
	 * Tests if the mthod's modifier is correctly extracted.
	 * @throws PMDException error verifying rule
	 * @throws FileNotFoundException error
	 */
    @Test
    public void testMethodsModifiers() throws PMDException, FileNotFoundException {
        final String sourceCode = "public class ClassSource {\n" + "private void testPrivate() {\n" + "}\n" + "final public Set<Type> testPublic() {\n" + "}\n" + "protected int testProtected() {\n" + "}\n" + "}\n";
        processSourceCode(sourceCode);
        final JavaSourceClassModel javaSourceClassModel = testClassGenerationRule.getJavaSourceClassModel();
        Method method = javaSourceClassModel.getMethod("testPrivate");
        assertTrue(method.isPrivate());
        assertFalse(method.isPublic());
        assertFalse(method.isProtected());
        assertFalse(method.isFinal());
        method = javaSourceClassModel.getMethod("testPublic");
        assertTrue(method.isPublic());
        assertTrue(method.isFinal());
        method = javaSourceClassModel.getMethod("testProtected");
        assertTrue(method.isProtected());
    }

    /**
	 * In:
	 * var.someMethod();
	 * tests if var is included in the read variables set.
	 * @throws PMDException error verifying rule
	 * @throws FileNotFoundException error
	 */
    @Test
    public void testInvokerVariableIsInReadVariables() throws PMDException, FileNotFoundException {
        final String sourceCode = "public class ClassSource {\n" + "private void testPrivate() {\n" + "Type var = new Type();\n" + "var.someMethod();\n" + "}\n" + "}\n";
        processSourceCode(sourceCode);
        final JavaSourceClassModel javaSourceClassModel = testClassGenerationRule.getJavaSourceClassModel();
        final Method method = javaSourceClassModel.getMethod("testPrivate");
        final ExecutionPath executionPath = method.getExecutionsPath().iterator().next();
        assertEquals(1, executionPath.getReadVariables().size());
        final Variable variableRead = executionPath.getReadVariables().iterator().next();
        final Variable variableExpected = new Variable();
        variableExpected.setScope(Scope.LOCAL_SCOPE);
        variableExpected.setVariableId("var");
        variableExpected.setType("Type");
        assertEquals(variableExpected, variableRead);
    }

    /**
	 * Test if "this" without any dereference is ignored as variable.
	 * @throws PMDException error verifying rule
	 * @throws FileNotFoundException error
	 */
    @Test
    public void testVariableThisIgnored() throws PMDException, FileNotFoundException {
        final String sourceCode = "public class ClassSource {\n" + "private void testThis(ObjType obj) {\n" + "if(this == obj) {\n" + "obj = this;\n" + "}\n" + "}\n" + "}\n";
        processSourceCode(sourceCode);
        final JavaSourceClassModel javaSourceClassModel = testClassGenerationRule.getJavaSourceClassModel();
        final Method method = javaSourceClassModel.getMethod("testThis");
        final ExecutionPath executionPath = method.getExecutionsPath().iterator().next();
        assertEquals(1, executionPath.getReadVariables().size());
        final Variable variableRead = executionPath.getReadVariables().iterator().next();
        final Variable variableExpected = new Variable();
        variableExpected.setScope(Scope.METHOD_SCOPE);
        variableExpected.setVariableId("obj");
        variableExpected.setType("ObjType");
        assertEquals(variableExpected, variableRead);
        assertEquals(1, executionPath.getVariablesWritten().size());
        final Variable variableWritten = executionPath.getVariablesWritten().iterator().next();
        assertEquals(variableExpected, variableWritten);
    }

    /**
	 * Tests generics type are correcty identified in parameters.
	 * @throws PMDException error verifying rule
	 * @throws FileNotFoundException error
	 */
    @Test
    public void testReadVarsNotInConditionalStatementBlock() throws PMDException, FileNotFoundException {
        final String sourceCode = "public class Teste {" + "private Field fieldName;" + "public void test() { " + "if(1 + 1 == 2){ " + "fieldName = new FieldName();" + "for(Type x : fieldName.getList()){" + "x.someMethod();" + "System.out.println(x);" + "}" + "}" + "} " + "}";
        processSourceCode(sourceCode);
        final JavaSourceClassModel javaSourceClassModel = testClassGenerationRule.getJavaSourceClassModel();
        final Method method = javaSourceClassModel.getMethod("test");
        assertEquals(3, method.getExecutionsPath().size());
        final Iterator<ExecutionPath> iteratorExecutionPath = method.getExecutionsPath().iterator();
        final ExecutionPath executionPathOne = iteratorExecutionPath.next();
        assertEquals(5, executionPathOne.getExecutionPathNodes().size());
        final ExecutionPath executionPathTwo = iteratorExecutionPath.next();
        assertEquals(3, executionPathTwo.getExecutionPathNodes().size());
        assertEquals(1, executionPathTwo.getReadVariables().size());
        assertEquals(1, executionPathTwo.getVariablesWritten().size());
        final ExecutionPath executionPathThree = iteratorExecutionPath.next();
        assertEquals(1, executionPathThree.getExecutionPathNodes().size());
        System.out.println(executionPathThree.getReadVariables());
        assertEquals(0, executionPathThree.getReadVariables().size());
        assertEquals(0, executionPathThree.getVariablesWritten().size());
    }

    /**
	 * Tests if in statements:
	 * "if(var.someModethod())"
	 * the returned value of someMethod invocation is correctly setted.
	 * @throws PMDException error verifying rule
	 * @throws FileNotFoundException error
	 */
    @Test
    public void testBooleanInvocationReturnValue() throws PMDException, FileNotFoundException {
        final String sourceCode = "public class ClassSource {\n" + "private void testReturnedValue(Var var) {\n" + "if(var.booleanMethod()){" + "System.out.println(var);" + "}" + "}\n" + "}\n";
        processSourceCode(sourceCode);
        final JavaSourceClassModel javaSourceClassModel = testClassGenerationRule.getJavaSourceClassModel();
        final Method method = javaSourceClassModel.getMethod("testReturnedValue");
        final Iterator<ExecutionPath> iteratorExecutionsPath = method.getExecutionsPath().iterator();
        final ExecutionPath executionPathOne = iteratorExecutionsPath.next();
        final ExecutionPathNode ifNodeFirstExecutionPath = executionPathOne.getIfExecutionNodes().get(0);
        assertTrue(ifNodeFirstExecutionPath.isEntersConditionalExpression());
        final MethodInvocation returnedValueTrueMethodInvocation = ifNodeFirstExecutionPath.getInternalMethodInvocations().get(0);
        assertEquals("true", returnedValueTrueMethodInvocation.getReturnedValue());
        final ExecutionPath executionPathTwo = iteratorExecutionsPath.next();
        final ExecutionPathNode ifNodeSecondExecutionPath = executionPathTwo.getIfExecutionNodes().get(0);
        assertFalse(ifNodeSecondExecutionPath.isEntersConditionalExpression());
        final MethodInvocation returnedValueFalseMethodInvocation = ifNodeSecondExecutionPath.getInternalMethodInvocations().get(0);
        assertEquals("false", returnedValueFalseMethodInvocation.getReturnedValue());
    }

    /**
	 * Tests if in statements:
	 * "if(var.someMethod() || other expression )"
	 * the returned type of someMethod invocation is correctly setted.
	 * @throws PMDException error verifying rule
	 * @throws FileNotFoundException error
	 */
    @Test
    public void testBooleanInvocationReturnedTypeConditionalExpression() throws PMDException, FileNotFoundException {
        final String sourceCode = "public class ClassSource {\n" + "private void testReturnedType(Var var) {\n" + "if(var != null || (var.booleanMethod() && var instanceof Var)){" + "System.out.println(var);" + "}" + "}\n" + "}\n";
        processSourceCode(sourceCode);
        final JavaSourceClassModel javaSourceClassModel = testClassGenerationRule.getJavaSourceClassModel();
        final Method method = javaSourceClassModel.getMethod("testReturnedType");
        final Iterator<ExecutionPath> iteratorExecutionsPath = method.getExecutionsPath().iterator();
        final ExecutionPath executionPathOne = iteratorExecutionsPath.next();
        final ExecutionPathNode ifNodeFirstExecutionPath = executionPathOne.getIfExecutionNodes().get(0);
        final MethodInvocation returnedValueTrueMethodInvocation = ifNodeFirstExecutionPath.getInternalMethodInvocations().get(0);
        assertEquals("boolean", returnedValueTrueMethodInvocation.getReturnedType());
    }

    /**
	 * Tests if in statements:
	 * "var = null;"
	 * var's value is correctly setted.
	 * @throws PMDException error verifying rule
	 * @throws FileNotFoundException error
	 */
    @Test
    public void testWrittenNullValue() throws PMDException, FileNotFoundException {
        final String sourceCode = "public class ClassSource {\n" + "private void testWrittenLiteralValue() {\n" + "Var nullVarTwo;\n" + "nullVarTwo = null;\n" + "}\n" + "}\n";
        processSourceCode(sourceCode);
        final JavaSourceClassModel javaSourceClassModel = testClassGenerationRule.getJavaSourceClassModel();
        final Method method = javaSourceClassModel.getMethod("testWrittenLiteralValue");
        assertEquals(1, method.getExecutionsPath().size());
        final ExecutionPath executionPath = method.getExecutionsPath().iterator().next();
        final Set<Variable> variablesWritten = executionPath.getVariablesWritten();
        assertEquals(1, variablesWritten.size());
        final Iterator<Variable> iterator = variablesWritten.iterator();
        final Variable nullVarTwo = iterator.next();
        assertEquals("null", nullVarTwo.getValue());
    }

    /**
	 * Tests if in statements:
	 * "Var var = null;"
	 * var's value is correctly setted.
	 * @throws PMDException error verifying rule
	 * @throws FileNotFoundException error
	 */
    @Test
    public void testWrittenNullValueInDeclaration() throws PMDException, FileNotFoundException {
        final String sourceCode = "public class ClassSource {\n" + "private void testWrittenLiteralValue() {\n" + "Var nullVar = null;\n" + "}\n" + "}\n";
        processSourceCode(sourceCode);
        final JavaSourceClassModel javaSourceClassModel = testClassGenerationRule.getJavaSourceClassModel();
        final Method method = javaSourceClassModel.getMethod("testWrittenLiteralValue");
        assertEquals(1, method.getExecutionsPath().size());
        final ExecutionPath executionPath = method.getExecutionsPath().iterator().next();
        final Set<Variable> variablesWritten = executionPath.getVariablesWritten();
        assertEquals(1, variablesWritten.size());
        final Iterator<Variable> iterator = variablesWritten.iterator();
        final Variable nullVar = iterator.next();
        assertEquals("null", nullVar.getValue());
    }

    /**
	 * Tests if in statements:
	 * "Var var = null;"
	 * var's value is correctly setted.
	 * @throws PMDException error verifying rule
	 * @throws FileNotFoundException error
	 */
    @Test
    public void testWrittenIntValueInDeclaration() throws PMDException, FileNotFoundException {
        final String sourceCode = "public class ClassSource {\n" + "private void testWrittenLiteralValue() {\n" + "int nullVar = 55;\n" + "}\n" + "}\n";
        processSourceCode(sourceCode);
        final JavaSourceClassModel javaSourceClassModel = testClassGenerationRule.getJavaSourceClassModel();
        final Method method = javaSourceClassModel.getMethod("testWrittenLiteralValue");
        assertEquals(1, method.getExecutionsPath().size());
        final ExecutionPath executionPath = method.getExecutionsPath().iterator().next();
        final Set<Variable> variablesWritten = executionPath.getVariablesWritten();
        assertEquals(1, variablesWritten.size());
        final Iterator<Variable> iterator = variablesWritten.iterator();
        final Variable intVar = iterator.next();
        assertEquals("55", intVar.getValue());
    }

    /**
	 * Tests if in statements:
	 * "Var var = null;"
	 * var's value is correctly setted.
	 * @throws PMDException error verifying rule
	 * @throws FileNotFoundException error
	 */
    @Test
    public void testWrittenIntValue() throws PMDException, FileNotFoundException {
        final String sourceCode = "public class ClassSource {\n" + "private void testWrittenLiteralValue() {\n" + "int nullVar;" + "nullVar = 55;\n" + "}\n" + "}\n";
        processSourceCode(sourceCode);
        final JavaSourceClassModel javaSourceClassModel = testClassGenerationRule.getJavaSourceClassModel();
        final Method method = javaSourceClassModel.getMethod("testWrittenLiteralValue");
        assertEquals(1, method.getExecutionsPath().size());
        final ExecutionPath executionPath = method.getExecutionsPath().iterator().next();
        final Set<Variable> variablesWritten = executionPath.getVariablesWritten();
        assertEquals(1, variablesWritten.size());
        final Iterator<Variable> iterator = variablesWritten.iterator();
        final Variable intVar = iterator.next();
        assertEquals("55", intVar.getValue());
    }

    /**
	 * Tests if in statements:
	 * "Var var = null;"
	 * var's value is correctly setted.
	 * @throws PMDException error verifying rule
	 * @throws FileNotFoundException error
	 */
    @Test
    public void testWrittenFloatValue() throws PMDException, FileNotFoundException {
        final String sourceCode = "public class ClassSource {\n" + "private void testWrittenLiteralValue() {\n" + "int nullVar;" + "nullVar = 55f;\n" + "}\n" + "}\n";
        processSourceCode(sourceCode);
        final JavaSourceClassModel javaSourceClassModel = testClassGenerationRule.getJavaSourceClassModel();
        final Method method = javaSourceClassModel.getMethod("testWrittenLiteralValue");
        assertEquals(1, method.getExecutionsPath().size());
        final ExecutionPath executionPath = method.getExecutionsPath().iterator().next();
        final Set<Variable> variablesWritten = executionPath.getVariablesWritten();
        assertEquals(1, variablesWritten.size());
        final Iterator<Variable> iterator = variablesWritten.iterator();
        final Variable intVar = iterator.next();
        assertEquals("55f", intVar.getValue());
    }

    /**
	 * Tests if in statements:
	 * "Var var = null;"
	 * var's value is correctly setted.
	 * @throws PMDException error verifying rule
	 * @throws FileNotFoundException error
	 */
    @Test
    public void testWrittenStringValue() throws PMDException, FileNotFoundException {
        final String sourceCode = "public class ClassSource {\n" + "private void testWrittenLiteralValue() {\n" + "int nullVar;" + "nullVar = \"string\";\n" + "}\n" + "}\n";
        processSourceCode(sourceCode);
        final JavaSourceClassModel javaSourceClassModel = testClassGenerationRule.getJavaSourceClassModel();
        final Method method = javaSourceClassModel.getMethod("testWrittenLiteralValue");
        assertEquals(1, method.getExecutionsPath().size());
        final ExecutionPath executionPath = method.getExecutionsPath().iterator().next();
        final Set<Variable> variablesWritten = executionPath.getVariablesWritten();
        assertEquals(1, variablesWritten.size());
        final Iterator<Variable> iterator = variablesWritten.iterator();
        final Variable intVar = iterator.next();
        assertEquals("\"string\"", intVar.getValue());
    }

    /**
	 * Tests if in statements:
	 * "Var var = null;"
	 * var's value is correctly setted.
	 * @throws PMDException error verifying rule
	 * @throws FileNotFoundException error
	 */
    @Test
    public void testWrittenCharValue() throws PMDException, FileNotFoundException {
        final String sourceCode = "public class ClassSource {\n" + "private void testWrittenLiteralValue() {\n" + "int nullVar;" + "nullVar = 'c';\n" + "}\n" + "}\n";
        processSourceCode(sourceCode);
        final JavaSourceClassModel javaSourceClassModel = testClassGenerationRule.getJavaSourceClassModel();
        final Method method = javaSourceClassModel.getMethod("testWrittenLiteralValue");
        assertEquals(1, method.getExecutionsPath().size());
        final ExecutionPath executionPath = method.getExecutionsPath().iterator().next();
        final Set<Variable> variablesWritten = executionPath.getVariablesWritten();
        assertEquals(1, variablesWritten.size());
        final Iterator<Variable> iterator = variablesWritten.iterator();
        final Variable intVar = iterator.next();
        assertEquals("'c'", intVar.getValue());
    }

    /**
	 * Tests if in statements:
	 * "Var var = null;"
	 * var's value is correctly setted.
	 * @throws PMDException error verifying rule
	 * @throws FileNotFoundException error
	 */
    @Test
    public void testWrittenBooleanValue() throws PMDException, FileNotFoundException {
        final String sourceCode = "public class ClassSource {\n" + "private void testWrittenLiteralValue() {\n" + "int nullVar;" + "nullVar = true;\n" + "}\n" + "}\n";
        processSourceCode(sourceCode);
        final JavaSourceClassModel javaSourceClassModel = testClassGenerationRule.getJavaSourceClassModel();
        final Method method = javaSourceClassModel.getMethod("testWrittenLiteralValue");
        assertEquals(1, method.getExecutionsPath().size());
        final ExecutionPath executionPath = method.getExecutionsPath().iterator().next();
        final Set<Variable> variablesWritten = executionPath.getVariablesWritten();
        assertEquals(1, variablesWritten.size());
        final Iterator<Variable> iterator = variablesWritten.iterator();
        final Variable intVar = iterator.next();
        assertEquals("true", intVar.getValue());
    }

    /**
	 * Tests if in statements:
	 * "Var var = true;
	 *  Var varTwo = var;"
	 * varTwo's value is assigned with true.
	 * @throws PMDException error verifying rule
	 * @throws FileNotFoundException error
	 */
    @Test
    public void testTransitiveAssignment() throws PMDException, FileNotFoundException {
        final String sourceCode = "public class ClassSource {\n" + "private void testTransitiveAssignment() {\n" + "Var var = true;\n" + "Var varTwo = var;\n" + "varTwo = false;" + "Var varThree;\n" + "varThree = varTwo;" + "}\n" + "}\n";
        processSourceCode(sourceCode);
        final JavaSourceClassModel javaSourceClassModel = testClassGenerationRule.getJavaSourceClassModel();
        final Method method = javaSourceClassModel.getMethod("testTransitiveAssignment");
        assertEquals(1, method.getExecutionsPath().size());
        final ExecutionPath executionPath = method.getExecutionsPath().iterator().next();
        final Set<Variable> variablesWritten = executionPath.getVariablesWritten();
        assertEquals(3, variablesWritten.size());
        final Iterator<Variable> iterator = variablesWritten.iterator();
        final Variable var = iterator.next();
        assertEquals("true", var.getValue());
        final Variable varTwo = iterator.next();
        assertEquals("false", varTwo.getValue());
        final Variable varThree = iterator.next();
        assertEquals("false", varThree.getValue());
    }

    /**
	 * Tests if in statements:
	 * "if(var){"
	 * var's value is setted accordingly with the path execution.
	 * @throws PMDException error verifying rule
	 * @throws FileNotFoundException error
	 */
    @Test
    public void testReadVariableKnownValue() throws PMDException, FileNotFoundException {
        final String sourceCode = "public class ClassSource {\n" + "private void testReadVariableKnownValue(Var varReadIf) {\n" + "if(varReadIf){\n" + "System.out.println(\"teste\");\n" + "}\n" + "}\n" + "}\n";
        processSourceCode(sourceCode);
        final JavaSourceClassModel javaSourceClassModel = testClassGenerationRule.getJavaSourceClassModel();
        final Method method = javaSourceClassModel.getMethod("testReadVariableKnownValue");
        assertEquals(2, method.getExecutionsPath().size());
        final Iterator<ExecutionPath> iteratorExecutionPath = method.getExecutionsPath().iterator();
        verifyExecutionPathReadVariableKnownValue(iteratorExecutionPath.next());
        verifyExecutionPathReadVariableKnownValue(iteratorExecutionPath.next());
    }

    private void verifyExecutionPathReadVariableKnownValue(final ExecutionPath executionPath) {
        final Set<Variable> variablesRead = executionPath.getReadVariables();
        assertEquals(1, variablesRead.size());
        final Variable variable = variablesRead.iterator().next();
        final ExecutionPathNode ifPathNode = executionPath.getIfExecutionNodes().iterator().next();
        assertEquals(String.valueOf(ifPathNode.isEntersConditionalExpression()), variable.getValue());
        assertEquals(String.valueOf(ifPathNode.isEntersConditionalExpression()), executionPath.getLastValue(variable));
    }

    /**
	 * Tests last value of a var in a execution path."
	 * var's value is setted accordingly with the path execution.
	 * @throws PMDException error verifying rule
	 * @throws FileNotFoundException error
	 */
    @Test
    public void testLastValueExecutionPath() throws PMDException, FileNotFoundException {
        final String sourceCode = "public class ClassSource {\n" + "private void testLastValue(Var var) {\n" + "var = var + var;" + "var = null;" + "var = true;" + "}\n" + "}\n";
        processSourceCode(sourceCode);
        final JavaSourceClassModel javaSourceClassModel = testClassGenerationRule.getJavaSourceClassModel();
        final Method method = javaSourceClassModel.getMethod("testLastValue");
        assertEquals(1, method.getExecutionsPath().size());
        final ExecutionPath executionPath = method.getExecutionsPath().iterator().next();
        final Variable variable = new Variable();
        variable.setVariableId("var");
        variable.setScope(Scope.METHOD_SCOPE);
        assertEquals("true", executionPath.getLastValue(variable));
    }

    /**
	 * Tests last value of a var in a execution path."
	 * var's value is setted accordingly with the path execution.
	 * @throws PMDException error verifying rule
	 * @throws FileNotFoundException error
	 */
    @Test
    public void testLastValueExecutionPathKnownValueReadAfterLastWrite() throws PMDException, FileNotFoundException {
        final String sourceCode = "public class ClassSource {\n" + "private void testLastValue(Var var) {\n" + "var = null;" + "var = System.methodUnknownReturnValue();" + "if(var) {" + "var.someThing();" + "}" + "}\n" + "}\n";
        processSourceCode(sourceCode);
        final JavaSourceClassModel javaSourceClassModel = testClassGenerationRule.getJavaSourceClassModel();
        final Method method = javaSourceClassModel.getMethod("testLastValue");
        assertEquals(2, method.getExecutionsPath().size());
        final Iterator<ExecutionPath> iterator = method.getExecutionsPath().iterator();
        ExecutionPath executionPath = iterator.next();
        final Variable variable = new Variable();
        variable.setVariableId("var");
        variable.setScope(Scope.METHOD_SCOPE);
        assertNull(executionPath.getInitialValue(variable));
        assertEquals("true", executionPath.getLastValue(variable));
        executionPath = iterator.next();
        assertNull(executionPath.getInitialValue(variable));
        assertEquals("false", executionPath.getLastValue(variable));
    }

    /**
	 * Tests last value of a var in a execution path."
	 * var's value is setted accordingly with the path execution.
	 * @throws PMDException error verifying rule
	 * @throws FileNotFoundException error
	 */
    @Test
    public void testLastValueExecutionPathKnownValueReadBeforeLastWriteUnknown() throws PMDException, FileNotFoundException {
        final String sourceCode = "public class ClassSource {\n" + "private void testLastValue(Var var) {\n" + "var = null;" + "if(var) {" + "var.someThing();" + "}" + "var = System.methodUnknownReturnValue();" + "}\n" + "}\n";
        processSourceCode(sourceCode);
        final JavaSourceClassModel javaSourceClassModel = testClassGenerationRule.getJavaSourceClassModel();
        final Method method = javaSourceClassModel.getMethod("testLastValue");
        assertEquals(2, method.getExecutionsPath().size());
        final Iterator<ExecutionPath> iterator = method.getExecutionsPath().iterator();
        ExecutionPath executionPath = iterator.next();
        final Variable variable = new Variable();
        variable.setVariableId("var");
        variable.setScope(Scope.METHOD_SCOPE);
        assertNull(executionPath.getInitialValue(variable));
        assertNull(executionPath.getLastValue(variable));
        executionPath = iterator.next();
        assertNull(executionPath.getInitialValue(variable));
        assertNull(executionPath.getLastValue(variable));
    }

    /**
	 * Tests last value of a var in a execution path."
	 * var's value is setted accordingly with the path execution.
	 * @throws PMDException error verifying rule
	 * @throws FileNotFoundException error
	 */
    @Test
    public void testInitialValue() throws PMDException, FileNotFoundException {
        final String sourceCode = "public class ClassSource {\n" + "private void testInitialValue(Var var) {\n" + "if(var) {" + "var.someThing();" + "}" + "var = System.methodUnknownReturnValue();" + "}\n" + "}\n";
        processSourceCode(sourceCode);
        final JavaSourceClassModel javaSourceClassModel = testClassGenerationRule.getJavaSourceClassModel();
        final Method method = javaSourceClassModel.getMethod("testInitialValue");
        assertEquals(2, method.getExecutionsPath().size());
        final Iterator<ExecutionPath> iterator = method.getExecutionsPath().iterator();
        ExecutionPath executionPath = iterator.next();
        final Variable variable = new Variable();
        variable.setVariableId("var");
        variable.setScope(Scope.METHOD_SCOPE);
        assertEquals("true", executionPath.getInitialValue(variable));
        executionPath = iterator.next();
        assertEquals("false", executionPath.getInitialValue(variable));
    }

    /**
	 * Tests last value of a var in a execution path."
	 * var's value is setted accordingly with the path execution.
	 * @throws PMDException error verifying rule
	 * @throws FileNotFoundException error
	 */
    @Test
    public void testInitialValueNull() throws PMDException, FileNotFoundException {
        final String sourceCode = "public class ClassSource {\n" + "private void testInitialValue(Var var) {\n" + "if(var == null) {" + "var.someThing();" + "}" + "var = System.methodUnknownReturnValue();" + "}\n" + "}\n";
        processSourceCode(sourceCode);
        final JavaSourceClassModel javaSourceClassModel = testClassGenerationRule.getJavaSourceClassModel();
        final Method method = javaSourceClassModel.getMethod("testInitialValue");
        assertEquals(2, method.getExecutionsPath().size());
        final Iterator<ExecutionPath> iterator = method.getExecutionsPath().iterator();
        ExecutionPath executionPath = iterator.next();
        final Variable variable = new Variable();
        variable.setVariableId("var");
        variable.setScope(Scope.METHOD_SCOPE);
        assertEquals("null", executionPath.getInitialValue(variable));
        executionPath = iterator.next();
        assertNull(executionPath.getInitialValue(variable));
    }

    /**
	 * Tests last value of a var in a execution path."
	 * var's value is setted accordingly with the path execution.
	 * @throws PMDException error verifying rule
	 * @throws FileNotFoundException error
	 */
    @Test
    public void testInitialValueNotNull() throws PMDException, FileNotFoundException {
        final String sourceCode = "public class ClassSource {\n" + "private void testInitialValue(Var var) {\n" + "if(var != null) {" + "var.someThing();" + "}" + "var = System.methodUnknownReturnValue();" + "}\n" + "}\n";
        processSourceCode(sourceCode);
        final JavaSourceClassModel javaSourceClassModel = testClassGenerationRule.getJavaSourceClassModel();
        final Method method = javaSourceClassModel.getMethod("testInitialValue");
        assertEquals(2, method.getExecutionsPath().size());
        final Iterator<ExecutionPath> iterator = method.getExecutionsPath().iterator();
        ExecutionPath executionPath = iterator.next();
        final Variable variable = new Variable();
        variable.setVariableId("var");
        variable.setScope(Scope.METHOD_SCOPE);
        assertNull(executionPath.getInitialValue(variable));
        executionPath = iterator.next();
        assertEquals("null", executionPath.getInitialValue(variable));
    }

    /**
	 * Tests last value of a var in a execution path."
	 * var's value is setted accordingly with the path execution.
	 * @throws PMDException error verifying rule
	 * @throws FileNotFoundException error
	 */
    @Test
    public void testUpdateLastVarWrite() throws PMDException, FileNotFoundException {
        final String sourceCode = "public class ClassSource {\n" + "Field field;" + "private void testUpdateLastVarWrite(Var var) {\n" + "var = field.someMethod();" + "if(var != null) {" + "var.someThing();" + "}" + "}\n" + "}\n";
        processSourceCode(sourceCode);
        final JavaSourceClassModel javaSourceClassModel = testClassGenerationRule.getJavaSourceClassModel();
        final Method method = javaSourceClassModel.getMethod("testUpdateLastVarWrite");
        assertEquals(2, method.getExecutionsPath().size());
        final Iterator<ExecutionPath> iterator = method.getExecutionsPath().iterator();
        ExecutionPath executionPath = iterator.next();
        final Variable variable = new Variable();
        variable.setVariableId("var");
        variable.setScope(Scope.METHOD_SCOPE);
        assertNull(executionPath.getInitialValue(variable));
        assertNull(executionPath.getInternalMethodInvocations().get(0).getReturnedValue());
        executionPath = iterator.next();
        assertNull(executionPath.getInitialValue(variable));
        assertEquals("null", executionPath.getLastValue(variable));
        assertEquals("null", executionPath.getInternalMethodInvocations().get(0).getReturnedValue());
    }

    /**
	 * Tests last value of a var in a execution path."
	 * var's value is setted accordingly with the path execution.
	 * @throws PMDException error verifying rule
	 * @throws FileNotFoundException error
	 */
    @Test
    public void testInferReadVariableValueEnum() throws PMDException, FileNotFoundException {
        final String sourceCode = "import org.jsmg.model.SomeEnum;\n" + "public class ClassSource {\n" + "Field field;" + "private void testReadVariableValueEnum(Var var) {\n" + "if(field == SomeEnum.SOME_ITEM) {" + "var.someThing();" + "}" + "}\n" + "}\n";
        processSourceCode(sourceCode);
        final JavaSourceClassModel javaSourceClassModel = testClassGenerationRule.getJavaSourceClassModel();
        final Method method = javaSourceClassModel.getMethod("testReadVariableValueEnum");
        assertEquals(2, method.getExecutionsPath().size());
        final Iterator<ExecutionPath> iterator = method.getExecutionsPath().iterator();
        ExecutionPath executionPath = iterator.next();
        final Variable variable = new Variable();
        variable.setVariableId("field");
        variable.setScope(Scope.CLASS_SCOPE);
        assertEquals("SomeEnum.SOME_ITEM", executionPath.getInitialValue(variable));
        assertEquals(1, executionPath.getInternalMethodInvocations().size());
        executionPath = iterator.next();
        assertEquals("SomeEnum.OTHER_ITEM", executionPath.getInitialValue(variable));
        assertTrue(executionPath.getInternalMethodInvocations().isEmpty());
    }

    /**
	 * Tests last value of a var in a execution path."
	 * var's value is setted accordingly with the path execution.
	 * @throws PMDException error verifying rule
	 * @throws FileNotFoundException error
	 */
    @Test
    public void testInferReadVariableValueEnumLeftSide() throws PMDException, FileNotFoundException {
        final String sourceCode = "import org.jsmg.model.SomeEnum;\n" + "public class ClassSource {\n" + "Field field;" + "private void testReadVariableValueEnum(Var var) {\n" + "if(SomeEnum.SOME_ITEM == field) {" + "var.someThing();" + "}" + "}\n" + "}\n";
        processSourceCode(sourceCode);
        final JavaSourceClassModel javaSourceClassModel = testClassGenerationRule.getJavaSourceClassModel();
        final Method method = javaSourceClassModel.getMethod("testReadVariableValueEnum");
        assertEquals(2, method.getExecutionsPath().size());
        final Iterator<ExecutionPath> iterator = method.getExecutionsPath().iterator();
        ExecutionPath executionPath = iterator.next();
        final Variable variable = new Variable();
        variable.setVariableId("field");
        variable.setScope(Scope.CLASS_SCOPE);
        assertEquals("SomeEnum.SOME_ITEM", executionPath.getInitialValue(variable));
        assertEquals(1, executionPath.getInternalMethodInvocations().size());
        executionPath = iterator.next();
        assertEquals("SomeEnum.OTHER_ITEM", executionPath.getInitialValue(variable));
        assertTrue(executionPath.getInternalMethodInvocations().isEmpty());
    }

    /**
	 * Tests last value of a var in a execution path."
	 * var's value is setted accordingly with the path execution.
	 * @throws PMDException error verifying rule
	 * @throws FileNotFoundException error
	 */
    @Test
    public void testInferReadVariableValueConstantObject() throws PMDException, FileNotFoundException {
        final String sourceCode = "import org.jsmg.model.SomeConstants;\n" + "public class ClassSource {\n" + "String field = SomeConstants.STRING_CONSTANT;" + "private void testReadVariableValueConstant(Var var) {\n" + "if(field == SomeConstants.STRING_CONSTANT) {" + "var.someThing();" + "}" + "}\n" + "}\n";
        processSourceCode(sourceCode);
        final JavaSourceClassModel javaSourceClassModel = testClassGenerationRule.getJavaSourceClassModel();
        final Method method = javaSourceClassModel.getMethod("testReadVariableValueConstant");
        assertEquals(2, method.getExecutionsPath().size());
        final Iterator<ExecutionPath> iterator = method.getExecutionsPath().iterator();
        ExecutionPath executionPath = iterator.next();
        final Variable variable = new Variable();
        variable.setVariableId("field");
        variable.setScope(Scope.CLASS_SCOPE);
        assertEquals("SomeConstants.STRING_CONSTANT", executionPath.getInitialValue(variable));
        assertEquals(1, executionPath.getInternalMethodInvocations().size());
        executionPath = iterator.next();
        assertEquals("new java.lang.String()", executionPath.getInitialValue(variable));
        assertTrue(executionPath.getInternalMethodInvocations().isEmpty());
    }

    /**
	 * Tests last value of a var in a execution path."
	 * var's value is setted accordingly with the path execution.
	 * @throws PMDException error verifying rule
	 * @throws FileNotFoundException error
	 */
    @Test
    public void testInferReadVariableValueConstantPrimitiveNumber() throws PMDException, FileNotFoundException {
        final String sourceCode = "import org.jsmg.model.SomeConstants;\n" + "public class ClassSource {\n" + "int field = SomeConstants.INT_CONSTANT;" + "private void testReadVariableValueConstant(Var var) {\n" + "if(field == SomeConstants.INT_CONSTANT) {" + "var.someThing();" + "}" + "}\n" + "}\n";
        processSourceCode(sourceCode);
        final JavaSourceClassModel javaSourceClassModel = testClassGenerationRule.getJavaSourceClassModel();
        final Method method = javaSourceClassModel.getMethod("testReadVariableValueConstant");
        assertEquals(2, method.getExecutionsPath().size());
        final Iterator<ExecutionPath> iterator = method.getExecutionsPath().iterator();
        ExecutionPath executionPath = iterator.next();
        final Variable variable = new Variable();
        variable.setVariableId("field");
        variable.setScope(Scope.CLASS_SCOPE);
        assertEquals("SomeConstants.INT_CONSTANT", executionPath.getInitialValue(variable));
        assertEquals(1, executionPath.getInternalMethodInvocations().size());
        executionPath = iterator.next();
        assertEquals("SomeConstants.INT_CONSTANT + 1", executionPath.getInitialValue(variable));
        assertTrue(executionPath.getInternalMethodInvocations().isEmpty());
    }

    /**
	 * Tests last value of a var in a execution path."
	 * var's value is setted accordingly with the path execution.
	 * @throws PMDException error verifying rule
	 * @throws FileNotFoundException error
	 */
    @Test
    public void testInferReadVariableValueConstantNoDefaultConstructor() throws PMDException, FileNotFoundException {
        final String sourceCode = "import org.jsmg.model.SomeConstants;\n" + "public class ClassSource {\n" + "ClassWithoutDefaultConstructor field = SomeConstants.CLASS_NO_DEFAULT_CONSTRUCTOR_CONSTANT;" + "private void testReadVariableValueConstant(Var var) {\n" + "if(field == SomeConstants.CLASS_NO_DEFAULT_CONSTRUCTOR_CONSTANT) {" + "var.someThing();" + "}" + "}\n" + "}\n";
        processSourceCode(sourceCode);
        final JavaSourceClassModel javaSourceClassModel = testClassGenerationRule.getJavaSourceClassModel();
        final Method method = javaSourceClassModel.getMethod("testReadVariableValueConstant");
        assertEquals(2, method.getExecutionsPath().size());
        final Iterator<ExecutionPath> iterator = method.getExecutionsPath().iterator();
        ExecutionPath executionPath = iterator.next();
        final Variable variable = new Variable();
        variable.setVariableId("field");
        variable.setScope(Scope.CLASS_SCOPE);
        assertEquals("SomeConstants.CLASS_NO_DEFAULT_CONSTRUCTOR_CONSTANT", executionPath.getInitialValue(variable));
        assertEquals(1, executionPath.getInternalMethodInvocations().size());
        executionPath = iterator.next();
        assertEquals("new org.jsmg.model.ClassWithoutDefaultConstructor(new java.lang.String(), 1, false, 'c')", executionPath.getInitialValue(variable));
        assertTrue(executionPath.getInternalMethodInvocations().isEmpty());
    }
}
