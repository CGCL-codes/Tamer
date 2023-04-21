package uk.ac.imperial.ma.metric.exercises.calculus.limits;

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
public class ExLimitsDifferenceOfSurds implements ExerciseInterface {

    public ExLimitsDifferenceOfSurds() {
    }

    public short getExerciseType() {
        return ExerciseInterface.FREE_FORM_TYPE_1;
    }

    public static final String TITLE = "GradPt";

    public static final String TOOL_TIP = "Tool tip for GradPt";

    public static final String PLATFORM = "TYPE1";

    private static String[] variables = { "diverges" };

    private static double[][] variableDomains = { { 0.0, 1.0 } };

    private static String[][] parameterNames = { { "p" }, { "q1" }, { "q2" }, { "r1" }, { "r2" }, { "s1" }, { "s2" }, { "degree" }, { "x" }, { "x0" } };

    private static double[] pValues = { 1.0, 1.0, 2.0, 3.0 };

    private static double[] qrs1Values = { -5.0, -3.0, -1.0, 1.0, 3.0, 5.0 };

    private static double[] qrs2Values = { -4.0, -2.0, 0.0, 0.0, 0.0, 0.0, 2.0, 4.0 };

    private static double[] degreeValues = { 1.0, 2.0, 3.0 };

    private static String[] xValues = { "x", "y", "t", "a" };

    private static String[] x0Values = { "0", "infinity", "infinity", "infinity" };

    private static String[] questionStrings = { "Calculate, the limit, as <i>$x$</i> tends to $x0$, of", "Type 'diverges' if no such limit exists." };

    private static String[][] questionExpressions = { { "x0==0&&q2==0&&r2==0", "(sqrt(([r1]~)*$x$^2+([q1]~)*$x$+([p*p]~))-([p]~))/$x$", "x0==0", "(sqrt(([r1]~)*$x$^2+([q1]~)*$x$+([p*p]~))-sqrt(([r2]~)*$x$^2+([q2]~)*$x$+([p*p]~)))/$x$", "degree==1&&q2==0", "sqrt($x$)*(sqrt(([p*p]~)*$x$+([q1]~))-([p]~)*sqrt($x$))", "degree==1", "sqrt($x$)*(sqrt(([p*p]~)*$x$+([q1]~))-sqrt(([p*p]~)*$x$+([q2]~))", "degree==2&&q2==0&&r2==0", "sqrt(([p*p]~)*$x$^2+([q1]~)*$x$+([r1]~))-([p]~)*$x$", "degree==2", "sqrt(([p*p]~)*$x$^2+([q1]~)*$x$+([r1]~))-sqrt(([p*p]~)*$x$^2+([q2]~)*$x$+([r2]~))", "q2==0&&r2==0&s2==0", "(sqrt(([p*p]~)*$x$^3+([q1]~)*$x$^2+([r1]~)*$x$+([s1]~))-([p]~)*$x$^(3/2))/sqrt($x$)", "true", "(sqrt(([p*p]~)*$x$^3+([q1]~)*$x$^2+([r1]~)*$x$+([s1]~))-sqrt(([p*p]~)*$x$^3+([q2]~)*$x$^2+([r2]~)*$x$+([s2]~)))/sqrt($x$)" } };

    private static String[] correctAnswerExpression = { "true", "[(q1-q2)/(p+p)]" };

    private static String[][] distractorExpressions = {};

    private static String[][] analysisStrings = {};

    private static String[][][] analysisExpressions = {};

    private static String[] workingStrings = { "We begin by multiplying the top and bottom of the fraction by", "giving", "Expanding, we obtain", "and hence", "Now", "and the limit of this expression as $x$ tends to $x0$ is", "" };

