package uk.ac.imperial.ma.metric.exercises.kinematics;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.*;
import java.util.StringTokenizer;
import javax.imageio.ImageIO;
import uk.ac.imperial.ma.metric.exercises.AnimatedGraphicalExerciseInterface;
import uk.ac.imperial.ma.metric.exercises.FreeformQuestionInterface;
import uk.ac.imperial.ma.metric.exercises.QuestionInterface;
import uk.ac.imperial.ma.metric.exercises.FreeformBranchingQuestionGenerator;
import uk.ac.imperial.ma.metric.parsing.ParameterSet;
import uk.ac.imperial.ma.metric.plotting.AxesPlotter;
import uk.ac.imperial.ma.metric.plotting.GridPlotter;
import uk.ac.imperial.ma.metric.plotting.CurvePlotter;
import uk.ac.imperial.ma.metric.plotting.ParametricCoordGenerator;
import uk.ac.imperial.ma.metric.plotting.MathPainter;
import uk.ac.imperial.ma.metric.plotting.MathPainterPanel;
import uk.ac.imperial.ma.metric.plotting.CopyPanel;

/**
 * Series 1 exercise data file.
 *
 * @author Phil Ramsden
 * @version 0.1
 */
public class AnimGrExKinematicsProjectilesFlat3 implements AnimatedGraphicalExerciseInterface, Runnable {

    public AnimGrExKinematicsProjectilesFlat3() {
    }

    public short getExerciseType() {
        return AnimatedGraphicalExerciseInterface.ANIMATED_GRAPHICAL_EXERCISE_TYPE_1;
    }

    MathPainterPanel thismpp;

    CopyPanel thiscp;

    Thread runner = null;

    boolean stop;

    short animationType = FreeformQuestionInterface.ANSWER;

    double userU = 0.0;

    double userV = 0.0;

    private static String[] variables = { "a" };

    private static String[] vectorVariables = {};

    private static double[][] variableDomains = { { -1.0, 1.0 } };

    private static double tolerance = 0.005;

    private static String[][] parameterNames = { { "type" }, { "v" }, { "theta", "costwotheta", "sintwotheta" }, { "theta1", "sintheta1" } };

    private static double[] typeValues = { 0.0, 1.0, 2.0, 3.0, 4.0 };

    private static double[] vValues = { 7.0, 14.0, 28.0, 35.0, 42.0 };

    private static double[] thetaValues = { 15.0, 30.0, 45.0, 60.0, 75.0 };

    private static String[] costwothetaValues = { "(sqrt(3)/2)", "(1/2)", "0", "(-1/2)", "(-sqrt(3)/2)" };

    private static String[] sintwothetaValues = { "(1/2)", "(sqrt(3)/2)", "1", "(sqrt(3)/2)", "(1/2)" };

    private static double[] theta1Values = { 30.0, 45.0, 60.0 };

    private static String[] sintheta1Values = { "(1/2)", "(sqrt(2)/2)", "(sqrt(3)/2)" };

