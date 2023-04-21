package uk.ac.imperial.ma.metric.exercises.trigonometry;

import uk.ac.imperial.ma.metric.exerciseEngine.classic.ExerciseInterface;
import uk.ac.imperial.ma.metric.exerciseEngine.classic.FreeformBranchingQuestionGenerator;
import uk.ac.imperial.ma.metric.exerciseEngine.classic.QuestionInterface;
import uk.ac.imperial.ma.metric.computerese.classic.ParameterSet;

/**
 * Sum, Difference and Double Angle Trigonometric Identities Exercise 1 data file.
 *
 * @author Daniel J. R. May
 * @version 0.1
 */
public class SumDifferenceIdentities1 implements ExerciseInterface {

    public SumDifferenceIdentities1() {
    }

    public short getExerciseType() {
        return ExerciseInterface.FREE_FORM_TYPE_1;
    }

    private static String[] variables = { "x", "y" };

    private static double[][] variableDomains = { { 0.0, 1.0 }, { 0.0, 1.0 } };

    private static double tolerance = 0.0005;

    private static String[] parameterNames = { "a", "b", "c" };

    private static double[] aValues = { -3.0, -2.0, -1.0, 1.0, 2.0, 3.0 };

    private static double[] bValues = { -3.0, -2.0, -1.0, 1.0, 2.0, 3.0 };

    private static double[] cValues = { 1, 2, 3 };

    private String[] questionStrings = { "Express", "in terms of", "only." };

    private static String[][] questionExpressions = { { "c==1", "sin(([a]~)*x)*cos(([b]~)*y)+cos(([a]~)*x)*sin(([b]~)*y)", "c==2", "cos(([a]~)*x)*cos(([b]~)*y)-sin(([a]~)*x)*sin(([b]~)*y)", "c==3", "(tan(([a]~)*x)+tan(([b]~)*y))/(1-(tan(([a]~)*x)*tan(([b]~)*y)))" }, { "c==1", "sin(f(x,y))", "c==2", "cos(f(x,y))", "c==3", "tan(f(x,y))" } };

    private static String[] correctAnswerExpression = { "c==1", "sin(([a]~)*x+([b]~)*y)", "c==2", "cos(([a]~)*x+([b]~)*y)", "c==3", "tan(([a]~)*x+([b]~)*y)" };

    private static String[] correctAnswerPatterns = { "c==1", "sin(([a]~)*x+([b]~)*y)", "c==2", "cos(([a]~)*x+([b]~)*y)", "c==3", "tan(([a]~)*x+([b]~)*y)" };

    private static String[][] distractorExpressions = { { "c==1", "sin([a]*x)*cos([b]*x)+cos([a]*x)*sin([b]*x)", "c==2", "cos([a]*x)*cos([b]*x)-sin([a]*x)*sin([b]*x)", "c==3", "(tan([a]*x)+tan([b]*x))/(1-(tan([a]*x)*tan([b]*x)))" } };

    private static String[][] analysisStrings = { { "You have just retyped the question. You have to express the answer in terms of " } };

    private static String[][][] analysisExpressions = { { { "c==1", "sin(f(x,y))", "c==2", "cos(f(x,y))", "c==3", "tan(f(x,y))" } } };

    private static String[] workingStrings = { "For this question we must use the following trigonometric identity", "where", "and", "hence", "" };

    private static String[][] workingExpressions = { { "c==1", "sin(A+B)=sin(A)*cos(B)+cos(A)*sin(B)", "c==2", "cos(A+B)=cos(A)*cos(B)-sin(A)*sin(B)", "c==3", "tan(A+B)=(tan(A)+tan(B))/(1-(tan(A)*tan(B)))" }, { "true", "A=([a]~)*x" }, { "true", "B=([b]~)*y" }, { "c==1", "sin([a]*x+[b]*y)=sin(([a]~)*x)*cos(([b]~)*y)+cos(([a]~)*x)*sin(([b]~)*y)", "c==2", "cos([a]*x+[b]*y)=cos(([a]~)*x)*cos(([b]~)*y)-sin(([a]~)*x)*sin(([b]~)*y)", "c==3", "tan([a]*x+[b]*y)=(tan(([a]~)*x)+tan(([b]~)*y))/(1-(tan(([a]~)*x)*tan(([b]~)*y)))" } };

    private static String[] helpStrings = { "Consider the following trigonometric identity", "" };

    private static String[][] helpExpressions = { { "c==1", "sin(A+B)=sin(A)*cos(B)+cos(A)*sin(B)", "c==2", "cos(A+B)=cos(A)*cos(B)-sin(A)*sin(B)", "c==3", "tan(A+B)=(tan(A)+tan(B))/(1-(tan(A)*tan(B)))" } };

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
        freeformBranchingQuestionGenerator = new FreeformBranchingQuestionGenerator();
        freeformBranchingQuestionGenerator.setParameterSet(parameterSet);
        freeformBranchingQuestionGenerator.setQuestions(questionStrings, questionExpressions);
        freeformBranchingQuestionGenerator.setCorrectAnswer(correctAnswerExpression, correctAnswerPatterns);
        freeformBranchingQuestionGenerator.setDistractors(distractorExpressions);
        freeformBranchingQuestionGenerator.setVariables(variables);
        freeformBranchingQuestionGenerator.setVariableDomains(variableDomains);
        freeformBranchingQuestionGenerator.setTolerance(tolerance);
        freeformBranchingQuestionGenerator.setAnalysis(analysisStrings, analysisExpressions);
        freeformBranchingQuestionGenerator.setWorking(workingStrings, workingExpressions);
        freeformBranchingQuestionGenerator.setHelp(helpStrings, helpExpressions);
        return freeformBranchingQuestionGenerator;
    }
}
