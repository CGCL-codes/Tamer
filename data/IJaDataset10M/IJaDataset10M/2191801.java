package pong.main;

import pong.player.*;
import pong.networking.*;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.event.KeyEvent;
import java.awt.geom.Line2D;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 * Controls the Ball and the 2 Players and draws everything.
 * 100 x 50 grid (x10 pixels/unit)
 * @author nikhil,keshav
 */
public class Game extends JComponent implements Runnable {

    public static final String INVALID_PORT_ERROR = "Not a valid port";

    public static final int SLEEP_TIME = 60;

    public boolean paused = false;

    public boolean gameOver = false;

    Thread thread;

    private Player player1, player2;

    private String titleText = "Network  Pong";

    private String pauseText = "Press 'p' to continue";

    private String pauseText2 = "Press 'p' to pause";

    private String contrastText = "Press 'c' for contrast";

    private boolean isFieldContrast;

    private Ball ball;

    int ballVelocity = 10;

    public boolean ballMovingUp = false;

    public boolean ballMovingRight = false;

    public boolean ballInPlay = false;

    public boolean delayBall = true;

    private static String ip;

    private int port = 6565;

    private boolean networking = false;

    private boolean host = false;

    private Socket socket = null;

    private ServerSocket server = null;

    private PrintWriter out;

    private BufferedReader in;

    private String inputLine, outputLine;

    private static boolean waiting;

