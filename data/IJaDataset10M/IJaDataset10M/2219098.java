package org.jmlspecs.eclipse.refactor.views;

import java.util.HashMap;
import java.util.Map;
import org.eclipse.jdt.ui.ISharedImages;
import org.eclipse.jdt.ui.JavaUI;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.PlatformUI;
import org.jmlspecs.eclipse.refactor.dom.JmlAssignable;
import org.jmlspecs.eclipse.refactor.dom.JmlCompilationUnit;
import org.jmlspecs.eclipse.refactor.dom.JmlNode;
import org.jmlspecs.eclipse.refactor.dom.JmlPostcondition;
import org.jmlspecs.eclipse.refactor.dom.JmlPrecondition;
import org.jmlspecs.eclipse.refactor.dom.JmlSpecCase;
import org.jmlspecs.eclipse.refactor.dom.JmlTypeDeclaration;
import org.jmlspecs.eclipse.refactor.dom.JmlVisible;
import org.jmlspecs.jc.JC.VisibilityModifier;

/**
 * Utility class to retrieve the images for JmlNodes.
 * 
 * @author iain
 */
public class JmlNodeImages {

    /**
     * Return the image for the specified node.
     * 
     * @param node
     *            the node to retrieve the image for.
     * 
     * @return the image for the specified node.
     */
    public static Image getImage(JmlNode node) {
        if (node instanceof JmlCompilationUnit) {
            return getJavaImage(ISharedImages.IMG_OBJS_CFILE);
        } else if (node instanceof JmlTypeDeclaration) {
            return getJavaImage(ISharedImages.IMG_OBJS_CLASS);
        } else if (node instanceof JmlVisible) {
            JmlVisible visnode = (JmlVisible) node;
            return getJavaImage(lookupMethodVisibility(visnode.getVisibility()));
        } else if (node instanceof JmlPostcondition) {
            return getJavaImage(ISharedImages.IMG_OBJS_LOCAL_VARIABLE);
        } else if (node instanceof JmlPrecondition) {
            return getJavaImage(ISharedImages.IMG_OBJS_LOCAL_VARIABLE);
        } else if (node instanceof JmlAssignable) {
            return getJavaImage(ISharedImages.IMG_OBJS_IMPDECL);
        } else if (node instanceof JmlSpecCase) {
            return getJavaImage(ISharedImages.IMG_OBJS_IMPCONT);
        } else if (node instanceof JavaContractsGroupNode) {
            return getPlatformImage(org.eclipse.ui.ISharedImages.IMG_OBJ_FOLDER);
        }
        return null;
    }

    private static Image getPlatformImage(String key) {
        return PlatformUI.getWorkbench().getSharedImages().getImage(key);
    }

    private static String lookupMethodVisibility(VisibilityModifier visibility) {
        return methodVisibility.get(visibility);
    }

    private static Image getJavaImage(String key) {
        return JavaUI.getSharedImages().getImage(key);
    }

    private static final Map<VisibilityModifier, String> methodVisibility = new HashMap<VisibilityModifier, String>();

    static {
        methodVisibility.put(VisibilityModifier.PACKAGE, ISharedImages.IMG_OBJS_PACKAGE);
        methodVisibility.put(VisibilityModifier.PUBLIC, ISharedImages.IMG_OBJS_PUBLIC);
        methodVisibility.put(VisibilityModifier.PRIVATE, ISharedImages.IMG_OBJS_PRIVATE);
        methodVisibility.put(VisibilityModifier.PROTECTED, ISharedImages.IMG_OBJS_PROTECTED);
    }
}