    private static String[][] workingExpressions = { { "x0==0&&q2==0&&r2==0", "sqrt(([r1]~)*$x$^2+([q1]~)*$x$+([p*p]~))+([p]~)", "x0==0", "sqrt(([r1]~)*$x$^2+([q1]~)*$x$+([p*p]~))+sqrt(([r2]~)*$x$^2+([q2]~)*$x$+([p*p]~))", "degree==1&&q2==0", "sqrt(([p*p]~)*$x$+([q1]~))+([p]~)*sqrt($x$)", "degree==1", "sqrt(([p*p]~)*$x$+([q1]~))+sqrt(([p*p]~)*$x$+([q2]~)", "degree==2&&q2==0&&r2==0", "sqrt(([p*p]~)*$x$^2+([q1]~)*$x$+([r1]~))+([p]~)*$x$", "degree==2", "sqrt(([p*p]~)*$x$^2+([q1]~)*$x$+([r1]~))+sqrt(([p*p]~)*$x$^2+([q2]~)*$x$+([r2]~))", "q2==0&&r2==0&s2==0", "sqrt(([p*p]~)*$x$^3+([q1]~)*$x$^2+([r1]~)*$x$+([s1]~))+([p]~)*$x$^(3/2)", "true", "sqrt(([p*p]~)*$x$^3+([q1]~)*$x$^2+([r1]~)*$x$+([s1]~))+sqrt(([p*p]~)*$x$^3+([q2]~)*$x$^2+([r2]~)*$x$+([s2]~))" }, { "x0==0&&q2==0&&r2==0", "((sqrt(([r1]~)*$x$^2+([q1]~)*$x$+([p*p]~))-([p]~))*(sqrt(([r1]~)*$x$^2+([q1]~)*$x$+([p*p]~))+([p]~)))/($x$*(sqrt(([r1]~)*$x$^2+([q1]~)*$x$+([p*p]~))+([p]~)))", "x0==0", "((sqrt(([r1]~)*$x$^2+([q1]~)*$x$+([p*p]~))-sqrt(([r2]~)*$x$^2+([q2]~)*$x$+([p*p]~)))*(sqrt(([r1]~)*$x$^2+([q1]~)*$x$+([p*p]~))+sqrt(([r2]~)*$x$^2+([q2]~)*$x$+([p*p]~))))/($x$*(sqrt(([r1]~)*$x$^2+([q1]~)*$x$+([p*p]~))+sqrt(([r2]~)*$x$^2+([q2]~)*$x$+([p*p]~))))", "degree==1&&q2==0", "(sqrt($x$)*(sqrt(([p*p]~)*$x$+([q1]~))-([p]~)*sqrt($x$))*(sqrt(([p*p]~)*$x$+([q1]~))+([p]~)*sqrt($x$)))/(sqrt(([p*p]~)*$x$+([q1]~))+([p]~)*sqrt($x$))", "degree==1", "(sqrt($x$)*(sqrt(([p*p]~)*$x$+([q1]~))-sqrt(([p*p]~)*$x$+([q2]~))*(sqrt(([p*p]~)*$x$+([q1]~))+sqrt(([p*p]~)*$x$+([q2]~)))/(sqrt(([p*p]~)*$x$+([q1]~))+sqrt(([p*p]~)*$x$+([q2]~)))", "degree==2&&q2==0&&r2==0", "((sqrt(([p*p]~)*$x$^2+([q1]~)*$x$+([r1]~))-([p]~)*$x$)*(sqrt(([p*p]~)*$x$^2+([q1]~)*$x$+([r1]~))+([p]~)*$x$))/(sqrt(([p*p]~)*$x$^2+([q1]~)*$x$+([r1]~))+([p]~)*$x$)", "degree==2", "((sqrt(([p*p]~)*$x$^2+([q1]~)*$x$+([r1]~))-sqrt(([p*p]~)*$x$^2+([q2]~)*$x$+([r2]~)))*(sqrt(([p*p]~)*$x$^2+([q1]~)*$x$+([r1]~))+sqrt(([p*p]~)*$x$^2+([q2]~)*$x$+([r2]~))))/(sqrt(([p*p]~)*$x$^2+([q1]~)*$x$+([r1]~))+sqrt(([p*p]~)*$x$^2+([q2]~)*$x$+([r2]~)))", "q2==0&&r2==0&s2==0", "((sqrt(([p*p]~)*$x$^3+([q1]~)*$x$^2+([r1]~)*$x$+([s1]~))-([p]~)*$x$^(3/2))*(sqrt(([p*p]~)*$x$^3+([q1]~)*$x$^2+([r1]~)*$x$+([s1]~))+([p]~)*$x$^(3/2)))/(sqrt($x$)*(sqrt(([p*p]~)*$x$^3+([q1]~)*$x$^2+([r1]~)*$x$+([s1]~))+([p]~)*$x$^(3/2)))", "true", "((sqrt(([p*p]~)*$x$^3+([q1]~)*$x$^2+([r1]~)*$x$+([s1]~))-sqrt(([p*p]~)*$x$^3+([q2]~)*$x$^2+([r2]~)*$x$+([s2]~)))*(sqrt(([p*p]~)*$x$^3+([q1]~)*$x$^2+([r1]~)*$x$+([s1]~))+sqrt(([p*p]~)*$x$^3+([q2]~)*$x$^2+([r2]~)*$x$+([s2]~))))/(sqrt($x$)*(sqrt(([p*p]~)*$x$^3+([q1]~)*$x$^2+([r1]~)*$x$+([s1]~))+sqrt(([p*p]~)*$x$^3+([q2]~)*$x$^2+([r2]~)*$x$+([s2]~))))" }, { "x0==0&&q2==0&&r2==0", "(sqrt(([r1]~)*$x$^2+([q1]~)*$x$+([p*p]~))^2-([p]~)^2)/($x$*(sqrt(([r1]~)*$x$^2+([q1]~)*$x$+([p*p]~))+([p]~)))", "x0==0", "(sqrt(([r1]~)*$x$^2+([q1]~)*$x$+([p*p]~))^2-sqrt(([r2]~)*$x$^2+([q2]~)*$x$+([p*p]~))^2)/($x$*(sqrt(([r1]~)*$x$^2+([q1]~)*$x$+([p*p]~))+sqrt(([r2]~)*$x$^2+([q2]~)*$x$+([p*p]~))))", "degree==1&&q2==0", "(sqrt($x$)*(sqrt(([p*p]~)*$x$+([q1]~))^2-(([p]~)*sqrt($x$))^2)/(sqrt(([p*p]~)*$x$+([q1]~))+([p]~)*sqrt($x$))", "degree==1", "(sqrt($x$)*(sqrt(([p*p]~)*$x$+([q1]~))^2-sqrt(([p*p]~)*$x$+([q2]~))^2)/(sqrt(([p*p]~)*$x$+([q1]~))+sqrt(([p*p]~)*$x$+([q2]~)))", "degree==2&&q2==0&&r2==0", "(sqrt(([p*p]~)*$x$^2+([q1]~)*$x$+([r1]~))^2-([p]~)^2*$x$^2)/(sqrt(([p*p]~)*$x$^2+([q1]~)*$x$+([r1]~))+([p]~)*$x$)", "degree==2", "(sqrt(([p*p]~)*$x$^2+([q1]~)*$x$+([r1]~))^2-sqrt(([p*p]~)*$x$^2+([q2]~)*$x$+([r2]~))^2)/(sqrt(([p*p]~)*$x$^2+([q1]~)*$x$+([r1]~))+sqrt(([p*p]~)*$x$^2+([q2]~)*$x$+([r2]~)))", "q2==0&&r2==0&s2==0", "(sqrt(([p*p]~)*$x$^3+([q1]~)*$x$^2+([r1]~)*$x$+([s1]~))^2-([p]~)^2*$x$^3)/(sqrt($x$)*(sqrt(([p*p]~)*$x$^3+([q1]~)*$x$^2+([r1]~)*$x$+([s1]~))+([p]~)*$x$^(3/2)))", "true", "(sqrt(([p*p]~)*$x$^3+([q1]~)*$x$^2+([r1]~)*$x$+([s1]~))^2-sqrt(([p*p]~)*$x$^3+([q2]~)*$x$^2+([r2]~)*$x$+([s2]~))^2)/(sqrt($x$)*(sqrt(([p*p]~)*$x$^3+([q1]~)*$x$^2+([r1]~)*$x$+([s1]~))+sqrt(([p*p]~)*$x$^3+([q2]~)*$x$^2+([r2]~)*$x$+([s2]~))))" }, { "x0==0&&q2==0&&r2==0", "((([r1]~)*$x$^2+([q1]~)*$x$+([p*p]~))-([p*p]~))/($x$*(sqrt(([r1]~)*$x$^2+([q1]~)*$x$+([p*p]~))+([p]~)))", "x0==0", "((([r1]~)*$x$^2+([q1]~)*$x$+([p*p]~))-(([r2]~)*$x$^2+([q2]~)*$x$+([p*p]~)))/($x$*(sqrt(([r1]~)*$x$^2+([q1]~)*$x$+([p*p]~))+sqrt(([r2]~)*$x$^2+([q2]~)*$x$+([p*p]~))))", "degree==1&&q2==0", "(sqrt($x$)*((([p*p]~)*$x$+([q1]~))-(([p*p]~)*$x$))/(sqrt(([p*p]~)*$x$+([q1]~))+([p]~)*sqrt($x$))", "degree==1", "(sqrt($x$)*((([p*p]~)*$x$+([q1]~))-(([p*p]~)*$x$+([q2]~))))/(sqrt(([p*p]~)*$x$+([q1]~))+sqrt(([p*p]~)*$x$+([q2]~)))", "degree==2&&q2==0&&r2==0", "((([p*p]~)*$x$^2+([q1]~)*$x$+([r1]~))-([p*p]~)*$x$^2)/(sqrt(([p*p]~)*$x$^2+([q1]~)*$x$+([r1]~))+([p]~)*$x$)", "degree==2", "((([p*p]~)*$x$^2+([q1]~)*$x$+([r1]~))-(([p*p]~)*$x$^2+([q2]~)*$x$+([r2]~)))/(sqrt(([p*p]~)*$x$^2+([q1]~)*$x$+([r1]~))+sqrt(([p*p]~)*$x$^2+([q2]~)*$x$+([r2]~)))", "q2==0&&r2==0&s2==0", "((([p*p]~)*$x$^3+([q1]~)*$x$^2+([r1]~)*$x$+([s1]~))-([p*p]~)*$x$^3)/(sqrt($x$)*(sqrt(([p*p]~)*$x$^3+([q1]~)*$x$^2+([r1]~)*$x$+([s1]~))+([p]~)*$x$^(3/2)))", "true", "((([p*p]~)*$x$^3+([q1]~)*$x$^2+([r1]~)*$x$+([s1]~))-(([p*p]~)*$x$^3+([q2]~)*$x$^2+([r2]~)*$x$+([s2]~)))/(sqrt($x$)*(sqrt(([p*p]~)*$x$^3+([q1]~)*$x$^2+([r1]~)*$x$+([s1]~))+sqrt(([p*p]~)*$x$^3+([q2]~)*$x$^2+([r2]~)*$x$+([s2]~))))" }, { "x0==0&&q2==0&&r2==0", "((([r1]~)*$x$^2+([q1]~)*$x$+([p*p]~))-([p*p]~))/($x$*(sqrt(([r1]~)*$x$^2+([q1]~)*$x$+([p*p]~))+([p]~)))=(([r1]~)*$x$+([q1]~))/(sqrt(([r1]~)*$x$^2+([q1]~)*$x$+([p*p]~))+([p]~))", "x0==0", "((([r1]~)*$x$^2+([q1]~)*$x$+([p*p]~))-(([r2]~)*$x$^2+([q2]~)*$x$+([p*p]~)))/($x$*(sqrt(([r1]~)*$x$^2+([q1]~)*$x$+([p*p]~))+sqrt(([r2]~)*$x$^2+([q2]~)*$x$+([p*p]~))))=(([r1-r2]~)*$x$+([q1-q2]~))/(sqrt(([r1]~)*$x$^2+([q1]~)*$x$+([p*p]~))+sqrt(([r2]~)*$x$^2+([q2]~)*$x$+([p*p]~)))", "degree==1&&q2==0", "(sqrt($x$)*((([p*p]~)*$x$+([q1]~))-(([p*p]~)*$x$))/(sqrt(([p*p]~)*$x$+([q1]~))+([p]~)*sqrt($x$))=(([q1]~)*sqrt($x$))/(sqrt(([p*p]~)*$x$+([q1]~))+([p]~)*sqrt($x$))=([q1]~)/(sqrt(([p*p]~)+([q1]~)/$x$)+([p]~))", "degree==1", "(sqrt($x$)*((([p*p]~)*$x$+([q1]~))-(([p*p]~)*$x$+([q2]~))))/(sqrt(([p*p]~)*$x$+([q1]~))+sqrt(([p*p]~)*$x$+([q2]~)))=(([q1-q2]~)*sqrt($x$))/(sqrt(([p*p]~)*$x$+([q1]~))+sqrt(([p*p]~)*$x$+([q2]~)))=([q1-q2]~)/(sqrt(([p*p]~)+([q1]~)/$x$)+sqrt(([p*p]~)+([q2]~)/$x$))", "degree==2&&q2==0&&r2==0", "((([p*p]~)*$x$^2+([q1]~)*$x$+([r1]~))-([p*p]~)*$x$^2)/(sqrt(([p*p]~)*$x$^2+([q1]~)*$x$+([r1]~))+([p]~)*$x$)=(([q1]~)*$x$+([r1]~))/(sqrt(([p*p]~)*$x$^2+([q1]~)*$x$+([r1]~))+([p]~)*$x$)=(([q1]~)+([r1]~)/$x$)/(sqrt(([p*p]~)+([q1]~)/$x$+([r1]~)/$x$^2)+([p]~))", "degree==2", "((([p*p]~)*$x$^2+([q1]~)*$x$+([r1]~))-(([p*p]~)*$x$^2+([q2]~)*$x$+([r2]~)))/(sqrt(([p*p]~)*$x$^2+([q1]~)*$x$+([r1]~))+sqrt(([p*p]~)*$x$^2+([q2]~)*$x$+([r2]~)))=(([q1-q2]~)*$x$+([r1-r2]~))/(sqrt(([p*p]~)*$x$^2+([q1]~)*$x$+([r1]~))+sqrt(([p*p]~)*$x$^2+([q2]~)*$x$+([r2]~)))=(([q1-q2]~)+([r1-r2]~)/$x$)/(sqrt(([p*p]~)+([q1]~)/$x$+([r1]~)/$x$^2)+sqrt(([p*p]~)+([q2]~)/$x$+([r2]~)/$x$^2))", "q2==0&&r2==0&s2==0", "((([p*p]~)*$x$^3+([q1]~)*$x$^2+([r1]~)*$x$+([s1]~))-([p*p]~)*$x$^3)/(sqrt($x$)*(sqrt(([p*p]~)*$x$^3+([q1]~)*$x$^2+([r1]~)*$x$+([s1]~))+([p]~)*$x$^(3/2)))=(([q1]~)*$x$^2+([r1]~)*$x$+([s1]~))/(sqrt($x$)*(sqrt(([p*p]~)*$x$^3+([q1]~)*$x$^2+([r1]~)*$x$+([s1]~))+([p]~)*$x$^(3/2)))=(([q1]~)+([r1]~)/$x$+([s1]~)/$x$^2)/(sqrt(([p*p]~)+([q1]~)/$x$+([r1]~)/$x$^2+([s1]~)/$x$^3)+([p]~))", "true", "((([p*p]~)*$x$^3+([q1]~)*$x$^2+([r1]~)*$x$+([s1]~))-(([p*p]~)*$x$^3+([q2]~)*$x$^2+([r2]~)*$x$+([s2]~)))/(sqrt($x$)*(sqrt(([p*p]~)*$x$^3+([q1]~)*$x$^2+([r1]~)*$x$+([s1]~))+sqrt(([p*p]~)*$x$^3+([q2]~)*$x$^2+([r2]~)*$x$+([s2]~))))=(([q1-q2]~)*$x$^2+([r1-r2]~)*$x$+([s1-s2]~))/(sqrt($x$)*(sqrt(([p*p]~)*$x$^3+([q1]~)*$x$^2+([r1]~)*$x$+([s1]~))+sqrt(([p*p]~)*$x$^3+([q2]~)*$x$^2+([r2]~)*$x$+([s2]~))))=(([q1-q2]~)+([r1-r2]~)/$x$+([s1-s2]~)/$x$^2)/(sqrt(([p*p]~)+([q1]~)/$x$+([r1]~)/$x$^2+([s1]~)/$x$^3)+sqrt(([p*p]~)+([q2]~)/$x$+([r2]~)/$x$^2+([s2]~)/$x$^3))" }, { "true", "([q1-q2]~)/(([p]~)+([p]~))=([(q1-q2)/(p+p)]~)" } };

