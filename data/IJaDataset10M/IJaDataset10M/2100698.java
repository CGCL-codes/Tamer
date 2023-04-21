package uk.ac.imperial.ma.metric.exercises.examples;

import uk.ac.imperial.ma.metric.exerciseEngine.classic.ExerciseInterface;
import uk.ac.imperial.ma.metric.exerciseEngine.classic.FreeformBranchingQuestionGenerator;
import uk.ac.imperial.ma.metric.exerciseEngine.classic.QuestionInterface;
import uk.ac.imperial.ma.metric.computerese.classic.ParameterSet;

/**
 * Tutorial Exercise 7 version 1.1.
 *
 * @author Phil Ramsden
 * @version 1.1
 */
public class TutorialExercise7v11 implements ExerciseInterface {

    public TutorialExercise7v11() {
    }

    public short getExerciseType() {
        return ExerciseInterface.FREE_FORM_TYPE_1;
    }

    private static String[] variables = { "x" };

    private static double[][] variableDomains = { { -1.0, 2.0 } };

    private static double tolerance = 0.00001;

    private static String[] parameterNames = { "a", "p", "q", "r", "s" };

    private static double[] qsValues = { -7.0, -5.0, -1.0, -1.0, 7.0, 5.0, 1.0, 1.0 };

    private static String[] questionStrings = { "Factorise the quadratic expression ", "" };

    private static String[] questionExpressions = { "([a*p*r]~)*x^2+([a*(p*s+q*r)]~)*x+([a*q*s]~)" };

    private static String[] correctAnswerExpression = { "p==r&&q==s", "([a]~)*(([p]~)*x+([q]~))^2", "true", "([a]~)*(([p]~)*x+([q]~))*(([r]~)*x+([s]~))" };

    private static String[][] correctAnswerPatterns = { { "p==r&&q==s" }, { "([a]~)*(([p]~)*x+([q]~))^2", "([a]~)*(([p]~)*x+([q]~))*(([r]~)*x+([s]~))" }, { "true" }, { "([a]~)*(([p]~)*x+([q]~))*(([r]~)*x+([s]~))", "([a]~)*(([r]~)*x+([s]~))*(([p]~)*x+([q]~))" } };

    private static String[] distractorExpressions = { "[a]*([p]*x+([q]))*([r]*x+([s]))", "wildcard", "wildcard" };

    private static String[] distractorPatterns = { "?", "?1*(?2*x+?3)*(?4*x+?5) @ ?1==[a]&&?2*?4==[p*r]&&?3*?5==[q*s] #" + "?1*(?2*x-?3)*(?4*x+?5) @ ?1==[a]&&?2*?4==[p*r]&&?3*?5==[-q*s] #" + "?1*(?2*x+?3)*(?4*x-?5) @ ?1==[a]&&?2*?4==[p*r]&&?3*?5==[-q*s] #" + "?1*(?2*x-?3)*(?4*x-?5) @ ?1==[a]&&?2*?4==[p*r]&&?3*?5==[q*s] #" + "?1*(?2*x+?3)^2 @ ?1==[a]&&?2*?2==[p*r]&&?3*?3==[q*s] #" + "?1*(?2*x-?3)^2 @ ?1==[a]&&?2*?2==[p*r]&&?3*?3==[q*s]", "?1*(?2*x+?3)*(?4*x+?5) @ ?1==[a]&&?2*?4==[p*r]&&?3*?5==[-q*s]#" + "?1*(?2*x-?3)*(?4*x+?5) @ ?1==[a]&&?2*?4==[p*r]&&?3*?5==[q*s]#" + "?1*(?2*x+?3)*(?4*x-?5) @ ?1==[a]&&?2*?4==[p*r]&&?3*?5==[q*s]#" + "?1*(?2*x-?3)*(?4*x-?5) @ ?1==[a]&&?2*?4==[p*r]&&?3*?5==[-q*s]#" + "?1*(?2*x+?3)^2 @ ?1==[a]&&?2*?2==[p*r]&&?3*?3==[-q*s]#" + "?1*(?2*x-?3)^2 @ ?1==[a]&&?2*?2==[p*r]&&?3*?3==[-q*s]" };

