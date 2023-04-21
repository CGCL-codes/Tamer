package org.gvt.action;

import java.util.ArrayList;
import java.util.Iterator;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.gvt.ChisioMain;
import org.gvt.util.GraphClusterMaker;
import org.ivis.layout.Cluster;

/**
 * Action for clustering a graph. The action clusters currently loaded graph
 * and colors it according to their clusters.
 *
 * @author Esat Belviranli
 *
 * Copyright: i-Vis Research Group, Bilkent University, 2007 - present
 */
public class ClusterGraphAction extends Action {

    ChisioMain main;

    public ClusterGraphAction(ChisioMain chisio) {
        super("Randomly Assign Clusters");
        this.setToolTipText("Randomly Assign Clusters");
        this.setImageDescriptor(ImageDescriptor.createFromFile(ChisioMain.class, "icon/blank.gif"));
        this.main = chisio;
    }

    public void run() {
        this.main.getHighlightLayer().clusterHighlights.clear();
        this.main.getRootGraph().getClusterManager().clearClusters();
        GraphClusterMaker clusterMaker = new GraphClusterMaker(this.main.getRootGraph(), 10, 10);
        clusterMaker.run();
        new ColorWithClusterIDAction(this.main).run();
        if (this.main.isClusterBoundShown) {
            Iterator<Cluster> iter = this.main.getRootGraph().getClusterManager().getClusters().iterator();
            while (iter.hasNext()) {
                this.main.getHighlightLayer().addHighlightToCluster(iter.next());
            }
        }
    }
}
