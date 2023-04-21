package pl.org.minions.stigma.editor.resourceset.map;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.util.LinkedList;
import java.util.List;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.ListCellRenderer;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import pl.org.minions.stigma.editor.gui.FieldChangeListener;
import pl.org.minions.stigma.editor.gui.GUIFactory;
import pl.org.minions.stigma.editor.resourceset.ResourceSetModel;
import pl.org.minions.stigma.game.map.MapType;
import pl.org.minions.stigma.game.map.TerrainSet;

/**
 * Properties outline of the map type editor.
 */
public class MapTypePropertiesPanel extends JPanel {

    private static final long serialVersionUID = 1L;

    private MapType mapType;

    private JTextField nameField;

    private JTextField descriptionField;

    private JComboBox terrainsetField;

    private SpinnerNumberModel widthField;

    private SpinnerNumberModel heightField;

    private SpinnerNumberModel segmentWidthField;

    private SpinnerNumberModel segmentHeightField;

    private SpinnerNumberModel maxActorsField;

    private List<ChangeListener> changeListeners = new LinkedList<ChangeListener>();

    private boolean listenToChanges = true;

    /**
     * Constructor.
     */
    public MapTypePropertiesPanel() {
        super();
        this.setLayout(null);
        Dimension dimension = new Dimension(180, 400);
        this.setMinimumSize(dimension);
        this.setPreferredSize(dimension);
        nameField = GUIFactory.createTitledTextField("Name:", "MyMap", 0, 0, 160, this);
        descriptionField = GUIFactory.createTitledTextField("Description:", "", 0, 1, 160, this);
        terrainsetField = GUIFactory.createTitledComboBoxField("Terrainset:", 0, 2, 160, this);
        terrainsetField.setRenderer(new TerrainSetListCellRenderer());
        widthField = GUIFactory.createTitledNumberSpinnerField("Width:", 10, 0, 3, 75, this);
        widthField.setMinimum(10);
        widthField.setMaximum((int) Short.MAX_VALUE);
        heightField = GUIFactory.createTitledNumberSpinnerField("Height:", 10, 85, 3, 75, this);
        heightField.setMinimum(10);
        heightField.setMaximum((int) Short.MAX_VALUE);
        segmentWidthField = GUIFactory.createTitledNumberSpinnerField("Seg. width:", 10, 0, 4, 75, this);
        segmentWidthField.setMinimum(10);
        segmentWidthField.setMaximum((int) Byte.MAX_VALUE);
        segmentHeightField = GUIFactory.createTitledNumberSpinnerField("Seg. height:", 10, 85, 4, 75, this);
        segmentHeightField.setMinimum(10);
        segmentHeightField.setMaximum((int) Byte.MAX_VALUE);
        maxActorsField = GUIFactory.createTitledNumberSpinnerField("Max. actors:", 100, 0, 5, 75, this);
        maxActorsField.setMinimum(1);
        maxActorsField.setMaximum((int) Short.MAX_VALUE);
        FieldChangeListener fieldChangeListener = new FieldChangeListener() {

            @Override
            public void fieldChanged() {
                updateMapType();
                notifyChange();
            }
        };
        nameField.addActionListener(fieldChangeListener);
        nameField.addCaretListener(fieldChangeListener);
        descriptionField.addActionListener(fieldChangeListener);
        descriptionField.addCaretListener(fieldChangeListener);
        heightField.addChangeListener(fieldChangeListener);
        widthField.addChangeListener(fieldChangeListener);
        segmentWidthField.addChangeListener(fieldChangeListener);
        segmentHeightField.addChangeListener(fieldChangeListener);
        maxActorsField.addChangeListener(fieldChangeListener);
    }

    /**
     * Returns map type name.
     * @return map type name
     */
    public String getMapTypeFieldName() {
        return nameField.getText().trim();
    }

    /**
     * Returns map type description.
     * @return map type description
     */
    protected String getMapTypeFieldDescription() {
        return descriptionField.getText().trim();
    }

    /**
     * Returns map type sizeX.
     * @return map type sizeX
     */
    protected short getMapTypeFieldSizeX() {
        return Short.parseShort(widthField.getValue().toString());
    }

