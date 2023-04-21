package net.sf.karatasi.desktop.settings;

import java.awt.GridBagLayout;
import java.util.prefs.Preferences;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import net.sf.japi.swing.action.ActionBuilder;
import org.jetbrains.annotations.NotNull;

/**
 * @author <a href="mailto:kussinger@sourceforge.net">Mathias Kussinger</a>
 *
 */
public abstract class Settings {

    /** The ActionBuilder. */
    private final ActionBuilder actionBuilder;

    /** The preferences storage. */
    private final Preferences prefs;

    /** Constructor
     * @param prefs the persistent preference store
     * @param actionBuilder the action builder for lacalization and default values
     */
    protected Settings(final Preferences prefs, final ActionBuilder actionBuilder) {
        this.prefs = prefs;
        this.actionBuilder = actionBuilder;
    }

    /** Getter for actionBuilder
     * @return the action builder
     */
    protected Preferences getPreferences() {
        return prefs;
    }

    /** Getter for preferences
     * @return the action builder
     */
    protected ActionBuilder getActionBuilder() {
        return actionBuilder;
    }

    /** Creates a panel with a titled border and equally weighted components.
     * @param key Key for the title of the titled border to get the title from {@link #actionBuilder}.
     * @param components Components to add, must either a String or a JComponent.
     *                   If a component is a String, it is treated as a key for a text from {@link #actionBuilder} for a JLabel.
     * @return JPanel with the components.
     */
    @SuppressWarnings({ "TypeMayBeWeakened" })
    protected JComponent createTitledPanel(@NotNull final String key, @NotNull final Object... components) {
        final JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder(actionBuilder.getString(key)));
        for (final Object component : components) {
            if (component instanceof JComponent) {
                panel.add((JComponent) component);
            } else if (component instanceof String) {
                panel.add(new JLabel(actionBuilder.getString((String) component)));
            } else {
                throw new IllegalArgumentException(component + " not of type JComponent or String.");
            }
        }
        return panel;
    }

    /** Create the settings panel.
     * @return the panel
     */
    abstract JComponent createPanel();

    /** Check if the entered value is valid.
     * @return true if value is valid
     */
    abstract boolean check();

    /** Apply the settings.
     * Get the entered values, store them and apply them to the compnent.
     */
    abstract void apply();

    /** Set to default value.
     */
    abstract void loadDefault();

    /** Load the settings from the persistent storage.
     */
    abstract void load();

    /** Test if the displayed settings values have been changed.
     * @return true if different values have been entered.
     */
    abstract boolean isChanged();
}
