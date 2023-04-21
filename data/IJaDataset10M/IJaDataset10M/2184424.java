package temp;

import java.awt.Color;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class SimpleTest extends JFrame {

    public SimpleTest(String titulo) {
        this.setTitle(titulo);
        this.setSize(300, 300);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLayout(new GridLayout(3, 3));
        JLabel numero0 = new JLabel("0");
        numero0.setHorizontalAlignment(JLabel.CENTER);
        numero0.setBorder(BorderFactory.createLineBorder(Color.black));
        JLabel numero1 = new JLabel("1");
        numero1.setHorizontalAlignment(JLabel.CENTER);
        JLabel numero2 = new JLabel("2");
        numero2.setHorizontalAlignment(JLabel.CENTER);
        JLabel numero3 = new JLabel("3");
        numero3.setHorizontalAlignment(JLabel.CENTER);
        JLabel numero4 = new JLabel("4");
        numero4.setHorizontalAlignment(JLabel.CENTER);
        JLabel numero5 = new JLabel("5");
        numero5.setHorizontalAlignment(JLabel.CENTER);
        JLabel numero6 = new JLabel("6");
        numero6.setHorizontalAlignment(JLabel.CENTER);
        JLabel numero7 = new JLabel("7");
        numero7.setHorizontalAlignment(JLabel.CENTER);
        JLabel numero8 = new JLabel("8");
        numero8.setHorizontalAlignment(JLabel.CENTER);
        this.add(numero0);
        this.add(numero2);
        this.add(numero3);
        this.add(numero4);
        this.add(numero5);
        this.add(numero6);
        this.add(numero7);
        this.add(numero8);
        this.add(numero1);
    }

    public static void main(String[] args) {
        SimpleTest st = new SimpleTest("Simple Test!");
        st.setVisible(true);
    }
}
