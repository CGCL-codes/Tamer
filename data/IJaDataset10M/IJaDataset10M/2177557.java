package org.semtinel.core.data.api;

import java.awt.datatransfer.DataFlavor;
import java.util.List;

/**
 *
 * @author kai
 */
public interface AnnotationSet {

    public static final DataFlavor DATA_FLAVOR = new DataFlavor(AnnotationSet.class, "AnnotationSet");

    List<AnnotationSetGroup> getAnnotationSetGroups();

    ConceptScheme getConceptScheme();

    Long getId();

    String getName();

    void setConceptScheme(ConceptScheme conceptScheme);

    void setId(Long id);

    void setName(String name);

    List<Annotation> getAnnotations();

    void setAnnotations(List<Annotation> annotations);

    int getSize();

    /**
     * Removes the record set from the specified group.
     *
     * WARNING: You can not use this method inside an iterated loop over
     * the recordsets of a group or the groups of a recordset, as both
     * collections are modified by this method. Will result in a ConcurrentModificationException.
     *
     * @param rsg
     */
    void removeFromGroup(AnnotationSetGroup rsg);
}
