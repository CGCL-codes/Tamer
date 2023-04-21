package edu.udo.cs.ai.nemoz.plugins.swinggui.browser.ntunes;

import java.util.Set;
import de.oliverflasch.commons.observing.Message;
import de.oliverflasch.commons.observing.Notifier;
import edu.udo.cs.ai.nemoz.model.entities.Item;

/**
 * TODO Enter a type comment here!
 *
 * @author oflasch
 */
public interface ItemSource extends Notifier {

    static class ItemsChangedMessage extends Message {

        public ItemsChangedMessage(final Notifier sender) {
            super(sender);
        }
    }

    Set<Item> getItems();

    boolean isFiltering();
}
