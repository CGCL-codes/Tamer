package de.huxhorn.lilith.swing.preferences.table;

import de.huxhorn.lilith.data.access.HttpStatus;
import de.huxhorn.lilith.data.logging.LoggingEvent;
import de.huxhorn.lilith.swing.table.ColorScheme;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.TableColumn;
import java.util.Map;

public class AccessStatusTypeColumnModel extends DefaultTableColumnModel {

    public static final int DEFAULT_COLUMN_INDEX_INDEX = 0;

    public static final int DEFAULT_COLUMN_INDEX_PREVIEW = 1;

    private static final String DEFAULT_COLUMN_NAME_INDEX = "#";

    private static final String DEFAULT_COLUMN_NAME_PREVIEW = "Preview";

    private AccessStatusTypePreviewRenderer previewRenderer;

    public AccessStatusTypeColumnModel() {
        super();
        {
            TableColumn col = new TableColumn(0);
            col.setHeaderValue(DEFAULT_COLUMN_NAME_INDEX);
            col.setCellRenderer(new IndexRenderer());
            addColumn(col);
        }
        {
            TableColumn col = new TableColumn(0);
            col.setHeaderValue(DEFAULT_COLUMN_NAME_PREVIEW);
            previewRenderer = new AccessStatusTypePreviewRenderer();
            col.setCellRenderer(previewRenderer);
            addColumn(col);
        }
    }

    public Map<HttpStatus.Type, ColorScheme> getSchemes() {
        return previewRenderer.getSchemes();
    }

    public void setSchemes(Map<HttpStatus.Type, ColorScheme> schemes) {
        previewRenderer.setSchemes(schemes);
    }
}
