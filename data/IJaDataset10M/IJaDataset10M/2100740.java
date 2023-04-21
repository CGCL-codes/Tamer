package gnu.ojama.test.demo.modelbuilder;

import gnu.ojama.event.*;

/**
 * Event object for Attributedescriptor related events.
 * @author Markku Vuorenmaa
 */
public class DescriptorEvent extends OjamaEvent {

    private String myAttributeName;

    private Object myOldValue;

    private Object myNewValue;

    /**
     * Constructor with event source.
     * @param source event source
     */
    public DescriptorEvent(Object source, String attributeName, Object oldValue, Object newValue) {
        super(source);
        myAttributeName = attributeName;
        myOldValue = oldValue;
        myNewValue = newValue;
    }

    public String getAttributeName() {
        return myAttributeName;
    }

    public Object getOldValue() {
        return myOldValue;
    }

    public Object getNewValue() {
        return myNewValue;
    }
}
