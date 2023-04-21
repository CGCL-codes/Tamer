package grameball;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *Tale Classe realizza il frame principale. Il Thread della palla, controlla la
 * vincita, permette l'utilizzo della tastiera per il movimento mediante i tasti "w","a","s","d",
 * permette di mettere in pausa caffe il gioco.....(da descrivere internamente)
 * @author Meco.Klodian (Granad)
 */
public class VisualFrame implements KeyListener {

    /**
     * True gioco in pausa mediante l'utilizo del pulsante "Pausa", false gioco non in pausa.
     */
    public static boolean pause = false;

    /**
     * Tale variabile statica di tipo booleana, viene utilizzata per conoscere l'eventuale utilizzo della tastiera per lo
     * spostamento del mirino, per bloccare lo spostamento con il mouse, in quanto se premuti entrambi, e senza questa variabile di controllo che l'impedisse
     * si otterrebbero piu Thread di movimento per il mirino, finalizzando un aumento della sua velocita ed una degenerazione nel movimento tale.
     */
    static boolean inUseKey = false;

    /**
     * Frame
     */
    private JFrame frm;

    /**
     * (JLabel) Mirino.
     */
    public static Figure x;

    /**
     * (JLabel) Palla.
     */
    private Figure o;

    /**
     * Dimmensione del Frame.
     */
    public static final Dimension XYFrm = new Dimension(370, 500);

    /**
     * Larghezza della pista da gioco.
     */
    public static int WP = XYFrm.width;

    /**
     * Altezza della pista da gioco
     */
    public static int HP = XYFrm.height - 200;

    /**
     * Punto di locazione nella pista del mirino.
     */
    public static Point xy1 = new Point(WP / 2, HP / 2);

    /**
    * Punto di locazione nella pista della palla..
    */
    public static Point xy2 = new Point(WP - 30, HP - 25);

    /**
     * Pista del gico
     */
    private JPanel pan;

    /**
     * Pulsante di pausa.
     */
    private JButton pausa;

    /**
     * Controlla l'inserimento o meno del turbo alla palla.
     */
    private boolean turbo;

    /**
     * Thread per il movimento della palla nel asse delle ordinate.
     */
    private MoveXO uno;

    /**
     * Thread per il movimento della palla nel asse delle ascisse.
     */
    private MoveXO due;

    /**
     * Sfondo della pista da gioco, impostato su una JLabel.
     */
    private JLabel sfondo;

    /**
     * pulsante per l'attivazione del turbo o disativvazione.
     */
    private JButton turboB;

    /**
     * Thread utilizzato dai tasti w,a,s,d per il movimento del mirino.
     */
    private Thread move;

    public VisualFrame() {
        impFrm();
        turbo = false;
        JPanel _ = new JPanel();
        _.setOpaque(true);
        _.setSize(XYFrm);
        _.setBackground(new Color(26, 8, 91));
        _.setLayout(null);
        frm.setContentPane(_);
        setPush();
        impFigAndPiano();
        setMoveToO();
        pulsantePausa();
        setTurbo();
        frm.setVisible(true);
    }

    /**
     * Imposta la grafica del frame, il mirino, la palla aggiungendoli al frame.
     */
    private void impFigAndPiano() {
        sfondo = new JLabel();
        sfondo.setSize(WP - 8, HP);
        sfondo.setLocation(0, 0);
        sfondo.setIcon(createImageIcon("pinguini.jpg"));
        sfondo.setToolTipText("Klodian.Granad");
        x = new Figure(true, 20);
        o = new Figure(false, 20);
        x.setLocation(xy1);
        o.setLocation(xy2);
        pan = new JPanel();
        pan.setSize(WP, HP);
        pan.setLocation(1, 1);
        pan.setLayout(null);
        pan.add(x);
        pan.add(o);
        pan.add(sfondo);
        frm.add(pan);
    }

    /**
     * Imposta il Frame principale.
     */
    private void impFrm() {
        frm = new JFrame("GameBall By Granad");
        frm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frm.setSize(XYFrm);
        frm.setLocation(200, 200);
        frm.setResizable(false);
        frm.setAlwaysOnTop(true);
        frm.addKeyListener(this);
        frm.setFocusable(true);
    }

