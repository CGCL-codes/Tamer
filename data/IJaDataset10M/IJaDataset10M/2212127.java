package org.vikamine.swing.subgroup.clustering;

import java.util.Collection;
import java.util.Collections;
import org.vikamine.kernel.subgroup.SG;

/**
 * @author Tobias Vogele
 */
public class SingleSGCluster extends SGCluster {

    private SG sg;

    public SingleSGCluster(SG sg) {
        this();
        setSg(sg);
    }

    public SingleSGCluster() {
        intraSimilarity = 1;
    }

    public SG getSg() {
        return sg;
    }

    public void setSg(SG sg) {
        this.sg = sg;
    }

    @Override
    public Collection getSubgroups() {
        return Collections.singletonList(getSg());
    }

    @Override
    public void calculateIntraSimilarity() {
        intraSimilarity = 1;
    }
}
