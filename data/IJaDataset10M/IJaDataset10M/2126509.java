package uk.ac.imperial.ma.metric.exercises.coordGeom;

import uk.ac.imperial.ma.metric.exerciseEngine.classic.ExerciseInterface;
import uk.ac.imperial.ma.metric.exerciseEngine.classic.FreeformBranchingQuestionGenerator;
import uk.ac.imperial.ma.metric.exerciseEngine.classic.QuestionInterface;
import uk.ac.imperial.ma.metric.computerese.classic.ParameterSet;

/**
 * Basic ODEs 1 exercise data file.
 *
 * @author Phil Ramsden
 * @version 0.1
 */
public class ExCGLineTwoPts implements ExerciseInterface {

    public ExCGLineTwoPts() {
    }

    public short getExerciseType() {
        return ExerciseInterface.FREE_FORM_TYPE_1;
    }

    /** This field is read by reflection by the <code>ExerciseLoader</code> to get a title for the tab containing the exercise's GUI */
    public static final String TITLE = "Coordinate Geometry 1";

    /** This field is read by reflection by the <code>ExerciseLoader</code> to get a tool tip for the tab containing the exercise's GUI */
    public static final String TOOL_TIP = "Simple Coordinate Geometry Exercise.";

    /** This field is read by reflection by the <code>ExerciseLoader</code> to get know what kind of exercise GUI the data should be used with */
    public static final String PLATFORM = "TYPE1";

    private static String[] variables = { "y", "x" };

    private static double[][] variableDomains = { { -2.0, 2.0 }, { -2.0, 2.0 } };

    private static double tolerance = 0.00001;

    private static String[] parameterNames = { "x0", "y0", "dx", "dy" };

    private static double[] xy0Values = { -10.0, -9.0, -8.0, -7.0, -6.0, -5.0, -4.0, -3.0, -2.0, -1.0, 1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0, 10.0 };

    private static double[] dxValues = { 1.0, 2.0, 3.0, 4.0, 5.0 };

    private static double[] dyValues = { -5.0, -4.0, -3.0, -2.0, -1.0, 1.0, 2.0, 3.0, 4.0, 5.0 };

    private static String[] questionStrings = { "Find the equation of the line that passes through the points ([x0], [y0]) and ([x0+dx], [y0+dy])." };

    private static String[][] questionExpressions = {};

    private static String correctAnswerExpression = "y=([dy/dx]~)*x+([y0-dy/dx*x0]~) #" + "([dy/dx]~)*x+([y0-dy/dx*x0]~) #" + "y+([-dy/dx]~)*x=([y0-dy/dx*x0]~) #" + "y+([-(y0-dy/dx*x0)]~)=([dy/dx]~)*x #" + "y+([-(y0-dy/dx*x0)]~)+([-dy/dx]~)*x=0 #" + "([dx/gcd(dy,dx)]~)*y=([dy/gcd(dy,dx)]~)*x+([dx/gcd(dy,dx)*y0-dy/gcd(dy,dx)*x0]~) #" + "([dx/gcd(dy,dx)]~)*y+([-dy/gcd(dy,dx)]~)*x=([dx/gcd(dy,dx)*y0-dy/gcd(dy,dx)*x0]~) #" + "([dx/gcd(dy,dx)]~)*y+([-dx/gcd(dy,dx)*y0+dy/gcd(dy,dx)*x0]~)=([dy/gcd(dy,dx)]~)*x #" + "([dx/gcd(dy,dx)]~)*y+([-dy/gcd(dy,dx)]~)*x+([-dx/gcd(dy,dx)*y0+dy/gcd(dy,dx)*x0]~)=0 #" + "([-dx/gcd(dy,dx)]~)*y=([-dy/gcd(dy,dx)]~)*x+([-dx/gcd(dy,dx)*y0+dy/gcd(dy,dx)*x0]~) #" + "([-dx/gcd(dy,dx)]~)*y+([dy/gcd(dy,dx)]~)*x=([-dx/gcd(dy,dx)*y0+dy/gcd(dy,dx)*x0]~) #" + "([-dx/gcd(dy,dx)]~)*y+([dx/gcd(dy,dx)*y0-dy/gcd(dy,dx)*x0]~)=([-dy/gcd(dy,dx)]~)*x #" + "([-dx/gcd(dy,dx)]~)*y+([dy/gcd(dy,dx)]~)*x+([dx/gcd(dy,dx)*y0-dy/gcd(dy,dx)*x0]~)=0";

