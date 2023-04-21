package net.sf.litprog4j.editors.codeeditor;

import org.eclipse.core.resources.IFile;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.part.FileEditorInput;
import net.sf.litprog4j.dom.ICodeDocument;
import net.sf.litprog4j.dom.java.JavaCodeDocument;

/**
 * @author KLin
 */
public class CodeEditorFactory {

    private static CodeEditorFactory instance;

    public static CodeEditorFactory getInstance() {
        if (instance == null) instance = new CodeEditorFactory();
        return instance;
    }

    public IEditorPart createEditor(ICodeDocument document) {
        if (document instanceof JavaCodeDocument) {
            return new LitProgJavaEditor((JavaCodeDocument) document);
        } else {
            return new LitProgCodeEditor(document);
        }
    }

    public IEditorInput getEditorInput(ICodeDocument document) {
        try {
            if (document instanceof JavaCodeDocument) {
                ICompilationUnit cu = ((JavaCodeDocument) document).getCompilationUnit();
                if (cu != null) return new FileEditorInput((IFile) cu.getCorrespondingResource()); else return null;
            } else {
                return new FileEditorInput(document.getCorrespondingResource());
            }
        } catch (JavaModelException jme) {
            return null;
        } catch (IllegalArgumentException iae) {
            return null;
        }
    }
}
