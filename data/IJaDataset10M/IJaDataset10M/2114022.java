package com.google.gwt.eclipse.core.refactoring.rpc;

import com.google.gdt.eclipse.core.java.JavaModelSearch;
import com.google.gwt.eclipse.core.GWTPluginLog;
import com.google.gwt.eclipse.core.refactoring.ChangeBuilder;
import com.google.gwt.eclipse.core.refactoring.ChangeUtilities;
import com.google.gwt.eclipse.core.refactoring.GWTRenameMemberParticipant;
import com.google.gwt.eclipse.core.refactoring.NestedRefactoringContext;
import com.google.gwt.eclipse.core.refactoring.RefactoringException;
import com.google.gwt.eclipse.core.refactoring.regionupdater.ReferenceUpdater;
import com.google.gwt.eclipse.core.refactoring.regionupdater.RegionUpdaterChangeWeavingVisitor;
import com.google.gwt.eclipse.core.refactoring.regionupdater.RenamedElementAstMatcher;
import com.google.gwt.eclipse.core.validators.rpc.RemoteServiceException;
import com.google.gwt.eclipse.core.validators.rpc.RemoteServiceUtilities;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.ITypeHierarchy;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.Signature;
import org.eclipse.jdt.core.refactoring.IJavaRefactorings;
import org.eclipse.jdt.core.refactoring.descriptors.RenameJavaElementDescriptor;
import org.eclipse.jdt.internal.corext.refactoring.rename.RenameVirtualMethodProcessor;
import org.eclipse.ltk.core.refactoring.Change;
import org.eclipse.ltk.core.refactoring.CompositeChange;
import org.eclipse.ltk.core.refactoring.participants.ProcessorBasedRefactoring;
import org.eclipse.ltk.core.refactoring.participants.RefactoringProcessor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * A Java method rename participant that looks for renames to methods in GWT RPC
 * interfaces and renames the corresponding method of the paired GWT RPC
 * interface.
 */
@SuppressWarnings("restriction")
public class PairedMethodRenameParticipant extends GWTRenameMemberParticipant {

    private static class MethodRenameChangeBuilder extends ChangeBuilder<RenameJavaElementDescriptor> {

        private final RenameVirtualMethodProcessor processor;

        private final IMethod method;

        private final String newName;

        public MethodRenameChangeBuilder(IMethod method, String newName, RenameVirtualMethodProcessor processor, IWorkspace workspace) {
            super(RenameJavaElementDescriptor.class, IJavaRefactorings.RENAME_METHOD, workspace);
            this.newName = newName;
            this.processor = processor;
            this.method = method;
        }

        @Override
        protected void configureDescriptor(RenameJavaElementDescriptor descriptor) {
            descriptor.setJavaElement(method);
            descriptor.setNewName(newName);
            descriptor.setProject(method.getJavaProject().getProject().getName());
            descriptor.setKeepOriginal(processor.getDelegateUpdating());
            descriptor.setDeprecateDelegate(processor.getDeprecateDelegates());
            descriptor.setUpdateReferences(processor.getUpdateReferences());
        }
    }

    /**
   * A nested refactoring context for a method rename. It tracks the methods
   * visited so far (imagine an interface hierarchy where each interface
   * overrides the method to be renamed), and the methods that still need to be
   * renamed. It also tracks whether the current refactoring is the root or
   * nested.
   */
    private static class MethodRenameRefactoringContext extends NestedRefactoringContext {

        private static MethodRenameRefactoringContext newNestedRefactoringContext(MethodRenameRefactoringContext callerData) {
            return new MethodRenameRefactoringContext(callerData.visitedMethods, callerData.toRefactorMethods, false);
        }

        private static MethodRenameRefactoringContext newRootRefactoringContext() {
            return new MethodRenameRefactoringContext(new HashSet<IMethod>(), new ArrayList<IMethod>(), true);
        }

        private final Set<IMethod> visitedMethods;

        private final List<IMethod> toRefactorMethods;

        private final boolean isRoot;

        private MethodRenameRefactoringContext(Set<IMethod> visitedMethods, List<IMethod> toVisitMethods, boolean isNested) {
            this.visitedMethods = visitedMethods;
            this.toRefactorMethods = toVisitMethods;
            this.isRoot = isNested;
        }
    }

