package org.gamegineer.engine.internal.core.extensions.commandeventmediator;

import java.lang.reflect.Field;
import java.util.List;
import org.gamegineer.engine.core.extensions.commandeventmediator.ICommandListener;
import org.gamegineer.engine.core.util.attribute.Attribute;

/**
 * A class for transparently accessing inaccessible members of the
 * {@code CommandEventMediatorExtension} class for testing purposes.
 */
final class CommandEventMediatorExtensionFacade {

    /**
     * Initializes a new instance of the
     * {@code CommandEventMediatorExtensionFacade} class.
     */
    private CommandEventMediatorExtensionFacade() {
        super();
    }

    static String ATTR_ACTIVE_COMMAND_LISTENERS() {
        try {
            final Field field = CommandEventMediatorExtension.class.getDeclaredField("ATTR_ACTIVE_COMMAND_LISTENERS");
            field.setAccessible(true);
            return (String) field.get(null);
        } catch (final Exception e) {
            throw new AssertionError("failed to read 'ATTR_ACTIVE_COMMAND_LISTENERS'");
        }
    }

    @SuppressWarnings("unchecked")
    static Attribute<List<ICommandListener>> COMMAND_LISTENERS_ATTRIBUTE() {
        try {
            final Field field = CommandEventMediatorExtension.class.getDeclaredField("COMMAND_LISTENERS_ATTRIBUTE");
            field.setAccessible(true);
            return (Attribute<List<ICommandListener>>) field.get(null);
        } catch (final Exception e) {
            throw new AssertionError("failed to read 'COMMAND_LISTENERS_ATTRIBUTE'");
        }
    }
}
