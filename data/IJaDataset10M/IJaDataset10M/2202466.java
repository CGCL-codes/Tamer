package visad;

/**
   DisplayReferenceEvent is the VisAD class for Events from Display
   DataReference obkects.  They are sourced by Display objects and
   received by DisplayListener objects.<P>
*/
public class DisplayReferenceEvent extends DisplayEvent {

    private DataDisplayLink link;

    /**
   * Constructs a DisplayReferenceEvent object with the specified
   * source display, type of event, and DataReference connection.
   *
   * @param  d  display that sends the event
   * @param  id  type of DisplayReferenceEvent that is sent
   * @param link DataReference link
   */
    public DisplayReferenceEvent(Display d, int id, DataDisplayLink link) {
        super(d, id);
        this.link = link;
    }

    /**
   * Return a new DisplayReferenceEvent which is a copy of this event,
   * but which uses the specified source display
   *
   * @param dpy Display to use for the new DisplayReferenceEvent
   */
    public DisplayEvent cloneButDisplay(Display dpy) {
        return new DisplayReferenceEvent(dpy, getId(), link);
    }

    /**
   * @return the DataDisplayLink referenced by this
   */
    public DataDisplayLink getDataDisplayLink() {
        return link;
    }
}
