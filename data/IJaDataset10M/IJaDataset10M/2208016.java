package ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

public class MainMenu extends JFrame {

    /**
	 * generated ID
	 */
    private static final long serialVersionUID = -1199792392732674767L;

    /**
	 * static instance for Singleton 
	 */
    private static MainMenu instance = null;

    private JFrame window = null;

    private MainMenu() {
        window = this;
        window.setTitle("Flughafen Informationssystem");
        createComponents();
    }

    public static MainMenu getInstance() {
        if (instance == null) {
            instance = new MainMenu();
        }
        return instance;
    }

    private void createComponents() {
        final JMenuItem startenAction = new JMenuItem("Starten");
        startenAction.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Starten geklickt - öffne Dialog");
            }
        });
        final JMenuItem landenAction = new JMenuItem("Landen");
        landenAction.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Landen geklickt - öffne Dialog");
            }
        });
        final JMenuItem positionAction = new JMenuItem("Position");
        positionAction.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Position geklickt - wechlse Ansicht");
            }
        });
        final JMenuItem logout = new JMenuItem("Logout");
        logout.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Logut User");
            }
        });
        final JMenuItem beenden = new JMenuItem("Beenden");
        beenden.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Logut User und Programm schliessen");
            }
        });
        JMenu menuAktion = new JMenu("Aktion");
        JMenu menuAnsicht = new JMenu("Ansicht");
        JMenu menuBeenden = new JMenu("Beenden");
        menuAktion.add(startenAction);
        menuAktion.add(landenAction);
        menuAktion.add(positionAction);
        menuBeenden.add(logout);
        menuBeenden.add(beenden);
        JMenuBar menuBar = new JMenuBar();
        menuBar.add(menuAktion);
        menuBar.add(menuAnsicht);
        menuBar.add(menuBeenden);
        window.setJMenuBar(menuBar);
        window.pack();
    }
}