    private static String[][] questionStrings = { { "type=0&&theta==45", "A particle is projected from a flat horizontal plane with speed " + "[v] metres per second, at an angle of θ degrees from the horizontal. " + "Given that the range of projection " + "(that is, the horizontal distance travelled during the " + "flight) is {decplaces(v*v*sin(2*theta*pi/180)/9.8,4)}" + " m, find θ. Take <i>g</i> = 9.8 m s<sup>-2</sup>.", "type=0", "A particle is projected from a flat horizontal plane with speed " + "[v] metres per second, at an angle of θ degrees from the horizontal. " + "Given that the range of projection " + "(that is, the horizontal distance travelled during the " + "flight) is {decplaces(v*v*sin(2*theta*pi/180)/9.8,4)}" + " m, find the two possible values of θ. Take <i>g</i> = 9.8 m s<sup>-2</sup>.", "type==1", "A particle is projected from a flat horizontal plane with speed " + "[v] metres per second, at an angle of θ degrees from the horizontal. " + "Given that the greatest height attained by the particle is " + "{decplaces(v*v*(1-cos(2*theta*pi/180))/(4*9.8),4)} m, find θ. " + "Take <i>g</i> = 9.8 m s<sup>-2</sup>.", "type==2", "A particle is projected from a flat horizontal plane with speed " + "[v] metres per second. " + "Given that the time of flight is {decplaces(2*v*(sin(theta*pi/180))/(9.8),4)} seconds, " + "find the range of projection in metres. Take <i>g</i> = 9.8 m s<sup>-2</sup>.", "type=3&&theta==45", "A particle is projected from a flat horizontal plane with speed " + "[v] metres per second. " + "Given that the range of projection " + "(that is, the horizontal distance travelled during the " + "flight) is {decplaces(v*v*sin(2*theta*pi/180)/9.8,4)}" + " m, find the greatest height attained. Take <i>g</i> = 9.8 m s<sup>-2</sup>.", "type=3", "A particle is projected from a flat horizontal plane with speed " + "[v] metres per second. " + "Given that the range of projection " + "(that is, the horizontal distance travelled during the " + "flight) is {decplaces(v*v*sin(2*theta*pi/180)/9.8,4)}" + " m, find the two possible values of the greatest height attained. Take <i>g</i> = 9.8 m s<sup>-2</sup>.", "type=4", "A particle is projected from a flat horizontal plane with speed " + "[v] metres per second. " + "Given that the greatest height attained is {decplaces(v*v*(1-cos(2*theta*pi/180))/(4*9.8),4)}" + " m, find the range of projection " + "(that is, the horizontal distance travelled during the " + "flight). Take <i>g</i> = 9.8 m s<sup>-2</sup>." } };

    private static String[] questionExpressions = {};

    private static String[] correctAnswerExpression = { "type==0&&theta==45", "[theta]", "type==0", "[min(theta,90-theta)],[max(theta,90-theta)]", "type==1", "[theta]", "type==2", "{decplaces(v*v*sin(2*theta*pi/180)/9.8,2)}", "type==3&&theta==45", "{decplaces(v*v*(1-cos(2*theta*pi/180))/(4*9.8),3)}", "type==3", "{decplaces(min(v*v*(1-cos(2*theta*pi/180))/(4*9.8),v*v*(1-cos(2*(90-theta)*pi/180))/(4*9.8)),3)}," + "{decplaces(max(v*v*(1-cos(2*theta*pi/180))/(4*9.8),v*v*(1-cos(2*(90-theta)*pi/180))/(4*9.8)),3)}", "type==4", "{decplaces(v*v*sin(2*theta*pi/180)/9.8,3)}" };

    private static String[][] distractorExpressions = {};

    private static String[][] distractorPatterns = {};

    private static String[][][] analysisStrings = {};

    private static String[][][] analysisExpressions = {};

    private static String[][] workingStrings = { { "type==0.0||type==2.0||type==3.0||type==4.0", "The time of flight, <i>T</i>, is the time at which " + "the height is zero. Using the standard formula", "type==1.0", "The greatest height, <i>h</i>, is the height at which " + "the vertical velocity is zero. Using the standard formula" }, { "true", "we obtain" }, { "true", "and hence" }, { "type==0.0||type==2.0||type==3.0||type==4.0", "We substitute this value of <i>T</i> into the expression", "true", "This equates to" }, { "type==0.0||type==2.0||type==3.0||type==4.0", "and obtain", "type==1.0", "whence" }, { "type==0.0||type==2.0", "hence", "type==1.0", "and thus", "type==3.0||type==4.0", "Now, the greatest height, <i>h</i>, is the height at which " + "the vertical velocity is zero. Using the standard formula", "true", "" }, { "type=0.0", "and thus", "type==2.0", "This simplifies to", "type==3.0||type==4.0", "we obtain", "true", "" }, { "type=0.0", "whence result.", "type=3.0||type==4.0", "and hence", "true", "" }, { "type==3.0||type==4.0", "This evaluates to", "true", "" }, { "type==4.0", "or simply", "true", "" }, { "type==4.0", "which is", "true", "" }, { "true", "" } };

