package co.fxl.gui.demo;

import co.fxl.gui.api.IContainer;
import co.fxl.gui.api.IHorizontalPanel;
import co.fxl.gui.api.IVerticalPanel;

class HorizontalPanelDemo extends DemoTemplate implements Decorator {

    @Override
    public void decorate(ExampleDecorator decorator, final IVerticalPanel vpanel) {
        ExampleComposite example = new ExampleComposite(decorator, vpanel, true);
        IContainer container = example.title("Horizontal Panel");
        IHorizontalPanel panel = container.panel().horizontal();
        panel.spacing(10).color().lightgray();
        panel.add().label().text("Label");
        panel.add().checkBox().text("Checkbox");
        panel.add().comboBox().addText("Combobox 1").addText("Combobox 2");
        panel.add().radioButton().text("Radio Button");
        StringBuffer b = new StringBuffer();
        b.append("IHorizontalPanel horizontalPanel = panel.add().panel().horizontal();");
        b.append("\nhorizontalPanel.spacing(10).color().lightgray();");
        b.append("\nhorizontalPanel.add().label().text(\"Label\");");
        b.append("\nhorizontalPanel.add().checkBox().text(\"Checkbox\");");
        b.append("\nhorizontalPanel.add().comboBox().addText(\"Combobox 1\", \"Combobox 2\");");
        b.append("\nhorizontalPanel.add().radioButton().text(\"Radio Button\");");
        example.codeFragment(b.toString());
    }
}
