package org.dyno.visual.swing.types;

import java.util.StringTokenizer;
import org.dyno.visual.swing.VisualSwingPlugin;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jface.viewers.ICellEditorValidator;

public class ImageIconValidator implements ICellEditorValidator {

    public String isValid(Object value) {
        if (value == null || value.equals("")) return null;
        StringTokenizer tokenizer = new StringTokenizer((String) value, "/");
        if (!tokenizer.hasMoreTokens()) {
            return Messages.ImageIconValidator_Incorrect_Icon_Image_Format;
        }
        do {
            String token = tokenizer.nextToken().trim();
            if (token.length() == 0) return Messages.ImageIconValidator_Incorrect_Icon_Image_Format;
            if (tokenizer.hasMoreTokens()) {
                char c = token.charAt(0);
                if (!Character.isJavaIdentifierStart(c)) {
                    return Messages.ImageIconValidator_Incorrect_Icon_Image_Format_Segment_Id;
                }
                int i = 0;
                while (true) {
                    c = token.charAt(i++);
                    if (!Character.isJavaIdentifierPart(c) && c != '.') return Messages.ImageIconValidator_Incorrect_Icon_Image_Format_Segment_Id;
                    if (i >= token.length()) break;
                }
            }
        } while (tokenizer.hasMoreTokens());
        IJavaProject prj = VisualSwingPlugin.getCurrentProject();
        IProject project = prj.getProject();
        IResource resource = project.findMember(new Path((String) value));
        if (resource == null) {
            IPackageFragmentRoot[] roots;
            try {
                roots = prj.getPackageFragmentRoots();
                for (IPackageFragmentRoot root : roots) {
                    if (!root.isArchive()) {
                        String src = root.getElementName();
                        src = "/" + src + value;
                        resource = project.findMember(new Path(src));
                        if (resource != null) {
                            String ext = resource.getFileExtension();
                            if (ext != null && (ext.equalsIgnoreCase("gif") || ext.equalsIgnoreCase("png") || ext.equalsIgnoreCase("jpg"))) return null; else return Messages.ImageIconValidator_Not_Image_File + value;
                        }
                    }
                }
            } catch (JavaModelException e) {
                VisualSwingPlugin.getLogger().error(e);
                return e.getLocalizedMessage();
            }
            return Messages.ImageIconValidator_Cannot_Find_Such_Image_File + value + "!";
        }
        return null;
    }
}
