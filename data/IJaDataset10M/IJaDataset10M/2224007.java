package nl.utwente.ewi.hmi.deira.iam.vvciam.visualizer;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import nl.utwente.ewi.hmi.deira.iam.vvciam.EventGenerator;
import nl.utwente.ewi.hmi.deira.iam.vvciam.Situation;

/** PlayField geeft een Robosoccerveld weer.
 * Deze class kan als grafisch Component ingevoegd worden in een interface.
 * @version   
 * @author Buursma, Hendriksen, Ten Hoeve, Van der Kooi
 */
public class PlayField extends Canvas {

    /** De verhouding (pixels):(werkelijke grootte). Waarbij de werkelijke grootte in millimeters is.*/
    protected static final float SCALE = (new Float(0.2)).floatValue();

    private static final int FIELD_LENGTH = 2200;

    private static final int FIELD_WIDTH = 1800;

    private static final int GOAL_WIDTH = 400;

    private static final int GOAL_DEPTH = 200;

    private static final int ROBOT_SIZE = 75;

    private static final int BALL_DIAM = 43;

    private final int POINT_SIZE = 20;

    private static final int DIS_X = 40;

    private static final int DIS_Y = 0;

    private Situation situation;

    /** Cre�ert een nieuw PlayField.
    */
    public PlayField() {
        situation = null;
        setSize(scale(FIELD_LENGTH + GOAL_DEPTH * 2) + 5, scale(FIELD_WIDTH) + 5);
    }

    /** Zet de huidige Situation. Het PlayField zal de nieuwe situatie weergeven
     * @require situation != null
     */
    public void setSituation(Situation situation) {
        this.situation = situation;
    }

    /** Tekent het canvas opnieuw. Wordt aangeroepen via de functie repaint().
     */
    public synchronized void paint(Graphics g) {
        g.drawRect(scaleX(0), scaleY(0), scale(FIELD_LENGTH), scale(FIELD_WIDTH));
        g.drawRect(scaleX(-GOAL_DEPTH), scaleY((FIELD_WIDTH - GOAL_WIDTH) / 2), scale(GOAL_DEPTH), scale(GOAL_WIDTH));
        g.drawRect(scaleX(FIELD_LENGTH), scaleY((FIELD_WIDTH - GOAL_WIDTH) / 2), scale(GOAL_DEPTH), scale(GOAL_WIDTH));
        g.drawRect(scaleX(FIELD_LENGTH - 150), scaleY(650), scale(150), scale(500));
        g.drawRect(scaleX(0), scaleY(650), scale(150), scale(500));
        g.drawRect(scaleX(FIELD_LENGTH - 350), scaleY(500), scale(350), scale(800));
        g.drawRect(scaleX(0), scaleY(500), scale(350), scale(800));
        g.drawLine(scaleX(FIELD_LENGTH / 2), scaleY(0), scaleX(FIELD_LENGTH / 2), scaleY(FIELD_WIDTH));
        drawCross((int) EventGenerator.centerPoint.getX(), (int) EventGenerator.centerPoint.getY(), POINT_SIZE, g);
        g.drawOval(scaleX((FIELD_LENGTH / 2) - 250), scaleY((FIELD_WIDTH / 2) - 250), scale(500), scale(500));
        for (int i = 0; i < 4; i++) {
            g.fillRect(scaleX((int) EventGenerator.freeballPosition[i].getX() - POINT_SIZE / 2), scaleY((int) EventGenerator.freeballPosition[i].getY() - POINT_SIZE / 2), scale(POINT_SIZE), scale(POINT_SIZE));
            drawCross((int) EventGenerator.freeballRobotPosition[i][0].getX(), (int) EventGenerator.freeballRobotPosition[i][0].getY(), POINT_SIZE, g);
            drawCross((int) EventGenerator.freeballRobotPosition[i][1].getX(), (int) EventGenerator.freeballRobotPosition[i][1].getY(), POINT_SIZE, g);
        }
        drawCross((int) EventGenerator.penaltyPosition[0].getX(), (int) EventGenerator.penaltyPosition[0].getY(), POINT_SIZE, g);
        drawCross((int) EventGenerator.penaltyPosition[1].getX(), (int) EventGenerator.penaltyPosition[1].getY(), POINT_SIZE, g);
        int[][] x = { { 0, 0, 70 }, { 0, 0, 70 }, { FIELD_LENGTH, FIELD_LENGTH, FIELD_LENGTH - 70 }, { FIELD_LENGTH, FIELD_LENGTH, FIELD_LENGTH - 70 } };
        int[][] y = { { 0, 70, 0 }, { FIELD_WIDTH, FIELD_WIDTH - 70, FIELD_WIDTH }, { 0, 70, 0 }, { FIELD_WIDTH, FIELD_WIDTH - 70, FIELD_WIDTH } };
        int[][] xc = scaleX(x);
        int[][] yc = scaleY(y);
        g.fillPolygon(xc[0], yc[0], 3);
        g.fillPolygon(xc[1], yc[1], 3);
        g.fillPolygon(xc[2], yc[2], 3);
        g.fillPolygon(xc[3], yc[3], 3);
        if (situation != null) {
            int xt = (int) situation.getBall().getX();
            int yt = (int) situation.getBall().getY();
            drawBall(xt, yt, g, Color.black);
            for (int i = 0; i < 2; i++) {
                Color c = (i == 0 ? Color.blue : Color.red);
                for (int j = 0; j < 5; j++) {
                    xt = (int) situation.getRobot(i, j).getX();
                    yt = (int) situation.getRobot(i, j).getY();
                    drawRobot(j, xt, yt, g, c);
                }
            }
        }
    }

