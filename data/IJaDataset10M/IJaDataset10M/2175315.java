package uk.ac.rothamsted.ovtk.GraphLibraryAdapter.yfiles;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import uk.ac.rothamsted.ovtk.Console.Console;
import y.module.OrganicEdgeRouterModule;
import y.module.YModule;
import y.view.Graph2DView;

/**
 * @author taubertj
 * 
 */
public class OrganicEdgeRoutingAction extends AbstractAction implements Runnable {

    private Thread t;

    private Graph2DView view;

    public OrganicEdgeRoutingAction(Graph2DView view) {
        super("Make Organic Edge Routing");
        this.view = view;
    }

    public void actionPerformed(ActionEvent ae) {
        if (t == null) {
            t = new Thread(this);
            t.start();
        }
    }

    public void run() {
        double start = System.currentTimeMillis();
        Console.println(0, "Start Organic Edge Routing.");
        Console.startProgress("Organic Edge Routing.");
        YModule module = new OrganicEdgeRouterModule();
        module.start(view.getGraph2D());
        Console.println(0, "Organic Edge Routing finished. - " + (System.currentTimeMillis() - start) / 1000 + " s");
        Console.stopProgress();
        t = null;
    }
}