    /**
     * Presa dell'immagine da un percorso.
     * @param percorso Indica il precorso di dove si trova l'immagine.
     * @return ImageIcon.
     */
    protected static ImageIcon createImageIcon(String percorso) {
        java.net.URL img = grameball.VisualFrame.class.getResource(percorso);
        if (img != null) return new ImageIcon(img); else return null;
    }

    /**
     * Imposta i pulsanti per il movimento, utilzzati dal mouse, con la sola neccessita di averlo sopra per
     * ottenere l'attivazione del movimento del mirino.
     */
    private void setPush() {
        frm.add(new GrandiosMove(50, 30, "up", new Point(XYFrm.width / 2 - 25, XYFrm.height - 200 + 45)));
        frm.add(new GrandiosMove(50, 30, "down", new Point(XYFrm.width / 2 - 25, XYFrm.height - 140 + 50 + 15)));
        frm.add(new GrandiosMove(50, 30, "left", new Point(XYFrm.width / 2 - 55 - 10, XYFrm.height - 200 + 45 + 40)));
        frm.add(new GrandiosMove(50, 30, "right", new Point(XYFrm.width / 2 + 5 + 10, XYFrm.height - 200 + 45 + 40)));
    }

    /**
     * Crea un nuovo Thread per il movimento della palla, la quale utilizza una stateggia
     * studiata con cura, la quale rende il suo movimento del tutto casuale, ed innoltre in grado di comprendere
     * l'avvicinamento del mirino, cercando di schivarla, tentando disperatamente di scappare.
     */
    private void setMoveToO() {
        class ThreadO extends Thread {

            /**
             *      Movimento :
             * Si ha un arrai di stringhe, in cui sono inserite varie combinazione di movimento
             * le quali vengono scelte ogni volta che il mirino è vicino alla palla in un modo intelligente
             * e in modo casuale quando sono distanti.
             * Il movimento viene cambiato anche ogni qual volta sia finita la sua vita, cioe quando la palla ha effettuato il numero
             * di passi per quel tipo di direzione.
             *      passi1, passi2 :
             * Indicano il numero di passi che la palla deve fare nelle due direzioni.
             *      comeMuoverlo :
             * E l'indice utlilizzato per scegliere la combinaizone di movimento da impostare alla palla.
             *      finish :
             * True quando la palla è vicina al mirino, oppure quando ha finito il numero di passi, ed abilita il cambio di direzione
             * ed il numero di passi da effettuare per il nuovo coambio, mediante modalita random o prestabilita a secoda della vicinanaza o meno del mirino
             * alla palla.
             *      uno, due :
             * I due Thread per il movimento della palla, uno per le ascise l'altro per le ordinate.
             *
             *
             * Si rimane all'interno del metodo run fino a quando non si vince, cioe fino a quando il rinino non becca la palla.
             *
             * Una volta vinto si può solo ammirare la grafica e chidere il programma.
             */
            @Override
            public void run() {
                int passi1 = 0, passi2 = 0, comeMuoverlo = 0;
                String movimento[][] = { { "up", "right" }, { "up", "left" }, { "down", "right" }, { "down", "left" } };
                boolean finish = true;
                uno = new MoveXO(movimento[comeMuoverlo][0], xy2, o, 35);
                due = new MoveXO(movimento[comeMuoverlo][1], xy2, o, 35);
                uno.start();
                due.start();
                while (!win()) {
                    if (finish) {
                        finish = false;
                        comeMuoverlo = (int) (Math.random() * 4);
                        if (distanz() < 50) {
                            if (GrandiosMove.direzione.equals("up")) comeMuoverlo = 0;
                            if (GrandiosMove.direzione.equals("down")) comeMuoverlo = 3;
                            if (GrandiosMove.direzione.equals("left")) comeMuoverlo = 1;
                            if (GrandiosMove.direzione.equals("right")) comeMuoverlo = 0;
                            if (GrandiosMove.direzione.equals("")) comeMuoverlo = 1;
                        }
                        passi1 = (int) (Math.random() * 150);
                        passi2 = (int) (Math.random() * 200);
                        uno.setDir(movimento[comeMuoverlo][0]);
                        due.setDir(movimento[comeMuoverlo][1]);
                    }
                    finish = (uno.getHowStep() >= passi1 && due.getHowStep() >= passi2) || distanz() < 50;
                }
                if (win()) {
                    uno.stop();
                    due.stop();
                    sfondo.setIcon(createImageIcon("winOcchio.jpg"));
                    pause = true;
                    pausa.setEnabled(false);
                    turboB.setEnabled(false);
                }
            }
        }
        new ThreadO().start();
    }

