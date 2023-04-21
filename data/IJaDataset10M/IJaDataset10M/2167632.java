package net.sf.gridarta.var.atrinik.gui.mappropertiesdialog;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Window;
import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileFilter;
import net.sf.gridarta.gui.dialog.help.Help;
import net.sf.gridarta.gui.map.maptilepane.AbstractMapTilePane;
import net.sf.gridarta.gui.map.maptilepane.IsoMapTilePane;
import net.sf.gridarta.gui.utils.GUIConstants;
import net.sf.gridarta.gui.utils.TextComponentUtils;
import net.sf.gridarta.model.mapmanager.MapManager;
import net.sf.gridarta.model.mapmodel.MapModel;
import net.sf.gridarta.model.mappathnormalizer.MapPathNormalizer;
import net.sf.gridarta.model.settings.GlobalSettings;
import net.sf.gridarta.utils.ActionBuilderUtils;
import net.sf.gridarta.utils.Size2D;
import net.sf.gridarta.var.atrinik.model.archetype.Archetype;
import net.sf.gridarta.var.atrinik.model.gameobject.GameObject;
import net.sf.gridarta.var.atrinik.model.maparchobject.MapArchObject;
import net.sf.japi.swing.action.ActionBuilder;
import net.sf.japi.swing.action.ActionBuilderFactory;
import net.sf.japi.swing.action.ActionMethod;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A dialog to change the properties of a map, like several flags and settings
 * about the environment and the map tiles.
 * @author <a href="mailto:andi.vogl@gmx.net">Andreas Vogl</a>
 * @author <a href="mailto:michael.toennies@nord-com.net">Michael Toennies</a>
 * @author <a href="mailto:cher@riedquat.de">Christian Hujer</a>
 * @author Andreas Kirschbaum
 */
public class MapPropertiesDialog extends JOptionPane {

    /**
     * Serial version UID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Action Builder.
     */
    @NotNull
    private static final ActionBuilder ACTION_BUILDER = ActionBuilderFactory.getInstance().getActionBuilder("net.sf.gridarta");

    /**
     * The parent frame for help windows.
     * @serial
     */
    @NotNull
    private final JFrame helpParent;

    /**
     * The {@link MapModel} this dialog shows.
     * @serial
     */
    @NotNull
    private final MapModel<GameObject, MapArchObject, Archetype> mapModel;

    /**
     * The {@link Window} that represents this dialog.
     * @serial
     */
    @Nullable
    private Window dialog = null;

    /**
     * The message text.
     * @serial
     */
    @NotNull
    private final JTextArea mapDescription = new JTextArea(4, 4);

    /**
     * The map's name.
     * @serial
     */
    @NotNull
    private final JTextField mapName = new JTextField();

    /**
     * The map's region.
     * @serial
     */
    @NotNull
    private final JTextField mapRegion = new JTextField();

    /**
     * The name of the sound file.
     * @serial
     */
    @NotNull
    private final JTextField mapBackgroundMusic = new JTextField();

    /**
     * The map's weather.
     * @serial
     */
    @NotNull
    private final JTextField mapWeather = new JTextField();

    /**
     * The width in squares.
     * @serial
     */
    @NotNull
    private final JTextField mapWidthField = new JTextField();

    /**
     * The height in squares.
     * @serial
     */
    @NotNull
    private final JTextField mapHeightField = new JTextField();

    /**
     * The outdoor attribute.
     * @serial
     */
    @NotNull
    private final AbstractButton checkboxOutdoor = new JCheckBox();

    /**
     * The enter x attribute.
     * @serial
     */
    @NotNull
    private final JTextField fieldEnterX = new JTextField();

    /**
     * The enter y attribute.
     * @serial
     */
    @NotNull
    private final JTextField fieldEnterY = new JTextField();

    /**
     * The swap time attribute.
     * @serial
     */
    @NotNull
    private final JTextField fieldSwapTime = new JTextField();

    /**
     * The reset timeout attribute.
     * @serial
     */
    @NotNull
    private final JTextField fieldResetTimeout = new JTextField();

    /**
     * The map difficulty attribute.
     * @serial
     */
    @NotNull
    private final JTextField fieldDifficulty = new JTextField();

    /**
     * The map darkness attribute.
     * @serial
     */
    @NotNull
    private final JTextField fieldDarkness = new JTextField();

    /**
     * The fixed reset attribute.
     * @serial
     */
    @NotNull
    private final AbstractButton checkboxFixedReset = new JCheckBox();

