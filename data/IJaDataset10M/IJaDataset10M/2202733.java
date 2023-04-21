package boulderdashmvc;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 *
 * @author Gergo
 */
public class boulderdashControl extends Frame {

    boulderdashView view;

    boulderdashModel model;

    /** Creates a new instance of boulderdashControl */
    public boulderdashControl() {
        super("Teszt");
        model = new boulderdashModel();
        view = new boulderdashView(model);
        addWindowListener(new WindowAdapter() {

            public void windowClosing(WindowEvent e) {
                System.exit(-1);
            }
        });
        view.buttonStart.addActionListener(new ButtonListener());
        view.buttonNewGame.addActionListener(new ButtonListener());
        view.buttonHelp.addActionListener(new ButtonListener());
        view.buttonQuit.addActionListener(new ButtonListener());
        view.jatekter.addKeyListener(new MyKeyListener());
    }

    class ButtonListener implements ActionListener {

        ButtonListener() {
            System.out.println("Megnyomva");
        }

        public void actionPerformed(ActionEvent e) {
            if (e.getActionCommand().equals("New Game")) {
                System.out.println("New Game has been clicked");
            }
            if (e.getActionCommand().equals("Restart level")) {
                System.out.println("Restart level has been clicked");
            }
            if (e.getActionCommand().equals("Help")) {
                System.out.println("Help has been clicked");
            }
            if (e.getActionCommand().equals("Quit")) {
                System.out.println("Quit has been clicked");
            }
        }
    }

    class MyKeyListener implements KeyListener {

        /** Handle the key typed event from the text field. */
        public void keyTyped(KeyEvent e) {
        }

        /** Handle the key-pressed event from the text field. */
        public void keyPressed(KeyEvent e) {
            switch(e.getKeyChar()) {
                case 'i':
                    System.out.println("North East ");
                    {
                        int i = model.getX();
                        int j = model.getY();
                        i += 10;
                        j -= 10;
                        System.out.println(i + " " + j);
                        model.setX(i);
                        model.setY(j);
                        view.display();
                        break;
                    }
                case 'k':
                    System.out.println("East ");
                    {
                        int i = model.getX();
                        i += 20;
                        System.out.println(i);
                        model.setX(i);
                        view.display();
                        break;
                    }
                case 'm':
                    System.out.println("South East ");
                    {
                        int i = model.getX();
                        int j = model.getY();
                        i += 10;
                        j += 10;
                        System.out.println(i + " " + j);
                        model.setX(i);
                        model.setY(j);
                        view.display();
                        break;
                    }
                case 'n':
                    System.out.println("South West ");
                    {
                        int i = model.getX();
                        int j = model.getY();
                        i -= 10;
                        j += 10;
                        System.out.println(i + " " + j);
                        model.setX(i);
                        model.setY(j);
                        view.display();
                        break;
                    }
                case 'h':
                    System.out.println("West ");
                    {
                        int i = model.getX();
                        i -= 20;
                        System.out.println(i);
                        model.setX(i);
                        view.display();
                        break;
                    }
                case 'u':
                    System.out.println("North West ");
                    {
                        int i = model.getX();
                        int j = model.getY();
                        i -= 10;
                        j -= 10;
                        System.out.println(i + " " + j);
                        model.setX(i);
                        model.setY(j);
                        view.display();
                        break;
                    }
                default:
                    System.out.println("ismeretlen");
            }
        }

        /** Handle the key-released event from the text field. */
        public void keyReleased(KeyEvent e) {
        }
    }
}
