package uk.ac.imperial.ma.metric.exercises.calculus.differentiation;

import uk.ac.imperial.ma.metric.exerciseEngine.classic.ExerciseInterface;
import uk.ac.imperial.ma.metric.exerciseEngine.classic.FreeformBranchingQuestionGenerator;
import uk.ac.imperial.ma.metric.exerciseEngine.classic.QuestionInterface;
import uk.ac.imperial.ma.metric.computerese.classic.ParameterSet;

/**
 * Differentiation exercise.
 * @author Phil Ramsden
 * @author Daniel J. R. May
 * @version 0.1
 */
public class ExDiffChain1 implements ExerciseInterface {

    public ExDiffChain1() {
    }

    public short getExerciseType() {
        return ExerciseInterface.FREE_FORM_TYPE_1;
    }

    public static final String TITLE = "Chain";

    public static final String TOOL_TIP = "Tool tip for Chain";

    public static final String PLATFORM = "TYPE1";

    private static String[] variables = { "x", "u" };

    private static double[][] variableDomains = { { 0.8, 0.4 }, { 0.8, 0.4 } };

    private static String[][] parameterNames = { { "f1", "f2", "fp1", "fp2" }, { "g", "gp" }, { "m" }, { "n" }, { "a" } };

    private static double[] mnValues = { 2.0, 3.0, 4.0, 5.0 };

    private static String[] f1Values = { "sin", "cos", "e^", "ln", "" };

    private static String[] f2Values = { "", "", "", "", "^([m]~)" };

    private static String[] fp1Values = { "cos", "-sin", "e^", "1/", "([m]~)*" };

    private static String[] fp2Values = { "", "", "", "", "^([m-1]~)" };

    private static String[] gValues = { "sin(([a]~)*x)", "cos(([a]~)*x)", "e^(([a]~)*x)", "ln(([a]~)*x)", "([a]~)*x^([n]~)" };

    private static String[] gpValues = { "cos(([a]~)*x)", "-sin(([a]~)*x)", "e^(([a]~)*x)", "1/x", "([n]~)*x^([n-1]~)" };

    private static String[] questionStrings = { "Using the chain rule, differentiate", "with respect to <i>x</i?" };

    private static String[][] questionExpressions = { { "f1==4&&g==4", "(1+([a]~)*x^([n]))^([m])", "f1==3&&g==4", "ln(1+([a]~)*x^([n]))", "f1==4&&g==2", "(1+e^(([a]~)*x))^([m])", "f1==2&&g==3", "ln(1-e^(-([a]~)*x))", "f1==3&&g==2", "ln(1+e^(([a]~)*x))", "true", "$f1$($g$)$f2$" } };

    private static String[] correctAnswerExpression = { "f1==4&&g==4", "([a*m*n]~)*x^([n-1]~)*(1+([a]~)*x^([n]))^([m-1]~)", "f1==3&&g==4", "(([a*n]~)*x^([n-1]~))/(1+([a]~)*x^([n]))", "f1==4&&g==3", "(([m])*ln(([a]~)*x)^([m-1]~))/x", "f1==4&&g==2", "([a*m])*e^x*(1+e^(([a]~)*x))^([m-1]~)", "f1==2&&g==3", "([a]~)*e^(-([a]~)*x)/(1-e^(-([a]~)*x))", "f1==3&&g==2", "([a]~)*e^(([a]~)*x)/(1+e^(([a]~)*x))", "f1==3&&g==3", "1/(x*ln(([a]~)*x))", "f1==3", "(([a]~)*($gp$))/$g$", "f1==4", "(([a*m])*($gp$))*($g$^([m-1]~))", "g==3", "(($fp1$($g$)))/x", "g==4", "([a*n]~)*x^([n-1]~)*($fp1$($g$))", "true", "([a]~)*(($gp$)*($fp1$($g$)))" };

