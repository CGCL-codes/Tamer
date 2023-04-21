package platform.client;

import java.util.HashSet;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.VerticalPanel;

public class PluginPresenter extends Composite {

    private HashSet<String> visiblePlugins = new HashSet<String>();

    public PluginPresenter() {
        final VerticalPanel panel = new VerticalPanel();
        new Timer() {

            @Override
            public void run() {
                for (final AbstractPlugin plugin : PluginRegistry.getPlugins()) {
                    if (!visiblePlugins.contains(plugin.getName())) {
                        visiblePlugins.add(plugin.getName());
                        panel.add(plugin.getWidget());
                    }
                }
            }
        }.scheduleRepeating(1000);
        initWidget(panel);
    }
}
