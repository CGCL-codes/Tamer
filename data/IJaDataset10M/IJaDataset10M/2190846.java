package gui.menu;

import javax.swing.*;
import core.CompositionFrame;
import core.specification.Specification;
import core.composition.Composer;
import core.modelcheck.ModelChecker;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import gui.environment.Universe;
import automata.fsa.FiniteStateAutomaton;

public class RunMenu extends JMenu implements ActionListener {

    private JMenuItem compose;

    private JMenuItem withSPIN;

    private JMenuItem withUPPAAL;

    private CompositionFrame frame;

    public RunMenu(CompositionFrame frame) {
        super("Run");
        compose = new JMenuItem("Compose");
        compose.addActionListener(this);
        this.add(compose);
        JMenu modelCheck = new JMenu("Model Verification");
        withSPIN = new JMenuItem("with SPIN");
        withSPIN.addActionListener(this);
        this.add(withSPIN);
        withUPPAAL = new JMenuItem("with UPPAAL");
        withUPPAAL.addActionListener(this);
        this.add(withUPPAAL);
        modelCheck.add(withSPIN);
        modelCheck.add(withUPPAAL);
        this.add(modelCheck);
        this.frame = frame;
    }

    public void actionPerformed(ActionEvent e) {
        JMenuItem item = (JMenuItem) e.getSource();
        if (item == compose) {
            Universe.setPrevAutomatonUpdate(false);
            Specification specification = frame.getSpecification();
            Composer composer = new Composer(specification, frame);
            composer.recompose();
            composer.composeNewAssignments();
            Universe.setPrevAutomatonUpdate(true);
        } else if (item == withSPIN) {
            try {
                FiniteStateAutomaton automaton = (FiniteStateAutomaton) frame.getActiveUsecase();
                ModelChecker modelChecker = new ModelChecker(automaton);
                modelChecker.modelCheck();
            } catch (Exception e1) {
            }
        } else if (item == withUPPAAL) {
        }
    }
}