    /**
     * The checkbox for the no save attribute.
     * @serial
     */
    @NotNull
    private final AbstractButton checkboxNoSave = new JCheckBox();

    /**
     * The checkbox for the no magic attribute.
     * @serial
     */
    @NotNull
    private final AbstractButton checkboxNoMagic = new JCheckBox();

    /**
     * The checkbox for the no priest attribute.
     * @serial
     */
    @NotNull
    private final AbstractButton checkboxNoPriest = new JCheckBox();

    /**
     * The checkbox for the no summon attribute.
     * @serial
     */
    @NotNull
    private final AbstractButton checkboxNoSummon = new JCheckBox();

    /**
     * The checkbox for the no harm attribute.
     * @serial
     */
    @NotNull
    private final AbstractButton checkboxNoHarm = new JCheckBox();

    /**
     * The checkbox for the no fixed login attribute.
     * @serial
     */
    @NotNull
    private final AbstractButton checkboxNoFixedLogin = new JCheckBox();

    /**
     * The checkbox for the unique attribute.
     * @serial
     */
    @NotNull
    private final AbstractButton checkboxUnique = new JCheckBox();

    /**
     * The checkbox for the fixed reset time attribute.
     * @serial
     */
    @NotNull
    private final AbstractButton checkboxFixedResetTime = new JCheckBox();

    /**
     * The checkbox for the player no save attribute.
     * @serial
     */
    @NotNull
    private final AbstractButton checkboxPlayerNoSave = new JCheckBox();

    /**
     * The checkbox for the pvp attribute.
     * @serial
     */
    @NotNull
    private final AbstractButton checkboxPvp = new JCheckBox();

    /**
     * The button for ok.
     * @serial
     */
    @NotNull
    private final JButton okButton = new JButton(ACTION_BUILDER.createAction(false, "mapOkay", this));

    /**
     * The button for cancel.
     * @serial
     */
    @NotNull
    private final JButton cancelButton = new JButton(ACTION_BUILDER.createAction(false, "mapCancel", this));

    /**
     * The {@link AbstractMapTilePane}.
     * @serial
     */
    @NotNull
    private final AbstractMapTilePane<GameObject, MapArchObject, Archetype> mapTilePane;

    /**
     * Whether the map tile pane was enabled.
     * @serial
     */
    private final boolean mapTilePaneEnabled;

    /**
     * Creates a new instance.
     * @param helpParent the parent frame for help windows
     * @param mapManager the map manager to use
     * @param globalSettings the global settings instance
     * @param mapModel the map model whose properties are shown/edited.
     * @param mapFileFilter the Swing file filter to use
     * @param mapPathNormalizer the map path normalizer for converting map paths
     * to files
     */
    public MapPropertiesDialog(@NotNull final JFrame helpParent, @NotNull final MapManager<GameObject, MapArchObject, Archetype> mapManager, @NotNull final GlobalSettings globalSettings, @NotNull final MapModel<GameObject, MapArchObject, Archetype> mapModel, @NotNull final FileFilter mapFileFilter, @NotNull final MapPathNormalizer mapPathNormalizer) {
        okButton.setDefaultCapable(true);
        final JButton helpButton = new JButton(ACTION_BUILDER.createAction(false, "mapHelp", this));
        final JButton restoreButton = new JButton(ACTION_BUILDER.createAction(false, "mapRestore", this));
        setOptions(new Object[] { helpButton, okButton, restoreButton, cancelButton });
        this.helpParent = helpParent;
        this.mapModel = mapModel;
        final MapArchObject map = mapModel.getMapArchObject();
        final JTabbedPane tabs = new JTabbedPane();
        tabs.setBorder(new EmptyBorder(10, 4, 4, 4));
        final Component mainPanel = createMainPanel(map);
        tabs.add(ActionBuilderUtils.getString(ACTION_BUILDER, "mapMapTabTitle"), mainPanel);
        mapTilePane = new IsoMapTilePane<GameObject, MapArchObject, Archetype>(mapManager, globalSettings, mapModel, mapFileFilter, mapPathNormalizer);
        final Component tilePanel = createTilePathPanel(mapTilePane);
        tabs.add(ActionBuilderUtils.getString(ACTION_BUILDER, "mapTilesTabTitle"), tilePanel);
        mapTilePaneEnabled = mapModel.getMapFile() != null;
        tabs.setEnabledAt(tabs.indexOfComponent(tilePanel), mapTilePaneEnabled);
        final Container layoutHack = new JPanel(new BorderLayout());
        layoutHack.add(tabs);
        setMessage(layoutHack);
        TextComponentUtils.setAutoSelectOnFocus(mapDescription);
        TextComponentUtils.setAutoSelectOnFocus(mapName);
        TextComponentUtils.setAutoSelectOnFocus(mapRegion);
        TextComponentUtils.setAutoSelectOnFocus(mapBackgroundMusic);
        TextComponentUtils.setAutoSelectOnFocus(mapWeather);
        TextComponentUtils.setAutoSelectOnFocus(mapWidthField);
        TextComponentUtils.setAutoSelectOnFocus(mapHeightField);
        TextComponentUtils.setAutoSelectOnFocus(fieldEnterX);
        TextComponentUtils.setAutoSelectOnFocus(fieldEnterY);
        TextComponentUtils.setAutoSelectOnFocus(fieldSwapTime);
        TextComponentUtils.setAutoSelectOnFocus(fieldResetTimeout);
        TextComponentUtils.setAutoSelectOnFocus(fieldDifficulty);
        TextComponentUtils.setAutoSelectOnFocus(fieldDarkness);
    }

