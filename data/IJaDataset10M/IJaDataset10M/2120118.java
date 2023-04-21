package net.sf.stump.eclipse.editor;

import net.sf.stump.eclipse.WicketPlugin;
import net.sf.stump.eclipse.util.TemplateHelper;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.IModel;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.dom.rewrite.ImportRewrite;
import org.eclipse.jface.text.IDocument;
import org.eclipse.swt.graphics.Image;

/**
 * @author Joni Suominen
 */
public class CreateInnerFormProposal extends AbstractCreationProposal {

    public CreateInnerFormProposal(String typeName, IJavaElement parent, Image image) {
        super(typeName, parent, image);
    }

    public void apply(IDocument document) {
        try {
            createForm(document);
        } catch (CoreException e) {
            throw new RuntimeException(e);
        }
    }

    private void createForm(IDocument document) throws CoreException {
        ICompilationUnit unit = (ICompilationUnit) getParent();
        IType enclosingType = unit.findPrimaryType();
        String content = TemplateHelper.getTemplateForForm(getTypeName());
        enclosingType.createType(content.toString(), null, false, null);
        updateImports(unit, document);
    }

    private void updateImports(ICompilationUnit unit, IDocument document) throws CoreException {
        ImportRewrite imports = ImportRewrite.create(unit, true);
        imports.addImport(Form.class.getName());
        imports.addImport(IModel.class.getName());
        try {
            imports.rewriteImports(new NullProgressMonitor()).apply(document);
        } catch (Exception e) {
            WicketPlugin.log(e);
        }
    }

    public String getAdditionalProposalInfo() {
        return "Create a 'Form' enclosed by this type";
    }

    public String getDisplayString() {
        return "Create Wicket inner 'Form' (" + Form.class.getName() + ")";
    }
}
