package pubweb.supernode.sched.dhs.demo;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class NewWorkerDlg extends JDialog implements ActionListener {

    private static final long serialVersionUID = 1L;

    private JTextField guid;

    private JTextField weight;

    private boolean approved = false;

    public NewWorkerDlg(JFrame owner, String title) {
        super(owner, title, true);
        JButton button;
        JPanel panel, subpanel;
        setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        setResizable(false);
        getContentPane().setLayout(new BorderLayout());
        panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        subpanel = new JPanel(new GridLayout(2, 1, 0, 5));
        subpanel.setPreferredSize(new Dimension(100, 40));
        subpanel.add(new JLabel("Guid:"));
        subpanel.add(new JLabel("Weight:"));
        panel.add(subpanel);
        subpanel = new JPanel(new GridLayout(2, 1, 0, 5));
        subpanel.setPreferredSize(new Dimension(225, 40));
        guid = new JTextField();
        guid.setToolTipText("A guid identification for the worker");
        subpanel.add(guid);
        weight = new JTextField();
        weight.setToolTipText("A guid identification for the worker");
        subpanel.add(weight);
        panel.add(subpanel);
        getContentPane().add(panel, BorderLayout.CENTER);
        panel = new JPanel(new GridLayout(1, 1, 10, 0));
        panel.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
        button = new JButton("Random");
        button.addActionListener(this);
        button.setActionCommand("random");
        panel.add(button);
        JLabel spacer = new JLabel();
        panel.add(spacer);
        button = new JButton("Cancel");
        button.addActionListener(this);
        button.setActionCommand("cancel");
        panel.add(button);
        button = new JButton("OK");
        button.addActionListener(this);
        button.setActionCommand("ok");
        panel.add(button);
        getRootPane().setDefaultButton(button);
        getContentPane().add(panel, BorderLayout.SOUTH);
        pack();
        setLocation(owner.getSize().width / 2 - getSize().width / 2 + owner.getX(), owner.getSize().height / 2 - getSize().height / 2 + owner.getY());
        fillRandom();
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("cancel")) {
            approved = false;
            setVisible(false);
        } else if (e.getActionCommand().equals("ok")) {
            boolean error = false;
            try {
                double d = new Double(weight.getText());
                if (d >= 0) {
                    approved = true;
                    setVisible(false);
                } else error = true;
            } catch (NumberFormatException e1) {
                error = true;
            }
            if (error) JOptionPane.showMessageDialog(this, "Weight has to be a positive double value.", "Error", JOptionPane.ERROR_MESSAGE);
        } else if (e.getActionCommand().equals("random")) {
            fillRandom();
        }
    }

    private void fillRandom() {
        char[] hex = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
        String s = "" + hex[((int) (Math.random() * 15)) + 1];
        for (int i = 1; i < 40; i++) {
            s = s + hex[((int) (Math.random() * 16))];
        }
        guid.setText(s);
        weight.setText(new Double(Math.random() * 25).toString());
    }

    public double getWeight() {
        return new Double(weight.getText()).doubleValue();
    }

    public String getGuid() {
        return guid.getText();
    }

    public boolean isApproved() {
        return approved;
    }
}
