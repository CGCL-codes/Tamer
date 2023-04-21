package com.mapquest.spatialbase.util.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Field.Index;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexWriter;
import com.mapquest.spatialbase.FloatLatLng;
import com.mapquest.spatialbase.LatLng;
import com.mapquest.spatialbase.lucene.search.SidEncoder;
import com.mapquest.spatialbase.lucene.search.SpatialConfig;
import com.mapquest.spatialbase.sid.SpatialIndexer;
import com.mapquest.spatialbase.sid.SpatialLevel;

/**
 * Simple class to load external files into a Lucene index
 * @author tlaurenzomq
 *
 */
public class POILoader {

    private IndexWriter indexWriter;

    private SidEncoder encoder = new SidEncoder(SpatialConfig.DEFAULT);

    private int maxLevel = 20;

    private int id;

    public POILoader(IndexWriter w) {
        this.indexWriter = w;
    }

    public void addPoi(double lat, double lng, String label) throws CorruptIndexException, IOException {
        LatLng ll = new FloatLatLng(lat, lng);
        Document doc = new Document();
        doc.add(new Field("id", String.valueOf(++id), Store.YES, Index.UN_TOKENIZED));
        doc.add(new Field("label", label, Store.YES, Index.TOKENIZED));
        encoder.addPointsToDocument(doc, ll);
        SpatialLevel[] levels = SpatialIndexer.calculateSpatialLevels(ll, maxLevel);
        for (SpatialLevel level : levels) {
            encoder.addSidsToDocument(doc, level.getSpatialIndex().getStringValue());
        }
        indexWriter.addDocument(doc);
    }

    public void indexGarminCSV(InputStream inStream) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(inStream, "UTF-8"));
        for (; ; ) {
            String line = in.readLine();
            if (line == null) break;
            line = line.trim();
            if (line.length() == 0) continue;
            int comma1 = line.indexOf(',');
            int comma2 = line.indexOf(',', comma1 + 1);
            if (comma1 < 0 || comma2 <= comma1) continue;
            String lngStr = line.substring(0, comma1).trim();
            String latStr = line.substring(comma1 + 1, comma2).trim();
            String remainder = line.substring(comma2 + 1).trim();
            if (remainder.startsWith("\"") && remainder.endsWith("\"")) remainder = remainder.substring(1, remainder.length() - 1);
            try {
                double lat = Double.parseDouble(latStr);
                double lng = Double.parseDouble(lngStr);
                addPoi(lat, lng, remainder);
            } catch (NumberFormatException e) {
            }
        }
    }
}
