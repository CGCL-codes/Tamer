package net.sourceforge.seqware.queryengine.backend.store.impl;

import net.sourceforge.seqware.queryengine.backend.io.berkeleydb.tuplebinders.ConsequenceTB;
import net.sourceforge.seqware.queryengine.backend.io.berkeleydb.tuplebinders.CoverageTB;
import net.sourceforge.seqware.queryengine.backend.io.berkeleydb.tuplebinders.FeatureTB;
import net.sourceforge.seqware.queryengine.backend.io.berkeleydb.tuplebinders.StringIdTB;
import net.sourceforge.seqware.queryengine.backend.io.berkeleydb.tuplebinders.TagTB;
import net.sourceforge.seqware.queryengine.backend.io.berkeleydb.tuplebinders.VariantTB;
import net.sourceforge.seqware.queryengine.backend.model.Feature;
import net.sourceforge.seqware.queryengine.backend.util.SeqWareSettings;
import net.sourceforge.seqware.queryengine.backend.util.iterators.LocatableSecondaryCursorIterator;
import net.sourceforge.seqware.queryengine.backend.util.iterators.CursorIterator;
import net.sourceforge.seqware.queryengine.backend.util.iterators.SecondaryCursorIterator;
import net.sourceforge.seqware.queryengine.prototypes.hadoop.TagIndexKeyGenerator;
import com.sleepycat.bind.tuple.TupleBinding;
import com.sleepycat.db.DatabaseEntry;
import com.sleepycat.db.DatabaseException;
import java.io.IOException;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HConstants;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.client.tableindexed.IndexSpecification;
import org.apache.hadoop.hbase.client.tableindexed.IndexedTable;
import org.apache.hadoop.hbase.client.tableindexed.IndexedTableAdmin;
import org.apache.hadoop.hbase.client.tableindexed.IndexedTableDescriptor;
import org.apache.hadoop.hbase.filter.ColumnValueFilter;
import org.apache.hadoop.hbase.filter.SingleColumnValueFilter;
import org.apache.hadoop.hbase.filter.ValueFilter;
import org.apache.hadoop.hbase.filter.CompareFilter.CompareOp;
import org.apache.hadoop.hbase.ipc.IndexedRegionInterface;
import org.apache.hadoop.hbase.regionserver.tableindexed.IndexedRegionServer;
import org.apache.hadoop.hbase.util.Bytes;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Date;
import java.util.HashMap;

/**
 * @author boconnor
 * This is the backend implementation that uses HBase to store 
 * 
 * DEPRECATED: I found the secondary indexing difficult to use so I'm just populating index tables manually, this is no longer used, see HBaseStore instead
 * 
 */
public class HBaseStoreIndexed extends Object {

    protected HBaseConfiguration config = null;

    private IndexedTableAdmin admin = null;

    private IndexedTable genomeTable = null;

    private IndexedTable tagIndexTable = null;

    FeatureTB ftb = new FeatureTB();

    VariantTB mtb = new VariantTB();

    ConsequenceTB ctb = new ConsequenceTB();

    CoverageTB covtb = new CoverageTB();

    TagTB ttb = new TagTB();

    StringIdTB midtb = new StringIdTB();

    String genomeId = "032399";

    String referenceId = "hg18";

    public void setup(SeqWareSettings settings, String genomeId, String referenceId) throws FileNotFoundException, DatabaseException, Exception {
        config = new HBaseConfiguration();
        config.set(HConstants.REGION_SERVER_IMPL, IndexedRegionServer.class.getName());
        config.set(HConstants.REGION_SERVER_CLASS, IndexedRegionInterface.class.getName());
        config.setInt("hbase.master.info.port", -1);
        config.setInt("hbase.regionserver.info.port", -1);
        HTableDescriptor desc = new HTableDescriptor(referenceId + "Table");
        desc.addFamily(new HColumnDescriptor("variantFamily:"));
        desc.addFamily(new HColumnDescriptor("tagFamily:"));
        IndexedTableDescriptor indexDesc = new IndexedTableDescriptor(desc);
        IndexSpecification colIndex = new IndexSpecification("tagIndex", Bytes.toBytes("tagFamily:"));
        TagIndexKeyGenerator tikg = new TagIndexKeyGenerator(Bytes.toBytes("tagFamily:"));
        indexDesc.addIndex(colIndex);
        admin = new IndexedTableAdmin(config);
        admin.createIndexedTable(indexDesc);
        genomeTable = new IndexedTable(config, desc.getName());
        if (!admin.tableExists("hg18Table-tagIndexGenome" + genomeId)) {
        }
    }

    public CursorIterator getFeaturesUnordered() {
        return (null);
    }

    public CursorIterator getFeatures() {
        return (null);
    }

    public LocatableSecondaryCursorIterator getFeatures(String contig, int start, int stop) {
        return (null);
    }

    public Feature getFeature(String featureId) throws Exception {
        IndexedTable table = new IndexedTable(config, Bytes.toBytes(referenceId + "Table"));
        Get g = new Get(Bytes.toBytes(featureId));
        Result r = table.get(g);
        DatabaseEntry value = new DatabaseEntry(r.getValue(Bytes.toBytes("variantFamily"), Bytes.toBytes("Genome" + genomeId)));
        Feature feature = (Feature) ftb.entryToObject(value);
        System.out.println("GET: ID: " + featureId + " VALUE CONTIG: \"" + feature.getContig() + "\"");
        byte[] tags = r.getValue(Bytes.toBytes("tagFamily"), null);
        String valueStr = Bytes.toString(tags);
        System.out.println("GET: TAG: tagFamily: VALUE: \"" + valueStr + "\"");
        return (null);
    }

    public void getFeaturesByTag(String tag) throws Exception {
        IndexedTable table = new IndexedTable(config, Bytes.toBytes("hg18Table"));
    }

    public SecondaryCursorIterator getFeaturesTags() {
        return (null);
    }

    public synchronized String putFeature(Feature feature) {
        String id = null;
        try {
            DatabaseEntry value = new DatabaseEntry();
            ftb.objectToEntry(feature, value);
            byte[] data = value.getData();
            IndexedTable table = new IndexedTable(config, Bytes.toBytes(referenceId + "Table"));
            id = feature.getContig() + ":" + padZeros(feature.getStartPosition(), 10);
            Put p = new Put(Bytes.toBytes(id));
            p.add(Bytes.toBytes("variantFamily"), Bytes.toBytes("Genome" + genomeId), data);
            HashMap<String, String> tags = feature.getTags();
            StringBuffer tagStr = new StringBuffer();
            for (String key : tags.keySet()) {
                tagStr.append(key + "=" + tags.get(key) + " ");
            }
            p.add(Bytes.toBytes("tagFamily"), null, Bytes.toBytes(tagStr.toString().trim()));
            p.add(Bytes.toBytes("tagFamily"), Bytes.toBytes("Genome" + genomeId), Bytes.toBytes(tagStr.toString().trim()));
            table.put(p);
            table.flushCommits();
            table.close();
        } catch (Exception e) {
            System.err.println(e.toString());
        }
        System.out.println("PUT: ID: " + id);
        return (id);
    }

    private String padZeros(int input, int totalPlaces) throws Exception {
        String strInput = new Integer(input).toString();
        if (strInput.length() > totalPlaces) {
            throw new Exception("Integer " + input + " is larger than total places of " + totalPlaces + " so padding this string failed.");
        }
        int diff = totalPlaces - strInput.length();
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < totalPlaces; i++) {
            buffer.append("0");
        }
        buffer.append(strInput);
        return (buffer.toString());
    }
}