    private static String[][] distractorExpressions = { { "f1==4&&g==4", "([m])*u^([m-1])*([a*n])*x^([n-1])", "f1==3&&g==4", "(1/u)*(([a*n])*x^([n-1]))", "f1==4&&g==3", "(([m])*u^([m-1]))*([a])/(([a])*x)", "f1==4&&g==2", "([m])*u^([m-1])*([a])*e^(([a])*x)", "f1==2&&g==3", "(1/u)*([a])*e^(-([a])*x)", "f1==3&&g==2", "(1/u)*([a])*e^(([a])*x)", "f1==3&&g==3", "1/(u)*([a])/(([a])*x)", "f1==3", "1/u*(([a])*($gp$))", "f1==4", "([m])*u^([m-1])*(([a])*($gp$))", "g==3", "($fp1$(u))*([a])/(([a])*x)", "g==4", "$fp1$(u)*([a*n])*x^([n-1])", "true", "($fp1$(u))*([a])*(($gp$)" } };

    private static String[][] analysisStrings = { { "Your answer is correct, except that it involves the variable <i>u</i>, which formed no part of the original question, and therefore can't appear in the answer. For <i>u</i>, substitute the expression", "to give", "" } };

    private static String[][][] analysisExpressions = { { { "f1==4&&g==4", "1+([a]~)*x^([n])", "f1==3&&g==4", "1+([a]~)*x^([n])", "f1==4&&g==2", "1+e^(([a]~)*x)", "f1==2&&g==3", "1-e^(-([a]~)*x)", "f1==3&&g==2", "1+e^(([a]~)*x)", "true", "$g$" }, { "f1==4&&g==4", "([a*m*n]~)*x^([n-1]~)*(1+([a]~)*x^([n]))^([m-1]~)", "f1==3&&g==4", "(([a*n]~)*x^([n-1]~))/(1+([a]~)*x^([n]))", "f1==4&&g==3", "(([m])*ln(([a]~)*x)^([m-1]~))/x", "f1==4&&g==2", "([a*m])*e^x*(1+e^(([a]~)*x))^([m-1]~)", "f1==2&&g==3", "([a]~)*e^(-([a]~)*x)/(1-e^(-([a]~)*x))", "f1==3&&g==2", "([a]~)*e^(([a]~)*x)/(1+e^(([a]~)*x))", "f1==3&&g==3", "1/(x*ln(([a]~)*x))", "f1==3", "(([a]~)*($gp$))/$g$", "f1==4", "(([a*m])*($gp$))*($g$^([m-1]~))", "g==3", "(($fp1$($g$)))/x", "g==4", "([a*n]~)*x^([n-1]~)*($fp1$($g$))", "true", "([a]~)*(($gp$)*($fp1$($g$)))" } } };

    private static String[] workingStrings = { "Let", "and let", "Then", "which is equal to", "and hence to", "This simplifies to give", "" };

