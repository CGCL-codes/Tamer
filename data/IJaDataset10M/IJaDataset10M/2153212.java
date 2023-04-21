package neo.aoi;

import buoy.widget.BDialog;
import buoy.widget.BFrame;
import buoy.widget.BProgressBar;

/**
 * <p>
 * ExportProgressBar displays the status of an export operation. It is a
 * horizontal bar that shows the status of the export operation. It displays a
 * text which says how many objects are being exported and how many are left for
 * exporting.
 * </p>
 * 
 * <p>
 * In order to avoid GUI freezing, it is recommended that ExportProgressBar in
 * the AWT event dispatching thread while the process it monitors is run on a
 * separate thread (SwingWorker Thread). Notice that the helper thread will need
 * to use the ExportProgressBar as an ExportProgressListener in order to notify
 * it of the required changes to the current displayed value.
 * </p>
 * 
 * <p>
 * Example: <code>
 * 	<br>ExportProgressBar bar = new ExportProgressBar(parent);
 * 	<br>SwingWorker worker = new SwingWorkerExporter(bar);
 * 	<br>SwingUtilities.invokeLater(bar);
 * 	<br>worker.execute();
 * </code>
 * </p>
 * 
 * @see <a
 *      href="http://java.sun.com/docs/books/tutorial/uiswing/components/progress.html">How
 *      to Use Progress Bars</a>
 * 
 * @author Carlos Martinez
 */
public class ExportProgressBar extends BDialog implements ExportProgressListener, Runnable {

    private int _numObjs;

    private int _currObj;

    private BProgressBar _bar;

    /**
	 * Creates an object of class ExportProgressBar. ExportProgressBar will be
	 * created as a modal dialog window using the given BFrame as its parent.
	 * 
	 * @param parent
	 *            The parent of the modal dialog to be created.
	 */
    public ExportProgressBar(BFrame parent) {
        super(parent, true);
        _numObjs = 0;
        _currObj = 0;
        _bar = new BProgressBar(0, 1);
        _bar.setShowProgressText(true);
    }

    /**
	 * This method is called to start displaying the ExportProgressBar. This
	 * method is implemented from the Runnable interface and thus allows
	 * ExportProgressDialog to be run using SwingUtilities.invokeLater(Runnable)
	 */
    public void run() {
        setContent(_bar);
        pack();
        setVisible(true);
    }

    public void alertObject(int currObj, int totalObjects) {
        _currObj = currObj;
        _numObjs = totalObjects;
        _bar.setIndeterminate(true);
        _bar.setProgressText("Exporting object " + _currObj + (_numObjs <= 0 ? "" : (" of " + _numObjs)) + "...");
        _bar.repaint();
    }

    public void finished() {
        _bar.setMaximum(1);
        _bar.setValue(1);
        _bar.setProgressText("Finished");
        setVisible(false);
    }
}
