package net.mjrz.fm.ui.panels.schedule;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.Date;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import net.mjrz.scheduler.task.BasicSchedule;
import net.mjrz.scheduler.task.Schedule;
import net.mjrz.scheduler.task.Schedule.RepeatType;

public class MinuteSchedulePanel extends JPanel {

    private static final long serialVersionUID = 1L;

    private JTextField num;

    public MinuteSchedulePanel() {
        super();
        initialize();
    }

    private void initialize() {
        setLayout(new GridBagLayout());
        num = new JTextField(5);
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.VERTICAL;
        c.gridx = 0;
        c.gridy = 0;
        c.ipadx = 10;
        add(new JLabel("Every "), c);
        c.fill = GridBagConstraints.VERTICAL;
        c.gridx = 1;
        c.gridy = 0;
        add(num, c);
        c.fill = GridBagConstraints.VERTICAL;
        c.gridx = 2;
        c.gridy = 0;
        add(new JLabel(" minutes(s)"), c);
        num.setText("2");
    }

    public boolean isValidSchedule() {
        String val = num.getText();
        if (val == null || val.length() == 0) return false;
        try {
            int i = Integer.parseInt(val);
            System.out.println(i);
            return i > 0;
        } catch (Exception e) {
            return false;
        }
    }

    public Schedule getSchedule(Date st, Date en) throws Exception {
        BasicSchedule s = new BasicSchedule(st, en);
        Integer val = Integer.parseInt(num.getText());
        s.setRepeatType(RepeatType.MINUTE, val);
        return s;
    }
}
