package org.easyrec.plugin.slopeone.model;

import java.util.List;

/**
 * Stores the result of a deviation calculation for {@link org.easyrec.plugin.slopeone.DeviationCalculationStrategy}
 * <p><b>Company:&nbsp;</b> SAT, Research Studios Austria</p> <p><b>Copyright:&nbsp;</b> (c) 2007</p> <p><b>last
 * modified:</b><br/> $Author$<br/> $Date$<br/> $Revision$</p>
 *
 * @author Patrick Marschik
 */
public class DeviationCalculationResult {

    private List<Deviation> deviations;

    private int created;

    private int modified;

    public DeviationCalculationResult(final List<Deviation> deviations, final int created, final int modified) {
        this.deviations = deviations;
        this.created = created;
        this.modified = modified;
    }

    public int getCreated() {
        return created;
    }

    public List<Deviation> getDeviations() {
        return deviations;
    }

    public int getModified() {
        return modified;
    }
}
