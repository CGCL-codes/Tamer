package uk.ac.imperial.ma.metric.exercises.calculus.differentialEquations;

import uk.ac.imperial.ma.metric.computerese.classic.ParameterSet;
import uk.ac.imperial.ma.metric.exerciseEngine.classic.ExerciseInterface;
import uk.ac.imperial.ma.metric.exerciseEngine.classic.FreeformBranchingQuestionGenerator;
import uk.ac.imperial.ma.metric.exerciseEngine.classic.QuestionInterface;

/**
 * Basic ODEs 1 exercise data file.
 *
 * @author Daniel J. R. May
 * @version 0.1
 */
public class BasicODEs1 implements ExerciseInterface {

    /** This field is read by reflection by the <code>ExerciseLoader</code> to get know what kind of exercise GUI the data should be used with */
    public static final String PLATFORM = "TYPE1";

    /** This field is read by reflection by the <code>ExerciseLoader</code> to get a title for the tab containing the exercise's GUI */
    public static final String TITLE = "Ordinary Differential Equations 1";

    /** This field is read by reflection by the <code>ExerciseLoader</code> to get a tool tip for the tab containing the exercise's GUI */
    public static final String TOOL_TIP = "Simple ODEs.";

    private static String[][][] analysisExpressions = { { { "c==-d", "([a/(b*(d+1))]~)*x^([d+1]~)+C", "true", "([(a*d)/(b*(c+d))]~)*x^([(c+d)/d]~)+C" } } };

    private static String[][] analysisStrings = { { "You forgot to add on a constant of integration <?metric-ipmml C ?>.", "" } };

    private static double[] aValues = { -10.0, -9.0, -8.0, -7.0, -6.0, -5.0, -4.0, -3.0, -2.0, -1.0, 1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0, 10.0 };

    private static double[] bValues = { -10.0, -9.0, -8.0, -7.0, -6.0, -5.0, -4.0, -3.0, -2.0, -1.0, 1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0, 10.0 };

    private static String[] correctAnswerExpression = { "c==-d", "([a/(b*(d+1))]~)*x^([d+1]~)+C", "true", "([(a*d)/(b*(c+d))]~)*x^([(c+d)/d]~)+C" };

    private static double[] cValues = { -10.0, -9.0, -8.0, -7.0, -6.0, -5.0, -4.0, -3.0, -2.0, -1.0, 0.0, 1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0, 10.0 };

    private static String[][] distractorExpressions = { { "c==-d", "([a/(b*(d+1))]~)*x^([d+1]~)", "true", "([(a*d)/(b*(c+d))]~)*x^([(c+d)/d]~)" } };

    private static double[] dValues = { -10.0, -9.0, -8.0, -7.0, -6.0, -5.0, -4.0, -3.0, -2.0, -1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0, 10.0 };

    private static FreeformBranchingQuestionGenerator freeformBranchingQuestionGenerator;

    private static String[][] helpExpressions = {};

    private static String[] helpStrings = { "Try integrating both sides with respect to <?metric-ipmml x ?>." };

    private static String[] parameterNames = { "a", "b", "c", "d" };

    private static ParameterSet parameterSet;

    private static String[][] questionExpressions = { { "c==-d", "diff(y,x)=[a/b]~*x^([d]~)", "true", "diff(y,x)=[a/b]~*x^([c/d]~)" } };

    private static String[] questionStrings = { "Find the general solution <?metric-ipmml y=f(x) ?> of the following ordinary differential equation", "Enter your expression for <?metric-ipmml f(x) ?>, using <?metric-ipmml C ?> for your constant of integration." };

    private static double tolerance = 0.00001;

    private static double[][] variableDomains = { { 1.0, 2.0 }, { 1.0, 2.0 }, { -5.0, 5.0 } };

    private static String[] variables = { "y", "x", "C" };

    private static String[][] workingExpressions = { { "c==-d", "diff(y,x)=[a/b]~*x^([d]~)", "true", "diff(y,x)=[a/b]~*x^([c/d]~)" }, { "c==-d", "int(diff(y,x),x)=int([a/b]~*x^([d]~),x)", "true", "int(diff(y,x),x)=int([a/b]~*x^([c/d]~),x)" }, { "c==-d", "y+A=int([a/b]~*x^([d]~),x)", "true", "y+A=int([a/b]~*x^([c/d]~),x)" }, { "c==-d", "y+A=([a/(b*(d+1))]~)*x^([d+1]~)+B", "true", "y+A=([(a*d)/(b*(c+d))]~)*x^([(c+d)/d]~)+B" }, { "c==-d", "y=([a/(b*(d+1))]~)*x^([d+1]~)+C", "true", "y=([(a*d)/(b*(c+d))]~)*x^([(c+d)/d]~)+C" } };

    private static String[] workingStrings = { "Given the differential equation", "we integrate both sides with respect to <?metric-ipmml C ?>", "which simplifies to", "where <?metric-ipmml A ?> is a constant of integration. We now evaluate the integral on the right hand side,", "where <?metric-ipmml B ?> is a constant of integration. Subtracting <?metric-ipmml A ?> from both sides we can combine our constants of integration together leaving us with,", "where <?metric-ipmml C=B-A ?>." };

    public BasicODEs1() {
    }

    /**
     * this method is called by the <code>ExerciseLoader</code> class.
     */
    public QuestionInterface getExercise() {
        parameterSet = new ParameterSet(parameterNames);
        parameterSet.createParameter("a", aValues);
        parameterSet.createParameter("b", bValues);
        parameterSet.createParameter("c", cValues);
        parameterSet.createParameter("d", dValues);
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
        freeformBranchingQuestionGenerator.setNotes("html/calculus/integration/BasicODEs1/index.html");
        freeformBranchingQuestionGenerator.setInstructions("html/help/exercise/type1.html");
        return freeformBranchingQuestionGenerator;
    }

    public short getExerciseType() {
        return ExerciseInterface.FREE_FORM_TYPE_1;
    }
}
