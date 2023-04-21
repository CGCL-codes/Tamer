package org.databene.jdbacl.model;

import java.util.List;

/**
 * Common interface for {@link DBObject}s which can hold {@link DBSequence}s.<br/><br/>
 * Created: 05.06.2011 16:58:33
 * @since 0.6.8
 * @author Volker Bergmann
 */
public interface SequenceHolder {

    List<DBSequence> getSequences(boolean recursive);
}
