package xcordion.api.events;

import xcordion.api.TestElement;
import xcordion.api.IgnoreState;

public class BeginEvent<T extends TestElement<T>> extends XcordionEvent<T> {

    public BeginEvent(T element, IgnoreState ignoreState) {
        super(element, ignoreState);
    }
}
