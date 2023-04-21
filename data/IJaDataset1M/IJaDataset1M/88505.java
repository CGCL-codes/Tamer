package org.plazmaforge.studio.reportdesigner.actions;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IWorkbenchPart;
import org.plazmaforge.studio.reportdesigner.ReportDesignerPlugin;
import org.plazmaforge.studio.reportdesigner.ReportDesignerResources;
import org.plazmaforge.studio.reportdesigner.model.DesignElement;
import org.plazmaforge.studio.reportdesigner.model.ITextElement;

/**
 * 
 * @author Oleh Hapon
 * $Id: FontItalicAction.java,v 1.9 2010/11/12 07:59:58 ohapon Exp $
 */
public class FontItalicAction extends AbstractTextAction {

    public static final String ID = FontItalicAction.class.getName();

    public static final ImageDescriptor IMAGE_DESCRIPTOR = ReportDesignerPlugin.getImageDescriptor("icons/toolbar/enabled/font-italic.gif");

    public static final ImageDescriptor DISABLED_IMAGE_DESCRIPTOR = ReportDesignerPlugin.getImageDescriptor("icons/toolbar/disabled/font-italic.gif");

    public static final String NAME = ReportDesignerResources.Font_italic;

    public FontItalicAction(IWorkbenchPart part, int style) {
        super(part, style);
        initialize();
    }

    public FontItalicAction(IWorkbenchPart part) {
        super(part);
        initialize();
    }

    private void initialize() {
        setImageDescriptor(IMAGE_DESCRIPTOR);
        setDisabledImageDescriptor(DISABLED_IMAGE_DESCRIPTOR);
        setText(NAME);
        setId(ID);
    }

    protected void performCheck(ITextElement element) {
        setChecked(element.isItalicFont());
    }

    public void run() {
        ITextElement element = getSelectedTextElement();
        if (element == null) {
            return;
        }
        boolean oldFlag = element.isItalicFont();
        boolean newFlag = !oldFlag;
        updateFontFlag(element, DesignElement.PROPERTY_ITALIC_FONT, oldFlag, newFlag);
    }
}
