package hp.game.othello;

import java.applet.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Othello extends JComponent {

    /**
	 * 
	 */
    private static final long serialVersionUID = -1853146966532657243L;

    static final boolean _DEBUG = false;

    private static final int _WIDTH = 50;

    private static final int _X = 11;

    private static final int _Y = 32;

    private static final int _WINDOW_WIDTH = 433;

    private static final int _WINDOW_HEIGHT = 472;

    private static final int _OFFSCREEN_WIDTH = 430;

    private static final int _OFFSCREEN_HEIGHT = 450;

    private static final int _HELPER_X = 350;

    private static final int _HELPER_Y = 6;

    private static final int _HELPER_WIDTH = 24;

    private static final int _HELPER_HEIGHT = 24;

    private static final int _CLOSER_X = 378;

    private static final int _CLOSER_Y = 6;

    private static final int _CLOSER_WIDTH = 24;

    private static final int _CLOSER_HEIGHT = 24;

    private static final int _NEWER_X = 290;

    private static final int _NEWER_Y = 6;

    private static final int _NEWER_WIDTH = 70;

    private static final int _NEWER_HEIGHT = 24;

    private static final Color _SCOREBG_COLOR = new Color(255, 255, 255, 180);

    private static final int _SCOREBG_X = 40;

    private static final int _SCOREBG_Y = 105;

    private static final int _SCOREBG_WIDTH = 340;

    private static final int _SCOREBG_HEIGHT = 185;

    private static final Font _SCORE_FONT = new Font("System Fixed", Font.BOLD, 100);

    private static final Color _SCORE_COLOR = new Color(255, 0, 0, 150);

    private static final int _SCORE_X = 70;

    private static final int _SCORE_Y = 200;

    private static final Font _WIN_FONT = new Font("Monospaced", Font.BOLD | Font.ITALIC, 30);

    private static final int _WIN_X = 70;

    private static final int _WIN_Y = 250;

    private static final int _WIN_PIC_X = 255;

    private static final int _WIN_PIC_Y = 215;

    private static final String _HELP_CMD = "EXPLORER.EXE";

    private static final String _HELP_URL = "http://www.othello-china.com/Rules.htm";

    Applet applet = null;

    private Computer ai = new Computer();

    private Square pla = new Square();

    private Square com = new Square();

    private int com_num = 0;

    private int pla_num = 0;

    private int com_step_x;

    private int com_step_y;

    private int pla_step_x;

    private int pla_step_y;

    private int oldx = -1;

    private int oldy = -1;

    private int over = 0;

    private boolean game_over = false;

    private Image board = null;

    private Image white_chess = null;

    private Image black_chess = null;

    private Image twhite_chess = null;

    private Image tblack_chess = null;

    private Image offscreen = null;

    private Image helper = null;

    private Image helper_over = null;

    private Image closer = null;

    private Image closer_over = null;

    private Image newer = null;

    private Image newer_over = null;

    public static void main(String[] args) {
        JFrame frm = new JFrame("+++ Othello +++");
        Othello othello = new Othello();
        frm.add(othello);
        frm.addWindowListener(new java.awt.event.WindowAdapter() {

            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
        frm.setSize(_WINDOW_WIDTH, _WINDOW_HEIGHT);
        frm.setResizable(false);
        frm.setLocationRelativeTo(null);
        frm.setVisible(true);
    }

    public Othello() {
        initComponent();
        newGame();
    }

    private void initComponent() {
        MediaTracker tracker = null;
        Toolkit toolkit = null;
        Class oclass = null;
        addMouseListener(new MouseAdapter() {

            public void mouseReleased(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1) {
                    onLeftButtonPress(e.getPoint());
                } else if (e.getButton() == MouseEvent.BUTTON3) {
                    onRightButtonPress(e.getPoint());
                } else if (e.getButton() == MouseEvent.BUTTON2) {
                }
            }
        });
        addMouseMotionListener(new MouseMotionAdapter() {

            public void mouseMoved(MouseEvent e) {
                onMouseMove(e.getPoint());
            }
        });
        tracker = new MediaTracker(this);
        toolkit = getToolkit();
        try {
            oclass = Class.forName("hp.game.othello.Othello");
            board = toolkit.getImage(oclass.getResource("board.png"));
            black_chess = toolkit.getImage(oclass.getResource("black.png"));
            white_chess = toolkit.getImage(oclass.getResource("white.png"));
            tblack_chess = toolkit.getImage(oclass.getResource("tblack.png"));
            twhite_chess = toolkit.getImage(oclass.getResource("twhite.png"));
            helper = toolkit.getImage(oclass.getResource("helper.png"));
            helper_over = toolkit.getImage(oclass.getResource("helper_over.png"));
            closer = toolkit.getImage(oclass.getResource("closer.png"));
            closer_over = toolkit.getImage(oclass.getResource("closer_over.png"));
            newer = toolkit.getImage(oclass.getResource("newer.png"));
            newer_over = toolkit.getImage(oclass.getResource("newer_over.png"));
            tracker.addImage(board, 1);
            tracker.addImage(black_chess, 2);
            tracker.addImage(white_chess, 3);
            tracker.addImage(tblack_chess, 4);
            tracker.addImage(twhite_chess, 5);
            tracker.addImage(helper, 6);
            tracker.addImage(helper_over, 7);
            tracker.addImage(closer, 8);
            tracker.addImage(closer_over, 9);
            tracker.addImage(newer, 10);
            tracker.addImage(newer_over, 11);
            tracker.waitForAll();
        } catch (Exception e) {
            e.printStackTrace();
        }
        offscreen = createImage(_OFFSCREEN_WIDTH, _OFFSCREEN_HEIGHT);
    }

    private void onRightButtonPress(Point p) {
        if (pass()) {
            trace("pass\n");
            update(getGraphics());
            if (isOver(true) && isOver(false)) {
                trace("over\n");
                sleep(1000);
                gameOver();
            }
        } else {
            trace("pass false\n");
            trace(this);
        }
    }

    private boolean pass() {
        int x1, y1;
        int step;
        int x = 0, y = 0;
        boolean sign = false;
        Square ret = new Square();
        if (!isOver(true)) {
            trace("player not over\n");
            return false;
        }
        ai.begin(com, pla, com_num, pla_num);
        x = ai.Sx;
        y = ai.Sy;
        ret.setSquare(ai.neighbor[x][y]);
        sign = false;
        if (ret.bitAnd(pla).isEmpty()) {
            return false;
        }
        for (int inx = -1; inx < 2; inx++) {
            for (int iny = -1; iny < 2; iny++) {
                if (inx == 0 && iny == 0) {
                    continue;
                }
                x1 = x + inx;
                y1 = iny + y;
                step = 0;
                while (pla.getChess(x1, y1)) {
                    x1 += inx;
                    y1 += iny;
                    step++;
                }
                if (step <= 0 || !com.getChess(x1, y1)) {
                    continue;
                }
                while (step > 0) {
                    sign = true;
                    x1 -= inx;
                    y1 -= iny;
                    pla.clearChess(x1, y1);
                    pla_num--;
                    com.setChess(x1, y1);
                    com_num++;
                    step--;
                }
            }
        }
        if (!sign) {
            return false;
        }
        com.setChess(x, y);
        com_num++;
        com_step_x = x;
        com_step_y = y;
        return true;
    }

    private boolean putChess(int x, int y) {
        int x1, y1;
        int change_num = 0;
        boolean sign = false;
        int step;
        if (x < 0 || x > 7) {
            return false;
        }
        if (y < 0 || y > 7) {
            return false;
        }
        if (pla.getChess(x, y) || com.getChess(x, y)) {
            return false;
        }
        Square ret = new Square(ai.neighbor[x][y]);
        if (ret.bitAnd(com).isEmpty()) {
            return false;
        }
        for (int inx = -1; inx < 2; inx++) {
            for (int iny = -1; iny < 2; iny++) {
                if (inx == 0 && iny == 0) {
                    continue;
                }
                x1 = x + inx;
                y1 = iny + y;
                step = 0;
                while (com.getChess(x1, y1)) {
                    x1 += inx;
                    y1 += iny;
                    step++;
                }
                if (step <= 0 || !pla.getChess(x1, y1)) {
                    continue;
                }
                while (step > 0) {
                    sign = true;
                    x1 -= inx;
                    y1 -= iny;
                    com.clearChess(x1, y1);
                    com_num--;
                    pla.setChess(x1, y1);
                    pla_num++;
                    step--;
                }
            }
        }
        if (sign) {
            pla.setChess(x, y);
            pla_num++;
            oldx = oldy = -1;
            trace(this);
            update(getGraphics());
            if (isOver(true) && isOver(false)) {
                trace("over\n");
                sleep(1000);
                gameOver();
                return true;
            }
        } else {
            return false;
        }
        ai.begin(com, pla, com_num, pla_num);
        x = ai.Sx;
        y = ai.Sy;
        ret.setSquare(ai.neighbor[x][y]);
        sign = false;
        if (ret.bitAnd(pla).isEmpty()) {
            return true;
        }
        for (int inx = -1; inx < 2; inx++) {
            for (int iny = -1; iny < 2; iny++) {
                if (inx == 0 && iny == 0) {
                    continue;
                }
                x1 = x + inx;
                y1 = iny + y;
                step = 0;
                while (pla.getChess(x1, y1)) {
                    x1 += inx;
                    y1 += iny;
                    step++;
                }
                if (step <= 0 || (!com.getChess(x1, y1))) {
                    continue;
                }
                while (step > 0) {
                    sign = true;
                    x1 -= inx;
                    y1 -= iny;
                    pla.clearChess(x1, y1);
                    pla_num--;
                    com.setChess(x1, y1);
                    com_num++;
                    step--;
                }
            }
        }
        if (sign) {
            com.setChess(x, y);
            com_num++;
            com_step_x = x;
            com_step_y = y;
            trace(this);
            sleep(200);
            update(getGraphics());
            if (isOver(true) && isOver(false)) {
                trace("over\n");
                sleep(1000);
                gameOver();
            }
        }
        return true;
    }

    private boolean testPutChess(int x, int y) {
        int x1, y1;
        int change_num = 0;
        int step;
        Square ret = new Square();
        if (x > 7 || x < 0) {
            return false;
        }
        if (y > 7 || y < 0) {
            return false;
        }
        if (pla.getChess(x, y) || com.getChess(x, y)) {
            return false;
        }
        ret.setSquare(ai.neighbor[x][y]);
        if (ret.bitAnd(com).isEmpty()) {
            return false;
        }
        for (int inx = -1; inx < 2; inx++) {
            for (int iny = -1; iny < 2; iny++) {
                if (inx == 0 && iny == 0) {
                    continue;
                }
                x1 = x + inx;
                y1 = iny + y;
                step = 0;
                while (com.getChess(x1, y1)) {
                    x1 += inx;
                    y1 += iny;
                    step++;
                }
                if (step <= 0 || !pla.getChess(x1, y1)) {
                    continue;
                }
                return true;
            }
        }
        return false;
    }

    private void onMouseMove(Point p) {
        int x = 0;
        int y = 0;
        if (p.y > _HELPER_Y && p.y < _HELPER_Y + _HELPER_HEIGHT) {
            if (p.x > _CLOSER_X && p.x < _CLOSER_X + _CLOSER_WIDTH) {
                if (over == 1) {
                    return;
                }
                over = 1;
                update(getGraphics());
                return;
            } else if (p.x > _HELPER_X && p.x < _HELPER_X + _HELPER_WIDTH) {
                if (over == 2) {
                    return;
                }
                over = 2;
                update(getGraphics());
                return;
            } else if (p.x > _NEWER_X && p.x < _NEWER_X + _NEWER_WIDTH) {
                if (over == 3) {
                    return;
                }
                over = 3;
                update(getGraphics());
                return;
            }
        }
        if (over != 0) {
            over = 0;
            update(getGraphics());
        }
        x = (p.x - _X) / _WIDTH;
        y = (p.y - _Y) / _WIDTH;
        if (x == oldx && y == oldy) {
            return;
        }
        if (!testPutChess(x, y)) {
            if (oldx == -1 || oldy == -1) {
                return;
            } else {
                oldx = oldy = -1;
            }
        } else {
            oldx = x;
            oldy = y;
        }
        update(getGraphics());
    }

    private void onLeftButtonPress(Point p) {
        if (p.y > _HELPER_Y && p.y < _HELPER_Y + _HELPER_HEIGHT) {
            if (p.x > _CLOSER_X && p.x < _CLOSER_X + _CLOSER_WIDTH) {
                System.exit(0);
                return;
            } else if (p.x > _HELPER_X && p.x < _HELPER_X + _HELPER_WIDTH) {
                doHelp();
                return;
            } else if (p.x > _NEWER_X && p.x < _NEWER_X + _NEWER_WIDTH) {
                newGame();
                update(getGraphics());
                return;
            }
        } else {
            int x = (p.x - _X) / _WIDTH;
            int y = (p.y - _Y) / _WIDTH;
            putChess(x, y);
        }
    }

    private void newGame() {
        com.setSquare(0);
        pla.setSquare(0);
        com.setChess(3, 4);
        com.setChess(4, 3);
        pla.setChess(3, 3);
        pla.setChess(4, 4);
        com_num = pla_num = 2;
        com_step_x = -1;
        com_step_y = -1;
        pla_step_x = -1;
        pla_step_y = -1;
        oldx = -1;
        oldy = -1;
        game_over = false;
    }

    private void drawBoard(Graphics g) {
        g.drawImage(board, 0, 0, this);
    }

    private void drawChess(Graphics g, Image chess, int x, int y) {
        g.drawImage(chess, x * _WIDTH + _X + 2, y * _WIDTH + _Y + 2, this);
    }

    private void drawGrid(Graphics g) {
        g.setColor(Color.yellow);
        for (int i = 0; i < 9; i++) {
            g.drawLine(_X, _Y + i * _WIDTH, _X + _WIDTH * 8, _Y + i * _WIDTH);
            g.drawLine(_X + i * _WIDTH, _Y, _X + i * _WIDTH, _Y + _WIDTH * 8);
        }
    }

    private void drawButton(Graphics g) {
        switch(over) {
            case 1:
                {
                    g.drawImage(helper, _HELPER_X, _HELPER_Y, this);
                    g.drawImage(closer_over, _CLOSER_X, _CLOSER_Y, this);
                    g.drawImage(newer, _NEWER_X, _NEWER_Y, this);
                }
                break;
            case 2:
                {
                    g.drawImage(helper_over, _HELPER_X, _HELPER_Y, this);
                    g.drawImage(closer, _CLOSER_X, _CLOSER_Y, this);
                    g.drawImage(newer, _NEWER_X, _NEWER_Y, this);
                }
                break;
            case 3:
                {
                    g.drawImage(helper, _HELPER_X, _HELPER_Y, this);
                    g.drawImage(closer, _CLOSER_X, _CLOSER_Y, this);
                    g.drawImage(newer_over, _NEWER_X, _NEWER_Y, this);
                }
                break;
            default:
                {
                    g.drawImage(helper, _HELPER_X, _HELPER_Y, this);
                    g.drawImage(closer, _CLOSER_X, _CLOSER_Y, this);
                    g.drawImage(newer, _NEWER_X, _NEWER_Y, this);
                }
        }
    }

    private void drawCross(Graphics g, int x, int y) {
        trace("drawCross x=" + x + "y =" + y + "\n");
        if (x == -1 || y == -1) {
            return;
        }
        int cx = x * _WIDTH + _X + (_WIDTH >> 1);
        int cy = y * _WIDTH + _Y + (_WIDTH >> 1);
        g.setColor(Color.white);
        g.drawLine(cx, cy - 4, cx, cy + 5);
        g.drawLine(cx + 1, cy - 4, cx + 1, cy + 5);
        g.drawLine(cx - 4, cy, cx + 5, cy);
        g.drawLine(cx - 4, cy + 1, cx + 5, cy + 1);
    }

    private void drawScore(Graphics g) {
        if (!game_over) {
            return;
        }
        g.setColor(_SCOREBG_COLOR);
        g.fillRect(_SCOREBG_X, _SCOREBG_Y, _SCOREBG_WIDTH, _SCOREBG_HEIGHT);
        g.setColor(_SCORE_COLOR);
        g.setFont(_SCORE_FONT);
        g.drawString("" + (pla_num / 10) + (pla_num % 10) + ":" + (com_num / 10) + (com_num % 10), _SCORE_X, _SCORE_Y);
        g.setFont(_WIN_FONT);
        g.drawString("WINNER IS: ", _WIN_X, _WIN_Y);
        if (pla_num > com_num) {
            g.drawImage(white_chess, _WIN_PIC_X, _WIN_PIC_Y, this);
        } else if (com_num > pla_num) {
            g.drawImage(black_chess, _WIN_PIC_X, _WIN_PIC_Y, this);
        } else {
            g.drawImage(white_chess, _WIN_PIC_X, _WIN_PIC_Y, this);
            g.drawImage(black_chess, _WIN_PIC_X + _WIDTH, _WIN_PIC_Y, this);
        }
    }

    private boolean isOver(boolean isPlayer) {
        int x1, y1;
        int step;
        int x = 0, y = 0;
        boolean sign = false;
        if (game_over) {
            return true;
        }
        if (pla_num + com_num == 64) {
            return true;
        }
        Square ret = new Square();
        Square p1, p2;
        if (isPlayer) {
            p1 = new Square(pla);
            p2 = new Square(com);
        } else {
            p1 = new Square(com);
            p2 = new Square(pla);
        }
        for (y = 0; y < 8; y++) {
            for (x = 0; x < 8; x++) {
                if (p1.getChess(x, y) || p2.getChess(x, y)) {
                    continue;
                }
                ret.setSquare(ai.neighbor[x][y]);
                if (ret.bitAnd(p2).isEmpty()) {
                    continue;
                }
                for (int inx = -1; inx < 2; inx++) {
                    for (int iny = -1; iny < 2; iny++) {
                        if (inx == 0 && iny == 0) {
                            continue;
                        }
                        step = 0;
                        x1 = x + inx;
                        y1 = iny + y;
                        while (p2.getChess(x1, y1)) {
                            x1 += inx;
                            y1 += iny;
                            step++;
                        }
                        if (step > 0 && p1.getChess(x1, y1)) {
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }

    private void gameOver() {
        int max = Math.max(pla_num, com_num);
        int p = 0;
        int c = 0;
        int x, y;
        pla.setSquare(0l);
        com.setSquare(0l);
        update(getGraphics());
        for (int i = 0; i < max; i++) {
            if (p < pla_num) {
                p++;
                x = (p - 1) & 7;
                y = (p - 1) >> 3;
                pla.setChess(x, y);
            }
            if (c < com_num) {
                c++;
                x = (64 - c) & 7;
                y = (64 - c) >> 3;
                com.setChess(x, y);
            }
            sleep(30);
            update(getGraphics());
        }
        game_over = true;
        update(getGraphics());
    }

    public void update(Graphics graphics) {
        if (offscreen == null) {
            offscreen = createImage(430, 450);
        }
        Graphics g = offscreen.getGraphics();
        paint(g);
        graphics.drawImage(offscreen, 0, 0, this);
    }

    public void paint(Graphics g) {
        drawBoard(g);
        drawButton(g);
        for (int y = 0; y < 8; y++) {
            for (int x = 0; x < 8; x++) {
                if (com.getChess(x, y)) {
                    drawChess(g, black_chess, x, y);
                } else if (pla.getChess(x, y)) {
                    drawChess(g, white_chess, x, y);
                }
            }
        }
        if (oldx != -1 && oldy != -1) {
            drawChess(g, twhite_chess, oldx, oldy);
        }
        drawCross(g, com_step_x, com_step_y);
        drawGrid(g);
        drawScore(g);
    }

    private void trace(Object o) {
        if (!_DEBUG) {
            return;
        }
        System.err.print(o);
    }

    private void doHelp() {
        HelpDialog dialog = new HelpDialog((Frame) this.getParent());
        dialog.setVisible(true);
    }

    private void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (Exception e) {
        }
    }

    public String toString() {
        String str = "player=" + pla_num + " computer=" + com_num + "\n";
        for (int y = 0; y < 8; y++) {
            for (int x = 0; x < 8; x++) {
                if (com.getChess(x, y)) {
                    str += "|X";
                }
                if (pla.getChess(x, y)) {
                    str += "|O";
                }
                if ((!pla.getChess(x, y)) && (!com.getChess(x, y))) {
                    str += "| ";
                }
            }
            str += "|\n";
        }
        return str;
    }
}
