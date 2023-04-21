package free.jin.console.prefs;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.util.ArrayList;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import free.jin.console.ConsoleManager;
import free.util.TextUtilities;
import free.util.swing.IntegerStrictPlainDocument;

/**
 * An extension of the <code>TextPrefsPanel</code> class which allows
 * adding a channels category panel from properties. The channels category
 * panel allows choosing text styles for numbered channels, such as ones
 * available on chessclub.com, freechess.org and similar servers.
 * To add such a panel you must specify that the category panel is custom, give
 * it the an id ending with the string <pre>"channels-panel"</pre> and specify
 * the following properties:
 * <ul>
 *   <li> channels-panel.ids: A list of ids specifying channel tell properties,
 *        separated by semicolons. For example:
 *        <pre>"channel-tell;channel-atell"</pre>.
 *   <li> channels-panel.channels-count: The amount of channels available on the 
 *        server.
 *   <li> channels-panel.name: The name of the category/panel.
 * </ul>
 * The <pre>"channels-panel"</pre> part of the property name can be replaced
 * with any string ending with <pre>"channels-panel"</pre>.
 * Here is an example properties which add a channels panel:
 * <pre>
 * preferences.categories.1.custom = true
 * preferences.categories.1.id = 1.channels-panel
 * preferences.categories.1.channels-panel.ids = channel-tell;channel-atell
 * preferences.categories.1.channels-panel.channels-count = 400
 * preferences.categories.1.channels-panel.name = Channels
 * </pre>
 */
public class ChannelTextPrefsPanel extends TextPrefsPanel {

    /**
   * A ArrayList holding all the <code>ChannelPanels</code>.
   */
    private ArrayList channelPanels;

    /**
   * Creates a new <code>ChannelTextPrefsPanel</code> for the specified
   * <code>ConsoleManager</code>.
   */
    public ChannelTextPrefsPanel(ConsoleManager consoleManager) {
        super(consoleManager);
    }

    /**
   * Overrides <code>createCustomCategoryPanel</code> to create
   * <code>ChannelCategoryPanels</code>.
   */
    protected CategoryPanel createCustomCategoryPanel(String id) {
        if (id.endsWith("channels-panel")) {
            String categoryName = prefs.getString("preferences.categories." + id + ".name");
            String[] categoryIDs = TextUtilities.getTokens(prefs.getString("preferences.categories." + id + ".ids"), ";");
            int channelsCount = prefs.getInt("preferences.categories." + id + ".channels-count");
            ChannelsCategoryPanel channelCategoryPanel = createChannelsCategoryPanel(categoryName, categoryIDs, channelsCount);
            if (channelPanels == null) channelPanels = new ArrayList();
            channelPanels.add(channelCategoryPanel);
            return channelCategoryPanel;
        } else return super.createCustomCategoryPanel(id);
    }

    /**
   * Creates a new ChannelsCategoryPanel.
   */
    private ChannelsCategoryPanel createChannelsCategoryPanel(String categoryName, String[] categoryIDs, int channelsCount) {
        String mainCategory = categoryIDs[0];
        Font font = getCategoryFont(mainCategory);
        Color foreground = (Color) prefs.lookup("foreground." + mainCategory, Color.white);
        Color background = prefs.getColor("background");
        boolean antialias = prefs.getBool("output-text.antialias", false);
        TextStyleChooserPanel textStyleChooser = new TextStyleChooserPanel(font, foreground, background, antialias, false, false);
        return new ChannelsCategoryPanel(categoryName, textStyleChooser, categoryIDs, channelsCount);
    }

    /**
   * An extension of <code>TextPrefsPanel.CategoryPanel</code> which
   * allows selecting the text style of numbered channels. It pretends to be
   * a CategoryPanel for different categories depending on the currently
   * specified (by the user) channel.
   */
    private class ChannelsCategoryPanel extends CategoryPanel {

        /**
     * The textfield where the user chooses the channel number.
     */
        private final JTextField channelNumberField;

        /**
     * Creates a new ChannelsCategoryPanel with the specified category name,
     * category ids, TextStyleChooserPanel and amount of channels available on
     * the server.
     */
        public ChannelsCategoryPanel(String categoryName, TextStyleChooserPanel textStyleChooser, String[] categoryIDs, int channelsCount) {
            super(categoryName, textStyleChooser, categoryIDs);
            channelNumberField = new free.workarounds.FixedJTextField(new IntegerStrictPlainDocument(0, channelsCount), "", String.valueOf(channelsCount).length() + 1);
            channelNumberField.getDocument().addDocumentListener(new DocumentListener() {

                public void changedUpdate(DocumentEvent e) {
                    setChannel(channelNumberField.getText());
                }

                public void insertUpdate(DocumentEvent e) {
                    setChannel(channelNumberField.getText());
                }

                public void removeUpdate(DocumentEvent e) {
                    setChannel(channelNumberField.getText());
                }
            });
            this.setLayout(new BorderLayout(5, 5));
            JPanel chNumberPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
            chNumberPanel.add(new JLabel("Channel number"));
            chNumberPanel.add(channelNumberField);
            JPanel chNumberAndSeparatorPanel = new JPanel(new BorderLayout(5, 5));
            chNumberAndSeparatorPanel.add(chNumberPanel, BorderLayout.NORTH);
            chNumberAndSeparatorPanel.add(new JSeparator(JSeparator.HORIZONTAL), BorderLayout.CENTER);
            this.add(chNumberAndSeparatorPanel, BorderLayout.NORTH);
            this.add(textStyleChooser, BorderLayout.CENTER);
        }

        /**
     * Sets the TextStyleChooserPanel to display the settings of the currently
     * selected channel. If the channel string is an empty string, the panel
     * will show the default channel settings.
     */
        public void setChannel(String channel) {
            TextStyleChooserPanel textStyleChooser = getTextStyleChooser();
            textStyleChooser.removeChangeListener(settingsChangeListener);
            String category = super.getMainCategory() + ("".equals(channel) ? "" : "." + channel);
            Font font = getCategoryFont(category);
            Color foreground = (Color) prefs.lookup("foreground." + category, Color.white);
            textStyleChooser.setSelectedFont(font);
            textStyleChooser.setSelectedForeground(foreground);
            textStyleChooser.addChangeListener(settingsChangeListener);
        }

        /**
     * Returns the currently displayed channel (as a string) or an empty string
     * if the default channel settings are displayed.
     */
        public String getChannel() {
            return channelNumberField.getText();
        }

        /**
     * Overrides getCategories() to pretend to be editing different categories
     * depending on the currently specified channel.
     */
        public String[] getCategories() {
            String[] categories = super.getCategories();
            String channel = getChannel();
            if (!"".equals(channel)) for (int i = 0; i < categories.length; i++) categories[i] = categories[i] + "." + channel;
            return categories;
        }
    }
}
