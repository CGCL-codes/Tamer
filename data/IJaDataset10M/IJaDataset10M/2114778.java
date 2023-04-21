package org.catacombae.jparted.lib.ps.gpt;

import org.catacombae.hfsexplorer.partitioning.GUIDPartitionTable;
import org.catacombae.io.ReadableRandomAccessStream;
import org.catacombae.hfsexplorer.partitioning.PartitionSystem;
import org.catacombae.jparted.lib.DataLocator;
import org.catacombae.jparted.lib.ps.Partition;
import org.catacombae.jparted.lib.ps.PartitionSystemHandler;
import org.catacombae.jparted.lib.ps.StandardPartition;

/**
 *
 * @author erik
 */
public class GPTHandler extends PartitionSystemHandler {

    private DataLocator partitionData;

    public GPTHandler(DataLocator partitionData) {
        this.partitionData = partitionData;
    }

    @Override
    public long getPartitionCount() {
        GUIDPartitionTable gpt = readPartitionTable();
        return gpt.getUsedPartitionCount();
    }

    @Override
    public Partition[] getPartitions() {
        PartitionSystem partitionTable = readPartitionTable();
        Partition[] result = new Partition[partitionTable.getUsedPartitionCount()];
        org.catacombae.hfsexplorer.partitioning.Partition[] parts = partitionTable.getUsedPartitionEntries();
        for (int i = 0; i < result.length; ++i) {
            result[i] = new StandardPartition(parts[i].getStartOffset(), parts[i].getLength(), parts[i].getType());
        }
        return result;
    }

    @Override
    public void close() {
    }

    private GUIDPartitionTable readPartitionTable() {
        ReadableRandomAccessStream llf = null;
        try {
            llf = partitionData.createReadOnlyFile();
            GUIDPartitionTable gpt = new GUIDPartitionTable(llf, 0);
            if (gpt.isValid()) return gpt; else return null;
        } finally {
            if (llf != null) llf.close();
        }
    }
}
