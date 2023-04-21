package edu.ohiou.lev_neiman.jung.volume_render;

import java.awt.*;
import javax.swing.*;

/**
 * <p>Title: Scientific Volume Rendering</p>
 *
 * <p>Description: Lev Neiman's Summer Job</p>
 *
 * <p>Copyright: Copyright (c) 2008, Lev A. Neiman</p>
 *
 * <p>Company: Dr. Peter Jung</p>
 *
 * @author Lev A. Neiman
 * @version 1.0
 */
public class BottomBar extends JPanel implements Runnable {

    private static class QuestionButton extends JButton {

        ImageIcon image;

        public QuestionButton() {
            super.setPreferredSize(new Dimension(15, 15));
            image = new ImageIcon(BottomBar.class.getResource("question_mark.png"));
        }

        public void paintComponent(Graphics g) {
            Graphics2D g2d = (Graphics2D) g;
            g2d.setColor(g2d.getBackground());
            g2d.fillRect(0, 0, 20, 20);
            image.paintIcon(this, g, 0, 0);
        }
    }

    private static class myLabel extends JLabel {

        public myLabel() {
            super();
        }

        public void paintComponent(Graphics g) {
            Graphics2D g2d = (Graphics2D) g;
            g2d.setColor(g2d.getBackground());
            g2d.fillRect(0, 0, 10000, 50);
            super.paintComponent(g);
        }

        public void omg() {
            paintComponent(this.getGraphics());
        }
    }

    private static class myProgressBar extends JProgressBar {

        public myProgressBar() {
            super(JProgressBar.HORIZONTAL, 0, 100);
        }

        public void omg() {
            super.paintComponent(this.getGraphics());
        }
    }

    static myLabel label_text = new myLabel();

    static myProgressBar progress_bar = new myProgressBar();

    static QuestionButton question_button = new QuestionButton();

    static Thread update_thread;

    public BottomBar() {
        super(new BorderLayout());
        super.setPreferredSize(new Dimension(900, 20));
        label_text.setPreferredSize(new Dimension(400, 20));
        JPanel progress_bar_panel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 2));
        progress_bar.setPreferredSize(new Dimension(150, 15));
        progress_bar_panel.setPreferredSize(new Dimension(500, 20));
        progress_bar_panel.add(progress_bar);
        progress_bar_panel.add(question_button);
        super.add(label_text, BorderLayout.WEST);
        super.add(progress_bar_panel, BorderLayout.EAST);
        label_text.setText(" Please load a data file to visualize it in 3D");
        question_button.setBorder(BorderFactory.createEmptyBorder());
    }

    public static String getLabelText() {
        return label_text.getText().trim();
    }

    public static void setLabelText(String text) {
        label_text.setText(" " + text);
        label_text.omg();
    }

    public static int getProgressVal() {
        return progress_bar.getValue();
    }

    public static void setProgressVal(int val) {
        progress_bar.setValue(val);
        progress_bar.omg();
    }

    public void run() {
        try {
            while (true) {
                progress_bar.repaint();
                label_text.repaint();
                update_thread.sleep(200);
            }
        } catch (Exception exc) {
            exc.printStackTrace();
        }
    }
}
