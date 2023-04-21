package org.ufacekit.ui.swing.jface.viewers.internal;

import org.ufacekit.ui.swing.jface.viewers.internal.swt.graphics.Color;
import org.ufacekit.ui.swing.jface.viewers.internal.swt.graphics.Font;
import org.ufacekit.ui.swing.jface.viewers.internal.swt.graphics.Image;

/**
 * The ColumnLabelProvider is the label provider for viewers that have column
 * support such as {@link TreeViewer} and {@link TableViewer}
 *
 * <p>
 * <b>This classes is intended to be subclassed</b>
 * </p>
 *
 * @param <ModelElement>
 *            the model element displayed in the viewer
 *
 * @since 3.3
 *
 */
public class ColumnLabelProvider<ModelElement> extends CellLabelProvider<ModelElement> implements IFontProvider<ModelElement>, IColorProvider<ModelElement>, ILabelProvider<ModelElement> {

    public void update(ViewerCell<ModelElement> cell) {
        ModelElement element = cell.getElement();
        cell.setText(getText(element));
        Image image = getImage(element);
        cell.setImage(image);
        cell.setBackground(getBackground(element));
        cell.setForeground(getForeground(element));
        cell.setFont(getFont(element));
    }

    public Font getFont(ModelElement element) {
        return null;
    }

    public Color getBackground(ModelElement element) {
        return null;
    }

    public Color getForeground(ModelElement element) {
        return null;
    }

    public Image getImage(ModelElement element) {
        return null;
    }

    public String getText(ModelElement element) {
        return element == null ? "" : element.toString();
    }
}
