package com.iver.utiles.swing.threads;

import org.gvsig.topology.Messages;
import org.gvsig.topology.Topology;
import com.iver.cit.gvsig.fmap.MapContext;

public class TopologyValidationTask extends CancellableProgressTask {

    static final String NOTE_PREFIX = Messages.getText("VALIDANDO_REGLA");

    Topology topology;

    MapContext mapContext;

    public TopologyValidationTask(Topology topology, MapContext mapContext) {
        this.topology = topology;
        this.mapContext = mapContext;
        super.statusMessage = Messages.getText("VALIDANDO_TOPOLOGIA");
        super.currentNote = NOTE_PREFIX + Messages.getText(topology.getRule(0).getName());
    }

    public void run() throws Exception {
        topology.validate(this);
        if (topology.getNumberOfErrors() > 0 && !isCanceled()) {
            topology.updateErrorLyr();
        }
        finished = true;
    }

    public void finished() {
        super.finished = true;
    }
}
