package org.lane.riccochet.ui;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;

public class Riccochet extends JFrame implements MouseMotionListener, FocusListener, KeyListener {

    public static final long serialVersionUID = 1L;

    private boolean arenaInFocus = false;

    private int wallBouncePoints = 20;

    private int bounceSpeedX = 3;

    private int bounceSpeedY = 1;

    private int xMultiplier = -1;

    private int yMultiplier = 1;

    private long sleepTime = 10;

    private long gametime = Calendar.getInstance().getTimeInMillis();

    private int polesCount = 4;

    private int currentHeroImage = 0;

    private int heroImagesCount = 4;

    private int bubbletimer = 0;

    private int bubbleTimerInterval = 10;

    private int levelBubbles = 3;

    private int mptimer = 0;

    private int mpTimerInterval = 30;

    private int magneticPowerMax = 70;

    private Point heroLocation = new Point(23, 350);

    private long points = 00000000L;

    private int lives = 3;

    private int magneticPower = 70;

    private long time = 000000L;

    private JPanel backgroundPanel;

    private JPanel titlePanel;

    private JPanel gamePanel;

    private JPanel userFeedbackPanel;

    private JPanel arena;

    private JLabel[] lightPoles = new JLabel[polesCount];

    private JLabel[] darkPoles = new JLabel[polesCount];

    private ArrayList<JLabel> bubbles;

    private ArrayList<JLabel> badsaucers;

    private JLabel heroSaucer;

    private JLabel titleLabel;

    private JLabel livesLabel;

    private JLabel pointsLabel;

    private JLabel magneticPowerLabel;

    private JLabel timeLabel;

    public Riccochet() {
        super("Riccochet");
        setupUI();
    }

    public void run() throws NullPointerException, InterruptedException {
        long timertime = 0;
        setVisible(true);
        arena.requestFocus();
        while (true) {
            if (arenaInFocus) {
                int currentX = heroLocation.x;
                int currentY = heroLocation.y;
                currentX += (bounceSpeedX * xMultiplier);
                currentY += (bounceSpeedY * yMultiplier);
                heroLocation.x = currentX;
                heroLocation.y = currentY;
                heroSaucer.setLocation(heroLocation);
                if (collisionX(heroSaucer)) {
                    xMultiplier *= (-1);
                }
                if (collisionY(heroSaucer)) {
                    yMultiplier *= (-1);
                }
                if (timertime >= 1000) {
                    time += 1;
                    timeLabel.setText("TIME: " + padTime(time));
                    timertime = 0;
                    bubbletimer += 1;
                    mptimer += 1;
                }
                if (bubbletimer == bubbleTimerInterval) {
                    bubbletimer = 0;
                }
                if (mptimer == mpTimerInterval) {
                    mptimer = 0;
                    if (magneticPower < magneticPowerMax) {
                        magneticPower += 5;
                        magneticPowerLabel.setText("MP: " + magneticPower + "%");
                    }
                }
                arena.requestFocus();
                timertime += 20;
            }
            Thread.sleep(sleepTime);
            this.update(getGraphics());
        }
    }

    private boolean collisionX(JLabel object) {
        boolean retVal = false;
        if (object.getLocation().x <= 0) {
            retVal = true;
            points += wallBouncePoints;
            pointsLabel.setText("POINTS: " + padPoints(points));
            bounceSpeedX = 3;
        } else if ((object.getLocation().x + object.getWidth()) >= 639) {
            retVal = true;
            points += wallBouncePoints;
            pointsLabel.setText("POINTS: " + padPoints(points));
            bounceSpeedX = 3;
        } else {
            bounceSpeedX = 3;
        }
        return retVal;
    }

    private boolean collisionY(JLabel object) {
        boolean retVal = false;
        if (object.getLocation().y <= 0) {
            retVal = true;
            points += wallBouncePoints;
            pointsLabel.setText("POINTS: " + padPoints(points));
            bounceSpeedY = 1;
        } else if ((object.getLocation().y + object.getHeight()) >= 437) {
            retVal = true;
            points += wallBouncePoints;
            pointsLabel.setText("POINTS: " + padPoints(points));
            bounceSpeedY = 1;
        } else {
            bounceSpeedY = 1;
        }
        return retVal;
    }

