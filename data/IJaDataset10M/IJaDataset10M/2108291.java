package org.davic.mpeg.sections;

/**
 * This event signals that enough section filter resources for the corresponding
 * section filter group were available at the time this event was generated. For
 * example, if a section filter group was created with 4 filters, then this
 * event indicates that at least 4 filters were available at the time this event
 * was generated. Note that these filters may no longer be available at the time
 * the application tries to attach the section filter group again. This event is
 * a hint to the application that it is useful to try to attach the section
 * filter group again. This event is only generated after a
 * ForcedDisconnectedEvent has been generated and before the application has
 * successfully attached the section filter group again.
 */
public class FilterResourcesAvailableEvent extends org.davic.resources.ResourceStatusEvent {

    /**
	 * This constructs a FilterResourcesAvailableEvent for the specified
	 * SectionFilterGroup object.
	 * @param f the SectionFilterGroup
	 */
    public FilterResourcesAvailableEvent(SectionFilterGroup f) {
        super(f);
    }

    /**
	 * This returns the SectionFilterGroup object for which enough filter
	 * resources were available at the time this event was generated.
	 */
    public Object getSource() {
        return null;
    }
}
