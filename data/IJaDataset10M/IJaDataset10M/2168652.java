package org.myrobotlab.control;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.Random;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import org.apache.log4j.Logger;
import org.myrobotlab.service.Runtime;
import org.myrobotlab.gp.GPMessageBestFound;
import org.myrobotlab.gp.GPMessageEvaluatingIndividual;
import org.myrobotlab.gp.RealPoint;
import org.myrobotlab.image.SerializableImage;
import org.myrobotlab.service.interfaces.GUI;
import org.myrobotlab.service.interfaces.VideoGUISource;

public class GeneticProgrammingGUI extends ServiceGUI implements ListSelectionListener, VideoGUISource {

    static final long serialVersionUID = 1L;

    public static final Logger LOG = Logger.getLogger(GeneticProgrammingGUI.class.toString());

    VideoWidget video = null;

    Graphics g = null;

    BufferedImage img = null;

    int width = 320;

    int height = 240;

    public Random rand = new Random();

    RealPoint[] fitnessCases = new RealPoint[4];

    JTextArea fitnessCasesTextArea = null;

    JTextArea bestProgram = null;

    JTextField populationSize = new JTextField("100");

    JTextField maxDepthNewInd = new JTextField("6.0");

    JTextField crossover = new JTextField("80.0");

    JTextField reproduction = new JTextField("0.0");

    JTextField mutation = new JTextField("20.0");

    JTextField depthForIndAlterCrossover = new JTextField("20.0");

    JTextField depthNewSubtreesInMutant = new JTextField("4.0");

    GPMessageBestFound lastBest = null;

    public GeneticProgrammingGUI(final String boundServiceName, final GUI myService) {
        super(boundServiceName, myService);
    }

    public void init() {
        img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        g = img.getGraphics();
        video.displayFrame(img);
        fitnessCases[0] = new RealPoint(123, 164);
        fitnessCases[1] = new RealPoint(249, 164);
        fitnessCases[2] = new RealPoint(218, 142);
        fitnessCases[3] = new RealPoint(130, 142);
        String s = "// the data \n";
        for (int i = 0; i < fitnessCases.length; ++i) {
            s += fitnessCases[i].x + " " + fitnessCases[i].y + "\n";
        }
        bestProgram = new JTextArea("new program", 8, 20);
        fitnessCasesTextArea = new JTextArea(s, 8, 20);
        video = new VideoWidget(boundServiceName, myService);
        video.init();
        gc.gridx = 0;
        gc.gridy = 0;
        gc.gridheight = 4;
        gc.gridwidth = 2;
        display.add(video.display, gc);
        gc.gridwidth = 1;
        gc.gridheight = 1;
        gc.gridx = 3;
        gc.gridy = 0;
        display.add(bestProgram, gc);
        gc.gridx = 0;
        gc.gridy = 5;
        display.add(fitnessCasesTextArea, gc);
        gc.gridx = 0;
        display.add(new JLabel("population size"), gc);
        gc.gridx = 1;
        ++gc.gridy;
        display.add(populationSize, gc);
        gc.gridx = 0;
        display.add(new JLabel("max depth for new ind"), gc);
        gc.gridx = 1;
        ++gc.gridy;
        display.add(maxDepthNewInd, gc);
        gc.gridx = 0;
        display.add(new JLabel("crossover"), gc);
        gc.gridx = 1;
        ++gc.gridy;
        display.add(crossover, gc);
        gc.gridx = 0;
        display.add(new JLabel("reproduction"), gc);
        gc.gridx = 1;
        ++gc.gridy;
        display.add(reproduction, gc);
        gc.gridx = 0;
        display.add(new JLabel("mutation"), gc);
        gc.gridx = 1;
        ++gc.gridy;
        display.add(mutation, gc);
        gc.gridx = 0;
        display.add(new JLabel("depth for ind alter crossover"), gc);
        gc.gridx = 1;
        ++gc.gridy;
        display.add(depthForIndAlterCrossover, gc);
        gc.gridx = 0;
        display.add(new JLabel("depth new subtrees in mutant"), gc);
        gc.gridx = 1;
        ++gc.gridy;
        display.add(depthNewSubtreesInMutant, gc);
        setCurrentFilterMouseListener();
    }

    protected ImageIcon createImageIcon(String path, String description) {
        java.net.URL imgURL = getClass().getResource(path);
        if (imgURL != null) {
            return new ImageIcon(imgURL, description);
        } else {
            System.err.println("Couldn't find file: " + path);
            return null;
        }
    }

    public void displayFrame(SerializableImage img) {
        video.displayFrame(img);
    }

    void drawFitnessGoal() {
        g.setColor(Color.yellow);
        for (int i = 0; i < fitnessCases.length; ++i) {
            if (i != fitnessCases.length - 1) {
                g.drawLine((int) fitnessCases[i].x, (int) fitnessCases[i].y, (int) fitnessCases[i + 1].x, (int) fitnessCases[i + 1].y);
            } else {
                g.drawLine((int) fitnessCases[i].x, (int) fitnessCases[i].y, (int) fitnessCases[0].x, (int) fitnessCases[0].y);
            }
        }
    }

    @Override
    public void attachGUI() {
        video.attachGUI();
        sendNotifyRequest("publishInd", "publishInd", GPMessageEvaluatingIndividual.class);
        sendNotifyRequest("publish", "publish", GPMessageBestFound.class);
        drawFitnessGoal();
        video.displayFrame(img);
    }

    void drawPath(RealPoint[] path) {
        for (int i = 0; i < path.length; ++i) {
            if (i != path.length - 1) {
                RealPoint p = path[i];
                RealPoint p1 = path[i + 1];
                g.drawLine((int) p.x, (int) p.y, (int) p1.x, (int) p1.y);
            } else {
                RealPoint p = path[i];
                RealPoint p1 = path[0];
                g.drawLine((int) p.x, (int) p.y, (int) p1.x, (int) p1.y);
            }
        }
    }

    public GPMessageEvaluatingIndividual publishInd(GPMessageEvaluatingIndividual ind) {
        g.setColor(Color.black);
        g.fillRect(0, 0, width, height);
        drawFitnessGoal();
        if (lastBest != null) {
            g.setColor(Color.red);
            g.drawString("fitness    " + lastBest.fitness, 10, 10);
            g.drawString("rfitness    " + lastBest.standardFitness, 10, 20);
            g.drawString("generation " + lastBest.generation, 10, 30);
            drawPath(lastBest.data);
        }
        g.setColor(Color.gray);
        g.drawString("ind #      " + ind.individualNr, 10, 40);
        g.drawString("generation " + ind.generationNr, 10, 50);
        g.drawString("fitness " + ind.fitness, 10, 60);
        g.drawString("rfitness " + ind.rawFitness, 10, 70);
        drawPath(ind.data);
        video.displayFrame(img);
        return ind;
    }

    public GPMessageBestFound publish(GPMessageBestFound best) {
        lastBest = best;
        g.setColor(Color.black);
        g.fillRect(0, 0, width, height);
        drawFitnessGoal();
        g.setColor(Color.red);
        drawPath(best.data);
        g.drawString("fitness    " + best.fitness, 10, 10);
        g.drawString("generation " + best.generation, 10, 20);
        bestProgram.setText(best.program);
        video.displayFrame(img);
        return best;
    }

    @Override
    public void detachGUI() {
        video.detachGUI();
    }

    public void setCurrentFilterMouseListener() {
    }

    @Override
    public VideoWidget getLocalDisplay() {
        return video;
    }

    @Override
    public void valueChanged(ListSelectionEvent arg0) {
    }
}
