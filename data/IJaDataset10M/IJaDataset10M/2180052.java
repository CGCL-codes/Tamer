package org.wings.event;

import java.util.EventListener;

/**
 * @author hengels
 * @version $Revision: 1334 $
 */
public interface SDocumentListener extends EventListener {

    /**
     * Gives notification that there was an insert into the document.  The
     * range given by the DocumentEvent bounds the freshly inserted region.
     *
     * @param e the document event
     */
    public void insertUpdate(SDocumentEvent e);

    /**
     * Gives notification that a portion of the document has been
     * removed.  The range is given in terms of what the view last
     * saw (that is, before updating sticky positions).
     *
     * @param e the document event
     */
    public void removeUpdate(SDocumentEvent e);

    /**
     * Gives notification that an attribute or set of attributes changed.
     *
     * @param e the document event
     */
    public void changedUpdate(SDocumentEvent e);
}
