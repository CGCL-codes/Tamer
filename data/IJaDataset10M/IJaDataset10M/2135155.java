package ingenias.generator.browser;

import ingenias.exception.NotFound;
import java.util.Iterator;

public interface AttributedElement {

    public GraphAttribute[] getAllAttrs();

    public GraphAttribute getAttributeByName(String name) throws NotFound;
}