    /**
     * Creates the main panel.
     * @param map the map arch object to create the panel for
     * @return the main panel
     */
    @NotNull
    private Component createMainPanel(@NotNull final MapArchObject map) {
        final GridBagConstraints gbc = new GridBagConstraints();
        final JComponent mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBorder(GUIConstants.DIALOG_BORDER);
        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.weightx = 1.0;
        gbc.weighty = 1.3;
        mainPanel.add(createMapPanel(map), gbc);
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.weighty = 1.0;
        mainPanel.add(createMapDataPanel(map), gbc);
        gbc.gridx = 2;
        gbc.gridy = 1;
        gbc.weighty = 2.0;
        gbc.gridheight = 4;
        mainPanel.add(createOptionsPanel(map), gbc);
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        return mainPanel;
    }

    /**
     * Creates the map data panel.
     * @param map the map arch object to create the panel for
     * @return the map data panel
     */
    @NotNull
    private Component createMapDataPanel(@NotNull final net.sf.gridarta.model.maparchobject.MapArchObject<MapArchObject> map) {
        mapDescription.setText(map.getText());
        mapDescription.setCaretPosition(0);
        final JScrollPane scrollPane = new JScrollPane(mapDescription);
        mapDescription.setBorder(BorderFactory.createEmptyBorder(1, 4, 0, 0));
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setBorder(createTitledBorder("mapText"));
        return scrollPane;
    }

    /**
     * Creates the options panel.
     * @param map the map arch object to create the panel for
     * @return the options panel
     */
    @NotNull
    private Component createOptionsPanel(@NotNull final MapArchObject map) {
        final JPanel optionPanel = new JPanel(new GridLayout(0, 2));
        optionPanel.add(createPanelLine(fieldEnterX, 10, map.getEnterX(), "mapEnterX"));
        optionPanel.add(createPanelLine(fieldEnterY, 10, map.getEnterY(), "mapEnterY"));
        optionPanel.add(createPanelLine(fieldDifficulty, 10, map.getDifficulty(), "mapDifficulty"));
        optionPanel.add(createPanelLine(fieldDarkness, 10, map.getDarkness(), "mapDarkness"));
        optionPanel.add(createPanelLine(fieldSwapTime, 10, map.getSwapTime(), "mapSwapTime"));
        optionPanel.add(createPanelLine(fieldResetTimeout, 10, map.getResetTimeout(), "mapResetTimeout"));
        optionPanel.add(createPanelCBox(checkboxNoSave, map.isNoSave(), "mapNoSave"));
        optionPanel.add(createPanelCBox(checkboxNoMagic, map.isNoMagic(), "mapNoMagic"));
        optionPanel.add(createPanelCBox(checkboxNoPriest, map.isNoPriest(), "mapNoPrayers"));
        optionPanel.add(createPanelCBox(checkboxNoHarm, map.isNoHarm(), "mapNoHarm"));
        optionPanel.add(createPanelCBox(checkboxNoSummon, map.isNoSummon(), "mapNoSumm"));
        optionPanel.add(createPanelCBox(checkboxNoFixedLogin, map.isFixedLogin(), "mapFixedLogin"));
        optionPanel.add(createPanelCBox(checkboxUnique, map.isUnique(), "mapUnique"));
        optionPanel.add(createPanelCBox(checkboxFixedResetTime, map.isFixedResetTime(), "mapFixedResetTime"));
        optionPanel.add(createPanelCBox(checkboxPlayerNoSave, map.isPlayerNoSave(), "mapPlayerNoSave"));
        optionPanel.add(createPanelCBox(checkboxPvp, map.isPvp(), "mapPvP"));
        final JComponent scrollPane = new JScrollPane(optionPanel);
        scrollPane.setBorder(createTitledBorder("mapOptions"));
        return scrollPane;
    }

