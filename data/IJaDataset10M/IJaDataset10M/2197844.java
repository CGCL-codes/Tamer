package net.sf.gridarta.gui.dialog.prefs;

import java.awt.Component;
import java.awt.Container;
import java.awt.GridBagLayout;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Locale;
import java.util.prefs.Preferences;
import javax.swing.AbstractButton;
import javax.swing.Box;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.TitledBorder;
import net.sf.gridarta.MainControl;
import net.sf.gridarta.gui.utils.GUIConstants;
import net.sf.gridarta.model.settings.GlobalSettings;
import net.sf.gridarta.utils.ActionBuilderUtils;
import net.sf.gridarta.utils.StringUtils;
import net.sf.japi.swing.action.ActionBuilder;
import net.sf.japi.swing.action.ActionBuilderFactory;
import net.sf.japi.swing.misc.LocaleListCellRenderer;
import net.sf.japi.swing.prefs.AbstractPrefs;
import net.sf.japi.util.LocaleComparator;
import org.jetbrains.annotations.NotNull;

/**
 * Preferences Module for user interface preferences.
 * @author <a href="mailto:cher@riedquat.de">Christian Hujer</a>
 * @serial exclude
 */
public class GUIPreferences extends AbstractPrefs {

    /**
     * Preferences key for language.
     */
    public static final String PREFERENCES_LANGUAGE = "language";

    /**
     * The serial version UID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Action Builder.
     */
    private static final ActionBuilder ACTION_BUILDER = ActionBuilderFactory.getInstance().getActionBuilder("net.sf.gridarta");

    /**
     * Preferences.
     */
    private static final Preferences PREFERENCES = Preferences.userNodeForPackage(MainControl.class);

    /**
     * An empty <code>String</code> array.
     */
    @NotNull
    private static final String[] EMPTY_STRING_ARRAY = new String[0];

    /**
     * The {@link GlobalSettings} to use.
     */
    @NotNull
    private final GlobalSettings globalSettings;

    /**
     * /** ComboBox for choosing the locale.
     */
    private JComboBox localeBox;

    /**
     * Whether to show the main window's toolbar.
     */
    private AbstractButton showMainToolbar;

    /**
     * Locale[].
     */
    private Locale[] locales;

    /**
     * LocaleComparator.
     */
    private final Comparator<Locale> comp = new LocaleComparator();

    /**
     * Creates a new instance.
     * @param globalSettings the global settings to use
     */
    public GUIPreferences(@NotNull final GlobalSettings globalSettings) {
        this.globalSettings = globalSettings;
        setListLabelText(ActionBuilderUtils.getString(ACTION_BUILDER, "prefsGUI.title"));
        setListLabelIcon(ACTION_BUILDER.getIcon("prefsGUI.icon"));
        add(createGlobalPanel());
        add(createLayoutPanel());
        add(Box.createVerticalGlue());
    }

    /**
     * Creates a titled border.
     * @param titleKey the action key for border title
     * @return the titled border
     */
    private static Border createTitledBorder(final String titleKey) {
        return new CompoundBorder(new TitledBorder(ActionBuilderUtils.getString(ACTION_BUILDER, titleKey)), GUIConstants.DIALOG_BORDER);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void apply() {
        final Locale loc = (Locale) localeBox.getSelectedItem();
        if (loc != null) {
            PREFERENCES.put(PREFERENCES_LANGUAGE, loc.getLanguage());
        } else {
            PREFERENCES.remove(PREFERENCES_LANGUAGE);
        }
        globalSettings.setShowMainToolbar(showMainToolbar.isSelected());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void revert() {
        final String current = PREFERENCES.get(PREFERENCES_LANGUAGE, null);
        localeBox.setSelectedIndex(Arrays.binarySearch(locales, current != null ? new Locale(current) : null, comp));
        showMainToolbar.setSelected(globalSettings.isShowMainToolbar());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void defaults() {
        localeBox.setSelectedIndex(Arrays.binarySearch(locales, null, comp));
        showMainToolbar.setSelected(GlobalSettings.SHOW_MAIN_TOOLBAR_DEFAULT);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isChanged() {
        final Locale loc = (Locale) localeBox.getSelectedItem();
        final String currentName = PREFERENCES.get(PREFERENCES_LANGUAGE, null);
        final Locale current = currentName != null ? new Locale(currentName) : null;
        return !((loc == null ? current == null : loc.equals(current)) && showMainToolbar.isSelected() == globalSettings.isShowMainToolbar());
    }

    /**
     * Constructs the combo box for the selection of locales.
     * @return the combo box with locales to select
     */
    private Component buildLocaleBox() {
        final Container lineLayout = Box.createHorizontalBox();
        final CharSequence availableLocales = ACTION_BUILDER.getString("availableLocales");
        final String[] locNames = availableLocales != null ? StringUtils.PATTERN_WHITESPACE.split(availableLocales, 0) : EMPTY_STRING_ARRAY;
        locales = new Locale[locNames.length + 1];
        for (int i = 0; i < locNames.length; i++) {
            locales[i + 1] = new Locale(locNames[i]);
        }
        Arrays.sort(locales, comp);
        lineLayout.add(ActionBuilderUtils.newLabel(ACTION_BUILDER, "optionsLanguage"));
        localeBox = new JComboBox(locales);
        localeBox.setRenderer(new LocaleListCellRenderer());
        final String current = PREFERENCES.get(PREFERENCES_LANGUAGE, null);
        localeBox.setSelectedIndex(Arrays.binarySearch(locales, current != null ? new Locale(current) : null, comp));
        lineLayout.add(localeBox);
        return lineLayout;
    }

    /**
     * Creates the sub-panel with the global settings.
     * @return the sub-panel
     */
    private Component createGlobalPanel() {
        final JComponent panel = new JPanel(new GridBagLayout());
        final PreferencesHelper preferencesHelper = new PreferencesHelper(panel);
        panel.setBorder(createTitledBorder("optionsGlobal"));
        preferencesHelper.addComponent(buildLocaleBox());
        return panel;
    }

    /**
     * Creates the sub-panel with the layout settings.
     * @return the sub-panel
     */
    private Component createLayoutPanel() {
        final JComponent panel = new JPanel(new GridBagLayout());
        final PreferencesHelper preferencesHelper = new PreferencesHelper(panel);
        panel.setBorder(createTitledBorder("optionsLayout"));
        showMainToolbar = new JCheckBox(ACTION_BUILDER.createAction(false, "optionsShowMainToolbar"));
        showMainToolbar.setSelected(globalSettings.isShowMainToolbar());
        preferencesHelper.addComponent(showMainToolbar);
        return panel;
    }
}
