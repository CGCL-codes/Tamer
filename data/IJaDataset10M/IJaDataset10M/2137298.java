package es.gva.cit.gvsig.gazetteer.gui;

import javax.swing.JDialog;
import org.gvsig.i18n.Messages;
import com.iver.andami.ui.mdiManager.IWindow;
import com.iver.andami.ui.mdiManager.WindowInfo;
import es.gva.cit.gazetteer.GazetteerClient;
import es.gva.cit.gazetteer.querys.Feature;
import es.gva.cit.gazetteer.querys.GazetteerQuery;
import es.gva.cit.gazetteer.ui.showresults.ShowResultsDialogPanel;
import es.gva.cit.gvsig.gazetteer.loaders.FeatureLoader;

/**
 * @author Jorge Piera Llodra (piera_jor@gva.es)
 */
public class ShowResultsDialog extends ShowResultsDialogPanel implements IWindow {

    private SearchDialog searchDialog = null;

    private JDialog frame = null;

    /**
     * @param client
     * @param features
     * @param recordsByPage
     */
    public ShowResultsDialog(JDialog frame, GazetteerClient client, Feature[] features, int recordsByPage, GazetteerQuery query) {
        super(client, features, recordsByPage, query);
        this.frame = frame;
    }

    public WindowInfo getWindowInfo() {
        WindowInfo m_viewinfo = new WindowInfo(WindowInfo.MODALDIALOG);
        m_viewinfo.setTitle(Messages.getText("gazetteer_search"));
        return m_viewinfo;
    }

    public Object getWindowProfile() {
        return WindowInfo.DIALOG_PROFILE;
    }

    public void closeButtonActionPerformed() {
        closeJDialog();
    }

    public void closeJDialog() {
        frame.setVisible(false);
    }

    /**
    * This method have to load the selected feature into gvSIG
    */
    public void loadButtonActionPerformed() {
        Feature feature = ppalPanel.getFeature();
        if (feature != null) {
            new FeatureLoader(client.getProjection()).load(feature, query);
        }
        closeJDialog();
    }

    /**
     * @return Returns the searchDialog.
     */
    public SearchDialog getSearchDialog() {
        return searchDialog;
    }

    /**
     * @param searchDialog The searchDialog to set.
     */
    public void setSearchDialog(SearchDialog searchDialog) {
        this.searchDialog = searchDialog;
    }
}
