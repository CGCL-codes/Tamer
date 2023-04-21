package uk.ac.imperial.ma.metric.exercises.calculus.differentialEquations;

import uk.ac.imperial.ma.metric.exerciseEngine.classic.ExerciseInterface;
import uk.ac.imperial.ma.metric.exerciseEngine.classic.FreeformBranchingQuestionGenerator;
import uk.ac.imperial.ma.metric.exerciseEngine.classic.QuestionInterface;
import uk.ac.imperial.ma.metric.computerese.classic.ParameterSet;

/**
 * This is an exercise on linear ODEs.
 *
 * @author Phil Ramsden
 * @version 1.0.0
 */
public class LinearODEs0 implements ExerciseInterface {

    /** The labels for the variables used in this exercise. */
    private static String[] variables = { "y", "x", "C", "Constant", "Const", "c", "constant", "const", "k", "A" };

    /** The values the variables can take. */
    private static double[][] variableDomains = { { 0.25, 0.5 }, { 0.25, 0.5 }, { 0.25, 0.5 }, { 0.25, 0.5 }, { 0.25, 0.5 }, { 0.25, 0.5 }, { 0.25, 0.5 }, { 0.25, 0.5 }, { 0.25, 0.5 }, { 0.25, 0.5 } };

    /** The tolerance used when checking answers. So if the real answer if x then answers given as
	x+/-tolerance will be marked correct. */
    private static double tolerance = 0.00001;

    /** The labels for the parameters used in this exercise. */
    private static String[][] parameterNames = { { "m", "dm", "dmoverm", "dmovermsquared", "rhsoverm", "rhsovermsquared", "moverdm", "one", "rhsoverdm", "oneoverm", "intrhsoverm" }, { "rhs", "intrhs", "rhsoverdmstraightrgen", "rhsoverdmstraightrone", "rhsoverdmstraightrtwo", "rhsoverdmreciprgen", "rhsoverdmreciprone", "rhsoverdmreciprtwo", "rhsovermsquaredstraight", "rhsovermsquaredrecip", "intrhsovermstraight", "intrhsovermrecip", "rhsovermstraight", "rhsovermrecip" }, { "eqn" }, { "a" }, { "b" }, { "r", "rhsoverdmstraight", "rhsoverdmrecip" } };

    /** The values this parameter can take. */
    private static String[] mValues = { "x^([r]~)+([a]~)", "1/(x^([r]~)+([a]~))" };

    /** The values this parameter can take. */
    private static String[] oneovermValues = { "1/(x^([r]~)+([a]~))", "x^([r]~)+([a]~)" };

    /** The values this parameter can take. */
    private static String[] rhsovermValues = { "$rhsovermstraight$", "$rhsovermrecip$" };

    /** The values this parameter can take. */
    private static String[] rhsovermsquaredValues = { "$rhsovermsquaredstraight$", "$rhsovermsquaredrecip$" };

    /** The values this parameter can take. */
    private static String[] intrhsovermValues = { "$intrhsovermstraight$", "$intrhsovermrecip$" };

    /** The values this parameter can take. */
    private static String[] rhsoverdmValues = { "$rhsoverdmstraight$", "$rhsoverdmrecip$" };

    /** The values this parameter can take. */
    private static String[] dmValues = { "([r]~)*x^([r-1]~)", "(([-r]~)*x^([r-1]~))/(x^([r]~)+([a]~))^2" };

    /** The values this parameter can take. */
    private static String[] dmovermValues = { "(([r]~)*x^([r-1]~))/(x^([r]~)+([a]~))", "(([-r]~)*x^([r-1]~))/(x^([r]~)+([a]~))" };

    /** The values this parameter can take. */
    private static String[] dmovermsquaredValues = { "(([r]~)*x^([r-1]~))/(x^([r]~)+([a]~))^2", "([-r]~)*x^([r-1]~)" };

    /** The values this parameter can take. */
    private static String[] moverdmValues = { "(x^([r]~)+([a]~))/(([r]~)*x^([r-1]~))", "(x^([r]~)+([a]~))/(([r]~)*x^([r-1]~))" };

    /** The values this parameter can take. */
    private static String[] oneValues = { "(1~)", "(-1~)" };

