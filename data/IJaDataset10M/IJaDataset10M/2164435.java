package uk.ac.imperial.ma.metric.exercises.calculus.differentialEquations;

import uk.ac.imperial.ma.metric.exerciseEngine.classic.ExerciseInterface;
import uk.ac.imperial.ma.metric.exerciseEngine.classic.FreeformBranchingQuestionGenerator;
import uk.ac.imperial.ma.metric.exerciseEngine.classic.QuestionInterface;
import uk.ac.imperial.ma.metric.computerese.classic.ParameterSet;

/**
 * Differential equations exercise.
 * @author Phil Ramsden
 * @author Daniel J. R. May
 * @version 0.1
 */
public class ExactODEs implements ExerciseInterface {

    public ExactODEs() {
    }

    public short getExerciseType() {
        return ExerciseInterface.FREE_FORM_TYPE_1;
    }

    public static final String TITLE = "XN";

    public static final String TOOL_TIP = "Tool tip for XN";

    public static final String PLATFORM = "TYPE1";

    private static String[] variables = { "x", "y", "C", "Constant", "Const", "c", "constant", "const", "k", "A" };

    private static double[][] variableDomains = { { 0.8, 0.4 }, { 0.8, 0.4 }, { 0.8, 0.4 }, { 0.8, 0.4 }, { 0.8, 0.4 }, { 0.8, 0.4 }, { 0.8, 0.4 }, { 0.8, 0.4 }, { 0.8, 0.4 }, { 0.8, 0.4 } };

    private static String[][] parameterNames = { { "f1", "fp1", "fdp1" }, { "c" }, { "op1", "xpower1", "ypower1" }, { "op2", "xpower2", "ypower2" }, { "n1" }, { "n2" }, { "m1" }, { "m2" } };

    private static double[] mnValues = { 2.0, 3.0, 4.0, 5.0 };

    private static double[] cValues = { -5.0, -4.0, -3.0, -2.0, -1.0, 0.0, 1.0, 2.0, 3.0, 4.0, 5.0 };

    private static String[] opValues = { "*", "+" };

    private static String[] ypower1Values = { "([n1]~)", "(0~)" };

    private static String[] xpower1Values = { "([m1]~)", "(0~)" };

    private static String[] ypower2Values = { "([n2]~)", "(0~)" };

    private static String[] xpower2Values = { "([m2]~)", "(0~)" };

    private static String[] f1Values = { "sin", "cos", "e^" };

    private static String[] fp1Values = { "cos", "-sin", "e^" };

    private static String[] fdp1Values = { "-sin", "-cos", "e^" };

    private static String[] questionStrings = { "Calculate the general solution of the differential equation", "Use <i>C</i> for the constant of integration." };

    private static String[][] questionExpressions = { { "true", "( ([m2]~)*x^([m2-1]~)*y^(($ypower2$)~)+([m1]~)*x^([m1-1]~)*y^(($ypower1$)~)*($fp1$(x^([m1]~)$op1$y^([n1]~))))*differential(x) " + "+( ([n2]~)*x^(($xpower2$)~)*y^([n2-1]~) + ([n1]~)*x^(($xpower1$)~)*y^([n1-1]~)*($fp1$(x^([m1]~)$op1$y^([n1]~))) )*differential(y)=0" } };

    private static String[] correctAnswerExpression = { "true", " x^([m2]~)$op2$y^([n2]~) + $f1$( x^([m1]~)$op1$y^([n1]~) )  = C #" + " x^([m2]~)$op2$y^([n2]~) + $f1$( x^([m1]~)$op1$y^([n1]~) )  = Constant #" + " x^([m2]~)$op2$y^([n2]~) + $f1$( x^([m1]~)$op1$y^([n1]~) )  = Const #" + " x^([m2]~)$op2$y^([n2]~) + $f1$( x^([m1]~)$op1$y^([n1]~) )  = c #" + " x^([m2]~)$op2$y^([n2]~) + $f1$( x^([m1]~)$op1$y^([n1]~) )  = constant #" + " x^([m2]~)$op2$y^([n2]~) + $f1$( x^([m1]~)$op1$y^([n1]~) )  = const #" + " x^([m2]~)$op2$y^([n2]~) + $f1$( x^([m1]~)$op1$y^([n1]~) )  = k #" + " x^([m2]~)$op2$y^([n2]~) + $f1$( x^([m1]~)$op1$y^([n1]~) )  = A" };

