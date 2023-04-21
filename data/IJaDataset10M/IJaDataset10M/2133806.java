package net.seagis.coverage.model;

import java.util.List;
import net.seagis.catalog.CatalogException;
import net.seagis.catalog.Element;
import net.seagis.coverage.catalog.Layer;
import org.opengis.coverage.Coverage;

/**
 * Base interface for numerical models, which may or may not be {@linkplain LinearModel linear}.
 *
 * @version $Id: Model.java 162 2007-11-06 15:05:54Z desruisseaux $
 * @author Martin Desruisseaux
 */
public interface Model extends Element {

    /**
     * Returns the layer which represents the results of this numerical model.
     */
    Layer getTarget();

    /**
     * Returns all descriptors used as input for this numerical model. The descriptor at
     * index <var>i</var> in this list determines the value at the same index <var>i</var>
     * in the {@code values} array given to the {@link #normalize} and {@link #evaluate}
     * methods, as in the following pseudo-code:
     *
     * <blockquote><pre>
     * values[i] = descriptors.get(i).getCoverage().evaluate(position);
     * </pre></blockquote>
     */
    List<Descriptor> getDescriptors();

    /**
     * {@linkplain Distribution#normalize Normalize} all values in the specified array. It is user
     * responsability to invoke this method exactly once before invoking {@link #evaluate evaluate}.
     * The normalization is performed in-place, i.e. the normalized values replace the specified
     * values.
     */
    void normalize(double[] values);

    /**
     * Computes a {@linkplain #getTarget target} value from the given {@linkplain #getDescriptors
     * descriptor} values.
     */
    double evaluate(double[] values);

    /**
     * Returns a view of this numerical model as a coverage.
     *
     * @throws CatalogException if the coverage can not be created.
     */
    Coverage asCoverage() throws CatalogException;
}
