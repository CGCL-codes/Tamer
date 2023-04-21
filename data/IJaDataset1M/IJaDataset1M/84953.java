package org.jowidgets.examples.swing;

import javax.swing.UIManager;
import org.jowidgets.addons.map.common.widget.IMapWidgetBlueprint;
import org.jowidgets.addons.map.swing.SwingGoogleEarthWidgetFactory;
import org.jowidgets.api.toolkit.Toolkit;
import org.jowidgets.examples.common.map.MapDemoApplication;

public final class SwingMapDemoStarter {

    private SwingMapDemoStarter() {
    }

    public static void main(final String[] args) throws Exception {
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        Toolkit.getWidgetFactory().register(IMapWidgetBlueprint.class, new SwingGoogleEarthWidgetFactory(MapDemoApplication.API_KEY));
        new MapDemoApplication().start();
    }
}