    /** The values this parameter can take. */
    private static String[] rhsovermstraightValues = { "([b]~)/(x^([r]~)+([a]~))", "([b]~)*x/(x^([r]~)+([a]~))", "([b]~)/(x*(x^([r]~)+([a]~)))", "([b]~)*x^2/(x^([r]~)+([a]~))", "([b]~)/(x^2*(x^([r]~)+([a]~)))", "([b]~)*sqrt(x)/(x^([r]~)+([a]~))", "([b]~)/(sqrt(x)*(x^([r]~)+([a]~)))" };

    /** The values this parameter can take. */
    private static String[] rhsovermrecipValues = { "([b]~)*(x^([r]~)+([a]~))", "([b]~)*x*(x^([r]~)+([a]~))", "([b]~)*(x^([r]~)+([a]~))/x", "([b]~)*x^2*(x^([r]~)+([a]~))", "([b]~)*(x^([r]~)+([a]~))/x^2", "([b]~)*sqrt(x)*(x^([r]~)+([a]~))", "([b]~)*(x^([r]~)+([a]~))/sqrt(x)" };

    /** The values this parameter can take. */
    private static String[] rhsovermsquaredstraightValues = { "([b]~)/(x^([r]~)+([a]~))^2", "([b]~)*x/(x^([r]~)+([a]~))^2", "([b]~)/(x*(x^([r]~)+([a]~))^2)", "([b]~)*x^2/(x^([r]~)+([a]~))^2", "([b]~)/(x^2*(x^([r]~)+([a]~))^2)", "([b]~)*sqrt(x)/(x^([r]~)+([a]~))^2", "([b]~)/(sqrt(x)*(x^([r]~)+([a]~))^2)" };

    /** The values this parameter can take. */
    private static String[] rhsovermsquaredrecipValues = { "([b]~)*(x^([r]~)+([a]~))^2", "([b]~)*x*(x^([r]~)+([a]~))^2", "([b]~)*(x^([r]~)+([a]~))^2/x", "([b]~)*x^2*(x^([r]~)+([a]~))^2", "([b]~)*(x^([r]~)+([a]~))^2/x^2", "([b]~)*sqrt(x)*(x^([r]~)+([a]~))^2", "([b]~)*(x^([r]~)+([a]~))^2/sqrt(x)" };

    /** The values this parameter can take. */
    private static String[] rhsValues = { "([b]~)", "([b]~)*x", "([b]~)/x", "([b]~)*x^2", "([b]~)/x^2", "([b]~)*sqrt(x)", "([b]~)/sqrt(x)" };

    /** The values this parameter can take. */
    private static String[] intrhsValues = { "([b]~)*x", "([b/2]~)*x^2", "([b]~)*ln(x)", "([b/3]~)*x^3", "([-b]~)/x", "([2*b/3]~)*x*sqrt(x)", "([2*b]~)*sqrt(x)" };

    /** The values this parameter can take. */
    private static String[] intrhsovermstraightValues = { "([b]~)*x/(x^([r]~)+([a]~))", "([b/gcd(b,2)]~)*x^2/(([2/gcd(b,2)]~)*(x^([r]~)+([a]~)))", "([b]~)*ln(x)/(x^([r]~)+([a]~))", "([b/gcd(b,3)]~)*x^3/(([3/gcd(b,3)]~)*(x^([r]~)+([a]~)))", "([-b]~)/(x*(x^([r]~)+([a]~)))", "([2*b/gcd(b,3)]~)*x*sqrt(x)/(([3/gcd(b,3)])*(x^([r]~)+([a]~)))", "([2*b]~)*sqrt(x)/(x^([r]~)+([a]~))" };

    /** The values this parameter can take. */
    private static String[] intrhsovermrecipValues = { "([b]~)*x*(x^([r]~)+([a]~))", "([b/2]~)*x^2*(x^([r]~)+([a]~))", "([b/a]~)*ln(x)*(x^([r]~)+([a]~))", "([b/3]~)*x^3*(x^([r]~)+([a]~))", "([-b]~)*(x^([r]~)+([a]~))/x", "([2*b/3]~)*x*sqrt(x)*(x^([r]~)+([a]~))", "([2*b]~)*sqrt(x)*(x^([r]~)+([a]~))" };