    /**
   * @see JavaModelSearch#findMethodsFromTopmostHierarchy(IMethod)
   */
    private static void addMethodsFromTopmostHierarchyToSet(IMethod method, Set<IMethod> set) throws CoreException {
        IMethod[] methods = JavaModelSearch.findMethodsFromTopmostHierarchy(method);
        set.addAll(Arrays.asList(methods));
    }

    /**
   * Computes the paired method enclosed in the paired type contained within the
   * given <code>typeContainer</code>.
   * 
   * @param typeContainer the container for the current type and paired type
   * @param method a method that will be used to form a signature used during
   *          the lookup
   * @return the paired method, or null if one could not be found
   * @throws RemoteServiceException
   * @throws JavaModelException
   */
    private static IMethod computePairedMethod(TypeContainer typeContainer, IMethod method) throws RemoteServiceException, JavaModelException {
        String[] paramTypeNames = null;
        if (typeContainer.isSync()) {
            paramTypeNames = RemoteServiceUtilities.computeAsyncParameterTypes(method);
        } else {
            paramTypeNames = RemoteServiceUtilities.computeSyncParameterTypes(method);
        }
        String[] paramTypeSigs = new String[paramTypeNames.length];
        for (int i = 0; i < paramTypeNames.length; i++) {
            paramTypeSigs[i] = Signature.createTypeSignature(paramTypeNames[i], true);
        }
        IType pairedType = typeContainer.getPairedType();
        return JavaModelSearch.findMethodInHierarchy(pairedType.newSupertypeHierarchy(new NullProgressMonitor()), pairedType, method.getElementName(), paramTypeSigs);
    }

    /**
   * GWT RPC type information
   */
    private TypeContainer typeContainer;

    private IMethod baseMethod;

    private IMethod pairedMethod;

    private RenameVirtualMethodProcessor processor;

    private MethodRenameRefactoringContext refactoringContext;

    /**
   * Only valid after
   * {@link #checkConditions(IProgressMonitor, org.eclipse.ltk.core.refactoring.participants.CheckConditionsContext)}
   * is called
   */
    private String newMethodName;

    @Override
    public Change createChange(IProgressMonitor pm) throws CoreException, OperationCanceledException {
        newMethodName = computeNewName();
        if (!checkNestedConditions()) {
            return null;
        }
        addPairedMethodsOfMyHierarchy();
        if (refactoringContext.isRoot) {
            return createChangeFromMethodsToRefactor();
        } else {
            return null;
        }
    }

    @Override
    public String getName() {
        return "Rename method in a GWT RPC paired interface";
    }

    @Override
    protected boolean initialize(Object element) {
        if (!super.initialize(element) || !(element instanceof IMethod)) {
            return false;
        }
        baseMethod = (IMethod) getRefactoringSupport().getOldElement();
        try {
            typeContainer = TypeContainer.createTypeContainer(baseMethod.getDeclaringType());
            if (typeContainer == null) {
                return false;
            }
            pairedMethod = computePairedMethod(typeContainer, baseMethod);
            if (pairedMethod == null) {
                return false;
            }
        } catch (JavaModelException e) {
            GWTPluginLog.logError(e);
            return false;
        } catch (RemoteServiceException e) {
            GWTPluginLog.logError(e);
            return false;
        }
        RefactoringProcessor refactoringProcessor = getProcessor();
        if (!(refactoringProcessor instanceof RenameVirtualMethodProcessor)) {
            GWTPluginLog.logWarning("Not participating in GWT RPC method rename due to invalid processor type: " + refactoringProcessor.getClass().getSimpleName());
            return false;
        }
        processor = (RenameVirtualMethodProcessor) refactoringProcessor;
        refactoringContext = (MethodRenameRefactoringContext) NestedRefactoringContext.forProcessor(refactoringProcessor);
        if (refactoringContext == null) {
            refactoringContext = MethodRenameRefactoringContext.newRootRefactoringContext();
            NestedRefactoringContext.storeForProcessor(refactoringProcessor, refactoringContext);
        }
        return true;
    }

