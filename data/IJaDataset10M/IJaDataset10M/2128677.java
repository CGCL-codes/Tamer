package worlds;

import utils.Utils;
import worlds.intf.WorldGridView;
import java.awt.*;
import java.io.Serializable;
import java.util.*;
import java.util.List;

/**
 * 2x2 creature, 2 arms added
 */
public class GridRadialWorld1 implements WorldGridView, Serializable {

    int lastCmdRes = 0;

    int availableResults = 0;

    String[] FIELD_INIT = { "bbbbbbbbb", "b yy   bb", "by  by bb", "b       b", "b       b", "b**b    b", "b      bb", "bbbbbbbbb" };

    public Collection<String> targetSensors() {
        return Collections.singleton("$");
    }

    public int availableResults() {
        return availableResults;
    }

    public boolean commandWrong(String cmd) {
        return false;
    }

    enum Dir {

        left(-1, 0, "<"), up(0, -1, "^"), right(1, 0, ">"), down(0, 1, "V");

        int dx;

        int dy;

        String ch;

        Dir(int dx, int dy, String ch) {
            this.dx = dx;
            this.dy = dy;
            this.ch = ch;
        }
    }

    int width = FIELD_INIT[0].length();

    int height = FIELD_INIT.length;

    Color[][] cdata = new Color[width][height];

    String prevCommand = null;

    Cr cr = new Cr();

    class Cr implements Serializable {

        int x0 = 2;

        int y0 = 2;

        int dirIdx = 1;

        boolean larm = false;

        boolean rarm = false;

        boolean lleg = false;

        boolean rleg = false;
    }

    Dir getDir() {
        return Dir.values()[cr.dirIdx];
    }

    Point getFwd(int dxFwd, int dyLeft) {
        Dir dirLeft = Dir.values()[dirIdxLeft()];
        return new Point(cr.x0 + getDir().dx * dxFwd + dirLeft.dx * dyLeft, cr.y0 + getDir().dy * dxFwd + dirLeft.dy * dyLeft);
    }

    Point getFwd() {
        return getFwd(1, 0);
    }

