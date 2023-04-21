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
public class ExIntSubstitutionIndefinite3 implements ExerciseInterface {

    public ExIntSubstitutionIndefinite3() {
    }

    public short getExerciseType() {
        return ExerciseInterface.FREE_FORM_TYPE_1;
    }

    private static String[] variables = { "x", "theta", "C", "c", "Constant", "constant", "Const", "const", "k", "A" };

    private static double[][] variableDomains = { { 0.0, 0.2 }, { 0.0, 0.2 }, { 1.0, 2.0 }, { 1.0, 2.0 }, { 1.0, 2.0 }, { 1.0, 2.0 }, { 1.0, 2.0 }, { 1.0, 2.0 }, { 1.0, 2.0 }, { 1.0, 2.0 } };

    private static double tolerance = 0.00001;

    private static String[][] parameterNames = { { "a", "asquared", "bOvera", "bOverTwoaCubed", "bOveraCubed" }, { "b" }, { "integrand", "answer", "distractor" } };

    private static String[] aValues = { "1", "sqrt(2)", "sqrt(3)", "2", "3" };

    private static double[] asquaredValues = { 1.0, 2.0, 3.0, 4.0, 9.0 };

    private static String[] bOveraValues = { "([b]~)", "([b/2]~)*sqrt(2)", "([b/3]~)*sqrt(3)", "([b/2]~)", "([b/3]~)" };

    private static String[] bOverTwoaCubedValues = { "([b/2]~)", "([b/8]~)*sqrt(2)", "([b/18]~)*sqrt(3)", "([b/16]~)", "([b/54]~)" };

    private static String[] bOveraCubedValues = { "([b]~)", "([b/4]~)*sqrt(2)", "([b/9]~)*sqrt(3)", "([b/8]~)", "([b/27]~)" };

    private static double[] bValues = { 1.0, 2.0, 3.0, 4.0, 5.0, 6.0 };

    private static String[] integrandValues = { "([b]~)/sqrt(([asquared]~)-x^2)", "([b]~)/(([asquared]~)-x^2)^(3/2)", "([b]~)*sqrt(([asquared]~)-x^2)", "([b]~)/(([asquared]~)+x^2)", "([b]~)/(([asquared]~)+x^2)^2" };

    private static String[] answerValues = { "([b]~)*arcsin(x/($a$~))", "(([b/gcd(b,asquared)]~)*x)/(([asquared/gcd(b,asquared)]~)*sqrt(([asquared]~)-x^2))", "([b*asquared/2]~)*arcsin(x/($a$~))+([b/2]~)*(x*sqrt(([asquared]~)-x^2))", "($bOvera$~)*arctan(x/($a$~))", "($bOverTwoaCubed$)*arctan(x/($a$~))+([b/(2*asquared)]~)**(x/(([asquared]~)+x^2))" };

    private static String[] distractorValues = { "([b]~)*theta", "([b/asquared]~)*tan(theta)", "([b*asquared/2]~)*(theta+1/2*sin(2*theta))", "($bOvera$~)*theta", "($bOverTwoaCubed$~)*(theta+1/2*sin(2*theta))" };

    private static String[] questionStrings = { "Evaluate the following indefinite integral", "Use C as your constant of integration, and simplify fully" };

    private static String[][] questionExpressions = { { "true", "int($integrand$,x)" } };

    private static String[] correctAnswerExpression = { "true", "$answer$+C # " + "$answer$+Constant # " + "$answer$+Const # " + "$answer$+c # " + "$answer$+constant # " + "$answer$+const  # " + "$answer$+k # " + "$answer$+A" };

    private static String[] correctAnswerPatterns = { "true", "?" };

    private static String[][] distractorExpressions = { { "true", "$answer$" }, { "true", "$distractor$+C # " + "$distractor$+Constant # " + "$distractor$+Const # " + "$distractor$+c # " + "$distractor$+constant # " + "$distractor$+const  # " + "$distractor$+k # " + "$distractor$+A #" + "$distractor$" } };

    private static String[][] analysisStrings = { { "You've left out the constant of integration, C." }, { "Your answer is in terms of <i>θ</i>; it should be in terms of <i>x</i>." } };

    private static String[][][] analysisExpressions = { {}, {} };

    private static String[] workingStrings = { "Set", "and note that", "and that", "The integral may be rewritten as", "which evaluates to", "or", "" };

