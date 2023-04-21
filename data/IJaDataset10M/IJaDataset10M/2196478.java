package impl.game.grid;

import framework.Business;
import framework.Game;
import framework.GameRenderer;
import framework.Person;
import java.awt.*;

/**
 * Date: 11.04.2008
 * Time: 17:00:31
 *
 * @author Denis DIR Rozhnev
 */
public class GridRenderer implements GameRenderer {

    private Color COOPERATE = Color.BLUE;

    private Color DEFECT = Color.RED;

    private Color D2C = Color.YELLOW;

    private Color C2D = Color.GREEN;

    private int cellSize = 10;

    private GridGame game;

    /** todo replace array by object */
    private Polygon[][][] polygons;

    private Player[] players;

    private boolean gameTuned = true;

    public GridRenderer(GridGame game, int cellSize, int playerSize) {
        this.cellSize = cellSize;
        Player.SIZE = playerSize;
        setGame(game);
    }

    public void setGame(GridGame game) {
        this.game = game;
        game.addListener(new Game.Listener() {

            public void tuned(int turnCount) {
                gameTuned = true;
            }
        });
        gameTuned = true;
        generatePolygonalGrid();
    }

    public void setCellSize(int cellSize, int playerSize) {
        Player.SIZE = playerSize;
        this.cellSize = cellSize;
        generatePolygonalGrid();
    }

    public void setDealsColors(Color c, Color d, Color c2d, Color d2c) {
        COOPERATE = c;
        DEFECT = d;
        D2C = d2c;
        C2D = c2d;
    }

    private void generatePolygonalGrid() {
        final int[][][] turn = game.getTurnDeals();
        final int playersCount = turn.length;
        if (playersCount == 0) throw new IllegalArgumentException("Game don't have players.");
        final int dealsCount = turn[0].length;
        if (dealsCount != 2) throw new IllegalArgumentException("Suppotred 2 dealsCount, but received " + dealsCount);
        final int[][] Q = game.getQ();
        final int resultsCount = turn[0][0].length;
        if (resultsCount != 2) throw new IllegalArgumentException("Suppotred 2 resultsCount, but received " + resultsCount);
        final Polygon[][][] result = new Polygon[playersCount][dealsCount][resultsCount];
        final Player[] resPlayers = new Player[playersCount];
        final int C2 = cellSize / 2;
        final int mx = (game.getMX()) * cellSize + C2;
        final int my = (game.getMY()) * cellSize + C2;
        for (int i = 0, x = 0, y = 0; i < playersCount; i++, x++) {
            if (x == game.getMX()) {
                y++;
                x = 0;
            }
            final Polygon[][] playerP = result[i];
            final int x0 = (x) * cellSize + C2;
            final int y0 = (y) * cellSize + C2;
            if (resPlayers[i] == null) {
                resPlayers[i] = new Player(x0, y0, game.getPerson(i).getShortName());
            } else resPlayers[i].setLocation(x0, y0);
            for (int j = 0; j < playerP.length; j++) {
                final Polygon[] polys = playerP[j];
                final int[] Q0 = Q[j];
                polys[0] = createPolygon(x0, y0, true, Q0);
                polys[1] = createPolygon((x == 0 && Q0[1] == -1) ? mx : x0, (y == 0 && Q0[0] == -1) ? my : y0, false, Q0);
            }
        }
        polygons = result;
        players = resPlayers;
    }

    private Polygon createPolygon(int x0, int y0, boolean near, int[] q0) {
        final int C = cellSize;
        final int C2 = cellSize / 2;
        int[] xx, yy;
        if (q0[0] == -1 && q0[1] == 0) {
            xx = new int[] { x0, x0 - C2, x0 + C2 };
            yy = new int[] { near ? y0 : (y0 - C), y0 - C2, y0 - C2 };
        } else if (q0[0] == 0 && q0[1] == -1) {
            xx = new int[] { near ? x0 : (x0 - C), x0 - C2, x0 - C2 };
            yy = new int[] { y0, y0 - C2, y0 + C2 };
        } else {
            throw new IllegalArgumentException("Suppotred (-1,0) and (0,-1) Q, but received " + q0);
        }
        return new Polygon(xx, yy, xx.length);
    }

    public void render(Graphics g) {
        if (gameTuned) updateScores();
        final int[][][] turn = game.getTurnDeals();
        final Business biz = game.getBiz();
        for (int i = 0; i < turn.length; i++) {
            int[][] t1 = turn[i];
            Polygon[][] p1 = polygons[i];
            for (int j = 0; j < t1.length; j++) {
                int[] t2 = biz.getDealsByScore(t1[j]);
                Polygon[] p2 = p1[j];
                Color c0, c1;
                if (t2[0] > 0) {
                    if (t2[1] > 0) c0 = c1 = COOPERATE; else {
                        c0 = C2D;
                        c1 = D2C;
                    }
                } else {
                    if (t2[1] > 0) {
                        c0 = D2C;
                        c1 = C2D;
                    } else c0 = c1 = DEFECT;
                }
                g.setColor(c0);
                g.fillPolygon(p2[0]);
                g.setColor(c1);
                g.fillPolygon(p2[1]);
            }
        }
        for (Player player : players) {
            player.draw(g);
        }
    }

    public void setPlayerShortName(int n, String s) {
        players[n].setName(s);
    }

    public boolean setPlayer(Point p, String className) {
        int n = getPlayerIndex(p);
        if (n >= 0) {
            try {
                Class klass = Class.forName(className);
                Person person = (Person) klass.newInstance();
                game.setPerson(n, person);
                players[n].setName(person.getShortName());
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
        return true;
    }

    public int getPlayerIndex(Point p) {
        for (int i = 0; i < players.length; i++) {
            Player player = players[i];
            if (player.contains(p)) return i;
        }
        return -1;
    }

    public void updateScores() {
        int[] scores = game.getScores();
        int max = 0, min = 0;
        for (int i = 0; i < scores.length; i++) {
            int score = scores[i];
            players[i].setScore(score);
            if (score > max) max = score;
            if (score < min) min = score;
        }
        Player.setScoreRange(min, max);
        gameTuned = false;
    }
}
