package functional.assets;

import wheel.components.StandaloneComponent;

public class ReusableComponent extends StandaloneComponent {

    public ReusableComponent(String componentId) {
        super(componentId);
        config().setReusable(true).useAsset("Custom.css");
    }

    public void buildComponent() {
        h2("Reusable component");
        img("http://www.foobar.com/test.jpg", "alt text");
    }
}