    private static String[][] distractorExpressions = {};

    private static String[][] analysisStrings = {};

    private static String[][][] analysisExpressions = {};

    private static String[] workingStrings = { "First, we check for exactness. ", "is equal to", "which equates to", "whereas", "is equal to", "which equates to the same thing, and hence the equation is exact.<br><br>" + "Now,", "is equal to", "and", "is equal to", "Overall, therefore, the solution is", "" };

    private static String[][] workingExpressions = { { "true", "partialdiff(,y)*(" + "([m2]~)*x^([m2-1]~)*y^(($ypower2$)~)+" + "([m1]~)*x^([m1-1]~)*y^(($ypower1$)~)*($fp1$(x^([m1]~)$op1$y^([n1]~))))" }, { "op2==0&&op1==0", "([m2]~)*x^([m2-1]~)**([n2]~)*y^([n2-1]~)+" + "([m1]~)*x^([m1-1]~)**([n1]~)*y^([n1-1]~)*($fp1$(x^([m1]~)$op1$y^([n1]~))))+" + "([m1]~)*x^([m1-1]~)**y^([n1]~)**x^([m1]~)**([n1]~)*y^([n1-1])*($fdp1$(x^([m1]~)$op1$y^([n1]~))))", "op2==0&&op1==1", "([m2]~)*x^([m2-1]~)**([n2]~)*y^([n2-1]~)+" + "([m1]~)*x^([m1-1]~)**([n1]~)*y^([n1-1]~)*($fdp1$(x^([m1]~)$op1$y^([n1]~))))", "op2==1&&op1==0", "([m1]~)*x^([m1-1]~)**([n1]~)*y^([n1-1]~)*($fp1$(x^([m1]~)$op1$y^([n1]~))))+" + "([m1]~)*x^([m1-1]~)**y^([n1]~)**x^([m1]~)**([n1]~)*y^([n1-1])*($fdp1$(x^([m1]~)$op1$y^([n1]~))))", "op2==1&&op1==1", "([m1]~)*x^([m1-1]~)**([n1]~)*y^([n1-1]~)*($fdp1$(x^([m1]~)$op1$y^([n1]~))))" }, { "op2==0&&op1==0", "([m2*n2]~)*x^([m2-1]~)*y^([n2-1]~)+" + "([m1*n1]~)*x^([m1-1]~)*y^([n1-1]~)*($fp1$(x^([m1]~)$op1$y^([n1]~))))+" + "([m1*n1]~)*x^([2*m1-1]~)*y^([2*n1-1]~)*($fdp1$(x^([m1]~)$op1$y^([n1]~))))", "op2==0&&op1==1", "([m2*n2]~)*x^([m2-1]~)*y^([n2-1]~)+" + "([m1*n1]~)*x^([m1-1]~)*y^([n1-1]~)*($fdp1$(x^([m1]~)$op1$y^([n1]~))))", "op2==1&&op1==0", "([m1*n1]~)*x^([m1-1]~)*y^([n1-1]~)*($fp1$(x^([m1]~)$op1$y^([n1]~))))+" + "([m1*n1]~)*x^([2*m1-1]~)*y^([2*n1-1]~)*($fdp1$(x^([m1]~)$op1$y^([n1]~))))", "op2==1&&op1==1", "([m1*n1]~)*x^([m1-1]~)*y^([n1-1]~)*($fdp1$(x^([m1]~)$op1$y^([n1]~))))" }, { "true", "partialdiff(,x)*(" + "([n2]~)*x^(($xpower2$)~)*y^([n2-1]~) + " + "([n1]~)*x^(($xpower1$)~)*y^([n1-1]~)*($fp1$(x^([m1]~)$op1$y^([n1]~))) )" }, { "op2==0&&op1==0", "([n2]~)**([m2]~)*x^([m2-1]~)*y^([n2-1]~)+" + "([n1]~)**([m1]~)*x^([m1-1]~)*y^([n1-1]~)*($fp1$(x^([m1]~)$op1$y^([n1]~))))+" + "([n1]~)*x^([m1]~)*y^([n1-1]~)**([m1]~)*x^([m1-1]~)*y^([n1])*($fdp1$(x^([m1]~)$op1$y^([n1]~))))", "op2==0&&op1==1", "([n2]~)**([m2]~)*x^([m2-1]~)*y^([n2-1]~)+" + "([n1]~)**([m1]~)*x^([m1-1]~)*y^([n1-1]~)*($fdp1$(x^([m1]~)$op1$y^([n1]~))))", "op2==1&&op1==0", "([n1]~)**([m1]~)*x^([m1-1]~)*y^([n1-1]~)*($fp1$(x^([m1]~)$op1$y^([n1]~))))+" + "([n1]~)*x^([m1]~)*y^([n1-1]~)**([m1]~)*x^([m1-1]~)*y^([n1])*($fdp1$(x^([m1]~)$op1$y^([n1]~))))", "op2==1&&op1==1", "([n1]~)**([m1]~)*x^([m1-1]~)*y^([n1-1]~)*($fdp1$(x^([m1]~)$op1$y^([n1]~))))" }, { "true", "int(( ([m2]~)*x^([m2-1]~)*y^(($ypower2$)~)+([m1]~)*x^([m1-1]~)*y^(($ypower1$)~)*($fp1$(x^([m1]~)$op1$y^([n1]~)))),x)" }, { "op2==0", "x^([m2]~)$op2$y^([n2]~) + $f1$( x^([m1]~)$op1$y^([n1]~) )+a(y)", "op2==1", "x^([m2]~) + $f1$( x^([m1]~)$op1$y^([n1]~) )+a(y)" }, { "true", "int(( ([n2]~)*x^(($xpower2$)~)*y^([n2-1]~) + ([n1]~)*x^(($xpower1$)~)*y^([n1-1]~)*($fp1$(x^([m1]~)$op1$y^([n1]~))) ),y)" }, { "op2==0", "x^([m2]~)$op2$y^([n2]~) + $f1$( x^([m1]~)$op1$y^([n1]~) )+b(x)", "op2==1", "y^([n2]~) + $f1$( x^([m1]~)$op1$y^([n1]~) )+b(x)" }, { "true", " x^([m2]~)$op2$y^([n2]~) + $f1$( x^([m1]~)$op1$y^([n1]~) )  = C " } };