    /**
   * Finds all interfaces in my hierarchy (including myself), and for each check
   * whether its paired interface does contain the method, in which case we add
   * that method to the list to rename.
   * <p>
   * Consider this example to understand the utility of this method:
   * SuperService (declares method), SuperServiceAsync (declares method),
   * SubService extends SuperService (does not declare method), and
   * SubServiceAsync (declares method, note that this is not a java subtype of
   * SuperServiceAsync). If we rename SuperService.method, this takes care of
   * renaming SubServiceAsync.method too. The SuperService.method causes
   * SuperServiceAsync.method to be renamed. However, since SubServiceAsync does
   * not directly subtype SuperServiceAsync, SubServiceAsync.method won't be
   * renamed. This method takes care of adding SubServiceAsync.method to the
   * list of methods to be refactored.
   */
    private void addPairedMethodsOfMyHierarchy() {
        ITypeHierarchy myHierarchy;
        try {
            myHierarchy = typeContainer.getBaseType().newTypeHierarchy(new NullProgressMonitor());
        } catch (JavaModelException e) {
            GWTPluginLog.logWarning(e, "There may be some GWT RPC methods that were not renamed.");
            return;
        }
        for (IType curInterface : myHierarchy.getAllInterfaces()) {
            try {
                TypeContainer curTypeContainer = TypeContainer.createTypeContainer(curInterface);
                if (curTypeContainer == null) {
                    continue;
                }
                IMethod curPairedMethod = computePairedMethod(curTypeContainer, baseMethod);
                if (curPairedMethod == null) {
                    continue;
                }
                if (!refactoringContext.visitedMethods.contains(curPairedMethod) && !refactoringContext.toRefactorMethods.contains(curPairedMethod)) {
                    refactoringContext.toRefactorMethods.add(curPairedMethod);
                    addMethodsFromTopmostHierarchyToSet(curPairedMethod, refactoringContext.visitedMethods);
                }
            } catch (JavaModelException e) {
                GWTPluginLog.logWarning(e, String.format("Could not search the hierarchy of %1$s for RPC methods to rename.", curInterface.getElementName()));
                continue;
            } catch (RemoteServiceException e) {
                GWTPluginLog.logWarning(e, String.format("Could not search the hierarchy of %1$s for RPC methods to rename.", curInterface.getElementName()));
                continue;
            } catch (CoreException e) {
                GWTPluginLog.logWarning(e, String.format("Could not search the hierarchy of %1$s for RPC methods to rename.", curInterface.getElementName()));
            }
        }
    }

    /**
   * @return true if the participant should continue, or false if it should
   *         abort
   */
    private boolean checkNestedConditions() {
        try {
            addMethodsFromTopmostHierarchyToSet(baseMethod, refactoringContext.visitedMethods);
        } catch (CoreException e) {
            GWTPluginLog.logWarning("Not refactoring GWT RPC: " + e.getMessage());
            return false;
        }
        return true;
    }

    private String computeNewName() {
        return ((IMethod) getRefactoringSupport().getNewElement()).getElementName();
    }

    private Change createChangeForMethodRename(IMethod methodToRename) throws RefactoringException {
        MethodRenameChangeBuilder builder = new MethodRenameChangeBuilder(methodToRename, newMethodName, processor, methodToRename.getJavaProject().getProject().getWorkspace());
        ProcessorBasedRefactoring refactoring = (ProcessorBasedRefactoring) builder.createRefactoring();
        RefactoringProcessor nestedProcessor = refactoring.getProcessor();
        NestedRefactoringContext.storeForProcessor(nestedProcessor, MethodRenameRefactoringContext.newNestedRefactoringContext(refactoringContext));
        return builder.createChange();
    }

    private Change createChangeFromMethodsToRefactor() {
        CompositeChange changes = new CompositeChange("GWT RPC paired method renames");
        changes.markAsSynthetic();
        while (refactoringContext.toRefactorMethods.size() > 0) {
            IMethod method = refactoringContext.toRefactorMethods.remove(0);
            try {
                Change change = createChangeForMethodRename(method);
                if (change != null) {
                    if (ChangeUtilities.mergeParticipantTextChanges(this, change)) {
                        continue;
                    }
                    ChangeUtilities.acceptOnChange(change, new RegionUpdaterChangeWeavingVisitor(new RenamedElementAstMatcher(pairedMethod.getElementName(), newMethodName), new ReferenceUpdater()));
                    changes.add(change);
                }
            } catch (RefactoringException e) {
                GWTPluginLog.logError("Could not rename method " + method);
            }
        }
        return (changes.getChildren().length > 0) ? changes : null;
    }
}
