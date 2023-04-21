package pl.xperios.rdk.client.common;

import com.google.gwt.inject.client.AbstractGinModule;
import java.util.ArrayList;
import pl.xperios.rdk.client.common.moduleRenderers.AbstractComponentRenderer;

/**
 *
 * @author Praca
 */
public abstract class AbstractRdkGinModule extends AbstractGinModule {

    public ArrayList<AbstractComponentRenderer<?>> getRenderers() {
        ArrayList<AbstractComponentRenderer<?>> out = new ArrayList<AbstractComponentRenderer<?>>();
        return out;
    }
}
