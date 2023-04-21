package com.googlecode.gaal.analysis.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import com.googlecode.gaal.analysis.api.VectorBuilder;
import com.googlecode.gaal.analysis.impl.Aligner.AlignmentQueue;
import com.googlecode.gaal.data.api.Corpus;
import com.googlecode.gaal.data.api.IntSequence;
import com.googlecode.gaal.data.api.Vector;
import com.googlecode.gaal.data.impl.BoundedPriorityQueue;
import com.googlecode.gaal.data.impl.SparseVector;
import com.googlecode.gaal.suffix.api.IntervalTree.Interval;

public class Aligner<C> implements Iterable<AlignmentQueue<C>> {

    public static class AlignmentQueue<T> extends BoundedPriorityQueue<AlignmentQueue<T>.Alignment<T>> {

        private final T source;

        public AlignmentQueue(T source, int alignmentsNumber) {
            super(alignmentsNumber);
            this.source = source;
        }

        public T getSource() {
            return source;
        }

        public class Alignment implements Comparable<Alignment> {

            private final T target;

            private final double similarity;

            public Alignment(T target, double similarity) {
                this.target = target;
                this.similarity = similarity;
            }

            public T getSource() {
                return source;
            }

            public T getTarget() {
                return target;
            }

            public double getSimilarity() {
                return similarity;
            }

            @Override
            public int compareTo(Alignment o) {
                return (similarity > o.similarity ? 1 : (similarity == o.similarity ? 0 : -1));
            }
        }
    }

    protected static boolean verbose;

    private final double minSimilarity;

    private final int alignmentsNumber;

    protected final List<Vector> srcVectors;

    protected final List<Vector> dstVectors;

    protected final List<C> srcObjects;

    protected final List<C> dstObjects;

    protected Aligner(double minSimilarity, int alignmentsNumber, final int minVectorSize) {
        this.minSimilarity = minSimilarity;
        this.alignmentsNumber = alignmentsNumber;
        this.srcVectors = new ArrayList<Vector>();
        this.dstVectors = new ArrayList<Vector>();
        this.srcObjects = new ArrayList<C>();
        this.dstObjects = new ArrayList<C>();
    }

    public <T> Aligner(final VectorBuilder<C> vectorBuilder, final Iterator<C> srcIterator, final Iterator<C> dstIterator, final Corpus<T> srcCorpus, final Corpus<T> dstCorpus, double minSimilarity, int alignmentsNumber, final int minVectorSize) {
        this(minSimilarity, alignmentsNumber, minVectorSize);
        Stopwatch stopwatch = null;
        if (verbose) {
            stopwatch = new Stopwatch();
            stopwatch.start();
        }
        vectorBuilder.buildVectors(srcIterator, srcVectors, srcObjects, srcCorpus, minVectorSize);
        vectorBuilder.buildVectors(dstIterator, dstVectors, dstObjects, dstCorpus, minVectorSize);
        if (stopwatch != null) {
            stopwatch.stop();
            System.out.printf("vector construction took %s\n", stopwatch);
        }
    }

    @Override
    public Iterator<AlignmentQueue<C>> iterator() {
        return new Iterator<AlignmentQueue<C>>() {

            int i = 0;

            AlignmentQueue<C> next = advance();

            @Override
            public boolean hasNext() {
                return next != null;
            }

            @Override
            public AlignmentQueue<C> next() {
                AlignmentQueue<C> alignment = next;
                next = advance();
                return alignment;
            }

            private AlignmentQueue<C> advance() {
                AlignmentQueue<C> queue = null;
                while (queue == null || queue.isEmpty()) {
                    if (i == srcVectors.size()) return null;
                    queue = new AlignmentQueue<C>(srcObjects.get(i), alignmentsNumber);
                    for (int j = 0; j < dstVectors.size(); j++) {
                        double sim = srcVectors.get(i).similarity(dstVectors.get(j));
                        if (sim > minSimilarity) {
                            queue.add(queue.new Alignment(dstObjects.get(j), sim));
                        }
                    }
                    i++;
                }
                return queue;
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }

    public static Vector toVector(Interval interval, Corpus<String> corpus) {
        Vector vector = new SparseVector();
        IntSequence indices = interval.indices();
        for (int i = 0; i < indices.size(); i++) {
            int start = indices.get(i);
            int documentId = corpus.getDocumentId(start);
            vector.add(documentId, 1);
        }
        return vector;
    }

    public static void setVerbose(boolean verbose) {
        Aligner.verbose = verbose;
    }

    public static boolean isVerbose() {
        return verbose;
    }
}
