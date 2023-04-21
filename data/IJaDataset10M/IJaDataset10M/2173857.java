package de.jmda.mview.fx.node.typeshape.relation;

import javafx.beans.property.ObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Point2D;
import de.jmda.fx.node.behaviour.Draggable;
import de.jmda.fx.node.behaviour.Highlightable;

/**
 * Simple relation segment that connects a start and end point property with a
 * straight line. Whenever these property values change this line's {@link
 * #startPointProperty()} or respective {@link #endPointProperty()} values
 * change accordingly.
 *
 * @author roger.jmda@gmail.com
 */
public class RelationSegmentStraight extends RelationSegment implements Highlightable, Draggable {

    private enum Endpoint {

        START, END
    }

    public RelationSegmentStraight(ObjectProperty<Point2D> start, ObjectProperty<Point2D> end) {
        super(start, end);
        start.addListener(new EndpointUpdater<Point2D>(Endpoint.START));
        end.addListener(new EndpointUpdater<Point2D>(Endpoint.END));
    }

    /**
	 * Makes sure that {@link RelationSegmentStraight#startXProperty()} or {@link
	 * RelationSegmentStraight#endXProperty()} values change if the according
	 * {@link RelationSegmentStraight#RelationSegmentStraight(ObjectProperty, ObjectProperty)}
	 * property parameter values change.
	 *
	 * @param <T>
	 */
    private class EndpointUpdater<T extends Point2D> implements ChangeListener<Point2D> {

        private Endpoint endpoint;

        public EndpointUpdater(Endpoint endpoint) {
            this.endpoint = endpoint;
        }

        @Override
        public void changed(ObservableValue<? extends Point2D> observable, Point2D oldValue, Point2D newValue) {
            if (endpoint == Endpoint.START) {
                setStartX(newValue.getX());
                setStartY(newValue.getY());
            } else {
                setEndX(newValue.getX());
                setEndY(newValue.getY());
            }
        }
    }
}