    private static String[][] workingExpressions = { { "type==0.0||type==2.0||type==3.0||type==4.0", "s=u*t+(1/2)*a*t^2", "type==1.0", "v^2=u^2+2*a*s" }, { "type==0.0||type==2.0||type==3.0||type==4.0", "0=v*sin(theta)*T-(1/2)*g*T^2", "type==1.0", "0=v^2*sin(theta)^2-2*g*h" }, { "type==0.0||type==2.0||type==3.0||type==4.0", "T=2*v*sin(theta)/g", "type==1.0", "(sin(theta)^2)=(2*g*h)/v^2=(2**9.8**{decplaces(v*v*(1-cos(2*theta*pi/180))/(4*9.8),4)})/([v])^2" }, { "type==0.0||type==2.0||type==3.0||type==4.0", "s=u*t", "type==1.0", "sin(theta)^2 = {decplaces(sin(theta*pi/180)*sin(theta*pi/180),4)}" }, { "type==0.0||type==3.0", "r=(v*cos(theta))**((2*v*sin(theta))/g)=v^2*sin(2*theta)/g", "type==1.0", "sin(theta) = {decplaces(sin(theta*pi/180),4)}", "type==4.0", "r=(v*cos(theta))**((2*v*sin(theta))/g)=2*v^2*sin(theta)*cos(theta)/g", "true", "r=v*cos(theta)*T" }, { "type==0.0", "sin(2*theta)=(g*r)/v^2=(9.8**{decplaces(v*v*sin(2*theta*pi/180)/9.8,4)})/[v]^2={decplaces($sintwotheta$,4)}", "type==1.0", "theta=[theta]", "type==3.0||type==4.0", "v^2=u^2+2*a*s", "true", "r=v*sqrt(1-sin(theta)^2)*T=v*T*sqrt(1-((g*T)/(2*v))^2)" }, { "type==0.0&&theta==45", "2*theta=90", "type==0.0", "2*theta=[min(2*theta,180-2*theta)],[max(2*theta,180-2*theta)]", "type==3.0||type==4.0", "h=v^2*sin(theta)^2/(2*g)", "true", "r=(1/2)*T*sqrt(4v^2-g^2T^2)" }, { "type==2.0", "r=0.5**{decplaces(2*v*(sin(theta*pi/180))/(9.8),4)}*sqrt(4**[v]^2-9.8^2**({decplaces(2*v*(sin(theta*pi/180))/(9.8),4)})^2)=" + "{decplaces(v*v*sin(2*theta*pi/180)/9.8,2)}", "type==3.0", "h==v^2*plusminus(1,sqrt(1-sin(2*theta)^2))/(4*g)=" + "v^2*plusminus(1,sqrt(1-((g*r)/(v))^2))/(4*g)", "type==4.0", "r=2*v^2*sqrt(2*g*h/v^2)*sqrt(1-2*g*h/v^2)", "true", "" }, { "type==3.0&&theta==45", "h=([v]^2)*plusminus(1,sqrt({decplaces(cos(2*theta*pi/180)*cos(2*theta*pi/180),4)}))/(4**9.8)=" + "{decplaces(v*v*(1-cos(2*theta*pi/180))/(4*9.8),3)}", "type==3.0", "h=([v]^2)*plusminus(1,sqrt({decplaces(cos(2*theta*pi/180)*cos(2*theta*pi/180),4)}))/(4**9.8)=" + "{decplaces(min(v*v*(1-cos(2*theta*pi/180))/(4*9.8),v*v*(1-cos(2*(90-theta)*pi/180))/(4*9.8)),3)}," + "{decplaces(max(v*v*(1-cos(2*theta*pi/180))/(4*9.8),v*v*(1-cos(2*(90-theta)*pi/180))/(4*9.8)),3)}", "type==4.0", "r==(2/g)*sqrt(2*g*h*v^2-4*g^2*h^2)", "true", "" }, { "type==4.0", "r==sqrt(2**9.8**{decplaces(v*v*(1-cos(2*theta*pi/180))/(4*9.8),4)}**[v]^2-4**9.8^2**{decplaces(v*v*(1-cos(2*theta*pi/180))/(4*9.8),4)}^2)/4.9", "true", "" }, { "type==4.0", "r=={decplaces(v*v*sin(2*theta*pi/180)/9.8,3)}", "true", "" } };

