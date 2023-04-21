package io.hbase.writers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.mapreduce.RecordWriter;
import org.apache.hadoop.mapreduce.TaskAttemptContext;
import utils.NumberUtils;
import data.Triple;
import data.TripleSource;

public class HBaseTriplesRecordWriter extends RecordWriter<TripleSource, Triple> {

    private static final int SIZE_CACHE = 1000;

    private HTable spocTable = null;

    private HTable ospcTable = null;

    private HTable poscTable = null;

    private List<Put> spocList = new ArrayList<Put>(SIZE_CACHE);

    private List<Put> ospcList = new ArrayList<Put>(SIZE_CACHE);

    private List<Put> poscList = new ArrayList<Put>(SIZE_CACHE);

    private byte[] bTriple = new byte[29];

    private static final byte[] COLUMN_FAMILY = "S".getBytes();

    private static final byte[] COLUMN_QUALIFIER = "N".getBytes();

    private byte[] bSource = new byte[5];

    public HBaseTriplesRecordWriter() throws IOException {
        spocTable = new HTable("spoc");
        poscTable = new HTable("posc");
        ospcTable = new HTable("ospc");
    }

    private void writeLists(int bufferSize) throws IOException {
        if (spocList.size() > bufferSize) {
            spocTable.put(spocList);
            poscTable.put(poscList);
            ospcTable.put(ospcList);
            spocList.clear();
            poscList.clear();
            ospcList.clear();
        }
    }

    @Override
    public void close(TaskAttemptContext context) throws IOException, InterruptedException {
        writeLists(0);
        spocTable.close();
        poscTable.close();
        ospcTable.close();
    }

    @Override
    public void write(TripleSource key, Triple value) throws IOException, InterruptedException {
        bSource[0] = key.getDerivation();
        NumberUtils.encodeInt(bSource, 1, key.getStep());
        if (key.getStep() == 0) bTriple[28] = 1; else bTriple[28] = 0;
        NumberUtils.encodeLong(bTriple, 0, value.getSubject());
        NumberUtils.encodeLong(bTriple, 8, value.getPredicate());
        NumberUtils.encodeLong(bTriple, 16, value.getObject());
        Put put = new Put(bTriple);
        put.add(COLUMN_FAMILY, COLUMN_QUALIFIER, bSource);
        spocList.add(put);
        NumberUtils.encodeLong(bTriple, 0, value.getObject());
        NumberUtils.encodeLong(bTriple, 8, value.getSubject());
        NumberUtils.encodeLong(bTriple, 16, value.getPredicate());
        put = new Put(bTriple);
        put.add(COLUMN_FAMILY, COLUMN_QUALIFIER, bSource);
        ospcList.add(put);
        NumberUtils.encodeLong(bTriple, 0, value.getPredicate());
        NumberUtils.encodeLong(bTriple, 8, value.getObject());
        NumberUtils.encodeLong(bTriple, 16, value.getSubject());
        put = new Put(bTriple);
        put.add(COLUMN_FAMILY, COLUMN_QUALIFIER, bSource);
        poscList.add(put);
        writeLists(SIZE_CACHE - 1);
    }
}
