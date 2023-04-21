package se.kth.cid.server.mobile.conzilla;

import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.Font;
import java.util.Hashtable;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Date;
import javax.microedition.lcdui.StringItem;
import javax.microedition.lcdui.Canvas;
import javax.microedition.midlet.MIDlet;
import javax.microedition.lcdui.Graphics;
import java.io.PrintStream;
import java.util.Vector;

public class Map extends Canvas implements CommandListener {

    ContextMap coMap = null;

    Concept currCpt;

    ConceptBox box = null;

    Relation rel = null;

    Parser pars = null;

    static Font font;

    Graphics g;

    public Display disp = null;

    boolean popup = false;

    Forms F;

    private int zoom = 0;

    HttpConn hc;

    boolean mouseOverBox = false;

    static final long toPopupDelay = 500;

    static final long popupLifetime = 2000;

    Timer timer1, timer2;

    MyTimerTask1 timerTask1;

    MyTimerTask2 timerTask2;

    Displayable prev;

    int w, h, chosenBox;

    int xdir = 0;

    int ydir = 0;

    private Hashtable concCMD = null;

    String mapURI, conceptURI, conceptDetailURI, title;

    static final Command CONCEPT = new Command("Concept(s)", Command.SCREEN, 1);

    static final Command REL = new Command("Relation(s)", Command.SCREEN, 1);

    static final Command BOKMARK = new Command("Bokmarks...", Command.SCREEN, 1);

    static final Command LINE = new Command("....................", Command.SCREEN, 1);

    static final Command LINE_1 = new Command(".........", Command.SCREEN, 1);

    static final Command DESC = new Command("Description...", Command.SCREEN, 1);

    static final Command ADDtoBOK = new Command("Add this to bokmarks", Command.SCREEN, 1);

    static final Command ZOOMIN = new Command("Zoom In", Command.SCREEN, 1);

    static final Command ZOOMOUT = new Command("Zoom Out", Command.SCREEN, 1);

    Vector currCMDs = new Vector(20, 5);

    Command CMD = null;

    int size = 0;

    int mx, my;

    static int nyy = 0;

    static int nyx = 0;

    public Map(Display dis, Forms f) {
        F = f;
        disp = dis;
        coMap = F.coMap;
        title = coMap.getTitle();
        initiate();
        disp.setCurrent(this);
    }

    void fixCMDs() {
        addCommand(LINE);
        addCommand(F.OPEN);
        addCommand(F.GOTO);
        addCommand(F.SAVE);
        addCommand(ADDtoBOK);
        addCommand(F.BOKMARK);
        addCommand(F.BACK);
    }

    void initiate() {
        mapList();
        fixCMDs();
        h = getHeight();
        w = getWidth();
        mx = w / 2;
        my = h / 2;
        setCommandListener(this);
    }

    void mapList() {
        Content conts;
        int nrBox = coMap.getnrBox();
        int nrRel = coMap.getnrRel();
        int contNr;
        String cmd = "";
        Command com = null;
        concCMD = new Hashtable(nrBox);
        addCommand(ZOOMIN);
        addCommand(ZOOMOUT);
        addCommand(LINE_1);
        for (int i = 0; i < nrBox; i++) {
            box = coMap.getBox(i);
            cmd = box.getTitle();
            if (cmd.length() != 0) {
                com = new Command(cmd, Command.SCREEN, 1);
                concCMD.put(com, box);
                currCMDs.addElement(com);
                addCommand(com);
            }
        }
        size = currCMDs.size();
    }

    public void commandAction(Command c, Displayable d) {
        if (c == F.BACK) {
            F.back = true;
            disp.setCurrent(F.backOrForth('b'));
        } else if (c == ZOOMIN) {
            zoom("in");
        } else if (c == ZOOMOUT) {
            zoom("out");
        } else if (c == F.OPEN) {
            F.current = F.openFile;
            disp.setCurrent(F.openFile);
        } else if (c == F.GOTO) {
            F.tf.setString("http://");
            F.current = F.wForm;
            disp.setCurrent(F.wForm);
        } else if (c == F.SAVE) {
            F.files.put(title, F.protocol);
            F.openFile.append(title, null);
            F.msgAl.setString("File '" + title + "' saved!");
            disp.setCurrent(F.msgAl, this);
        } else if (c == F.BOKMARK) {
            F.current = F.bokmarkList;
            disp.setCurrent(F.bokmarkList);
        } else if (c == ADDtoBOK) {
            F.bokmarksVec.addElement(mapURI);
            F.bokmarkList.append(mapURI, null);
            F.msgAl.setString("Bokmark '" + mapURI + "' saved!");
            disp.setCurrent(F.msgAl, this);
        } else if (c == LINE) {
            return;
        } else if (concCMD.containsKey(c)) {
            currCpt = (Concept) concCMD.get(c);
            F.descBox.setString(currCpt.getInfo());
            F.current = F.descBox;
            disp.setCurrent(F.descBox);
        }
        cleanMouse();
        System.gc();
    }

