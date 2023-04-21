package regominer2;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.util.LinkedList;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import regominer2.Hexagon;
import regominer2.Board;

class MyMouseListener implements java.awt.event.MouseListener {

    public MyMouseListener() {
    }

    public void mouseClicked(java.awt.event.MouseEvent e) {
    }

    public void mouseReleased(MouseEvent e) {
        myButton b = (myButton) e.getSource();
        System.out.println("click " + b.getRow() + ", " + b.getNumber());
        System.out.println(e.paramString());
        if ((e.getModifiersEx() & MouseEvent.BUTTON1_DOWN_MASK) > 0 && (e.getModifiersEx() & MouseEvent.BUTTON3_DOWN_MASK) > 0) {
            RegomineR2.board.Bclick(RegomineR2.board.findTile(b.getRow(), b.getNumber()));
        } else if (e.getButton() == MouseEvent.BUTTON1) {
            RegomineR2.board.Lclick(RegomineR2.board.findTile(b.getRow(), b.getNumber()));
        } else if (e.getButton() == MouseEvent.BUTTON3) {
            RegomineR2.board.Rclick(RegomineR2.board.findTile(b.getRow(), b.getNumber()));
        }
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }

    public void mousePressed(MouseEvent e) {
    }
}

public class RegomineR2 extends javax.swing.JFrame {

    public static Board board;

    public static int tileSize = 30;

    public static int margin = 0;

    public static MyMouseListener mouseListener;

    public static int size = 25;

    private LinkedList<JPanel> panel;

    private LinkedList<myButton> buttons;

    private myButton buttonsArray[][];

    public myButton getButton(int row, int number) {
        return buttonsArray[row][number];
    }

    public void createGUI() {
        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
        setSize(600, 500);
        panel = new LinkedList();
        for (int i = 0; i < size + 1; i++) {
            int width = size - (i + 1) % 2;
            JPanel p = new JPanel();
            p.setLayout(new FlowLayout());
            if (i < size) {
                for (int j = 0; j < width; j++) {
                    myButton b = new myButton(" ", i, j);
                    b.addMouseListener(mouseListener);
                    p.add(b);
                    buttons.add(b);
                    buttonsArray[i][j] = b;
                }
            }
            this.add(p);
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        }
    }

    public RegomineR2() {
        super();
        buttons = new LinkedList();
        buttonsArray = new myButton[size][size];
        mouseListener = new MyMouseListener();
        createGUI();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                RegomineR2 inst = new RegomineR2();
                board = new Board(size, inst);
                inst.setLocationRelativeTo(null);
                inst.setVisible(true);
            }
        });
    }
}

class myButton extends JToggleButton {

    private int row;

    private int number;

    myButton(String label, int row, int number) {
        super(label);
        this.setRow(row);
        this.setNumber(number);
        this.setMaximumSize(new Dimension(10, 10));
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getRow() {
        return row;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getNumber() {
        return number;
    }
}
