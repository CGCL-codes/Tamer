package gui.menu.expression;

import core.CompositionFrame;
import gui.forms.ExpressionByTransitionDialog;
import gui.forms.ExpressionByStatesDialog;
import gui.forms.ExpressionByLabelDialog;
import core.specification.compositionexpression.CompositionExpression;
import core.specification.compositionexpression.extensionpoint.ExtensionPoint;
import core.specification.compositionexpression.extensionpoint.TransitionExtensionPoint;
import core.specification.compositionexpression.extensionpoint.StateExtensionPoint;
import core.specification.compositionexpression.extensionpoint.LabelExtensionPoint;
import javax.swing.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;

/**
 * Created by IntelliJ IDEA.
 * User: Siamak
 * Date: 26-Apr-2008
 * Time: 5:28:18 PM
 * To change this template use File | Settings | File Templates.
 */
public class EditExpressionMenuItem extends JMenuItem implements ActionListener, MouseListener {

    CompositionFrame frame;

    public EditExpressionMenuItem(CompositionFrame frame) {
        super("Edit Expression");
        this.frame = frame;
        this.addActionListener(this);
    }

    public void actionPerformed(ActionEvent e) {
        editExpression();
    }

    public void mouseClicked(MouseEvent e) {
        if (e.getClickCount() == 2) {
            editExpression();
        }
    }

    private void editExpression() {
        int index = frame.getAssList().getSelectedIndex();
        if (index < 0) return;
        CompositionExpression expression = (CompositionExpression) frame.getSpecification().getAssignmentList().get(index);
        ExtensionPoint extPoint = expression.getOperator().getFirstExtensionPoint();
        if (extPoint == null) return;
        if (extPoint instanceof TransitionExtensionPoint) {
            ExpressionByTransitionDialog nad = new ExpressionByTransitionDialog(frame);
            nad.setExpression(expression);
            expression = nad.getCompositionExpression();
        } else if (extPoint instanceof StateExtensionPoint) {
            ExpressionByStatesDialog nad = new ExpressionByStatesDialog(frame);
            nad.setExpression(expression);
            expression = nad.getCompositionExpression();
        } else if (extPoint instanceof LabelExtensionPoint) {
            ExpressionByLabelDialog nad = new ExpressionByLabelDialog(frame);
            nad.setExpression(expression);
            expression = nad.getCompositionExpression();
        }
        if (expression != null) {
            frame.getSpecification().setAssignment(index, expression);
            frame.updateAssignmentList();
        }
    }

    public void mousePressed(MouseEvent e) {
    }

    public void mouseReleased(MouseEvent e) {
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }
}
