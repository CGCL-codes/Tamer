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
public class ExDiffProduct implements ExerciseInterface {

    public ExDiffProduct() {
    }

    public short getExerciseType() {
        return ExerciseInterface.FREE_FORM_TYPE_1;
    }

    public static final String TITLE = "XN";

    public static final String TOOL_TIP = "Tool tip for XN";

    public static final String PLATFORM = "TYPE1";

    private static String[] variables = { "x" };

    private static double[][] variableDomains = { { 0.1, 0.8 } };

    private static String[][] parameterNames = { { "fnum", "fden", "fpnum", "fpden" }, { "gnum", "gpnum" } };

    private static String[] fnumValues = { "x^2", "1~", "x^4", "x^3", "sqrt(x)", "1~", "x^5", "ln(x)", "arcsin(x)" };

    private static String[] fdenValues = { "1~", "x", "1~", "1~", "1~", "sqrt(x)", "1~", "1~", "1~" };

    private static String[] fpnumValues = { "2*x", "(-1~)", "4*x^3", "3*x^2", "1~", "(-1~)", "5*x^4", "1~", "1~" };

    private static String[] fpdenValues = { "1~", "x^2", "1~", "1~", "(2*sqrt(x))", "(2*x*sqrt(x))", "1~", "x", "sqrt(1-x^2)" };

    private static String[] gnumValues = { "cos(x)", "sin(x)", "e^x" };

    private static String[] gpnumValues = { "(-sin(x))", "cos(x)", "e^x" };

    private static String[] questionStrings = { "<b>Using the product rule</b>, calculate the derivative of", "with respect to <i>x</i>." };

    private static String[][] questionExpressions = { { "true", "($fnum$*$gnum$)/($fden$)" } };

    private static String[] correctAnswerExpression = { "true", "($fpnum$*$gnum$)/($fpden$)+($fnum$*$gpnum$)/($fden$)" };

    private static String[][] distractorExpressions = {};

    private static String[][] analysisStrings = {};

    private static String[][][] analysisExpressions = {};

    private static String[] workingStrings = { "The derivative of", "is", "and that of", "is", "The derivative of", "is therefore", "or simply", "" };

    private static String[][] workingExpressions = { { "true", "$fnum$/$fden$" }, { "true", "$fpnum$/$fpden$" }, { "true", "$gnum$" }, { "true", "$gpnum$" }, { "true", "($fnum$*$gnum$)/($fden$)" }, { "true", "($fpnum$/$fpden$)**$gnum$+($fnum$/$fden$)**$gpnum$" }, { "true", "($fpnum$*$gnum$)/($fpden$)+($fnum$*$gpnum$)/($fden$)" } };

    private static String[] helpStrings = { "The derivative of", "is", "" };

    private static String[][] helpExpressions = { { "true", "u(x)*v(x)" }, { "true", "u'(x)*v(x)+u(x)*v'(x)" } };

    private static double tolerance = 0.00001;

    private static FreeformBranchingQuestionGenerator freeformBranchingQuestionGenerator;

    private static ParameterSet parameterSet;

    public QuestionInterface getExercise() {
        parameterSet = new ParameterSet(parameterNames);
        parameterSet.createParameter("fnum", fnumValues);
        parameterSet.createParameter("gnum", gnumValues);
        parameterSet.createParameter("fpnum", fpnumValues);
        parameterSet.createParameter("gpnum", gpnumValues);
        parameterSet.createParameter("fden", fdenValues);
        parameterSet.createParameter("fpden", fpdenValues);
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
        freeformBranchingQuestionGenerator.setNotes("html/calculus/differentiation/diffproduct.html");
        freeformBranchingQuestionGenerator.setInstructions("html/help/exercise/type1.html");
        return freeformBranchingQuestionGenerator;
    }
}
