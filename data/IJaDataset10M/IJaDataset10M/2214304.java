package observer;

import java.awt.Color;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JTextField;

public class ChangeColorFrame extends Observable {

    public JFrame frame;

    JRadioButton red;

    JRadioButton green;

    JRadioButton blue;

    JLabel label = new JLabel();

    public ChangeColorFrame() {
        frame = new JFrame("ColorSubject");
        frame.setSize(400, 400);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        frame.setLayout(new FlowLayout(FlowLayout.LEFT, 1, 2));
        red = new JRadioButton("red");
        green = new JRadioButton("green");
        blue = new JRadioButton("blue");
        ButtonGroup group = new ButtonGroup();
        group.add(red);
        group.add(green);
        group.add(blue);
        ActionListener al = new ActionListener() {

            public void actionPerformed(ActionEvent ae) {
                JRadioButton radio = (JRadioButton) ae.getSource();
                if (radio == red) {
                    setChanged();
                    notifyObservers("Red");
                } else if (radio == green) {
                    setChanged();
                    notifyObservers("Green");
                } else {
                    setChanged();
                    notifyObservers("Blue");
                }
            }
        };
        red.addActionListener(al);
        green.addActionListener(al);
        blue.addActionListener(al);
        frame.getContentPane().add(red);
        frame.getContentPane().add(green);
        frame.getContentPane().add(blue);
        frame.add(label);
        frame.validate();
    }

    public static void main(String[] args) {
        ChangeColorFrame f = new ChangeColorFrame();
    }
}
