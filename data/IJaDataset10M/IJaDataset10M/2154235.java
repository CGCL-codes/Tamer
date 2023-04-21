package mipt.gui.choice;

import java.util.Map;
import javax.swing.table.DefaultTableCellRenderer;

/**
 * TableCellRenderer which displays rendered view getting from Map.
 * @author Korchak
 */
public class MapTableCellRenderer extends DefaultTableCellRenderer {

    private static final long serialVersionUID = 1243609729022728725L;

    private Map map = null;

    public MapTableCellRenderer() {
        super();
    }

    public MapTableCellRenderer(Map map) {
        super();
        this.map = map;
    }

    /**
	 * @return Returns the map.
	 */
    public Map getMap() {
        return map;
    }

    /**
	 * @param map The map to set.
	 */
    public void setMap(Map map) {
        this.map = map;
    }

    /**
	 * Overridden for displaying value form map.
	 * @see javax.swing.table.DefaultTableCellRenderer#setValue(java.lang.Object)
	 */
    protected void setValue(Object value) {
        super.setValue(map.get(value));
    }
}
