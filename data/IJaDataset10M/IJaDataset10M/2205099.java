package edu.psu.bd.math.grooms;

import java.util.concurrent.ExecutionException;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingWorker;
import org.cgsuite.util.Grid;

/**
 *
 * @author jfp149
 */
public class GRoomsWorker extends SwingWorker<Grid[], Void> {

    private final GoRoom gr;

    /** Creates a new instance of GRoomsWorker */
    public GRoomsWorker(GoRoom gr) {
        this.gr = gr;
    }

    protected Grid[] doInBackground() throws Exception {
        gr.initialize();
        return gr.getGrids();
    }

    public GoRoom getGoRoom() {
        return gr;
    }
}
