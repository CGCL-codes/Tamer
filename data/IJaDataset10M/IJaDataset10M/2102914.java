package swing;

import java.awt.BorderLayout;
import java.text.SimpleDateFormat;
import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.text.DateFormatter;

public class TestJFormattedTextField {

    public static void main(String[] argv) {
        JFrame frame = new JFrame();
        frame.setSize(320, 200);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        DateFormatter df = new DateFormatter(new SimpleDateFormat("yyyy/MM/dd"));
        JFormattedTextField tf = new JFormattedTextField(df);
        tf.setSize(300, 24);
        frame.getContentPane().add(tf, BorderLayout.NORTH);
        frame.getContentPane().add(new JButton("abc"), BorderLayout.SOUTH);
    }
}
