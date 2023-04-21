package uk.ac.imperial.ma.metric.exercises.vectors;

import uk.ac.imperial.ma.metric.exerciseEngine.classic.ExerciseInterface;
import uk.ac.imperial.ma.metric.exerciseEngine.classic.FreeformBranchingQuestionGenerator;
import uk.ac.imperial.ma.metric.exerciseEngine.classic.QuestionInterface;
import uk.ac.imperial.ma.metric.computerese.classic.ParameterSet;

/**
 * Vector equation of a line exercise data file.
 *
 * @author Phil Ramsden
 * @version 0.1
 */
public class ExVectorsLine3 implements ExerciseInterface {

    public ExVectorsLine3() {
    }

    public short getExerciseType() {
        return ExerciseInterface.FREE_FORM_TYPE_1;
    }

    /** This field is read by reflection by the <code>ExerciseLoader</code> to get a title for the tab containing the exercise's GUI */
    public static final String TITLE = "Vectors: Vector Equation of a Line";

    /** This field is read by reflection by the <code>ExerciseLoader</code> to get a tool tip for the tab containing the exercise's GUI */
    public static final String TOOL_TIP = "Equation of a line through a point parallel to a vector.";

    /** This field is read by reflection by the <code>ExerciseLoader</code> to get know what kind of exercise GUI the data should be used with */
    public static final String PLATFORM = "TYPE1";

    private static String[] variables = { "i", "j", "k", "t", "r" };

    private static String[] vectorVariables = { "i", "j", "k", "r" };

    private static double[][] variableDomains = { { 0.0, 1.0 }, { 0.0, 1.0 }, { 0.0, 1.0 }, { 0.0, 1.0 }, { 0.0, 1.0 } };

    private static double tolerance = 0.00001;

    private static String[][] parameterNames = { { "a1" }, { "b1" }, { "c1" }, { "a2" }, { "b2" }, { "c2" }, { "aSignSymbol1", "aSignNumber1" }, { "bSignSymbol1", "bSignNumber1" }, { "cSignSymbol1", "cSignNumber1" }, { "aSignSymbol2", "aSignNumber2" }, { "bSignSymbol2", "bSignNumber2" }, { "cSignSymbol2", "cSignNumber2" } };

    private static double[] abcValues = { 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0, 10.0, 11.0, 12.0, 13.0, 14.0, 15.0 };

    private static String[] aSignSymbolValues = { "", "-" };

    private static String[] signSymbolValues = { "+", "-" };

    private static double[] signNumberValues = { 1.0, -1.0 };

    private static String[] questionStrings = { "Find, in the form <b>r</b> &times; <b>b</b> = <b>v</b>, a vector equation for the line passing through the point ([aSignNumber1*a1], [bSignNumber1*b1], [cSignNumber1*c1]) parallel to the vector $aSignSymbol2$[a2] <b>i</b> $bSignSymbol2$ [b2] <b>j</b> $cSignSymbol2$ [c2] <b>k</b>. Express your answer in terms of the unit vectors <b>i</b>, <b>j</b> and <b>k</b>.<br><br>Equation: <b>r</b> = ..." };

    private static String[][] questionExpressions = {};

    private static String correctAnswerExpression = "r**(([a2*aSignNumber2]~)*i+([b2*bSignNumber2]~)*j+([c2*cSignNumber2]~)*k) = ([b1*bSignNumber1*c2*cSignNumber2-b2*bSignNumber2*c1*cSignNumber1]~)*i+([c1*cSignNumber1*a2*aSignNumber2-c2*cSignNumber2*a1*aSignNumber1]~)*j+([a1*aSignNumber1*b2*bSignNumber2-a2*aSignNumber2*b1*bSignNumber1]~)*k # r**(([a2*aSignNumber2/gcd(a2,gcd(b2,c2))]~)*i+([b2*bSignNumber2/gcd(a2,gcd(b2,c2))]~)*j+([c2*cSignNumber2/gcd(a2,gcd(b2,c2))]~)*k) = ([(b1*bSignNumber1*c2*cSignNumber2-b2*bSignNumber2*c1*cSignNumber1)/gcd(a2,gcd(b2,c2))]~)*i+([(c1*cSignNumber1*a2*aSignNumber2-c2*cSignNumber2*a1*aSignNumber1)/gcd(a2,gcd(b2,c2))]~)*j+([(a1*aSignNumber1*b2*bSignNumber2-a2*aSignNumber2*b1*bSignNumber1)/gcd(a2,gcd(b2,c2))]~)*k";

    private static String[] distractorExpressions = {};

    private static String[][] analysisStrings = {};

