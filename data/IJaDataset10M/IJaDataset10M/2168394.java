package uk.ac.imperial.ma.metric.exercises.calculus.integration;

import uk.ac.imperial.ma.metric.exerciseEngine.classic.ExerciseInterface;
import uk.ac.imperial.ma.metric.exerciseEngine.classic.FreeformBranchingQuestionGenerator;
import uk.ac.imperial.ma.metric.exerciseEngine.classic.QuestionInterface;
import uk.ac.imperial.ma.metric.computerese.classic.ParameterSet;

/**
 * Integration of trigonometric functions 1 exercise data file.
 *
 * @author Daniel J. R. May
 * @author Phil Ramsden
 * @version 1.0
 */
public class ExIntTrig implements ExerciseInterface {

    public ExIntTrig() {
    }

    public short getExerciseType() {
        return ExerciseInterface.FREE_FORM_TYPE_1;
    }

    private static String[] variables = { "x", "C", "c", "Constant", "constant", "Const", "const", "k", "A" };

    private static double[][] variableDomains = { { 1.0, 2.0 }, { 1.0, 2.0 }, { 1.0, 2.0 }, { 1.0, 2.0 }, { 1.0, 2.0 }, { 1.0, 2.0 }, { 1.0, 2.0 }, { 1.0, 2.0 }, { 1.0, 2.0 } };

    private static double tolerance = 0.00001;

    private static String[][] parameterNames = { { "a" }, { "b" }, { "z", "p", "q", "preanswer", "answer" }, { "type" } };

    private static double[] aValues = { -10.0, -9.0, -8.0, -7.0, -6.0, -5.0, -4.0, -3.0, -2.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0, 10.0 };

    private static double[] bValues = { 1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0, 10.0 };

    private static double[] zValues = { 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 2.0, 2.0, 2.0, 2.0, 2.0, 2.0, 2.0, 2.0, 2.0, 2.0, 3.0, 3.0, 3.0, 3.0, 3.0, 3.0, 4.0, 4.0, 4.0, 4.0, 4.0, 4.0, 5.0, 5.0, 5.0, 5.0, 5.0, 5.0, 6.0, 6.0, 6.0, 6.0, 6.0, 6.0 };

    private static String[] pValues = { "0", "0", "0", "0", "pi/([6*b]~)", "pi/([6*b]~)", "pi/([6*b]~)", "pi/([4*b]~)", "pi/([4*b]~)", "pi/([3*b]~)", "0", "0", "0", "0", "pi/([6*b]~)", "pi/([6*b]~)", "pi/([6*b]~)", "pi/([4*b]~)", "pi/([4*b]~)", "pi/([3*b]~)", "0", "0", "0", "pi/([6*b]~)", "pi/([6*b]~)", "pi/([4*b]~)", "pi/([6*b]~)", "pi/([6*b]~)", "pi/([6*b]~)", "pi/([4*b]~)", "pi/([4*b]~)", "pi/([3*b]~)", "0", "0", "0", "pi/([6*b]~)", "pi/([6*b]~)", "pi/([4*b]~)", "pi/([6*b]~)", "pi/([6*b]~)", "pi/([6*b]~)", "pi/([4*b]~)", "pi/([4*b]~)", "pi/([3*b]~)" };

    private static String[] qValues = { "pi/([6*b]~)", "pi/([4*b]~)", "pi/([3*b]~)", "pi/([2*b]~)", "pi/([4*b]~)", "pi/([3*b]~)", "pi/([2*b]~)", "pi/([3*b]~)", "pi/([2*b]~)", "pi/([2*b]~)", "pi/([6*b]~)", "pi/([4*b]~)", "pi/([3*b]~)", "pi/([2*b]~)", "pi/([4*b]~)", "pi/([3*b]~)", "pi/([2*b]~)", "pi/([3*b]~)", "pi/([2*b]~)", "pi/([2*b]~)", "pi/([6*b]~)", "pi/([4*b]~)", "pi/([3*b]~)", "pi/([4*b]~)", "pi/([3*b]~)", "pi/([3*b]~)", "pi/([4*b]~)", "pi/([3*b]~)", "pi/([2*b]~)", "pi/([3*b]~)", "pi/([2*b]~)", "pi/([2*b]~)", "pi/([6*b]~)", "pi/([4*b]~)", "pi/([3*b]~)", "pi/([4*b]~)", "pi/([3*b]~)", "pi/([3*b]~)", "pi/([4*b]~)", "pi/([3*b]~)", "pi/([2*b]~)", "pi/([3*b]~)", "pi/([2*b]~)", "pi/([2*b]~)" };