    /**
     * Creates the map panel.
     * @param map the map arch object to create the panel for
     * @return the map panel
     */
    @NotNull
    private Component createMapPanel(@NotNull final MapArchObject map) {
        final JPanel mapPanel = new JPanel(new GridBagLayout());
        final GridBagConstraints labelGbc = new GridBagConstraints();
        labelGbc.anchor = GridBagConstraints.EAST;
        labelGbc.insets = new Insets(2, 10, 2, 2);
        final GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(2, 2, 2, 2);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.weightx = 2.0;
        mapPanel.add(ActionBuilderUtils.newLabel(ACTION_BUILDER, "mapName"), labelGbc);
        mapName.setColumns(16);
        mapName.setText(map.getMapName());
        mapPanel.add(mapName, gbc);
        mapPanel.add(ActionBuilderUtils.newLabel(ACTION_BUILDER, "mapBackgroundMusic"), labelGbc);
        mapBackgroundMusic.setColumns(16);
        mapBackgroundMusic.setText(map.getBackgroundMusic());
        mapPanel.add(mapBackgroundMusic, gbc);
        mapPanel.add(ActionBuilderUtils.newLabel(ACTION_BUILDER, "mapRegion"), labelGbc);
        mapRegion.setColumns(16);
        mapRegion.setText(map.getRegion());
        mapPanel.add(mapRegion, gbc);
        mapPanel.add(ActionBuilderUtils.newLabel(ACTION_BUILDER, "mapWeather"), labelGbc);
        mapWeather.setColumns(16);
        mapWeather.setText(map.getWeather());
        mapPanel.add(mapWeather, gbc);
        mapPanel.add(ActionBuilderUtils.newLabel(ACTION_BUILDER, "mapWidth"), labelGbc);
        mapWidthField.setColumns(5);
        mapWidthField.setText(Integer.toString(mapModel.getMapArchObject().getMapSize().getWidth()));
        gbc.gridwidth = 1;
        gbc.weightx = 1.0;
        mapPanel.add(mapWidthField, gbc);
        mapPanel.add(ActionBuilderUtils.newLabel(ACTION_BUILDER, "mapHeight"), labelGbc);
        mapHeightField.setColumns(5);
        mapHeightField.setText(Integer.toString(mapModel.getMapArchObject().getMapSize().getHeight()));
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.weightx = 1.0;
        mapPanel.add(mapHeightField, gbc);
        final Container p2 = new JPanel(new GridBagLayout());
        gbc.gridwidth = 1;
        gbc.weightx = 1.0;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.NONE;
        p2.add(createPanelCBox(checkboxOutdoor, map.isOutdoor(), "mapOutdoor"), gbc);
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        p2.add(createPanelCBox(checkboxFixedReset, map.isFixedReset(), "mapFixedReset"), gbc);
        gbc.weightx = 2.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        mapPanel.add(p2, gbc);
        final JComponent scrollPane = new JScrollPane(mapPanel);
        scrollPane.setBorder(createTitledBorder("mapMap"));
        return scrollPane;
    }

    /**
     * Creates the tile path panel.
     * @param mapTilePane the map tile pane contents
     * @return the tile path panel
     */
    @NotNull
    private static Component createTilePathPanel(@NotNull final Component mapTilePane) {
        final Container panel = new JPanel(new BorderLayout());
        panel.add(mapTilePane, BorderLayout.NORTH);
        return panel;
    }

    /**
     * Action method for help.
     */
    @ActionMethod
    public void mapHelp() {
        new Help(helpParent, "tut_mapattr.html").setVisible(true);
    }

    /**
     * Action method for okay.
     */
    @ActionMethod
    public void mapOkay() {
        if (modifyMapProperties()) {
            setValue(okButton);
        }
    }

    /**
     * Action method for restore.
     */
    @ActionMethod
    public void mapRestore() {
        restoreMapProperties();
    }

