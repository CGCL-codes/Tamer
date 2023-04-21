package uk.ac.imperial.ma.metric.exercises.calculus.differentiation;

import uk.ac.imperial.ma.metric.exerciseEngine.classic.ExerciseInterface;
import uk.ac.imperial.ma.metric.exerciseEngine.classic.FreeformBranchingQuestionGenerator;
import uk.ac.imperial.ma.metric.exerciseEngine.classic.QuestionInterface;
import uk.ac.imperial.ma.metric.computerese.classic.ParameterSet;

/**
 * Differentiation exercise.
 * @author Phil Ramsden
 * @author Daniel J. R. May
 * @version 0.1
 */
public class ExDiffQuotient implements ExerciseInterface {

    public ExDiffQuotient() {
    }

    public short getExerciseType() {
        return ExerciseInterface.FREE_FORM_TYPE_1;
    }

    public static final String TITLE = "XN";

    public static final String TOOL_TIP = "Tool tip for XN";

    public static final String PLATFORM = "TYPE1";

    private static String[] variables = { "x" };

    private static double[][] variableDomains = { { 1.0, 2.0 } };

    private static String[][] parameterNames = { { "f", "fp", "g", "gp", "gcancel", "gpcancel", "gsqdcancel" }, { "n" } };

    private static double[] nValues = { 1.0, 2.0, 3.0, 4.0, 5.0, 6.0 };

    private static String[] fValues = { "x^([n]~)", "x^([n]~)", "x^([n]~)", "sin(x)", "cos(x)", "e^x" };

    private static String[] fpValues = { "([n]~)*x^([n-1]~)", "([n]~)*x^([n-1]~)", "([n]~)*x^([n-1]~)", "cos(x)", "(-sin(x))", "e^x" };

    private static String[] gValues = { "cos(x)", "sin(x)", "e^x", "x^([n]~)", "x^([n]~)", "x^([n]~)" };

    private static String[] gpValues = { "(-sin(x))", "cos(x)", "e^x", "([n]~)*x^([n-1]~)", "([n]~)*x^([n-1]~)", "([n]~)*x^([n-1]~)" };

    private static String[] gcancelValues = { "cos(x)", "sin(x)", "1~", "x", "x", "x" };

    private static String[] gpcancelValues = { "(-sin(x))", "cos(x)", "1~", "([n]~)", "([n]~)", "([n]~)" };

    private static String[] gsqdcancelValues = { "(cos(x))^2", "(sin(x))^2", "e^x", "x^([n+1]~)", "x^([n+1]~)", "x^([n+1]~)" };

    private static String[] questionStrings = { "<b>Using the quotient rule</b>, calculate the derivative of", "with respect to <i>x</i>." };

    private static String[][] questionExpressions = { { "true", "($f$)/($g$)" } };

    private static String[] correctAnswerExpression = { "f<2", "($fp$*$gcancel$-$f$*$gpcancel$)/$gsqdcancel$", "true", "($gcancel$*$fp$-$gpcancel$*$f$)/$gsqdcancel$" };

    private static String[][] distractorExpressions = {};

    private static String[][] analysisStrings = {};

    private static String[][][] analysisExpressions = {};

    private static String[] workingStrings = { "The derivative of", "is", "and that of", "is", "The derivative of", "is therefore", "or simply", "" };

    private static String[][] workingExpressions = { { "true", "$f$" }, { "true", "$fp$" }, { "true", "$g$" }, { "true", "$gp$" }, { "true", "($f$)/($g$)" }, { "true", "($fp$**$g$-$f$**$gp$)/($g$)^2" }, { "f<2", "($fp$*$gcancel$-$f$*$gpcancel$)/$gsqdcancel$", "true", "($gcancel$*$fp$-$gpcancel$*$f$)/$gsqdcancel$" } };

    private static String[] helpStrings = { "The derivative of", "is", "" };

    private static String[][] helpExpressions = { { "true", "u(x)/v(x)" }, { "true", "(u'(x)*v(x)-u(x)*v'(x))/(v(x))^2" } };

    private static double tolerance = 0.00001;

    private static FreeformBranchingQuestionGenerator freeformBranchingQuestionGenerator;

    private static ParameterSet parameterSet;

    public QuestionInterface getExercise() {
        parameterSet = new ParameterSet(parameterNames);
        parameterSet.createParameter("n", nValues);
        parameterSet.createParameter("f", fValues);
        parameterSet.createParameter("g", gValues);
        parameterSet.createParameter("fp", fpValues);
        parameterSet.createParameter("gp", gpValues);
        parameterSet.createParameter("gcancel", gcancelValues);
        parameterSet.createParameter("gpcancel", gpcancelValues);
        parameterSet.createParameter("gsqdcancel", gsqdcancelValues);
        freeformBranchingQuestionGenerator = new FreeformBranchingQuestionGenerator();
        freeformBranchingQuestionGenerator.setParameterSet(parameterSet);
        freeformBranchingQuestionGenerator.setQuestions(questionStrings, questionExpressions);
        freeformBranchingQuestionGenerator.setCorrectAnswer(correctAnswerExpression);
        freeformBranchingQuestionGenerator.setDistractors(distractorExpressions);
        freeformBranchingQuestionGenerator.setVariables(variables);
        freeformBranchingQuestionGenerator.setVariableDomains(variableDomains);
        freeformBranchingQuestionGenerator.setTolerance(tolerance);
        freeformBranchingQuestionGenerator.setAnalysis(analysisStrings, analysisExpressions);
        freeformBranchingQuestionGenerator.setWorking(workingStrings, workingExpressions);
        freeformBranchingQuestionGenerator.setHelp(helpStrings, helpExpressions);
        freeformBranchingQuestionGenerator.setNotes("html/calculus/differentiation/diffquotient.html");
        freeformBranchingQuestionGenerator.setInstructions("html/help/exercise/type1.html");
        return freeformBranchingQuestionGenerator;
    }
}
