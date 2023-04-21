package net.sf.cplab.experiment.course;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import net.sf.cplab.experiment.method.ConstantStimuli;
import net.sf.cplab.experiment.setup.SetupAppDialog;
import net.sf.cplab.experiment.setup.TargetDistractorSetup;
import net.sf.cplab.experiment.stimulus.thresholdable.GenericThresholdable;
import net.sf.cplab.experiment.stimulus.visual.CharactersStimulus;

/**
 * @author James Tse
 * 
 */
public class VisualSearch extends ConstantStimuli implements MouseListener, KeyListener {

    /**
	 * 
	 */
    private static final long serialVersionUID = 901658051522649323L;

    private CharactersStimulus[] targets;

    private CharactersStimulus[] distractors;

    private Point target;

    private MouseEvent mouseEvent;

    private boolean escapeKeyPressed;

    private HashMap reactionTimeMap;

    private ArrayList pointList;

    private boolean[][] points;

    private BufferedImage searchArrayImage;

    private JLabel clickHere;

    public VisualSearch() {
        super();
        setTitle("Visual Search");
        reactionTimeMap = new HashMap();
    }

    protected boolean requestParameters() {
        GenericThresholdable stimulus = new GenericThresholdable();
        stimulus.setUnits("number of distractors");
        stimulus.setThresholdIterator(4, 32, 4);
        setThresholdStimulus(stimulus);
        if (super.requestParameters() == false) return false;
        TargetDistractorSetup setup = new TargetDistractorSetup();
        setup.launch();
        int response = setup.getResponse();
        setup.dispose();
        if (response == SetupAppDialog.RESPONSE_CANCEL) {
            return false;
        }
        Object[] object = setup.getTargets().toArray();
        targets = new CharactersStimulus[object.length];
        for (int i = 0; i < object.length; i++) {
            targets[i] = (CharactersStimulus) object[i];
        }
        object = setup.getDistractors().toArray();
        distractors = new CharactersStimulus[object.length];
        for (int i = 0; i < object.length; i++) {
            distractors[i] = (CharactersStimulus) object[i];
        }
        clickHere = new JLabel("+");
        int h = computer.getHeight();
        int w = computer.getWidth();
        searchArrayImage = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        h = (int) (h * .9);
        w = (int) (w * .9);
        pointList = new ArrayList(h * w);
        for (int j = 0; j < h; j++) {
            for (int i = 0; i < w; i++) {
                pointList.add(new Point(i, j));
            }
        }
        points = new boolean[h][w];
        return true;
    }

    protected boolean runSingleTrial(Double trial) {
        computer.setCursorVisible(true);
        long startTime = 0;
        Point clickPoint = new Point(0, 0);
        computer.showPrompt(clickHere);
        while (center.distanceSq(clickPoint) > 900) {
            MouseEvent me = getMouseClickEvent();
            if (escapeKeyPressed) {
                computer.removePrompt(clickHere);
                return false;
            }
            clickPoint = me.getPoint();
            startTime = me.getWhen();
        }
        long t1 = computer.time.currentTimeMillis();
        computer.removePrompt(clickHere);
        int distractors = trial.intValue();
        int w = computer.getWidth();
        int h = computer.getHeight();
        createSearchArrayImage(distractors);
        JLabel image = new JLabel(new ImageIcon(searchArrayImage));
        computer.showPrompt(image);
        long t2 = computer.time.currentTimeMillis();
        MouseEvent me = getMouseClickEvent();
        if (escapeKeyPressed) {
            computer.removePrompt(image);
            return false;
        }
        double radiusSq = (w * h / (distractors + 1) / 2) / Math.PI;
        long rt = me.getWhen() - (startTime + (t2 - t1));
        JLabel feedback;
        if (target.distanceSq(me.getPoint()) < radiusSq) {
            recordTrial(trial, true, rt);
            feedback = new JLabel("Correct. RT: " + rt + " ms.");
        } else {
            recordTrial(trial, false, rt);
            feedback = new JLabel("Incorrect. RT: " + rt + " ms.");
        }
        computer.removePrompt(image);
        computer.clearScreen();
        computer.showPrompt(feedback);
        computer.sleepFor(1500);
        computer.removePrompt(feedback);
        computer.setCursorVisible(false);
        return true;
    }

    /**
     * @param trial
     * @param b
     * @param rt
     */
    private void recordTrial(Double trial, boolean b, long rt) {
        super.recordTrial(trial, b);
        Record rec = (Record) reactionTimeMap.get(trial);
        if (rec == null) {
            rec = new Record();
            reactionTimeMap.put(trial, rec);
        }
        if (b) {
            rec.first += rt;
        } else {
            rec.second += rt;
        }
    }

