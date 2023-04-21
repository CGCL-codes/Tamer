package uk.ac.shef.wit.aleph.algorithm.svm;

import it.unimi.dsi.fastutil.ints.Int2DoubleMap;
import it.unimi.dsi.fastutil.ints.Int2DoubleArrayMap;
import no.uib.cipr.matrix.Vector;
import no.uib.cipr.matrix.VectorEntry;
import uk.ac.shef.wit.aleph.AlephException;
import uk.ac.shef.wit.aleph.AdaptiveVector;
import uk.ac.shef.wit.aleph.SparseVector;
import uk.ac.shef.wit.aleph.algorithm.svm.loss.Loss;
import uk.ac.shef.wit.aleph.algorithm.svm.loss.LossHinge;
import uk.ac.shef.wit.aleph.algorithm.Learner;
import uk.ac.shef.wit.aleph.algorithm.Classifier;
import uk.ac.shef.wit.aleph.algorithm.MultiLabel;
import uk.ac.shef.wit.aleph.algorithm.Supervised;
import uk.ac.shef.wit.aleph.dataset.Dataset;
import uk.ac.shef.wit.aleph.dataset.Instance;
import uk.ac.shef.wit.aleph.dataset.observer.DatasetObserver;
import uk.ac.shef.wit.aleph.dataset.observer.DatasetObserverTargetCount;
import uk.ac.shef.wit.aleph.dataset.observer.DatasetObserverDatasetSize;
import uk.ac.shef.wit.aleph.dataset.observer.DatasetViewObserver;
import uk.ac.shef.wit.aleph.dataset.view.targets.TargetModifier;
import uk.ac.shef.wit.aleph.dataset.view.instances.InstancesFilterTrainTest;
import uk.ac.shef.wit.commons.DoubleContainer;
import java.util.*;
import java.util.logging.Logger;
import java.io.ObjectOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

/**
 * An implementation the Partial Labels SVM algorithm by Nguyen and Caruana.
 * Please refer to the paper "Classification with Partial Labels", KDD 2008 for more information.
 *
 * @author Jose' Iria, NLP Group, University of Sheffield
 *         (<a  href="mailto:J.Iria@dcs.shef.ac.uk" >email</a>)
 */
public class LearnerPartialLabelsSVM implements Learner, MultiLabel, Supervised {

    private static final Logger log = Logger.getLogger(LearnerPartialLabelsSVM.class.getName());

    private static final double DEFAULT_LAMBDA = 0.01;

    private final double _lambda;

    private final int _iterations;

    private final Loss _loss;

    private final boolean _useBias;

    public LearnerPartialLabelsSVM() {
        this(DEFAULT_LAMBDA);
    }

    public LearnerPartialLabelsSVM(final double lambda) {
        this(lambda, (int) (10.0 / lambda));
    }

    public LearnerPartialLabelsSVM(final double lambda, final int iterations) {
        this(lambda, iterations, new LossHinge());
    }

    public LearnerPartialLabelsSVM(final double lambda, final int iterations, final Loss loss) {
        this(lambda, iterations, loss, true);
    }

    public LearnerPartialLabelsSVM(final double lambda, final int iterations, final Loss loss, final boolean useBias) {
        _lambda = lambda;
        _iterations = iterations;
        _loss = loss;
        _useBias = useBias;
    }

    @Override
    public String toString() {
        return "PL-SVM learner";
    }