    private static String[][][] analysisExpressions = {};

    private static String[] workingStrings = { "The vectors [b]r[/b] - ($aSignSymbol1$[a1] <b>i</b> $bSignSymbol1$ [b1] <b>j</b> $cSignSymbol1$ [c1] <b>k</b>) and  $aSignSymbol2$[a2] <b>i</b> $bSignSymbol2$ [b2] <b>j</b> $cSignSymbol2$ [c2] <b>k</b> are parallel. It follows that their vector product is zero, and thus that", "" };

    private static String[] workingExpressions = { "r**(([a2*aSignNumber2]~)*i+([b2*bSignNumber2]~)*j+([c2*cSignNumber2]~)*k) = (([a1*aSignNumber1]~)*i+([b1*bSignNumber1]~)*j+([c1*cSignNumber1]~)*k)**(([a2*aSignNumber2]~)*i+([b2*bSignNumber2]~)*j+([c2*cSignNumber2]~)*k) = ([b1*bSignNumber1*c2*cSignNumber2-b2*bSignNumber2*c1*cSignNumber1]~)*i+([c1*cSignNumber1*a2*aSignNumber2-c2*cSignNumber2*a1*aSignNumber1]~)*j+([a1*aSignNumber1*b2*bSignNumber2-a2*aSignNumber2*b1*bSignNumber1]~)*k" };

    private static String[] helpStrings = { "The vectors [b]r[/b] - ($aSignSymbol1$[a1] <b>i</b> $bSignSymbol1$ [b1] <b>j</b> $cSignSymbol1$ [c1] <b>k</b>) and  $aSignSymbol2$[a2] <b>i</b> $bSignSymbol2$ [b2] <b>j</b> $cSignSymbol2$ [c2] <b>k</b> are parallel. What does this tell you about their vector product?" };

    private static String[] helpExpressions = {};

    private static FreeformBranchingQuestionGenerator freeformBranchingQuestionGenerator;

    private static ParameterSet parameterSet;

    /**
     * this method is called by the <code>ExerciseLoader</code> class.
     */
    public QuestionInterface getExercise() {
        parameterSet = new ParameterSet(parameterNames);
        parameterSet.createParameter("a1", abcValues);
        parameterSet.createParameter("b1", abcValues);
        parameterSet.createParameter("c1", abcValues);
        parameterSet.createParameter("a2", abcValues);
        parameterSet.createParameter("b2", abcValues);
        parameterSet.createParameter("c2", abcValues);
        parameterSet.createParameter("aSignSymbol1", aSignSymbolValues);
        parameterSet.createParameter("bSignSymbol1", signSymbolValues);
        parameterSet.createParameter("cSignSymbol1", signSymbolValues);
        parameterSet.createParameter("aSignNumber1", signNumberValues);
        parameterSet.createParameter("bSignNumber1", signNumberValues);
        parameterSet.createParameter("cSignNumber1", signNumberValues);
        parameterSet.createParameter("aSignSymbol2", aSignSymbolValues);
        parameterSet.createParameter("bSignSymbol2", signSymbolValues);
        parameterSet.createParameter("cSignSymbol2", signSymbolValues);
        parameterSet.createParameter("aSignNumber2", signNumberValues);
        parameterSet.createParameter("bSignNumber2", signNumberValues);
        parameterSet.createParameter("cSignNumber2", signNumberValues);
        freeformBranchingQuestionGenerator = new FreeformBranchingQuestionGenerator();
        freeformBranchingQuestionGenerator.setParameterSet(parameterSet);
        freeformBranchingQuestionGenerator.setQuestions(questionStrings, questionExpressions);
        freeformBranchingQuestionGenerator.setCorrectAnswer(correctAnswerExpression);
        freeformBranchingQuestionGenerator.setDistractors(distractorExpressions);
        freeformBranchingQuestionGenerator.setVariables(variables);
        freeformBranchingQuestionGenerator.setVectorVariables(vectorVariables);
        freeformBranchingQuestionGenerator.setVariableDomains(variableDomains);
        freeformBranchingQuestionGenerator.setTolerance(tolerance);
        freeformBranchingQuestionGenerator.setAnalysis(analysisStrings, analysisExpressions);
        freeformBranchingQuestionGenerator.setWorking(workingStrings, workingExpressions);
        freeformBranchingQuestionGenerator.setHelp(helpStrings, helpExpressions);
        freeformBranchingQuestionGenerator.setNotes("html/geometry/AlevelCoordGeom/equation of a straight line.htm");
        freeformBranchingQuestionGenerator.setInstructions("html/help/exercise/type1.html");
        return freeformBranchingQuestionGenerator;
    }
}
