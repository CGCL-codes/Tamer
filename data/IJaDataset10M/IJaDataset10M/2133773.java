package org.jtools.shovel.api;

import org.jtools.shovel.meta.MetaData;
import org.jtools.shovel.meta.RecordDefinition;
import org.jtools.shovel.spi.TransformerSPI;

public interface Transformation {

    TransformerSPI getTransformer();

    RecordDefinition<MetaData> resolve(boolean forward, RecordDefinition<MetaData> metaData);
}
