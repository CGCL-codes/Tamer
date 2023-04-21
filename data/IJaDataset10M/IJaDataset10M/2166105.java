package glaceo.gui.client.vc.widgets;

import java.util.List;
import com.extjs.gxt.ui.client.Style.SortDir;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.form.ComboBox;
import glaceo.gui.client.GGlaceoGui;
import glaceo.gui.client.model.dao.GJsContestRound;
import glaceo.gui.client.res.GI18NMessages;
import glaceo.gui.client.rpc.GRpcResultReceiver;
import glaceo.gui.client.vc.GContentController;

/**
 *
 * @version $Id$
 * @author jjanke
 */
public class GRoundSelector extends LayoutContainer implements GRpcResultReceiver<List<GJsContestRound>> {

    private GContentController d_controller;

    private ListStore<GJsContestRound> d_dataStore;

    private ComboBox<GJsContestRound> d_comboBox;

    private boolean d_fInitialised;

    /**
   * Creates a new season selection combo-box.
   */
    public GRoundSelector(final GContentController controller) {
        d_controller = controller;
        d_dataStore = new ListStore<GJsContestRound>();
        d_fInitialised = false;
        buildComboBox();
        add(d_comboBox);
    }

    /**
   * Sets the seasons in the list to the combo-box.
   *
   * @param listRounds the rounds to be set
   */
    public void setRpcData(final List<GJsContestRound> listRounds) {
        GI18NMessages msg = GGlaceoGui.messages();
        d_dataStore.removeAll();
        d_dataStore.add(listRounds);
        d_dataStore.sort(GJsContestRound.NUM, SortDir.ASC);
        for (int i = 0; i < listRounds.size(); i++) {
            GJsContestRound round = d_dataStore.getAt(i);
            round.setText(msg.roundWithNumber(round.getName(), round.getNum()));
            if (d_dataStore.getAt(i).isPreselected()) d_comboBox.setValue(d_dataStore.getAt(i));
        }
        d_fInitialised = true;
    }

    /**
   * Returns the actual combo-box widget. It may be used to listen to events (e.g. when
   * the selection changes).
   */
    public ComboBox<GJsContestRound> getComboBox() {
        return d_comboBox;
    }

    /**
   * Indicates whether the underlying combo-box has been initialised or not.
   */
    public boolean isInitialised() {
        return d_fInitialised;
    }

    private void buildComboBox() {
        d_comboBox = new ComboBox<GJsContestRound>();
        d_comboBox.setStore(d_dataStore);
        d_comboBox.setDisplayField(GJsContestRound.TEXT);
        d_comboBox.setValueField(GJsContestRound.ATTR_ID);
        d_comboBox.setLazyRender(true);
        d_comboBox.setEditable(false);
        d_comboBox.setAllowBlank(false);
        d_comboBox.setForceSelection(true);
        d_comboBox.setWidth(200);
    }
}
