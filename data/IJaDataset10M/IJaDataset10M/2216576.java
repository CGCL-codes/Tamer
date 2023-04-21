package org.gwtoolbox.widget.client.panel.windowingsystem;

import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.Image;

/**
 * @author Uri Boness
 * @deprecated use {@link org.gwtoolbox.widget.client.panel.layout.drawer.DrawerLayout} instead
 */
@Deprecated
public class WindowSpec {

    private String id;

    private Image icon;

    private String name;

    private String description;

    private Widget content;

    private String preferredSize;

    private WindowingSystem.Position position = WindowingSystem.Position.LEFT;

    public WindowSpec(String id, String name, Widget content, String preferredSize) {
        this(id, null, name, content, preferredSize);
    }

    public WindowSpec(String id, Image icon, String name, Widget content, String preferredSize) {
        this(id, icon, name, null, content, preferredSize);
    }

    public WindowSpec(String id, Image icon, String name, String description, Widget content, String preferredSize) {
        this.id = id;
        this.icon = icon;
        this.name = name;
        this.description = description;
        this.content = content;
        this.preferredSize = preferredSize;
    }

    public String getId() {
        return id;
    }

    public Image getIcon() {
        return icon;
    }

    public void setIcon(Image icon) {
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Widget getContent() {
        return content;
    }

    public void setContent(Widget content) {
        this.content = content;
    }

    public String getPreferredSize() {
        return preferredSize;
    }

    public void setPreferredSize(String preferredSize) {
        this.preferredSize = preferredSize;
    }

    public WindowingSystem.Position getPosition() {
        return position;
    }

    public void setPosition(WindowingSystem.Position position) {
        this.position = position;
    }
}
