package co.fxl.gui.demo;

import co.fxl.gui.api.IButton;
import co.fxl.gui.api.ICardPanel;
import co.fxl.gui.api.IContainer;
import co.fxl.gui.api.IHorizontalPanel;
import co.fxl.gui.api.IVerticalPanel;
import co.fxl.gui.api.IClickable.IClickListener;

public class CardPanelDemo extends DemoTemplate implements Decorator {

    @Override
    public void decorate(ExampleDecorator decorator, final IVerticalPanel vpanel) {
        vpanel.stretch(true);
        ExampleComposite example = new ExampleComposite(decorator, vpanel, true);
        IContainer container = example.title("Card Panel");
        IVerticalPanel verticalPanel = container.panel().vertical();
        verticalPanel.stretch(true);
        IHorizontalPanel buttonPanel = verticalPanel.add().panel().horizontal();
        ICardPanel cardPanel = verticalPanel.add().panel().card();
        IHorizontalPanel card1 = addCard(buttonPanel, cardPanel, "1");
        addCard(buttonPanel, cardPanel, "2");
        cardPanel.show(card1);
        StringBuffer b = new StringBuffer();
        b.append("IVerticalPanel verticalPanel = panel.add().panel().vertical();");
        b.append("\nIHorizontalPanel buttonPanel = verticalPanel.add().panel().horizontal();");
        b.append("\nICardPanel cardPanel = verticalPanel.add().panel().card();");
        b.append("\naddCard(buttonPanel, cardPanel, \"1\");");
        b.append("\naddCard(buttonPanel, cardPanel, \"2\");");
        b.append("\n");
        b.append("\nvoid addCard(IHorizontalPanel buttonPanel, final ICardPanel cardPanel, String name) {");
        b.append("\n&nbsp;&nbsp;IButton button = buttonPanel.add().button().text(\"Show Card \" + name);");
        b.append("\n&nbsp;&nbsp;final IHorizontalPanel contentPanel = cardPanel.add().panel().horizontal().spacing(10);");
        b.append("\n&nbsp;&nbsp;contentPanel.color().lightgray();");
        b.append("\n&nbsp;&nbsp;contentPanel.add().label().text(\"Content Card \" + name);");
        b.append("\n&nbsp;&nbsp;button.addClickListener(new IClickListener() {");
        b.append("\n&nbsp;&nbsp;&nbsp;&nbsp;@Override");
        b.append("\n&nbsp;&nbsp;&nbsp;&nbsp;public void onClick() {");
        b.append("\n&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;cardPanel.show(contentPanel);");
        b.append("\n&nbsp;&nbsp;&nbsp;&nbsp;}");
        b.append("\n&nbsp;&nbsp;});");
        b.append("\n}");
        example.codeFragment(b.toString());
    }

    IHorizontalPanel addCard(IHorizontalPanel buttonPanel, final ICardPanel cardPanel, String name) {
        IButton button = buttonPanel.add().button().text("Show Card " + name);
        final IHorizontalPanel contentPanel = cardPanel.add().panel().horizontal().spacing(10);
        contentPanel.color().lightgray();
        contentPanel.add().label().text("Content Card " + name);
        button.addClickListener(new IClickListener() {

            @Override
            public void onClick() {
                cardPanel.show(contentPanel);
            }
        });
        return contentPanel;
    }
}
