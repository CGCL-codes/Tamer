package PrologPlusCG.gui;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;

public class GoToDialog extends Dialog implements ActionListener {

    private static final long serialVersionUID = 3257566196122661686L;

    ImageIcon imageIcoPrlg = null;

    boolean bWasOK;

    JPanel panel1 = new JPanel();

    JPanel panel2 = new JPanel();

    JTextField textField1 = new JTextField(5);

    JButton button1 = new JButton();

    JButton button2 = new JButton();

    JPanel insetsPanel1 = new JPanel();

    JPanel insetsPanel2 = new JPanel();

    JPanel insetsPanel3 = new JPanel();

    JLabel label1 = new JLabel();

    BorderLayout borderLayout1 = new BorderLayout();

    BorderLayout borderLayout2 = new BorderLayout();

    FlowLayout flowLayout1 = new FlowLayout();

    FlowLayout flowLayout2 = new FlowLayout();

    GridLayout gridLayout1 = new GridLayout();

    public GoToDialog(Frame parent) {
        super(parent);
        enableEvents(AWTEvent.WINDOW_EVENT_MASK);
        imageIcoPrlg = new ImageIcon(PrologPlusCGFrame.ppcgLogoPath);
        try {
            jbInit();
        } catch (Exception e) {
            e.printStackTrace();
        }
        pack();
        textField1.requestFocusInWindow();
    }

    private void jbInit() throws Exception {
        this.setTitle("Go to line");
        setResizable(false);
        panel1.setLayout(borderLayout1);
        insetsPanel1.setLayout(flowLayout1);
        gridLayout1.setRows(1);
        gridLayout1.setColumns(1);
        label1.setText("Enter line");
        insetsPanel3.setLayout(flowLayout2);
        insetsPanel3.setBorder(new EmptyBorder(10, 60, 10, 10));
        button1.setText("OK");
        button1.addActionListener(this);
        button2.setText("Cancel");
        button2.addActionListener(this);
        this.add(panel1, null);
        insetsPanel3.add(label1, null);
        insetsPanel3.add(textField1, null);
        panel2.add(insetsPanel3, BorderLayout.WEST);
        insetsPanel1.add(button1, null);
        insetsPanel1.add(button2, null);
        panel1.add(insetsPanel1, BorderLayout.SOUTH);
        panel1.add(panel2, BorderLayout.NORTH);
        textField1.addKeyListener(new KeyAdapter() {

            public void keyReleased(KeyEvent e) {
                goToDialog_keyReleased(e);
            }
        });
    }

    void goToDialog_keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            OK();
        }
    }

    protected void processWindowEvent(WindowEvent e) {
        if (e.getID() == WindowEvent.WINDOW_CLOSING) {
            cancel();
        }
        super.processWindowEvent(e);
    }

    void cancel() {
        bWasOK = false;
        dispose();
    }

    void OK() {
        boolean bDoDispose = true;
        bWasOK = true;
        try {
            int lineno = Integer.parseInt(textField1.getText());
            if (lineno < 1) {
                JOptionPane.showMessageDialog(this, "Number must be greater than 0.");
                bDoDispose = false;
            }
        } catch (NumberFormatException nfe) {
            bDoDispose = false;
            JOptionPane.showMessageDialog(this, "Enter a number.");
        }
        if (bDoDispose) dispose();
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == button2) {
            cancel();
        } else if (e.getSource() == button1) {
            OK();
        }
    }

    public boolean getWasOK() {
        return bWasOK;
    }

    public int getInteger() {
        return Integer.parseInt(textField1.getText());
    }
}
