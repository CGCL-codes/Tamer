package org.fao.waicent.kids.editor;

import java.util.Iterator;
import java.util.Vector;
import org.fao.waicent.attributes.Attributes;
import org.fao.waicent.util.Log;
import org.fao.waicent.util.StringTable;
import org.fao.waicent.util.TableReader;
import org.fao.waicent.xmap2D.FeatureLayer;
import org.fao.waicent.xmap2D.FeatureProperties;

public abstract class ImportDatasetHandler {

    ImportDatasetWizard _wizard;

    int index = -1;

    private Log _log;

    public abstract int createDataset();

    public void setWizard(ImportDatasetWizard wiz) {
        _wizard = wiz;
    }

    public ImportDatasetWizard getWizard() {
        return _wizard;
    }

    public TableReader getTableReader() {
        return _wizard.getTableReader();
    }

    public void setTableReader(TableReader reader) {
        _wizard.setTableReader(reader);
    }

    public FeatureLayer getFeatureLayer() {
        return _wizard.getFeatureLayer();
    }

    public void setFeatureLayer(FeatureLayer layer) {
        _wizard.setFeatureLayer(layer);
    }

    public StringTable getStringTable() {
        return _wizard.getStringTable();
    }

    public void setStringTable(StringTable table) {
        _wizard.setStringTable(table);
    }

    public abstract String getCommandsString();

    public String getName() {
        return _wizard.getName();
    }

    public String getHeader() {
        return _wizard.getHeader();
    }

    public Log getLog() {
        return _log;
    }

    public void setLog(Log log) {
        _log = log;
    }

    public boolean isSuccessfulParse() {
        if (hasLogErrors()) {
            return false;
        }
        return true;
    }

    public boolean hasLogErrors() {
        if (getLog() != null && getLog().errorsOccurred()) {
            String err = "An error occured in creating the dataset.  Please return to the first step and re-import the file.";
            err += getLog().dumptoString(Log.LEVEL_ERROR);
            this._wizard._ele.setAttribute("error_message", err);
            return true;
        }
        return false;
    }

    public Vector getLayerCodes() {
        Vector layerCodes = new Vector();
        Iterator feature_iterator = getFeatureLayer().getFeaturePropertiesIterator();
        if (feature_iterator != null) {
            while (feature_iterator.hasNext()) {
                FeatureProperties fp = (FeatureProperties) feature_iterator.next();
                if (!fp.code.equals("") && !fp.code.equals(null)) {
                    layerCodes.add(fp.code);
                }
            }
        }
        return layerCodes;
    }

    public abstract Attributes getAttributes();

    public void setDatasetSecurity(int dataset_access, int dataset_network_access) {
        _wizard.getWizard().getAttributes().setAccessType(dataset_access);
        _wizard.getWizard().getAttributes().setNetworkAccessType(dataset_network_access);
    }
}
