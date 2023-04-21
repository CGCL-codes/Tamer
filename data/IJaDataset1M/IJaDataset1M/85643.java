package org.kaleidofoundry.core.context.service_with_provider;

import org.kaleidofoundry.core.context.RuntimeContext;
import org.kaleidofoundry.core.plugin.Declare;

/**
 * @author Jerome RADUGET
 */
@Declare(value = "myProvidedService.with.context")
public class MyServiceWithRuntimeContext implements MyServiceInterface {

    private RuntimeContext<MyServiceInterface> context;

    public RuntimeContext<MyServiceInterface> getContext() {
        return context;
    }
}