    private static String[][] analysisStrings = { { "Your answer is mathematically equivalent to the correct one, but isn't in the right form. " + "This could be because you haven't factorised it enough--have you looked for any numerical " + "common factor, for example? Alternatively, you might find it can be expressed more simply." }, { "Your factorisation isn't quite right. If we expand your expression, we find that " + "the constant term and the coefficient of <i>x</i><sup>2</sup> come out correct, but that " + "we get the wrong coefficient of <i>x</i>." }, { "Your factorisation isn't quite right. If we expand your expression, we find that " + "the coefficient of <i>x</i><sup>2</sup> comes out correct, but that " + "there's a <i>sign error</i> in the constant term." } };

    private static String[][][] analysisExpressions = { {}, {}, {} };

    private static String[][] workingStrings = { { "a==1", "We begin by looking for any <i>numerical</i> common factors. In this case, there " + "are none other than ", "true", "We begin by looking for any <i>numerical</i> common factors. In this case, there " + "is indeed such a factor, namely " }, { "true", "If we now examine the expression" }, { "true", "our aim is to express it in the form" }, { "true", "It's clear that we need to pick a pair of <i>x</i>-coefficents, <i>p</i> and <i>r</i>, " + "such that" }, { "true", "and a pair of constant terms, <i>q</i> and <i>s</i>, such that" }, { "true", "Well, there are only so many ways of doing that, and only one of them, when we expand " + "the brackets, gives us the right <i>x</i>-coefficient, namely" }, { "true", "This turns out to be " }, { "true", "" }, { "true", "" }, { "true", "" }, { "true", "giving us the complete factorisation" }, { "true", "" } };

    private static String[] workingExpressions = { "[a]", "([a]~)*(([p*r]~)*x^2+([p*s+q*r]~)*x+([q*s]~))", "([a]~)*(p*x+q)*(r*x+s)", "p*r=[p*r]", "q*s=[q*s]", "[p*s+q*r]", "p=[p]", "r=[r]", "q=[q]", "s=[s]", "([a]~)*(([p]~)*x+([q]~))*(([r]~)*x+([s]~))" };

    private static String[] helpStrings = { "We begin by looking for any <i>numerical</i> common factors. In this case, the " + "only such factor is ", "If we now examine the expression", "our aim is to express it in the form", "It's clear that we need to pick a pair of <i>x</i>-coefficents, <i>p</i> and <i>r</i>, " + "such that", "and a pair of constant terms, <i>q</i> and <i>s</i>, such that", "For example, one choice for the <i>x</i>-coefficients would be", "and", "There may be others. Simply work through the choices until you find one that, when " + "you expand the brackets, gives the right <i>x</i>-coefficient, namely", "" };

    private static String[] helpExpressions = { "[a]", "([a]~)*(([p*r]~)*x^2+([p*s+q*r]~)*x+([q*s]~))", "([a]~)*(p*x+q)*(r*x+s)", "p*r=[p*r]", "q*s=[q*s]", "p=[p*r]", "r=1", "[p*s+q*r]" };

    private static FreeformBranchingQuestionGenerator freeformBranchingQuestionGenerator;

    private static ParameterSet parameterSet;

    public QuestionInterface getExercise() {
        parameterSet = new ParameterSet(parameterNames);
        parameterSet.createParameter("a", 1, 3);
        parameterSet.createParameter("p", 1, 3);
        parameterSet.createParameter("r", 1, 3);
        parameterSet.createParameter("q", qsValues);
        parameterSet.createParameter("s", qsValues);
        freeformBranchingQuestionGenerator = new FreeformBranchingQuestionGenerator();
        freeformBranchingQuestionGenerator.setParameterSet(parameterSet);
        freeformBranchingQuestionGenerator.setQuestions(questionStrings, questionExpressions);
        freeformBranchingQuestionGenerator.setCorrectAnswer(correctAnswerExpression, correctAnswerPatterns);
        freeformBranchingQuestionGenerator.setDistractors(distractorExpressions, distractorPatterns);
        freeformBranchingQuestionGenerator.setVariables(variables);
        freeformBranchingQuestionGenerator.setVariableDomains(variableDomains);
        freeformBranchingQuestionGenerator.setTolerance(tolerance);
        freeformBranchingQuestionGenerator.setAnalysis(analysisStrings, analysisExpressions);
        freeformBranchingQuestionGenerator.setWorking(workingStrings, workingExpressions);
        freeformBranchingQuestionGenerator.setHelp(helpStrings, helpExpressions);
        freeformBranchingQuestionGenerator.setNotes("html/algebra/quadratics/factorisation.html");
        freeformBranchingQuestionGenerator.setInstructions("html/help/exercise/type1.html");
        return freeformBranchingQuestionGenerator;
    }
}
