package Evolution;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JToggleButton;

public class TaskCtrlPanel implements ActionListener {

    private JPanel panel = new JPanel(false);

    private Evolution evo = null;

    private Thread evoThread = null;

    private ImageIcon runIcon = new ImageIcon();

    private JToggleButton runButton = new JToggleButton("RUN");

    private ImageIcon initIcon = new ImageIcon();

    private JButton initButton = new JButton("INIT");

    private ImageIcon stepIcon = new ImageIcon();

    private JButton stepButton = new JButton("STEP");

    private ImageIcon resetIcon = new ImageIcon();

    private JButton resetButton = new JButton("RESET");

    private ImageIcon quitIcon = new ImageIcon();

    private JButton quitButton = new JButton("Quit");

    public TaskCtrlPanel(Evolution evo) {
        this.evo = evo;
        new Thread(this.evo);
        this.evo.setTaskCtrlPanel(this);
        GridBagLayout layout = new GridBagLayout();
        GridBagConstraints c = new GridBagConstraints();
        this.panel.setLayout(layout);
        panel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder("task control"), BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        c.fill = GridBagConstraints.BOTH;
        c.gridx = 0;
        c.gridy = 0;
        layout.setConstraints(initButton, c);
        initButton.setEnabled(true);
        initButton.addActionListener(this);
        panel.add(initButton);
        c.fill = GridBagConstraints.BOTH;
        c.gridx = 1;
        c.gridy = 0;
        layout.setConstraints(resetButton, c);
        resetButton.setEnabled(false);
        resetButton.addActionListener(this);
        panel.add(resetButton);
        c.fill = GridBagConstraints.BOTH;
        c.gridx = 2;
        c.gridy = 0;
        layout.setConstraints(stepButton, c);
        stepButton.setEnabled(false);
        stepButton.addActionListener(this);
        panel.add(stepButton);
        c.anchor = GridBagConstraints.SOUTH;
        c.fill = GridBagConstraints.BOTH;
        c.gridx = 3;
        c.gridy = 0;
        layout.setConstraints(runButton, c);
        runButton.setEnabled(false);
        runButton.addActionListener(this);
        panel.add(runButton);
        c.fill = GridBagConstraints.BOTH;
        c.gridx = 4;
        c.gridy = 0;
        layout.setConstraints(quitButton, c);
        quitButton.addActionListener(this);
        panel.add(quitButton);
    }

    public JPanel getPanel() {
        return this.panel;
    }

    public void actionPerformed(ActionEvent e) {
        int intVal;
        if ((e.getSource() == runButton)) {
            if (runButton.isSelected()) {
                new Thread(this.evo).start();
                resetButton.setEnabled(false);
                stepButton.setEnabled(false);
                System.out.println(": start evolution.....");
            } else {
                this.evo.myStop();
                runButton.setEnabled(false);
                System.out.println(": stop evolution.....");
            }
        }
        if ((e.getSource() == stepButton)) {
            new Thread(this.evo).start();
            resetButton.setEnabled(false);
            runButton.setEnabled(false);
            stepButton.setEnabled(false);
            this.evo.myStop();
            System.out.println(": just one step.....");
        }
        if ((e.getSource() == initButton)) {
            this.evo.evoInit();
            resetButton.setEnabled(true);
            runButton.setEnabled(true);
            stepButton.setEnabled(true);
            quitButton.setEnabled(false);
            initButton.setEnabled(false);
        }
        if ((e.getSource() == resetButton)) {
            Object[] options = { "Yes, new start", "No, continue" };
            int n = JOptionPane.showOptionDialog(this.panel, "A in general 'RESET' ends the current evolution task. Be aware,\n" + "a start of a new one by pressing 'INIT' deletes all old logging files?", "new task?", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[1]);
            if (n == 0) {
                this.evo.evoReset();
                runButton.setEnabled(false);
                stepButton.setEnabled(false);
                initButton.setEnabled(true);
                quitButton.setEnabled(true);
            }
        }
        if ((e.getSource() == quitButton)) {
            Object[] options = { "Yes, quit", "No, continue" };
            int n = JOptionPane.showOptionDialog(this.panel, "Really quit " + "the evolutionary process?", "finished?", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[1]);
            if (n == 0) {
                System.exit(0);
            }
        }
        if ((e.getSource() == this.evo) && (e.getActionCommand() == "END_STEP")) {
            resetButton.setEnabled(true);
            runButton.setEnabled(true);
            stepButton.setEnabled(true);
        }
    }
}
