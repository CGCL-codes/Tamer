package com.iver.cit.gvsig.project.documents.table.operators;

import org.apache.bsf.BSFException;
import org.apache.bsf.BSFManager;
import com.iver.andami.PluginServices;
import com.iver.cit.gvsig.ExpressionFieldExtension;
import com.iver.cit.gvsig.project.documents.table.AbstractOperator;
import com.iver.cit.gvsig.project.documents.table.IOperator;

/**
 * @author Vicente Caballero Navarro
 */
public class Ceil extends AbstractOperator {

    public String addText(String s) {
        return toString() + "(" + s + ")";
    }

    public String toString() {
        return "ceil";
    }

    public void eval(BSFManager interpreter) throws BSFException {
        interpreter.exec(ExpressionFieldExtension.JYTHON, null, -1, -1, "def ceil(value):\n" + "  import java.lang.Math\n" + "  return java.lang.Math.ceil(value)");
    }

    public boolean isEnable() {
        return (getType() == IOperator.NUMBER);
    }

    public String getDescription() {
        return PluginServices.getText(this, "parameter") + ": " + PluginServices.getText(this, "numeric_value") + "\n" + PluginServices.getText(this, "returns") + ": " + PluginServices.getText(this, "numeric_value") + "\n" + PluginServices.getText(this, "description") + ": " + "Returns the smallest (closest to negative infinity) double value that is not less than the argument and is equal to a mathematical integer. Special cases:\n" + "* If the argument value is already equal to a mathematical integer, then the result is the same as the argument.\n" + "* If the argument is NaN or an infinity or positive zero or negative zero, then the result is the same as the argument.\n" + "* If the argument value is less than zero but greater than -1.0, then the result is negative zero.\n";
    }
}
