package TournamentManager;

import java.awt.*;
import java.util.LinkedList;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * Class: BracketPanel
 * 
 * Description: The BracketPanel manages the layout of the bracket design.
 * 
 * @author Matt Stephen
 */
public class BracketPanel extends JPanel {

    private int elimination;

    private int players;

    private Dimension size;

    private LinkedList<Player> playerList;

    private LinkedList<Game> winnerGames;

    private LinkedList<Game> loserGames;

    private int numBrackets;

    private int numRounds;

    private int numTotalRounds;

    private int heightSpacer;

    private int widthSpacer;

    private int bracketNumber = 0;

    public BracketPanel(int elimination, int players, Dimension size, LinkedList<Player> playerList, LinkedList<Game> winnerGames, LinkedList<Game> loserGames) {
        this.elimination = elimination;
        this.players = players;
        this.size = size;
        this.playerList = playerList;
        this.winnerGames = winnerGames;
        this.loserGames = loserGames;
        this.setLayout(null);
        setOpaque(true);
        setup();
    }

    public void switchToJLabel(Game game) {
        for (int i = 0; i < winnerGames.size(); i++) {
            if (game == winnerGames.get(i)) {
                Rectangle bounds = winnerGames.get(i).getBounds();
                this.remove(winnerGames.get(i));
                JLabel label = new JLabel(winnerGames.get(i).getWinner().getName());
                this.add(label);
                label.setBounds(bounds);
                if (winnerGames.size() == 1) {
                    Rectangle loserBounds;
                    JLabel label2 = new JLabel(winnerGames.get(i).getLoser().getName());
                    this.add(label2);
                    loserBounds = new Rectangle(winnerGames.get(i).getBounds().x - 2 * widthSpacer, bounds.y, (int) bounds.getWidth(), (int) bounds.getHeight());
                    label2.setBounds(loserBounds);
                } else if (winnerGames.size() == 3) {
                    Rectangle loserBounds;
                    JLabel label2 = new JLabel(winnerGames.get(i).getLoser().getName());
                    this.add(label2);
                    if (i < 2) {
                        loserBounds = new Rectangle(winnerGames.get(i).getBounds().x - 2 * widthSpacer, bounds.y, (int) bounds.getWidth(), (int) bounds.getHeight());
                    } else {
                        loserBounds = new Rectangle(winnerGames.get(i).getBounds().x - 4 * widthSpacer, bounds.y - 2 * heightSpacer, (int) bounds.getWidth(), (int) bounds.getHeight());
                    }
                    label2.setBounds(loserBounds);
                } else if (winnerGames.size() == 7) {
                    Rectangle loserBounds;
                    JLabel label2 = new JLabel(winnerGames.get(i).getLoser().getName());
                    this.add(label2);
                    if (i < 4) {
                        loserBounds = new Rectangle(winnerGames.get(i).getBounds().x - 2 * widthSpacer, bounds.y, (int) bounds.getWidth(), (int) bounds.getHeight());
                    } else if (i == 4) {
                        loserBounds = new Rectangle(winnerGames.get(i).getBounds().x - 4 * widthSpacer, bounds.y + 6 * heightSpacer, (int) bounds.getWidth(), (int) bounds.getHeight());
                    } else if (i == 5) {
                        loserBounds = new Rectangle(winnerGames.get(i).getBounds().x - 4 * widthSpacer, bounds.y - 6 * heightSpacer, (int) bounds.getWidth(), (int) bounds.getHeight());
                    } else {
                        loserBounds = new Rectangle(winnerGames.get(i).getBounds().x - 7 * widthSpacer, bounds.y - 2 * heightSpacer, (int) bounds.getWidth(), (int) bounds.getHeight());
                    }
                    label2.setBounds(loserBounds);
                }
                if (winnerGames.size() == 15) {
                    Rectangle loserBounds;
                    JLabel label2 = new JLabel(winnerGames.get(i).getLoser().getName());
                    this.add(label2);
                    if (i < 8) {
                        loserBounds = new Rectangle(winnerGames.get(i).getBounds().x - 2 * widthSpacer, bounds.y, (int) bounds.getWidth(), (int) bounds.getHeight());
                    } else if (i == 8) {
                        loserBounds = new Rectangle(winnerGames.get(i).getBounds().x - 4 * widthSpacer, bounds.y + 14 * heightSpacer, (int) bounds.getWidth(), (int) bounds.getHeight());
                    } else if (i == 9) {
                        loserBounds = new Rectangle(winnerGames.get(i).getBounds().x - 4 * widthSpacer, bounds.y + 6 * heightSpacer, (int) bounds.getWidth(), (int) bounds.getHeight());
                    } else if (i == 10) {
                        loserBounds = new Rectangle(winnerGames.get(i).getBounds().x - 4 * widthSpacer, bounds.y - (int) (5.5 * heightSpacer), (int) bounds.getWidth(), (int) bounds.getHeight());
                    } else if (i == 11) {
                        loserBounds = new Rectangle(winnerGames.get(i).getBounds().x - 4 * widthSpacer, bounds.y - 14 * heightSpacer, (int) bounds.getWidth(), (int) bounds.getHeight());
                    } else if (i == 12) {
                        loserBounds = new Rectangle(winnerGames.get(i).getBounds().x - 7 * widthSpacer, bounds.y - 3 * heightSpacer, (int) bounds.getWidth(), (int) bounds.getHeight());
                    } else if (i == 13) {
                        loserBounds = new Rectangle(winnerGames.get(i).getBounds().x - 7 * widthSpacer, bounds.y + 3 * heightSpacer, (int) bounds.getWidth(), (int) bounds.getHeight());
                    } else {
                        loserBounds = new Rectangle(winnerGames.get(i).getBounds().x - 10 * widthSpacer, bounds.y - 2 * heightSpacer, (int) bounds.getWidth(), (int) bounds.getHeight());
                    }
                    label2.setBounds(loserBounds);
                } else if (winnerGames.size() == 31) {
                    Rectangle loserBounds;
                    JLabel label2 = new JLabel(winnerGames.get(i).getLoser().getName());
                    this.add(label2);
                    if (i < 16) {
                        loserBounds = new Rectangle(winnerGames.get(i).getBounds().x - 2 * widthSpacer, bounds.y, (int) bounds.getWidth(), (int) bounds.getHeight());
                    } else if (i == 16) {
                        loserBounds = new Rectangle(winnerGames.get(i).getBounds().x - 4 * widthSpacer, bounds.y + 30 * heightSpacer, (int) bounds.getWidth(), (int) bounds.getHeight());
                    } else if (i == 17) {
                        loserBounds = new Rectangle(winnerGames.get(i).getBounds().x - 4 * widthSpacer, bounds.y + 22 * heightSpacer, (int) bounds.getWidth(), (int) bounds.getHeight());
                    } else if (i == 18) {
                        loserBounds = new Rectangle(winnerGames.get(i).getBounds().x - 4 * widthSpacer, bounds.y + 14 * heightSpacer, (int) bounds.getWidth(), (int) bounds.getHeight());
                    } else if (i == 19) {
                        loserBounds = new Rectangle(winnerGames.get(i).getBounds().x - 4 * widthSpacer, bounds.y + 6 * heightSpacer, (int) bounds.getWidth(), (int) bounds.getHeight());
                    } else if (i == 20) {
                        loserBounds = new Rectangle(winnerGames.get(i).getBounds().x - 4 * widthSpacer, bounds.y - 6 * heightSpacer, (int) bounds.getWidth(), (int) bounds.getHeight());
                    } else if (i == 21) {
                        loserBounds = new Rectangle(winnerGames.get(i).getBounds().x - 4 * widthSpacer, bounds.y - 14 * heightSpacer, (int) bounds.getWidth(), (int) bounds.getHeight());
                    } else if (i == 22) {
                        loserBounds = new Rectangle(winnerGames.get(i).getBounds().x - 4 * widthSpacer, bounds.y - 22 * heightSpacer, (int) bounds.getWidth(), (int) bounds.getHeight());
                    } else if (i == 23) {
                        loserBounds = new Rectangle(winnerGames.get(i).getBounds().x - 4 * widthSpacer, bounds.y - 30 * heightSpacer, (int) bounds.getWidth(), (int) bounds.getHeight());
                    } else if (i == 24) {
                        loserBounds = new Rectangle(winnerGames.get(i).getBounds().x - 7 * widthSpacer, bounds.y - 3 * heightSpacer, (int) bounds.getWidth(), (int) bounds.getHeight());
                    } else if (i == 25) {
                        loserBounds = new Rectangle(winnerGames.get(i).getBounds().x - 7 * widthSpacer, bounds.y - 3 * heightSpacer, (int) bounds.getWidth(), (int) bounds.getHeight());
                    } else if (i == 26) {
                        loserBounds = new Rectangle(winnerGames.get(i).getBounds().x - 7 * widthSpacer, bounds.y + 3 * heightSpacer, (int) bounds.getWidth(), (int) bounds.getHeight());
                    } else if (i == 27) {
                        loserBounds = new Rectangle(winnerGames.get(i).getBounds().x - 7 * widthSpacer, bounds.y + 3 * heightSpacer, (int) bounds.getWidth(), (int) bounds.getHeight());
                    } else if (i == 28) {
                        loserBounds = new Rectangle(winnerGames.get(i).getBounds().x - 10 * widthSpacer, bounds.y + 20 * heightSpacer, (int) bounds.getWidth(), (int) bounds.getHeight());
                    } else if (i == 29) {
                        loserBounds = new Rectangle(winnerGames.get(i).getBounds().x - 10 * widthSpacer, bounds.y - 20 * heightSpacer, (int) bounds.getWidth(), (int) bounds.getHeight());
                    } else {
                        loserBounds = new Rectangle(winnerGames.get(i).getBounds().x - 13 * widthSpacer, bounds.y - 2 * heightSpacer, (int) bounds.getWidth(), (int) bounds.getHeight());
                    }
                    label2.setBounds(loserBounds);
                }
                return;
            }
        }
        if (!loserGames.isEmpty()) {
            for (int i = 0; i < loserGames.size(); i++) {
                if (game == loserGames.get(i)) {
                    Rectangle bounds = loserGames.get(i).getBounds();
                    this.remove(loserGames.get(i));
                    JLabel label = new JLabel(loserGames.get(i).getWinner().getName());
                    this.add(label);
                    label.setBounds(bounds);
                    label.revalidate();
                    return;
                }
            }
        }
    }

