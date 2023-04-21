package uapp.configuration;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import uapp.Page;

public class PageConfig {

    private String id;

    private String location;

    private String name;

    private String description;

    private boolean visible = true;

    private String layout;

    private List<WidgetConfig> widgets;

    @XmlAttribute(name = "id", required = true)
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @XmlAttribute(name = "location", required = false)
    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    @XmlAttribute(name = "name", required = true)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @XmlAttribute(name = "description", required = false)
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    @XmlAttribute(name = "visible", required = false)
    public boolean isVisible() {
        return visible;
    }

    public void setLayout(String layout) {
        this.layout = layout;
    }

    @XmlAttribute(name = "layout", required = false)
    public String getLayout() {
        if (layout == null || layout == "") return "layout";
        return layout;
    }

    @XmlElement(name = "widget", required = true)
    public List<WidgetConfig> getWidgets() {
        if (widgets == null) widgets = new ArrayList<WidgetConfig>();
        return widgets;
    }

    public void setWidgets(List<WidgetConfig> widgets) {
        this.widgets = widgets;
    }

    public Page getInstance() {
        Page page = new Page();
        page.setId(getId());
        page.setLocation(getLocation());
        page.setName(getName());
        page.setDescription(getDescription());
        page.setVisible(isVisible());
        page.setLayout(getLayout());
        for (WidgetConfig config : getWidgets()) {
            page.getWidgets().add(config.getInstance());
        }
        return page;
    }
}
