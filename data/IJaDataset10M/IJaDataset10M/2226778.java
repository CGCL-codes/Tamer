package jsynoptic.plugins.merge;

import javax.swing.JOptionPane;
import jsynoptic.plugins.merge.JSAsynchronousMergeDSCollection.AsynchronousMergeDSCollectionInformation;
import jsynoptic.plugins.merge.JSSynchronousMergeDSCollection.SynchronousMergeDSCollectionInformation;
import jsynoptic.ui.JSynoptic;
import simtools.data.DataSource;
import simtools.data.DataSourceCollection;
import simtools.data.DataSourcePool;
import simtools.data.DataSourceProvider;
import simtools.data.merge.MergeDataException;
import simtools.ui.ReportingDialog;

/**
 * Get MergedCollection back from ids...
 * @author zxpletran007
 *
 */
public class JSMergeDSCollectionProvider implements DataSourceProvider {

    public Object getOptionalInformation(DataSource ds, DataSourceCollection dsc) {
        if (dsc instanceof JSAsynchronousMergeDSCollection) return ((JSAsynchronousMergeDSCollection) dsc).getMergeInformation();
        if (dsc instanceof JSSynchronousMergeDSCollection) return ((JSSynchronousMergeDSCollection) dsc).getMergeInformation();
        return null;
    }

    public DataSource provide(String id, String dscId, Object optionalInformation, DataSourcePool pool) {
        if ((id == null) || (dscId == null) || !(dscId.startsWith(JSAsynchronousMergeDSCollection.ID_MARKER) || dscId.startsWith(JSSynchronousMergeDSCollection.ID_MARKER))) return null;
        DataSource foundDS = null;
        DataSourceCollection adsc = null;
        MergeDataException.mergeDataErrors.clear();
        try {
            if (dscId.startsWith(JSAsynchronousMergeDSCollection.ID_MARKER)) adsc = new JSAsynchronousMergeDSCollection((AsynchronousMergeDSCollectionInformation) optionalInformation); else adsc = new JSSynchronousMergeDSCollection((SynchronousMergeDSCollectionInformation) optionalInformation);
            if ((adsc == null) || !(adsc instanceof DataSourceCollection)) {
                return null;
            }
            for (int i = 0; i < adsc.size(); ++i) {
                if (id.equals(adsc.getInformation(i).id)) {
                    foundDS = (DataSource) adsc.get(i);
                    break;
                }
            }
        } catch (Exception e) {
        }
        if (!MergeDataException.mergeDataErrors.isEmpty()) {
            JOptionPane.showMessageDialog(JSynoptic.gui.getOwner(), new ReportingDialog("Following errors have occured during merging process:", MergeDataException.mergeDataErrors), "Merging process errors", JOptionPane.INFORMATION_MESSAGE);
        }
        if ((foundDS != null) && (adsc != null) && (pool != null)) pool.addDataSourceCollection(adsc);
        return foundDS;
    }
}
