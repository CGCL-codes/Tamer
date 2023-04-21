package com.lucene.index;

import java.util.Vector;
import java.io.IOException;
import com.lucene.store.Directory;
import com.lucene.store.OutputStream;
import com.lucene.store.InputStream;
import com.lucene.document.Document;
import com.lucene.util.PriorityQueue;
import com.lucene.util.BitVector;

final class SegmentMerger {

    private Directory directory;

    private String segment;

    private Vector readers = new Vector();

    private FieldInfos fieldInfos;

    SegmentMerger(Directory dir, String name) {
        directory = dir;
        segment = name;
    }

    final void add(SegmentReader reader) {
        readers.addElement(reader);
    }

    final SegmentReader segmentReader(int i) {
        return (SegmentReader) readers.elementAt(i);
    }

    final void merge() throws IOException {
        try {
            mergeFields();
            mergeTerms();
            mergeNorms();
        } finally {
            for (int i = 0; i < readers.size(); i++) {
                SegmentReader reader = (SegmentReader) readers.elementAt(i);
                reader.close();
            }
        }
    }

    private final void mergeFields() throws IOException {
        fieldInfos = new FieldInfos();
        for (int i = 0; i < readers.size(); i++) {
            SegmentReader reader = (SegmentReader) readers.elementAt(i);
            fieldInfos.add(reader.fieldInfos);
        }
        fieldInfos.write(directory, segment + ".fnm");
        FieldsWriter fieldsWriter = new FieldsWriter(directory, segment, fieldInfos);
        try {
            for (int i = 0; i < readers.size(); i++) {
                SegmentReader reader = (SegmentReader) readers.elementAt(i);
                BitVector deletedDocs = reader.deletedDocs;
                int maxDoc = reader.maxDoc();
                for (int j = 0; j < maxDoc; j++) if (deletedDocs == null || !deletedDocs.get(j)) fieldsWriter.addDocument(reader.document(j));
            }
        } finally {
            fieldsWriter.close();
        }
    }

    private OutputStream freqOutput = null;

    private OutputStream proxOutput = null;

    private TermInfosWriter termInfosWriter = null;

    private SegmentMergeQueue queue = null;

    private final void mergeTerms() throws IOException {
        try {
            freqOutput = directory.createFile(segment + ".frq");
            proxOutput = directory.createFile(segment + ".prx");
            termInfosWriter = new TermInfosWriter(directory, segment, fieldInfos);
            mergeTermInfos();
        } finally {
            if (freqOutput != null) freqOutput.close();
            if (proxOutput != null) proxOutput.close();
            if (termInfosWriter != null) termInfosWriter.close();
            if (queue != null) queue.close();
        }
    }

    private final void mergeTermInfos() throws IOException {
        queue = new SegmentMergeQueue(readers.size());
        int base = 0;
        for (int i = 0; i < readers.size(); i++) {
            SegmentReader reader = (SegmentReader) readers.elementAt(i);
            SegmentTermEnum termEnum = (SegmentTermEnum) reader.terms();
            SegmentMergeInfo smi = new SegmentMergeInfo(base, termEnum, reader);
            base += reader.numDocs();
            if (smi.next()) queue.put(smi); else smi.close();
        }
        SegmentMergeInfo[] match = new SegmentMergeInfo[readers.size()];
        while (queue.size() > 0) {
            int matchSize = 0;
            match[matchSize++] = (SegmentMergeInfo) queue.pop();
            Term term = match[0].term;
            SegmentMergeInfo top = (SegmentMergeInfo) queue.top();
            while (top != null && term.compareTo(top.term) == 0) {
                match[matchSize++] = (SegmentMergeInfo) queue.pop();
                top = (SegmentMergeInfo) queue.top();
            }
            mergeTermInfo(match, matchSize);
            while (matchSize > 0) {
                SegmentMergeInfo smi = match[--matchSize];
                if (smi.next()) queue.put(smi); else smi.close();
            }
        }
    }

    private final TermInfo termInfo = new TermInfo();

    private final void mergeTermInfo(SegmentMergeInfo[] smis, int n) throws IOException {
        long freqPointer = freqOutput.getFilePointer();
        long proxPointer = proxOutput.getFilePointer();
        int df = appendPostings(smis, n);
        if (df > 0) {
            termInfo.set(df, freqPointer, proxPointer);
            termInfosWriter.add(smis[0].term, termInfo);
        }
    }

    private final SegmentTermPositions postings = new SegmentTermPositions();

    private final int appendPostings(SegmentMergeInfo[] smis, int n) throws IOException {
        int lastDoc = 0;
        int df = 0;
        for (int i = 0; i < n; i++) {
            SegmentMergeInfo smi = smis[i];
            int base = smi.base;
            int[] docMap = smi.docMap;
            smi.termEnum.termInfo(termInfo);
            postings.open(smi.reader, termInfo);
            try {
                while (postings.next()) {
                    int doc;
                    if (docMap == null) doc = base + postings.doc; else doc = base + docMap[postings.doc];
                    if (doc < lastDoc) throw new IllegalStateException("docs out of order");
                    int docCode = (doc - lastDoc) << 1;
                    lastDoc = doc;
                    int freq = postings.freq;
                    if (freq == 1) {
                        freqOutput.writeVInt(docCode | 1);
                    } else {
                        freqOutput.writeVInt(docCode);
                        freqOutput.writeVInt(freq);
                    }
                    int lastPosition = 0;
                    for (int j = 0; j < freq; j++) proxOutput.writeVInt(postings.nextPosition() - lastPosition);
                    df++;
                }
            } finally {
                postings.close();
            }
        }
        return df;
    }

    private final void mergeNorms() throws IOException {
        for (int i = 0; i < fieldInfos.size(); i++) {
            FieldInfo fi = fieldInfos.fieldInfo(i);
            if (fi.isIndexed) {
                OutputStream output = directory.createFile(segment + ".f" + i);
                try {
                    for (int j = 0; j < readers.size(); j++) {
                        SegmentReader reader = (SegmentReader) readers.elementAt(j);
                        BitVector deletedDocs = reader.deletedDocs;
                        InputStream input = reader.normStream(fi.name);
                        int maxDoc = reader.maxDoc();
                        try {
                            for (int k = 0; k < maxDoc; k++) {
                                byte norm = input != null ? input.readByte() : (byte) 0;
                                if (deletedDocs == null || !deletedDocs.get(k)) output.writeByte(norm);
                            }
                        } finally {
                            if (input != null) input.close();
                        }
                    }
                } finally {
                    output.close();
                }
            }
        }
    }
}
