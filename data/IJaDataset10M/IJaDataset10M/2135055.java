package org.cycads.entities.annotation.feature;

import java.util.Collection;
import org.cycads.entities.annotation.AnnotationMethod;
import org.cycads.entities.sequence.Location;
import org.cycads.entities.sequence.Sequence;

public interface Gene<GENE_TYPE extends Gene<?, ?, ?, ?, ?>, L extends Location<?, ?, ?, ?, ?, ?, ?, ?, ?>, SEQ extends Sequence<?, ?, ?, ?, ?, ?>, M extends AnnotationMethod, R extends RNA<?, ?, ?, ?, ?, ?>> extends Feature<GENE_TYPE, L, SEQ, M> {

    public Collection<R> getRNAProducts();
}