    /**
     * Action method for cancel.
     */
    @ActionMethod
    public void mapCancel() {
        setValue(cancelButton);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setValue(@Nullable final Object newValue) {
        super.setValue(newValue);
        if (dialog != null && newValue != UNINITIALIZED_VALUE) {
            dialog.dispose();
        }
    }

    /**
     * Creates a border to be used in dialogs to frame dialog component groups.
     * @param key the i18n Key for looking up the l10n title using the
     * ActionBuilder
     * @return the border
     */
    @NotNull
    private static Border createTitledBorder(@NotNull final String key) {
        return BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder(ActionBuilderUtils.getString(ACTION_BUILDER, key)), GUIConstants.DIALOG_BORDER);
    }

    /**
     * Creates an "attribute"-line (format: &lt;label&gt; &lt;textfield&gt;).
     * @param textField formatted text field
     * @param n the length of the text field
     * @param defaultValue the initial value in formatted text field
     * @param labelKey the (attribute-)label key
     * @return the created panel
     */
    @NotNull
    private static Component createPanelLine(@NotNull final JTextField textField, final int n, final int defaultValue, @NotNull final String labelKey) {
        final Container lineLayout = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        lineLayout.add(ActionBuilderUtils.newLabel(ACTION_BUILDER, labelKey));
        textField.setColumns(n);
        textField.setText(Integer.toString(defaultValue));
        lineLayout.add(textField);
        return lineLayout;
    }

    /**
     * Creates a checkbox-line, similar to {@link #createPanelLine(JTextField,
     * int, int, String)}.
     * @param checkBox the checkbox
     * @param state the initial state
     * @param labelKey the (attribute-)label key
     * @return the panel
     */
    @NotNull
    private static Component createPanelCBox(@NotNull final AbstractButton checkBox, final boolean state, @NotNull final String labelKey) {
        final JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        panel.add(checkBox);
        checkBox.setText(ActionBuilderUtils.getString(ACTION_BUILDER, labelKey));
        checkBox.setSelected(state);
        return panel;
    }

    /**
     * Checks the given values and modifies the current map.
     * @return <code>true</code> if the map properties were edited,
     *         <code>false</code> if the parameters were wrong.
     */
    private boolean modifyMapProperties() {
        final int darkness;
        final int difficulty;
        final int swapTime;
        final int resetTimeout;
        final Size2D mapSize;
        final int enterX;
        final int enterY;
        try {
            final int width = parseProperty(mapWidthField.getText(), "Width");
            final int height = parseProperty(mapHeightField.getText(), "Height");
            enterX = parseProperty(fieldEnterX.getText(), "Enter X");
            enterY = parseProperty(fieldEnterY.getText(), "Enter Y");
            swapTime = parseProperty(fieldSwapTime.getText(), "Swap Time");
            resetTimeout = parseProperty(fieldResetTimeout.getText(), "Reset Timeout");
            difficulty = parseProperty(fieldDifficulty.getText(), "Difficulty");
            if (difficulty < 1) {
                throw new IllegalArgumentException("Difficulty must be > 0");
            }
            darkness = parseProperty(fieldDarkness.getText(), "Darkness");
            if (width < 1 || height < 1) {
                ACTION_BUILDER.showMessageDialog(this, "mapErrorIllegalSize");
                return false;
            }
            mapSize = new Size2D(width, height);
            if (mapName.getText().length() == 0) {
                ACTION_BUILDER.showMessageDialog(this, "mapErrorMissingMapName");
                return false;
            }
        } catch (final IllegalArgumentException e) {
            ACTION_BUILDER.showMessageDialog(this, "mapErrorInvalidEntry", e.getMessage());
            return false;
        }
        mapModel.beginTransaction("Map properties");
        try {
            final MapArchObject mapArchObject = mapModel.getMapArchObject();
            mapArchObject.beginTransaction();
            try {
                mapArchObject.setMapSize(mapSize);
                mapArchObject.setText(mapDescription.getText());
                mapArchObject.setMapName(mapName.getText());
                mapArchObject.setRegion(mapRegion.getText());
                mapArchObject.setWeather(mapWeather.getText());
                mapArchObject.setBackgroundMusic(mapBackgroundMusic.getText());
                mapArchObject.setEnterX(enterX);
                mapArchObject.setEnterY(enterY);
                mapArchObject.setResetTimeout(resetTimeout);
                mapArchObject.setSwapTime(swapTime);
                mapArchObject.setDifficulty(difficulty);
                mapArchObject.setFixedReset(checkboxFixedReset.isSelected());
                mapArchObject.setDarkness(darkness);
                mapArchObject.setOutdoor(checkboxOutdoor.isSelected());
                mapArchObject.setNoSave(checkboxNoSave.isSelected());
                mapArchObject.setNoMagic(checkboxNoMagic.isSelected());
                mapArchObject.setNoPriest(checkboxNoPriest.isSelected());
                mapArchObject.setNoHarm(checkboxNoHarm.isSelected());
                mapArchObject.setNoSummon(checkboxNoSummon.isSelected());
                mapArchObject.setFixedLogin(checkboxNoFixedLogin.isSelected());
                mapArchObject.setUnique(checkboxUnique.isSelected());
                mapArchObject.setFixedResetTime(checkboxFixedResetTime.isSelected());
                mapArchObject.setPlayerNoSave(checkboxPlayerNoSave.isSelected());
                mapArchObject.setPvp(checkboxPvp.isSelected());
                mapTilePane.modifyMapProperties();
            } finally {
                mapArchObject.endTransaction();
            }
        } finally {
            mapModel.endTransaction();
        }
        return true;
    }

