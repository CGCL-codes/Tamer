package co.fxl.gui.demo;

import co.fxl.gui.api.IContainer;
import co.fxl.gui.api.IImage;
import co.fxl.gui.api.IVerticalPanel;
import co.fxl.gui.api.IClickable.IClickListener;

class ImageDemo extends DemoTemplate implements Decorator {

    private ExampleComposite example;

    @Override
    public void decorate(ExampleDecorator decorator, final IVerticalPanel panel) {
        example = new ExampleComposite(decorator, panel);
        IContainer container = example.title("Image");
        IImage button = container.image();
        button.resource("monalisa.jpg");
        example.codeFragment("panel.add().image().resource(\"monalisa.jpg\").addClickListener(new IClickListener() {" + "\n&nbsp;&nbsp;@Override" + "\n&nbsp;&nbsp;public void onClick() {" + "\n&nbsp;&nbsp;&nbsp;&nbsp;output.add().label().text(\"Image clicked!\");" + "\n&nbsp;&nbsp;}" + "\n});");
        button.addClickListener(new IClickListener() {

            @Override
            public void onClick() {
                example.append("Image clicked!");
            }
        });
    }

    @Override
    public void update(IVerticalPanel panel) {
        example.clearOutput();
    }
}
