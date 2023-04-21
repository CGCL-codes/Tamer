package org.eclipse.mylyn.internal.tasks.ui.editors;

import org.eclipse.jface.viewers.DecoratingLabelProvider;
import org.eclipse.jface.viewers.ILabelDecorator;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ITableColorProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.mylyn.internal.tasks.ui.TaskListColorsAndFonts;
import org.eclipse.mylyn.internal.tasks.ui.TasksUiImages;
import org.eclipse.mylyn.tasks.core.AbstractAttachmentHandler;
import org.eclipse.mylyn.tasks.core.RepositoryAttachment;
import org.eclipse.mylyn.tasks.ui.editors.AbstractRepositoryTaskEditor;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.internal.WorkbenchImages;
import org.eclipse.ui.themes.IThemeManager;

/**
 * @author Mik Kersten
 */
public class AttachmentTableLabelProvider extends DecoratingLabelProvider implements ITableColorProvider, ITableLabelProvider {

    private final AbstractRepositoryTaskEditor AbstractTaskEditor;

    private IThemeManager themeManager = PlatformUI.getWorkbench().getThemeManager();

    public AttachmentTableLabelProvider(AbstractRepositoryTaskEditor AbstractTaskEditor, ILabelProvider provider, ILabelDecorator decorator) {
        super(provider, decorator);
        this.AbstractTaskEditor = AbstractTaskEditor;
    }

    public Image getColumnImage(Object element, int columnIndex) {
        RepositoryAttachment attachment = (RepositoryAttachment) element;
        if (columnIndex == 0) {
            if (AbstractAttachmentHandler.MYLAR_CONTEXT_DESCRIPTION.equals(attachment.getDescription()) || AbstractAttachmentHandler.MYLAR_CONTEXT_DESCRIPTION_LEGACY.equals(attachment.getDescription())) {
                return TasksUiImages.getImage(TasksUiImages.CONTEXT_TRANSFER);
            } else if (attachment.isPatch()) {
                return TasksUiImages.getImage(TasksUiImages.ATTACHMENT_PATCH);
            } else {
                return WorkbenchImages.getImage(ISharedImages.IMG_OBJ_FILE);
            }
        } else {
        }
        return null;
    }

    public String getColumnText(Object element, int columnIndex) {
        RepositoryAttachment attachment = (RepositoryAttachment) element;
        switch(columnIndex) {
            case 0:
                return " " + attachment.getDescription();
            case 1:
                if (attachment.isPatch()) {
                    return "patch";
                } else {
                    return attachment.getContentType();
                }
            case 2:
                return attachment.getCreator();
            case 3:
                return this.AbstractTaskEditor.formatDate(attachment.getDateCreated());
        }
        return "unrecognized column";
    }

    @Override
    public void addListener(ILabelProviderListener listener) {
    }

    @Override
    public void dispose() {
    }

    @Override
    public boolean isLabelProperty(Object element, String property) {
        return false;
    }

    @Override
    public void removeListener(ILabelProviderListener listener) {
    }

    public Color getForeground(Object element, int columnIndex) {
        RepositoryAttachment att = (RepositoryAttachment) element;
        if (att.isObsolete()) {
            return themeManager.getCurrentTheme().getColorRegistry().get(TaskListColorsAndFonts.THEME_COLOR_COMPLETED);
        }
        return super.getForeground(element);
    }

    public Color getBackground(Object element, int columnIndex) {
        return super.getBackground(element);
    }

    public Font getFont(Object element, int columnIndex) {
        return super.getFont(element);
    }
}