    private static String[] distractorExpressions = { "y=([-dy/dx]~)*x+([y0+dy/dx*x0]~) #" + "y+([dy/dx]~)*x=([y0+dy/dx*x0]~) #" + "y+([-(y0+dy/dx*x0)]~)=([-dy/dx]~)*x #" + "y+([-(y0+dy/dx*x0)]~)+([dy/dx]~)*x=0 #" + "([-dy/dx]~)*x+([y0+dy/dx*x0]~) #" + "([dx/gcd(dy,dx)]~)*y=([-dy/gcd(dy,dx)]~)*x+([dx/gcd(dy,dx)*y0+dy/gcd(dy,dx)*x0]~) #" + "([dx/gcd(dy,dx)]~)*y+([dy/gcd(dy,dx)]~)*x=([dx/gcd(dy,dx)*y0+dy/gcd(dy,dx)*x0]~) #" + "([dx/gcd(dy,dx)]~)*y+([-dx/gcd(dy,dx)*y0-dy/gcd(dy,dx)*x0]~)=([-dy/gcd(dy,dx)]~)*x #" + "([dx/gcd(dy,dx)]~)*y+([dy/gcd(dy,dx)]~)*x+([-dx/gcd(dy,dx)*y0-dy/gcd(dy,dx)*x0]~)=0 #" + "([-dx/gcd(dy,dx)]~)*y=([dy/gcd(dy,dx)]~)*x+([-dx/gcd(dy,dx)*y0-dy/gcd(dy,dx)*x0]~) #" + "([-dx/gcd(dy,dx)]~)*y+([-dy/gcd(dy,dx)]~)*x=([-dx/gcd(dy,dx)*y0-dy/gcd(dy,dx)*x0]~) #" + "([-dx/gcd(dy,dx)]~)*y+([dx/gcd(dy,dx)*y0+dy/gcd(dy,dx)*x0]~)=([dy/gcd(dy,dx)]~)*x #" + "([-dx/gcd(dy,dx)]~)*y+([-dy/gcd(dy,dx)]~)*x+([dx/gcd(dy,dx)*y0+dy/gcd(dy,dx)*x0]~)=0 #" + "y=([-dy/dx]~)*x+([(y0+dy)+dy/dx*(x0+dx)]~) #" + "([-dy/dx]~)*x+([(y0+dy)+dy/dx*(x0+dx)]~) #" + "y+([dy/dx]~)*x=([(y0+dy)+dy/dx*(x0+dx)]~) #" + "y+([-((y0+dy)+dy/dx*(x0+dx))]~)=([-dy/dx]~)*x #" + "y+([-((y0+dy)+dy/dx*(x0+dx))]~)+([dy/dx]~)*x=0 #" + "([dx/gcd(dy,dx)]~)*y=([-dy/gcd(dy,dx)]~)*x+([dx/gcd(dy,dx)*(y0+dy)+dy/gcd(dy,dx)*(x0+dx)]~) #" + "([dx/gcd(dy,dx)]~)*y+([dy/gcd(dy,dx)]~)*x=([dx/gcd(dy,dx)*(y0+dy)+dy/gcd(dy,dx)*(x0+dx)]~) #" + "([dx/gcd(dy,dx)]~)*y+([-dx/gcd(dy,dx)*(y0+dy)-dy/gcd(dy,dx)*(x0+dx)]~)=([-dy/gcd(dy,dx)]~)*x #" + "([dx/gcd(dy,dx)]~)*y+([dy/gcd(dy,dx)]~)*x+([-dx/gcd(dy,dx)*(y0+dy)-dy/gcd(dy,dx)*(x0+dx)]~)=0 #" + "([-dx/gcd(dy,dx)]~)*y=([dy/gcd(dy,dx)]~)*x+([-dx/gcd(dy,dx)*(y0+dy)-dy/gcd(dy,dx)*(x0+dx)]~) #" + "([-dx/gcd(dy,dx)]~)*y+([-dy/gcd(dy,dx)]~)*x=([-dx/gcd(dy,dx)*(y0+dy)-dy/gcd(dy,dx)*(x0+dx)]~) #" + "([-dx/gcd(dy,dx)]~)*y+([dx/gcd(dy,dx)*(y0+dy)+dy/gcd(dy,dx)*(x0+dx)]~)=([dy/gcd(dy,dx)]~)*x #" + "([-dx/gcd(dy,dx)]~)*y+([-dy/gcd(dy,dx)]~)*x+([dx/gcd(dy,dx)*(y0+dy)+dy/gcd(dy,dx)*(x0+dx)]~)=0" };

    private static String[][] analysisStrings = { { "There seems to be a <i>sign error</i> in your calculation of the gradient: note that the gradient is [dy/dx] rather than [-dy/dx]." } };

    private static String[][][] analysisExpressions = { {} };

    private static String[] workingStrings = { "The gradient of the line is given by", "which in this case is", "We can now apply the formula", "giving", "which rearranges to", "" };

    private static String[] workingExpressions = { "m=(y1-y0)/(x1-x0)", "m=(([y0+dy])-([y0]))/(([x0+dx])-([x0]))=[dy/dx]", "y-y0 = m*(x-x0)", "y-([y0]~)=([dy/dx])**(x-([x0]~))", "y=([dy/dx]~)*x+([y0-dy/dx*x0]~)" };

    private static String[] helpStrings = { "Begin by calculating the <i>gradient</i> of the line <i>m</i>, then recall that the equation is given by", "" };

    private static String[] helpExpressions = { "y-y0 = m*(x-x0)" };

    private static FreeformBranchingQuestionGenerator freeformBranchingQuestionGenerator;

    private static ParameterSet parameterSet;

    /**
     * this method is called by the <code>ExerciseLoader</code> class.
     */
    public QuestionInterface getExercise() {
        parameterSet = new ParameterSet(parameterNames);
        parameterSet.createParameter("x0", xy0Values);
        parameterSet.createParameter("y0", xy0Values);
        parameterSet.createParameter("dx", dxValues);
        parameterSet.createParameter("dy", dyValues);
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
        freeformBranchingQuestionGenerator.setnQuestions(5);
        freeformBranchingQuestionGenerator.setNotes("html/geometry/AlevelCoordGeom/equation of a straight line.htm");
        freeformBranchingQuestionGenerator.setInstructions("html/help/exercise/type1.html");
        return freeformBranchingQuestionGenerator;
    }
}
