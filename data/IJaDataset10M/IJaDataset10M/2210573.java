package ru.concretesoft.concretesplitviewer;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.LinkedList;
import java.util.List;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import ru.spb.ConcreteSoft.tipWindow.MouseMoveQueue;
import ru.spb.ConcreteSoft.tipWindow.TipWindow;

/**
 *
 * @author Mytinski Leonid
 * 
 * Panel for viewing splits in standatd way
 * Панель для отображения сплитов в стандартном виде
 */
public class StandardSplitViewer extends javax.swing.JPanel implements SplitViewer, ListDataListener, ListSelectionListener, MouseListener, MouseMotionListener {

    private AthleteListModel aModel;

    private int[] xCoord;

    private int otst = 5;

    private TipWindow tipWindow;

    private TipThreadSplitViewer tipThread;

    private List<XCoordinatesListener> listeners;

    private GlassStandartSplitViewerPanel glassPane;

    private int editingCPNumber;

    private AthleteIcon editingAthlete;

    private int yLocationOfStartDrag;

    /**
     * Creates new form StandardSplitViewer
     */
    public StandardSplitViewer() {
        tipWindow = new TipWindow();
        initComponents();
        addMouseMotionListener(MouseMoveQueue.getInstance());
        addMouseListener(this);
        addMouseMotionListener(this);
        listeners = new LinkedList<XCoordinatesListener>();
        glassPane = new GlassStandartSplitViewerPanel();
    }

    private void initComponents() {
        setLayout(new java.awt.GridBagLayout());
    }

    @Override
    public void paint(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        Dimension d = getSize();
        g2.setPaint(Color.WHITE);
        g2.fillRect(0, 0, d.width, d.height);
        FontMetrics fM = g2.getFontMetrics();
        otst = fM.stringWidth("-000:00") + 5;
        int width = d.width - otst;
        int height = d.height;
        AthleteIcon[] athletes = (AthleteIcon[]) (aModel.getSelectedValues());
        int[] spl = aModel.getViewingSplits();
        Distance dist = aModel.getDistance();
        int size = athletes.length;
        if ((spl != null) && (size > 0)) {
            xCoord = new int[spl.length];
            if (dist != null) {
                int totDist = Tools.calculateTotalLength(dist, spl);
                int curDist = otst;
                for (int i = 0; i < spl.length; i++) {
                    xCoord[i] = curDist + (int) ((dist.getLengthOfDist(spl[i]) / (double) totDist) * width);
                    curDist = xCoord[i];
                }
            } else {
                int sizeLap = width / spl.length;
                for (int i = 0; i < spl.length; i++) xCoord[i] = sizeLap * (i + 1);
            }
            g2.setPaint(Color.BLACK);
            for (int i = 0; i < spl.length; i++) {
                g2.drawLine(xCoord[i], 0, xCoord[i], height);
            }
            int yMax = athletes[size - 1].getTotalTime().getTimeInSeconds();
            int hTime = fM.getHeight();
            double stepTime = 30.0;
            int nT = (int) (yMax / stepTime);
            while ((height / nT) < (hTime + 10)) {
                stepTime *= 2;
                nT = (int) (yMax / stepTime);
            }
            Time tmp = new Time(0, 2);
            String s = tmp.getTimeString();
            g2.setPaint(Color.BLACK);
            g2.drawString(s, otst - fM.stringWidth(s), height - 1);
            float dash1[] = { 10.0f };
            BasicStroke dashed = new BasicStroke(1.0f, java.awt.BasicStroke.CAP_BUTT, java.awt.BasicStroke.JOIN_MITER, 10.0f, dash1, 0.0f);
            g2.setStroke(dashed);
            for (int i = 1; i < nT; i++) {
                int timeCur = Math.abs((int) (stepTime * i));
                tmp.setTimeInSeconds(timeCur);
                s = tmp.getTimeString();
                int yH = height - (int) ((stepTime * i / yMax) * height);
                g2.setPaint(Color.BLACK);
                g2.drawString(s, otst - fM.stringWidth(s), yH);
                g2.setPaint(Color.LIGHT_GRAY);
                g2.drawLine(otst, yH, otst + width, yH);
            }
            tmp.setTimeInSeconds((int) (stepTime * nT));
            s = tmp.getTimeString();
            g2.setPaint(Color.BLACK);
            g2.drawString(s, otst - fM.stringWidth(s), hTime + 1);
            int yH = height - (int) ((stepTime * nT / yMax) * height);
            g2.setPaint(Color.LIGHT_GRAY);
            g2.drawLine(otst, yH, otst + width, yH);
            g2.setStroke(new BasicStroke(1.0f));
            for (int j = 0; j < size; j++) {
                g2.setPaint(athletes[j].getColor());
                Athlete tmpA = athletes[j].getAthlete();
                int x = xCoord[0];
                int y = height - (int) ((tmpA.getLap(spl[0]).getTimeInSeconds() / (double) yMax) * height);
                int totTime = tmpA.getLap(spl[0]).getTimeInSeconds();
                g2.drawLine(otst, height, x, y);
                for (int i = 1; i < spl.length; i++) {
                    int x1 = xCoord[i];
                    if (tmpA.getLap(spl[i]).getTimeInSeconds() == 0) break;
                    totTime += tmpA.getLap(spl[i]).getTimeInSeconds();
                    int y1 = height - (int) ((totTime / (double) yMax) * height);
                    g2.drawLine(x, y, x1, y1);
                    x = x1;
                    y = y1;
                }
            }
        } else {
            xCoord = null;
        }
        for (XCoordinatesListener e : listeners) {
            e.xCoordinatesChanged(this);
        }
    }

