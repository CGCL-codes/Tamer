package net.sourceforge.sqlexplorer.sqleditor.actions;

import net.sourceforge.sqlexplorer.Messages;
import net.sourceforge.sqlexplorer.util.ImageUtil;
import org.eclipse.jface.resource.ImageDescriptor;

public class ClearTextAction extends AbstractEditorAction {

    private ImageDescriptor img = ImageUtil.getDescriptor("Images.ClearTextIcon");

    public String getText() {
        return Messages.getString("Clear_1");
    }

    public void run() {
        _editor.clearText();
    }

    public boolean isEnabled() {
        return true;
    }

    public String getToolTipText() {
        return Messages.getString("Clear_2");
    }

    public ImageDescriptor getHoverImageDescriptor() {
        return img;
    }

    public ImageDescriptor getImageDescriptor() {
        return img;
    }

    ;
}
