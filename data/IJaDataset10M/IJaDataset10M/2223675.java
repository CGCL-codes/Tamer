package uk.ac.imperial.ma.metric.exercises.series;

import uk.ac.imperial.ma.metric.exerciseEngine.classic.ExerciseInterface;
import uk.ac.imperial.ma.metric.exerciseEngine.classic.FreeformBranchingQuestionGenerator;
import uk.ac.imperial.ma.metric.exerciseEngine.classic.QuestionInterface;
import uk.ac.imperial.ma.metric.computerese.classic.ParameterSet;

/**
 * Series 2 exercise data file.
 * 
 *
 * @author F. Bresme
 * @version 0.1
 */
public class Series2 implements ExerciseInterface {

    public Series2() {
    }

    public short getExerciseType() {
        return ExerciseInterface.FREE_FORM_TYPE_1;
    }

    /** This field is read by reflection by the <code>ExerciseLoader</code> to get a title for the tab containing the exercise's GUI */
    public static final String TITLE = "Series 2 Test";

    /** This field is read by reflection by the <code>ExerciseLoader</code> to get a tool tip for the tab containing the exercise's GUI */
    public static final String TOOL_TIP = "Series 2 Test";

    /** This field is read by reflection by the <code>ExerciseLoader</code> to get know what kind of exercise GUI the data should be used with */
    public static final String PLATFORM = "TYPE1";

    private static String[] variables = { "x", "y", "n" };

    private static double[][] variableDomains = { { 1.0, 2.0 }, { 1.0, 2.0 }, { -5.0, 5.0 } };

    private static double tolerance = 0.00001;

    private static String[] parameterNames = { "a", "b", "c", "d" };

    private static double[] aValues = { -3.0, -1.0, 1.0, 2.0, 3.0 };

    private static double[] bValues = { 2.0, 3.0 };

    private static double[] cValues = { -1.0, 1.0 };

    private static double[] dValues = { 1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0 };

    private static String[] questionStrings = { "Calculate the limit(n->infty) of the following series", "and decide whether the series is divergent" };

    private static String[][] questionExpressions = { { "d==1.0", "(sum(1,infty)(1/n))", "d==2.0", "(sum(0,infty)(1/[b]^n))", "d==3.0", "(sum(2,infty)(1/ln(n)))", "d==4.0", "(sum(2,infty)(n/(n^2 -1))", "d==5.0", "(sum(2,infty)(n/(n-1))", "d==6.0", "(sum(2,infty)(n^2/(n^2-1)))", "d==7.0", "(sum(1,infty)(n^2-(n-3)^2)/(n^2)))", "true", "(sum(1,infty)(1/sqrt(n)))" } };

    private static String[] correctAnswerExpression = { "d==1.0", "0", "d==2.0", "0", "d==3.0", "0", "d==4.0", "0", "d==5.0", "1", "d==6.0", "1", "d==7.0", "0", "true", "0" };

    private static String[][] distractorExpressions = { {} };

    private static String[][] analysisStrings = { { "There is not analysis available for this exercise", "" } };

    private static String[][][] analysisExpressions = { { { "true", "[a]^n" } } };

    private static String[] workingStrings = { "This is a test of the working bit" };

    private static String[][] workingExpressions = { { "true", "[a]^n" } };

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
        freeformBranchingQuestionGenerator.setNotes("html/series/series_conver/series_conver2.html");
        freeformBranchingQuestionGenerator.setInstructions("html/help/exercise/type1.html");
        return freeformBranchingQuestionGenerator;
    }
}