    @Override
    public String toString() {
        return java.util.ResourceBundle.getBundle("ru/concretesoft/concretesplitviewer/i18on").getString("Standart_View");
    }

    public void removeSplit(int x) {
        if (xCoord != null) {
            for (int i = 0; i < xCoord.length; i++) {
                if (x < xCoord[i]) {
                    aModel.removeSplitsForN(aModel.getViewingSplits()[i]);
                    break;
                }
            }
        }
    }

    public void setModel(AthleteListModel aM) {
        aModel = aM;
        aM.addListDataListener(this);
        aM.addListSelectionListener(this);
    }

    public AthleteListModel getModel() {
        return aModel;
    }

    public int getSplit(Point p) {
        if (xCoord != null) {
            for (int i = 0; i < xCoord.length; i++) {
                if (p.getX() < xCoord[i]) {
                    return aModel.getViewingSplits()[i];
                } else ;
            }
        } else {
            return -1;
        }
        return -1;
    }

    public void intervalAdded(ListDataEvent e) {
        contentsChanged(e);
    }

    public void intervalRemoved(ListDataEvent e) {
        contentsChanged(e);
    }

    public void contentsChanged(ListDataEvent e) {
        repaint();
    }

    public void valueChanged(ListSelectionEvent e) {
        repaint();
    }

    public void mouseClicked(MouseEvent evt) {
        if ((evt.getButton() == MouseEvent.BUTTON2) || (MouseEvent.getMouseModifiersText(evt.getModifiers()).equals("Shift+Button1"))) {
            aModel.restoreAllSplits();
        } else if (evt.getButton() == MouseEvent.BUTTON1) {
            removeSplit(evt.getX());
        }
    }

    public void mousePressed(MouseEvent e) {
        int x = e.getX();
        int y = e.getY();
        for (int i = 0; i < xCoord.length; i++) {
            if (Math.abs(xCoord[i] - x) < 5) {
                editingCPNumber = i;
                AthleteIcon[] selectedAthletes = (AthleteIcon[]) aModel.getSelectedValues();
                int yMax = selectedAthletes[selectedAthletes.length - 1].getTotalTime().getTimeInSeconds();
                double scale = yMax / (double) getSize().height;
                for (int j = 0; j < selectedAthletes.length; j++) {
                    int yA = 0;
                    for (int k = 0; k <= editingCPNumber; k++) {
                        yA += selectedAthletes[j].getAthlete().getLap(aModel.getViewingSplits()[k]).getTimeInSeconds();
                    }
                    yA = getSize().height - (int) (yA / scale);
                    if (Math.abs(y - yA) < 2) {
                        yLocationOfStartDrag = yA;
                        editingAthlete = selectedAthletes[j];
                        glassPane.setAthlete(editingAthlete);
                        glassPane.setViewingSplits(aModel.getViewingSplits());
                        glassPane.setCPsNumber(editingCPNumber);
                        glassPane.setScale(scale);
                        glassPane.setXCoord(xCoord);
                        glassPane.setYMax(yMax);
                        glassPane.setOtst(otst);
                        glassPane.setLocationOnScreen(this.getLocationOnScreen());
                        glassPane.setVisible(true);
                        glassPane.setYLocation(y);
                        break;
                    }
                }
                break;
            } else if (x < xCoord[i]) {
                break;
            }
        }
    }

    public void mouseReleased(MouseEvent e) {
        glassPane.setVisible(false);
        glassPane.setAthlete(null);
        if (editingAthlete == null) {
            return;
        }
        AthleteIcon[] selectedAthletes = (AthleteIcon[]) aModel.getSelectedValues();
        int yMax = selectedAthletes[selectedAthletes.length - 1].getTotalTime().getTimeInSeconds();
        double scale = yMax / (double) getSize().height;
        editingCPNumber = aModel.getViewingSplits()[editingCPNumber];
        Time oldTime = editingAthlete.getAthlete().getLap(editingCPNumber);
        int diff = e.getY() - yLocationOfStartDrag;
        int diffInSec = (int) (diff * scale);
        Time newTime = new Time(0, 2);
        newTime.setTimeInSeconds(oldTime.getTimeInSeconds() - diffInSec);
        editingAthlete.getAthlete().setTimeOnLap(newTime, editingCPNumber);
        editingAthlete = null;
        editingCPNumber = 0;
    }

    public void mouseEntered(MouseEvent e) {
        tipThread = new TipThreadSplitViewer(tipWindow, this);
        tipThread.start();
    }

    public void mouseExited(MouseEvent e) {
        tipThread.finish();
    }

    public int[] getXCoordinatesOfLaps() {
        return xCoord;
    }

    public void addXCoordinatesListener(XCoordinatesListener listener) {
        listeners.add(listener);
    }

    public void removeXCoordinatesListener(XCoordinatesListener listener) {
        listeners.remove(listener);
    }

    public GlassStandartSplitViewerPanel getGlassPane() {
        return glassPane;
    }

    public void mouseDragged(MouseEvent e) {
        if (editingAthlete == null) {
            return;
        }
        glassPane.setYLocation(e.getY());
    }

    public void mouseMoved(MouseEvent e) {
    }
}
