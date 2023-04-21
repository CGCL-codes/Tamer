package view;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import controler.Preparing;

public class GameMenu extends JMenuBar {

    public JMenuItem newGame;

    public JMenuItem exit;

    public JMenuItem getExtensions;

    public NewGameDialog nGame;

    private MainWindow mainWindow;

    public GameMenu(MainWindow m) {
        this.mainWindow = m;
        this.setBackground(Color.white);
        Font fontMenu = new Font("Arial", Font.PLAIN, 12);
        Font fontItem = new Font("Arial", Font.PLAIN, 12);
        JMenu file = new JMenu("Fichier");
        file.setFont(fontMenu);
        newGame = new JMenuItem("Nouvelle partie");
        newGame.setFont(fontItem);
        newGame.setBackground(Color.white);
        exit = new JMenuItem("Quitter");
        exit.setFont(fontItem);
        exit.setBackground(Color.white);
        file.add(newGame);
        file.add(exit);
        JMenu extensions = new JMenu("Extensions");
        extensions.setFont(fontMenu);
        extensions.setBackground(Color.white);
        getExtensions = new JMenuItem("Gerer extensions");
        getExtensions.setFont(fontItem);
        getExtensions.setBackground(Color.white);
        extensions.add(getExtensions);
        JMenu help = new JMenu("A propos");
        help.setFont(fontMenu);
        JMenuItem rulesGame = new JMenuItem("Regles de jeu");
        rulesGame.setFont(fontItem);
        rulesGame.setBackground(Color.white);
        help.add(rulesGame);
        this.add(file);
        this.add(extensions);
        this.add(help);
        newGame.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                nGame = new NewGameDialog(mainWindow);
                Preparing preparing = new Preparing(nGame);
                nGame.setVisible(true);
            }
        });
    }
}
