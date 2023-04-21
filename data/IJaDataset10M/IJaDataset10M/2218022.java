package uk.ac.imperial.ma.metric.exercises.trigonometry;

import uk.ac.imperial.ma.metric.exerciseEngine.classic.ExerciseInterface;
import uk.ac.imperial.ma.metric.exerciseEngine.classic.FreeformBranchingQuestionGenerator;
import uk.ac.imperial.ma.metric.exerciseEngine.classic.QuestionInterface;
import uk.ac.imperial.ma.metric.computerese.classic.ParameterSet;

/**
 * Inverse Trigononometric Functions Exercise Data File.
 *
 * @author Daniel J. R. May
 * @version 0.1
 */
public class InverseTrigonometricFunctions1 implements ExerciseInterface {

    public InverseTrigonometricFunctions1() {
    }

    public short getExerciseType() {
        return ExerciseInterface.FREE_FORM_TYPE_1;
    }

    private static String[] variables = { "z" };

    private static double[][] variableDomains = { { 1.0, 2.0 } };

    private static double tolerance = 0.0005;

    private static String[] parameterNames = { "a", "b", "x", "y" };

    private static double[] aValues = { 1.0, 2.0, 3.0 };

    private static double[] bValues = { 1.0, 2.0 };

    private static double[] xValues = { 1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0, 10.0 };

    private static double[] yValues = { 1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0, 10.0 };

    private String[] questionStrings = { "Imagine a right-angled triangle with its sides labelled according to the diagram in the documentation panel for this exercise i.e. opposite by <i>x</i>, adjacent by <i>y</i> and hypotenuse by <i>r</i>. Hence given ", "and", "Work out the value of", "in radians correct to 3 decimal places." };

    private static String[][] questionExpressions = { { "a==1", "x=[x]", "a==2", "y=[y]", "a==3", "r={decplaces(sqrt(x*x+y*y),3)}" }, { "a==1 && b==1", "y=[y]", "a==1 && b==2", "r={decplaces(sqrt(x*x+y*y),3)}", "a==2 && b==1", "x=[x]", "a==2 && b==2", "r={decplaces(sqrt(x*x+y*y),3)}", "a==3 && b==1", "x=[x]", "a==3 && b==2", "y=[y]" }, { "true", "theta" } };

    private static String[] correctAnswerExpression = { "a==1 && b==1", "{decplaces(arctan(y/x),3)}", "a==1 && b==2", "{decplaces(arccos(x/(sqrt(x*x+y*y))),3)}", "a==2 && b==1", "{decplaces(arctan(y/x),3)}", "a==2 && b==2", "{decplaces(arcsin(y/(sqrt(x*x+y*y))),3)}", "a==3 && b==1", "{decplaces(arccos(x/(sqrt(x*x+y*y))),3)}", "a==3 && b==2", "{decplaces(arcsin(y/(sqrt(x*x+y*y))),3)}" };

    private static String[][] distractorExpressions = { { "true", "a" }, { "true", "a" } };

    private static String[][] analysisStrings = { { "", "" }, { "" } };

    private static String[][][] analysisExpressions = { { { "true", "a" } }, {} };

    private static String[] workingStrings = { "Starting with the definition", "we then apply", "to both sides, which gives us", "We now simply substitute in our values", "and evaluate (with the use of a calculator in radians mode) to find", "which can then be rounded to 3 decimal places.", "" };

    private static String[][] workingExpressions = { { "a==1 && b==1", "tan(theta)=y/x", "a==1 && b==2", "cos(theta)=x/r", "a==2 && b==1", "tan(theta)=y/x", "a==2 && b==2", "sin(theta)=y/r", "a==3 && b==1", "cos(theta)=x/r", "a==3 && b==2", "sin(theta)=y/r" }, { "a==1 && b==1", "arctan", "a==1 && b==2", "arccos", "a==2 && b==1", "arctan", "a==2 && b==2", "arcsin", "a==3 && b==1", "arccos", "a==3 && b==2", "arcsin" }, { "a==1 && b==1", "theta=arctan(y/x)", "a==1 && b==2", "theta=arccos(x/r)", "a==2 && b==1", "theta=arctan(y/x)", "a==2 && b==2", "theta=arcsin(y/r)", "a==3 && b==1", "theta=arccos(x/r)", "a==3 && b==2", "theta=arcsin(y/r)" }, { "a==1 && b==1", "theta=arctan([y]/[x])", "a==1 && b==2", "theta=arccos([x]/{decplaces(sqrt(x*x+y*y),3)})", "a==2 && b==1", "theta=arctan([y]/[x])", "a==2 && b==2", "theta=arcsin([y]/{decplaces(sqrt(x*x+y*y),3)})", "a==3 && b==1", "theta=arccos([x]/{decplaces(sqrt(x*x+y*y),3)})", "a==3 && b==2", "theta=arcsin([y]/{decplaces(sqrt(x*x+y*y),3)})" }, { "a==1 && b==1", "theta={arctan(y/x)}", "a==1 && b==2", "theta={arccos(x/sqrt(x*x+y*y))}", "a==2 && b==1", "theta={arctan(y/x)}", "a==2 && b==2", "theta={arcsin(y/sqrt(x*x+y*y))}", "a==3 && b==1", "theta={arccos(x/sqrt(x*x+y*y))}", "a==3 && b==2", "theta={arcsin(y/sqrt(x*x+y*y))}" }, { "a==1 && b==1", "theta={decplaces(arctan(y/x),3)}", "a==1 && b==2", "theta={decplaces(arccos(x/sqrt(x*x+y*y)),3)}", "a==2 && b==1", "theta={decplaces(arctan(y/x),3)}", "a==2 && b==2", "theta={decplaces(arcsin(y/sqrt(x*x+y*y)),3)}", "a==3 && b==1", "theta={decplaces(arccos(x/sqrt(x*x+y*y)),3)}", "a==3 && b==2", "theta={decplaces(arcsin(y/sqrt(x*x+y*y)),3)}" } };

    private static String[] helpStrings = { "For this triangle <i>x</i> is the <i>adjacent</i>, <i>y</i> is the <i>opposite</i> and <i>r</i> is the <i>hypotenuse</i>. So the trigonometric functions, in terms of this triangle are", "", "", "Try applying the inverse trigonometric functions arcsin, arccos and arctan to these equations to obtain an expresssion for", "" };

    private static String[][] helpExpressions = { { "true", "sin(theta)=y/r" }, { "true", "cos(theta)=x/r" }, { "true", "tan(theta)=y/x" }, { "true", "theta" } };

    private static FreeformBranchingQuestionGenerator freeformBranchingQuestionGenerator;

    private static ParameterSet parameterSet;

    /**
     * this method is called by the <code>ExerciseLoader</code> class.
     */
    public QuestionInterface getExercise() {
        parameterSet = new ParameterSet(parameterNames);
        parameterSet.createParameter("a", aValues);
        parameterSet.createParameter("b", bValues);
        parameterSet.createParameter("x", xValues);
        parameterSet.createParameter("y", yValues);
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
        freeformBranchingQuestionGenerator.setNotes("html/trigonometry/TrigonometricFunctions1/index.html");
        freeformBranchingQuestionGenerator.setInstructions("html/help/exercise/type1.html");
        return freeformBranchingQuestionGenerator;
    }
}
