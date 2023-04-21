package com.google.gwt.eclipse.core.refactoring;

import com.google.gwt.eclipse.core.platformproxy.refactoring.IJsniReferenceChange;
import com.google.gwt.eclipse.core.platformproxy.refactoring.JsniReferenceChangeFactory;
import com.google.gwt.eclipse.core.search.IIndexedJavaRef;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.ltk.core.refactoring.TextFileChange;
import org.eclipse.text.edits.MultiTextEdit;
import org.eclipse.text.edits.ReplaceEdit;
import org.eclipse.text.edits.TextEdit;

/**
 * Tests the {@link JsniReferenceChange} class.
 */
public class JsniReferenceChangeTest extends AbstractRefactoringTestImpl {

    private class DummyRefactoringSupport extends GWTRefactoringSupport {

        public DummyRefactoringSupport() {
            IJavaElement oldElement = rClass.getCompilationUnit().getType("R");
            setOldElement(oldElement);
            IJavaElement newElement = rClass.getCompilationUnit().getType("RRR");
            setNewElement(newElement);
        }

        @Override
        protected TextEdit createEdit(IIndexedJavaRef ref) {
            return new ReplaceEdit(ref.getClassOffset(), 0, "");
        }

        @Override
        protected String getEditDescription() {
            return "";
        }
    }

    public void testPerform() throws CoreException {
        ICompilationUnit cu = refactorTestClass.getCompilationUnit();
        GWTRefactoringSupport support = new DummyRefactoringSupport();
        JsniReferenceChangeFactory factory = new JsniReferenceChangeFactory(support);
        IJsniReferenceChange jsniChange = factory.createChange(cu);
        TextEdit oldRootEdit = new MultiTextEdit();
        oldRootEdit.addChild(new ReplaceEdit(252, 0, ""));
        ((TextFileChange) jsniChange).setEdit(oldRootEdit);
        ((TextFileChange) jsniChange).perform(new NullProgressMonitor());
        TextEdit newRootEdit = ((TextFileChange) jsniChange).getEdit();
        assertEquals(1, newRootEdit.getChildrenSize());
    }

    @Override
    protected boolean requiresTestProject() {
        return true;
    }
}
