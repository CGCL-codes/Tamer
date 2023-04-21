package org.fest.swing.driver;

import javax.swing.JComboBox;
import org.fest.swing.annotation.RunsInCurrentThread;
import static java.lang.String.valueOf;
import static org.fest.util.Strings.concat;

/**
 * Understands verification that a given number is a valid index of an item in a <code>{@link JComboBox}</code>.
 * <p>
 * <b>Note:</b> Methods in this class are <b>not</b> executed in the event dispatch thread (EDT.) Clients are
 * responsible for invoking them in the EDT.
 * </p>
 *
 * @author Alex Ruiz
 */
final class JComboBoxItemIndexValidator {

    @RunsInCurrentThread
    static void validateIndex(JComboBox comboBox, int index) {
        int itemCount = comboBox.getItemCount();
        if (index >= 0 && index < itemCount) return;
        throw new IndexOutOfBoundsException(concat("Item index (", valueOf(index), ") should be between [", valueOf(0), "] and [", valueOf(itemCount - 1), "] (inclusive)"));
    }

    private JComboBoxItemIndexValidator() {
    }
}