    private static String[][] workingExpressions = { { "f1==4&&g==4", "u==1+([a]~)*x^([n])", "f1==3&&g==4", "u==1+([a]~)*x^([n])", "f1==4&&g==2", "u==1+e^(([a]~)*x)", "f1==2&&g==3", "u==1-e^(-([a]~)*x)", "f1==3&&g==2", "u==1+e^(([a]~)*x)", "true", "u==$gp$" }, { "f1==2&&g==3", "y==ln(u)", "true", "y==$f1$(u)$f2$" }, { "true", "diff(y,x)==diff(y,u)**diff(u,x)" }, { "f1==4&&g==4", "([m]~)*u^([m-1]~)**([a*n]~)*x^([n-1]~)", "f1==3&&g==4", "(1/u)**(([a*n]~)*x^([n-1]~))", "f1==4&&g==3", "(([m])*u^([m-1]~))**([a]~)/(([a]~)*x)", "f1==4&&g==2", "([m])*u^([m-1]~)**([a]~)*e^(([a]~)*x)", "f1==2&&g==3", "(1/u)**([a]~)*e^(-([a]~)*x)", "f1==3&&g==2", "(1/u)**([a]~)*e^(([a]~)*x)", "f1==3&&g==3", "1/(u)**([a]~)/(([a]~)*x)", "f1==3", "1/u**(([a]~)*($gp$))", "f1==4", "([m])*u^([m-1]~)**(([a]~)*($gp$))", "g==3", "($fp1$(u))**([a]~)/(([a]~)*x)", "g==4", "$fp1$(u)**([a*n]~)*x^([n-1]~)", "true", "($fp1$(u))**([a]~)*(($gp$)" }, { "f1==4&&g==4", "([m]~)*(1+([a]~)*x^([n]))^([m-1]~)**([a*n]~)*x^([n-1]~)", "f1==3&&g==4", "(1/(1+([a]~)*x^([n])))**(([a*n]~)*x^([n-1]~))", "f1==4&&g==3", "(([m])*ln(([a]~)*x)^([m-1]~))**([a]~)/(([a]~)*x)", "f1==4&&g==2", "([m])*(1+e^(([a]~)*x))^([m-1]~)**([a]~)*e^(([a]~)*x)", "f1==2&&g==3", "(1/(1-e^(-([a]~)*x)))**([a]~)*e^(-([a]~)*x)", "f1==3&&g==2", "(1/(1+e^(([a]~)*x)))**([a]~)*e^(([a]~)*x)", "f1==3&&g==3", "1/(ln(([a]~)*x))**([a]~)/(([a]~)*x)", "f1==3", "1/$g$**(([a]~)*($gp$))", "f1==4", "([m])*$g$^([m-1]~)**(([a]~)*($gp$))", "g==3", "($fp1$($g$))**([a]~)/(([a]~)*x)", "g==4", "$fp1$($g$)**([a*n]~)*x^([n-1]~)", "true", "($fp1$($g$))**([a]~)*(($gp$)" }, { "f1==4&&g==4", "([a*m*n]~)*x^([n-1]~)*(1+([a]~)*x^([n]))^([m-1]~)", "f1==3&&g==4", "(([a*n]~)*x^([n-1]~))/(1+([a]~)*x^([n]))", "f1==4&&g==3", "(([m])*ln(([a]~)*x)^([m-1]~))/x", "f1==4&&g==2", "([a*m])*e^x*(1+e^(([a]~)*x))^([m-1]~)", "f1==2&&g==3", "([a]~)*e^(-([a]~)*x)/(1-e^(-([a]~)*x))", "f1==3&&g==2", "([a]~)*e^(([a]~)*x)/(1+e^(([a]~)*x))", "f1==3&&g==3", "1/(x*ln(([a]~)*x))", "f1==3", "(([a]~)*($gp$))/$g$", "f1==4", "(([a*m])*($gp$))*($g$^([m-1]~))", "g==3", "(($fp1$($g$)))/x", "g==4", "([a*n]~)*x^([n-1]~)*($fp1$($g$))", "true", "([a]~)*(($gp$)*($fp1$($g$)))" } };

    private static String[] helpStrings = { "Let", "and let", "Then", "" };

    private static String[][] helpExpressions = { { "f1==4&&g==4", "u==1+([a]~)*x^([n])", "f1==3&&g==4", "u==1+([a]~)*x^([n])", "f1==4&&g==2", "u==1+e^(([a]~)*x)", "f1==2&&g==3", "u==1-e^(-([a]~)*x)", "f1==3&&g==2", "u==1+e^(([a]~)*x)", "true", "u==$gp$" }, { "f1==2&&g==3", "y==ln(u)", "true", "y==$f1$(u)$f2$" }, { "true", "diff(y,x)==diff(y,u)**diff(u,x)" } };

    private static double tolerance = 0.001;

    private static FreeformBranchingQuestionGenerator freeformBranchingQuestionGenerator;

    private static ParameterSet parameterSet;

    public QuestionInterface getExercise() {
        parameterSet = new ParameterSet(parameterNames);
        parameterSet.createParameter("m", mnValues);
        parameterSet.createParameter("n", mnValues);
        parameterSet.createParameter("a", 1, 1);
        parameterSet.createParameter("f1", f1Values);
        parameterSet.createParameter("f2", f2Values);
        parameterSet.createParameter("g", gValues);
        parameterSet.createParameter("fp1", fp1Values);
        parameterSet.createParameter("fp2", fp1Values);
        parameterSet.createParameter("gp", gpValues);
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
        freeformBranchingQuestionGenerator.setNotes("html/calculus/differentiation/diffimplicit.html");
        freeformBranchingQuestionGenerator.setInstructions("html/help/exercise/type1.html");
        return freeformBranchingQuestionGenerator;
    }
}