    private static String[] preanswerValues = { "([a]~)*(sin(pi/6)-sin(0))=([a]~)*(1/2-0)", "([a]~)*(sin(pi/4)-sin(0))=([a]~)*(1/sqrt(2)-0)", "([a]~)*(sin(pi/3)-sin(0))=([a]~)*(sqrt(3)/2-0)", "([a]~)*(sin(pi/2)-sin(0))=([a]~)*(1-0)", "([a]~)*(sin(pi/4)-sin(pi/6))=([a]~)*(sqrt(2)/2-1/2)", "([a]~)*(sin(pi/3)-sin(pi/6))=([a]~)*(sqrt(3)/2-1/2)", "([a]~)*(sin(pi/2)-sin(pi/6))=([a]~)*(1-1/2)", "([a]~)*(sin(pi/3)-sin(pi/4))=([a]~)*(sqrt(3)/2-sqrt(2)/2)", "([a]~)*(sin(pi/2)-sin(pi/4))=([a]~)*(1-sqrt(2)/2)", "([a]~)*(sin(pi/2)-sin(pi/3))=([a]~)*(1-sqrt(3)/2)", "([a]~)*(cos(pi/6)-cos(0))=([a]~)*(sqrt(3)/2-1)", "([a]~)*(cos(pi/4)-cos(0))=([a]~)*(sqrt(2)/2-1)", "([a]~)*(cos(pi/3)-cos0))=([a]~)*(1/2-1)", "([a]~)*(cos(pi/2)-cos(0))=([a]~)*(0-1)", "([a]~)*(cos(pi/4)-cos(pi/6))=([a]~)*(sqrt(2)/2-sqrt(3)/2)", "([a]~)*(cos(pi/3)-cos(pi/6))=([a]~)*(1/2-sqrt(3)/2)", "([a]~)*(cos(pi/2)-cos(pi/6))=([a]~)*(0-sqrt(3)/2)", "([a]~)*(cos(pi/3)-cos(pi/4))=([a]~)*(1/2-sqrt(2)/2)", "([a]~)*(cos(pi/2)-cos(pi/4))=([a]~)*(0-sqrt(2)/2)", "([a]~)*(cos(pi/2)-cos(pi/3))=([a]~)*(0-1)/2)", "([a]~)*(tan(pi/6)-tan(0))=([a]~)*(sqrt(3)/3-0)", "([a]~)*(tan(pi/4)-tan(0))=([a]~)*(1-0)", "([a]~)*(tan(pi/3)-tan(0))=([a]~)*(sqrt(3)-0)", "([a]~)*(tan(pi/4)-tan(pi/6))=([a]~)*(1-sqrt(3)/3)", "([a]~)*(tan(pi/3)-tan(pi/6))=([a]~)*(sqrt(3)-sqrt(3)/3)", "([a]~)*(tan(pi/3)-tan(pi/4))", "([a]~)*(cosec(pi/4)-cosec(pi/6))", "([a]~)*(cosec(pi/3)-cosec(pi/6))=([a]~)*(2*sqrt(3)/3-2)", "([a]~)*(cosec(pi/2)-cosec(pi/6))=([a]~)*(1-2)", "([a]~)*(cosec(pi/3)-cosec(pi/4))=([a]~)*(2*sqrt(3)/3-sqrt(2))", "([a]~)*(cosec(pi/2)-cosec(pi/4))", "([a]~)*(cosec(pi/2)-cosec(pi/3))=([a]~)*(1-2*sqrt(3)/3)", "([a]~)*(sec(pi/6)-sec(0))=([a]~)*(2*sqrt(3)/3-1)", "([a]~)*(sec(pi/4)-sec(0))", "([a]~)*(sec(pi/3)-sec(0))=([a]~)*(2-1)", "([a]~)*(sec(pi/4)-sec(pi/6))=([a]~)*(sqrt(2)-2*sqrt(3)/3)", "([a]~)*(sec(pi/3)-sec(pi/6))=([a]~)*(2-2*sqrt(3)/3)", "([a]~)*(sec(pi/3)-sec(pi/4))", "([a]~)*(cot(pi/4)-cot(pi/6))", "([a]~)*(cot(pi/3)-cot(pi/6))=([a]~)*(sqrt(3)/3-sqrt(3))", "([a]~)*(cot(pi/2)-cot(pi/6))=([a]~)*(0-sqrt(3))", "([a]~)*(cot(pi/3)-cot(pi/4))=([a]~)*(sqrt(3)/3-1)", "([a]~)*(cot(pi/2)-cot(pi/4))=([a]~)*(0-1)", "([a]~)*(cot(pi/2)-cot(pi/3))=([a]~)*(0-1/sqrt(3))" };