    public void zoom(String z) {
        if (z.equals("in")) {
            if (zoom + 2 >= 4) {
                zoom = 4;
                return;
            } else {
                zoom += 2;
                repaint();
            }
        } else {
            if (zoom - 2 <= -6) {
                zoom = -6;
                return;
            } else {
                zoom -= 2;
                repaint();
            }
        }
    }

    public void cleanScreen(Graphics g) {
        g.setColor(0x00CCFFFF);
        g.fillRect(0, 0, w, h);
        g.setColor(0x00000000);
    }

    void rmCMDs() {
        for (int i = 0; i < size; i++) {
            CMD = (Command) currCMDs.elementAt(i);
            removeCommand(CMD);
        }
        currCMDs.removeAllElements();
        size = currCMDs.size();
    }

    protected void keyRepeated(int key) {
        int action = getGameAction(key);
        int dir = 0;
        switch(key) {
            case 49:
                mx = w / 2 - 2;
                my = h / 2 - 2;
                break;
            case 51:
                mx = w / 2 + 2;
                my = h / 2 - 2;
                break;
            case 55:
                mx = w / 2 - 2;
                my = h / 2 + 2;
                break;
            case 57:
                mx = w / 2 + 2;
                my = h / 2 + 2;
                break;
            default:
                return;
        }
        repaint();
    }

    protected void keyPressed(int key) {
        int mv = 4;
        if (key >= -4 && key <= -1) {
            setXYDir(key);
        } else if (key >= 49 && key <= 57) {
            switch(key) {
                case 48:
                    print("Key=0 == Forward");
                    break;
                case 49:
                    moveMouse(49, -mv, -mv, w / 2, h / 2);
                    break;
                case 50:
                    moveMouse(50, 0, -mv, 0, h / 2);
                    break;
                case 51:
                    moveMouse(51, mv, -mv, -w / 2, h / 2);
                    break;
                case 52:
                    moveMouse(52, -mv, 0, w / 2, 0);
                    break;
                case 53:
                    detailSurf();
                    break;
                case 54:
                    moveMouse(54, mv, 0, -w / 2, 0);
                    break;
                case 55:
                    moveMouse(55, -mv, mv, w / 2, -h / 2);
                    break;
                case 56:
                    moveMouse(56, 0, mv, 0, -h / 2);
                    break;
                case 57:
                    moveMouse(57, mv, mv, -w / 2, -h / 2);
                    break;
                default:
                    return;
            }
        }
        repaint();
        findBox();
    }

    void moveMouse(int key, int x, int y, int wid, int hei) {
        mx += x;
        my += y;
        moveXY(wid, hei, key);
    }

    void mouse(Graphics g) {
        g.drawLine(mx, my, mx + 8, my + 5);
        g.drawLine(mx + 8, my + 5, mx + 5, my + 8);
        g.drawLine(mx + 5, my + 8, mx, my);
    }

    public void cleanMouse() {
        xdir = 0;
        ydir = 0;
        mx = w / 2;
        my = h / 2;
        nyx = 0;
        nyy = 0;
    }

    public void findBox() {
        int[] pos = coMap.getAllBoxPos();
        int x, y, xx, yy;
        mouseOverBox = false;
        if (timer1 != null) {
            timer1.cancel();
            timer1 = null;
        }
        if (timer2 != null) {
            timer2.cancel();
            timer2 = null;
        }
        popup = false;
        for (int k = 0; k < (pos.length / 4); k++) {
            x = pos[4 * k];
            y = pos[4 * k + 1];
            xx = pos[4 * k + 2];
            yy = pos[4 * k + 3];
            if (zoom > 0) {
                x = x * zoom;
                y = y * zoom;
                xx = xx * zoom;
                yy = yy * zoom;
            } else if (zoom < 0) {
                x = x / (-zoom);
                y = y / (-zoom);
                xx = xx / (-zoom);
                yy = yy / (-zoom);
            }
            if (mx >= x && mx <= x + xx && my >= y && my <= y + yy) {
                chosenBox = k;
                box = coMap.getBox(k);
                title = box.getTitle();
                mouseOverBox = true;
                timer1 = new Timer();
                timerTask1 = new MyTimerTask1();
                timer1.schedule(timerTask1, toPopupDelay);
                repaint();
                break;
            }
        }
    }

