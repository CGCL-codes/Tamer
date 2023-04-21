package examples.page;

import org.t2framework.t2.annotation.core.Default;
import org.t2framework.t2.annotation.core.Page;
import org.t2framework.t2.contexts.Request;
import org.t2framework.t2.navigation.Forward;
import org.t2framework.t2.spi.Navigation;

@Page("/forward")
public class ForwardSample {

    @Default
    public Navigation index(Request request) {
        request.setAttribute("message", "forward from " + this.getClass().getCanonicalName());
        return Forward.to(ForwardSample2.class);
    }
}
