package net.sf.refactorit.ui.module.common;

import net.sf.refactorit.classmodel.BinClass;
import net.sf.refactorit.classmodel.BinField;
import net.sf.refactorit.classmodel.expressions.BinFieldInvocationExpression;
import net.sf.refactorit.refactorings.Refactoring;
import net.sf.refactorit.refactorings.delegate.AddDelegatesModel;
import net.sf.refactorit.refactorings.delegate.AddDelegatesRefactoring;
import net.sf.refactorit.ui.module.AbstractRefactorItAction;
import net.sf.refactorit.ui.module.JConfirmationDialog;
import net.sf.refactorit.ui.module.RefactorItContext;
import java.awt.Dimension;

/**
 * @author Tonis Vaga
 */
public class AddDelegatesAction extends AbstractRefactorItAction {

    public static final String KEY = "refactorit.generate.delegates";

    public static final String NAME = "Add Delegate Methods";

    private static final Object[] EMPTY_ARRAY = {};

    private static final Class ARRAY_CLASS = EMPTY_ARRAY.getClass();

    public AddDelegatesAction() {
    }

    public String getName() {
        return NAME;
    }

    public String getKey() {
        return KEY;
    }

    public Refactoring createRefactoring(RefactorItContext context, Object object) {
        return new AddDelegatesRefactoring(context, object);
    }

    /**
   * @param context
   * @param parent
   * @param refactoring
   * @return true if continue, false if cancel action
   */
    public boolean readUserInput(Refactoring ref) {
        AddDelegatesRefactoring refactoring = (AddDelegatesRefactoring) ref;
        AddDelegatesModel model = refactoring.getModel();
        RefactorItContext context = refactoring.getContext();
        String helpTopicId = "refact.add_delegates";
        JConfirmationDialog cd = new JConfirmationDialog(this.getName(), "Select target field and methods to delegate", model, context, "", helpTopicId, new Dimension(600, 400), model.isSomethingSelected());
        cd.show();
        if (!cd.isOkPressed()) {
            return false;
        }
        refactoring.setModel((AddDelegatesModel) cd.getModel());
        return true;
    }

    public boolean isMultiTargetsSupported() {
        return true;
    }

    public boolean isAvailableForType(Class type) {
        if (BinClass.class.equals(type) || BinField.class.isAssignableFrom(type) || BinFieldInvocationExpression.class.isAssignableFrom(type)) {
            return true;
        }
        if (ARRAY_CLASS.isAssignableFrom(type)) {
            return true;
        }
        return false;
    }

    public boolean isReadonly() {
        return false;
    }

    public char getMnemonic() {
        return 'D';
    }
}