    /**
     * This is a simple string-to-int parser that throws
     * IllegalArgumentExceptions with appropriate error messages.
     * @param s string to be parse
     * @param label attribute label for error message
     * @return value of String 's', zero if 's' is empty
     * @throws IllegalArgumentException when parsing fails
     */
    private static int parseProperty(@NotNull final String s, @NotNull final String label) {
        if (s.length() == 0) {
            return 0;
        }
        final int r = Integer.parseInt(s);
        if (r < 0 && !label.equals("Darkness")) {
            throw new IllegalArgumentException(label + ": '" + s + "' is negative.");
        }
        return r;
    }

    /**
     * Resets all map properties to the saved values in the map arch object.
     */
    private void restoreMapProperties() {
        final MapArchObject map = mapModel.getMapArchObject();
        mapDescription.setText(map.getText());
        mapName.setText(map.getMapName());
        mapRegion.setText(map.getRegion());
        mapWeather.setText(map.getWeather());
        mapBackgroundMusic.setText(map.getBackgroundMusic());
        mapWidthField.setText(Integer.toString(map.getMapSize().getWidth()));
        mapHeightField.setText(Integer.toString(map.getMapSize().getHeight()));
        fieldEnterX.setText(Integer.toString(map.getEnterX()));
        fieldEnterY.setText(Integer.toString(map.getEnterY()));
        fieldSwapTime.setText(Integer.toString(map.getSwapTime()));
        fieldResetTimeout.setText(Integer.toString(map.getResetTimeout()));
        fieldDarkness.setText(Integer.toString(map.getDarkness()));
        fieldDifficulty.setText(Integer.toString(map.getDifficulty()));
        checkboxOutdoor.setSelected(map.isOutdoor());
        checkboxFixedReset.setSelected(map.isFixedReset());
        checkboxNoSave.setSelected(map.isNoSave());
        checkboxNoMagic.setSelected(map.isNoMagic());
        checkboxNoPriest.setSelected(map.isNoPriest());
        checkboxNoHarm.setSelected(map.isNoHarm());
        checkboxNoSummon.setSelected(map.isNoSummon());
        checkboxNoFixedLogin.setSelected(map.isFixedLogin());
        checkboxUnique.setSelected(map.isUnique());
        checkboxFixedResetTime.setSelected(map.isFixedResetTime());
        checkboxPlayerNoSave.setSelected(map.isPlayerNoSave());
        checkboxPvp.setSelected(map.isPvp());
        mapTilePane.restoreMapProperties();
    }

    /**
     * Creates and displays the map properties dialog.
     * @param parentComponent the parent component of the dialog
     */
    public void showDialog(@NotNull final Component parentComponent) {
        final String title = ACTION_BUILDER.format("mapTitle", mapModel.getMapArchObject().getMapName(), mapModel.getMapArchObject().getMapName());
        final JDialog window = createDialog(parentComponent, title);
        dialog = window;
        window.getRootPane().setDefaultButton(okButton);
        window.setResizable(true);
        window.setModal(false);
        window.setVisible(true);
        if (!mapTilePaneEnabled) {
            ACTION_BUILDER.showOnetimeMessageDialog(window, WARNING_MESSAGE, "mapTilesNoMapFileNoMapTilePane");
        }
    }
}
