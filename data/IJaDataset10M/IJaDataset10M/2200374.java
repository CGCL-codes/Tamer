package com.google.gdt.eclipse.designer.gxt.databinding;

import com.google.gdt.eclipse.designer.util.Utils;
import org.eclipse.wb.core.editor.IDesignerEditor;
import org.eclipse.wb.core.editor.IEditorPage;
import org.eclipse.wb.core.editor.IEditorPageFactory;
import org.eclipse.wb.internal.core.databinding.ui.BindingDesignPage;
import org.eclipse.jdt.core.IImportDeclaration;
import org.eclipse.jdt.core.IJavaProject;
import java.util.List;

/**
 * 
 * @author lobas_av
 */
public class DesignPageFactory implements IEditorPageFactory {

    public void createPages(IDesignerEditor editor, List<IEditorPage> pages) {
        if (isGWT(editor) && isGXT(editor)) {
            BindingDesignPage.addPage(pages);
        }
    }

    private static boolean isGWT(IDesignerEditor editor) {
        try {
            IImportDeclaration[] imports = editor.getCompilationUnit().getImports();
            for (IImportDeclaration importDeclaration : imports) {
                String elementName = importDeclaration.getElementName();
                if (elementName.startsWith("com.google.gwt") || elementName.startsWith("com.extjs.gxt")) {
                    return true;
                }
            }
        } catch (Throwable e) {
        }
        return false;
    }

    private static boolean isGXT(IDesignerEditor editor) {
        try {
            IJavaProject javaProject = editor.getCompilationUnit().getJavaProject();
            if (Utils.isGWTProject(javaProject) && javaProject.findType("com.extjs.gxt.ui.client.GXT") != null) {
                return true;
            }
        } catch (Throwable e) {
        }
        return false;
    }
}
