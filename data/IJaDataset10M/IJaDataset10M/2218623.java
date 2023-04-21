package uk.ac.imperial.ma.metric.exercises.series;

import uk.ac.imperial.ma.metric.exerciseEngine.classic.ExerciseInterface;
import uk.ac.imperial.ma.metric.exerciseEngine.classic.FreeformBranchingQuestionGenerator;
import uk.ac.imperial.ma.metric.exerciseEngine.classic.QuestionInterface;
import uk.ac.imperial.ma.metric.computerese.classic.ParameterSet;

/**
 * Series 11 exercise data file.
 * 
 *
 * @author F. Bresme
 * @version 0.1
 */
public class Series11 implements ExerciseInterface {

    public Series11() {
    }

    public short getExerciseType() {
        return ExerciseInterface.FREE_FORM_TYPE_1;
    }

    /** This field is read by reflection by the <code>ExerciseLoader</code> to get a title for the tab containing the exercise's GUI */
    public static final String TITLE = "Series 11 Test";

    /** This field is read by reflection by the <code>ExerciseLoader</code> to get a tool tip for the tab containing the exercise's GUI */
    public static final String TOOL_TIP = "Series 11 Test";

    /** This field is read by reflection by the <code>ExerciseLoader</code> to get know what kind of exercise GUI the data should be used with */
    public static final String PLATFORM = "TYPE1";

    private static String[] variables = { "x", "y", "C" };

    private static double[][] variableDomains = { { 1.0, 2.0 }, { 1.0, 2.0 }, { -5.0, 5.0 } };

    private static double tolerance = 0.00001;

    private static String[] parameterNames = { "a", "b", "c", "d" };

    private static double[] aValues = { -3, -1, 1, 2, 3 };

    private static double[] bValues = { 1, 2, 3 };

    private static double[] cValues = { 2, 3, 4, 5 };

    private static double[] dValues = { 1, 1, 1, 2, 3 };

    private static String[] questionStrings = { "Find the binomial expansion of the following expression", "" };

    private static String[][] questionExpressions = { { "c==1", "[a] + [b/d] x", "c==2", "([a] + [b/d]~x)^2", "c==3", "([a] + [b/d]~x)^3", "c==4", "([a] + [b/d]~x)^4", "true", "([a] + [b/d]~x)^5" } };

    private static String[] correctAnswerExpression = { "c==1", "([a] + [b/d] * x)", "c==2", "([a^2] + [2*a*(b/d)]*x + [(b/d)^2]*x^2)", "c==3", "([a^3] + [3*a^2*(b/d)]*x + [3*a*(b/d)^2] *x^2 + [(b/d)^3]*x^3)", "c==4", "([a^4]+  [4*a^3*(b/d)]*x + [6*a^2*(b/d)^2]*x^2 +[4*a*(b/d)^3]*x^3+[(b/d)^4]*x^4)", "true", "([a^5]+[5*a^4*(b/d)]*x+[10*a^3*(b/d)^2]*x^2 + [10*a^2*(b/d)^3]*x^3 + [5*a*(b/d)^4]*x^4 + [(b/d)^5]*x^5)" };

    private static String[][] distractorExpressions = { { "c==-d", "([a/(b*(d+1))]~)*x^([d+1]~)", "true", "([(a*d)/(b*(c+d))]~)*x^([(c+d)/d]~)" } };

    private static String[][] analysisStrings = { { "There is not analysis available for this exercise", "" } };

    private static String[][][] analysisExpressions = { { { "c==-d", "([a/(b*(d+1))]~)*x^([d+1]~)+C", "true", "([(a*d)/(b*(c+d))]~)*x^([(c+d)/d]~)+C" } } };

    private static String[] workingStrings = { "This is a test of the working bit" };

    private static String[][] workingExpressions = { { "c==-d", "diff(y,x)=[a/b]~*x^([d]~)", "true", "diff(y,x)=[a/b]~*x^([c/d]~)" } };

    private static String[] helpStrings = { "Try reading the notes." };

    private static String[][] helpExpressions = {};

    private static FreeformBranchingQuestionGenerator freeformBranchingQuestionGenerator;

    private static ParameterSet parameterSet;

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
        freeformBranchingQuestionGenerator.setNotes("html/series/series_binomial/binomial.html");
        freeformBranchingQuestionGenerator.setInstructions("html/help/exercise/type1.html");
        return freeformBranchingQuestionGenerator;
    }
}
