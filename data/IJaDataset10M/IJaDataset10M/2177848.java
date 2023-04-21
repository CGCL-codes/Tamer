package org.proteinarchitect.format;

import java.util.List;
import org.proteinarchitect.util.property.impl.PropertyInfo;

/**
 * @author mhaimel
 *
 */
public interface SequenceCollectionI<T extends ProteinSequenceI> {

    void addSequence(T sequ);

    List<T> getSequences();

    void setSequences(List<T> sequences);

    PropertyInfo<String> getTree();
}
