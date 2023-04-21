package iceGUI;

import main.Output;
import newerGameLogic.Board;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class GamePanel extends JPanel implements ActionListener {

    JMenuBar menuBar;

    JButton flip;

    JButton toggleNotation;

    JButton toggleConsole;

    JLabel opponentsLabel;

    JLabel turn;

    SetupDialog setupDialog;

    ArrayList<JMenuItem> debugLevels;

    ArrayList<JMenuItem> mainMenuItems;

    ArrayList<JMenuItem> viewMenuItems;

    SidePanel northPanel;

    SidePanel eastPanel;

    SidePanel southPanel;

    TextView notation;

    TextView console;

    Board gb;

    BoardArea ba;

    public GamePanel(JMenuBar menuBar) {
        super();
        initialize();
        setupMenus(menuBar);
    }

    private void setupMenus(JMenuBar bar) {
        this.menuBar = bar;
        debugLevels = new ArrayList<JMenuItem>(6);
        for (int i = 0; i < 6; i++) {
            debugLevels.add(new JMenuItem("" + i));
        }
        JMenu mainMenu = new JMenu("Menu");
        menuBar.add(mainMenu);
        mainMenuItems = new ArrayList<JMenuItem>(3);
        mainMenuItems.add(new JMenuItem("New Game"));
        mainMenuItems.add(new JMenuItem("Clear Console"));
        mainMenuItems.add(new JMenuItem("Quit"));
        for (JMenuItem item : mainMenuItems) {
            mainMenu.add(item);
            item.addActionListener(this);
        }
        JMenu viewMenu = new JMenu("View");
        menuBar.add(viewMenu);
        viewMenuItems = new ArrayList<JMenuItem>(3);
        viewMenuItems.add(new JMenuItem("Flip Board"));
        viewMenuItems.add(new JMenuItem("Toggle Notation"));
        viewMenuItems.add(new JMenuItem("Toggle Console"));
        for (JMenuItem menuitem : viewMenuItems) {
            viewMenu.add(menuitem);
            menuitem.addActionListener(this);
        }
        JMenu debugMenu = new JMenu("Debug");
        menuBar.add(debugMenu);
        for (JMenuItem menuitem : debugLevels) {
            debugMenu.add(menuitem);
            menuitem.addActionListener(this);
        }
    }

    private void initialize() {
        setLayout(new BorderLayout());
        eastPanel = new SidePanel();
        opponentsLabel = new JLabel();
        eastPanel.addItem(opponentsLabel, 0, 0);
        turn = new JLabel("White");
        eastPanel.addItem(turn, 0, 1);
        notation = new TextView(30, 10);
        eastPanel.addItem(notation, 0, 2);
        add(eastPanel, BorderLayout.EAST);
        southPanel = new SidePanel();
        int top = southPanel.constraints.insets.top;
        southPanel.constraints.insets.top = 10;
        southPanel.addItem(new JLabel("Console"), 0, 0);
        console = new TextView(5, 40);
        southPanel.constraints.insets.top = top;
        southPanel.addItem(console, 0, 1);
        add(southPanel, BorderLayout.SOUTH);
        ba = new BoardArea();
        add(ba, BorderLayout.CENTER);
    }

    public void switchTurn() {
        if (turn.getText().equals("Black")) turn.setText("White"); else turn.setText("Black");
    }

    public String promotionPrompt() {
        Object[] choices = { "Queen", "Rook", "Bishop", "Knight" };
        String s = (String) JOptionPane.showInputDialog(this, "Choose promotion type:", "Promote to...", JOptionPane.PLAIN_MESSAGE, null, choices, "Queen");
        if (s != null && s.length() > 0) {
            return s;
        }
        return "Queen";
    }

    public void setOpponents(String white, String black) {
        opponentsLabel.setText(white + " vs. " + black);
    }

    public void setupGame(Board gb) {
        this.gb = gb;
        notation.clear();
        turn.setText("White");
        ba.setupBoard(gb);
    }

    protected void paintComponent(Graphics g) {
        Dimension size = this.getSize();
        System.out.println("height:" + size.height + " - width:" + size.width);
        System.out.println(size.height / 16 - 15);
        notation.setRows(size.height / 16 - 15);
        console.setColumns(size.width / 16);
    }

    public void clearConsole() {
        console.clear();
    }

    public void printConsole(String str) {
        console.append(str);
    }

    public void printNotation(String str) {
        notation.append(str);
    }

    public void actionPerformed(ActionEvent e) {
        Object src = e.getSource();
        if (mainMenuItems.contains(src)) {
            JMenuItem selected = (JMenuItem) src;
            if (selected.getText().equals("New Game")) {
                setupDialog = new SetupDialog(this);
                setupDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
                setupDialog.setVisible(true);
                gb.resetBoard();
                setupGame(gb);
            } else if (selected.getText().equals("Clear Console")) {
                console.clear();
            } else if (selected.getText().equals("Quit")) {
                System.exit(0);
            }
        } else if (debugLevels.contains(src)) {
            JMenuItem selected = (JMenuItem) src;
            int level = Integer.parseInt(selected.getText());
            Output.debug("Setting debug level to: " + level, 3);
            Output.setDebugLevel(level);
        } else if (viewMenuItems.contains(src)) {
            JMenuItem selected = (JMenuItem) src;
            if (selected.getText().equals("Flip Board")) {
                ba.flipBoard();
            } else if (selected.getText().equals("Toggle Notation")) {
                eastPanel.setVisible(!eastPanel.isVisible());
            } else if (selected.getText().equals("Toggle Console")) {
                southPanel.setVisible(!southPanel.isVisible());
            }
        }
    }
}
