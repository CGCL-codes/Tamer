package ee.webAppToolkit.core.expert;

import javax.inject.Provider;
import ee.webAppToolkit.core.Result;
import ee.webAppToolkit.core.WrappingController;

public interface ControllerHandler extends Handler {

    public Result handle(WrappingController controller) throws Throwable;

    public Provider<? extends WrappingController> getControllerProvider();
}
