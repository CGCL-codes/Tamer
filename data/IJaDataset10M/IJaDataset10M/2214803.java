package org.demo.treeeditor;

import java.util.List;
import org.boxlayout.gui.BoxLayoutEngine;
import org.boxlayout.gui.VisualElement;
import org.boxlayout.gui.layout.LayoutFactory;
import org.boxlayout.gui.type.ColorEnum;

public class HelpDisplayer {

    BoxLayoutEngine ble;

    VisualElement helpRoot;

    VisualElement container;

    public void init(BoxLayoutEngine ble, VisualElement helpRoot) {
        this.ble = ble;
        this.helpRoot = helpRoot;
        helpRoot.setLayout(LayoutFactory.getInstance().getFloatingLayout());
        container = ble.createElement().setLayout(LayoutFactory.getInstance().getVerticalLayout()).setX(-0.5f).setY(1.4f).setAlpha(0.0f);
        helpRoot.add(container);
    }

    public void show(List<String> list) {
        clear();
        for (String str : list) {
            VisualElement ve = ble.createElement().setPadding(0.1f).setPreferedWidth(0.2f).setPreferedHeight(0.1f).setColor(ColorEnum.GREEN).setScaleFactor(0.5f).setAlpha(0.5f).setRounded(true);
            ve.setText(str);
            container.add(ve);
        }
    }

    public void clear() {
        container.clear();
    }
}
