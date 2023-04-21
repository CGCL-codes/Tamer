package lyuzkez;

import java.awt.*;
import java.awt.event.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;

/**
 * Kontroller osztály az ablak megtestesítője, ő rajzolja ki a figuráinkat.
 * @author Lacika
 */
public class Kontroller extends JFrame implements WindowListener, ActionListener, KeyListener, Runnable {

    private lyuzkez.Rajzter rajzter;

    private lyuzkez.StatuszSav statuszSav;

    private lyuzkez.Jatek jatek;

    public int fazis;

    private final int fIdo = 100;

    private Thread idozito;

    private MessageBox messagebox;

    /**
     * Új játék kezdését indító fv.
     */
    public void ujJatek() {
        this.jatek.ujrakezdJatek();
        if (idozito.isAlive()) {
            idozito.interrupt();
        }
        idozito.start();
    }

    /**
     * Visszaadja az aktuális játékot
     * @return a játék
     */
    public Jatek getJatek() {
        return jatek;
    }

    public Kontroller() {
        super("LyuzKez&LyuzLab");
        this.setSize(800, 600);
        this.setResizable(false);
        this.addKeyListener(this);
        this.addWindowListener(this);
        this.setLayout(new BorderLayout());
        JMenuBar menulista = new JMenuBar();
        JMenu jatekMenu = new JMenu("Játék");
        JMenuItem ujjatek = new JMenuItem("Új játék");
        ujjatek.setActionCommand("uj");
        ujjatek.addActionListener(this);
        JMenuItem sugo = new JMenuItem("Súgó");
        sugo.setActionCommand("sugo");
        sugo.addActionListener(this);
        JMenuItem kilep = new JMenuItem("Kilép");
        kilep.setActionCommand("kilep");
        kilep.addActionListener(this);
        menulista.add(jatekMenu);
        jatekMenu.add(ujjatek);
        jatekMenu.add(sugo);
        jatekMenu.add(kilep);
        this.setJMenuBar(menulista);
        this.rajzter = new Rajzter(this);
        this.add("Center", this.rajzter);
        this.statuszSav = new StatuszSav(this);
        this.add("South", this.statuszSav);
        messagebox = new MessageBox(this);
        this.messagebox.addWindowListener(messagebox);
        this.jatek = Jatek.getJatek();
        this.fazis = 0;
        idozito = new Thread(this);
    }

    public static void main(String[] args) {
        Kontroller kontroller = new Kontroller();
        kontroller.setVisible(true);
    }

    /**
     * Játék vége fv.
     */
    public void vege() {
        idozito.interrupt();
    }

    /**
     * Fázis visszaadása
     * @return fázis (0-4)
     */
    public int getFazis() {
        return fazis;
    }

    /**
     * A barlang cseleksziket periodikusan hívó függvény.
     */
    public void cselekszik() {
        jatek.getBarlang().cselekszik();
    }

    /**
     * Implementált absztrakt metódusok....
     * @param e
     */
    public void windowOpened(WindowEvent e) {
    }

    public void windowClosing(WindowEvent e) {
        System.exit(0);
    }

    public void windowClosed(WindowEvent e) {
    }

    public void windowIconified(WindowEvent e) {
    }

    public void windowDeiconified(WindowEvent e) {
    }

    public void windowActivated(WindowEvent e) {
    }

    public void windowDeactivated(WindowEvent e) {
    }

    public void actionPerformed(ActionEvent e) {
        System.out.println(e.getActionCommand());
        if (e.getActionCommand().compareTo("uj") == 0) {
            this.ujJatek();
        } else if (e.getActionCommand().compareTo("kilep") == 0) {
            this.vege();
            System.exit(0);
        } else if (e.getActionCommand().compareTo("sugo") == 0) {
            this.messagebox.setVisible(true);
        }
    }

    public void keyTyped(KeyEvent e) {
    }

    public void keyPressed(KeyEvent e) {
        if (this.jatek.getBarlang() == null || this.jatek.getBarlangSzam() >= this.jatek.getOsszBarlangSzam()) {
            return;
        }
        switch(e.getKeyChar()) {
            case 'w':
            case 'W':
                jatek.getLyuzlab().setIrany(Irany.FEL);
                break;
            case 's':
            case 'S':
                jatek.getLyuzlab().setIrany(Irany.LE);
                break;
            case 'q':
            case 'Q':
                jatek.getLyuzlab().setIrany(Irany.BALRAFEL);
                break;
            case 'a':
            case 'A':
                jatek.getLyuzlab().setIrany(Irany.BALRALE);
                break;
            case 'd':
            case 'D':
                jatek.getLyuzlab().setIrany(Irany.JOBBRALE);
                break;
            case 'e':
            case 'E':
                jatek.getLyuzlab().setIrany(Irany.JOBBRAFEL);
                break;
            case 'r':
            case 'R':
                jatek.getLyuzlab().robbanoszertRak();
                break;
            case '8':
            case 'U':
            case 'u':
                jatek.getLyuzkez().setIrany(Irany.FEL);
                break;
            case 'j':
            case 'J':
            case '5':
                jatek.getLyuzkez().setIrany(Irany.LE);
                break;
            case 'z':
            case 'Z':
            case '7':
                jatek.getLyuzkez().setIrany(Irany.BALRAFEL);
                break;
            case 'h':
            case 'H':
            case '4':
                jatek.getLyuzkez().setIrany(Irany.BALRALE);
                break;
            case 'k':
            case 'K':
            case '6':
                jatek.getLyuzkez().setIrany(Irany.JOBBRALE);
                break;
            case 'i':
            case 'I':
            case '9':
                jatek.getLyuzkez().setIrany(Irany.JOBBRAFEL);
                break;
            case 'o':
            case 'O':
            case '0':
                jatek.getLyuzkez().robbanoszertRak();
                break;
        }
    }

    public void keyReleased(KeyEvent e) {
    }

    /**
     * Proto kiírBarlangPro fv-e a teszteléshez.
     */
    public void kiirBarlangPro(Barlang b) {
        String[][] elems = b.getBarlangKep();
        System.out.print(";");
        for (int i = 0; i < elems.length; ++i) {
            for (int j = 0; j < elems[i].length; j++) {
                if (j % 2 == 0) System.out.printf("/%3s\\", elems[i][j]); else System.out.printf("___");
            }
            System.out.print("\n;");
            System.out.print("\\");
            for (int j = 0; j < elems[i].length; j++) {
                if (j % 2 != 0) System.out.printf("/%3s\\", elems[i][j]); else System.out.printf("___");
            }
            System.out.print("/\n;");
        }
        System.out.print("\n");
    }

    public void run() {
        while (idozito == Thread.currentThread()) {
            if ((this.fazis = ++this.fazis % 5) == 0) {
                this.cselekszik();
                this.statuszSav.Frissit();
            }
            this.rajzter.repaint();
            try {
                Thread.sleep(fIdo);
            } catch (InterruptedException ex) {
            }
        }
    }
}