    private void setup() {
        if (elimination == 1 || elimination == 2) {
            for (int i = 0; true; i++) {
                if (players <= Math.pow(2.0, i)) {
                    numBrackets = (int) Math.pow(2.0, i);
                    numRounds = i;
                    break;
                }
            }
            heightSpacer = size.height / (numBrackets + 1);
            for (int i = 0; i < winnerGames.size(); i++) {
                this.add(winnerGames.get(i));
            }
            if (elimination == 1) {
                numTotalRounds = numRounds;
                widthSpacer = size.width / (numRounds + 1);
            } else {
                numTotalRounds = 1 + 3 * (numRounds - 1);
                widthSpacer = size.width / (numTotalRounds + 2);
                for (int i = 0; i < loserGames.size(); i++) {
                    this.add(loserGames.get(i));
                }
            }
        } else {
        }
    }

    private LinkedList<Point> drawFirstRound(Graphics g, boolean doubleElimination) {
        LinkedList<Point> points = new LinkedList<Point>();
        if (doubleElimination) {
            for (int i = 0; i < numBrackets; i++) {
                g.drawLine((numTotalRounds - numRounds + 1) * widthSpacer, (i + 1) * heightSpacer, (numTotalRounds - numRounds + 2) * widthSpacer, (i + 1) * heightSpacer);
                points.add(new Point((numTotalRounds - numRounds + 2) * widthSpacer, (i + 1) * heightSpacer));
            }
        } else {
            for (int i = 0; i < numBrackets; i++) {
                g.drawLine(0, (i + 1) * heightSpacer, widthSpacer, (i + 1) * heightSpacer);
                points.add(new Point(widthSpacer, (i + 1) * heightSpacer));
            }
        }
        if (numBrackets == 2) {
            g.drawString(playerList.get(0).getName(), points.get(0).x - widthSpacer, points.get(0).y - 5);
            g.drawString(playerList.get(1).getName(), points.get(1).x - widthSpacer, points.get(1).y - 5);
        }
        if (numBrackets == 4) {
            g.drawString(playerList.get(0).getName(), points.get(0).x - widthSpacer, points.get(0).y - 5);
            g.drawString(playerList.get(1).getName(), points.get(3).x - widthSpacer, points.get(3).y - 5);
            g.drawString(playerList.get(2).getName(), points.get(2).x - widthSpacer, points.get(2).y - 5);
            g.drawString(playerList.get(3).getName(), points.get(1).x - widthSpacer, points.get(1).y - 5);
        } else if (numBrackets == 8) {
            g.drawString(playerList.get(0).getName(), points.get(0).x - widthSpacer, points.get(0).y - 5);
            g.drawString(playerList.get(1).getName(), points.get(7).x - widthSpacer, points.get(7).y - 5);
            g.drawString(playerList.get(2).getName(), points.get(4).x - widthSpacer, points.get(4).y - 5);
            g.drawString(playerList.get(3).getName(), points.get(3).x - widthSpacer, points.get(3).y - 5);
            g.drawString(playerList.get(4).getName(), points.get(2).x - widthSpacer, points.get(2).y - 5);
            g.drawString(playerList.get(5).getName(), points.get(5).x - widthSpacer, points.get(5).y - 5);
            g.drawString(playerList.get(6).getName(), points.get(6).x - widthSpacer, points.get(6).y - 5);
            g.drawString(playerList.get(7).getName(), points.get(1).x - widthSpacer, points.get(1).y - 5);
        } else if (numBrackets == 16) {
            g.drawString(playerList.get(0).getName(), points.get(0).x - widthSpacer, points.get(0).y - 3);
            g.drawString(playerList.get(1).getName(), points.get(15).x - widthSpacer, points.get(15).y - 3);
            g.drawString(playerList.get(2).getName(), points.get(8).x - widthSpacer, points.get(8).y - 3);
            g.drawString(playerList.get(3).getName(), points.get(7).x - widthSpacer, points.get(7).y - 3);
            g.drawString(playerList.get(4).getName(), points.get(4).x - widthSpacer, points.get(4).y - 3);
            g.drawString(playerList.get(5).getName(), points.get(11).x - widthSpacer, points.get(11).y - 3);
            g.drawString(playerList.get(6).getName(), points.get(12).x - widthSpacer, points.get(12).y - 3);
            g.drawString(playerList.get(7).getName(), points.get(3).x - widthSpacer, points.get(3).y - 3);
            g.drawString(playerList.get(8).getName(), points.get(2).x - widthSpacer, points.get(2).y - 3);
            g.drawString(playerList.get(9).getName(), points.get(13).x - widthSpacer, points.get(13).y - 3);
            g.drawString(playerList.get(10).getName(), points.get(10).x - widthSpacer, points.get(10).y - 3);
            g.drawString(playerList.get(11).getName(), points.get(5).x - widthSpacer, points.get(5).y - 3);
            g.drawString(playerList.get(12).getName(), points.get(6).x - widthSpacer, points.get(6).y - 3);
            g.drawString(playerList.get(13).getName(), points.get(9).x - widthSpacer, points.get(9).y - 3);
            g.drawString(playerList.get(14).getName(), points.get(14).x - widthSpacer, points.get(14).y - 3);
            g.drawString(playerList.get(15).getName(), points.get(1).x - widthSpacer, points.get(1).y - 3);
        } else if (numBrackets == 32) {
            g.drawString(playerList.get(0).getName(), points.get(0).x - widthSpacer, points.get(0).y - 2);
            g.drawString(playerList.get(1).getName(), points.get(31).x - widthSpacer, points.get(31).y - 2);
            g.drawString(playerList.get(2).getName(), points.get(16).x - widthSpacer, points.get(16).y - 2);
            g.drawString(playerList.get(3).getName(), points.get(15).x - widthSpacer, points.get(15).y - 2);
            g.drawString(playerList.get(4).getName(), points.get(8).x - widthSpacer, points.get(8).y - 2);
            g.drawString(playerList.get(5).getName(), points.get(23).x - widthSpacer, points.get(23).y - 2);
            g.drawString(playerList.get(6).getName(), points.get(24).x - widthSpacer, points.get(24).y - 2);
            g.drawString(playerList.get(7).getName(), points.get(7).x - widthSpacer, points.get(7).y - 2);
            g.drawString(playerList.get(8).getName(), points.get(4).x - widthSpacer, points.get(4).y - 2);
            g.drawString(playerList.get(9).getName(), points.get(27).x - widthSpacer, points.get(27).y - 2);
            g.drawString(playerList.get(10).getName(), points.get(20).x - widthSpacer, points.get(20).y - 2);
            g.drawString(playerList.get(11).getName(), points.get(11).x - widthSpacer, points.get(11).y - 2);
            g.drawString(playerList.get(12).getName(), points.get(12).x - widthSpacer, points.get(12).y - 2);
            g.drawString(playerList.get(13).getName(), points.get(19).x - widthSpacer, points.get(19).y - 2);
            g.drawString(playerList.get(14).getName(), points.get(28).x - widthSpacer, points.get(28).y - 2);
            g.drawString(playerList.get(15).getName(), points.get(3).x - widthSpacer, points.get(3).y - 2);
            g.drawString(playerList.get(16).getName(), points.get(2).x - widthSpacer, points.get(2).y - 2);
            g.drawString(playerList.get(17).getName(), points.get(29).x - widthSpacer, points.get(29).y - 2);
            g.drawString(playerList.get(18).getName(), points.get(18).x - widthSpacer, points.get(18).y - 2);
            g.drawString(playerList.get(19).getName(), points.get(13).x - widthSpacer, points.get(13).y - 2);
            g.drawString(playerList.get(20).getName(), points.get(10).x - widthSpacer, points.get(10).y - 2);
            g.drawString(playerList.get(21).getName(), points.get(21).x - widthSpacer, points.get(21).y - 2);
            g.drawString(playerList.get(22).getName(), points.get(26).x - widthSpacer, points.get(26).y - 2);
            g.drawString(playerList.get(23).getName(), points.get(5).x - widthSpacer, points.get(5).y - 2);
            g.drawString(playerList.get(24).getName(), points.get(6).x - widthSpacer, points.get(6).y - 2);
            g.drawString(playerList.get(25).getName(), points.get(25).x - widthSpacer, points.get(25).y - 2);
            g.drawString(playerList.get(26).getName(), points.get(22).x - widthSpacer, points.get(22).y - 2);
            g.drawString(playerList.get(27).getName(), points.get(9).x - widthSpacer, points.get(9).y - 2);
            g.drawString(playerList.get(28).getName(), points.get(14).x - widthSpacer, points.get(14).y - 2);
            g.drawString(playerList.get(29).getName(), points.get(17).x - widthSpacer, points.get(17).y - 2);
            g.drawString(playerList.get(30).getName(), points.get(30).x - widthSpacer, points.get(30).y - 2);
            g.drawString(playerList.get(31).getName(), points.get(1).x - widthSpacer, points.get(1).y - 2);
        }
        return points;
    }

