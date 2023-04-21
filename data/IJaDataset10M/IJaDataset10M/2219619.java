package ru.susu.algebra.partition.writer;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import ru.susu.algebra.partition.Partition;
import ru.susu.algebra.partition.PartitionsCacheConstructor;
import ru.susu.algebra.partition.filter.AllPartitionsFilter;

/**
 * @author akargapolov
 * @since: 09.03.2009
 */
public class StandartAllPartitionsToWorkspaceWriter extends FormattedPartitionsWriter implements Runnable {

    private int _representedNumber;

    public StandartAllPartitionsToWorkspaceWriter(int representedNumber) {
        _representedNumber = representedNumber;
    }

    private void writePartitions() throws IOException {
        PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("ExecuteResults/Partitions/" + _representedNumber + ".txt")));
        List<Partition> partitions = new PartitionsCacheConstructor().getAscendingPartitions(_representedNumber, new AllPartitionsFilter());
        this.write(partitions, out);
        out.close();
    }

    @Override
    public void run() {
        try {
            this.writePartitions();
        } catch (IOException e) {
            e.printStackTrace(System.out);
        }
    }
}
