package com.mapquest.spatialbase.lucene.cache;

import java.io.IOException;
import java.lang.ref.WeakReference;
import org.apache.lucene.index.IndexReader;
import com.mapquest.spatialbase.lucene.search.SidEncoder;

/**
 * Sid-data that lazily gets its lat/lngs when it detects the Integer.MAX_VALUE
 * sentinel value.
 * @author tlaurenzomq
 *
 */
public class LazySidData extends SidData {

    private WeakReference<IndexReader> readerReference;

    private SidEncoder encoder;

    public LazySidData(IndexReader reader, SidEncoder encoder, int capacity) {
        super(capacity);
        this.readerReference = new WeakReference<IndexReader>(reader);
        this.encoder = encoder;
    }

    @Override
    public int lat(int i) {
        int ret = super.lat(i);
        if (ret == Integer.MAX_VALUE) {
            int[] ll = loadLatLng(i);
            return ll[0];
        } else {
            return ret;
        }
    }

    @Override
    public int lng(int i) {
        int ret = super.lng(i);
        if (ret == Integer.MAX_VALUE) {
            int[] ll = loadLatLng(i);
            return ll[1];
        } else {
            return ret;
        }
    }

    private int[] loadLatLng(int i) {
        int doc = docId(i);
        int[] ll = new int[2];
        try {
            IndexReader reader = readerReference.get();
            if (reader == null) {
                throw new IllegalStateException("Attempt to lazily load lat/lng after the IndexReader has gone out of scope");
            }
            encoder.loadPoint(reader, doc, ll);
            setLatLng(i, ll[0], ll[1]);
        } catch (IOException e) {
            throw new RuntimeException("Error lazy loading lat/lng", e);
        }
        return ll;
    }
}
