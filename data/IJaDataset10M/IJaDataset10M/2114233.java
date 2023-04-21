package net.sourceforge.gomoku;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

public final class Gomoku implements GomokuConstants, MoveListener {

    private final JFrame frame;

    private GomokuButton[] buttons;

    private Game game;

    private HumanPlayer human;

    private ComputerPlayer compPlayer;

    private byte rows = Board.ROWS;

    private byte cols = Board.COLS;

    private final String VERSION = "1.0";

    private Gomoku() {
        this.human = new HumanPlayer(USER_PIECE);
        this.compPlayer = new ComputerPlayer(COMPUTER_PIECE);
        JFrame.setDefaultLookAndFeelDecorated(true);
        frame = new JFrame("Gomoku");
        frame.setIconImage(new ImageIcon(getClass().getResource("images/Gomoku.png")).getImage());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(createBoard(), BorderLayout.CENTER);
        frame.setJMenuBar(createMenuBar());
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        newGame();
    }

    void newGame() {
        if (game != null) {
            game.cancel();
        }
        human.startNewGame();
        compPlayer.startNewGame();
        game = new Game(human, compPlayer, rows, cols);
        game.addMoveListener(this);
        for (int i = 0; i < buttons.length; ++i) {
            buttons[i].setPiece(NO_PIECE);
        }
        new Thread(new Runnable() {

            public void run() {
                final byte test = game.play();
                SwingUtilities.invokeLater(new Runnable() {

                    public void run() {
                        switch(test) {
                            case COMPUTER_WIN:
                                JOptionPane.showMessageDialog(frame, "I win, silly human!");
                                break;
                            case USER_WIN:
                                JOptionPane.showMessageDialog(frame, "Arg! I am vanquished.");
                                break;
                            case DRAW:
                                JOptionPane.showMessageDialog(frame, "A draw? How unsatisfying.");
                                break;
                            default:
                                break;
                        }
                    }
                });
            }
        }).start();
    }

    JMenuBar createMenuBar() {
        final JMenuBar menuBar = new JMenuBar();
        JMenu menu;
        JMenuItem menuItem;
        menu = new JMenu("Game");
        menu.setMnemonic(KeyEvent.VK_G);
        menuBar.add(menu);
        menuItem = new JMenuItem("New Game");
        menuItem.setMnemonic(KeyEvent.VK_N);
        menu.add(menuItem);
        menuItem.addActionListener(new ActionListener() {

            public void actionPerformed(final ActionEvent e) {
                newGame();
            }
        });
        menuItem = new JMenuItem("Search Depth...");
        menuItem.setMnemonic(KeyEvent.VK_S);
        menu.add(menuItem);
        menuItem.addActionListener(new ActionListener() {

            public void actionPerformed(final ActionEvent e) {
                if (compPlayer instanceof ComputerPlayer) {
                    final String[] vals = { "1", "2", "3", "4", "5", "6", "7", "8", "9", "10" };
                    final String sd = (String) JOptionPane.showInputDialog(frame, "Enter a search depth.", "Search Depth", JOptionPane.PLAIN_MESSAGE, null, vals, Integer.toString(((ComputerPlayer) compPlayer).getSearchDepth()));
                    if (sd != null) {
                        ((ComputerPlayer) compPlayer).setSearchDepth(Integer.parseInt(sd));
                    }
                }
            }
        });
        menu.addSeparator();
        menuItem = new JMenuItem("Exit");
        menuItem.setMnemonic(KeyEvent.VK_E);
        menuItem.addActionListener(new ActionListener() {

            public void actionPerformed(final ActionEvent e) {
                System.exit(0);
            }
        });
        menu.add(menuItem);
        menu = new JMenu("Help");
        menu.setMnemonic(KeyEvent.VK_H);
        menuBar.add(menu);
        menuItem = new JMenuItem("About Gomoku...");
        menuItem.setMnemonic(KeyEvent.VK_A);
        menu.add(menuItem);
        menuItem.addActionListener(new ActionListener() {

            public void actionPerformed(final ActionEvent e) {
                final JDialog d = new JDialog(frame, "About Gomoku", true);
                final Container p = d.getContentPane();
                JPanel panel = new JPanel(new FlowLayout());
                p.setLayout(new GridLayout(2, 1));
                panel.add(new JLabel(new ImageIcon("Gomoku.png")));
                panel.add(new JLabel("<html>" + "<p>Gomoku " + VERSION + "<p>Copyright (C) 2004-2005 Douglas Ryan Richardson" + "<p>Licensed under the terms of the General Public License" + "<p>Contributions made by Anton Safonov" + "</html>"));
                p.add(panel);
                panel = new JPanel(new FlowLayout());
                final JButton ok = new JButton("OK");
                ok.addActionListener(new ActionListener() {

                    public void actionPerformed(final ActionEvent e) {
                        d.setVisible(false);
                    }
                });
                panel.add(ok);
                p.add(panel);
                d.pack();
                d.setLocationRelativeTo(frame);
                d.setVisible(true);
            }
        });
        return menuBar;
    }

    Component createBoard() {
        final JPanel pane = new JPanel(new GridLayout(rows, cols, 0, 0));
        buttons = new GomokuButton[rows * cols];
        for (int i = 0; i < rows; i++) {
            for (int k = 0; k < cols; k++) {
                int pos = Board.toPosition(i, k);
                buttons[pos] = new GomokuButton(SquareFactory.create(i, k));
                buttons[pos].addActionListener(this.human);
                pane.add(buttons[pos]);
            }
        }
        return pane;
    }

    public void moveMade(final Square move, byte piece) {
        buttons[Board.toPosition(move.getRow(), move.getCol())].setPiece(piece);
    }

    public static void main(final String[] args) {
        new Gomoku();
    }
}
