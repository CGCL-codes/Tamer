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
public class ExAlgLongDivision implements ExerciseInterface {

    public static final String PLATFORM = "TYPE1";

    public static final String TITLE = "Algebraic long division";

    public static final String DESCRIPTION = "Simplify various expressions by the use of algebraic long division.";

    public static final ExerciseDevelopmentStatus STATUS = ExerciseDevelopmentStatus.UNDER_DEVELOPMENT;

    private static String[][][] analysisExpressions = { {} };

    private static String[][] analysisStrings = { { "Your answer is mathematically correct, but the machine doesn't recognise it as being in the right form. Have you simplified it fully?" } };

    private static double[] aValues = { 1.0, 2.0, 3.0, 4.0 };

    private static double[] bValues = { -4.0, -3.0, -2.0, -1.0, 1.0, 2.0, 3.0, 4.0 };

    private static double[] cdeValues = { -4.0, -3.0, -2.0, -1.0, 0.0, 1.0, 2.0, 3.0, 4.0 };

    private static String correctAnswerExpression = "([a/gcd(a,p)]~)*x^3/([p/gcd(a,p)]~)+ ([(b*p-a*q)/gcd(b*p-a*q,p*p)]~)*x^2/([p*p/gcd(b*p-a*q,p*p)]~)+([(c*p*p-b*q*p+a*q*q)/gcd(c*p*p-b*q*p+a*q*q,p*p*p)]~)*x/([p*p*p/gcd(c*p*p-b*q*p+a*q*q,p*p*p)]~)+([(d*p*p*p-c*q*p*p+b*q*q*p-a*q*q*q)/(p*p*p*p)]~)+ ([(e*p*p*p*p-d*q*p*p*p+c*q*q*p*p-b*q*q*q*p+a*q*q*q*q)/gcd(e*p*p*p*p-d*q*p*p*p+c*q*q*p*p-b*q*q*q*p+a*q*q*q*q,p*p*p*p)]~)/( ([p*p*p*p/gcd(e*p*p*p*p-d*q*p*p*p+c*q*q*p*p-b*q*q*q*p+a*q*q*q*q,p*p*p*p)]~) * (([p]~)*x+([q]~)) )";

    private static String correctAnswerPatterns = "([a/gcd(a,p)]~)*x^3/([p/gcd(a,p)]~)+ ([(b*p-a*q)/gcd(b*p-a*q,p*p)]~)*x^2/([p*p/gcd(b*p-a*q,p*p)]~)+([(c*p*p-b*q*p+a*q*q)/gcd(c*p*p-b*q*p+a*q*q,p*p*p)]~)*x/([p*p*p/gcd(c*p*p-b*q*p+a*q*q,p*p*p)]~)+([(d*p*p*p-c*q*p*p+b*q*q*p-a*q*q*q)/(p*p*p*p)]~)+ ([(e*p*p*p*p-d*q*p*p*p+c*q*q*p*p-b*q*q*q*p+a*q*q*q*q)/gcd(e*p*p*p*p-d*q*p*p*p+c*q*q*p*p-b*q*q*q*p+a*q*q*q*q,p*p*p*p)]~)/( ([p*p*p*p/gcd(e*p*p*p*p-d*q*p*p*p+c*q*q*p*p-b*q*q*q*p+a*q*q*q*q,p*p*p*p)]~) * (([p]~)*x+([q]~)) )";

    private static String[] distractorExpressions = { "(([a]~)*x^4+([b]~)*x^3+([c]~)*x^2+([d]~)*x+([e]~))/(([p]~)*x+([q]~))" };

    private static FreeformBranchingQuestionGenerator freeformBranchingQuestionGenerator;

    private static String[][] helpExpressions = {};

    private static String[] helpStrings = { "Treat the problem like an ordinary long division calculation." };

    private static String[] parameterNames = { "a", "b", "c", "d", "e", "p", "q" };

    private static ParameterSet parameterSet;

    private static double[] pValues = { 1.0 };

    private static String[] questionExpressions = { "(([a]~)*x^4+([b]~)*x^3+([c]~)*x^2+([d]~)*x+([e]~))/(([p]~)*x+([q]~))", "p(x)+q/(([p]~)*x+([q]~))" };

    private static String[] questionStrings = { "Using algebraic division, express", "in the form", "where <?metric-ipmml p(x) ?> is a polynomial." };

    private static double[] qValues = { -4.0, -3.0, -2.0, -1.0, 1.0, 2.0, 3.0, 4.0 };

    private static double tolerance = 0.00001;

    private static double[][] variableDomains = { { -1.0, 2.0 } };

    private static String[] variables = { "x" };

    private static String[] workingExpressions = { "([a/gcd(a,p)]~)*x^3/([p/gcd(a,p)]~)+ ([(b*p-a*q)/gcd(b*p-a*q,p*p)]~)*x^2/([p*p/gcd(b*p-a*q,p*p)]~)+([(c*p*p-b*q*p+a*q*q)/gcd(c*p*p-b*q*p+a*q*q,p*p*p)]~)*x/([p*p*p/gcd(c*p*p-b*q*p+a*q*q,p*p*p)]~)+([(d*p*p*p-c*q*p*p+b*q*q*p-a*q*q*q)/(p*p*p*p)]~)" };