    private LinkedList<Point> getReverseFirstRound(Graphics g) {
        LinkedList<Point> previousPoints = new LinkedList<Point>();
        LinkedList<Point> points = new LinkedList<Point>();
        for (int i = 0; i < numBrackets; i++) {
            previousPoints.add(new Point((numTotalRounds - numRounds + 1) * widthSpacer, (i + 1) * heightSpacer));
        }
        Point a;
        Point b;
        for (int i = 0; i < numBrackets; i += 2) {
            a = previousPoints.get(i);
            b = previousPoints.get(i + 1);
            g.drawLine(a.x, a.y, b.x, b.y);
            Point startPoint = new Point(a.x, b.y + (a.y - b.y) / 2);
            Point endPoint = new Point(a.x - widthSpacer, startPoint.y);
            g.drawLine(startPoint.x, startPoint.y, endPoint.x, endPoint.y);
            points.add(endPoint);
        }
        return points;
    }

    private Point drawRound(Graphics g, Point a, Point b) {
        g.drawLine(a.x, a.y, b.x, b.y);
        Point startPoint = new Point(a.x, b.y + (a.y - b.y) / 2);
        Point endPoint = new Point(a.x + widthSpacer, startPoint.y);
        g.drawLine(startPoint.x, startPoint.y, endPoint.x, endPoint.y);
        winnerGames.get(bracketNumber).setBounds(startPoint.x, startPoint.y - 20, widthSpacer, 20);
        return endPoint;
    }

