package net.sf.refactorit.ui.module.move;

import net.sf.refactorit.classmodel.BinType;
import net.sf.refactorit.common.util.Assert;
import net.sf.refactorit.commonIDE.IDEController;
import net.sf.refactorit.refactorings.RefactoringStatus;
import net.sf.refactorit.refactorings.movetype.MoveType;
import net.sf.refactorit.ui.JProgressDialog;
import net.sf.refactorit.ui.SearchingInterruptedException;
import net.sf.refactorit.ui.module.AbstractRefactorItAction;
import net.sf.refactorit.ui.module.RefactoringStatusViewer;
import net.sf.refactorit.ui.module.RefactorItActionUtils;
import net.sf.refactorit.ui.module.RefactorItContext;

/**
 * @author Anton Safonov
 */
public class MoveTypeAction extends AbstractRefactorItAction {

    public static final String KEY = "refactorit.action.MoveTypeAction";

    public static final String NAME = "Move Class";

    RefactoringStatus status;

    public boolean isMultiTargetsSupported() {
        return true;
    }

    public String getName() {
        return NAME;
    }

    public char getMnemonic() {
        return 'C';
    }

    public String getKey() {
        return KEY;
    }

    public boolean isReadonly() {
        return false;
    }

    /**
   * Module execution.
   *
   * @param context context of the refactoring (also provides us current Project)
   * @param object  Bin object to operate
   * @return  false if nothing changed, true otherwise
   */
    public boolean run(final RefactorItContext context, final Object object) {
        if (Assert.enabled) {
            Assert.must(context != null, "Attempt to pass NULL context into MoveTypeAction.run()");
            Assert.must(object != null, "Attempt to pass NULL object into MoveTypeAction.run()");
        }
        final Object target = RefactorItActionUtils.unwrapTarget(object);
        final MoveType mover = new MoveType(context, target);
        status = mover.checkPreconditions();
        if (status.isCancel()) {
            status = null;
            return false;
        }
        if (!status.isOk()) {
            showStatusMessage(context, status, target);
            if (status.isErrorOrFatal()) {
                status = null;
                return false;
            }
        }
        do {
            PackageDialog packageSelector = new PackageDialog(context, target);
            packageSelector.show();
            mover.setTargetPackage(packageSelector.getPackage());
            mover.setTargetSource(packageSelector.getTargetSource());
            mover.setChangeMemberAccess(packageSelector.isChangeMemberAccess());
            mover.setChangeInNonJavaFiles(packageSelector.isChangeInNonJavaFiles());
            status = mover.checkTargetPackage();
            if (status.isCancel()) {
                status = null;
                return false;
            }
            if (!status.isOk()) {
                showStatusMessage(context, status, target);
            }
        } while (status.isErrorOrFatal());
        MoveTypeConflictViewer conflicts = new MoveTypeConflictViewer(context, mover);
        conflicts.display();
        conflicts.cleanup();
        if (!conflicts.isOkPressed()) {
            status = null;
            return false;
        }
        if (!IDEController.runningJDev()) {
            try {
                JProgressDialog.run(context, new Runnable() {

                    public void run() {
                        status = mover.apply();
                    }
                }, false);
            } catch (SearchingInterruptedException ex) {
                Assert.must(false);
            }
        } else {
            status = mover.apply();
        }
        return finishMove(context, target);
    }

    private boolean finishMove(RefactorItContext context, Object target) {
        if (status.isCancel()) {
            status = null;
            return false;
        }
        if (!status.isOk()) {
            showStatusMessage(context, status, target);
        }
        boolean result = !status.isErrorOrFatal();
        status = null;
        return result;
    }

    private void showStatusMessage(RefactorItContext context, RefactoringStatus status, Object target) {
        RefactoringStatusViewer statusViewer = new RefactoringStatusViewer(context, status.isErrorOrFatal() ? "Can not continue with Move Class." : "Following problems arose during Move Class.", "refact.movetype");
        statusViewer.display(status);
    }

    static String getName(Object target) {
        if (target instanceof Object[]) {
            String name = "";
            for (int i = 0; i < ((Object[]) target).length; i++) {
                if (i > 0) {
                    name += ", ";
                }
                name += ((BinType) ((Object[]) target)[i]).getName();
            }
            return name;
        } else if (target instanceof BinType) {
            return ((BinType) target).getName();
        } else {
            return "<wrong target>";
        }
    }

    public void updateEnvironment(final RefactorItContext context) {
        super.updateEnvironment(context);
        context.getProject().cleanEmptyPackages();
    }

    /**
   * @see net.sf.refactorit.ui.module.RefactorItAction#isAvailableForType(java.lang.Class)
   */
    public boolean isAvailableForType(Class type) {
        throw new UnsupportedOperationException("method not implemented yet");
    }
}