    /** The values this parameter can take. */
    private static String[] rhsoverdmstraightrgenValues = { "([b/gcd(b,r)]~)/(([r/gcd(b,r)]~)*x^([r-1]~))", "([b/gcd(b,r)]~)/(([r/gcd(b,r)]~)*x^([r-2]~))", "([b/gcd(b,r)]~)/(([r/gcd(b,r)]~)*(x^([r]~)))", "([b/gcd(b,r)]~)/(([r/gcd(b,r)]~)*x^([r-3]~))", "([b/gcd(b,r)]~)/(([r/gcd(b,r)]~)*(x^([r+1]~)))", "([b/gcd(b,r)]~)/(([r/gcd(b,r)]~)*sqrt(x)*x^([r-2]~))", "([b/gcd(b,r)]~)/(([r/gcd(b,r)]~)*sqrt(x)*(x^([r-1]~)))" };

    /** The values this parameter can take. */
    private static String[] rhsoverdmstraightroneValues = { "([b/gcd(b,r)]~)/(([r/gcd(b,r)]~))", "([b/gcd(b,r)]~)*x/(([r/gcd(b,r)]~))", "([b/gcd(b,r)]~)/(([r/gcd(b,r)]~)*(x^([r]~)))", "([b/gcd(b,r)]~)*x^2/(([r/gcd(b,r)]~))", "([b/gcd(b,r)]~)/(([r/gcd(b,r)]~)*(x^([r+1]~)))", "([b/gcd(b,r)]~)*sqrt(x)/(([r/gcd(b,r)]~))", "([b/gcd(b,r)]~)/(([r/gcd(b,r)]~)*sqrt(x))" };

    /** The values this parameter can take. */
    private static String[] rhsoverdmstraightrtwoValues = { "([b/gcd(b,r)]~)/(([r/gcd(b,r)]~)*x", "([b/gcd(b,r)]~)/(([r/gcd(b,r)]~)", "([b/gcd(b,r)]~)/(([r/gcd(b,r)]~)*(x^([r]~)))", "([b/gcd(b,r)]~)*x/(([r/gcd(b,r)]~))", "([b/gcd(b,r)]~)/(([r/gcd(b,r)]~)*(x^([r+1]~)))", "([b/gcd(b,r)]~)/(([r/gcd(b,r)]~)*sqrt(x)*x^([r-2]~))", "([b/gcd(b,r)]~)/(([r/gcd(b,r)]~)*sqrt(x)*(x^([r-1]~)))" };

    /** The values this parameter can take. */
    private static String[] rhsoverdmreciprgenValues = { "([b/gcd(b,r)]~)*(x^([r]~)+([a]~))^2/(([r/gcd(b,r)]~)*x^([r-1]~))", "([b/gcd(b,r)]~)*(x^([r]~)+([a]~))^2/(([r/gcd(b,r)]~)*x^([r-2]~))", "([b/gcd(b,r)]~)*(x^([r]~)+([a]~))^2/(([r/gcd(b,r)]~)*(x^([r]~)))", "([b/gcd(b,r)]~)*(x^([r]~)+([a]~))^2/(([r/gcd(b,r)]~)*x^([r-3]~))", "([b/gcd(b,r)]~)*(x^([r]~)+([a]~))^2/(([r/gcd(b,r)]~)*(x^([r+1]~)))", "([b/gcd(b,r)]~)*(x^([r]~)+([a]~))^2/(([r/gcd(b,r)]~)*sqrt(x)*x^([r-2]~))", "([b/gcd(b,r)]~)*(x^([r]~)+([a]~))^2/(([r/gcd(b,r)]~)*sqrt(x)*(x^([r-1]~)))" };

    /** The values this parameter can take. */
    private static String[] rhsoverdmreciproneValues = { "([b/gcd(b,r)]~)*(x^([r]~)+([a]~))^2/(([r/gcd(b,r)]~))", "([b/gcd(b,r)]~)*x*(x^([r]~)+([a]~))^2/(([r/gcd(b,r)]~))", "([b/gcd(b,r)]~)*(x^([r]~)+([a]~))^2/(([r/gcd(b,r)]~)*(x^([r]~)))", "([b/gcd(b,r)]~)*x^2*(x^([r]~)+([a]~))^2/(([r/gcd(b,r)]~))", "([b/gcd(b,r)]~)*(x^([r]~)+([a]~))^2/(([r/gcd(b,r)]~)*(x^([r+1]~)))", "([b/gcd(b,r)]~)*(x^([r]~)+([a]~))^2*sqrt(x)/(([r/gcd(b,r)]~))", "([b/gcd(b,r)]~)*(x^([r]~)+([a]~))^2/(([r/gcd(b,r)]~)*sqrt(x)*(x^([r-1]~)))" };

