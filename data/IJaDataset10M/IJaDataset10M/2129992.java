package gui;

import java.awt.LayoutManager;

/**
 */
public abstract class DataEditGUI extends DataViewGUI {

    /**
     * 
     */
    public DataEditGUI() {
        super();
    }

    /**
     * @param isDoubleBuffered
     */
    public DataEditGUI(boolean isDoubleBuffered) {
        super(isDoubleBuffered);
    }

    /**
     * @param layout
     */
    public DataEditGUI(LayoutManager layout) {
        super(layout);
    }

    /**
     * @param layout
     * @param isDoubleBuffered
     */
    public DataEditGUI(LayoutManager layout, boolean isDoubleBuffered) {
        super(layout, isDoubleBuffered);
    }

    public abstract DataEditGUI edit(jdb.DataStructure toEdit);

    public DataViewGUI show(jdb.DataStructure showIt) {
        return edit(showIt);
    }
}
