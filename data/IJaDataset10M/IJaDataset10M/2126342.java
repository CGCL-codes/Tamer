package neo.aoi.octree;

import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import neo.aoi.ExportProgressBar;
import artofillusion.Scene;
import artofillusion.Translator;

/**
 * Voxel exporter plugin class. This class serves as a plugin for Art of
 * illusion and exports data in a voxel format which can then be loaded in
 * matlab. The output is a file which contains matlab matrices which represent
 * the 'Occupancy Grid' of the exported object.
 * 
 * @author Mauro Santos
 */
public class OctreeTranslator implements Translator {

    /**
	 * Specify whether this translator can export files.
	 * 
	 * @return Always true
	 */
    public boolean canExport() {
        return true;
    }

    /**
	 * Specify whether this translator can import files.
	 * 
	 * @return Always false
	 */
    public boolean canImport() {
        return false;
    }

    /**
	 * Prompt the user for a filename and any other necessary information, and
	 * export the scene. A progress bar is displayed while the process Scene is
	 * being exported
	 */
    public void exportFile(buoy.widget.BFrame parent, Scene theScene) {
        OctreeExporter.ExportParameters params = OctreeExporter.getExportParameters(parent);
        if (params.proceed()) {
            ExportProgressBar bar = new ExportProgressBar(parent);
            SwingWorker worker = new OctreeExportTask(params, theScene, parent, bar);
            SwingUtilities.invokeLater(bar);
            worker.execute();
        }
    }

    /**
	 * Get the name of the file format which this translator imports or exports.
	 */
    public String getName() {
        return "Octree Exporter";
    }

    /**
	 * Prompt the user to select a file, read it, and create a new LayoutWindow
	 * containing the imported scene.
	 */
    public void importFile(buoy.widget.BFrame parent) {
    }
}
