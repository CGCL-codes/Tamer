package org.personalsmartspace.pm.merging.impl;

import java.util.ArrayList;
import org.personalsmartspace.log.impl.PSSLog;
import org.personalsmartspace.pm.prefmodel.api.platform.IPreference;

/**
 * @author Elizabeth
 *
 */
public class CommonNodeCounter {

    private final PSSLog logging = new PSSLog(this);

    private class NodeCounter {

        IPreference node;

        int counter;

        NodeCounter(IPreference ptn, int c) {
            this.node = ptn;
            this.counter = c;
        }

        public int getCounter() {
            return this.counter;
        }

        public IPreference getNode() {
            return this.node;
        }
    }

    ArrayList<NodeCounter> nc;

    public CommonNodeCounter() {
        nc = new ArrayList<NodeCounter>();
    }

    public void add(IPreference ptn, int counter) {
        this.nc.add(new NodeCounter(ptn, counter));
    }

    public IPreference getMostCommonNode() {
        int counter = 0;
        IPreference ptn = null;
        logging.debug("nc size:" + this.nc.size());
        for (int i = 0; i < this.nc.size(); i++) {
            int c = this.nc.get(i).getCounter();
            if (counter < c) {
                counter = c;
                ptn = this.nc.get(i).getNode();
            }
        }
        return ptn;
    }
}
