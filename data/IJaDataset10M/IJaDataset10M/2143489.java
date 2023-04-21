package cn.heapstack.sudoku.gui.prototype;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.Timer;
import cn.heapstack.sudoku.gui.ImageBorder;

public class DemoConfigPanel extends JPanel implements ActionListener {

    private static final long serialVersionUID = 4100509515122280282L;

    private JPanel timeCountingPanel = new JPanel();

    private JPanel assistantPanel = new JPanel();

    private JPanel miscPanel = new JPanel();

    private Timer t;

    private JCheckBox cb = new JCheckBox("��ʾ����");

    private JCheckBox cbDrawLineHelp = new JCheckBox("���߸���");

    private JButton btnReGenerate = new JButton("���³���");

    private JButton btnAutoSolve = new JButton("�����");

    private JLabel label = new JLabel("�Ѷ�ϵ��:");

    private JTextField textFiledDifficulty = new JTextField("5", 3);

    public DemoConfigPanel() {
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        buildTimeCountingPanel();
        buildAssistantPanel();
        buildMiscPanel();
        setSubPanelsSize();
    }

    private void buildTimeCountingPanel() {
        timeCountingPanel.setBorder(ImageBorder.generateDefaultImageBorder());
        timeCountingPanel.setLayout(new GridBagLayout());
        final JLabel labelTime = new JLabel();
        t = new Timer(1000, new ActionListener() {

            int i = 0;

            public void actionPerformed(ActionEvent e) {
                i++;
                int s = i % 60;
                int m = (i / 60) % 60;
                int h = (i / 3600) % 60;
                String cost = "";
                if (h < 10) {
                    cost = cost.concat("0");
                }
                cost = cost.concat(String.valueOf(h));
                cost = cost.concat(":");
                if (m < 10) {
                    cost = cost.concat("0");
                }
                cost = cost.concat(String.valueOf(m));
                cost = cost.concat(":");
                if (s < 10) {
                    cost = cost.concat("0");
                }
                cost = cost.concat(String.valueOf(s));
                labelTime.setText(cost);
            }
        });
        t.setRepeats(true);
        t.start();
        timeCountingPanel.add(labelTime);
        this.add(timeCountingPanel);
    }

    private void setSubPanelsSize() {
        Dimension preferredSize = new Dimension(180, 180);
        this.assistantPanel.setPreferredSize(preferredSize);
        this.miscPanel.setPreferredSize(preferredSize);
        this.timeCountingPanel.setPreferredSize(preferredSize);
    }

    private void buildAssistantPanel() {
        JPanel subPanel = new JPanel();
        subPanel.setLayout(new GridLayout(3, 3));
        ButtonGroup g = new ButtonGroup();
        for (int i = 1; i < 10; i++) {
            String actionCommand = "help_draw_" + String.valueOf(i);
            JToggleButton b = new JToggleButton(String.valueOf(i));
            b.setActionCommand(actionCommand);
            b.addActionListener(this);
            subPanel.add(b);
            g.add(b);
        }
        assistantPanel.setBorder(ImageBorder.generateDefaultImageBorder());
        assistantPanel.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(2, 2, 2, 2);
        c.gridx = 0;
        c.gridy = 0;
        assistantPanel.add(cb, c);
        c.gridx = 0;
        c.gridy = 1;
        assistantPanel.add(cbDrawLineHelp, c);
        c.gridx = 0;
        c.gridy = 2;
        assistantPanel.add(subPanel, c);
        this.add(assistantPanel);
    }

    private void buildMiscPanel() {
        textFiledDifficulty.setToolTipText("������1-10֮�����ֵ");
        miscPanel.setBorder(BorderFactory.createTitledBorder("�������"));
        miscPanel.setBorder(ImageBorder.generateDefaultImageBorder());
        miscPanel.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(4, 2, 4, 2);
        c.gridx = 0;
        c.gridy = 0;
        miscPanel.add(label, c);
        c.gridx = 1;
        c.gridy = 0;
        miscPanel.add(textFiledDifficulty, c);
        c.gridx = 0;
        c.gridy = 1;
        c.gridwidth = 2;
        miscPanel.add(btnReGenerate, c);
        c.gridx = 0;
        c.gridy = 2;
        c.gridwidth = 2;
        miscPanel.add(btnAutoSolve, c);
        this.add(miscPanel);
    }

    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        if (command.startsWith("help_draw_")) {
            int number = Integer.parseInt(command.substring(command.length() - 1));
            System.out.println(command + number);
        }
    }

    /**
	 * @param args
	 */
    public static void main(String[] args) {
        JFrame frame = new JFrame("Demo Config Panel");
        DemoConfigPanel p = new DemoConfigPanel();
        frame.add(p);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
