package reTest;

import java.awt.Dimension;
import java.awt.Point;
import javax.swing.*;
import alm.ALMLayout;
import alm.LayoutSpec;

public class Program extends JPanel {

    ALMLayout le = new ALMLayout();

    LayoutSpec ls = new LayoutSpec();

    JButton button1;

    JButton button2;

    JList listBox1;

    JTextArea textBox1;

    public Program() throws Exception {
        setLayout(le);
        invalidate();
        button1 = new JButton();
        button2 = new JButton();
        textBox1 = new JTextArea();
        button1.setLocation(new Point(13, 9));
        button1.setName("button1");
        button1.setSize(new Dimension(82, 37));
        button1.setText("button1");
        button2.setLocation(new Point(13, 52));
        button2.setName("button2");
        button2.setSize(new Dimension(82, 36));
        button2.setText("button2");
        listBox1 = new JList(new String[] { "item1", "item2" });
        listBox1.setLocation(new Point(13, 94));
        listBox1.setName("listBox1");
        listBox1.setSize(new Dimension(82, 95));
        textBox1.setLocation(new Point(101, 9));
        textBox1.setName("textBox1");
        textBox1.setSize(new Dimension(179, 180));
        textBox1.setText("hello");
        add(textBox1);
        add(listBox1);
        add(button2);
        add(button1);
        setLayoutSpec();
        validate();
    }

    public void setLayoutSpec() throws Exception {
        le.recoverLayout(this);
        ls = le.getLayoutSpec();
    }

    public static void main(String[] args) throws Exception {
        JFrame frame = new JFrame("RETest");
        frame.setSize(292, 200);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(new Program());
        frame.setVisible(true);
    }
}