    private Point drawReverseRound(Graphics g, Point a, Point b) {
        g.drawLine(a.x, a.y, b.x, b.y);
        Point startPoint = new Point(a.x, b.y + (a.y - b.y) / 2);
        Point endPoint = new Point(a.x - widthSpacer, startPoint.y);
        g.drawLine(startPoint.x, startPoint.y, endPoint.x, endPoint.y);
        loserGames.get(bracketNumber).setBounds(endPoint.x, endPoint.y - 20, widthSpacer, 20);
        return endPoint;
    }

    private Point drawReverseNewRound(Graphics g, Point a, double position) {
        Point b;
        if (position >= .5) {
            b = new Point(a.x, a.y + 2 * heightSpacer);
            g.drawLine(b.x + 3 * widthSpacer / 4, b.y, a.x, b.y);
        } else {
            b = new Point(a.x, a.y - 2 * heightSpacer);
            g.drawLine(b.x + 3 * widthSpacer / 4, b.y, a.x, b.y);
        }
        g.drawLine(a.x, a.y, b.x, b.y);
        Point startPoint = new Point(a.x, b.y + (a.y - b.y) / 2);
        Point endPoint = new Point(a.x - widthSpacer, startPoint.y);
        g.drawLine(startPoint.x, startPoint.y, endPoint.x, endPoint.y);
        loserGames.get(bracketNumber).setBounds(endPoint.x, endPoint.y - 20, widthSpacer, 20);
        return endPoint;
    }

