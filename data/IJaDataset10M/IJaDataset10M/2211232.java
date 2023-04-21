package org.nbrowse.io;

import java.io.File;
import java.util.List;
import org.nbrowse.config.Config;
import org.nbrowse.dataset.MetaDataManager;
import org.nbrowse.dataset.Species;
import org.nbrowse.io.database.DatabaseManager;
import org.nbrowse.io.database.NbrowseJdbcAdapter;
import org.nbrowse.utils.ErrorUtil;

/**
 * manages data adapters
 * currently just 2 (sortof), TabFileAdapter, and almagem of DB&XML adapter that need
 * to be separated
 * @author mgibson
 */
public final class DataAdapterManager {

    /** for now hardwiring to nbrowse jdbc adapter, eventually should be configurable
   * or even a user chooser gui */
    private String ipAddress;

    private String user;

    private List<Species> speciesList;

    private DataAdapterI dataAdapter = NbrowseJdbcAdapter.inst();

    private DataAdapterI expansionAdapter;

    private DataAdapterI fileAdapter = RamAdapter.inst();

    private DataAdapterI modMineAdapter = ModMineAdapter.inst();

    private static DataAdapterManager singleton = new DataAdapterManager();

    private DataAdapterManager() {
    }

    public static DataAdapterManager inst() {
        return singleton;
    }

    /** this needs to be split up into: getExpansionAdapter, getNodeInfo/Atrribs,
   * getNewDataDb? getFile already exists
   * @return dataAdapter that does load, nodeInfo... */
    public static DataAdapterI getDataAdapter() {
        if (DatabaseManager.inst().getCurrentDB().isWebService()) return inst().modMineAdapter; else return inst().dataAdapter;
    }

    /** @return dataAdapterI that is in charge of doing expansions on the current adapter
   * this is either the data adapter that did the load (nbrowse-jdbc/db) or some adapter
   * that the loader is coordinating with (file->ramAdap) */
    public DataAdapterI getExpansionAdapter() {
        if (expansionAdapter == null) expansionAdapter = getDataAdapter();
        return expansionAdapter;
    }

    public void setIP(String ip) {
        ipAddress = ip;
    }

    public String getUser() {
        if (user != null) return user;
        return ipAddress;
    }

    public DataAdapterI getModMineAdapter() {
        return modMineAdapter;
    }

    DataAdapterI getFileAdapter(File file) {
        return fileAdapter;
    }

    /**
   * used to return current database adapter being used - instance method - currently bused by RamAdapter
   * @return
   */
    DataAdapterI getDBAdapter() {
        if (DatabaseManager.inst().getCurrentDB().isWebService()) return modMineAdapter; else return dataAdapter;
    }

    public void setExpansionAdapter(DataAdapterI e) {
        expansionAdapter = e;
    }

    public void loadSpeciesList() {
        try {
            speciesList = getDataAdapter().getSpeciesList();
        } catch (Exception e) {
            ErrorUtil.info(this, "No species found from datasource, using default");
            speciesList = Species.getDefaultSpeciesList();
        }
        boolean isDefaultFound = false;
        if (Config.inst().hasDefaultSpecies()) {
            int index = -1;
            String name = Config.inst().getDefaultSpecies();
            for (Species s : speciesList) {
                if (s.getLatinName().equals(name)) {
                    MetaDataManager.inst().setTaxonId(s.getId());
                    isDefaultFound = true;
                    break;
                } else {
                    int taxonID = s.getId();
                    int newTax = -1;
                    try {
                        newTax = Integer.parseInt(name);
                    } catch (NumberFormatException ex) {
                        continue;
                    }
                    if (taxonID == newTax) {
                        MetaDataManager.inst().setTaxonId(s.getId());
                        isDefaultFound = true;
                        break;
                    }
                }
            }
        }
        if (!isDefaultFound) MetaDataManager.inst().setTaxonId(speciesList.get(0).getId());
        StatusManager.inst().newSpeciesList();
    }

    public int getSelectedMetaDataTaxon() {
        return MetaDataManager.inst().getTaxonId();
    }

    public List<Species> getSpeciesList() {
        return speciesList;
    }

    /** returns null if taxId not found - shouldnt happen */
    public String speciesLatinForId(int taxonId) {
        for (Species s : getSpeciesList()) if (taxonId == s.getId()) return s.getLatinName();
        return null;
    }
}
