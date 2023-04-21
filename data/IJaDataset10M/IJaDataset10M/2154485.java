package uk.ac.shef.wit.aleph.dataset.view;

import uk.ac.shef.wit.aleph.AlephException;
import uk.ac.shef.wit.aleph.dataset.Dataset;
import uk.ac.shef.wit.aleph.dataset.Instance;
import uk.ac.shef.wit.commons.IteratorNoRemove;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.logging.Logger;
import no.uib.cipr.matrix.Vector;
import no.uib.cipr.matrix.Matrices;
import no.uib.cipr.matrix.VectorEntry;

/**
 * When applied, this view returns a dataset that is a memory-cached version of the original dataset.
 *
 * @author Jose' Iria, NLP Group, University of Sheffield
 *         (<a  href="mailto:J.Iria@dcs.shef.ac.uk" >email</a>)
 */
public class DatasetViewMemoryCached extends DatasetViewAbstract {

    private static final Logger log = Logger.getLogger(DatasetViewMemoryCached.class.getName());

    public DatasetViewMemoryCached() {
        this(null);
    }

    public DatasetViewMemoryCached(final DatasetView nested) {
        super(nested);
    }

    @Override
    protected Dataset applyLocal(final Dataset dataset) throws AlephException {
        final Instance[] cache = cache(dataset);
        final String label = dataset.label();
        return new Dataset() {

            public String label() {
                return label;
            }

            public int size() {
                return cache.length;
            }

            public Iterator<Instance> iterator() {
                return new IteratorNoRemove<Instance>() {

                    private int _current;

                    public boolean hasNext() {
                        return _current < cache.length;
                    }

                    public Instance next() {
                        if (!hasNext()) throw new NoSuchElementException();
                        return cache[_current++];
                    }
                };
            }
        };
    }

    protected Instance[] cache(final Dataset dataset) throws AlephException {
        log.fine("caching dataset: " + dataset.label());
        final Instance[] cache = new Instance[dataset.size()];
        int i = 0;
        for (final Instance instance : dataset) {
            final Vector targets = instance.getTargets();
            final Iterator<VectorEntry> it = targets.iterator();
            final double target = it.next().index();
            cache[i++] = it.hasNext() ? new InstanceCacheMultiLabel(instance.getObjectId(), targets, instance.getFeatures()) : new InstanceCacheSingleLabel(instance.getObjectId(), target, instance.getFeatures());
            log.finest("cached instance number " + i);
        }
        return cache;
    }
}