    public void paint(Graphics g) {
        super.paint(g);
        heightSpacer = this.size.height / (numBrackets + 1);
        if (elimination == 1) {
            widthSpacer = this.size.width / (numRounds + 1);
        } else if (elimination == 2) {
            widthSpacer = this.size.width / (numTotalRounds + 2);
        }
        bracketNumber = 0;
        if (elimination == 1) {
            LinkedList<Point> previousPoints = new LinkedList<Point>();
            LinkedList<Point> points = drawFirstRound(g, false);
            for (int i = 0; i < numRounds; i++) {
                previousPoints = (LinkedList<Point>) points.clone();
                points.clear();
                for (int j = 1; j < previousPoints.size(); j += 2) {
                    points.add(drawRound(g, previousPoints.get(j), previousPoints.get(j - 1)));
                    bracketNumber++;
                }
            }
        } else if (elimination == 2) {
            LinkedList<Point> previousPoints = new LinkedList<Point>();
            LinkedList<Point> points = drawFirstRound(g, true);
            for (int i = 0; i < numRounds; i++) {
                previousPoints = (LinkedList<Point>) points.clone();
                points.clear();
                for (int j = 1; j < previousPoints.size(); j += 2) {
                    points.add(drawRound(g, previousPoints.get(j), previousPoints.get(j - 1)));
                    bracketNumber++;
                }
            }
            points = getReverseFirstRound(g);
            bracketNumber = 0;
            for (int i = 0; i < (numTotalRounds - numRounds); i++) {
                previousPoints = (LinkedList<Point>) points.clone();
                points.clear();
                for (int j = 0; j < previousPoints.size(); j++) {
                    if (i % 2 == 0) {
                        points.add(drawReverseRound(g, previousPoints.get(j), previousPoints.get(j + 1)));
                        j++;
                        bracketNumber++;
                    } else {
                        points.add(drawReverseNewRound(g, previousPoints.get(j), (double) j / (double) previousPoints.size()));
                        bracketNumber++;
                    }
                }
            }
        } else {
        }
        for (int i = 0; i < winnerGames.size(); i++) {
            winnerGames.get(i).revalidate();
        }
        if (!loserGames.isEmpty()) {
            for (int i = 0; i < loserGames.size(); i++) {
                loserGames.get(i).revalidate();
            }
        }
    }
}
