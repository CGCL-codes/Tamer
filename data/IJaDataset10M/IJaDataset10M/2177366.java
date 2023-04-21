package net.sf.stump.eclipse.editor;

import net.sf.stump.eclipse.util.TemplateHelper;
import org.eclipse.core.resources.IMarker;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.IMarkerResolution2;

/**
 * @author Joni Suominen
 */
public class CreateHtmlTemplateQuickFix implements IMarkerResolution2 {

    public void run(IMarker marker) {
        ICompilationUnit unit = (ICompilationUnit) JavaCore.create(marker.getResource());
        IType type = unit.findPrimaryType();
        try {
            TemplateHelper.createMarkupTemplate("wicket.panelHtml", type.getElementName(), type.getPackageFragment());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public String getLabel() {
        return "Create HTML template";
    }

    public String getDescription() {
        return "Create a new HTML template for the Panel component";
    }

    public Image getImage() {
        return null;
    }
}
