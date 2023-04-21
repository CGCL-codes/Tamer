package org.modss.facilitator.util.description;

import org.modss.facilitator.util.event.*;
import org.swzoo.nursery.event.EventGenerator;
import java.util.Arrays;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;

/**
 * A standard implementation of MutableDescribable. Handles listeners for changes to
 * fields.
 *
 * @author John Farrell
 * @author mag@netstorm.net.au
 */
class DefaultDescription implements MutableDescribable {

    /** The short description. */
    private String shortDesc = "";

    /** The long description. */
    private String longDesc = "";

    /** The comment. */
    private String comment = "";

    /** Event delegate. */
    private EventGenerator evG = EventGenerator.Factory.create(ChangeListener.class, ChangeEvent.class);

    /** Add an object to be notified of ChangeEvents. */
    public void addChangeListener(ChangeListener listener) {
        evG.addListener(listener);
    }

    /** Remove an object to be no longer notified of ChangeEvents. */
    public void removeChangeListener(ChangeListener listener) {
        evG.removeListener(listener);
    }

    /** @return the short description of this object. */
    public String getShortDescription() {
        return (shortDesc == null || shortDesc.equals(BLANK_STRING)) ? longDesc : shortDesc;
    }

    /** @return the long description of this object. */
    public String getLongDescription() {
        return longDesc;
    }

    /** @return the comment associated with this object. */
    public String getComment() {
        return comment;
    }

    /** set the short description of this object. */
    public void setShortDescription(String shortDesc) {
        if (shortDesc == null) shortDesc = BLANK_STRING;
        if (!shortDesc.equals(this.shortDesc)) {
            this.shortDesc = shortDesc;
            if (this.longDesc.equals(BLANK_STRING)) {
                this.longDesc = shortDesc;
                evG.fireEvent(new DetailedChangeEvent(this, Arrays.asList(new String[] { SHORT_DESC, LONG_DESC })));
            } else {
                evG.fireEvent(new DetailedChangeEvent(this, SHORT_DESC));
            }
        }
    }

    /** set the long description of this object. */
    public void setLongDescription(String longDesc) {
        if (longDesc == null) longDesc = BLANK_STRING;
        if (!longDesc.equals(this.longDesc)) {
            this.longDesc = longDesc;
            evG.fireEvent(new DetailedChangeEvent(this, LONG_DESC));
        }
    }

    /** set the comment associated with this object. */
    public void setComment(String comment) {
        if (comment == null) comment = BLANK_STRING;
        if (!comment.equals(this.comment)) {
            this.comment = comment;
            evG.fireEvent(new DetailedChangeEvent(this, COMMENT));
        }
    }

    /** set the description of this object. */
    public void setDescription(Describable desc) {
        setLongDescription(desc.getLongDescription());
        setShortDescription(desc.getShortDescription());
        setComment(desc.getComment());
    }

    /**
     * Provide a string representation of this object.
     *
     * @return a string represenation.
     */
    public String toString() {
        return "DefaultDescription[shortDesc=" + shortDesc + ",longDesc=" + longDesc + ",comment=" + comment + "]";
    }

    /** Blank string. */
    private static final String BLANK_STRING = "";
}
