package org.rubypeople.rdt.internal.ui.rubyeditor.outline;

import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.swt.graphics.Image;
import org.rubypeople.rdt.internal.core.RubyPlugin;
import org.rubypeople.rdt.internal.core.parser.ast.IRubyElement;
import org.rubypeople.rdt.internal.core.parser.ast.RubyElement;
import org.rubypeople.rdt.internal.ui.RdtUiImages;

public class RubyOutlineLabelProvider implements ILabelProvider {

    /**
	 * @see org.eclipse.jface.viewers.ILabelProvider#getImage(java.lang.Object)
	 */
    public Image getImage(Object object) {
        if (object == null) {
            log("Attempting to get Image for null object in outline elements");
            return RdtUiImages.get(RdtUiImages.IMG_OBJS_ERROR);
        }
        if (object instanceof RubyElement) {
            RubyElement rubyElement = (RubyElement) object;
            if (rubyElement.isType(RubyElement.GLOBAL)) return RdtUiImages.get(RdtUiImages.IMG_CTOOLS_RUBYGLOBAL);
            if (rubyElement.isType(RubyElement.CLASS_VAR)) return RdtUiImages.get(RdtUiImages.IMG_CTOOLS_RUBYCLASSVAR);
            if (rubyElement.isType(RubyElement.MODULE)) return RdtUiImages.get(RdtUiImages.IMG_CTOOLS_RUBYMODULE);
            if (rubyElement.isType(RubyElement.REQUIRES)) return RdtUiImages.get(RdtUiImages.IMG_CTOOLS_RUBYIMPORT);
            if (rubyElement.isType(RubyElement.METHOD)) {
                if (rubyElement.getAccess().equals(RubyElement.PUBLIC)) {
                    return RdtUiImages.get(RdtUiImages.IMG_CTOOLS_RUBYMETHOD_PUB);
                }
                if (rubyElement.getAccess().equals(RubyElement.PROTECTED)) {
                    return RdtUiImages.get(RdtUiImages.IMG_CTOOLS_RUBYMETHOD_PRO);
                }
                return RdtUiImages.get(RdtUiImages.IMG_CTOOLS_RUBYMETHOD);
            }
            if (rubyElement.isType(RubyElement.INSTANCE_VAR)) {
                if (rubyElement.getAccess().equals(RubyElement.READ)) {
                    return RdtUiImages.get(RdtUiImages.IMG_CTOOLS_RUBYINSTVAR_READ);
                }
                if (rubyElement.getAccess().equals(RubyElement.WRITE)) {
                    return RdtUiImages.get(RdtUiImages.IMG_CTOOLS_RUBYINSTVAR_WRITE);
                }
                if (rubyElement.getAccess().equals(RubyElement.PUBLIC)) {
                    return RdtUiImages.get(RdtUiImages.IMG_CTOOLS_RUBYINSTVAR);
                }
                return RdtUiImages.get(RdtUiImages.IMG_CTOOLS_RUBYINSTVAR_PRIV);
            }
            if (rubyElement.isType(RubyElement.CLASS)) return RdtUiImages.get(RdtUiImages.IMG_CTOOLS_RUBYCLASS);
        }
        log("Attempting to get Image for unknown object in outline elements: " + object);
        return RdtUiImages.get(RdtUiImages.IMG_OBJS_ERROR);
    }

    /**
	 * @param string
	 */
    protected void log(String string) {
        RubyPlugin.log(string);
    }

    /**
	 * @see org.eclipse.jface.viewers.ILabelProvider#getText(java.lang.Object)
	 */
    public String getText(Object obj) {
        if (obj instanceof IRubyElement) return ((IRubyElement) obj).getName();
        return "Invalid object: " + obj.getClass().getName();
    }

    /**
	 * @see org.eclipse.jface.viewers.IBaseLabelProvider#addListener(org.eclipse.jface.viewers.ILabelProviderListener)
	 */
    public void addListener(ILabelProviderListener arg0) {
    }

    /**
	 * @see org.eclipse.jface.viewers.IBaseLabelProvider#dispose()
	 */
    public void dispose() {
    }

    /**
	 * @see org.eclipse.jface.viewers.IBaseLabelProvider#isLabelProperty(java.lang.Object,
	 *      java.lang.String)
	 */
    public boolean isLabelProperty(Object arg0, String arg1) {
        return false;
    }

    /**
	 * @see org.eclipse.jface.viewers.IBaseLabelProvider#removeListener(org.eclipse.jface.viewers.ILabelProviderListener)
	 */
    public void removeListener(ILabelProviderListener arg0) {
    }
}
