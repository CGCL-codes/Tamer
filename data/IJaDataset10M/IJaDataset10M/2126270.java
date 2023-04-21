package fr.soleil.mambo.datasources.file;

import java.util.Hashtable;
import fr.soleil.commonarchivingapi.ArchivingTools.Tools.Criterions;
import fr.soleil.mambo.data.view.ViewConfiguration;

public class DummyViewConfigurationManagerImpl implements IViewConfigurationManager {

    public void saveViewConfiguration(ViewConfiguration ac, Hashtable saveParameters) throws Exception {
    }

    public ViewConfiguration[] loadViewConfigurations(Criterions searchCriterions) throws Exception {
        return null;
    }

    public ViewConfiguration[] findViewConfigurations(ViewConfiguration[] in, Criterions searchCriterions) throws Exception {
        return null;
    }

    public void startUp() throws Exception {
    }

    public void shutDown() throws Exception {
    }

    public ViewConfiguration loadViewConfiguration() throws Exception {
        return null;
    }

    public String getDefaultSaveLocation() {
        return null;
    }

    public String getNonDefaultSaveLocation() {
        return null;
    }

    public void setNonDefaultSaveLocation(String location) {
    }

    public String getSaveLocation() {
        return null;
    }
}