    public Game(final int gameopt, final String ip) {
        Runnable r1 = new Runnable() {

            public void run() {
                if (gameopt == 0) {
                    Game.waiting = true;
                    networking = true;
                    host = true;
                    try {
                        server = new ServerSocket(port);
                    } catch (IOException e) {
                        System.out.println("Could not listen on port: " + port);
                        System.exit(-1);
                    }
                    try {
                        socket = server.accept();
                    } catch (IOException e) {
                        System.out.println("Accept failed: " + port);
                        System.exit(-1);
                    }
                    System.out.println("here");
                } else if (gameopt == 1) {
                    Game.ip = ip;
                    networking = true;
                    host = false;
                    try {
                        socket = new Socket(ip, port);
                    } catch (UnknownHostException e) {
                        System.out.println("Could not find host!");
                        System.exit(-1);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (networking) {
                    try {
                        out = new PrintWriter(socket.getOutputStream(), true);
                        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                Game.waiting = false;
            }
        };
        Thread thr1 = new Thread(r1);
        thr1.start();
        player1 = new Player(0, 100, Player.LEFT_SIDE);
        player2 = new Player(0, 100, Player.RIGHT_SIDE);
        ball = new Ball(320, 200);
        thread = new Thread(this);
        thread.start();
    }

    public void run() {
        while (true) {
            if (waiting) {
                repaint();
            } else {
                try {
                    Thread.sleep(SLEEP_TIME);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (networking) {
                    updateNetwork();
                }
                if (!paused) {
                    player1.run();
                    player2.run();
                    if (delayBall) {
                        try {
                            Thread.sleep(1000);
                            ballInPlay = true;
                        } catch (InterruptedException e) {
                        }
                    } else {
                        ballInPlay = true;
                    }
                    moveBall();
                    repaint();
                }
            }
        }
    }

    private int booltoint(boolean b) {
        if (b) {
            return 1;
        } else {
            return 0;
        }
    }

    private boolean inttobool(int i) {
        if (i == 0) {
            return false;
        } else {
            return true;
        }
    }

    private void updateNetwork() {
        if (host == true) {
            int[] n = new int[9];
            n[0] = player1.getPosition();
            n[1] = player2.getPosition();
            n[2] = player1.getScore();
            n[3] = player2.getScore();
            n[4] = ball.getX();
            n[5] = ball.getY();
            n[6] = booltoint(gameOver);
            n[7] = booltoint(paused);
            n[8] = booltoint(isFieldContrast);
            out.println(java.util.Arrays.toString(n));
        } else if (host == false) {
            try {
                if (in.readLine() != null) inputLine = in.readLine();
                String[] s = new String[10];
                s = inputLine.split(",");
                s[0] = s[0].replace("[", "");
                s[8] = s[8].replace("]", "");
                player1.setPosition(Integer.parseInt(s[0].trim()));
                player2.setPosition(Integer.parseInt(s[1].trim()));
                player1.setScore(Integer.parseInt(s[2].trim()));
                player2.setScore(Integer.parseInt(s[3].trim()));
                ball.setX(Integer.parseInt(s[4].trim()));
                ball.setX(Integer.parseInt(s[5].trim()));
                isFieldContrast = inttobool(Integer.parseInt(s[8].trim()));
            } catch (IOException e) {
                JOptionPane.showMessageDialog(new JFrame(), "Socket error occued!");
                System.exit(-1);
                e.printStackTrace();
            }
        }
    }

    private void drawPause(Graphics g) {
        if (paused) {
            g.drawString(pauseText, 10, getHeight() - 10);
        } else {
            g.drawString(pauseText2, 10, getHeight() - 10);
        }
    }

    private void drawWait(Graphics g) {
        g.drawString("Waiting for player", 50, 110);
    }

    private void drawLabels(Graphics g) {
        Stroke drawingStroke = new BasicStroke(3, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[] { 9 }, 0);
        Line2D line = new Line2D.Double(getWidth() / 2, 30, getWidth() / 2, getHeight());
        Graphics2D g2d = (Graphics2D) g;
        g2d.setStroke(drawingStroke);
        g2d.draw(line);
        FontMetrics fm = g.getFontMetrics();
        java.awt.geom.Rectangle2D rect2 = fm.getStringBounds(contrastText, g);
        int textWidth = (int) (rect2.getWidth());
        g.drawString(contrastText, getWidth() - textWidth - 10, getHeight() - 10);
        java.awt.geom.Rectangle2D rect = fm.getStringBounds(titleText, g);
        textWidth = (int) (rect.getWidth());
        int panelWidth = this.getWidth();
        int x = (panelWidth - textWidth) / 2;
        g.drawString(titleText, x, 20);
    }

    private void drawScores(Graphics g) {
        Font scoreFont = new Font("Arial", Font.PLAIN, 22);
        g.setFont(scoreFont);
        int player1score = player1.getScore();
        int player2score = player2.getScore();
        String p1score = Integer.toString(player1score);
        String p2score = Integer.toString(player2score);
        FontMetrics fm = g.getFontMetrics();
        java.awt.geom.Rectangle2D rect = fm.getStringBounds(p1score, g);
        int textWidth = (int) (rect.getWidth());
        int x = (this.getWidth() - textWidth) / 2;
        g.drawString(p1score, x - 30, 50);
        g.drawString(p2score, x + 30, 50);
    }

    private void moveBall() {
        int xpos = ball.getX();
        int ypos = ball.getY();
        int p1pos = player1.getPosition();
        int p2pos = player2.getPosition();
        if (ballInPlay) {
            delayBall = false;
            if (ballMovingUp) {
                if (ypos < 10) {
                    ballMovingUp = false;
                } else {
                    ball.setY(ypos - ballVelocity);
                }
            } else {
                if (ypos > getHeight() - 10) {
                    ballMovingUp = true;
                } else {
                    ball.setY(ypos + ballVelocity);
                }
            }
            if (ballMovingRight) {
                if (xpos == Player.RIGHT_POSITION && ypos >= p2pos - (Player.PLAYER_HEIGHT / 2) && ypos <= p2pos + (Player.PLAYER_HEIGHT / 2)) {
                    ballMovingRight = false;
                } else if (xpos > getWidth() - Player.PLAYER_WIDTH) {
                    player1.incrementScore();
                    ball.reset();
                    delayBall = true;
                    repaint();
                } else if (ballMovingRight) {
                    ball.setX(xpos + ballVelocity);
                }
            } else {
                if (xpos == Player.LEFT_POSITION + Player.PLAYER_WIDTH && ypos >= p1pos - (Player.PLAYER_HEIGHT / 2) && ypos <= p1pos + (Player.PLAYER_HEIGHT / 2)) {
                    ballMovingRight = true;
                } else if (xpos < Player.PLAYER_WIDTH) {
                    player2.incrementScore();
                    ball.reset();
                    delayBall = true;
                } else {
                    ball.setX(xpos - ballVelocity);
                }
            }
        }
    }

    private void setContrast(boolean contrast) {
        player1.setContrast(contrast);
        player2.setContrast(contrast);
        ball.setContrast(contrast);
        isFieldContrast = contrast;
        if (isFieldContrast) {
            Display.gameFrame.getContentPane().setBackground(Color.BLACK);
        } else {
            Display.gameFrame.getContentPane().setBackground(Color.WHITE);
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        if (waiting) {
            drawWait(g);
        } else {
            player1.draw(g);
            player2.draw(g);
            ball.draw(g);
            drawPause(g);
            drawLabels(g);
            drawScores(g);
        }
    }

    public void keyPressed(KeyEvent e) {
        switch(e.getKeyCode()) {
            case KeyEvent.VK_P:
                paused = !paused;
                repaint();
                break;
            case KeyEvent.VK_C:
                setContrast(!isFieldContrast);
                break;
        }
        if (networking == false || host == false) {
            switch(e.getKeyCode()) {
                case KeyEvent.VK_UP:
                    player2.setUpKeyPressed(true);
                    repaint();
                    break;
                case KeyEvent.VK_DOWN:
                    player2.setDownKeyPressed(true);
                    repaint();
                    break;
            }
        }
        if (networking == false || host == true) {
            switch(e.getKeyCode()) {
                case KeyEvent.VK_W:
                    player1.setUpKeyPressed(true);
                    repaint();
                    break;
                case KeyEvent.VK_S:
                    player1.setDownKeyPressed(true);
                    repaint();
                    break;
            }
        }
    }

    public void keyReleased(KeyEvent e) {
        if (networking == false || host == true) {
            switch(e.getKeyCode()) {
                case KeyEvent.VK_W:
                    player1.setUpKeyPressed(false);
                    repaint();
                    break;
                case KeyEvent.VK_S:
                    player1.setDownKeyPressed(false);
                    repaint();
                    break;
            }
        }
        if (networking == false || host == false) {
            switch(e.getKeyCode()) {
                case KeyEvent.VK_UP:
                    player2.setUpKeyPressed(false);
                    repaint();
                    break;
                case KeyEvent.VK_DOWN:
                    player2.setDownKeyPressed(false);
                    repaint();
                    break;
            }
        }
    }
}
