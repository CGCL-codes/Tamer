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
public class ExQuadSolve2 implements ExerciseInterface {

    public static final String PLATFORM = "TYPE1";

    public static final ExerciseDevelopmentStatus STATUS = ExerciseDevelopmentStatus.UNDER_DEVELOPMENT;

    public static final String TITLE = "Solving Quadratics 2";

    public static final String TOOL_TIP = "Tool tip for Quadratic solving 2";

    private static String[][][] analysisExpressions = { {} };

    private static String[][] analysisStrings = { { "Your answer is mathematically correct, but not in the right form. Perhaps you have some cancelling to do, or some reduction of surds?" } };

    private static String[] correctAnswerExpression = { "floor(sqrt(r))==ceil(sqrt(r))", "[(p+q*sqrt(r))/a],[(p-q*sqrt(r))/a] # [(p-q*sqrt(r))/a] ,[(p+q*sqrt(r))/a] # x==[(p+q*sqrt(r))/a],x==[(p-q*sqrt(r))/a] # x==[(p-q*sqrt(r))/a] ,x==[(p+q*sqrt(r))/a]", "true", "([p/a]~)+([q/a]~)*sqrt([r]),([p/a]~)-([q/a]~)*sqrt([r]) # ([p/a]~)-([q/a]~)*sqrt([r]),([p/a]~)+([q/a]~)*sqrt([r]) # x==([p/a]~)+([q/a]~)*sqrt([r]),x==([p/a]~)-([q/a]~)*sqrt([r]) # x==([p/a]~)-([q/a]~)*sqrt([r]),x==([p/a]~)+([q/a]~)*sqrt([r])" };

    private static String[][] distractorExpressions = { { "true", "(([p]~)+([q]~)*sqrt([r]))/([a]~),(([p]~)-([q]~)*sqrt([r]))/([a]~) # (([p]~)-([q]~)*sqrt([r]))/([a]~),(([p]~)+([q]~)*sqrt([r]))/([a]~) # x==(([p]~)+([q]~)*sqrt([r]))/([a]~),x==(([p]~)-([q]~)*sqrt([r]))/([a]~) # x==(([p]~)-([q]~)*sqrt([r]))/([a]~),x==(([p]~)+([q]~)*sqrt([r]))/([a]~)" } };

    private static FreeformBranchingQuestionGenerator freeformBranchingQuestionGenerator;

    private static String[][] helpExpressions = { { "true", "((x^2-([2*p/a)]~)*x+([(p*p-q*q*r)/(a*a)]~)==0" } };

    private static String[] helpStrings = { "Start by dividing throughout by the coefficient of  <i>x</i><sup>2</sup>, to give" + "Then complete the square, rearrange, and take square roots of both sides " };

    private static String[] parameterNames = { "a", "p", "q", "r", "s" };

    private static ParameterSet parameterSet;

    private static String[][] questionExpressions = { { "true", "([s*a*a/gcd(gcd(a*a,2*p*a),p*p-q*q*r)]~)*x^2-([2*s*p*a/gcd(gcd(a*a,2*p*a),p*p-q*q*r)]~)*x+([s*(p*p-q*q*r)/gcd(gcd(a*a,2*p*a),p*p-q*q*r)]~)==0" }, { "true", "a,b" } };

    private static String[] questionStrings = { "Solve the quadratic equation ", "by completing the square, expressing your answer in the form", "" };

    private static double[] qValues = { 1.0, 2.0, 3.0, 4.0, 5.0 };

    private static double[] rValues = { 1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 9.0, 10.0 };

    private static double tolerance = 0.00001;

    private static double[][] variableDomains = { { -1.0, 2.0 } };

    private static String[] variables = { "x" };

    private static String[][] workingExpressions = { { "true", "([s*a*a/gcd(gcd(a*a,2*p*a),p*p-q*q*r)]~)*x^2-([2*s*p*a/gcd(gcd(a*a,2*p*a),p*p-q*q*r)]~)*x+([s*(p*p-q*q*r)/gcd(gcd(a*a,2*p*a),p*p-q*q*r)]~)==0" }, { "true", "((x^2-([2*p/a)]~)*x+([(p*p-q*q*r)/(a*a)]~)==0" }, { "true", "((x-([p/a)]~))^2-([p*p/(a*a)]~)+([(p*p-q*q*r)/(a*a)]~)==0" }, { "true", "((x-([p/a)]~))^2==([(q*q*r)/(a*a)]~)" }, { "true", "([p/a]~)+sqrt([q*q*r/(a*a)]),([p/a]~)-sqrt([q*q*r/(a*a)])" }, { "floor(sqrt(r))==ceil(sqrt(r))", "[(p+q*sqrt(r))/a],[(p-q*sqrt(r))/a]", "true", "([p/a]~)+([q/a]~)*sqrt([r]),([p/a]~)-([q/a]~)*sqrt([r])" } };

    private static String[] workingStrings = { "We begin by dividing throughoout by the coefficient of <i>x</i><sup>2</sup>. Thus ", "becomes ", "Completing the square gives", "which rearranges to give", "giving us the solutions", "which may be expressed", "" };

    public ExQuadSolve2() {
    }

    public QuestionInterface getExercise() {
        parameterSet = new ParameterSet(parameterNames);
        parameterSet.createParameter("a", 1, 3);
        parameterSet.createParameter("p", -5, 10);
        parameterSet.createParameter("r", rValues);
        parameterSet.createParameter("q", qValues);
        parameterSet.createParameter("s", 1, 3);
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
        freeformBranchingQuestionGenerator.setNotes("html/algebra/quadratics/completing_the_square_solving.html");
        freeformBranchingQuestionGenerator.setInstructions("html/help/exercise/type1.html");
        return freeformBranchingQuestionGenerator;
    }

    public short getExerciseType() {
        return ExerciseInterface.FREE_FORM_TYPE_1;
    }
}
