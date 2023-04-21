package streamcruncher.innards.core.partition.inmem;

import java.util.List;
import streamcruncher.api.artifact.TableFQN;
import streamcruncher.innards.core.partition.Row;
import streamcruncher.util.AppendOnlyPrimitiveLongList;

public interface InMemPartitionDataProducer {

    public TableFQN getTargetTableFQN();

    /**
     * @return <code>null</code> if there was nothing.
     */
    public List<Row> retrieveNewRowsInBatch();

    /**
     * @return <code>null</code> if there was nothing.
     */
    public AppendOnlyPrimitiveLongList retrieveDeadRowIdsInBatch();
}
