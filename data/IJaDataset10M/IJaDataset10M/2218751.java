package fr.soleil.mambo.datasources.file;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Hashtable;
import fr.soleil.archiving.gui.tools.GUIUtilities;
import fr.soleil.commonarchivingapi.ArchivingTools.Tools.Criterions;
import fr.soleil.mambo.Mambo;
import fr.soleil.mambo.data.view.ViewConfiguration;
import fr.soleil.mambo.tools.xmlhelpers.XMLUtils;
import fr.soleil.mambo.tools.xmlhelpers.vc.ViewConfigurationXMLHelper;

public class XMLViewConfigurationManagerImpl implements IViewConfigurationManager {

    private String resourceLocation;

    private String defaultSaveLocation;

    private String saveLocation;

    private PrintWriter writer;

    protected XMLViewConfigurationManagerImpl() {
        startUp();
    }

    public void saveViewConfiguration(final ViewConfiguration vc, final Hashtable saveParameters) throws Exception {
        final String _location = getSaveLocation();
        writer = new PrintWriter(new FileWriter(_location, false));
        GUIUtilities.writeWithoutDate(writer, XMLUtils.XML_HEADER, true);
        GUIUtilities.writeWithoutDate(writer, vc.toString(), true);
        writer.close();
    }

    public ViewConfiguration[] loadViewConfigurations(final Criterions searchCriterions) throws Exception {
        return null;
    }

    public ViewConfiguration[] findViewConfigurations(final ViewConfiguration[] in, final Criterions searchCriterions) throws Exception {
        return null;
    }

    public void startUp() throws IllegalStateException {
        resourceLocation = null;
        boolean illegal = false;
        final String vcPath = Mambo.getPathToResources() + "/vc";
        File f = null;
        try {
            f = new File(vcPath);
            if (!f.canWrite()) {
                illegal = true;
            }
        } catch (final Exception e) {
            illegal = true;
        }
        if (illegal) {
            f.mkdir();
        }
        resourceLocation = vcPath;
        defaultSaveLocation = resourceLocation;
    }

    public void shutDown() throws Exception {
    }

    public ViewConfiguration loadViewConfiguration() throws Exception {
        final String _location = getSaveLocation();
        final ViewConfiguration vc = ViewConfigurationXMLHelper.loadViewConfigurationIntoHash(_location);
        return vc;
    }

    /**
     * @return Returns the defaultSaveLocation.
     */
    public String getDefaultSaveLocation() {
        return defaultSaveLocation;
    }

    /**
     * @param defaultSaveLocation
     *            The defaultSaveLocation to set.
     */
    public void setDefaultSaveLocation(final String defaultSaveLocation) {
        this.defaultSaveLocation = defaultSaveLocation;
    }

    /**
     * @return Returns the saveLocation.
     */
    public String getNonDefaultSaveLocation() {
        return saveLocation;
    }

    /**
     * @param saveLocation
     *            The saveLocation to set.
     */
    public void setNonDefaultSaveLocation(final String saveLocation) {
        this.saveLocation = saveLocation;
    }

    public String getSaveLocation() {
        final String _location = saveLocation == null ? defaultSaveLocation : saveLocation;
        return _location;
    }
}
