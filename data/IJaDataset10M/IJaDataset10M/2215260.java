package glaceo.gui.client.vc.widgets;

import java.util.List;
import com.extjs.gxt.ui.client.Style.SortDir;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.form.ComboBox;
import glaceo.gui.client.model.dao.GJsSeason;
import glaceo.gui.client.rpc.GRpcResultReceiver;
import glaceo.gui.client.vc.GContentController;

/**
 * Implements a combo box that allows to select a season.
 *
 * @version $Id$
 * @author jjanke
 */
public class GSeasonSelector extends LayoutContainer implements GRpcResultReceiver<List<GJsSeason>> {

    private GContentController d_controller;

    private ListStore<GJsSeason> d_dataStore;

    private ComboBox<GJsSeason> d_comboBox;

    private boolean d_fInitialised;

    /**
   * Creates a new season selection combo-box.
   */
    public GSeasonSelector(final GContentController controller) {
        d_controller = controller;
        d_dataStore = new ListStore<GJsSeason>();
        d_fInitialised = false;
        buildComboBox();
        add(d_comboBox);
    }

    /**
   * Sets the seasons in the list to the combo-box.
   *
   * @param listSeasons the seasons to be set
   */
    public void setRpcData(final List<GJsSeason> listSeasons) {
        d_dataStore.removeAll();
        d_dataStore.add(listSeasons);
        d_dataStore.sort(GJsSeason.ATTR_ID, SortDir.DESC);
        d_comboBox.setValue(d_dataStore.getAt(0));
        d_fInitialised = true;
    }

    /**
   * Returns the actual combo-box widget. It may be used to listen to events (e.g. when
   * the selection changes).
   */
    public ComboBox<GJsSeason> getComboBox() {
        return d_comboBox;
    }

    /**
   * Indicates whether the underlying combo-box has been initialised or not.
   */
    public boolean isInitialised() {
        return d_fInitialised;
    }

    private void buildComboBox() {
        d_comboBox = new ComboBox<GJsSeason>();
        d_comboBox.setStore(d_dataStore);
        d_comboBox.setDisplayField(GJsSeason.NAME);
        d_comboBox.setValueField(GJsSeason.ATTR_ID);
        d_comboBox.setLazyRender(true);
        d_comboBox.setEditable(false);
        d_comboBox.setAllowBlank(false);
        d_comboBox.setForceSelection(true);
        d_comboBox.setWidth(200);
    }
}
