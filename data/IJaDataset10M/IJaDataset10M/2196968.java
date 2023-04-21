package edu.luc.cs.trull;

import java.beans.PropertyChangeEvent;
import org.apache.log4j.Logger;

/**
 * A basic component for unconditional event emission.
 * When started, the component schedules an event for emission; 
 * as soon as the event has been scheduled, the component terminates immediately.
 */
public class Emit extends EmitComponent {

    private static final Logger logger = Logger.getLogger(Emit.class);

    /**
   * Constructs an Emit component.  Specific properties can be changed later.
   */
    public Emit() {
    }

    /**
   * Constructs an Emit component that fires an event with the given String
   * label (property name) and no data.
   * @param label the label of the event to be fired.
   */
    public Emit(String label) {
        setLabel(label);
    }

    /**
   * Constructs an Emit component that fires an event with the given String
   * label and the given constant data.
   * @param label the label of the event to be emitted.
   * @param value the data value (new property value) of the event to be emitted.
   */
    public Emit(String label, final Object value) {
        this(label, new EventFunction() {

            public Object apply(PropertyChangeEvent incoming) {
                return value;
            }
        });
    }

    /**
   * Constructs an Emit component that fires an event with the given String
   * label and whose data value (new property value) 
   * is computed dynamically using the given generator.
   * @param label the label of the event to be emitted.
   * @param generator the generator for the data value of the event to be emitted.
   */
    public Emit(String label, EventFunction generator) {
        setLabel(label);
        setValueGenerator(generator);
    }

    /**
   * The label (property name) of the event to be emitted.
   */
    private String label;

    /**
   * The function on events used to generate the data value of the event to be emitted.
   */
    private EventFunction generator;

    /**
	 * Sets the label (property name) of the event to be emitted. 
	 */
    public synchronized void setLabel(String label) {
        this.label = label;
    }

    /**
   * Returns the label (property name) of the event to be emitted. 
   */
    public synchronized String getLabel() {
        return label;
    }

    /**
	 * Sets the generator for the data value (new property value) of the event to be emitted. 
	 */
    public synchronized void setValueGenerator(EventFunction generator) {
        this.generator = generator;
    }

    /**
   * Returns the generator for the data value (new property value) of the event to be emitted. 
   */
    public synchronized EventFunction getValueGenerator() {
        return generator;
    }

    public void start(final PropertyChangeEvent incoming) {
        super.start(incoming);
        Object value = generator == null ? null : generator.apply(incoming);
        scheduleEvent(label, value, true);
    }
}