    /** Geeft een kruis weer met op x,y met grote size.
    */
    private void drawCross(int x, int y, int size, Graphics g) {
        g.drawLine(scaleX(x), scaleY(y - size / 2), scaleX(x), scaleY(y + size / 2));
        g.drawLine(scaleX(x - size / 2), scaleY(y), scaleX(x + size / 2), scaleY(y));
    }

    /** Geeft de bal weer in gegeven kleur op (x,y).
    */
    private void drawBall(int x, int y, Graphics g, Color c) {
        g.setColor(c);
        g.fillOval(scaleX(x - BALL_DIAM / 2), scaleY(y - BALL_DIAM / 2), scale(BALL_DIAM), scale(BALL_DIAM));
    }

    /** Geeft een Robot weer op het gegeven punt (x,y). De Robot wordt afgebeld in de opgegeven kleur en met gegeven nummer.
    */
    private void drawRobot(int n, int x, int y, Graphics g, Color c) {
        g.setColor(c);
        g.fillRect(scaleX(x - ROBOT_SIZE / 2), scaleY(y - ROBOT_SIZE / 2), scale(ROBOT_SIZE), scale(ROBOT_SIZE));
        g.setColor(Color.white);
        g.drawString("" + n, scaleX(x - ROBOT_SIZE / 2) + 4, scaleY(y + ROBOT_SIZE / 2) - 2);
    }

    private int scale(int v) {
        return Math.round(v * SCALE);
    }

    private int scaleX(int v) {
        return Math.round(v * SCALE) + DIS_X;
    }

    private int scaleY(int v) {
        return Math.round(v * SCALE) + DIS_Y;
    }

    private int[][] scaleX(int[][] vs) {
        for (int i = 0; i < vs.length; i++) {
            for (int j = 0; j < vs[i].length; j++) {
                vs[i][j] = scaleX(vs[i][j]);
            }
        }
        return vs;
    }

    private int[][] scaleY(int[][] vs) {
        for (int i = 0; i < vs.length; i++) {
            for (int j = 0; j < vs[i].length; j++) {
                vs[i][j] = scaleY(vs[i][j]);
            }
        }
        return vs;
    }
}
