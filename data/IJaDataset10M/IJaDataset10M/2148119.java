package ch.usi.inf.pf2.gui.action;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import ch.usi.inf.pf2.circuit.Component;
import ch.usi.inf.pf2.circuit.Input;
import ch.usi.inf.pf2.circuit.Value;
import ch.usi.inf.pf2.gui.CircuitFrame;
import ch.usi.inf.pf2.gui.Model;
import ch.usi.inf.pf2.gui.SelectionListener;
import ch.usi.inf.pf2.time.Schedule;
import ch.usi.inf.pf2.time.SetValueOfInputEvent;

public final class DeleteAction extends AbstractAction {

    private static final long serialVersionUID = -2246877170217385828L;

    private final Model model;

    private final CircuitFrame circuitFrame;

    public DeleteAction(final CircuitFrame circuitFrame, final Model model) {
        this.circuitFrame = circuitFrame;
        this.model = model;
        putValue(NAME, "Delete");
        final ImageIcon icon = new ImageIcon(getClass().getResource("../icons/delete.png"));
        putValue(SMALL_ICON, icon);
        putValue(SHORT_DESCRIPTION, "Delete the selected shapes");
        setEnabled(model.getSelection().getSize() > 0);
        model.addSelectionListener(new SelectionListener() {

            public void selectionChanged() {
                final int count = model.getSelectionSize();
                putValue(SHORT_DESCRIPTION, count == 0 ? "Delete the selected shapes" : ("Delete the " + count + " selected shapes"));
                setEnabled(count > 0);
            }
        });
    }

    public void actionPerformed(final ActionEvent ev) {
        ResetAction.reset(circuitFrame, model);
        final ArrayList<Component> originallySelectedShapes = new ArrayList<Component>();
        for (final Component shape : model.getSelection()) {
            originallySelectedShapes.add(shape);
        }
        for (final Component shape : originallySelectedShapes) {
            model.getSelection().remove(shape);
            model.getCircuit().remove(shape);
        }
        circuitFrame.getEditor().removeAll();
        for (Input input : model.getInputs()) {
            circuitFrame.setEditor(input.getLabel());
        }
        circuitFrame.getEditor().revalidate();
        circuitFrame.getEditor().repaint();
    }
}
