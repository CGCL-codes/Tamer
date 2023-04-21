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
public class ExDiffXN3a implements ExerciseInterface {

    public ExDiffXN3a() {
    }

    public short getExerciseType() {
        return ExerciseInterface.FREE_FORM_TYPE_1;
    }

    public static final String TITLE = "XN3";

    public static final String TOOL_TIP = "Tool tip for XN3";

    public static final String PLATFORM = "TYPE1";

    private static String[] variables = { "x" };

    private static double[][] variableDomains = { { 1.0, 2.0 } };

    private static String[][] parameterNames = { { "xIndex1", "xPower1" }, { "xIndex2", "xPower2" }, { "a" }, { "b" }, { "c" }, { "d" }, { "type" } };

    private static String[] xPowerValues = { "sqrt(x)", "x*sqrt(x)", "x^(1/3)", "x^(2/3)", "x*x^(1/3)", "x", "x^2", "x^3" };

    private static double[] xIndexValues = { 3.0, 9.0, 2.0, 4.0, 8.0, 6.0, 12.0, 18.0 };

    private static double[] aValues = { 1.0, 2.0, 3.0, 4.0, 5.0 };

    private static double[] cValues = { 0.0, 1.0, 2.0, 3.0, 4.0, 5.0 };

    private static double[] bdValues = { -5.0, -4.0, -3.0, -2.0, -1.0, 1.0, 2.0, 3.0, 4.0, 5.0 };

    private static double[] typeValues = { 0.0, 1.0 };

    private static String[] questionStrings = { "Calculate the derivative of", "with respect to <i>x</i>." };

    private static String[][] questionExpressions = { { "type==0", "(([a]~)+([b]~)*$xPower1$)*(([c]~)+([d]~)*$xPower2$)", "true", "(([a]~)+([b]~)*$xPower1$)/(([d]~)*$xPower2$)" } };

    private static String[] correctAnswerExpression = { "type==0", "([a*d*xIndex2/6]~)*x^([xIndex2/6-1]~)+([b*c*xIndex1/6]~)*x^([xIndex1/6-1]~)+([b*d*(xIndex1+xIndex2)/6]~)*x^([(xIndex1+xIndex2)/6-1]~)", "true", "([-a/d*xIndex2/6]~)*x^([-xIndex2/6-1]~)+([b/d*(xIndex1-xIndex2)/6]~)*x^([(xIndex1-xIndex2)/6-1]~)" };

    private static String[][] distractorExpressions = {};

    private static String[][][] distractorPatterns = {};

    private static String[][] analysisStrings = {};

    private static String[][][] analysisExpressions = {};

    private static String[] workingStrings = { "No worked solution is available." };

    private static String[][] workingExpressions = {};

    private static String[] helpStrings = { "No hint is available." };

    private static String[][] helpExpressions = {};

    private static double tolerance = 0.00001;

    private static FreeformBranchingQuestionGenerator freeformBranchingQuestionGenerator;

    private static ParameterSet parameterSet;

    public QuestionInterface getExercise() {
        parameterSet = new ParameterSet(parameterNames);
        parameterSet.createParameter("xPower1", xPowerValues);
        parameterSet.createParameter("xPower2", xPowerValues);
        parameterSet.createParameter("xIndex1", xIndexValues);
        parameterSet.createParameter("xIndex2", xIndexValues);
        parameterSet.createParameter("a", aValues);
        parameterSet.createParameter("c", cValues);
        parameterSet.createParameter("b", bdValues);
        parameterSet.createParameter("d", bdValues);
        parameterSet.createParameter("type", typeValues);
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
        freeformBranchingQuestionGenerator.setNotes("html/calculus/differentiation/diffxn.html");
        freeformBranchingQuestionGenerator.setInstructions("html/help/exercise/type1.html");
        return freeformBranchingQuestionGenerator;
    }
}
