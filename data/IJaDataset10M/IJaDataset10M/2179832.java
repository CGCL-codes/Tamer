package com.isa.jump.plugin;

import java.io.File;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import com.vividsolutions.jump.io.datasource.DataSource;
import com.vividsolutions.jump.io.datasource.DataSourceQuery;
import com.vividsolutions.jump.workbench.WorkbenchContext;
import com.vividsolutions.jump.workbench.model.Layer;
import com.vividsolutions.jump.workbench.plugin.AbstractPlugIn;
import com.vividsolutions.jump.workbench.plugin.EnableCheckFactory;
import com.vividsolutions.jump.workbench.plugin.MultiEnableCheck;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;
import com.vividsolutions.jump.workbench.ui.plugin.FeatureInstaller;

public class SynchPathWithLayerName extends AbstractPlugIn {

    private static final String CONFIRMMESSAGE = "Do you want to rename the files associated " + "with the selected layers?";

    private static final String FILEMSG = "The file associated with this layer ";

    private static final String FILEREADONLY = FILEMSG + "is read only.";

    private static final String FILENOTEXISTS = FILEMSG + "does not exist.";

    private static final String[] SHPFILEEXTS = { "shp", "shx", "dbf", "prj", "sbn", "sbx", "shp.xml", "xml" };

    private void syncronizeLayeWithFile(Layer changedLayer) {
        DataSourceQuery dsq = changedLayer.getDataSourceQuery();
        if (dsq == null) return;
        Object fnameObj = dsq.getDataSource().getProperties().get(DataSource.FILE_KEY);
        if (fnameObj == null) return;
        String dsqSourceClass = dsq.getDataSource().getClass().getName();
        int dotPos = dsqSourceClass.lastIndexOf(".");
        if (dotPos > 0) dsqSourceClass = dsqSourceClass.substring(dotPos + 1);
        dotPos = dsqSourceClass.lastIndexOf("$");
        if (dotPos > 0) dsqSourceClass = dsqSourceClass.substring(dotPos + 1);
        if (!dsqSourceClass.equals("Shapefile")) return;
        String fullPath = fnameObj.toString();
        dotPos = fullPath.lastIndexOf(File.separatorChar);
        String sourcePath = (dotPos > 0) ? fullPath.substring(dotPos + 1) : fullPath;
        String pathOnly = (dotPos > 0) ? fullPath.substring(0, dotPos) : fullPath;
        dotPos = sourcePath.lastIndexOf(".");
        String sourceName = (dotPos > 0) ? sourcePath.substring(0, dotPos) : sourcePath;
        String newLayerName = changedLayer.getName();
        if (newLayerName.equals(sourceName)) return;
        File shpfile = new File(fullPath);
        if (!shpfile.exists()) {
            JOptionPane.showMessageDialog(null, FILENOTEXISTS);
            return;
        }
        if (!shpfile.canWrite()) {
            JOptionPane.showMessageDialog(null, FILEREADONLY);
            return;
        }
        boolean success = true;
        for (int i = 0; i < SHPFILEEXTS.length; i++) {
            success &= renamePathName(pathOnly, sourceName, newLayerName, SHPFILEEXTS[i]);
        }
        String newDSPath = pathOnly + File.separatorChar + newLayerName + "." + SHPFILEEXTS[0];
        dsq.getDataSource().getProperties().put(DataSource.FILE_KEY, newDSPath);
        changedLayer.setDescription(newDSPath);
    }

    public void initialize(PlugInContext context) throws Exception {
        WorkbenchContext workbenchContext = context.getWorkbenchContext();
        FeatureInstaller featureInstaller = new FeatureInstaller(workbenchContext);
        JPopupMenu layerNamePopupMenu = workbenchContext.getWorkbench().getFrame().getLayerNamePopupMenu();
        featureInstaller.addPopupMenuItem(layerNamePopupMenu, this, "Rename Selected Datasets", false, null, createEnableCheck(workbenchContext));
    }

    public boolean execute(PlugInContext context) throws Exception {
        if (!confirm(context, CONFIRMMESSAGE)) return false;
        Layer[] selectedLayers = context.getSelectedLayers();
        for (int i = 0; i < selectedLayers.length; i++) {
            syncronizeLayeWithFile(selectedLayers[i]);
        }
        return true;
    }

    public static MultiEnableCheck createEnableCheck(WorkbenchContext workbenchContext) {
        EnableCheckFactory checkFactory = new EnableCheckFactory(workbenchContext);
        return new MultiEnableCheck().add(checkFactory.createWindowWithSelectionManagerMustBeActiveCheck()).add(checkFactory.createAtLeastNLayersMustBeSelectedCheck(1));
    }

    private static boolean confirm(PlugInContext context, String message) {
        return (JOptionPane.OK_OPTION == JOptionPane.showOptionDialog(context.getLayerViewPanel(), message, "Confirm", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null));
    }

    public boolean renamePathName(String pathOnly, String oldFileName, String newFileName, String ext) {
        File file = new File(pathOnly + File.separatorChar + oldFileName + "." + ext);
        File dest = new File(pathOnly + File.separatorChar + newFileName + "." + ext);
        return file.renameTo(dest);
    }
}
