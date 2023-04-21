package dash.obtain.performance;

import dash.obtain.exampleComponent.TestComponent;
import dash.obtain.provider.ObtainLookup;
import dash.obtain.provider.Provider;

public class TestProvider implements Provider {

    public Object lookup(ObtainLookup lookup) {
        return new TestComponent();
    }
}
