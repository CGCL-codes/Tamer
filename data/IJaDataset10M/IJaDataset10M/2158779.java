package preprocessing.methods.Import.databasedata.gui.components.panels;

import java.awt.Insets;
import java.awt.LayoutManager;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

/**
 * This class represents titled panel which can configures its Insets.
 *
 * @author Jiri Petnik
 */
public class TitledPanel extends BackgroundPanel {

    /**
     * Creates a new instance with default Insets.
     *
     * @param layout LayoutManager object
     * @param title panel's title
     */
    public TitledPanel(LayoutManager layout, String title) {
        super(layout, new Insets(20, 10, 13, 10));
        setBorder(new TitledBorder(new EtchedBorder(), title));
    }

    /**
     * Craates new instance with given Insets.
     *
     * @param layout LayoutManager object
     * @param title panel's title
     * @param i Insets object
     */
    public TitledPanel(LayoutManager layout, String title, Insets i) {
        super(layout, i);
        setBorder(new TitledBorder(new EtchedBorder(), title));
    }
}
