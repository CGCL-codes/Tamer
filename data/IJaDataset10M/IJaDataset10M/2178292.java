package foa.properties.sequences;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import foa.flow.FlowDirector;

/**
 * @author Fabio Giannetti
 * @version 0.0.1
 */
public class UpdateContentSequenceReferenceAction extends AbstractAction {

    private FlowDirector flowDirector;

    private Container c;

    private JDialog getName;

    public UpdateContentSequenceReferenceAction(Container frame, FlowDirector flowDirector) {
        super("Update", null);
        this.flowDirector = flowDirector;
        c = frame;
    }

    public void actionPerformed(ActionEvent e) {
        String contSeqRef = flowDirector.getContSeqRefAttributes();
        if (contSeqRef.equals("")) {
            JOptionPane.showMessageDialog(c, "Undefined content sequence reference !", "Incomplete Content Sequence", JOptionPane.ERROR_MESSAGE);
        } else {
            flowDirector.isContentSelRefDialogOpened = false;
            flowDirector.isAValidContentSeqRef = true;
            if (flowDirector.isANewContSeqRef) {
                flowDirector.addContentSequenceReference();
            } else {
                flowDirector.updateContentSequenceReference();
            }
            ((Window) c).dispose();
        }
    }
}
