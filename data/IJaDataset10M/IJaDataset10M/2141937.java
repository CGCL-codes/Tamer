package net.sf.osadm.linedata;

import net.sf.osadm.linedata.domain.CommentRow;
import net.sf.osadm.linedata.domain.FieldDataRow;
import net.sf.osadm.linedata.domain.TableDataContainer;
import net.sf.osadm.linedata.domain.TableDataFilter;

/**
 * Implements {@link TableDataVisitor} which fills a {@link TableDataContainer}.
 * 
 * @author T. Verhagen
 */
public class LineDataContainerFiller extends AbstractTableDataVisitor {

    /** The container to fill. */
    private TableDataContainer container;

    /**
     * 
     * @param  filter  The filter.
     * @param  container  The container to fill.
     */
    public LineDataContainerFiller(TableDataFilter filter, TableDataContainer container) {
        super(filter);
        this.container = container;
    }

    /**
     * Adds the visited {@link FieldDataRow} instance to the container. 
     * @param  fieldDataRow  The {@code FieldDataRow} instance to add.
     */
    public void visit(FieldDataRow fieldDataRow) {
        container.add(fieldDataRow);
    }

    /**
     * Adds the visited {@link CommentRow} instance to the container. 
     * @param  commentRow  The {@code CommentRow} instance to add.
     */
    public void visit(CommentRow commentRow) {
        container.add(commentRow);
    }
}