    /**
     * =2*sqrt(2**9.8**{decplaces(v*v*(1-cos(2*theta*pi/180))/(4*9.8),4)}**[v]^2-4**9.8^2**{decplaces(v*v*(1-cos(2*theta*pi/180))/(4*9.8),4)}^2)/4.9=" +
        			"{decplaces(v*v*sin(2*theta*pi/180)/9.8,3)}
     */
    private static String[][] helpStrings = { { "type==0.0||type==2.0||type==3.0||type==4.0", "The time of flight, <i>T</i>, is the time at which " + "the height is zero. Using the standard formula", "type==1.0", "The greatest height, <i>h</i>, is the height at which " + "the vertical velocity is zero. Using the standard formula" }, { "true", "we obtain" }, { "true", "" } };

    private static String[][] helpExpressions = { { "type==0.0||type==2.0||type==3.0||type==4.0", "s=u*t+(1/2)*a*t^2", "type==1.0", "v^2=u^2+2*a*s", "type==3.0", "v=u+a*t" }, { "type==0.0||type==2.0", "0=v*sin(theta)*T-(1/2)*g*T^2", "type==1.0", "0=v^2*sin(theta)^2-2*g*h", "type==3.0", "0=v*sin(theta)-g*T" } };

    private static FreeformBranchingQuestionGenerator freeformBranchingQuestionGenerator;

    private static ParameterSet parameterSet;

    private static MathPainter mp;

    private static AxesPlotter ap;

    private static GridPlotter gp;

    /** The <code>ParametricCoordGenerator</code> used by this this graphical exercise. */
    private static ParametricCoordGenerator pcg;

    /** The <code>CurvePlotter</code> used by this this graphical exercise. */
    private static CurvePlotter curvePlotter;

    double type;

    double v;

    double vx1;

    double vy1;

    double vx2;

    double vy2;

    double theta;

    double tMax1;

    double tMax2;

    double h1;

    double h2;

    double r;

    public void drawBase(MathPainterPanel mpp) {
        mpp.setPreferredSize(new Dimension(100, 100));
        mpp.setBase();
    }

