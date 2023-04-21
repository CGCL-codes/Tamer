package uk.ac.imperial.ma.metric.exercises.calculus.integration;

import uk.ac.imperial.ma.metric.exerciseEngine.classic.ExerciseInterface;
import uk.ac.imperial.ma.metric.exerciseEngine.classic.FreeformBranchingQuestionGenerator;
import uk.ac.imperial.ma.metric.exerciseEngine.classic.QuestionInterface;
import uk.ac.imperial.ma.metric.computerese.classic.ParameterSet;

/**
 * Integration By Parts 1 exercise data file.
 *
 * @author Phil Ramsden
 * @version 0.1
 */
public class ExIntSubstitutionIndefiniteWithHint2 implements ExerciseInterface {

    public ExIntSubstitutionIndefiniteWithHint2() {
    }

    public short getExerciseType() {
        return ExerciseInterface.FREE_FORM_TYPE_1;
    }

    private static String[] variables = { "x", "C", "c", "Constant", "constant", "Const", "const", "k", "A" };

    private static double[][] variableDomains = { { 0.0, 0.5 }, { 1.0, 2.0 }, { 1.0, 2.0 }, { 1.0, 2.0 }, { 1.0, 2.0 }, { 1.0, 2.0 }, { 1.0, 2.0 }, { 1.0, 2.0 }, { 1.0, 2.0 } };

    private static double tolerance = 0.00001;

    private static String[][] parameterNames = { { "a" }, { "b" }, { "ux", "dux", "duxCoefficient" }, { "yu", "dyu", "yuAnswer1", "yuAnswer2", "dyuIntegrandCoefficient", "dyuIntegrand1", "dyuIntegrand2" } };

    private static double[] aValues = { -6.0, -5.0, -4.0, -3.0, -2.0, -1.0, 1.0, 2.0, 3.0, 4.0, 5.0, 6.0 };

    private static double[] bValues = { 1.0, 2.0, 3.0, 4.0, 5.0 };

    private static String[] uxValues = { "([b]~)+x^2", "([b]~)+x^3", "([b]~)+x^4", "([b]~)-x^2", "([b]~)-x^3", "([b]~)-x^4", "([b-1]~)+sin(x)", "([b-1]~)+cos(x)", "([b]~)+e^x", "([b]~)-sin(x)", "([b]~)-cos(x)" };

    private static String[] duxValues = { "x", "x^2", "x^3", "x", "x^2", "x^3", "cos(x)", "sin(x)", "e^x", "cos(x)", "sin(x)" };

    private static double[] duxCoefficientValues = { 2.0, 3.0, 4.0, -2.0, -3.0, -4.0, 1.0, -1.0, 1.0, -1.0, 1.0 };

    private static String[] yuValues = { "([a]~)/u", "([a]~)*sqrt(u)", "([a]~)*u^(3/2)", "([a]~)*ln(u)", "([a]~)/sqrt(u)", "([a]~)/u^2" };

    private static String[] dyuValues = { "([-a]~)/u^2", "([a/gcd(a,2)]~)/(([2/gcd(a,2)]~)*sqrt(u))", "([3*a/gcd(a,2)]~)*sqrt(u)/([2/gcd(a,2)]~)", "([a]~)/u", "([-a/gcd(a,2)]~)/(([2/gcd(a,2)]~)*u^(3/2))", "([-2*a]~)/u^3" };

    private static String[] yuAnswer1Values = { "([a]~)/(", "([a]~)*sqrt(", "([a]~)*(", "([a]~)*ln(", "([a]~)/sqrt(", "([a]~)/(" };

    private static String[] yuAnswer2Values = { ")", ")", ")^(3/2)", ")", ")", ")^2" };

    private static String[] dyuIntegrandCoefficientValues = { "([-a*duxCoefficient]~)", "([a*duxCoefficient/2]~)", "([3*a*duxCoefficient/2]~)", "([a*duxCoefficient]~)", "([-a*duxCoefficient/2)]~)", "([-2*a*duxCoefficient]~)" };

