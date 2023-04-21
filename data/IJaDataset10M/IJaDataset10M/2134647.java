package org.o14x.alpha.ui.views.folder;

import java.io.File;
import java.util.Arrays;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ITableColorProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.dnd.FileTransfer;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.PlatformUI;
import org.o14x.alpha.services.FileServices;
import org.o14x.alpha.ui.controller.UIController;
import org.o14x.alpha.ui.views.folder.columns.ColumnHelper;

/**
 * LabelProvider of the file table.
 * 
 * @author Olivier DANGREAUX
 */
public class FileTableLabelProvider implements ITableLabelProvider, ITableColorProvider {

    /**
	 * UIController
	 */
    private UIController uiController;

    @Override
    public Image getColumnImage(Object element, int columnIndex) {
        File file = (File) element;
        ColumnHelper columnHelper = uiController.getUi().getColumnHelperManager().getEnabledColumnHelperList().get(columnIndex);
        Image image = columnHelper.getColumnImage(file);
        return image;
    }

    @Override
    public String getColumnText(Object element, int columnIndex) {
        String columnText = null;
        if (element != null) {
            File file = (File) element;
            ColumnHelper columnHelper = uiController.getUi().getColumnHelperManager().getEnabledColumnHelperList().get(columnIndex);
            columnText = columnHelper.getColumnText(file);
        }
        return columnText;
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

    /**
	 * Returns the value of uiController.
	 *
	 * @return The value of uiController.
	 */
    public UIController getUiController() {
        return uiController;
    }

    /**
	 * Sets the value of uiController.
	 *
	 * @param uiController The value of uiController to set.
	 */
    public void setUiController(UIController uiController) {
        this.uiController = uiController;
    }

    @Override
    public Color getBackground(Object element, int columnIndex) {
        Color color = null;
        if (uiController.getDomain().getClipboard() != null && uiController.getDomain().getClipboard().isCut()) {
            Clipboard cb = new Clipboard(PlatformUI.getWorkbench().getDisplay());
            FileTransfer fileTransfer = FileTransfer.getInstance();
            String[] selectedPaths = (String[]) cb.getContents(fileTransfer);
            FileServices fileServices = uiController.getServices().getFileServices();
            File[] copiedFiles = fileServices.toFiles(selectedPaths);
            if (Arrays.asList(copiedFiles).contains(element)) {
                color = new Color(PlatformUI.getWorkbench().getDisplay(), 255, 216, 0);
            }
        }
        return color;
    }

    @Override
    public Color getForeground(Object element, int columnIndex) {
        return null;
    }
}
