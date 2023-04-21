package eu.somatik.moviebrowser.gui;

import eu.somatik.moviebrowser.domain.MovieStatus;
import java.awt.Component;
import javax.swing.Icon;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

final class MovieStatusCellRenderer extends DefaultTableCellRenderer {

    private final Icon defaultIcon;

    private final Icon loadedIcon;

    private final Icon loadingIcon;

    private final Icon failedIcon;

    public MovieStatusCellRenderer(final IconLoader iconLoader) {
        super();
        this.defaultIcon = iconLoader.loadIcon("images/16/bullet_black.png");
        this.loadedIcon = iconLoader.loadIcon("images/16/bullet_green.png");
        this.loadingIcon = iconLoader.loadIcon("images/16/bullet_orange.png");
        this.failedIcon = iconLoader.loadIcon("images/16/bullet_red.png");
        setIcon(defaultIcon);
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        MovieStatus movieStatus = (MovieStatus) value;
        switch(movieStatus) {
            case NEW:
                setIcon(defaultIcon);
                break;
            case CACHED:
                setIcon(loadedIcon);
                break;
            case LOADED:
                setIcon(loadedIcon);
                break;
            case LOADING:
                setIcon(loadingIcon);
                break;
            case ERROR:
                setIcon(failedIcon);
                break;
        }
        setText(null);
        return this;
    }
}
