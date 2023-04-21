package clustermines.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;

public class NumberPanel extends JPanel implements ActionListener {

    private JLabel[] digits;

    private Timer timer;

    private int seconds;

    NumberPanel() {
        digits = new JLabel[3];
        for (int i = digits.length - 1; i >= 0; i--) {
            digits[i] = new JLabel();
            digits[i].setPreferredSize(SweeperPanel.getIconSize("time-"));
            this.add(digits[i]);
        }
        this.setBorder(BorderFactory.createLoweredBevelBorder());
        this.setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
        reset();
    }

    void reset() {
        this.stop();
        seconds = 0;
        this.setValue(seconds);
    }

    void setValue(int value) {
        int digit;
        boolean minus;
        if (value < 0) {
            minus = true;
            value = -value;
            if (value > 99) {
                value = 99;
            }
        } else {
            minus = false;
            if (value > 999) {
                value = 999;
            }
        }
        for (int i = 0; i < digits.length + (minus ? -1 : 0); i++) {
            digit = value % 10;
            digits[i].setIcon(SweeperPanel.getIcon("time" + digit));
            value /= 10;
        }
        if (minus) {
            digits[digits.length - 1].setIcon(SweeperPanel.getIcon("time-"));
        }
    }

    void start() {
        if (timer == null) {
            timer = new Timer(0, this);
            timer.setDelay(1000);
        }
        timer.start();
    }

    void stop() {
        if (timer != null) {
            timer.stop();
            timer = null;
        }
    }

    boolean isRunning() {
        return (timer != null && timer.isRunning());
    }

    int getTime() {
        return seconds;
    }

    public void actionPerformed(ActionEvent e) {
        this.setValue(++seconds);
    }
}
