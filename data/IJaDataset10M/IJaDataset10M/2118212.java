package org.iverein.gxt.rte.client.widget.toolbar;

import org.iverein.gxt.rte.client.widget.ImageBundleButton;
import org.iverein.gxt.rte.client.widget.toolbar.EditorCommand;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.extjs.gxt.ui.client.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.BaseEvent;

/**
 * 
 *
 * @author: Peter Quiel <peter.quiel@iverein-networks.de>
 * Date: 31.10.2008
 * Time: 17:25:30
 */
public class ImageBundleCommandButton extends ImageBundleButton {

    public ImageBundleCommandButton(AbstractImagePrototype imagePrototype, final EditorCommand command) {
        super(imagePrototype);
        this.addListener(Events.Select, new Listener() {

            public void handleEvent(BaseEvent event) {
                command.exec(null);
            }
        });
    }
}