    private static String[] answerValues = { "([a/2]~)", "([a]~)/sqrt(2)", "{[a/gcd(a,2)]~)*sqrt(3)/([2/gcd(a,2)]~)", "([a]~)", "([a/2]~)*(sqrt(2)-1)", "([a/2]~)*(sqrt(3)-1)", "([a/2]~)", "([a/2]~)*(sqrt(3)-sqrt(2))", "([a/2]~)*(2-sqrt(2))", "([a/2]~)*(2-sqrt(3))", "([-a/2]~)*(2-sqrt(3))", "([-a/2]~)*(2-sqrt(2))", "([-a/2]~)", "([-a]~)", "([-a/2]~)*(sqrt(3)-sqrt(2))", "([-a/2]~)*(sqrt(3)-1)", "{[-a/gcd(a,2)]~)*sqrt(3)/([2/gcd(a,2)]~)", "([-a/2]~)*(sqrt(2)-1)", "([-a]~)/sqrt(2)", "([-a/2]~)", "([a]~)/sqrt(3)", "([a]~)", "([a]~)*sqrt(3)", "([a/3]~)*(3-sqrt(3))", "([2*a]~)/sqrt(3)", "([a]~)*(sqrt(3)-1)", "([-a]~)*(2-sqrt(2))", "([-2*a/3]~)*(3-sqrt(3))", "([-a]~)", "([-a/3]~)*(3*sqrt(2)-2*sqrt(3))", "([-a]~)*(sqrt(2)-1)", "([-a/3]~)*(2*sqrt(3)-3)", "([a/3]~)*(2*sqrt(3)-3)", "([a]~)*(sqrt(2)-1)", "([a]~)", "([a/3]~)*(3*sqrt(2)-2*sqrt(3))", "([2*a/3]~)*(3-sqrt(3))", "([a]~)*(2-sqrt(2))", "([-a]~)*(sqrt(3)-1)", "([-2*a]~)/sqrt(3)", "([-a]~)*sqrt(3)", "([-a/3]~)*(3-sqrt(3))", "([-a]~)", "([-a]~)/sqrt(3)" };

    private static double[] typeValues = { 0.0, 1.0 };

    private static String[][] questionStrings = { { "true", "Evaluate the following integral" }, { "type=0", "Use <i>C</i> for your constant of integration.", "true", "" } };

