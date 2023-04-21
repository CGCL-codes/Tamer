package net.sourceforge.hlm.gui.math.semantic.placeholder.content;

import net.sourceforge.hlm.gui.managers.*;
import net.sourceforge.hlm.gui.math.*;
import net.sourceforge.hlm.gui.math.generic.*;
import net.sourceforge.hlm.gui.math.semantic.placeholder.*;
import net.sourceforge.hlm.library.contexts.*;
import net.sourceforge.hlm.library.formulae.*;
import net.sourceforge.hlm.visual.templates.*;

public class StructuralFormulaView extends StructuralView<Formula> {

    public StructuralFormulaView(ObjectFrame frame, LibraryObserver libraryManager, StructuralFormula formula) {
        super(frame, libraryManager, formula.getCases());
        this.negationCount = formula.getNegationCount();
    }

    @Override
    protected ObjectView createInnerView(ObjectFrame frame) {
        int negationCount = this.negationCount;
        if (negationCount > 0) {
            GridView gridView = new GridView(frame, 1, 2, 0, 0, 0);
            String symbol = "";
            do {
                symbol += "¬ ";
            } while (--negationCount > 0);
            gridView.getCell(0, 0).setText(symbol, FontStyle.SYMBOL);
            ObjectFrame structuralFrame = gridView.getCell(0, 1);
            structuralFrame.setView(super.createInnerView(frame));
            return gridView;
        } else {
            return super.createInnerView(frame);
        }
    }

    @Override
    protected ObjectView createPlaceholderView(ObjectFrame frame, ContextPlaceholder<Formula> placeholder) {
        return new FormulaPlaceholderView(frame, this.libraryManager, placeholder, false, true);
    }

    private int negationCount;
}