    public void drawQuestion(MathPainterPanel mpp, CopyPanel cp) {
        try {
            type = parameterSet.getValue("type");
            v = parameterSet.getValue("v");
            theta = parameterSet.getValue("theta");
            tMax1 = 2 * v * (Math.sin(theta * Math.PI / 180)) / (9.8);
            tMax2 = 2 * v * (Math.sin((90 - theta) * Math.PI / 180)) / (9.8);
            h1 = v * v * (1 - Math.cos(2 * theta * Math.PI / 180)) / (4 * 9.8);
            h2 = v * v * (1 - Math.cos((180 - 2 * theta) * Math.PI / 180)) / (4 * 9.8);
            r = v * v * Math.sin(2 * theta * Math.PI / 180) / 9.8;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        if (type == 0.0 || type == 3.0) {
            mp = mpp.init(-0.2 * r, -0.1 * r, 1.3 * r, 1.2 * r, true);
        } else if (type == 1.0 || type == 4.0) {
            mp = mpp.init(-0.2 * h1, -0.1 * h1, 1.3 * h1, 1.2 * h1, true);
        }
        mp.setScales();
        mp.setPaint(Color.WHITE);
        mp.setBackground();
        ap = new AxesPlotter(mp);
        gp = new GridPlotter(mp);
        if (type == 0.0 || type == 1.0 || type == 3.0 || type == 4.0) {
            mp.setPaint(Color.LIGHT_GRAY);
            gp.drawFineGrid();
            mp.setPaint(Color.GRAY);
            gp.drawGrid();
            mp.setPaint(Color.BLUE);
            ap.drawAxes();
            ap.drawTicks();
        }
        vx1 = v * Math.cos(theta * Math.PI / 180.0);
        vy1 = v * Math.sin(theta * Math.PI / 180.0);
        vx2 = v * Math.cos((90 - theta) * Math.PI / 180.0);
        vy2 = v * Math.sin((90 - theta) * Math.PI / 180.0);
        mp.setPaint(Color.RED);
        if (type == 0.0 || type == 3.0) {
            mp.fillCircle(0.0, 0.0);
            mp.fillCircle(r, 0.0);
        } else if (type == 1.0 || type == 4.0) {
            mp.fillCircle(0.0, 0.0);
            mp.drawLine(-0.2 * r, h1, 1.3 * r, h1);
        }
        mpp.setBase();
    }

    public void drawAnswer(MathPainterPanel mpp, CopyPanel cp) {
        if (type == 0.0 || type == 3.0) {
            mp = mpp.init(-0.2 * r, -0.1 * Math.max(h1, h2), 1.3 * r, 1.2 * Math.max(h1, h2), true);
        } else {
            mp = mpp.init(-0.2 * r, -0.1 * h1, 1.3 * r, 1.2 * h1, true);
        }
        mp.setScales();
        mpp.clearCompletely();
        ap = new AxesPlotter(mp);
        gp = new GridPlotter(mp);
        mp.setPaint(Color.WHITE);
        mp.setBackground();
        mp.setPaint(Color.LIGHT_GRAY);
        gp.drawFineGrid();
        mp.setPaint(Color.GRAY);
        gp.drawGrid();
        mp.setPaint(Color.BLUE);
        ap.drawAxes();
        ap.drawTicks();
        pcg = new ParametricCoordGenerator(mp);
        curvePlotter = new CurvePlotter(mp, pcg);
        this.animationType = FreeformQuestionInterface.ANSWER;
        thismpp = mpp;
        thiscp = cp;
        mp.setPaint(Color.BLACK);
        if ((type == 0.0 || type == 3.0) && !(theta == 45)) {
            try {
                pcg.setPoints("" + vx1 + "*t", "" + vy1 + "*t-4.9*t*t", "t", 0.0, tMax1);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            curvePlotter.plot();
            try {
                pcg.setPoints("" + vx2 + "*t", "" + vy2 + "*t-4.9*t*t", "t", 0.0, tMax2);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            curvePlotter.plot();
        } else {
            try {
                pcg.setPoints("" + vx1 + "*t", "" + vy1 + "*t-4.9*t*t", "t", 0.0, tMax1);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            curvePlotter.plot();
        }
        mp.setPaint(Color.RED);
        if (type == 0.0 || type == 3.0) {
            mp.fillCircle(0.0, 0.0);
            mp.fillCircle(r, 0.0);
        } else if (type == 1.0 || type == 4.0) {
            mp.fillCircle(0.0, 0.0);
            mp.drawLine(-0.2 * r, h1, 1.3 * r, h1);
        }
        mpp.setBase();
        if (runner != null && runner.isAlive()) {
            stop = true;
        }
        runner = null;
        if (runner == null) {
            runner = new Thread(this);
            runner.start();
        }
    }

    public void drawHint(MathPainterPanel mpp, CopyPanel cp) {
    }

    public void drawAnalysis(MathPainterPanel mpp, CopyPanel cp) {
    }

    public void run() {
        stop = false;
        if (runner != null) {
            double tMax;
            if (type == 0.0 || type == 3.0) tMax = Math.max(tMax1, tMax2); else tMax = tMax1;
            if (this.animationType == FreeformQuestionInterface.ANSWER) {
                try {
                    for (int i = 0; i <= 500; i++) {
                        double thist = i / 500.0 * tMax;
                        thismpp.clear();
                        mp.setPaint(Color.RED);
                        if ((type == 0.0 || type == 3.0) && !(theta == 45)) {
                            if (thist <= vy1 / 4.9) {
                                mp.fillCircle(vx1 * thist, vy1 * thist - 4.9 * thist * thist);
                                mp.drawVector(vx1 * thist, vy1 * thist - 4.9 * thist * thist, vx1 / 5, (vy1 - 9.8 * thist) / 5);
                            }
                            if (thist <= vy2 / 4.9) {
                                mp.fillCircle(vx2 * thist, vy2 * thist - 4.9 * thist * thist);
                                mp.drawVector(vx2 * thist, vy2 * thist - 4.9 * thist * thist, vx2 / 5, (vy2 - 9.8 * thist) / 5);
                            }
                        } else {
                            mp.fillCircle(vx1 * thist, vy1 * thist - 4.9 * thist * thist);
                            mp.drawVector(vx1 * thist, vy1 * thist - 4.9 * thist * thist, vx1 / 5, (vy1 - 9.8 * thist) / 5);
                        }
                        mp.setPaint(Color.BLUE);
                        if (type == 2.0) mp.drawString("t = " + Math.round(1000.0 * (thist)) / 1000.0, r / 2.2, 3 * h1 / 2);
                        Thread.sleep(5);
                        thismpp.update();
                        thiscp.update();
                        if (stop) {
                            break;
                        }
                    }
                } catch (InterruptedException iex) {
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    public QuestionInterface getExercise() {
        parameterSet = new ParameterSet(parameterNames);
        parameterSet.createParameter("type", typeValues);
        parameterSet.createParameter("v", vValues);
        parameterSet.createParameter("theta", thetaValues);
        parameterSet.createParameter("costwotheta", costwothetaValues);
        parameterSet.createParameter("sintwotheta", sintwothetaValues);
        parameterSet.createParameter("theta1", theta1Values);
        parameterSet.createParameter("sintheta1", sintheta1Values);
        freeformBranchingQuestionGenerator = new FreeformBranchingQuestionGenerator();
        freeformBranchingQuestionGenerator.setParameterSet(parameterSet);
        freeformBranchingQuestionGenerator.setQuestions(questionStrings, questionExpressions);
        freeformBranchingQuestionGenerator.setCorrectAnswer(correctAnswerExpression);
        freeformBranchingQuestionGenerator.setDistractors(distractorExpressions, distractorPatterns);
        freeformBranchingQuestionGenerator.setVariables(variables);
        freeformBranchingQuestionGenerator.setVectorVariables(vectorVariables);
        freeformBranchingQuestionGenerator.setVariableDomains(variableDomains);
        freeformBranchingQuestionGenerator.setTolerance(tolerance);
        freeformBranchingQuestionGenerator.setAnalysis(analysisStrings, analysisExpressions);
        freeformBranchingQuestionGenerator.setWorking(workingStrings, workingExpressions);
        freeformBranchingQuestionGenerator.setHelp(helpStrings, helpExpressions);
        freeformBranchingQuestionGenerator.setNotes("html/series/series_conver/series_conver1.html");
        freeformBranchingQuestionGenerator.setInstructions("html/help/exercise/type1.html");
        freeformBranchingQuestionGenerator.setFeedbackPrompt("<br><font color='red'>Click Feedback for more about your answer</font>");
        return freeformBranchingQuestionGenerator;
    }
}
