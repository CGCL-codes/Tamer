package org.eclipse.babel.editor.refactoring;

import org.eclipse.babel.core.message.MessagesBundleGroup;
import org.eclipse.babel.core.message.tree.KeyTreeNode;
import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.ltk.core.refactoring.Refactoring;
import org.eclipse.ltk.core.refactoring.RefactoringContribution;
import org.eclipse.ltk.core.refactoring.RefactoringCore;
import org.eclipse.ltk.core.refactoring.RefactoringDescriptor;
import org.eclipse.ltk.core.refactoring.RefactoringStatus;
import org.eclipse.ltk.core.refactoring.participants.RenameRefactoring;

/**
 * Refactoring descriptor for the rename resource bundle key refactoring.
 * <p>
 * An instance of this refactoring descriptor may be obtained by calling
 * {@link RefactoringContribution#createDescriptor()} on a refactoring
 * contribution requested by invoking
 * {@link RefactoringCore#getRefactoringContribution(String)} with the
 * refactoring id ({@link #ID}).
 */
public final class RenameKeyDescriptor extends RefactoringDescriptor {

    public static final String ID = "org.eclipse.babel.editor.refactoring.renameKey";

    /** The name attribute */
    private String fNewName;

    private KeyTreeNode fKeyNode;

    private MessagesBundleGroup fMessagesBundleGroup;

    /** Configures if references will be updated */
    private boolean fRenameChildKeys;

    /**
	 * Creates a new refactoring descriptor.
	 * <p>
	 * Clients should not instantiated this class but use {@link RefactoringCore#getRefactoringContribution(String)}
	 * with {@link #ID} to get the contribution that can create the descriptor.
	 * </p>
	 */
    public RenameKeyDescriptor() {
        super(ID, null, "N/A", null, RefactoringDescriptor.STRUCTURAL_CHANGE | RefactoringDescriptor.MULTI_CHANGE);
        fNewName = null;
    }

    /**
	 * Sets the new name to rename the resource to.
	 *
	 * @param name
	 *            the non-empty new name to set
	 */
    public void setNewName(final String name) {
        Assert.isNotNull(name);
        Assert.isLegal(!"".equals(name), "Name must not be empty");
        fNewName = name;
    }

    /**
	 * Returns the new name to rename the resource to.
	 *
	 * @return
	 *            the new name to rename the resource to
	 */
    public String getNewName() {
        return fNewName;
    }

    /**
	 * 	If set to <code>true</code>, this rename will also rename child keys. The default is to rename child keys.
	 *
	 * @param renameChildKeys  <code>true</code> if this rename will rename child keys
	 */
    public void setRenameChildKeys(boolean renameChildKeys) {
        fRenameChildKeys = renameChildKeys;
    }

    public void setRenameChildKeys(KeyTreeNode keyNode, MessagesBundleGroup messagesBundleGroup) {
        this.fKeyNode = keyNode;
        this.fMessagesBundleGroup = messagesBundleGroup;
    }

    /**
	 * Returns if this rename will also rename child keys
	 *
	 * @return returns <code>true</code> if this rename will rename child keys
	 */
    public boolean isRenameChildKeys() {
        return fRenameChildKeys;
    }

    public Refactoring createRefactoring(RefactoringStatus status) throws CoreException {
        String newName = getNewName();
        if (newName == null || newName.length() == 0) {
            status.addFatalError("The rename resource bundle key refactoring can not be performed as the new name is invalid");
            return null;
        }
        RenameKeyProcessor processor = new RenameKeyProcessor(fKeyNode, fMessagesBundleGroup);
        processor.setNewResourceName(newName);
        processor.setRenameChildKeys(fRenameChildKeys);
        return new RenameRefactoring(processor);
    }
}