    /** The values this parameter can take. */
    private static String[] rhsoverdmreciprtwoValues = { "([b/gcd(b,r)]~)*(x^([r]~)+([a]~))^2/(([r/gcd(b,r)]~)*x", "([b/gcd(b,r)]~)*(x^([r]~)+([a]~))^2/(([r/gcd(b,r)]~)", "([b/gcd(b,r)]~)*(x^([r]~)+([a]~))^2/(([r/gcd(b,r)]~)*(x^([r]~)))", "([b/gcd(b,r)]~)*x*(x^([r]~)+([a]~))^2/(([r/gcd(b,r)]~))", "([b/gcd(b,r)]~)*(x^([r]~)+([a]~))^2/(([r/gcd(b,r)]~)*(x^([r+1]~)))", "([b/gcd(b,r)]~)*(x^([r]~)+([a]~))^2/(([r/gcd(b,r)]~)*sqrt(x)*x^([r-2]~))", "([b/gcd(b,r)]~)*(x^([r]~)+([a]~))^2/(([r/gcd(b,r)]~)*sqrt(x)*(x^([r-1]~)))" };

    /** The values this parameter can take. */
    private static String[] eqnValues = { "($m$)*diff(y,x)+($dm$)*y=$rhs$", "diff(y,x)+($dmoverm$)*y=$rhsoverm$", "($moverdm$)*diff(y,x)+($one$)*y=$rhsoverdm$", "($oneoverm$)*diff(y,x)+($dmovermsquared$)*y=$rhsovermsquared$" };

    /** The values this parameter can take. */
    private static double[] aValues = { 1.0, 2.0, 3.0, 4.0, 5.0 };

    /** The values this parameter can take. */
    private static double[] rValues = { 1.0, 2.0, 3.0, 4.0, 5.0 };

    /** The values this parameter can take. */
    private static String[] rhsoverdmstraightValues = { "$rhsoverdmstraightrone$", "$rhsoverdmstraightrtwo$", "$rhsoverdmstraightrgen$", "$rhsoverdmstraightrgen$", "$rhsoverdmstraightrgen$" };

    /** The values this parameter can take. */
    private static String[] rhsoverdmrecipValues = { "$rhsoverdmreciprone$", "$rhsoverdmreciprtwo$", "$rhsoverdmreciprgen$", "$rhsoverdmreciprgen$", "$rhsoverdmreciprgen$" };

    /** The values this parameter can take. */
    private static double[] bValues = { -1.0, -2.0, -3.0, -4.0, -5.0, 1.0, 2.0, 3.0, 4.0, 5.0 };

    /** The elements of the <code>questionStrings</code> array are weaved with the 
	elements of the <code>questionExpressions</code> array. The <code>questionStrings</code> 
	array supplies the first and last element of the weave; so we must have 
	<code>questionStrings.length = questionExpressions.length + 1</code>. */
    private static String[][] questionStrings = { { "true", "Find the general solution of the following ordinary differential equation" }, { "true", "in the form <i>y</i> = <i>f</i>(<i>x</i>). Use <i>C</i> for the constant " + "of integration." } };

    /** The <code>questionExpressions</code> array elements alternate between a case and a corresponding 
	expression. */
    private static String[][] questionExpressions = { { "true", "$eqn$" } };

    /** The <code>correctAnswerExpression</code> array elements alternate between a case and a corresponding 
	expression. */
    private static String[] correctAnswerExpression = { "true", "y=$intrhsoverm$+C*($oneoverm$) # $intrhsoverm$+C*($oneoverm$) # y**$m$=$intrhs$+C " + "y=$intrhsoverm$+c*($oneoverm$) # $intrhsoverm$+c*($oneoverm$) # y**$m$=$intrhs$+c " + "y=$intrhsoverm$+Constant*($oneoverm$) # $intrhsoverm$+Constant*($oneoverm$) # y**$m$=$intrhs$+Constant " + "y=$intrhsoverm$+constant*($oneoverm$) # $intrhsoverm$+constant*($oneoverm$) # y**$m$=$intrhs$+constant " + "y=$intrhsoverm$+Const*($oneoverm$) # $intrhsoverm$+Const*($oneoverm$) # y**$m$=$intrhs$+Const " + "y=$intrhsoverm$+const*($oneoverm$) # $intrhsoverm$+const*($oneoverm$) # y**$m$=$intrhs$+const " + "y=$intrhsoverm$+k*($oneoverm$) # $intrhsoverm$+k*($oneoverm$) # y**$m$=$intrhs$+k " + "y=$intrhsoverm$+A*($oneoverm$) # $intrhsoverm$+A*($oneoverm$) # y**$m$=$intrhs$+A " };