    private static String[] helpStrings = { "Begin by multiplying the top and bottom of the fraction by", "" };

    private static String[][] helpExpressions = { { "x0==0&&q2==0&&r2==0", "sqrt(([r1]~)*$x$^2+([q1]~)*$x$+([p*p]~))+([p]~)", "x0==0", "sqrt(([r1]~)*$x$^2+([q1]~)*$x$+([p*p]~))+sqrt(([r2]~)*$x$^2+([q2]~)*$x$+([p*p]~))", "degree==1&&q2==0", "sqrt(([p*p]~)*$x$+([q1]~))+([p]~)*sqrt($x$)", "degree==1", "sqrt(([p*p]~)*$x$+([q1]~))+sqrt(([p*p]~)*$x$+([q2]~)", "degree==2&&q2==0&&r2==0", "sqrt(([p*p]~)*$x$^2+([q1]~)*$x$+([r1]~))+([p]~)*$x$", "degree==2", "sqrt(([p*p]~)*$x$^2+([q1]~)*$x$+([r1]~))+sqrt(([p*p]~)*$x$^2+([q2]~)*$x$+([r2]~))", "q2==0&&r2==0&s2==0", "sqrt(([p*p]~)*$x$^3+([q1]~)*$x$^2+([r1]~)*$x$+([s1]~))+([p]~)*$x$^(3/2)", "true", "sqrt(([p*p]~)*$x$^3+([q1]~)*$x$^2+([r1]~)*$x$+([s1]~))+sqrt(([p*p]~)*$x$^3+([q2]~)*$x$^2+([r2]~)*$x$+([s2]~))" } };

    private static double tolerance = 0.00001;

    private static FreeformBranchingQuestionGenerator freeformBranchingQuestionGenerator;

    private static ParameterSet parameterSet;

    public QuestionInterface getExercise() {
        parameterSet = new ParameterSet(parameterNames);
        parameterSet.createParameter("p", pValues);
        parameterSet.createParameter("q1", qrs1Values);
        parameterSet.createParameter("r1", qrs1Values);
        parameterSet.createParameter("s1", qrs1Values);
        parameterSet.createParameter("q2", qrs2Values);
        parameterSet.createParameter("r2", qrs2Values);
        parameterSet.createParameter("s2", qrs2Values);
        parameterSet.createParameter("degree", degreeValues);
        parameterSet.createParameter("x", xValues);
        parameterSet.createParameter("x0", x0Values);
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
        freeformBranchingQuestionGenerator.setNotes("html/calculus/differentiation/diffgradpt.html");
        freeformBranchingQuestionGenerator.setInstructions("html/help/exercise/type1.html");
        return freeformBranchingQuestionGenerator;
    }
}
