package testingapplication.table;

import com.softaspects.jsf.component.table.Header;

/**
 * Title:TableHeaderBean
 * Description:Header for Table Component
 */
public class TableHeaderBean extends Header {

    /**
     * Constructor - assembling
     */
    public TableHeaderBean() {
        super();
        init();
    }

    private void init() {
        this.setHeight("40pt");
        this.setHintText("Use Shift/Ctrl and mouse-click for multiple column selection, click to sort");
    }
}
