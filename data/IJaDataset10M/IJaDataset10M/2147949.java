package org.makagiga.desktop.todo;

import org.makagiga.desktop.Widget;
import org.makagiga.desktop.WidgetPlugin;

/**
 * @since 3.8.2
 */
public final class TodoWidgetPlugin extends WidgetPlugin {

    @Override
    public Widget create() {
        return new TodoWidget(getInfo());
    }
}
