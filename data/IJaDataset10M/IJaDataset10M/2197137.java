package de.hpi.eworld.importer.file;

import java.io.FileInputStream;
import org.apache.log4j.Logger;
import com.trolltech.qt.QSignalEmitter;
import de.hpi.eworld.core.ModelManager;

/**
 * @author Jonas Truemper
 *
 */
public class OsmThread extends QSignalEmitter implements Runnable {

    /**
	 * the file to import the map from
	 */
    private String fileName;

    /**
	 * Checkbox for pedestrian way importing
	 */
    private boolean pedestrian;

    /**
	 * Checkbox for filtering cyclic edges
	 */
    private boolean filterCyclic;

    /**
	 * done signal
	 * @author Jonas Truemper
	 */
    public Signal0 done = new Signal0();

    /**
	 * failed signal
	 * @author Jonas Truemper
	 */
    public Signal0 failed = new Signal0();

    /**
	 * empty signal
	 * @author Jonas Truemper
	 */
    public Signal0 nothing = new Signal0();

    /**
	 * for progress indication
	 */
    public Signal1<Integer> progress = new Signal1<Integer>();

    /**
	 * the osm xml handler
	 */
    private Osm2Model osm;

    /**
	 * @author Jonas Truemper
	 * @param fileName the maps filename
	 * @param pedestrian true if pedestrian ways shall be imported
	 * @param filterCyclic true if cyclic edges shall be imported
	 */
    public OsmThread(String fileName, boolean pedestrian, boolean filterCyclic) {
        this.fileName = fileName;
        this.pedestrian = pedestrian;
        this.filterCyclic = filterCyclic;
    }

    /**
	 * @author Jonas Truemper
	 * @see java.lang.Runnable#run()
	 */
    public void run() {
        try {
            ModelManager.getInstance().startBatchProcess.emit();
            osm = new Osm2Model(pedestrian, filterCyclic);
            osm.progress.connect(this, "progress(int)");
            osm.parseFile(new FileInputStream(fileName));
            if (osm.somethingImported()) {
                done.emit();
            } else {
                nothing.emit();
            }
            if (!osm.wasInterrupted()) ModelManager.getInstance().endBatchProcess.emit();
        } catch (Exception e) {
            Logger.getLogger(this.getClass()).error("Error occured during OSM file import", e);
            failed.emit();
        }
    }

    /**
	 * @author Jonas Truemper
	 * @param progress the import's progress
	 */
    @SuppressWarnings("unused")
    private void progress(int progress) {
        this.progress.emit(progress);
    }

    /**
	 * stops the import by calling the handlers interrupt method.
	 * @see Osm2Model#interrupt()
	 */
    public void stopImport() {
        if (osm != null) osm.interrupt();
    }
}
