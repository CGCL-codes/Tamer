package automenta.netention.gwtdepr.ui.pattern;

import automenta.netention.gwtdepr.NetworkService;
import automenta.netention.gwtdepr.NetworkServiceAsync;
import automenta.netention.gwtdepr.data.PatternData;
import automenta.netention.server.value.Property;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class PatternPanel extends DockPanel {

    private final NetworkServiceAsync netService = GWT.create(NetworkService.class);

    private VerticalPanel propertiesPanel;

    public PatternPanel(PatternData p) {
        super();
        propertiesPanel = new VerticalPanel();
        propertiesPanel.setHeight("100%");
        add(propertiesPanel, CENTER);
        setPattern(p);
    }

    public void setPattern(PatternData p) {
        propertiesPanel.clear();
        netService.getPropertyData(p.getProperties(), new AsyncCallback<Property[]>() {

            @Override
            public void onFailure(Throwable caught) {
                Window.alert(caught.toString());
            }

            @Override
            public void onSuccess(Property[] result) {
                for (Property pd : result) {
                    propertiesPanel.add(new PatternPropertyPanel(pd, false));
                }
            }
        });
    }
}
