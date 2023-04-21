package ch.unisi.inf.pfii.teammagenta.jcalc.gui;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.Window;

public class Board {

    private JFrame frame;

    private JTextField display;

    private Interface show;

    public Board(Interface inter) {
        show = inter;
        makeFrame();
    }

    public void makeFrame() {
        frame = new JFrame("CalCulator");
        makeMenuBar(frame);
    }

    public void openFile() {
    }

    public void quit() {
        System.exit(0);
    }

    private void makeMenuBar(JFrame frame) {
        JMenuBar menubar = new JMenuBar();
        frame.setJMenuBar(menubar);
        JMenu fileMenu = new JMenu("File");
        menubar.add(fileMenu);
        JMenuItem openItem = new JMenuItem("Open");
        openItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                openFile();
            }
        });
        fileMenu.add(openItem);
        JMenuItem quitItem = new JMenuItem("Quit");
        quitItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                quit();
            }
        });
        fileMenu.add(quitItem);
        JPanel contentPane = (JPanel) frame.getContentPane();
        contentPane.setLayout(new BorderLayout(8, 8));
        contentPane.setBorder(new EmptyBorder(4, 4, 4, 4));
        display = new JTextField();
        contentPane.add(display, BorderLayout.NORTH);
        JPanel buttonPanel = new JPanel(new GridLayout(5, 7));
        String[] buttonNames = { "(", ")", "%", "C", "+/-", "/", "*", "1/x", "x^2", "x^3", "7", "8", "9", "-", "x!", "square root", "log", "4", "5", "6", "+", "sin", "cos", "tag", "1", "2", "3", "=", "pi", "e", "ln", "0", ".", "=" };
        for (String s : buttonNames) Buttonadd(buttonPanel, s);
        Buttonadd(buttonPanel, "(");
        Buttonadd(buttonPanel, ")");
        Buttonadd(buttonPanel, "%");
        Buttonadd(buttonPanel, "C");
        Buttonadd(buttonPanel, "+/-");
        Buttonadd(buttonPanel, "/");
        Buttonadd(buttonPanel, "*");
        Buttonadd(buttonPanel, "1/x");
        Buttonadd(buttonPanel, "x^2");
        Buttonadd(buttonPanel, "x^3");
        Buttonadd(buttonPanel, "7");
        Buttonadd(buttonPanel, "8");
        Buttonadd(buttonPanel, "9");
        Buttonadd(buttonPanel, "-");
        Buttonadd(buttonPanel, "x!");
        Buttonadd(buttonPanel, "square root");
        Buttonadd(buttonPanel, "log");
        Buttonadd(buttonPanel, "4");
        Buttonadd(buttonPanel, "5");
        Buttonadd(buttonPanel, "6");
        Buttonadd(buttonPanel, "+");
        Buttonadd(buttonPanel, "sin");
        Buttonadd(buttonPanel, "cos");
        Buttonadd(buttonPanel, "tag");
        Buttonadd(buttonPanel, " 1");
        Buttonadd(buttonPanel, "2");
        Buttonadd(buttonPanel, "3");
        Buttonadd(buttonPanel, "=");
        Buttonadd(buttonPanel, " pi");
        Buttonadd(buttonPanel, "e");
        Buttonadd(buttonPanel, "ln");
        Buttonadd(buttonPanel, "0");
        Buttonadd(buttonPanel, ".");
        Buttonadd(buttonPanel, "=");
        contentPane.add(buttonPanel, BorderLayout.CENTER);
        frame.pack();
        frame.setVisible(true);
    }

    public void actionPerformed(ActionEvent event) {
        String command = event.getActionCommand();
        if (command.equals("0") || command.equals("1") || command.equals("2") || command.equals("3") || command.equals("4") || command.equals("5") || command.equals("6") || command.equals("7") || command.equals("8") || command.equals("9")) {
            int number = Integer.parseInt(command);
            show.numberPressed(number);
        } else if (command.equals("+")) show.Plus(); else if (command.equals("-")) show.minus(); else if (command.equals("=")) show.equals(); else if (command.equals("C")) show.clear(); else if (command.equals("sin")) show.sin(); else if (command.equals("cos")) show.cos(); else if (command.equals("tang")) show.tang(); else if (command.equals("(")) show.openparentez(); else if (command.equals(")")) show.closeparentez(); else if (command.equals("%")) show.percent(); else if (command.equals("+/-")) show.log(); else if (command.equals("/")) show.log(); else if (command.equals("*")) show.multi(); else if (command.equals("1/x")) show.dividebyx(); else if (command.equals("x^2")) show.power2(); else if (command.equals("x^3")) show.power3(); else if (command.equals("square root")) show.squareroot(); else if (command.equals("e")) show.constante(); else if (command.equals("pi")) show.constant(); else if (command.equals("ln")) show.ln(); else if (command.equals("rad")) show.rad(); else if (command.equals("x!")) show.factorial(); else if (command.equals("=")) show.equal();
    }

    private void Buttonadd(Container container, final String text) {
        JButton button = new JButton(text);
        button.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                display.setText(display.getText() + text);
            }
        });
        container.add(button);
    }

    public JPanel createTextPanel() {
        JPanel text = new JPanel();
        JTextArea textArea = new JTextArea(1, 15);
        textArea.setEditable(false);
        text.add(textArea);
        return text;
    }

    public void setVisible(boolean visible) {
        frame.setVisible(visible);
    }
}