    private static String[][] workingExpressions = { { "integrand<3", "x = ($a$~)*sin(theta)", "true", "x = ($a$~)*tan(theta)" }, { "integrand<3", "sqrt(([asquared]~)-x^2) = sqrt(([asquared]~)-([asquared]~)*sin(theta)^2) = ($a$~)*cos(theta)", "true", "([asquared]~)+x^2=([asquared]~)+([asquared]~)*tan(theta)^2=([asquared]~)*sec(theta)^2" }, { "integrand<3", "differential(x) = ($a$~)*cos(theta)*differential(theta)", "true", "differential(x) = ($a$~)*sec(theta)^2*differential(theta)" }, { "integrand==0", "int((([b]~)/(($a$~)*cos(theta)))**(($a$~)*cos(theta)),theta)=int(([b]~),theta)", "integrand==1", "int((([b]~)/(($a$~)*cos(theta))^3)**($a$~)*cos(theta),theta)=" + "int(([b/asquared]~)*sec(theta)^2,theta)", "integrand==2", "int(([b]~)**(($a$~)*cos(theta))**(($a$~)*cos(theta)),theta)=" + "int(([b*asquared]~)*cos(theta)^2,theta)=" + "int(([b*asquared/2]~)*(1+cos(2*theta)),theta)", "integrand==3", "int((([b]~)/(([asquared]~)*sec(theta)^2))**(($a$~)*sec(theta)^2),theta)=" + "int(($bOvera$~),theta)", "integrand==4", "int((([b]~)/(([asquared*asquared]~)*(sec(theta))^4))**($a$~)*sec(theta)^2,theta)=" + "int(($bOveraCubed$~)*cos(theta)^2,theta)=" + "int(($bOverTwoaCubed$~)*(1+cos(2*theta)),theta)" }, { "integrand==0", "([b]~)*theta+C", "integrand==1", "([b/asquared]~)*tan(theta)+C=([b/asquared]~)*((($a$~)*sin(theta))/(($a$~)*cos(theta)))+C", "integrand==2", "([b*asquared/2]~)*(theta+1/2*sin(2*theta))+C=([b/2])*(([asquared]~)*theta+($a$~)*sin(theta)**($a$~)*cos(theta))", "integrand==3", "($bOvera$~)*theta+C", "integrand==4", "($bOverTwoaCubed$~)*(theta+1/2*sin(2*theta))+C=" + "($bOverTwoaCubed$~)*(theta+sin(theta)*cos(theta))+C=" + "($bOverTwoaCubed$~)*(theta+($a$~)*(($a$~)*(tan(theta))/(([asquared]~)*sec(theta)^2)))+C" }, { "integrand==0", "$answer$+C", "integrand==1", "([b/asquared]~)*(x/sqrt(([asquared]~)-x^2))+C", "integrand==2", "([b/2]~)*([asquared]~)*arcsin(x/($a$~))+x*sqrt(([asquared]~)-x^2))+C", "integrand==3", "$answer$+C", "integrand==4", "($bOverTwoaCubed$~)*(arctan(x/($a$~))+($a$~)*(x/(([asquared]~)+x^2)))+C" } };

    private static String[] helpStrings = { "Set", "and note that", "and that", "" };

    private static String[][] helpExpressions = { { "integrand<3", "x = ($a$~)*sin(theta)", "true", "x = ($a$~)*tan(theta)" }, { "integrand<3", "sqrt(([asquared]~)-x^2) = sqrt(([asquared]~)-([asquared]~)*sin(theta)^2) = ($a$~)*cos(theta)", "true", "([asquared]~)+x^2=([asquared]~)+([asquared]~)*tan(theta)^2=([asquared]~)*sec(theta)^2" }, { "integrand<3", "differential(x) = ($a$~)*cos(theta)*differential(theta)", "true", "differential(x) = ($a$~)*sec(theta)^2*differential(theta)" } };

    private static FreeformBranchingQuestionGenerator freeformBranchingQuestionGenerator;

    private static ParameterSet parameterSet;

    /**
     * this method is called by the <code>ExerciseLoader</code> class.
     */
    public QuestionInterface getExercise() {
        parameterSet = new ParameterSet(parameterNames);
        parameterSet.createParameter("a", aValues);
        parameterSet.createParameter("asquared", asquaredValues);
        parameterSet.createParameter("bOvera", bOveraValues);
        parameterSet.createParameter("bOverTwoaCubed", bOverTwoaCubedValues);
        parameterSet.createParameter("bOveraCubed", bOveraCubedValues);
        parameterSet.createParameter("b", bValues);
        parameterSet.createParameter("integrand", integrandValues);
        parameterSet.createParameter("answer", answerValues);
        parameterSet.createParameter("distractor", distractorValues);
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
        freeformBranchingQuestionGenerator.setNotes("html/calculus/integration/IndefiniteIntegration1/index.html");
        freeformBranchingQuestionGenerator.setInstructions("html/help/exercise/type1.html");
        freeformBranchingQuestionGenerator.setnQuestions(5);
        return freeformBranchingQuestionGenerator;
    }
}