    private static String[] dyuIntegrand1Values = { "/(", "/sqrt(", "*sqrt(", "/(", "/(", "/(" };

    private static String[] dyuIntegrand2Values = { ")^2", ")", ")", ")", ")^(3/2)", ")^3" };

    private static String[] questionStrings = { "Evaluate the following indefinite integral", "Use <i>C</i> for your constant of integration. <b>Hint</b>: use the substitution", "" };

    private static String[][] questionExpressions = { { "true", "int($dyuIntegrandCoefficient$*($dux$$dyuIntegrand1$$ux$$dyuIntegrand2$),x)" }, { "true", "u=$ux$" } };

    private static String[] correctAnswerExpression = { "true", "$yuAnswer1$$ux$$yuAnswer2$ + C # " + "$yuAnswer1$$ux$$yuAnswer2$ + Constant # " + "$yuAnswer1$$ux$$yuAnswer2$ + Const # " + "$yuAnswer1$$ux$$yuAnswer2$ + c # " + "$yuAnswer1$$ux$$yuAnswer2$ + constant # " + "$yuAnswer1$$ux$$yuAnswer2$ + const # " + "$yuAnswer1$$ux$$yuAnswer2$ + k # " + "$yuAnswer1$$ux$$yuAnswer2$ + A" };

    private static String[][] distractorExpressions = { { "true", "$yuAnswer1$$ux$$yuAnswer2$" } };

    private static String[][] analysisStrings = { { "You forgot to add on a constant of integration <i>C</i>.", "" } };

    private static String[][][] analysisExpressions = { { { "true", "$yuAnswer1$$ux$$yuAnswer2$ + C" } } };

    private static String[] workingStrings = { "Set", "and note that", "The integral may be rewritten as", "which becomes", "This is simply", "or", "" };

    private static String[] workingExpressions = { "u=$ux$", "differential(u) = ([duxCoefficient]~)*$dux$*differential(x)", "int($dyu$*([duxCoefficient]~)*$dux$,x)", "int($dyu$,u)", "$yu$+C", "$yuAnswer1$$ux$$yuAnswer2$ + C" };

    private static String[] helpStrings = { "Set", "and note that", "" };

    private static String[] helpExpressions = { "u=$ux$", "differential(u) = ([duxCoefficient]~)*$dux$*differential(x)" };

    private static FreeformBranchingQuestionGenerator freeformBranchingQuestionGenerator;

    private static ParameterSet parameterSet;

    /**
     * this method is called by the <code>ExerciseLoader</code> class.
     */
    public QuestionInterface getExercise() {
        parameterSet = new ParameterSet(parameterNames);
        parameterSet.createParameter("a", aValues);
        parameterSet.createParameter("b", bValues);
        parameterSet.createParameter("ux", uxValues);
        parameterSet.createParameter("dux", duxValues);
        parameterSet.createParameter("duxCoefficient", duxCoefficientValues);
        parameterSet.createParameter("yu", yuValues);
        parameterSet.createParameter("dyu", dyuValues);
        parameterSet.createParameter("yuAnswer1", yuAnswer1Values);
        parameterSet.createParameter("yuAnswer2", yuAnswer2Values);
        parameterSet.createParameter("dyuIntegrandCoefficient", dyuIntegrandCoefficientValues);
        parameterSet.createParameter("dyuIntegrand1", dyuIntegrand1Values);
        parameterSet.createParameter("dyuIntegrand2", dyuIntegrand2Values);
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
        freeformBranchingQuestionGenerator.setNotes("html/calculus/integration/IndefiniteIntegration1/index.html");
        freeformBranchingQuestionGenerator.setInstructions("html/help/exercise/type1.html");
        return freeformBranchingQuestionGenerator;
    }
}
