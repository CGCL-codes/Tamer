package com.isa.jump.plugin.igor;

import java.awt.Dimension;
import java.io.File;
import java.util.List;
import javax.swing.JFrame;
import org.gdal.ogr.igor.Ogr2OgrObject;
import org.gdal.ogr.igor.OgrFileLocations;
import org.gdal.ogr.igor.OgrInfo;
import org.gdal.ogr.igor.OgrPluginExtra;
import org.gdal.ogr.igor.gui.OgrDialog;
import org.gdal.ogr.igor.gui.OgrProgressMonitor;
import org.gdal.ogr.igor.gui.TextPaneDialog;
import org.gdal.ogr.igor.gui.TargetDialog;
import com.vividsolutions.jump.util.FileUtil;
import com.vividsolutions.jump.workbench.WorkbenchContext;
import com.vividsolutions.jump.workbench.plugin.AbstractPlugIn;
import com.vividsolutions.jump.workbench.plugin.EnableCheckFactory;
import com.vividsolutions.jump.workbench.plugin.MultiEnableCheck;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;
import com.vividsolutions.jump.workbench.ui.GUIUtil;
import com.vividsolutions.jump.workbench.ui.MenuNames;

/**
 * @author Larry Becker
 * The JUMP method of invoking OgrInfo
 */
public class IGorPlugIn extends AbstractPlugIn {

    private final String TOP_LEVEL_MENU = MenuNames.FILE;

    private static PlugInContext context;

    private static String outputFolder = "";

    public void initialize(PlugInContext context) throws Exception {
        context.getFeatureInstaller().addMainMenuItem(this, new String[] { TOP_LEVEL_MENU }, "Convert / Reproject (OGR)..." + "{pos:9}", false, null, this.createEnableCheck(context.getWorkbenchContext()));
    }

    public MultiEnableCheck createEnableCheck(final WorkbenchContext workbenchContext) {
        EnableCheckFactory checkFactory = new EnableCheckFactory(workbenchContext);
        return new MultiEnableCheck().add(checkFactory.createTaskWindowMustBeActiveCheck());
    }

    public boolean execute(final PlugInContext context) throws Exception {
        try {
            OgrFileLocations locations = OgrFileLocations.getInstance(SkyJumpOgrLocation.getOgrLocation());
            String missingFiles = locations.getMissingFiles();
            if (missingFiles != null) {
                throw new Exception(missingFiles);
            }
            IGorPlugIn.context = context;
            WorkbenchContext wc = context.getWorkbenchContext();
            File loadDir = FileUtil.GetLoadDirectory(wc);
            OgrInfo ogrInfo = new OgrInfo();
            String[] cmdOutputLines = ogrInfo.getOgrInfo(loadDir, wc.getWorkbench().getFrame());
            if (cmdOutputLines == null) return false;
            String ogrInfoStatus = ogrInfo.getStatusLines(cmdOutputLines);
            Object[][] layerData = ogrInfo.getLayerData(cmdOutputLines);
            String[] layerColumns = ogrInfo.getLayerColumns();
            File fileSource = ogrInfo.getSourceFile();
            display(wc.getWorkbench().getFrame(), layerData, layerColumns, ogrInfoStatus, fileSource);
        } catch (Exception e) {
            context.getWorkbenchFrame().warnUser("Error: see output window");
            context.getWorkbenchFrame().getOutputFrame().createNewDocument();
            context.getWorkbenchFrame().getOutputFrame().addText(GoogleEarthKmlPlugIn.class + " " + e.toString());
        }
        return true;
    }

    public static void display(final JFrame frame, final Object[][] data, final String[] columnNames, final String statusText, final File fileSource) {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                createAndShowGUI(frame, data, columnNames, statusText, fileSource);
            }
        });
    }

    private static void createAndShowGUI(final JFrame frame, Object[][] data, String[] columnNames, final String statusText, File fileSource) {
        OgrDialog infoDialog = new OgrDialog(data, columnNames, statusText, frame, "OgrInfo", true);
        GUIUtil.centreOnWindow(infoDialog);
        infoDialog.setVisible(true);
        if (infoDialog.getOKStatus()) {
            TargetDialog targetDialog = new TargetDialog(frame, "Source & Output Options", true, infoDialog.getSelectedData(), fileSource.getAbsolutePath(), true, outputFolder);
            GUIUtil.centreOnWindow(targetDialog);
            targetDialog.setVisible(true);
            if (targetDialog.getOKStatus()) {
                List<Ogr2OgrObject> ogrItems = targetDialog.getOgr2OgrItems();
                if (ogrItems != null) {
                    OgrPluginExtra ogrPlugIn = getAddinPlugin(targetDialog.getLoadIntoJump(), frame);
                    OgrProgressMonitor progress = new OgrProgressMonitor(frame, ogrItems, ogrPlugIn);
                    progress.startOgrProgressMonitor();
                }
            }
        }
    }

    private static OgrPluginExtra getAddinPlugin(boolean loadIntoJump, final JFrame frame) {
        if (loadIntoJump) {
            return new OgrPluginExtra() {

                public void loadLayers(List<String> completedLayers, String ogrOutputString) {
                    TextPaneDialog result = new TextPaneDialog(frame, "Ogr2Ogr Command Output", true, ogrOutputString, new Dimension(600, 380));
                    GUIUtil.centreOnWindow(result);
                    result.setVisible(true);
                    LoadOgrLayersToJump ogrAddLayersToJump = new LoadOgrLayersToJump(context);
                    ogrAddLayersToJump.addLayers(completedLayers);
                }
            };
        }
        return new OgrPluginExtra() {

            public void loadLayers(List<String> completedLayers, String ogrOutputString) {
                TextPaneDialog result = new TextPaneDialog(frame, "Ogr2Ogr", true, ogrOutputString, new Dimension(600, 380));
                GUIUtil.centreOnWindow(result);
                result.setVisible(true);
            }
        };
    }
}
