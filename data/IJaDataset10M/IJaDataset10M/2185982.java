package net.sf.lunareclipse.internal.ui.editor;

import net.sf.lunareclipse.core.LuaConstants;
import net.sf.lunareclipse.internal.ui.LuaImages;
import org.eclipse.dltk.core.IMember;
import org.eclipse.dltk.core.ModelException;
import org.eclipse.dltk.internal.ui.editor.AnnotatedImageDescriptor;
import org.eclipse.dltk.ui.DLTKPluginImages;
import org.eclipse.dltk.ui.viewsupport.ImageImageDescriptor;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ILabelDecorator;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;

public class LuaOutlineLabelDecorator extends LabelProvider implements ILabelDecorator {

    protected static class LuaOutlineImageDescriptor extends AnnotatedImageDescriptor {

        private int flags;

        public LuaOutlineImageDescriptor(ImageDescriptor baseImageDescriptor, Point size, int flags) {
            super(baseImageDescriptor, size);
            this.flags = flags;
        }

        protected void drawAnnotations() {
            ImageData data = null;
            if ((flags & LuaConstants.AccLocal) != 0) {
                data = getImageData(LuaImages.DESC_OVR_LOCAL);
            }
            if (data != null) {
                drawImageBottomRight(data);
            }
            if ((flags & LuaConstants.LuaAttributeModifier) != 0) {
                data = getImageData(DLTKPluginImages.DESC_OVR_ABSTRACT);
                drawImageTopLeft(data);
            }
        }
    }

    public LuaOutlineLabelDecorator() {
    }

    public Image decorateImage(Image image, Object element) {
        try {
            if (element instanceof IMember) {
                IMember member = (IMember) element;
                int flags = member.getFlags();
                ImageDescriptor baseImage = new ImageImageDescriptor(image);
                Rectangle bounds = image.getBounds();
                ImageDescriptor dsc = new LuaOutlineImageDescriptor(baseImage, new Point(bounds.width, bounds.height), flags);
                return dsc.createImage();
            }
        } catch (ModelException e) {
            e.printStackTrace();
        }
        return image;
    }

    public String decorateText(String text, Object element) {
        return text;
    }
}
