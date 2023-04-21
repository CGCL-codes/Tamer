package org.tm4j.topicmap.index.basic;

import org.tm4j.topicmap.Topic;
import org.tm4j.topicmap.index.Index;
import java.util.Collection;

/**
* This interface provides mechanisms dealing with
* <em>association types</em>, the topics defining
* the types of associations.
*
* @author <a href="mailto:kal@techquila.com">Kal Ahmed</a>
* @since  0.7.0
*/
public interface AssociationTypesIndex extends Index {

    /**
     * Returns the associations typed by <code>type</code> and,
     * if <code>includeMergedTopics</code> is true, the associations
     * typed by any topics that are merged with <code>type</code>.
     * @param type The type of the returned associations
     * @param includeMergedTopics if true, then associations typed by
     *                 topics merged with <code>type</code> are also returned.
     * @return An unmodifiable Collection of Associations
     * @since 0.9.5
     */
    public Collection getAssociationsOfType(Topic type, boolean includeMergedTopics);

    /**
     * Returns the associations which are typed by <code>type</code>.
     * @param type The topic which types all of the associations returned.
     * @return An unmodifiable Collection containing those
     *         {@link org.tm4j.topicmap.Association}s for which
     *         <code>type</code> defines the association type.
     */
    public Collection getAssociationsOfType(Topic type);

    /**
     * Returns the topics which define the type of one or more
     * associations in the topic map.
     * @return An unmodifiable Collection containing those {@link Topic}s which are the
     *         type of at least one association in the topic map indexed.
     */
    public Collection getAssociationTypes();
}