    public GridRadialWorld1() {
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                Color c;
                char ch = FIELD_INIT[j].charAt(i);
                switch(ch) {
                    case '*':
                        c = Color.GRAY;
                        break;
                    case 'b':
                        c = Color.BLACK;
                        break;
                    case ' ':
                        c = Color.WHITE;
                        break;
                    case 'y':
                        c = Color.YELLOW;
                        break;
                    default:
                        throw new RuntimeException("char=" + ch);
                }
                cdata[i][j] = c;
            }
        }
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    /**
   * For painting only, not used from creature algorithm
   */
    public Color getColorDisplay(int x, int y) {
        Color c = getColor(x, y);
        return c;
    }

    public boolean pointInLimb(int x, int y) {
        Point p = new Point(x, y);
        if (cr.larm) {
            if (getFwd(1, 0).equals(p)) return true;
        }
        if (cr.rarm) {
            if (getFwd(1, -1).equals(p)) return true;
        }
        if (cr.lleg) {
            if (getFwd(-2, 0).equals(p)) return true;
        }
        if (cr.rleg) {
            if (getFwd(-2, -1).equals(p)) return true;
        }
        return false;
    }

    public boolean pointInCr(int x, int y) {
        Point p = new Point(x, y);
        if (getFwd(0, 0).equals(p)) return true;
        if (getFwd(-1, 0).equals(p)) return true;
        if (getFwd(0, -1).equals(p)) return true;
        if (getFwd(-1, -1).equals(p)) return true;
        return pointInLimb(x, y);
    }

    public Color getColor(int x, int y) {
        if (pointInLimb(x, y)) {
            return Color.CYAN;
        }
        if (pointInCr(x, y)) {
            return Color.BLUE;
        }
        if (x < 0 || y < 0 || x >= getWidth() || y >= getHeight()) {
            return Color.BLACK;
        }
        Color c = cdata[x][y];
        return c;
    }

    public Color getColor(Point p) {
        return getColor(p.x, p.y);
    }

    public String getColorName(Point p) {
        return Utils.color2name(getColor(p));
    }

    public String getChar(int x, int y) {
        if (x == cr.x0 && y == cr.y0) {
            return getDir().ch;
        }
        if (crVisible().contains(new Point(x, y))) {
            return "*";
        }
        return null;
    }

    public List<String> commands() {
        return Arrays.asList("L", "R", "N", "Fb", "A1", "A2F", "A2B");
    }

    int dirIdxLeft() {
        int i = cr.dirIdx - 1;
        if (i < 0) {
            i = Dir.values().length - 1;
        }
        return i;
    }

    void setX0Y0(Point p) {
        cr.x0 = p.x;
        cr.y0 = p.y;
    }

    public void command(String cmd) {
        int result = 0;
        if (cmd.equals("L") && !cr.larm && !cr.rarm && !cr.lleg && !cr.rleg) {
            setX0Y0(getFwd(-1, 0));
            cr.dirIdx = dirIdxLeft();
        }
        if (cmd.equals("R") && !cr.larm && !cr.rarm && !cr.lleg && !cr.rleg) {
            setX0Y0(getFwd(0, -1));
            cr.dirIdx++;
            if (cr.dirIdx == Dir.values().length) {
                cr.dirIdx = 0;
            }
        }
        if (cmd.equals("Fb") && !cr.larm && !cr.rarm && !cr.lleg && !cr.rleg) {
            result = moveFwd();
        }
        if (cmd.equals("A1")) {
            if (!cr.larm) {
                Point p = getFwd();
                Color color = getColor(p);
                if (color.equals(Color.YELLOW)) {
                    cdata[p.x][p.y] = Color.ORANGE;
                } else if (color.equals(Color.ORANGE)) {
                    cdata[p.x][p.y] = Color.WHITE;
                    cr.larm = true;
                    result = 1;
                } else if (color.equals(Color.GRAY)) {
                    cdata[p.x][p.y] = Color.WHITE;
                    cr.larm = true;
                } else if (color.equals(Color.WHITE)) {
                    cr.larm = true;
                }
            } else {
                cr.larm = false;
            }
        }
        if (cmd.equals("A2F") && !cr.rarm) {
            Point p = getFwd(1, -1);
            Color color = getColor(p);
            if (color.equals(Color.YELLOW)) {
                cdata[p.x][p.y] = Color.ORANGE;
            } else if (color.equals(Color.ORANGE)) {
                cdata[p.x][p.y] = Color.WHITE;
                cr.rarm = true;
                result = 1;
            } else if (color.equals(Color.GRAY)) {
                cdata[p.x][p.y] = Color.WHITE;
                cr.rarm = true;
            } else if (color.equals(Color.WHITE)) {
                cr.rarm = true;
            }
        }
        if (cmd.equals("A2B") && cr.rarm) {
            cr.rarm = false;
        }
        prevCommand = cmd;
        lastCmdRes = result;
    }

    private int moveFwd() {
        int result = 0;
        Point prwd = getFwd();
        Color color = getColor(prwd);
        Color colorFR = getColor(getFwd(1, -1));
        if (color.equals(Color.WHITE) && colorFR.equals(Color.WHITE)) {
            cr.x0 = prwd.x;
            cr.y0 = prwd.y;
        } else if (color.equals(Color.BLACK) || colorFR.equals(Color.BLACK)) {
            result = -1;
        }
        addNewObject(Color.YELLOW);
        addNewObject(Color.GRAY);
        return result;
    }

    int count(Color c) {
        int count = 0;
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if (getColor(x, y).equals(c)) {
                    count++;
                }
            }
        }
        return count;
    }

    void addNewObject(Color color) {
        if (count(color) > width * height * 0.2) {
            return;
        }
        long t0 = System.currentTimeMillis();
        while (true) {
            int x = (int) (Math.random() * width);
            int y = (int) (Math.random() * height);
            Point pNew = new Point(x, y);
            if (getColor(pNew).equals(Color.WHITE) && distToCr(pNew) >= 3) {
                cdata[x][y] = color;
                return;
            }
            if (System.currentTimeMillis() - t0 > 30) {
                return;
            }
        }
    }

    int distToCr(Point p) {
        return Math.max(Math.abs(p.x - cr.x0), Math.abs(p.y - cr.y0));
    }

    public Map<String, Point> sensorLocations() {
        Map<String, Point> m = new HashMap<String, Point>();
        for (int i = 0; i <= 8; i++) {
            m.put("r" + i, new Point(1, -i));
        }
        return m;
    }

    Map<String, Point> visibleCrRelatives() {
        Map<String, Point> m = new HashMap<String, Point>();
        for (int i = 0; i <= 8; i++) {
            int grad = 180 - 9 - i * 20;
            double rad = grad / 180. * Math.PI;
            for (double d = 0.7; ; d += 0.7) {
                int fwd = 1 + (int) (d * Math.sin(rad));
                int left = (int) Math.floor(-d * Math.cos(rad));
                Point abs = getFwd(fwd, left);
                Color c = getColor(abs);
                if (!c.equals(Color.WHITE)) {
                    m.put("r" + i, new Point(fwd, left));
                    break;
                }
            }
        }
        return m;
    }

    Set<Point> crVisible() {
        Set<Point> s = new HashSet<Point>();
        Map<String, Point> rel = visibleCrRelatives();
        for (String k : rel.keySet()) {
            Point abs = getFwd(rel.get(k).x, rel.get(k).y);
            s.add(abs);
        }
        return s;
    }

    public Map<String, Object> view() {
        Map<String, Object> m = new HashMap<String, Object>();
        Map<String, Point> rel = visibleCrRelatives();
        for (String k : rel.keySet()) {
            Point abs = getFwd(rel.get(k).x, rel.get(k).y);
            m.put(k, (Object) getColorName(abs));
        }
        m.put("$", lastCmdRes);
        return m;
    }
}