    /** The <code>distractorExpressions</code> array elements alternate between a case and a corresponding 
	expression. The expressions are compared with the users answer and if they match then the analysis 
	tab is filled with a weaving of the <code>analysisStrings</code> and <code>analysisExpressions</code>. */
    private static String[][] distractorExpressions = {};

    /** The elements of the <code>analysisStrings</code> array are weaved with the 
	elements of the <code>analysisExpressions</code> array. The <code>analysisStrings</code> 
	array supplies the first and last element of the weave; so we must have 
	<code>analysisStrings.length = analysisExpressions.length + 1</code>. */
    private static String[][] analysisStrings = {};

    /** The <code>analysisExpression</code> array elements alternate between a case and a corresponding 
	expression. */
    private static String[][][] analysisExpressions = {};

    /** The elements of the <code>workingStrings</code> array are weaved with the 
	elements of the <code>workingExpressions</code> array. The <code>workingStrings</code> 
	array supplies the first and last element of the weave; so we must have 
	<code>workingStrings.length = workingExpressions.length + 1</code>. */
    private static String[][] workingStrings = { { "eqn=1", "This equation is already in the form", "true", "We first write the equation in the form" }, { "eqn=1", "", "true", "If we divide throughout by" }, { "eqn=1", "", "true", "we obtain" }, { "true", "Now, to obtain an integrating factor, we first integrate <i>p</i>(<i>x</i>):" }, { "true", "Our integrating factor is therefore" }, { "true", "and the equation becomes" }, { "true", "which may be written" }, { "eqn=0", "You may have noticed that this puts the equation back in its original form. " + "If you happen to spot that in this form the equation is <b>exact</b>, you can, in this case, " + "skip all the above calculations. However, this can be quite hard to spot, and you may " + "prefer to include them.<br><br>" + "This exact equation may be written", "true", "In this form, the equation is <b>exact</b>, and may be written" }, { "true", "Integrating both sides with respect to <i>x</i> gives" }, { "true", "Making <i>y</i> the subject:" }, { "true", "" } };

    /** The <code>workingExpression</code> array elements alternate between a case and a corresponding 
	expression. */
    private static String[][] workingExpressions = { { "true", "diff(y,x)+p(x)*y=q(x)" }, { "eqn=0", "$m$", "eqn=1", "", "eqn=2", "$moverdm$", "eqn=3", "$oneoverm$" }, { "eqn=1", "", "true", "diff(y,x)+($dmoverm$)*y=$rhsoverm$" }, { "true", "int($dmoverm$,x)=ln(abs($m$))+C" }, { "true", "exp(ln($m$)) = $m$" }, { "true", "($m$)*diff(y,x)+($m$)**($dmoverm$)*y=$rhs$" }, { "true", "($m$)*diff(y,x)+($dm$)*y=$rhs$" }, { "true", "diff(,x)*vector((($m$)**y))=$rhs$" }, { "true", "($m$)**y = $intrhs$+C" }, { "true", "y = $intrhsoverm$+C*($oneoverm$)" } };

    /** The elements of the <code>helpStrings</code> array are woven with the 
	elements of the <code>helpExpressions</code> array. The <code>helpStrings</code> 
	array supplies the first and last element of the weave; so we must have 
	<code>helpStrings.length = helpExpressions.length + 1</code>. */
    private static String[] helpStrings = { "First, if necessary, rearrange the equation so that it is in the form", "In this case, that is", "Then, integrate", "and raise <i>e</i> to the power of the result, obtaining the integrating factor", "Multiply throughout by the integrating factor. The resulting equation,", "is <b>exact</b>." };

    /** The <code>helpExpression</code> array elements alternate between a case and a corresponding 
	expression. */
    private static String[] helpExpressions = { "diff(y,x)+p(x)*y=q(x)", "diff(y,x)+($dmoverm$)*y=$rhsoverm$", "p(x)=$dmoverm$", "i(x)=exp(ln($m$))=$m$", "($m$)*diff(y,x)+($m$)**($dmoverm$)*y=$rhs$" };

