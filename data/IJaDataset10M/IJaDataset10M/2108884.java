package automenta.netention.client.ui.detail.property;

import java.util.Arrays;
import automenta.netention.client.NetworkService;
import automenta.netention.client.NetworkServiceAsync;
import automenta.netention.client.data.DetailData;
import automenta.netention.client.property.Property;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;

public abstract class PropertyPanel extends HorizontalPanel {

    private NetworkServiceAsync netService = GWT.create(NetworkService.class);

    private String property;

    private Label propLabel;

    private FlowPanel nameHolder;

    private Property propertyData;

    private DetailData node;

    public static class PropertyTextBox extends TextBox {

        public PropertyTextBox() {
            super();
            addStyleName("PropertyTextBox");
        }
    }

    public PropertyPanel(String property) {
        super();
        this.property = property;
        propLabel = new Label();
        addStyleName("PropertyPanel");
        nameHolder = new FlowPanel();
        add(nameHolder);
        netService.getPropertyData(new String[] { property }, new AsyncCallback<Property[]>() {

            @Override
            public void onFailure(Throwable caught) {
                add(new Label(caught.toString() + Arrays.asList(caught.getStackTrace())));
            }

            @Override
            public void onSuccess(Property[] result) {
                propertyData = result[0];
                initPropertyPanel();
            }
        });
    }

    /** called after getPropertyData() becomes available */
    protected void initPropertyPanel() {
        String name = getPropertyData().getName();
        propLabel.setText(name);
        propLabel.addStyleName("PropertyLabel");
        nameHolder.add(propLabel);
    }

    public Property getPropertyData() {
        return propertyData;
    }

    protected String getProperty() {
        return property;
    }

    protected void setIs() {
        removeStyleName("PropertyWillBe");
        addStyleName("PropertyIs");
    }

    protected void setWillBe() {
        removeStyleName("PropertyIs");
        addStyleName("PropertyWillBe");
    }

    public Label getPropertyLabel() {
        return propLabel;
    }

    public abstract void widgetToValue();

    public void setNode(DetailData node) {
        this.node = node;
    }

    public DetailData getNode() {
        return node;
    }
}
