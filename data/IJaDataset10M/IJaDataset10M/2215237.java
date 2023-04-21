package gui.forms;

import automata.State;
import automata.fsa.FiniteStateAutomaton;
import core.CompositionFrame;
import core.modelcheck.SPINVerifier;
import gui.environment.Universe;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class ModelCheckingDialog extends JDialog {

    private boolean answer = false;

    JPanel panel = new JPanel(null);

    JLabel liberateLabel = new JLabel();

    JTextArea parsedAutomatonTextArea = new JTextArea();

    DefaultListModel propertiesModel = new DefaultListModel();

    JList propertiesList = new JList(propertiesModel);

    JScrollPane propertiesScrollPane;

    JButton checkButton = new JButton();

    JButton noButton = new JButton();

    JButton pmButton = new JButton();

    CompositionFrame frame;

    FiniteStateAutomaton automaton;

    State state;

    public ModelCheckingDialog(final CompositionFrame frame, final FiniteStateAutomaton automaton) {
        super(frame, "Model Checking", true);
        this.frame = frame;
        this.automaton = automaton;
        setSize(400, 625);
        setLocationRelativeTo(frame);
        this.getContentPane().setLayout(null);
        panel.setBounds(new Rectangle(1, 2, 400, 625));
        panel.setLayout(null);
        try {
            String dirName = Universe.getWorkingDirectory();
            String fileName = "pan_in";
            BufferedReader in = new BufferedReader(new FileReader(fileName));
            String str;
            while ((str = in.readLine()) != null) {
                parsedAutomatonTextArea.append(str + "\n");
            }
            in.close();
        } catch (IOException e) {
        }
        propertiesScrollPane = new JScrollPane(parsedAutomatonTextArea);
        propertiesScrollPane.setBounds(10, 20, 370, 500);
        Border backwardBorder = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED, Color.gray, Color.lightGray);
        Border assBorder = new TitledBorder(backwardBorder, "SPIN Model");
        propertiesScrollPane.setBorder(assBorder);
        liberateLabel.setText("Model Check this automaton?");
        liberateLabel.setBounds(new Rectangle(10, 530, 250, 15));
        checkButton.setBounds(new Rectangle(25, 555, 75, 25));
        checkButton.addActionListener(new AbstractAction() {

            public void actionPerformed(ActionEvent evt) {
                SPINVerifier verifier = new SPINVerifier(frame, automaton);
                int errors = verifier.verify();
                if (errors == 0) JOptionPane.showMessageDialog(frame, "Property satisfies by the model!"); else {
                    String error = "";
                    String[] errorTrail = verifier.debug();
                    for (String str : errorTrail) error = error + str + ", ";
                    JOptionPane.showMessageDialog(frame, "Property not satisfied by the model.\nViolating scenario:\n" + error);
                }
                answer = true;
            }
        });
        checkButton.setText("Check");
        noButton.setBounds(new Rectangle(115, 555, 75, 25));
        noButton.addActionListener(new AbstractAction() {

            public void actionPerformed(ActionEvent evt) {
                answer = false;
                setVisible(false);
            }
        });
        noButton.setText("Cancel");
        pmButton.setBounds(new Rectangle(205, 555, 150, 25));
        pmButton.addActionListener(new AbstractAction() {

            public void actionPerformed(ActionEvent evt) {
                PropertyManagerDialog pmd = new PropertyManagerDialog(frame);
                answer = false;
            }
        });
        pmButton.setText("Property Manager");
        this.getContentPane().add(panel, null);
        panel.add(propertiesScrollPane);
        panel.add(liberateLabel);
        panel.add(checkButton);
        panel.add(noButton);
        panel.add(pmButton);
        setVisible(true);
    }

    public boolean getAnswer() {
        return answer;
    }

    public String getSpecName() {
        return null;
    }

    public CompositionFrame getFrame() {
        return frame;
    }
}