    /**
     * Controlla se il mirino ha colpito la palla.
     * @return boolean, true se il mirino è all'interno della palla, false se non lo è.
     */
    private boolean win() {
        return distanz() <= 9;
    }

    /**
     * Calcola la distanza tra mirino e palla.
     * @return Distanza mirino palla. (double)
     */
    private double distanz() {
        return Math.sqrt(Math.pow(xy1.x - xy2.x, 2) + Math.pow(xy1.y - xy2.y, 2));
    }

    /**
     * Permette il controllo del movimento di entrambi gli oggetti, fermandoli
     * o riattivandoli in caso siano precedentemente fermati. E rendendo indisponibile il loro movimento
     * mediante tastiera o mouse.
     */
    private void pulsantePausa() {
        pausa = new JButton("Pausa");
        pausa.setSize(WP - 8, 25);
        pausa.setLocation(1, HP + 2);
        class AntionPause implements ActionListener {

            public void actionPerformed(ActionEvent e) {
                if (pause) {
                    pausa.setText("Pausa");
                    pause = false;
                    if (!win()) {
                        uno.resume();
                        due.resume();
                        pausa.setFocusable(false);
                    }
                } else {
                    pausa.setText("Avvia");
                    pause = true;
                    if (!win()) {
                        uno.suspend();
                        due.suspend();
                    }
                }
            }
        }
        ActionListener list3 = new AntionPause();
        pausa.addActionListener(list3);
        frm.add(pausa);
    }

    /**
     * Tale metodo permette l'inserimento del tubo alla palla, modificandole il valore dello sleep nel thread rendendola
     * piu veloce nel movimento, diminuendo lo sleep di 20, in alternativa se disattivato riporta tutto allo stato normale.
     */
    private void setTurbo() {
        turboB = new JButton("Turno Off");
        turboB.setSize(100, 25);
        turboB.setLocation(1, HP + 2 + 30);
        turboB.setFocusable(false);
        class AntionTurbo implements ActionListener {

            public void actionPerformed(ActionEvent e) {
                if (!turbo) {
                    turbo = true;
                    turboB.setText("Turbo On");
                    uno.setSpeed(uno.getSpeed() - 20);
                    due.setSpeed(due.getSpeed() - 20);
                } else {
                    turbo = false;
                    turboB.setText("Turbo Off");
                    uno.setSpeed(uno.getSpeed() + 20);
                    due.setSpeed(due.getSpeed() + 20);
                }
            }
        }
        ActionListener list4 = new AntionTurbo();
        turboB.addActionListener(list4);
        frm.add(turboB);
    }

    /**
     * Ridefinizione del metodo KeyTyped, il quale intercetta i tasti w,s,a,d
     * ed attiva il movimento del mirino, controllando se nello stesso tempo è stato
     * attivato quello del mouse e studiando esclusivamente i stasti interessati.
     * @param e Evento Key.
     */
    public void keyTyped(KeyEvent e) {
        if (!GrandiosMove.inUseMouse && !pause) {
            inUseKey = true;
            String is;
            switch(Character.toLowerCase(e.getKeyChar())) {
                case 'w':
                    is = "up";
                    break;
                case 's':
                    is = "down";
                    break;
                case 'a':
                    is = "left";
                    break;
                case 'd':
                    is = "right";
                    break;
                default:
                    is = "null";
                    break;
            }
            if (((move == null)) && !is.equals("null")) {
                GrandiosMove.direzione = is;
                move = new MoveXO(is, VisualFrame.xy1, VisualFrame.x, 15);
                move.start();
            }
        }
    }

    public void keyPressed(KeyEvent e) {
    }

    /**
     * Ritorna il controllo per la pressione di una altro tasto o del mouse.
     * Blocca innolte il tread del movimento per la direzione precedentemente impostata.
     * @param e Evento Key.
     */
    public void keyReleased(KeyEvent e) {
        if (move != null && move.isAlive()) {
            GrandiosMove.direzione = "";
            move.stop();
            move = null;
            inUseKey = false;
        }
    }
}