    private void setupUI() {
        setSize(642, 486);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setUndecorated(true);
        backgroundPanel = new JPanel();
        backgroundPanel.setBackground(Color.BLACK);
        backgroundPanel.setLayout(null);
        backgroundPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        setContentPane(backgroundPanel);
        titlePanel = new JPanel();
        titlePanel.setBackground(Color.GRAY);
        titlePanel.setLayout(new BorderLayout());
        titlePanel.setSize(640, 4);
        backgroundPanel.add(titlePanel);
        titlePanel.setLocation(1, 1);
        titlePanel.addMouseMotionListener(this);
        gamePanel = new JPanel();
        gamePanel.setBackground(Color.BLACK);
        gamePanel.setLayout(null);
        gamePanel.setSize(640, 480);
        backgroundPanel.add(gamePanel);
        gamePanel.setLocation(1, 6);
        setupGamePanel();
    }

    private void setupGamePanel() {
        arena = new JPanel();
        arena.setOpaque(false);
        arena.setBorder(BorderFactory.createLineBorder(Color.YELLOW, 2));
        arena.setSize(640, 440);
        arena.setLayout(null);
        gamePanel.add(arena);
        arena.setLocation(0, 0);
        arena.setFocusable(true);
        arena.addFocusListener(this);
        arena.addKeyListener(this);
        setupArena();
        userFeedbackPanel = new JPanel();
        userFeedbackPanel.setBackground(Color.GRAY);
        userFeedbackPanel.setLayout(null);
        userFeedbackPanel.setSize(640, 40);
        gamePanel.add(userFeedbackPanel);
        userFeedbackPanel.setLocation(0, 439);
        setupUserFeedbackPanel();
    }

    private void setupUserFeedbackPanel() {
        Font feedbackFont = new Font("Arial", Font.BOLD, 22);
        Color feedbackColor = new Color(0, 255, 222);
        livesLabel = new JLabel("LIVES: " + lives);
        livesLabel.setFont(feedbackFont);
        livesLabel.setForeground(feedbackColor);
        livesLabel.setSize(livesLabel.getPreferredSize().width, livesLabel.getPreferredSize().height);
        userFeedbackPanel.add(livesLabel);
        livesLabel.setLocation(0, 0);
        pointsLabel = new JLabel("POINTS: " + padPoints(points));
        pointsLabel.setFont(feedbackFont);
        pointsLabel.setForeground(feedbackColor);
        pointsLabel.setSize(pointsLabel.getPreferredSize().width, pointsLabel.getPreferredSize().height);
        userFeedbackPanel.add(pointsLabel);
        pointsLabel.setLocation(130, 0);
        magneticPowerLabel = new JLabel("MP: " + magneticPower + "%");
        magneticPowerLabel.setFont(feedbackFont);
        magneticPowerLabel.setForeground(feedbackColor);
        magneticPowerLabel.setSize(magneticPowerLabel.getPreferredSize().width, magneticPowerLabel.getPreferredSize().height);
        userFeedbackPanel.add(magneticPowerLabel);
        magneticPowerLabel.setLocation(360, 0);
        timeLabel = new JLabel("TIME: " + padTime(time));
        timeLabel.setFont(feedbackFont);
        timeLabel.setForeground(feedbackColor);
        timeLabel.setSize(timeLabel.getPreferredSize().width, timeLabel.getPreferredSize().height);
        userFeedbackPanel.add(timeLabel);
        timeLabel.setLocation(480, 0);
        ImageIcon icon = new ImageIcon("images/riccochettitle.png");
        titleLabel = new JLabel(icon);
        titleLabel.setSize(icon.getIconWidth(), icon.getIconHeight());
        userFeedbackPanel.add(titleLabel);
        titleLabel.setLocation(480, 30);
    }

