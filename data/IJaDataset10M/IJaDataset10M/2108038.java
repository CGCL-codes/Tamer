package com.graphs.graphicengine;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.util.Date;
import java.util.Iterator;
import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.Timer;
import com.graphs.engine.algorithm.PhisicEngine;
import com.graphs.engine.data.Edge;
import com.graphs.engine.data.EngineTest;
import com.graphs.engine.data.GraphContener;
import com.graphs.engine.data.GraphException;
import com.graphs.engine.data.GraphLoader;
import com.graphs.engine.data.Vertex;

public class GraphPanel extends JPanel {

    private final int FRAMES_PER_SECOND = 50;

    private final String SCREENSHOOT_FILENAME = "scr.jpg";

    private final int SCROLL_SPEED = 15;

    JButton startAnimated = new JButton("Start Animated");

    JButton startNormal = new JButton("Start Normal");

    JButton loadGraph = new JButton("Load Graph");

    JButton restartGraph = new JButton("Restart");

    JButton graphGenerator = new JButton("Generator");

    JButton engineTest = new JButton("Engine Test");

    private GraphPanel me = this;

    Timer repainter;

    GraphContener graphContener;

    PhisicEngine engine;

    Edge newEdge = null;

    int newEdgeX;

    int newEdgeY;

    public GraphPanel(GraphContener data) {
        setBackground(Color.white);
        addMouseMotionListener(new MouseAdapter() {

            public void mouseDragged(MouseEvent e) {
                if (graphContener.getSelectedVertex() != null && newEdge == null) {
                    graphContener.getSelectedVertex().setX(e.getX() - graphContener.getTranslationX());
                    graphContener.getSelectedVertex().setY(e.getY() - graphContener.getTranslationY());
                }
                if (newEdge != null) {
                    newEdgeX = e.getX() - graphContener.getTranslationX();
                    newEdgeY = e.getY() - graphContener.getTranslationY();
                }
            }
        });
        addMouseListener(new MouseAdapter() {

            public void mousePressed(MouseEvent e) {
                boolean wasSelected = false;
                for (Iterator<Vertex> iter = graphContener.getVertexes().iterator(); iter.hasNext(); ) {
                    Vertex v = (Vertex) iter.next();
                    if (v.contains(e.getX() - graphContener.getTranslationX(), e.getY() - graphContener.getTranslationY())) {
                        graphContener.setSelectedVertex(v);
                        wasSelected = true;
                    }
                }
                if (e.getButton() == MouseEvent.BUTTON1) {
                    return;
                }
                if (e.getButton() == MouseEvent.BUTTON3) {
                    if (!wasSelected) {
                        final int wx = e.getX();
                        final int wy = e.getY();
                        JPopupMenu menu = new JPopupMenu();
                        JMenuItem itm = new JMenuItem("New Vertex");
                        itm.addActionListener(new ActionListener() {

                            public void actionPerformed(ActionEvent ae) {
                                try {
                                    graphContener.addVertex(String.valueOf(graphContener.getVertexes().size() + 1));
                                    Vertex v = graphContener.getVertex(String.valueOf(graphContener.getVertexes().size()));
                                    v.setX(wx);
                                    v.setY(wy);
                                } catch (GraphException e1) {
                                    e1.printStackTrace();
                                }
                            }
                        });
                        menu.add(itm);
                        itm = new JMenuItem("Calc crossed edges count");
                        itm.addActionListener(new ActionListener() {

                            public void actionPerformed(ActionEvent ae) {
                                int c = graphContener.getCrossedEdgesCount();
                                JOptionPane.showMessageDialog(getParent(), "Crossed edges = " + c);
                            }
                        });
                        menu.add(itm);
                        menu.show(e.getComponent(), e.getX(), e.getY());
                        return;
                    }
                    try {
                        newEdge = new Edge(graphContener.getSelectedVertex(), graphContener.getSelectedVertex());
                        newEdgeX = (int) graphContener.getSelectedVertex().getX();
                        newEdgeY = (int) graphContener.getSelectedVertex().getY();
                    } catch (GraphException e1) {
                    }
                }
            }

            public void mouseReleased(MouseEvent e) {
                if (newEdge != null) {
                    Vertex endVertex = null;
                    for (Iterator<Vertex> iter = graphContener.getVertexes().iterator(); iter.hasNext(); ) {
                        Vertex v = (Vertex) iter.next();
                        if (v.contains(e.getX() - graphContener.getTranslationX(), e.getY() - graphContener.getTranslationY())) {
                            endVertex = v;
                        }
                    }
                    if (endVertex != null) {
                        try {
                            graphContener.addEdge(newEdge.getA(), endVertex);
                        } catch (GraphException e1) {
                            e1.printStackTrace();
                        }
                    }
                }
                graphContener.setSelectedVertex(null);
                newEdge = null;
            }

            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    saveScreenshot();
                }
            }
        });
        setLayout(new BorderLayout());
        graphContener = data;
        engine = new PhisicEngine(data);
        prepareRepainter();
        repainter.start();
        loadGraph.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                final JFileChooser fc = new JFileChooser(new File("."));
                int returnVal = fc.showOpenDialog(getParent());
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    File file = fc.getSelectedFile();
                    engine.stopNormal();
                    engine.stopAnimation();
                    graphContener = GraphLoader.load(file);
                    engine.setGraphContener(graphContener);
                    engine.initCoords();
                }
            }
        });
        startAnimated.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                if (startAnimated.getText().equals("Start Animated")) {
                    startAnimated.setText("Stop Animated");
                    engine.startAnimation();
                    startNormal.setEnabled(false);
                } else {
                    startAnimated.setText("Start Animated");
                    engine.stopAnimation();
                    startNormal.setEnabled(true);
                }
            }
        });
        startNormal.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                if (startNormal.getText().equals("Start Normal")) {
                    startNormal.setText("Stop Normal");
                    engine.startNormal();
                    startAnimated.setEnabled(false);
                } else {
                    startNormal.setText("Start Normal");
                    engine.stopNormal();
                    startAnimated.setEnabled(true);
                }
            }
        });
        restartGraph.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                if (graphContener != null && graphContener.getFilePath() != null) {
                    System.out.println("Restarting engine");
                    engine.stopNormal();
                    engine.stopAnimation();
                    graphContener = GraphLoader.load(new File(graphContener.getFilePath()));
                    engine.setGraphContener(graphContener);
                    engine.initCoords();
                }
            }
        });
        graphGenerator.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                GeneratorDialog.showGeneratorDialog();
            }
        });
        engineTest.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                for (double gravity = 10000.0; gravity < 300000.0; gravity += 25000.0) for (double hookConst = 0.1; hookConst < 1.5; hookConst += 0.1) {
                    System.out.println("TESTING FOR:" + gravity + "," + hookConst);
                    EngineTest.testEngine(GraphPanel.this.getParent(), gravity, hookConst);
                }
            }
        });
        JPanel buttonContener = new JPanel();
        buttonContener.add(startAnimated);
        buttonContener.add(startNormal);
        buttonContener.add(loadGraph);
        buttonContener.add(restartGraph);
        buttonContener.add(graphGenerator);
        buttonContener.add(engineTest);
        add(buttonContener, BorderLayout.SOUTH);
        KeyAdapter adapter = new KeyAdapter() {

            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                    getGraphContener().adjustTranslationX(-SCROLL_SPEED);
                }
                if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                    getGraphContener().adjustTranslationX(SCROLL_SPEED);
                }
                if (e.getKeyCode() == KeyEvent.VK_UP) {
                    getGraphContener().adjustTranslationY(SCROLL_SPEED);
                }
                if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                    getGraphContener().adjustTranslationY(-SCROLL_SPEED);
                }
            }
        };
        startNormal.addKeyListener(adapter);
        startAnimated.addKeyListener(adapter);
        loadGraph.addKeyListener(adapter);
        restartGraph.addKeyListener(adapter);
        engineTest.addKeyListener(adapter);
        graphGenerator.addKeyListener(adapter);
    }

    private void saveScreenshot() {
        BufferedImage buff = GraphDrawer.generateImage(graphContener);
        try {
            ImageIO.write(buff, "jpg", new File(SCREENSHOOT_FILENAME));
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        JOptionPane.showMessageDialog(GraphPanel.this, "Screenshot saved");
    }

    private void prepareRepainter() {
        repainter = new Timer(1000 / FRAMES_PER_SECOND, new ActionListener() {

            public void actionPerformed(ActionEvent arg0) {
                repaint();
            }
        });
    }

    private void drawTime(Graphics2D g) {
        Date now = new Date();
        g.drawString(DateFormat.getTimeInstance().format(now), 20, 20);
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawTime((Graphics2D) g);
        GraphDrawer.drawGraph((Graphics2D) g, graphContener, 0, 0, false);
        drawNewEdge((Graphics2D) g);
    }

    private void drawNewEdge(Graphics2D g) {
        AffineTransform saveAT = g.getTransform();
        g.translate(graphContener.getTranslationX(), graphContener.getTranslationY());
        g.setColor(Color.black);
        if (newEdge != null) {
            g.drawLine((int) newEdge.getA().getX(), (int) newEdge.getA().getY(), newEdgeX, newEdgeY);
        }
        g.setTransform(saveAT);
    }

    public GraphContener getGraphContener() {
        return graphContener;
    }
}