    private static String[][] questionExpressions = { { "type==1&&z==1", "defint(defintlimits($p$,$q$),defintbody(([a*b]~)*cos(([b]~)*x),x))", "type==1&&z==2", "defint(defintlimits($p$,$q$),defintbody(([-1*a*b]~)*sin(([b]~)*x),x))", "type==1&&z==3", "defint(defintlimits($p$,$q$),defintbody(([a*b]~)*(sec(([b]~)*x))^2,x))", "type==1&&z==4", "defint(defintlimits($p$,$q$),defintbody(([-1*a*b]~)*cosec(([b]~)*x)*cot(([b]~)*x),x))", "type==1&&z==5", "defint(defintlimits($p$,$q$),defintbody(([a*b]~)*sec(([b]~)*x)*tan(([b]~)*x),x))", "type==1&&z==6", "defint(defintlimits($p$,$q$),defintbody(([-1*a*b]~)*(cosec(([b]~)*x))^2,x))", "z==1", "int(([a*b]~)*cos(([b]~)*x),x)", "z==2", "int(([-1*a*b]~)*sin(([b]~)*x),x)", "z==3", "int(([a*b]~)*(sec(([b]~)*x))^2,x)", "z==4", "int(([-1*a*b]~)*cosec(([b]~)*x)*cot(([b]~)*x),x)", "z==5", "int(([a*b]~)*sec(([b]~)*x)*tan(([b]~)*x),x)", "z==6", "int(([-1*a*b]~)*(cosec(([b]~)*x))^2,x)" } };

    private static String[] correctAnswerExpression = { "type==1", "$answer$", "z==1", "([a]~)*sin(([b]~)*x)+C # ([a]~)*sin(([b]~)*x)+Constant # ([a]~)*sin(([b]~)*x)+Const # ([a]~)*sin(([b]~)*x)+c # ([a]~)*sin(([b]~)*x)+constant # ([a]~)*sin(([b]~)*x)+const # ([a]~)*sin(([b]~)*x)+const + k # ([a]~)*sin(([b]~)*x)+A", "z==2", "([a]~)*cos(([b]~)*x)+C # ([a]~)*cos(([b]~)*x)+Constant # ([a]~)*cos(([b]~)*x)+Const # ([a]~)*cos(([b]~)*x)+c # ([a]~)*cos(([b]~)*x)+constant # ([a]~)*cos(([b]~)*x)+const # ([a]~)*cos(([b]~)*x)+const + k # ([a]~)*cos(([b]~)*x)+ A", "z==3", "([a]~)*tan(([b]~)*x)+C # ([a]~)*tan(([b]~)*x)+Constant # ([a]~)*tan(([b]~)*x)+Const # ([a]~)*tan(([b]~)*x)+c # ([a]~)*tan(([b]~)*x)+constant # ([a]~)*tan(([b]~)*x)+const # ([a]~)*tan(([b]~)*x)+const + k # ([a]~)*tan(([b]~)*x)+ A", "z==4", "([a]~)*cosec(([b]~)*x)+C # ([a]~)*cosec(([b]~)*x)+Constant # ([a]~)*cosec(([b]~)*x)+Const # ([a]~)*cosec(([b]~)*x)+c # ([a]~)*cosec(([b]~)*x)+constant # ([a]~)*cosec(([b]~)*x)+const # ([a]~)*cosec(([b]~)*x)+const + k # ([a]~)*cosec(([b]~)*x)+ A", "z==5", "([a]~)*sec(([b]~)*x)+C # ([a]~)*sec(([b]~)*x)+Constant # ([a]~)*sec(([b]~)*x)+Const # ([a]~)*sec(([b]~)*x)+c # ([a]~)*sec(([b]~)*x)+constant # ([a]~)*sec(([b]~)*x)+const # ([a]~)*sec(([b]~)*x)+const + k # ([a]~)*sec(([b]~)*x)+ A", "z==6", "([a]~)*cot(([b]~)*x)+C # ([a]~)*cot(([b]~)*x)+Constant # ([a]~)*cot(([b]~)*x)+Const # ([a]~)*cot(([b]~)*x)+c # ([a]~)*cot(([b]~)*x)+constant # ([a]~)*cot(([b]~)*x)+const # ([a]~)*cot(([b]~)*x)+const + k # ([a]~)*cot(([b]~)*x)+ A" };

