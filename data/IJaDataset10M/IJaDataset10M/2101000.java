package net.sf.refactorit.ui.module.extractmethod;

import net.sf.refactorit.classmodel.BinModifier;
import net.sf.refactorit.classmodel.BinSelection;
import net.sf.refactorit.common.util.Assert;
import net.sf.refactorit.options.GlobalOptions;
import net.sf.refactorit.refactorings.RefactoringStatus;
import net.sf.refactorit.refactorings.extract.ExtractMethod;
import net.sf.refactorit.refactorings.undo.RitUndoManager;
import net.sf.refactorit.source.SourceCoordinate;
import net.sf.refactorit.ui.dialog.RitDialog;
import net.sf.refactorit.ui.module.AbstractRefactorItAction;
import net.sf.refactorit.ui.module.IdeWindowContext;
import net.sf.refactorit.ui.module.RefactorItContext;
import javax.swing.JOptionPane;

/**
 * @author Anton Safonov
 */
public class ExtractMethodAction extends AbstractRefactorItAction {

    public static final String KEY = "refactorit.action.ExtractMethodAction";

    public static final String NAME = "Extract Method";

    private static final int[] modifiers = { BinModifier.PRIVATE, BinModifier.PACKAGE_PRIVATE, BinModifier.PROTECTED, BinModifier.PUBLIC };

    public String getName() {
        return NAME;
    }

    public boolean isReadonly() {
        return false;
    }

    public boolean isMultiTargetsSupported() {
        return false;
    }

    public String getKey() {
        return KEY;
    }

    public boolean isAvailableForType(Class type) {
        return BinSelection.class.equals(type);
    }

    public boolean run(final RefactorItContext context, Object inObject) {
        if (Assert.enabled && !(inObject instanceof BinSelection)) {
            Assert.must(false, "Run ExtractMethod not on selection: " + inObject);
        }
        BinSelection selection = (BinSelection) inObject;
        ExtractMethod extractor = new ExtractMethod(context, selection);
        RefactoringStatus status = extractor.checkPreconditions();
        if (status.isErrorOrFatal()) {
            RitDialog.showMessageDialog(context, status.getAllMessages(), "Not possible to extract method", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (status.isInfoOrWarning()) {
            RitDialog.showMessageDialog(context, status.getAllMessages(), "Warning:", JOptionPane.ERROR_MESSAGE);
        }
        if (!showExtractMethodDialog(context, extractor)) {
            return false;
        }
        RitUndoManager.getCurrentTransaction().setPresentationDetails(extractor.getMethodName() + "( )");
        RefactoringStatus result = extractor.apply();
        if (result.isOk()) {
            context.setState(selection);
            return true;
        } else {
            return false;
        }
    }

    /** Overrides super! */
    public void updateEnvironment(final RefactorItContext context) {
        BinSelection selection = (BinSelection) context.getState();
        SourceCoordinate start = selection.getStartSourceCoordinate();
        context.show(selection.getCompilationUnit(), start.getLine(), GlobalOptions.getOption("source.selection.highlight").equals("true"));
        super.updateEnvironment(context);
    }

    private boolean showExtractMethodDialog(IdeWindowContext context, ExtractMethod extractor) {
        RefactoringStatus status = null;
        int[] mods = new int[modifiers.length];
        System.arraycopy(modifiers, 0, mods, 0, modifiers.length);
        if (extractor.shouldBeStatic() || extractor.mustBeStatic()) {
            for (int j = 0; j < mods.length; j++) {
                mods[j] |= BinModifier.STATIC;
            }
        }
        JExtractDialog dlg = new JExtractDialog(context, mods, mods[0], extractor.getAnalyzedParameters(), extractor.getReturnType(), extractor.getAnalyzedExceptions());
        do {
            dlg.show();
            if (!dlg.isOkPressed()) {
                return false;
            }
            extractor.setMethodName(dlg.getMethodName());
            extractor.setNewParameterNames(dlg.getParamNames());
            extractor.setNewParameterIds(dlg.getParamIds());
            extractor.setModifier(dlg.getModifier());
            status = extractor.checkUserInput();
            if (status.isCancel()) {
                return false;
            }
            if (!status.isOk()) {
                RitDialog.showMessageDialog(context, status.getAllMessages(), "Problem", JOptionPane.ERROR_MESSAGE);
            }
        } while (status.isErrorOrFatal());
        return true;
    }

    public char getMnemonic() {
        return 'X';
    }
}
