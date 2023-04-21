package tictactoe.gui;

import javax.swing.ButtonGroup;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JRadioButtonMenuItem;

public class TicTacToeMenu {

    public static JMenuBar getMenu(MenuInput menuInput) {
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        JMenuItem exitMenuItem = new JMenuItem("Exit");
        exitMenuItem.addActionListener(menuInput);
        fileMenu.add(exitMenuItem);
        JMenu gameMenu = new JMenu("Game");
        JMenuItem newGameMenuItem = new JMenuItem("New Game");
        newGameMenuItem.addActionListener(menuInput);
        gameMenu.add(newGameMenuItem);
        ButtonGroup group = new ButtonGroup();
        JMenuItem playAsXMenuItem = new JRadioButtonMenuItem("Play as 'X'");
        playAsXMenuItem.addActionListener(menuInput);
        playAsXMenuItem.setSelected(true);
        gameMenu.add(playAsXMenuItem);
        group.add(playAsXMenuItem);
        JMenuItem playAsOMenuItem = new JRadioButtonMenuItem("Play as 'O'");
        playAsOMenuItem.addActionListener(menuInput);
        gameMenu.add(playAsOMenuItem);
        group.add(playAsOMenuItem);
        JMenu helpMenu = new JMenu("Help");
        helpMenu.add("About");
        menuBar.add(fileMenu);
        menuBar.add(gameMenu);
        menuBar.add(helpMenu);
        return menuBar;
    }
}
