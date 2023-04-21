package edu.hawaii.ics.ami.app.estream.view;

import edu.hawaii.ics.ami.app.estream.model.DataCollection;
import java.util.Observable;
import java.util.Observer;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.JLabel;
import king.lib.access.ResourceHookup;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

/**
 * Visualization for the data collection status.
 *
 * @author   king
 * @since    October 21, 2004
 */
public class DataCollectionStatusPanel extends JPanel implements Observer, Runnable {

    /** The data collection associated with this panel. */
    private DataCollection dataCollection;

    /** The status label that shows the status wether recording or not. */
    private JLabel statusLabel;

    /** Waiting image. */
    private ImageIcon waitingImage;

    /** Recording image. */
    private ImageIcon recordingImage;

    /** Finished image. */
    private ImageIcon finishedImage;

    /** Empty image. */
    private ImageIcon emptyImage;

    /** Thread responsible for blinking. */
    private Thread thread;

    /** Defines if visible or not. */
    private boolean visible = true;

    /**
   * Constructor for the data collection status panel which shows the status of
   * data collection.
   * 
   * @param dataCollection  The data collection associated with this panel.
   */
    public DataCollectionStatusPanel(DataCollection dataCollection) {
        this.dataCollection = dataCollection;
        this.dataCollection.addObserver(this);
        FormLayout layout = new FormLayout("4dlu, fill:pref:grow, 4dlu", "2dlu, fill:pref:grow, 2dlu");
        setLayout(layout);
        CellConstraints cc = new CellConstraints();
        this.statusLabel = new JLabel("");
        add(this.statusLabel, cc.xy(2, 2));
        this.waitingImage = new ImageIcon(ResourceHookup.getInstance().getTrackedImage("conf/image/icon/pause.gif"));
        this.recordingImage = new ImageIcon(ResourceHookup.getInstance().getTrackedImage("conf/image/icon/recording.png"));
        this.finishedImage = new ImageIcon(ResourceHookup.getInstance().getTrackedImage("conf/image/icon/finished.gif"));
        this.emptyImage = new ImageIcon(ResourceHookup.getInstance().getTrackedImage("conf/image/icon/empty.gif"));
        update(dataCollection, null);
    }

    /**
   * Called, when the observable changed that this object is showing.
   * 
   * @param observable  The object that is being observed.
   * @param object  The change that occured.
   */
    public void update(Observable observable, Object object) {
        if (this.dataCollection.getStatus() == DataCollection.WAITING) {
            this.statusLabel.setText("Waiting for Connection ...");
            if (this.visible) {
                this.statusLabel.setIcon(this.waitingImage);
            } else {
                this.statusLabel.setIcon(this.emptyImage);
            }
            if (this.thread == null) {
                this.thread = new Thread(this);
                this.thread.start();
            }
        } else if (this.dataCollection.getStatus() == DataCollection.RECORDING) {
            this.statusLabel.setText("Recording ...");
            if (this.visible) {
                this.statusLabel.setIcon(this.recordingImage);
            } else {
                this.statusLabel.setIcon(this.emptyImage);
            }
        } else if (this.dataCollection.getStatus() == DataCollection.FINISHED) {
            this.statusLabel.setText("Data Recording Completed");
            this.statusLabel.setIcon(this.finishedImage);
            this.thread = null;
        } else if (this.dataCollection.getStatus() == DataCollection.INACTIVE) {
            this.thread = null;
        }
        revalidate();
        repaint();
    }

    /** 
   * Main method responsible for blinking.
   */
    public void run() {
        this.visible = true;
        Thread currentThread = Thread.currentThread();
        while (this.thread == currentThread) {
            try {
                Thread.sleep(700);
            } catch (InterruptedException e) {
                throw new IllegalThreadStateException(e.toString());
            }
            this.visible = !this.visible;
            update(this.dataCollection, null);
        }
        this.visible = true;
    }
}
