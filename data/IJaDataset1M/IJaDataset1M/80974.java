package org.servingMathematics.mqat.ui.WizardEditor.VariableTypePanel;

import javax.swing.JPanel;
import org.imsglobal.qti.dom.Expression;

/**
 * TODO Description
 *
 * @author <a href="mailto:j.kahovec@imperial.ac.uk">Jakub Kahovec</a>
 *
 */
public abstract class AbstractVariableTypePanel extends JPanel {

    Expression expression;

    public AbstractVariableTypePanel(Expression templateValue) {
        this.expression = templateValue;
    }

    public abstract Expression getExpression();
}