    /**
     * 
     * @return MouseEvent. If MouseEvent is null, then escape key was pressed
     */
    private MouseEvent getMouseClickEvent() {
        mouseEvent = null;
        addMouseListener(this);
        addKeyListener(this);
        requestFocus();
        escapeKeyPressed = false;
        while (mouseEvent == null && escapeKeyPressed == false) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        removeMouseListener(this);
        removeKeyListener(this);
        MouseEvent me = mouseEvent;
        mouseEvent = null;
        return me;
    }

    private void createSearchArrayImage(int number) {
        int w = (int) (searchArrayImage.getWidth() * .9);
        int h = (int) (searchArrayImage.getHeight() * .9);
        int offsetX = (int) (searchArrayImage.getWidth() * .05);
        int offsetY = (int) (searchArrayImage.getHeight() * .05);
        Graphics g = searchArrayImage.getGraphics();
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, searchArrayImage.getWidth(), searchArrayImage.getHeight());
        for (int j = 0; j < points.length; j++) {
            for (int i = 0; i < points[0].length; i++) {
                points[j][i] = false;
            }
        }
        Collections.shuffle(pointList);
        Iterator listIter = pointList.iterator();
        target = getRandomAvailablePoint(listIter, points);
        double area = w * h / (number + 1);
        double radius = Math.sqrt(area / Math.PI) * 1.5;
        excludeRegion(points, target, radius);
        target.x += offsetX;
        target.y += offsetY;
        int t = computer.random.nextInt(targets.length);
        targets[t].setLocation(target.x, target.y);
        targets[t].draw(g);
        for (int i = 1; i <= number; i++) {
            Point p = getRandomAvailablePoint(listIter, points);
            if (p != null) {
                excludeRegion(points, p, radius);
                int d = computer.random.nextInt(distractors.length);
                distractors[d].setLocation(p.x + offsetX, p.y + offsetY);
                distractors[d].draw(g);
            } else {
                System.out.println("No more points available " + i + "/" + number);
            }
        }
        g.dispose();
    }

    /**
     * Marks coordinates around Point p of specified radius as true
     * 
     * @param points
     * @param p
     * @param radius Must be radius squared
     */
    private static void excludeRegion(boolean[][] points, Point p, double radius) {
        int x = p.x;
        int y = p.y;
        int r = (int) Math.floor(radius);
        double radius2 = radius * radius;
        int yMin = y - r;
        if (yMin < 0) yMin = 0;
        int yMax = y + r;
        if (yMax > points.length) yMax = points.length;
        int xMin = x - r;
        if (xMin < 0) xMin = 0;
        int xMax = x + r;
        if (xMax > points[0].length) xMax = points[0].length;
        for (int j = yMin; j < yMax; j++) {
            for (int i = xMin; i < xMax; i++) {
                double dist2 = (x - i) * (x - i) + (y - j) * (y - j);
                if (dist2 < radius2) {
                    points[j][i] = true;
                }
            }
        }
    }

    /**
     * Returns a random false point
     * @param points
     * @return
     */
    private static Point getRandomAvailablePoint(Iterator i, boolean[][] points) {
        Point point = null;
        while (i.hasNext() && point == null) {
            Point p = (Point) (i.next());
            try {
                if (points[p.y][p.x] == false) {
                    point = p;
                }
            } catch (ArrayIndexOutOfBoundsException e) {
                System.out.println("Trying to access point (" + p.x + "/" + points[0].length + "," + p.y + "/" + points.length + ")");
            }
        }
        return point;
    }

    public String getReportHeader() {
        return "Distractors,Correct,Incorrect,Correct RTs,Incorrect RTs\n";
    }

    public String getReportRow(Double level, Record record) {
        Record rtRec = (Record) reactionTimeMap.get(level);
        int correctRT = 0;
        int incorrectRT = 0;
        if (rtRec != null) {
            if (record.first != 0) correctRT = Math.round(rtRec.first / record.first);
            if (record.second != 0) incorrectRT = Math.round(rtRec.second / record.second);
        }
        return level.intValue() + "," + record.first + "," + record.second + "," + correctRT + "," + incorrectRT + "\n";
    }

    public void mouseClicked(MouseEvent e) {
        if (mouseEvent == null) mouseEvent = e;
    }

    public void mousePressed(MouseEvent arg0) {
    }

    public void mouseReleased(MouseEvent arg0) {
    }

    public void mouseEntered(MouseEvent arg0) {
    }

    public void mouseExited(MouseEvent arg0) {
    }

    public void keyTyped(KeyEvent arg0) {
    }

    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) escapeKeyPressed = true;
    }

    public void keyReleased(KeyEvent arg0) {
    }
}
