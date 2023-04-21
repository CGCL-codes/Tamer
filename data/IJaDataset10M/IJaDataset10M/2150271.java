package uk.ac.imperial.ma.metric.exercises.algebra;

import uk.ac.imperial.ma.metric.computerese.classic.ParameterSet;
import uk.ac.imperial.ma.metric.exerciseEngine.classic.ExerciseDevelopmentStatus;
import uk.ac.imperial.ma.metric.exerciseEngine.classic.ExerciseInterface;
import uk.ac.imperial.ma.metric.exerciseEngine.classic.FreeformBranchingQuestionGenerator;
import uk.ac.imperial.ma.metric.exerciseEngine.classic.QuestionInterface;

/**
 * Completeing the square exercise.
 * 
 * @author Phil Ramsden
 * @author Daniel J. R. May
 * @version 0.1
 */
public class ExQuadFact2a implements ExerciseInterface {

    public static final String PLATFORM = "TYPE1";

    public static final ExerciseDevelopmentStatus STATUS = ExerciseDevelopmentStatus.DEPRECATED;

    public static final String TITLE = "ExQuadFact2";

    public static final String TOOL_TIP = "Tool tip for ExQuadFact2";

    private static String[][][] analysisExpressions = {};

    private static String[][] analysisStrings = {};

    private static String[] correctAnswerExpression = { "q==s", "(x+([q]~))^2", "true", "(x+([q]~))*(x+([s]~))" };

    private static String[][] correctAnswerPatterns = { { "q==s" }, { "(x+([q]~))^2", "(x+([q]~))*(x+([s]~))" }, { "true" }, { "(x+([q]~))*(x+([s]~))", "(x+([s]~))*(x+([q]~))" } };

    private static String[][] distractorExpressions = {};

    private static String[][][] distractorPatterns = {};

    private static FreeformBranchingQuestionGenerator freeformBranchingQuestionGenerator;

    private static String[][] helpExpressions = {};

    private static String[] helpStrings = { "No hint is available." };

    private static String[] parameterNames = { "q", "s" };

    private static ParameterSet parameterSet;

    private static double[] qsValues = { -12.0, -11.0, -10.0, -9.0, -8.0, -7.0, -6.0, -5.0, -4.0, -3.0, -2.0, -1.0, 1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0, 10.0, 11.0, 12.0 };

    private static String[][] questionExpressions = { { "true", "x^2+([q+s]~)*x+([q*s]~)" } };

    private static String[] questionStrings = { "Factorise the quadratic expression ", "" };

    private static double tolerance = 0.00001;

    private static double[][] variableDomains = { { -1.0, 2.0 } };

    private static String[] variables = { "x" };

    private static String[][] workingExpressions = {};

    private static String[] workingStrings = { "No worked solution is available." };

    public ExQuadFact2a() {
    }

    public QuestionInterface getExercise() {
        parameterSet = new ParameterSet(parameterNames);
        parameterSet.createParameter("q", qsValues);
        parameterSet.createParameter("s", qsValues);
        freeformBranchingQuestionGenerator = new FreeformBranchingQuestionGenerator();
        freeformBranchingQuestionGenerator.setParameterSet(parameterSet);
        freeformBranchingQuestionGenerator.setQuestions(questionStrings, questionExpressions);
        freeformBranchingQuestionGenerator.setCorrectAnswer(correctAnswerExpression, correctAnswerPatterns);
        freeformBranchingQuestionGenerator.setDistractors(distractorExpressions, distractorPatterns);
        freeformBranchingQuestionGenerator.setVariables(variables);
        freeformBranchingQuestionGenerator.setVariableDomains(variableDomains);
        freeformBranchingQuestionGenerator.setTolerance(tolerance);
        freeformBranchingQuestionGenerator.setAnalysis(analysisStrings, analysisExpressions);
        freeformBranchingQuestionGenerator.setWorking(workingStrings, workingExpressions);
        freeformBranchingQuestionGenerator.setHelp(helpStrings, helpExpressions);
        freeformBranchingQuestionGenerator.setNotes("html/algebra/quadratics/factorisation.html");
        freeformBranchingQuestionGenerator.setInstructions("html/help/exercise/type1.html");
        return freeformBranchingQuestionGenerator;
    }

    public short getExerciseType() {
        return ExerciseInterface.FREE_FORM_TYPE_1;
    }
}
