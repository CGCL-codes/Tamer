package functional.assets;

import wheel.components.StandaloneComponent;

public class ReusableComponent2 extends StandaloneComponent {

    public ReusableComponent2(String componentId) {
        super(componentId);
        config().setReusable(true).useAsset("ReusableComponent2.css").useAsset("TestCss.css").useAsset("http://www.foobar.com/extCss.css").useAsset("http://www.foobar.com/extJs.js");
    }

    public void buildComponent() {
    }
}
