package org.fest.swing.query;

import javax.swing.text.JTextComponent;
import org.fest.swing.edt.GuiQuery;
import static org.fest.swing.edt.GuiActionRunner.execute;

/**
 * Understands an action, executed in the event dispatch thread, that returns the text of a 
 * <code>{@link JTextComponent}</code>.
 * @see JTextComponent#getText()
 *
 * @author Yvonne Wang
 * @author Alex Ruiz
 */
public final class JTextComponentTextQuery {

    /**
   * Returns the text of the given <code>{@link JTextComponent}</code>. This action is executed in the event dispatch
   * thread.
   * @param textComponent the given <code>JTextComponent</code>.
   * @return the text of the given <code>JTextComponent</code>.
   * @see JTextComponent#getText()
   */
    public static String textOf(final JTextComponent textComponent) {
        return execute(new GuiQuery<String>() {

            protected String executeInEDT() {
                return textComponent.getText();
            }
        });
    }

    private JTextComponentTextQuery() {
    }
}