    private void setupArena() {
        ImageIcon icon = new ImageIcon("images/heroship00.png");
        heroSaucer = new JLabel(icon);
        heroSaucer.setSize(icon.getIconWidth() + 6, icon.getIconHeight() + 6);
        arena.add(heroSaucer);
        heroSaucer.setLocation(heroLocation);
        for (int i = 0; i < darkPoles.length; ++i) {
            icon = new ImageIcon("images/darkpole.png");
            darkPoles[i] = new JLabel(icon);
            darkPoles[i].setSize(icon.getIconWidth() + 6, icon.getIconHeight() + 6);
            arena.add(darkPoles[i]);
        }
        darkPoles[0].setLocation(100 - 3, 80 - 3);
        darkPoles[1].setLocation(540 - 3, 80 - 3);
        darkPoles[2].setLocation(100 - 3, 340 - 3);
        darkPoles[3].setLocation(540 - 3, 340 - 3);
        for (int i = 0; i < lightPoles.length; ++i) {
            icon = new ImageIcon("images/lightpole.png");
            lightPoles[i] = new JLabel(icon);
            lightPoles[i].setSize(icon.getIconWidth() + 6, icon.getIconHeight() + 6);
            arena.add(lightPoles[i]);
        }
        lightPoles[0].setLocation(220 - 3, 140 - 3);
        lightPoles[1].setLocation(420 - 3, 140 - 3);
        lightPoles[2].setLocation(220 - 3, 300 - 3);
        lightPoles[3].setLocation(420 - 3, 300 - 3);
    }

    private String padPoints(long locpoints) {
        String retVal = "" + locpoints;
        String padding = "";
        for (int i = retVal.length(); i < 8; ++i) {
            padding += "0";
        }
        retVal = padding + locpoints;
        return retVal;
    }

    private String padTime(long loctime) {
        String retVal = "" + loctime;
        String padding = "";
        for (int i = retVal.length(); i < 6; ++i) {
            padding += "0";
        }
        retVal = padding + loctime;
        return retVal;
    }

    public void mouseDragged(MouseEvent me) {
        if (me.getSource().equals(titlePanel)) {
            Point lastMouseLocation = me.getPoint();
            Point lastFrameLocation = backgroundPanel.getLocation();
            setLocation(lastFrameLocation.x + lastMouseLocation.x, lastFrameLocation.y + lastMouseLocation.y);
            System.out.println("Mouse dragged: " + titlePanel.getLocation().x + ", " + titlePanel.getLocation().y);
        }
    }

    public void mouseMoved(MouseEvent me) {
        System.out.println("mouse moved: " + me.getX() + ", " + me.getY());
    }

    public void focusGained(FocusEvent fe) {
        if (fe.getSource().equals(arena)) {
            arenaInFocus = true;
        }
    }

    public void focusLost(FocusEvent fe) {
        if (fe.getSource().equals(arena)) {
            arenaInFocus = false;
        }
    }

    public void keyPressed(KeyEvent ke) {
        if (ke.getSource().equals(arena)) {
            System.out.println("Arena fired keyPressed event: " + ke.getKeyCode() + " mod: " + ke.getModifiersEx());
            if (ke.getKeyCode() == 27) {
                System.out.println("Game ended");
                setVisible(false);
                dispose();
                System.exit(0);
            } else if (ke.getKeyCode() == 37) {
                System.out.println("Hero pointing left");
                heroSaucer.setIcon(new ImageIcon("images/heroship03.png"));
            } else if (ke.getKeyCode() == 38) {
                System.out.println("Hero pointing up");
                heroSaucer.setIcon(new ImageIcon("images/heroship00.png"));
            } else if (ke.getKeyCode() == 39) {
                System.out.println("Hero pointing right");
                heroSaucer.setIcon(new ImageIcon("images/heroship01.png"));
            } else if (ke.getKeyCode() == 40) {
                System.out.println("Hero pointing down");
                heroSaucer.setIcon(new ImageIcon("images/heroship02.png"));
            } else if (ke.getKeyCode() == 16) {
                System.out.println("Shift");
            } else if (ke.getKeyCode() == 17) {
                System.out.println("Control");
            } else if (ke.getKeyCode() == 18) {
                System.out.println("Alt");
            } else if (ke.getKeyCode() == 90) {
                System.out.println("'Z'");
            } else if (ke.getKeyCode() == 88) {
                System.out.println("'X'");
            }
        }
    }

    public void keyReleased(KeyEvent ke) {
    }

    public void keyTyped(KeyEvent ke) {
    }
}