    private static String[] workingStrings = { "<table><tr><td>              </td><td> </td><td>       </td><td> </td> <td>[a/p] <i>x</i><sup>3</sup><td>+</td><td>[((b*p-a*q)/(p*p)] <i>x</i><sup>2</sup><td>+</td><td>[(c*p*p-b*q*p+a*q*q)/(p*p*p)] <i>x</i><td>+</td><td>[(d*p*p*p-c*q*p*p+b*q*q*p-a*q*q*q)/(p*p*p*p)]</td></tr> <tr> <td>              </td><td> </td><td>       </td><td> </td><td colspan='9'><hr></td>     </tr>       <tr>       <td>[p] <i>x</i></td><td>+</td><td>[q]</td><td>)</td> <td>[a] <i>x</i><sup>4</sup></td> <td>+</td><td>[b] <i>x</i><sup>3</sup></td> <td>+</td><td>[c] <i>x</i><sup>2</sup></td> <td>+</td><td>[d] <i>x</i></td> <td>+</td><td>[e]</td>       </tr> <tr><td>              </td><td> </td><td>       </td><td></td><td>[a] <i>x</i><sup>4</sup><td>+</td>[a*q/p] <i>x</i><sup>3</sup></tr> <tr><td>              </td><td> </td><td>       </td><td> </td> <td colspan = '3'><hr></td>  </tr> <tr><td>              </td><td> </td><td>       </td><td></td><td></td><td></td><td>[(b*p-a*q)/p] <i>x</i><sup>3</sup><td>+</td>[c] <i>x</i><sup>2</sup></td></tr>  <tr><td>              </td><td> </td><td>       </td><td></td><td></td><td></td><td>[(b*p-a*q)/p] <i>x</i><sup>3</sup><td>+</td>[(b*p*q-a*q*q)/(p*p)] <i>x</i><sup>2</sup></td></tr> <tr><td>              </td><td> </td><td>       </td><td></td><td></td><td></td><td colspan = '3'><hr></td></tr> <tr><td>              </td><td> </td><td>       </td><td></td><td></td><td></td><td></td><td></td><td>[(c*p*p-b*p*q+a*q*q)/(p*p)] <i>x</i><sup>2</sup><td>+</td>[d] <i>x</i></td></tr> <tr><td>              </td><td> </td><td>       </td><td></td><td></td><td></td><td></td><td></td><td>[(c*p*p-b*p*q+a*q*q)/(p*p)] <i>x</i><sup>2</sup><td>+</td>[(c*p*p*q-b*p*q*q+a*q*q*q)/(p*p*p)] <i>x</i></td></tr>  <tr><td>              </td><td> </td><td>       </td><td></td><td></td><td></td><td></td><td></td><td colspan = '3'><hr></td>  <tr><td>              </td><td> </td><td>       </td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td>[(d*p*p*p-c*p*p*q+b*p*q*q-a*q*q*q)/(p*p*p)] <i>x</i><td>+</td>[e]</td></tr>  <tr><td>              </td><td> </td><td>       </td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td>[(d*p*p*p-c*p*p*q+b*p*q*q-a*q*q*q)/(p*p*p)] <i>x</i><td>+</td>[(d*p*p*p*q-c*p*p*q*q+b*p*q*q*q-a*q*q*q*q)/(p*p*p*p)]</td></tr>  <tr><td>              </td><td> </td><td>       </td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td colspan = '3'><hr></tr> <tr><td>              </td><td> </td><td>       </td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td>[(e*p*p*p*p-d*p*p*p*q+c*p*p*q*q-b*p*q*q*q+a*q*q*q*q)/(p*p*p*p)]</td></tr>              </table><br>The quotient is", "and the remainder is [(e*p*p*p*p-d*p*p*p*q+c*p*p*q*q-b*p*q*q*q+a*q*q*q*q)/(p*p*p*p)]." };

    public ExAlgLongDivision() {
    }

    public QuestionInterface getExercise() {
        parameterSet = new ParameterSet(parameterNames);
        parameterSet.createParameter("a", aValues);
        parameterSet.createParameter("b", bValues);
        parameterSet.createParameter("c", cdeValues);
        parameterSet.createParameter("d", cdeValues);
        parameterSet.createParameter("e", cdeValues);
        parameterSet.createParameter("p", pValues);
        parameterSet.createParameter("q", qValues);
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
        freeformBranchingQuestionGenerator.setNotes("html/algebra/alglongdivision.html");
        freeformBranchingQuestionGenerator.setInstructions("html/help/exercise/type1.html");
        return freeformBranchingQuestionGenerator;
    }

    public short getExerciseType() {
        return ExerciseInterface.FREE_FORM_TYPE_1;
    }
}