    /** The question generator for this exercise. */
    private static FreeformBranchingQuestionGenerator freeformBranchingQuestionGenerator;

    /** The <code>ParameterSet</code> for this exercise. */
    private static ParameterSet parameterSet;

    /**
     * Constructors for exercise classes are (almost) always empty.
     */
    public LinearODEs0() {
    }

    /**
     * <code>ExerciseInterface</code> interface method.
     *
     * @return the exercise type as defined by the fields in <code>ExerciseInterface</code> 
     * e.g. <code>FREE_FORM_TYPE_1</code>
     */
    public short getExerciseType() {
        return ExerciseInterface.FREE_FORM_TYPE_1;
    }

    /**
     * <code>ExerciseInterface</code> interface method. This method is called by
     * the <code>ExerciseLoader</code> class. It is also the method which we add 
     * the exercises parameters to the exercises <code>ParameterSet</code> and 
     * set up the question generator.
     *
     * @return the <code>QuestionInterface</code> for this exercise.
     * 
     * 
    	{"m","dm","dmoverm","dmovermsquared","rhsoverm","rhsovermsquared","moverdm","one","rhsoverdm",
    		"oneoverm","intrhsoverm"},
    	{"rhs","intrhs",
    			"rhsovermstraightrgen","rhsovermstraightrone","rhsovermstraightrtwo",
    			"rhsovermreciprgen","rhsovermreciprone","rhsovermreciprtwo",
        		"rhsovermsquaredstraight","rhsovermsquaredrecip",
        		"intrhsovermstraight","intrhsovermrecip",
    		"rhsoverdmstraight","rhsoverdmrecip"},
    	{"eqn"},
    	{"a"},
    	{"b"},
    	{"r","rhsovermstraight","rhsovermrecip"}
    };
     */
    public QuestionInterface getExercise() {
        parameterSet = new ParameterSet(parameterNames);
        parameterSet.createParameter("m", mValues);
        parameterSet.createParameter("dm", dmValues);
        parameterSet.createParameter("dmoverm", dmovermValues);
        parameterSet.createParameter("dmovermsquared", dmovermsquaredValues);
        parameterSet.createParameter("rhsoverm", rhsovermValues);
        parameterSet.createParameter("rhsovermsquared", rhsovermsquaredValues);
        parameterSet.createParameter("moverdm", moverdmValues);
        parameterSet.createParameter("one", oneValues);
        parameterSet.createParameter("rhsoverdm", rhsoverdmValues);
        parameterSet.createParameter("oneoverm", oneovermValues);
        parameterSet.createParameter("intrhsoverm", intrhsovermValues);
        parameterSet.createParameter("rhs", rhsValues);
        parameterSet.createParameter("intrhs", intrhsValues);
        parameterSet.createParameter("rhsovermstraight", rhsovermstraightValues);
        parameterSet.createParameter("rhsovermrecip", rhsovermrecipValues);
        parameterSet.createParameter("rhsovermsquaredstraight", rhsovermsquaredstraightValues);
        parameterSet.createParameter("rhsovermsquaredrecip", rhsovermsquaredrecipValues);
        parameterSet.createParameter("intrhsovermstraight", intrhsovermstraightValues);
        parameterSet.createParameter("intrhsovermrecip", intrhsovermrecipValues);
        parameterSet.createParameter("rhsoverdmstraightrgen", rhsoverdmstraightrgenValues);
        parameterSet.createParameter("rhsoverdmstraightrone", rhsoverdmstraightroneValues);
        parameterSet.createParameter("rhsoverdmstraightrtwo", rhsoverdmstraightrtwoValues);
        parameterSet.createParameter("rhsoverdmreciprgen", rhsoverdmreciprgenValues);
        parameterSet.createParameter("rhsoverdmreciprone", rhsoverdmreciproneValues);
        parameterSet.createParameter("rhsoverdmreciprtwo", rhsoverdmreciprtwoValues);
        parameterSet.createParameter("eqn", eqnValues);
        parameterSet.createParameter("a", aValues);
        parameterSet.createParameter("b", bValues);
        parameterSet.createParameter("r", rValues);
        parameterSet.createParameter("rhsoverdmstraight", rhsoverdmstraightValues);
        parameterSet.createParameter("rhsoverdmrecip", rhsoverdmrecipValues);
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
        return freeformBranchingQuestionGenerator;
    }
}
