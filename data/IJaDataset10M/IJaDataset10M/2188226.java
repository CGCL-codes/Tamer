package org.vikamine.swing.util.formulaparser;

import org.vikamine.kernel.formula.FormulaElement;

/**
 * @author Tobias Vogele
 */
public interface FormulaMessageBox {

    void showFormulaErrorMessage(Exception e);

    void clearFormulaMessage();

    void showFormulaMessage(FormulaElement formula);
}