    private static String[][] distractorExpressions = { { "type=1", "false", "z==1", "([a]~)*sin(([b]~)*x)", "z==2", "([a]~)*cos(([b]~)*x)", "z==3", "([a]~)*tan(([b]~)*x)", "z==4", "([a]~)*cosec(([b]~)*x)", "z==5", "([a]~)*sec(([b]~)*x)", "z==6", "([a]~)*cot(([b]~)*x)" } };

    private static String[][] analysisStrings = { { "You forgot to add on a constant of integration <i>C</i>.", "" } };

    private static String[][][] analysisExpressions = { { { "z==1", "([a]~)*sin(([b]~)*x)+C", "z==2", "([a]~)*cos(([b]~)*x)+C", "z==3", "([a]~)*tan(([b]~)*x)+C", "z==4", "([a]~)*cosec(([b]~)*x)+C", "z==5", "([a]~)*sec(([b]~)*x)+C", "z==6", "([a]~)*cot(([b]~)*x)+C" } } };

    private static String[][] workingStrings = { { "true", "First, recall that" }, { "true", "Now, note that" }, { "true", "and therefore" }, { "type=1", "This in turn resolves to", "true", "" }, { "true", "" } };

    private static String[][] workingExpressions = { { "z==1", "diff(,x)(sin(a*x))=a*cos(a*x)", "z==2", "diff(,x)(cos(a*x))=-a*sin(a*x)", "z==3", "diff(,x)(tan(a*x))=a*(sec(a*x))^2", "z==4", "diff(,x)(cosec(a*x))=-a*cosec(a*x)*cot(a*x)", "z==5", "diff(,x)(sec(a*x))=a*sec(a*x)*tan(a*x)", "z==6", "diff(,x)(cot(a*x))=-a*(cosec(a*x))^2" }, { "type==1&&z==1", "defint(defintlimits($p$,$q$),defintbody(([a*b]~)*cos(([b]~)*x),x))=([a]~)*defint(defintlimits($p$,$q$),defintbody(([b]~)*cos(([b]~)*x),x))", "type==1&&z==2", "defint(defintlimits($p$,$q$),defintbody(([-1*a*b]~)*sin(([b]~)*x),x))=([a]~)*defint(defintlimits($p$,$q$),defintbody(([-b]~)*sin(([b]~)*x),x))", "type==1&&z==3", "defint(defintlimits($p$,$q$),defintbody(([a*b]~)*(sec(([b]~)*x))^2,x))=([a]~)*defint(defintlimits($p$,$q$),defintbody(([b]~)*(sec(([b]~)*x))^2,x))", "type==1&&z==4", "defint(defintlimits($p$,$q$),defintbody(([-1*a*b]~)*cosec(([b]~)*x)*cot(([b]~)*x),x))=([a]~)*defint(defintlimits($p$,$q$),defintbody(([-b]~)*cosec(([b]~)*x)*cot(([b]~)*x),x))", "type==1&&z==5", "defint(defintlimits($p$,$q$),defintbody(([a*b]~)*sec(([b]~)*x)*tan(([b]~)*x),x))=([a]~)*defint(defintlimits($p$,$q$),defintbody(([b]~)*sec(([b]~)*x)*tan(([b]~)*x),x))", "type==1&&z==6", "defint(defintlimits($p$,$q$),defintbody(([-1*a*b]~)*(cosec(([b]~)*x))^2,x))=([a]~)*defint(defintlimits($p$,$q$),defintbody(([-b]~)*(cosec(([b]~)*x))^2,x))", "z==1", "int(([a*b]~)*cos(([b]~)*x),x)=([a]~)*int(([b]~)*cos(([b]~)*x),x)", "z==2", "int(([-1*a*b]~)*sin(([b]~)*x),x)=([a]~)*int(([-b]~)*sin(([b]~)*x),x)", "z==3", "int(([a*b]~)*(sec(([b]~)*x))^2,x)=([a]~)*int(([b]~)*(sec(([b]~)*x))^2,x)", "z==4", "int(([-1*a*b]~)*cosec(([b]~)*x)*cot(([b]~)*x),x)=([a]~)*int(([-b]~)*cosec(([b]~)*x)*cot(([b]~)*x),x)", "z==5", "int(([a*b]~)*sec(([b]~)*x)*tan(([b]~)*x),x)=([a]~)*int(([b]~)*sec(([b]~)*x)*tan(([b]~)*x),x)", "z==6", "int(([-1*a*b]~)*(cosec(([b]~)*x))^2,x)=([a]~)*int(([-b]~)*(cosec(([b]~)*x))^2,x)" }, { "type==1&&z==1", "defint(defintlimits($p$,$q$),defintbody(([a*b]~)*cos(([b]~)*x),x))=defintbrackets(([a]~)*sin(([b]~)*x),defintbracketlimits($p$,$q$))", "type==1&&z==2", "defint(defintlimits($p$,$q$),defintbody(([-1*a*b]~)*sin(([b]~)*x),x))=defintbrackets(([a]~)*cos(([b]~)*x),defintbracketlimits($p$,$q$))", "type==1&&z==3", "defint(defintlimits($p$,$q$),defintbody(([a*b]~)*(sec(([b]~)*x))^2,x))=defintbrackets(([a]~)*tan(([b]~)*x),defintbracketlimits($p$,$q$))", "type==1&&z==4", "defint(defintlimits($p$,$q$),defintbody(([-1*a*b]~)*cosec(([b]~)*x)*cot(([b]~)*x),x))=defintbrackets(([a]~)*cosec(([b]~)*x),defintbracketlimits($p$,$q$))", "type==1&&z==5", "defint(defintlimits($p$,$q$),defintbody(([a*b]~)*sec(([b]~)*x)*tan(([b]~)*x),x))=defintbrackets(([a]~)*sec(([b]~)*x),defintbracketlimits($p$,$q$))", "type==1&&z==6", "defint(defintlimits($p$,$q$),defintbody(([-1*a*b]~)*(cosec(([b]~)*x))^2,x))=defintbrackets(([a]~)*cot(([b]~)*x),defintbracketlimits($p$,$q$))", "z==1", "int(([a*b]~)*cos(([b]~)*x),x)=([a]~)*sin(([b]~)*x)+C", "z==2", "int(([-1*a*b]~)*sin(([b]~)*x),x)=([a]~)*cos(([b]~)*x)+C", "z==3", "int(([a*b]~)*(sec(([b]~)*x))^2,x)=([a]~)*tan(([b]~)*x)+C", "z==4", "int(([-1*a*b]~)*cosec(([b]~)*x)*cot(([b]~)*x),x)=([a]~)*cosec(([b]~)*x)+C", "z==5", "int(([a*b]~)*sec(([b]~)*x)*tan(([b]~)*x),x)=([a]~)*sec(([b]~)*x)+C", "z==6", "int(([-1*a*b]~)*(cosec(([b]~)*x))^2,x)=([a]~)*cot(([b]~)*x)+C" }, { "type=0", "", "type==1", "$preanswer$=$answer$" } };

    private static String[] helpStrings = { "Think about the how one would differentiate a expression like", "" };

    private static String[][] helpExpressions = { { "z==1", "a*sin(b*x)", "z==2", "a*cos(b*x)", "z==3", "a*tan(b*x)", "z==4", "a*cosec(b*x)", "z==5", "a*sec(b*x)", "z==6", "a*cot(b*x)" } };

    private static FreeformBranchingQuestionGenerator freeformBranchingQuestionGenerator;

    private static ParameterSet parameterSet;

    /**
     * this method is called by the <code>ExerciseLoader</code> class.
     */
    public QuestionInterface getExercise() {
        parameterSet = new ParameterSet(parameterNames);
        parameterSet.createParameter("a", aValues);
        parameterSet.createParameter("b", bValues);
        parameterSet.createParameter("z", zValues);
        parameterSet.createParameter("type", typeValues);
        parameterSet.createParameter("p", pValues);
        parameterSet.createParameter("q", qValues);
        parameterSet.createParameter("preanswer", preanswerValues);
        parameterSet.createParameter("answer", answerValues);
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
