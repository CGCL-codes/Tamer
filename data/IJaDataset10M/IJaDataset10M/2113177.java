package dialogs;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import main.GlobalMethods;
import main.Icons;

/**
 *
 * @author Raed Shomali
 */
public class EquationEditorDialog extends JDialog implements ActionListener {

    JTextField aTextField = new JTextField("2.94");

    JTextField bTextField = new JTextField("0.91");

    JTextField cTextField = new JTextField("3.67");

    JTextField dTextField = new JTextField("0.28");

    JButton applyButton = new JButton("Apply");

    JButton resetButton = new JButton("Reset");

    JButton closeButton = new JButton("Close");

    public EquationEditorDialog(JFrame frame) {
        super(frame);
        this.setModal(true);
        this.setTitle("Equation Editor");
        GlobalMethods.updateStatusBar("Done.");
        applyButton.addActionListener(this);
        resetButton.addActionListener(this);
        closeButton.addActionListener(this);
        applyButton.setFocusable(false);
        resetButton.setFocusable(false);
        closeButton.setFocusable(false);
        applyButton.setIcon(Icons.SAVE_ICON);
        resetButton.setIcon(Icons.RESET_ICON);
        closeButton.setIcon(Icons.CLOSE_ICON);
        JLabel exponentEquationLabel = new JLabel("Exponent Equation");
        JLabel effortEquationLabel = new JLabel("Effort Equation");
        JLabel scheduleEquationLabel = new JLabel("Schedule Equation");
        exponentEquationLabel.setHorizontalAlignment(JLabel.CENTER);
        effortEquationLabel.setHorizontalAlignment(JLabel.CENTER);
        scheduleEquationLabel.setHorizontalAlignment(JLabel.CENTER);
        exponentEquationLabel.setForeground(Color.BLUE);
        effortEquationLabel.setForeground(Color.BLUE);
        scheduleEquationLabel.setForeground(Color.BLUE);
        aTextField.setPreferredSize(new Dimension(40, 25));
        bTextField.setPreferredSize(new Dimension(40, 25));
        cTextField.setPreferredSize(new Dimension(40, 25));
        dTextField.setPreferredSize(new Dimension(40, 25));
        aTextField.setFont(new Font("courier", 0, 12));
        bTextField.setFont(new Font("courier", 0, 12));
        cTextField.setFont(new Font("courier", 0, 12));
        dTextField.setFont(new Font("courier", 0, 12));
        JPanel exponentEquationPanel = new JPanel();
        JLabel exponentPart1 = new JLabel("E = ");
        JLabel exponentPart2 = new JLabel(" + 0.01 (SF1 + ... + SF5)");
        exponentEquationPanel.add(exponentPart1);
        exponentEquationPanel.add(bTextField);
        exponentEquationPanel.add(exponentPart2);
        JPanel effortEquationPanel = new JPanel();
        JLabel effortPart1 = new JLabel("PM = EM x ... x EM17 x ");
        JLabel effortPart2 = new JLabel(" x (Size) ^ B + (ASLOC x (AT / 100) / ATPROD)");
        effortEquationPanel.add(effortPart1);
        effortEquationPanel.add(aTextField);
        effortEquationPanel.add(effortPart2);
        JPanel scheduleEquationPanel = new JPanel();
        JLabel schedulePart1 = new JLabel("TDEV = [ ");
        JLabel schedulePart2 = new JLabel(" x PM ^ ( ");
        JLabel schedulePart3 = new JLabel(" + 0.2 x (E - B) ) ] x (SCED% / 100)");
        scheduleEquationPanel.add(schedulePart1);
        scheduleEquationPanel.add(cTextField);
        scheduleEquationPanel.add(schedulePart2);
        scheduleEquationPanel.add(dTextField);
        scheduleEquationPanel.add(schedulePart3);
        JPanel southPanel = new JPanel();
        southPanel.add(applyButton);
        southPanel.add(resetButton);
        southPanel.add(closeButton);
        JSeparator separator = new JSeparator();
        this.setLayout(null);
        this.add(exponentEquationLabel);
        this.add(exponentEquationPanel);
        this.add(effortEquationLabel);
        this.add(effortEquationPanel);
        this.add(scheduleEquationLabel);
        this.add(scheduleEquationPanel);
        this.add(separator);
        this.add(southPanel);
        exponentEquationLabel.setBounds(0, 10, 500, 30);
        exponentEquationPanel.setBounds(0, 40, 500, 30);
        effortEquationLabel.setBounds(0, 90, 500, 30);
        effortEquationPanel.setBounds(0, 120, 500, 30);
        scheduleEquationLabel.setBounds(0, 170, 500, 30);
        scheduleEquationPanel.setBounds(0, 200, 500, 30);
        separator.setBounds(0, 265, 500, 25);
        southPanel.setBounds(0, 270, 500, 50);
        this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        this.setLocation(this.getOwner().getX() + 100, this.getOwner().getY() + 100);
        this.setResizable(false);
        this.setSize(500, 345);
        this.setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == applyButton) {
        } else if (e.getSource() == resetButton) {
        } else {
            this.dispose();
        }
    }
}
