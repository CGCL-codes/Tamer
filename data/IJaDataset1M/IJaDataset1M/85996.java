package nl.headspring.photoz.imagecollection.fs.events;

import nl.headspring.photoz.common.eventbus.BusEvent;
import nl.headspring.photoz.common.eventbus.EventClass;
import nl.headspring.photoz.imagecollection.Annotation;

/**
 * Class AnnotationUpdatedBusEvent.
 *
 * @author Eelco Sommer
 * @since Oct 4, 2010
 */
public class AnnotationUpdatedBusEvent implements BusEvent {

    public static final EventClass EVENT_CLASS = new EventClass("annotation.updated", AnnotationUpdatedBusEvent.class);

    private final Annotation annotation;

    public AnnotationUpdatedBusEvent(Annotation annotation) {
        this.annotation = annotation;
    }

    public EventClass getEventClass() {
        return EVENT_CLASS;
    }

    public Annotation getAnnotation() {
        return annotation;
    }
}
