package dash.performance.sequential.consumers;

import java.util.concurrent.atomic.AtomicBoolean;
import dash.Component;
import dash.Obtain;
import dash.examples.component.IComponent;
import dash.examples.component.TestComponent;

/**
 * @author jheintz
 *
 */
@Component
public class MethodObtainedConsumer implements Consumer {

    @Obtain("")
    public IComponent comp;

    public AtomicBoolean comp_atomicboolean = new AtomicBoolean(false);

    @Obtain("test")
    public IComponent getComponent() {
        return new TestComponent();
    }
}
