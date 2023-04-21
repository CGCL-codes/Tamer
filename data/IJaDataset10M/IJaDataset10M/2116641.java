package net.sf.doolin.app.sc.game.dist;

import java.util.HashMap;
import java.util.Map;

public class SpatialDistributionDefinitionImpl implements SpatialDistributionDefinition {

    private static final long serialVersionUID = 1L;

    private final SpatialDistributionType type;

    private final Map<String, String> parameters;

    public SpatialDistributionDefinitionImpl(SpatialDistributionType type) {
        this.type = type;
        this.parameters = new HashMap<String, String>();
    }

    @Override
    public Map<String, String> getParameters() {
        return this.parameters;
    }

    @Override
    public SpatialDistributionType getType() {
        return this.type;
    }
}
