package uk.ac.imperial.ma.metric.exercises.vectors;

import uk.ac.imperial.ma.metric.exerciseEngine.classic.ExerciseInterface;
import uk.ac.imperial.ma.metric.exerciseEngine.classic.FreeformBranchingQuestionGenerator;
import uk.ac.imperial.ma.metric.exerciseEngine.classic.QuestionInterface;
import uk.ac.imperial.ma.metric.computerese.classic.ParameterSet;

/**
 * Basic ODEs 1 exercise data file.
 *
 * @author Phil Ramsden
 * @version 0.1
 */
public class ExVectorsVectorProduct implements ExerciseInterface {

    public ExVectorsVectorProduct() {
    }

    public short getExerciseType() {
        return ExerciseInterface.FREE_FORM_TYPE_1;
    }

    /** This field is read by reflection by the <code>ExerciseLoader</code> to get a title for the tab containing the exercise's GUI */
    public static final String TITLE = "Vectors: Vector Product";

    /** This field is read by reflection by the <code>ExerciseLoader</code> to get a tool tip for the tab containing the exercise's GUI */
    public static final String TOOL_TIP = "Simple Vector Product Exercise.";

    /** This field is read by reflection by the <code>ExerciseLoader</code> to get know what kind of exercise GUI the data should be used with */
    public static final String PLATFORM = "TYPE1";

    private static String[] variables = { "i", "j", "k" };

    private static String[] vectorVariables = { "i", "j", "k" };

    private static double[][] variableDomains = { { 0.0, 0.1 }, { 0.0, 0.1 }, { 0.0, 0.1 } };

    private static double tolerance = 0.00001;

    private static String[][] parameterNames = { { "a" }, { "b" }, { "c" }, { "d" }, { "e" }, { "f" } };

    private static double[] abcdefValues = { -6.0, -5.0, -4.0, -3.0, -2.0, -1.0, 1.0, 2.0, 3.0, 4.0, 5.0, 6.0 };

    private static String[] questionStrings = { "Calculate the vector product of the vectors", "and", "" };

    private static String[] questionExpressions = { "([a]~)*i+([b]~)*j+([c]~)*k", "([d]~)*i+([e]~)*j+([f]~)*k" };

    private static String correctAnswerExpression = "([b*f-c*e]~)*i+([c*d-a*f]~)*j+([a*e-b*d]~)*k";

    private static String[] distractorExpressions = {};

    private static String[][] analysisStrings = {};

    private static String[][][] analysisExpressions = {};

    private static String[] workingStrings = { "The vector product is", "Expanding, we obtain", "At this point, we recall that", "and that", "Our expression therefore becomes", "" };

    private static String[] workingExpressions = { "(([a]~)*i+([b]~)*j+([c]~)*k)**(([d]~)*i+([e]~)*j+([f]~)*k)", "([a]~)**([d]~)*i**i+([a]~)**([e]~)*i**j+([a]~)**([f]~)*i**k +([b]~)**([d]~)*j**i+([b]~)**([e]~)*j**j+([b]~)**([f]~)*j**k +([c]~)**([d]~)*k**i+([c]~)**([e]~)*k**j+([c]~)**([f]~)*k**k", "i**i==j**j==k**k==0", "i**j==-j**i==k,j**k==-k**j==i,k**i==-i**k==j", "([a]~)**([e]~)*k-([a]~)**([f]~)*j -([b]~)**([d]~)*k+([b]~)**([f]~)*i +([c]~)**([d]~)*j-([c]~)**([e]~)*i==([b*f-c*e]~)*i+([c*d-a*f]~)*j+([a*e-b*d]~)*k" };

    private static String[] helpStrings = { "Try expanding the brackets, and then recall that", "and", "Otherwise, represent the vector product as a determinant." };

    private static String[] helpExpressions = { "i**i==j**j==k**k==0", "i**j==-j**i==k,j**k==-k**j==i,k**i==-i**k==j" };

    private static FreeformBranchingQuestionGenerator freeformBranchingQuestionGenerator;

    private static ParameterSet parameterSet;

    /**
     * this method is called by the <code>ExerciseLoader</code> class.
     */
    public QuestionInterface getExercise() {
        parameterSet = new ParameterSet(parameterNames);
        parameterSet.createParameter("a", abcdefValues);
        parameterSet.createParameter("d", abcdefValues);
        parameterSet.createParameter("b", abcdefValues);
        parameterSet.createParameter("c", abcdefValues);
        parameterSet.createParameter("e", abcdefValues);
        parameterSet.createParameter("f", abcdefValues);
        freeformBranchingQuestionGenerator = new FreeformBranchingQuestionGenerator();
        freeformBranchingQuestionGenerator.setParameterSet(parameterSet);
        freeformBranchingQuestionGenerator.setQuestions(questionStrings, questionExpressions);
        freeformBranchingQuestionGenerator.setCorrectAnswer(correctAnswerExpression);
        freeformBranchingQuestionGenerator.setDistractors(distractorExpressions);
        freeformBranchingQuestionGenerator.setVariables(variables);
        freeformBranchingQuestionGenerator.setVariableDomains(variableDomains);
        freeformBranchingQuestionGenerator.setVectorVariables(vectorVariables);
        freeformBranchingQuestionGenerator.setTolerance(tolerance);
        freeformBranchingQuestionGenerator.setAnalysis(analysisStrings, analysisExpressions);
        freeformBranchingQuestionGenerator.setWorking(workingStrings, workingExpressions);
        freeformBranchingQuestionGenerator.setHelp(helpStrings, helpExpressions);
        freeformBranchingQuestionGenerator.setNotes("html/geometry/AlevelCoordGeom/equation of a straight line.htm");
        freeformBranchingQuestionGenerator.setInstructions("html/help/exercise/type1.html");
        return freeformBranchingQuestionGenerator;
    }
}