    public void detailSurf() {
        if (mouseOverBox) {
            String uri = box.grann_URIs;
            String url = box.getURI();
            F.initConn(box.getDetailURI());
            F.initMap();
        }
    }

    public class MyTimerTask1 extends TimerTask {

        public void run() {
            popup = true;
            timer2 = new Timer();
            timerTask2 = new MyTimerTask2();
            timer2.schedule(timerTask2, popupLifetime);
            repaint();
        }
    }

    public class MyTimerTask2 extends TimerTask {

        public void run() {
            popup = false;
            repaint();
        }
    }

    public void boxAction(String t) {
        g.drawRect(5, 5, w - 10, h / 2);
        g.drawString(t, (w / 2) - 10, (h / 2) - h / 4, 0 | 0);
    }

    public void drawPopup() {
        int br = (font.stringWidth(title)) / 2;
        int ho = (font.getHeight()) / 2;
        int Tx = nyx + w / 2 - br;
        int Ty = nyy + h / 2 - ho;
        int tmpColor = g.getColor();
        g.setColor(0xFFFFFF);
        g.fillRect(nyx + 10, nyy + h / 4, (w - 20), (h / 3));
        g.setColor(0x000000);
        g.drawRect(nyx + 10, nyy + h / 4, (w - 20), (h / 3));
        g.setColor(tmpColor);
        g.setFont(Font.getFont(Font.FACE_SYSTEM, Font.STYLE_PLAIN, Font.SIZE_LARGE));
        g.drawString(title, Tx, Ty, 0 | 0);
    }

    public void setXYDir(int x) {
        int mapX = coMap.mapArr[0];
        int mapY = coMap.mapArr[1];
        int fri = 20;
        switch(x) {
            case -4:
                if ((xdir - w / 2) <= -mapX) xdir = -mapX - fri; else xdir -= w / 2;
                mx += w / 2;
                nyx += w / 2;
                break;
            case -3:
                if ((xdir + w / 2) >= mapX) xdir = mapX + fri; else xdir += w / 2;
                mx -= w / 2;
                nyx -= w / 2;
                break;
            case -1:
                if ((ydir + h / 2) >= mapY) ydir = mapY + fri; else ydir += h / 2;
                my -= h / 2;
                nyy -= h / 2;
                break;
            case -2:
                if ((ydir - h / 2) <= -mapY) ydir = -mapY - fri; else ydir -= h / 2;
                my += h / 2;
                nyy += h / 2;
                break;
            case 49:
                xdir += w / 2;
                ydir += h / 2;
                break;
            case 50:
                ydir += h / 2;
                break;
            case 51:
                xdir -= w / 2;
                ydir += h / 2;
                break;
            case 52:
                xdir += w / 2;
                break;
            case 54:
                xdir -= w / 2;
                break;
            case 55:
                xdir += w / 2;
                ydir -= h / 2;
                break;
            case 56:
                ydir -= h / 2;
                break;
            case 57:
                xdir -= w / 2;
                ydir -= h / 2;
                break;
            default:
                return;
        }
    }

    public void moveXY(int x, int y, int key) {
        if ((mx < nyx || mx > (w + nyx)) && ((my < nyy) || my > (h + nyy))) {
            nyx += x;
            nyy += y;
            setXYDir(key);
        } else if (mx < nyx || mx > (w + nyx)) {
            nyx += x;
            setXYDir(key);
        } else if (my < nyy || my > (h + nyy)) {
            nyy += y;
            setXYDir(key);
        }
    }

    public void paint(Graphics gr) {
        g = gr;
        font = gr.getFont();
        cleanScreen(gr);
        gr.translate(xdir, ydir);
        coMap.paint(gr, w, h, zoom);
        mouse(gr);
        if (popup) drawPopup();
    }

    void print(String s) {
        System.out.println("Map:" + s);
    }
}