    public Classifier learn(final Dataset dataset) throws AlephException {
        final DatasetObserverTargetCount oCount = new DatasetObserverTargetCount();
        final DatasetObserverDatasetSize oSize = new DatasetObserverDatasetSize();
        new DatasetViewObserver().add(oCount).add(oSize).applyNow(dataset);
        final Vector labels = oCount.getCountAsVector();
        final int size = oSize.getDatasetSize();
        final Map<Integer, Vector> w = new HashMap<Integer, Vector>(), avgW = new HashMap<Integer, Vector>();
        for (final VectorEntry e : labels) {
            final int label = e.index();
            w.put(label, new SparseVector());
            avgW.put(label, new SparseVector());
        }
        for (int t = 0; t < _iterations; ++t) {
            log.fine("PL-SVM: running iteration " + t);
            final double eta = 1.0 / (_lambda * (t + 2.0));
            final Map<Integer, Vector> gradients = new HashMap<Integer, Vector>();
            for (final VectorEntry e : labels) gradients.put(e.index(), new SparseVector());
            for (final Instance instance : dataset) {
                Vector features = instance.getFeatures();
                if (_useBias) {
                    features = features.copy();
                    features.set(0, 1.0);
                }
                final Vector targets = instance.getTargets();
                if (targets.get(0) == 0.0) {
                    final Iterator<VectorEntry> it = targets.iterator();
                    final int label = it.next().index();
                    if (it.hasNext()) {
                        int argmaxIn = Integer.MIN_VALUE, argmaxOut = Integer.MIN_VALUE;
                        double maxIn = Double.NEGATIVE_INFINITY, maxOut = Double.NEGATIVE_INFINITY;
                        for (final Map.Entry<Integer, Vector> e : w.entrySet()) {
                            final int l = e.getKey();
                            final double value = e.getValue().dot(features);
                            if (targets.get(l) != 0.0) {
                                if (maxIn < value) {
                                    maxIn = value;
                                    argmaxIn = l;
                                }
                            } else {
                                if (maxOut < value) {
                                    maxOut = value;
                                    argmaxOut = l;
                                }
                            }
                        }
                        if (_loss.loss(maxIn - maxOut) > 0.0) {
                            gradients.get(argmaxIn).add(features);
                            gradients.get(argmaxOut).add(-1.0, features);
                        }
                    } else {
                        int argmax = Integer.MIN_VALUE;
                        double max = Double.NEGATIVE_INFINITY;
                        for (final Map.Entry<Integer, Vector> e : w.entrySet()) {
                            final int l = e.getKey();
                            if (l != label) {
                                final double value = e.getValue().dot(features);
                                if (max < value) {
                                    max = value;
                                    argmax = l;
                                }
                            }
                        }
                        if (_loss.loss(w.get(label).dot(features) - max) > 0.0) {
                            gradients.get(label).add(features);
                            gradients.get(argmax).add(-1.0, features);
                        }
                    }
                }
            }
            for (final VectorEntry e : labels) {
                final int label = e.index();
                final Vector wlabel = w.get(label);
                wlabel.scale(1.0 - eta * _lambda);
                wlabel.add(eta / size, gradients.get(label));
                final double norm2 = wlabel.norm(Vector.Norm.Two);
                if (norm2 > 1.0 / _lambda) wlabel.scale(Math.sqrt(1.0 / (_lambda * norm2)));
                avgW.get(label).add(1.0 / (double) _iterations, wlabel);
            }
        }
        return new Classifier() {

            Map<Integer, Vector> _w = avgW;

            @Override
            public String toString() {
                return "PL-SVM classifier";
            }

            public Dataset classify(final Dataset dataset) throws AlephException {
                return new TargetModifier() {

                    @Override
                    protected double modifyTarget(final Dataset dataset, final Instance instance) throws AlephException {
                        int target = Integer.MIN_VALUE;
                        double max = Double.NEGATIVE_INFINITY;
                        for (final Map.Entry<Integer, Vector> e : _w.entrySet()) {
                            final Vector w = e.getValue();
                            final double confidence = w.get(0) + w.dot(instance.getFeatures());
                            if (max < confidence) {
                                max = confidence;
                                target = e.getKey();
                            }
                        }
                        return target;
                    }
                }.apply(dataset);
            }

            private void writeObject(final ObjectOutputStream s) throws IOException {
                s.writeObject(_w);
            }

            private void readObject(final ObjectInputStream s) throws IOException, ClassNotFoundException {
                _w = (Map<Integer, Vector>) s.readObject();
            }
        };
    }

    public Classifier learn(final Dataset dataset, final Classifier classifier) throws AlephException {
        assert false : "to implement";
        return null;
    }
}
