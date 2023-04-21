package nl.novay.basiscommarmoede.client;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import nl.novay.basiscommarmoede.shared.DataItem;
import nl.novay.basiscommarmoede.shared.Encoder;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * GUI menu for selecting dataitem values.
 * Uses GWT hyperlinks to select the dataitem to view and edit.
 *
 * @author Martijn Oostdijk (martijn.oostdijk@novay.nl)
 * 
 * @deprecated This is old. Before we used spreadsheet as a data source.
 */
public class MenuPanel extends VerticalPanel {

    private Map<String, Hyperlink> links;

    private DataItem topItem;

    public MenuPanel(String type, List<DataItem> values) {
        updateMenu(type, values);
        History.addValueChangeHandler(new ValueChangeHandler<String>() {

            public void onValueChange(ValueChangeEvent<String> event) {
                String token = event.getValue();
                if (token != null) {
                    setHighlighted(token);
                }
            }
        });
    }

    private void updateMenu(String type, List<DataItem> values) {
        if (type == null || values == null) {
            throw new IllegalArgumentException("Arguments null");
        }
        links = new HashMap<String, Hyperlink>();
        topItem = null;
        addMenuHeader(type);
        for (DataItem value : values) {
            if (topItem == null) {
                topItem = value;
            }
            addMenuItem(value);
        }
    }

    public DataItem getTopItem() {
        return topItem;
    }

    public void setHighlighted(String token) {
        if (token == null) {
            return;
        }
        for (Map.Entry<String, Hyperlink> e : links.entrySet()) {
            Hyperlink link = e.getValue();
            link.removeStyleName("highlighted");
        }
        Hyperlink link = links.get(token);
        if (link != null) {
            link.addStyleName("highlighted");
        }
    }

    private Label addMenuHeader(String name) {
        if (name == null) {
            throw new IllegalArgumentException("Arguments null");
        }
        Label label = new Label(name);
        label.addStyleName("menuHeader");
        add(label);
        return label;
    }

    private void addMenuItem(DataItem dataItem) {
        if (dataItem == null) {
            throw new IllegalArgumentException("Arguments null");
        }
        String name = dataItem.toString();
        String token = Encoder.encode(dataItem);
        Hyperlink link = new Hyperlink(name, token);
        links.put(token, link);
        link.addStyleName("menuItem");
        this.add(link);
    }
}
