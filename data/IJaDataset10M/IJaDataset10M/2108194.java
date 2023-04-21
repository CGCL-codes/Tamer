package orcajo.azada.discoverer.views;

import java.util.ArrayList;
import java.util.List;
import orcajo.azada.core.util.OlapUtil;
import org.olap4j.metadata.Dimension;

public class SharedDimension {

    private List<Dimension> dimensions;

    SharedDimension(List<Dimension> dimensions) {
        super();
        this.dimensions = new ArrayList<Dimension>();
        if (dimensions != null) {
            for (Dimension d : dimensions) {
                if (OlapUtil.isVisible(d)) {
                    this.dimensions.add(d);
                }
            }
        }
    }

    public List<Dimension> getDimensions() {
        return dimensions;
    }

    public String getName() {
        return Messages.SharedDimension_sharedDimensions;
    }
}