    /**
     * Returns map type sizeY.
     * @return map type sizeY
     */
    protected short getMapTypeFieldSizeY() {
        return Short.parseShort(heightField.getValue().toString());
    }

    /**
     * Returns map type segment sizeX.
     * @return map type segment sizeX
     */
    protected byte getMapTypeFieldSegmentSizeX() {
        return Byte.parseByte(segmentWidthField.getValue().toString());
    }

    /**
     * Returns map type segment sizeY.
     * @return map type segment sizeY
     */
    protected byte getMapTypeFieldSegmentSizeY() {
        return Byte.parseByte(segmentHeightField.getValue().toString());
    }

    /**
     * Returns map type max actors.
     * @return map type max actors
     */
    protected short getMapTypeFieldMaxActors() {
        return Short.parseShort(maxActorsField.getValue().toString());
    }

    /**
     * Returns map type terrain set id.
     * @return terrain set id
     */
    protected short getMapTypeFieldTerrainSetId() {
        if (terrainsetField.getSelectedItem() != null) {
            return ((TerrainSet) terrainsetField.getSelectedItem()).getId();
        }
        return -1;
    }

    /**
     * Initializes properties panel with a given mapType.
     * @param mapType
     *            map type
     */
    public void init(MapType mapType) {
        this.mapType = mapType;
        updateValues();
        updateMapType();
    }

    private void updateValues() {
        listenToChanges = false;
        nameField.setText(mapType.getName());
        descriptionField.setText(mapType.getDescription());
        widthField.setValue(mapType.getSizeX());
        heightField.setValue(mapType.getSizeY());
        segmentWidthField.setValue(mapType.getSegmentSizeX());
        segmentHeightField.setValue(mapType.getSegmentSizeY());
        maxActorsField.setValue(mapType.getMaxActors());
        terrainsetField.removeAllItems();
        for (TerrainSet terrainSet : ResourceSetModel.getInstance().getResourceSet().getTerrainSets()) {
            terrainsetField.addItem(terrainSet);
        }
        TerrainSet selectedTerrainSet = ResourceSetModel.getInstance().getResourceSet().getTerrainSet(mapType.getTerrainSetId());
        terrainsetField.setSelectedItem(selectedTerrainSet);
        listenToChanges = true;
    }

    private void updateMapType() {
        if (!listenToChanges) {
            return;
        }
        mapType.setName(getMapTypeFieldName());
        mapType.setDescription(getMapTypeFieldDescription());
        mapType.setTerrainSetId(getMapTypeFieldTerrainSetId());
        mapType.setSizeX(getMapTypeFieldSizeX());
        mapType.setSizeY(getMapTypeFieldSizeY());
        mapType.setSegmentSizeX(getMapTypeFieldSegmentSizeX());
        mapType.setSegmentSizeY(getMapTypeFieldSegmentSizeY());
        mapType.setMaxActors(getMapTypeFieldMaxActors());
    }

    /**
     * Registers change listener.
     * @param changeListener
     *            change listener
     */
    public void addChangeListener(ChangeListener changeListener) {
        changeListeners.add(changeListener);
    }

    /**
     * Removes change listener.
     * @param changeListener
     *            change listener
     */
    public void removeValidatorListener(ChangeListener changeListener) {
        changeListeners.remove(changeListener);
    }

    /**
     * Notifies change in this panel.
     */
    protected void notifyChange() {
        for (ChangeListener changeListener : changeListeners) {
            changeListener.stateChanged(new ChangeEvent(this));
        }
    }

    private static class TerrainSetListCellRenderer extends JLabel implements ListCellRenderer {

        private static final long serialVersionUID = 1L;

        /** {@inheritDoc} */
        @Override
        public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            if (value instanceof TerrainSet) {
                String name = ((TerrainSet) value).getName();
                this.setText(name == null || name.isEmpty() ? "<" + ((TerrainSet) value).getId() + ">" : name);
            }
            if (isSelected) {
                this.setOpaque(true);
                this.setForeground(Color.WHITE);
                this.setBackground(Color.BLUE);
            } else {
                this.setForeground(Color.BLACK);
                this.setBackground(Color.WHITE);
            }
            return this;
        }
    }
}