    private static String[] helpStrings = { "Start by checking that the equation is exact, by showning that", "and", "are equal. Then the solution will be <i>f<i>(<i>x</i>,<i>y</i>)=<i>C</i>, where <i>f<i>(<i>x</i>,<i>y</i>) is", "and", "" };

    private static String[][] helpExpressions = { { "true", "partialdiff(,y)*(" + "([m2]~)*x^([m2-1]~)*y^(($ypower2$)~)+" + "([m1]~)*x^([m1-1]~)*y^(($ypower1$)~)*($fp1$(x^([m1]~)$op1$y^([n1]~))))" }, { "true", "partialdiff(,x)*(" + "([n2]~)*x^(($xpower2$)~)*y^([n2-1]~) + " + "([n1]~)*x^(($xpower1$)~)*y^([n1-1]~)*($fp1$(x^([m1]~)$op1$y^([n1]~))) )" }, { "true", "int(( ([m2]~)*x^([m2-1]~)*y^(($ypower2$)~)+([m1]~)*x^([m1-1]~)*y^(($ypower1$)~)*($fp1$(x^([m1]~)$op1$y^([n1]~)))),x)" }, { "true", "int(( ([n2]~)*x^(($xpower2$)~)*y^([n2-1]~) + ([n1]~)*x^(($xpower1$)~)*y^([n1-1]~)*($fp1$(x^([m1]~)$op1$y^([n1]~))) ),y)" } };

    private static double tolerance = 0.00001;

    private static FreeformBranchingQuestionGenerator freeformBranchingQuestionGenerator;

    private static ParameterSet parameterSet;

    public QuestionInterface getExercise() {
        parameterSet = new ParameterSet(parameterNames);
        parameterSet.createParameter("m1", mnValues);
        parameterSet.createParameter("m2", mnValues);
        parameterSet.createParameter("n1", mnValues);
        parameterSet.createParameter("n2", mnValues);
        parameterSet.createParameter("f1", f1Values);
        parameterSet.createParameter("fp1", fp1Values);
        parameterSet.createParameter("fdp1", fdp1Values);
        parameterSet.createParameter("op1", opValues);
        parameterSet.createParameter("op2", opValues);
        parameterSet.createParameter("xpower1", xpower1Values);
        parameterSet.createParameter("ypower1", ypower1Values);
        parameterSet.createParameter("xpower2", xpower2Values);
        parameterSet.createParameter("ypower2", ypower2Values);
        parameterSet.createParameter("c", cValues);
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
