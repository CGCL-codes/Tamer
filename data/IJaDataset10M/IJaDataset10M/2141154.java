package org.plazmaforge.studio.reportdesigner.commands;

import org.eclipse.gef.commands.Command;
import org.plazmaforge.studio.reportdesigner.model.DesignContainer;
import org.plazmaforge.studio.reportdesigner.model.TextField;
import org.plazmaforge.studio.reportdesigner.model.data.Expression;
import org.plazmaforge.studio.reportdesigner.requests.DropExpressionRequest;

/** 
 * The Command adds TextField to container 
 * 
 * @author Oleh Hapon
 * $Id: DropFieldCommand.java,v 1.3 2010/11/14 11:28:21 ohapon Exp $
 */
public class DropFieldCommand extends Command {

    private final Expression expression;

    private final DesignContainer container;

    private final TextField textField;

    private final String calculation;

    public DropFieldCommand(TextField textField, Expression expression, DesignContainer container) {
        this(textField, expression, null, container);
    }

    public DropFieldCommand(TextField textField, Expression expression, String calculation, DesignContainer container) {
        super(DropExpressionRequest.TYPE);
        this.textField = textField;
        this.expression = expression;
        this.calculation = calculation;
        this.container = container;
    }

    /**
     * @see org.eclipse.gef.commands.Command#execute()
     */
    public void execute() {
        textField.setExpression(expression);
        container.addChild(textField);
    }

    /**
     * @see org.eclipse.gef.commands.Command#undo()
     */
    public void undo() {
        if (textField != null) {
            container.removeChild(textField);
        }
    }
}
