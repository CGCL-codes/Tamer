package Graphic;

import java.awt.event.*;
import java.awt.*;
import java.util.Timer;
import java.util.TimerTask;
import java.lang.*;
import java.io.*;
import physic.*;

/**
 *
 * @author  ����
 */
public class GameApplet extends javax.swing.JDialog {

    /** Creates new form NewJDialog */
    GameApplet(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
    }

    private void initComponents() {
        bindingGroup = new org.jdesktop.beansbinding.BindingGroup();
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        org.jdesktop.beansbinding.Binding binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, this, org.jdesktop.beansbinding.ELProperty.create("Satellite"), this, org.jdesktop.beansbinding.BeanProperty.create("title"));
        bindingGroup.addBinding(binding);
        addMouseListener(new java.awt.event.MouseAdapter() {

            public void mouseClicked(java.awt.event.MouseEvent evt) {
                formMouseClicked(evt);
            }
        });
        addWindowListener(new java.awt.event.WindowAdapter() {

            public void windowActivated(java.awt.event.WindowEvent evt) {
                formWindowActivated(evt);
            }

            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
        });
        addKeyListener(new java.awt.event.KeyAdapter() {

            public void keyPressed(java.awt.event.KeyEvent evt) {
                formKeyPressed(evt);
            }

            public void keyReleased(java.awt.event.KeyEvent evt) {
                formKeyReleased(evt);
            }
        });
        getContentPane().setLayout(null);
        bindingGroup.bind();
        pack();
    }

    private void formWindowActivated(java.awt.event.WindowEvent evt) {
    }

    private void formKeyReleased(java.awt.event.KeyEvent evt) {
        if (current == null) {
            if (evt.getKeyCode() == Pl1Left) {
                Left = false;
            }
            if (evt.getKeyCode() == Pl1Right) {
                Right = false;
            }
            if (evt.getKeyCode() == Pl1Up) {
                Up = false;
            }
            if (evt.getKeyCode() == Pl1Down) {
                Down = false;
            }
            if (evt.getKeyCode() == Pl2Left) {
                Left1 = false;
            }
            if (evt.getKeyCode() == Pl2Right) {
                Right1 = false;
            }
            if (evt.getKeyCode() == Pl2Up) {
                Up1 = false;
            }
            if (evt.getKeyCode() == Pl2Down) {
                Down1 = false;
            }
            if (evt.getKeyCode() == KeyEvent.VK_ESCAPE) {
                game = !game;
                current = escmenu;
            }
        }
    }

    private void formKeyPressed(java.awt.event.KeyEvent evt) {
        if (current == null) {
            if (evt.getKeyCode() == Pl1Left) {
                Left = true;
            }
            if (evt.getKeyCode() == Pl1Right) {
                Right = true;
            }
            if (evt.getKeyCode() == Pl1Up) {
                Up = true;
            }
            if (evt.getKeyCode() == Pl1Down) {
                Down = true;
            }
            if (evt.getKeyCode() == Pl2Left) {
                Left1 = true;
            }
            if (evt.getKeyCode() == Pl2Right) {
                Right1 = true;
            }
            if (evt.getKeyCode() == Pl2Up) {
                Up1 = true;
            }
            if (evt.getKeyCode() == Pl2Down) {
                Down1 = true;
            }
        }
    }

    private void formWindowOpened(java.awt.event.WindowEvent evt) {
        init();
        timer = new Timer();
        timertask = new TimerTask() {

            public void run() {
                ontime();
            }
        };
        timer.schedule(timertask, 100, 10);
    }

    private void formMouseClicked(java.awt.event.MouseEvent evt) {
        if (current != null) {
            current.Click(evt.getX(), evt.getY());
        }
    }

    public Image loadImage(int width, int height, String filename) {
        Image image = this.createImage(width, height);
        try {
            InputStream in = this.getClass().getResourceAsStream(filename);
            byte[] buffer = new byte[in.available()];
            for (int i = 0; i < buffer.length; i++) {
                buffer[i] = (byte) in.read();
            }
            image = Toolkit.getDefaultToolkit().createImage(buffer);
        } catch (Exception e) {
            System.err.println("Image File [" + filename + "] not found");
        }
        return image;
    }

    @Override
    public void paint(Graphics g) {
        if (run) g.drawImage(img, 0, 0, this);
    }

    private void savegame() {
    }

    private void loadgame() {
    }

    private void newgame() {
        World = new PhWorld();
        String filename = "init.txt";
        try {
            InputStream in = this.getClass().getResourceAsStream(filename);
            char[] buffer = new char[in.available()];
            for (int i = 0; i < buffer.length; i++) {
                buffer[i] = (char) in.read();
            }
            String s = java.lang.String.copyValueOf(buffer);
            s = "";
            for (int i = 0; i < buffer.length; i++) {
                if (buffer[i] != '\n') s = s + buffer[i]; else {
                    int x, y;
                    if (buffer[i + 1] == 'x') {
                        i++;
                        String st = "";
                        i += 2;
                        for (; buffer[i] != '\n'; i++) {
                            st += buffer[i];
                        }
                        x = java.lang.Integer.decode(st);
                        if (buffer[i + 1] == 'y') {
                            i += 2;
                            i++;
                            st = "";
                            for (; buffer[i] != '\n'; i++) {
                                st += buffer[i];
                            }
                            y = java.lang.Integer.decode(st);
                            if (s.equals("AddStar")) World.AddStar(x, y);
                            if (s.equals("AddPlanet")) World.AddPlanet(x, y);
                            if (s.equals("AddMeteor")) World.AddMeteor(x, y);
                        } else System.err.println("Error in File [" + filename + "]");
                    } else System.err.println("Error in File [" + filename + "]");
                    if (buffer[i + 1] == 'v' && buffer[i + 2] == 'x') {
                        PhObject star = World.GetObjects().get(World.GetObjects().size() - 1);
                        i += 3;
                        i++;
                        String st = "";
                        for (; buffer[i] != '\n'; i++) {
                            st += buffer[i];
                        }
                        Integer vx = java.lang.Integer.decode(st);
                        star.SetVx(vx);
                    }
                    if (buffer[i + 1] == 'v' && buffer[i + 2] == 'y') {
                        PhObject star = World.GetObjects().get(World.GetObjects().size() - 1);
                        i += 3;
                        i++;
                        String st = "";
                        for (; buffer[i] != '\n'; i++) {
                            st += buffer[i];
                        }
                        Integer vy = java.lang.Integer.decode(st);
                        star.SetVy(vy);
                    }
                    if (buffer[i + 1] == 'R') {
                        PhObject star = World.GetObjects().get(World.GetObjects().size() - 1);
                        i += 2;
                        i++;
                        String st = "";
                        for (; buffer[i] != '\n'; i++) {
                            st += buffer[i];
                        }
                        Integer R = java.lang.Integer.decode(st);
                        star.SetR(R);
                    }
                    s = "";
                }
            }
        } catch (Exception e) {
            System.err.println("File [" + filename + "] not found");
        }
        double pi = 3.14159265359;
        PhBullet a, c;
        PhEvent b;
        Weapon w = new Weapon();
        b = new PhEvent();
        b.SetType(1);
        b.SetTime(2000);
        b.SetActions("A");
        a = new PhBullet();
        a.SetR(3);
        a.SetDamage(0);
        a.SetSdamage(0.5);
        a.SetM(0.04);
        a.SetHealth(1);
        a.SetPower(0);
        a.SetAngle(1);
        c = a.clone();
        c.SetR(2);
        c.SetHealth(0.000000001);
        a.GetEvents().add(b.clone());
        w.SetReloading(0.2);
        w.SetEnergy(200);
        c = a.clone();
        w.AddBullet(c, 0, 20, 500);
        w.AddBullet(c, pi / 20, 20, 500);
        w.AddBullet(c, -pi / 20, 20, 500);
        b.SetType(1);
        b.SetTime(5);
        b.SetActions("F0");
        a.GetEvents().add(b.clone());
        b.SetType(1);
        b.SetTime(4);
        a.GetVWeapon().add(w.clone());
        a.GetEvents().add(b.clone());
        for (int ii = 0; ii < 2; ii++) {
            c = a.clone();
            a.GetVWeapon().get(0).GetVBullet().set(0, c);
            a.GetVWeapon().get(0).GetVBullet().set(1, c);
            a.GetVWeapon().get(0).GetVBullet().set(2, c);
        }
        World.AddShip("one");
        World.GetObjects().get(World.GetObjects().size() - 1).SetM(10);
        PhShip temp = (PhShip) World.GetObjects().get(World.GetObjects().size() - 1);
        temp.SetX(100);
        temp.SetY(1600);
        temp.SetMaxEnergy(1000);
        temp.SetEnergy(1000);
        temp.SetEngine(10);
        temp.SetPower(2000);
        temp.SetR(20);
        temp.AddWeapon(w);
        temp.SetRegeration(0.1);
        temp.SetSolarBattery(30000);
        temp.SetDamage(10);
        temp.SetSdamage(10);
        World.AddShip("two");
        temp.SetX(2900);
        temp.SetY(1600);
        temp = (PhShip) World.GetObjects().get(World.GetObjects().size() - 1);
        temp.SetMaxEnergy(1000);
        temp.SetEnergy(1000);
        temp.SetEngine(10);
        temp.SetPower(200);
        temp.SetR(20);
        World.GetObjects().get(World.GetObjects().size() - 1).SetM(10);
        temp.SetMaxEnergy(1000);
        temp.SetEnergy(1000);
        temp.SetEngine(10);
        temp.SetPower(2000);
        temp.SetDamage(10);
        temp.AddWeapon(w);
        temp.SetSolarBattery(30000);
        temp.SetDamage(10);
        temp.SetSdamage(10);
        World.GetObjects().get(World.GetObjects().size() - 1).SetDamage(0.4);
        World.GetObjects().get(World.GetObjects().size() - 1).SetDamage(0.5);
        World.AddMeteor(300, 500, 0, -70);
        World.GetObjects().get(World.GetObjects().size() - 1).SetSdamage(0);
        World.AddMeteor(299, 600, 0, 70);
        World.GetObjects().get(World.GetObjects().size() - 1).SetSdamage(0);
        double vx, vy;
        int am = 100;
        for (int i = 0; i < am; i++) {
            vy = -40 * Math.cos(i * 2 * pi / am);
            vx = 40 * Math.sin(i * 2 * pi / am);
            World.AddMeteor(1800 + (1200 + Math.random() * 100) * Math.cos(i * 2 * pi / am), 1600 + (1200 + Math.random() * 100) * Math.sin(i * 2 * pi / am), vx, vy);
        }
        World.Init();
        game = true;
    }

    public void init() {
        ship = loadImage(20, 20, "ship.gif");
        star = loadImage(100, 100, "star.gif");
        planet = loadImage(20, 20, "planet.gif");
        meteor = loadImage(20, 20, "meteor.gif");
        energy = loadImage(100, 100, "energy.gif");
        bullet = loadImage(100, 100, "bullet.gif");
        life = loadImage(100, 100, "life.gif");
        backgr = loadImage(1024, 1000, "background1.gif");
        Image resumeim = loadImage(569, 96, "Resume.gif");
        Image exitim = loadImage(569, 96, "Exit.gif");
        Image optionsim = loadImage(569, 96, "Options.gif");
        Image saveim = loadImage(569, 96, "Save.gif");
        Image loadim = loadImage(569, 96, "Load.gif");
        Image newgim = loadImage(569, 96, "New.gif");
        Image newuim = loadImage(569, 96, "New+up.gif");
        Image mainmenuim = loadImage(1024, 1000, "background1.gif");
        img = createImage(1024, 1000);
        img.getGraphics().drawImage(backgr, 0, 0, 1024, 1000, this);
        escmenu = new MenuItem(0, 0, 1000, 800, mainmenuim) {

            public void run() {
                game = true;
                current = null;
            }
        };
        MenuItem temp;
        temp = new MenuItem(100, 100, 569, 96, resumeim) {

            public void run() {
                game = true;
                current = null;
            }
        };
        escmenu.AddItem(temp);
        temp = new MenuItem(100, 100 + 96, 569, 96, newgim) {

            public void run() {
                current = newmenu;
            }
        };
        escmenu.AddItem(temp);
        temp = new MenuItem(100, 196 + 96, 569, 96, loadim) {

            public void run() {
                loadgame();
            }
        };
        escmenu.AddItem(temp);
        temp = new MenuItem(100, 196 + 96 + 96, 569, 96, saveim) {

            public void run() {
                savegame();
            }
        };
        escmenu.AddItem(temp);
        temp = new MenuItem(100, 196 + 96 + 96 + 96, 569, 96, exitim) {

            public void run() {
                System.exit(0);
            }
        };
        escmenu.AddItem(temp);
        mainmenu = new MenuItem(0, 0, 1000, 800, mainmenuim) {

            public void run() {
                game = true;
                current = null;
            }
        };
        temp = new MenuItem(100, 100, 569, 96, newuim) {

            public void run() {
                current = newmenu;
            }
        };
        mainmenu.AddItem(temp);
        temp = new MenuItem(100, 196, 569, 96, loadim) {

            public void run() {
                loadgame();
            }
        };
        mainmenu.AddItem(temp);
        temp = new MenuItem(100, 196 + 96, 569, 96, optionsim) {

            public void run() {
            }
        };
        mainmenu.AddItem(temp);
        temp = new MenuItem(100, 196 + 96 + 96, 569, 96, exitim) {

            public void run() {
                System.exit(0);
            }
        };
        mainmenu.AddItem(temp);
        newmenu = new MenuItem(0, 0, 1000, 800, backgr) {

            public void run() {
            }
        };
        temp = new MenuItem(100, 100, 569, 96, loadImage(569, 96, "Oneplayer.gif")) {

            public void run() {
            }
        };
        newmenu.AddItem(temp);
        temp = new MenuItem(100, 100 + 96, 569, 96, loadImage(569, 96, "Twoplayer.gif")) {

            public void run() {
                newgame();
                current = null;
            }
        };
        newmenu.AddItem(temp);
        temp = new MenuItem(100, 100 + 96 + 96, 569, 96, loadImage(569, 96, "Network.gif")) {

            public void run() {
            }
        };
        newmenu.AddItem(temp);
        current = mainmenu;
        Pl1Up = KeyEvent.VK_W;
        Pl2Up = KeyEvent.VK_UP;
        Pl1Down = KeyEvent.VK_S;
        Pl2Down = KeyEvent.VK_DOWN;
        Pl1Right = KeyEvent.VK_D;
        Pl2Right = KeyEvent.VK_RIGHT;
        Pl1Left = KeyEvent.VK_A;
        Pl2Left = KeyEvent.VK_LEFT;
    }

    public void DrawStar(PhObject obj) {
        Image im = star;
        img.getGraphics().drawImage(im, (int) Math.round((obj.GetX() - obj.GetR()) * k), (int) Math.round((obj.GetY() - obj.GetR()) * k), (int) Math.round(obj.GetR() * k * 2), (int) Math.round(obj.GetR() * k * 2), this);
    }

    public void DrawPlanet(PhObject obj) {
        Image im = planet;
        img.getGraphics().drawImage(im, (int) Math.round((obj.GetX() - obj.GetR()) * k), (int) Math.round((obj.GetY() - obj.GetR()) * k), (int) Math.round(obj.GetR() * k * 2), (int) Math.round(obj.GetR() * k * 2), this);
    }

    public void DrawShip(PhObject obj) {
        PhShip sh;
        sh = (PhShip) obj;
        Image im = ship;
        img.getGraphics().drawImage(im, (int) Math.round((obj.GetX() - obj.GetR()) * k), (int) Math.round((obj.GetY() - obj.GetR()) * k), (int) Math.round(obj.GetR() * k * 2), (int) Math.round(obj.GetR() * k * 2), this);
        img.getGraphics().drawImage(meteor, (int) Math.round((sh.GetX() - sh.GetR() * 0.8 / 2 + sh.GetR() / 2 * Math.cos(sh.GetAngle())) * k), (int) Math.round((sh.GetY() - sh.GetR() * 0.8 / 2 + sh.GetR() / 2 * Math.sin(sh.GetAngle())) * k), (int) Math.round(sh.GetR() * 0.8 * k), (int) Math.round(sh.GetR() * 0.8 * k), this);
        img.getGraphics().drawImage(meteor, (int) Math.round((sh.GetX() - sh.GetR() * 0.8 / 2 + sh.GetR() / 2 * Math.cos(sh.GetAngle())) * k), (int) Math.round((sh.GetY() - sh.GetR() * 0.8 / 2 + sh.GetR() / 2 * Math.sin(sh.GetAngle())) * k), (int) Math.round(sh.GetR() * 0.8 * k), (int) Math.round(sh.GetR() * 0.8 * k), this);
        if (obj.GetName().equals("s_one")) {
            img.getGraphics().drawImage(life, 10, 20, (int) Math.round(sh.GetHealth() * 400 / (int) Math.round(sh.GetMaxHealth())), 20, this);
            img.getGraphics().drawImage(energy, 10, 40, (int) Math.round(sh.GetEnergy() * 400 / (int) Math.round(sh.GetMaxEnergy())), 20, this);
        }
        if (obj.GetName().equals("s_two")) {
            img.getGraphics().drawImage(life, 600, 20, (int) Math.round(sh.GetHealth() * 400 / (int) Math.round(sh.GetMaxHealth())), 20, this);
            img.getGraphics().drawImage(energy, 600, 40, (int) Math.round(sh.GetEnergy() * 400 / (int) Math.round(sh.GetMaxEnergy())), 20, this);
        }
    }

    public void DrawMeteor(PhObject obj) {
        Image im = meteor;
        img.getGraphics().drawImage(im, (int) Math.round((obj.GetX() - obj.GetR()) * k), (int) Math.round((obj.GetY() - obj.GetR()) * k), (int) Math.round(obj.GetR() * k * 2), (int) Math.round(obj.GetR() * k * 2), this);
    }

    public void DrawBullet(PhObject obj) {
        Image im = bullet;
        img.getGraphics().drawImage(im, (int) Math.round((obj.GetX() - obj.GetR()) * k), (int) Math.round((obj.GetY() - obj.GetR()) * k), (int) Math.round(obj.GetR() * k * 2), (int) Math.round(obj.GetR() * k * 2), this);
    }

    public void DrawAll() {
        for (int i = 0; i < World.GetObjects().size(); i++) {
            PhObject obj = World.GetObjects().get(i);
            if (obj.GetX() < fieldx1 * k || obj.GetY() < fieldy1 * k || obj.GetY() > fieldy2 * k || obj.GetX() > fieldx2 * k) {
                if (obj.GetName().substring(0, 2).equals("s_")) {
                    ((PhShip) obj).SetHealth(-1);
                } else {
                    World.GetObjects().remove(i);
                }
            } else {
                if (obj.GetName().equals("Star")) {
                    DrawStar(obj);
                }
                if (obj.GetName().equals("Planet")) {
                    DrawPlanet(obj);
                }
                if (obj.GetName().substring(0, 2).equals("s_")) {
                    DrawShip(obj);
                }
                if (obj.GetName().equals("Meteor")) {
                    DrawMeteor(obj);
                }
                if (obj.GetName().equals("Bullet")) {
                    DrawBullet(obj);
                }
            }
        }
    }

    public void ontime() {
        if (Up) {
            World.SendCommand("one", "A");
        }
        if (Down) {
            World.SendCommand("one", "F0");
        }
        if (Left) {
            World.SendCommand("one", "L");
        }
        if (Right) {
            World.SendCommand("one", "R");
        }
        if (Up1) {
            World.SendCommand("two", "A");
        }
        if (Down1) {
            World.SendCommand("two", "F0");
        }
        if (Left1) {
            World.SendCommand("two", "L");
        }
        if (Right1) {
            World.SendCommand("two", "R");
        }
        run = false;
        img.getGraphics().drawImage(backgr, 0, 0, 1024, 1000, this);
        if (game) {
            World.macrostep(0.01);
            if (Math.random() * 100 <= ammeteor) World.AddMeteor(Math.random() * fieldx2 * k, fieldy1 * k, Math.random() * 100 - 50, Math.random() * 500);
            if (Math.random() * 100 <= ammeteor) World.AddMeteor(Math.random() * fieldx2 * k, fieldy2 * k, Math.random() * 100 - 50, -Math.random() * 500);
            if (Math.random() * 100 <= ammeteor) World.AddMeteor(fieldx1 * k, Math.random() * fieldy2 * k, Math.random() * 500, Math.random() * 100 - 50);
            if (Math.random() * 100 <= ammeteor) World.AddMeteor(fieldx2 * k, Math.random() * fieldy2 * k, -Math.random() * 500, Math.random() * 100 - 50);
            DrawAll();
        }
        if (current != null) {
            current.Draw(img, this);
        }
        run = true;
        this.repaint();
    }

    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                GameApplet fr = new GameApplet(new javax.swing.JFrame(), true);
                fr.setSize(1024, 1000);
                fr.addWindowListener(new WindowAdapter() {

                    @Override
                    public void windowClosing(WindowEvent ev) {
                        System.exit(0);
                    }

                    ;
                });
                fr.setAlwaysOnTop(true);
                fr.setVisible(true);
            }
        });
    }

    private org.jdesktop.beansbinding.BindingGroup bindingGroup;

    Timer timer;

    TimerTask timertask;

    boolean Up, Down, Right, Left, Up1, Down1, Right1, Left1, run, game;

    int Pl1Up, Pl2Up, Pl1Down, Pl2Down, Pl1Right, Pl2Right, Pl1Left, Pl2Left;

    Image img, ship, star, planet, meteor, bullet, energy, life;

    Image backgr;

    PhWorld World;

    final double k = 0.25;

    final double ammeteor = 0.2;

    final int fieldx1 = 0, fieldy1 = 0, fieldx2 = 16000, fieldy2 = 12800;

    MenuItem mainmenu, newmenu, escmenu, current;
}
