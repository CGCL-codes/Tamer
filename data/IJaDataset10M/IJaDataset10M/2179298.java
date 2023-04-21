package de.hpi.eworld.scenarios;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import de.hpi.eworld.core.ModelManager;
import de.hpi.eworld.core.ui.MainWindow;
import de.hpi.eworld.exporter.sumo.data.SumoManager;
import de.hpi.eworld.exporter.sumo.data.SumoVehicleType;
import de.hpi.eworld.model.db.data.Area;
import de.hpi.eworld.model.db.data.ModelElement;
import de.hpi.eworld.model.db.data.Area.AreaType;
import de.hpi.eworld.networkview.NetworkView;
import de.hpi.eworld.networkview.model.AreaItem;
import de.hpi.eworld.networkview.model.CircleAreaItem;
import de.hpi.eworld.networkview.model.ViewItem;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.swing.DefaultListModel;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import net.infonode.docking.View;

/**
 * The Class holds the Dockwidget for the Scenarios.
 * 
 * @author Markus Behrens, Gerald Toepper
 * 
 */
public class ScenariosDockWidget extends JPanel {

    private static final long serialVersionUID = -6174289501704805415L;

    /**
	 * Currently selected item in the network view.
	 */
    private AreaItem selectedAreaItem;

    /**
	 * Counter for the number of start areas.
	 */
    private int numberOfStartAreas = 0;

    /**
	 * Reference to the main window.
	 */
    private MainWindow mainWindow;

    /**
	 * Counter for the number of destination areas.
	 */
    private int numberOfDestinationAreas = 0;

    private static final String LABEL_IDENTIFIER = "identifier for area";

    private static final String LABEL_START_TIME = "simulation time in start area";

    private static final String LABEL_DESTINATION_TIME = "simulation time in destination area";

    private static final String LABEL_DESTINATION_AREAS = "destination areas of this start area";

    private static final String LABEL_VEHICLE_COUNT = "vehicle count";

    private static final String LABEL_VEHICLE_EMIT_INTERVAL = "vehicle emit interval";

    private static final String LABEL_VEHICLE_TYPES = "vehicle types";

    private static final String TOOLTIP_AREA_CREATION = "Drag an area and drop it on the map. Each underlying edge will be considered to emit (start area) or to assimilate (destination area) vehicles.<br>If no start area is defined on the map, the vehicles will be emitted anywhere on the map. In this case you have to specify all properties in the destination areas. If no destination area is defined on the map, the vehicles will be assimilated anywhere on the map.";

    private static final String TOOLTIP_SIMULATION_TIME = "Specify the simulation time, when vehicles should be emitted (in case this area is a start area) or when vehicles should be assimilated (in case this area is a destination area).";

    private static final String TOOLTIP_DESTINATION_AREAS = "Specify which destination areas should be a destination for this start area.<br>Click on an item in the list in order to change the selection.";

    private static final String TOOLTIP_VEHICLE_TYPES = "Specify which vehicle types should be used for this area.<br>Click on an item in the list in order to change the selection. If no vehicle type is selected, the DefaultVehicle will be used.";

    private static final int UNCHECKED = 0;

    private static final int CHECKED = 1;

    private JLabel identifierLabel;

    private JTextField identifierLineEdit;

    private JLabel simulationTimeLabel;

    private JSlider simulationTimeSlider;

    private JLabel simulationTimeValueLabel;

    private JLabel destinationAreasOfStartAreaLabel;

    private DefaultListModel destinationAreasOfStartAreaListModel;

    private JList destinationAreasOfStartAreaListWidget;

    private JLabel vehicleCountLabel;

    private JSpinner vehicleCountSpinBox;

    private JLabel vehicleEmitIntervalLabel;

    private JSpinner vehicleEmitIntervalSpinBox;

    private JLabel vehicleTypesLabel;

    private DefaultListModel vehicleTypesListModel;

    private JList vehicleTypesListWidget;

    /**
	 * Defines the used size for the icons in the toolbar.
	 */
    private final AreaWidget areaWidget;

    private final JComponent scneariosWidgetComponent;

    private View scenariosView;

    /**
	 * The class represents a dock widget which contains a tab widget. The tab
	 * widget is divided into the tabs <i>Creation</i>, <i>General</i> and
	 * <i>Vehicles</i>. The tab <i>Creation</i> contains widgets for the
	 * creation of scenarios. The tab <i>General</i> contains widgets for
	 * customizing general attributes of an area like the identifier or the
	 * simulation time. The tab <i>Vehicles</i> contains widgets for customizing
	 * vehicle attributes of an area like the number of vehicles and the vehicle
	 * types.
	 * 
	 * @author Markus Behrens, Gerald Toepper
	 */
    public ScenariosDockWidget() {
        super();
        scenariosView = new View("Scenarios", null, this);
        scenariosView.setName("scenarios@" + ScenariosDockWidgetPlugin.PLUGIN_ID);
        setLayout(new BorderLayout());
        areaWidget = new AreaWidget();
        scneariosWidgetComponent = areaWidget.getComponent();
        scneariosWidgetComponent.setMinimumSize(new Dimension(150, 50));
        scneariosWidgetComponent.setMaximumSize(new Dimension(210, scneariosWidgetComponent.getMaximumSize().height));
        add(scneariosWidgetComponent, BorderLayout.CENTER);
    }

    /**
	 * Initializes the events widget.
	 */
    public void initialize() {
        areaWidget.initialize();
    }

    /**
	 * Adds the dock widget to the main window, thus causing it do be shown from
	 * now on
	 */
    public void startUp(final MainWindow parentWindow) {
        this.mainWindow = parentWindow;
        View creationView = new View("Creation", null, createCreationWidget());
        creationView.setName("creation@" + ScenariosDockWidgetPlugin.PLUGIN_ID);
        View vehiclesView = new View("Vehicles", null, createVehiclesWidget());
        vehiclesView.setName("vehicles@" + ScenariosDockWidgetPlugin.PLUGIN_ID);
        View generalView = new View("General", null, createGeneralWidget());
        generalView.setName("general@" + ScenariosDockWidgetPlugin.PLUGIN_ID);
        mainWindow.addView(creationView);
        mainWindow.addView(vehiclesView);
        mainWindow.addView(generalView);
    }

    @Override
    public void setVisible(final boolean b) {
        super.setVisible(b);
    }

    public void setNetworkView(final NetworkView networkView) {
        areaWidget.setNetworkView(networkView);
    }

    private JPanel createCreationWidget() {
        JPanel creationPanel = new JPanel();
        AreaWidget areaWidget = new AreaWidget();
        areaWidget.setToolTipText(TOOLTIP_AREA_CREATION);
        areaWidget = new AreaWidget();
        creationPanel.add(scneariosWidgetComponent, BorderLayout.CENTER);
        return creationPanel;
    }

    private JPanel createGeneralWidget() {
        JPanel generalPanel = new JPanel();
        FormLayout layout = new FormLayout("150px, 50px", "20px, 20px, 20px, 20px, 20px, 20px, 300px");
        generalPanel.setLayout(layout);
        CellConstraints cc = new CellConstraints();
        identifierLabel = new JLabel(LABEL_IDENTIFIER);
        generalPanel.add(identifierLabel, cc.rcw(1, 1, 2));
        identifierLineEdit = new JTextField();
        generalPanel.add(identifierLineEdit, cc.rcw(2, 1, 2));
        identifierLineEdit.addKeyListener(new KeyAdapter() {

            public void keyPressed(KeyEvent ke) {
                saveIdentifier();
            }
        });
        simulationTimeLabel = new JLabel(LABEL_START_TIME);
        simulationTimeLabel.setToolTipText(TOOLTIP_SIMULATION_TIME);
        generalPanel.add(simulationTimeLabel, cc.rcw(4, 1, 2));
        ModelManager modelManager = ModelManager.getInstance();
        simulationTimeSlider = new JSlider(JSlider.HORIZONTAL, modelManager.getSimulationStartTime(), modelManager.getSimulationEndTime(), Area.DEFAULT_SIMULATION_TIME);
        simulationTimeSlider.setToolTipText(TOOLTIP_SIMULATION_TIME);
        simulationTimeSlider.addChangeListener(new ChangeListener() {

            @Override
            public void stateChanged(ChangeEvent arg0) {
                saveSimulationTimeValue();
            }
        });
        generalPanel.add(simulationTimeSlider, cc.rc(5, 1));
        simulationTimeValueLabel = new JLabel(String.valueOf(Area.DEFAULT_SIMULATION_TIME));
        simulationTimeValueLabel.setToolTipText(TOOLTIP_SIMULATION_TIME);
        generalPanel.add(simulationTimeValueLabel, cc.rc(5, 2));
        destinationAreasOfStartAreaLabel = new JLabel(LABEL_DESTINATION_AREAS);
        destinationAreasOfStartAreaLabel.setToolTipText(TOOLTIP_DESTINATION_AREAS);
        destinationAreasOfStartAreaLabel.setVisible(false);
        generalPanel.add(destinationAreasOfStartAreaLabel, cc.rcw(6, 1, 2));
        destinationAreasOfStartAreaListModel = new DefaultListModel();
        destinationAreasOfStartAreaListWidget = new JList(destinationAreasOfStartAreaListModel);
        destinationAreasOfStartAreaListWidget.setToolTipText(TOOLTIP_DESTINATION_AREAS);
        destinationAreasOfStartAreaListWidget.setVisibleRowCount(10);
        destinationAreasOfStartAreaListWidget.setCellRenderer(new CheckListRenderer());
        destinationAreasOfStartAreaListWidget.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        destinationAreasOfStartAreaListWidget.addMouseListener(new MouseAdapter() {

            public void mouseClicked(MouseEvent event) {
                JList list = (JList) event.getSource();
                int index = list.locationToIndex(event.getPoint());
                CheckListItem item = (CheckListItem) list.getModel().getElementAt(index);
                Area area = (Area) selectedAreaItem.getAssociatedElement();
                area.addDestinationArea((Area) item.getData());
                item.setSelected(!item.isSelected());
                list.repaint(list.getCellBounds(index, index));
            }
        });
        generalPanel.add(destinationAreasOfStartAreaListWidget, cc.rc(7, 1));
        return generalPanel;
    }

    private JPanel createVehiclesWidget() {
        JPanel vehiclePanel = new JPanel();
        FormLayout layout = new FormLayout("200px", "20px, 30px, 20px, 30px, 20px, 300px");
        vehiclePanel.setLayout(layout);
        CellConstraints cc = new CellConstraints();
        vehicleCountLabel = new JLabel(LABEL_VEHICLE_COUNT);
        vehiclePanel.add(vehicleCountLabel, cc.rc(1, 1));
        SpinnerNumberModel model = new SpinnerNumberModel(Area.DEFAULT_VEHICLE_COUNT, 1, 1000, 1);
        vehicleCountSpinBox = new JSpinner(model);
        vehicleCountSpinBox.addChangeListener(new ChangeListener() {

            @Override
            public void stateChanged(ChangeEvent e) {
                saveVehicleCountValue();
            }
        });
        vehiclePanel.add(vehicleCountSpinBox, cc.rc(2, 1));
        vehicleEmitIntervalLabel = new JLabel(LABEL_VEHICLE_EMIT_INTERVAL);
        vehiclePanel.add(vehicleEmitIntervalLabel, cc.rc(3, 1));
        model = new SpinnerNumberModel(Area.DEFAULT_VEHICLE_EMIT_INTERVAL, 0, 100, 1);
        vehicleEmitIntervalSpinBox = new JSpinner(model);
        vehicleEmitIntervalSpinBox.addChangeListener(new ChangeListener() {

            @Override
            public void stateChanged(ChangeEvent e) {
                saveVehicleEmitIntervalValue();
            }
        });
        vehiclePanel.add(vehicleEmitIntervalSpinBox, cc.rc(4, 1));
        vehicleTypesLabel = new JLabel(LABEL_VEHICLE_TYPES);
        vehicleTypesLabel.setToolTipText(TOOLTIP_VEHICLE_TYPES);
        vehicleTypesLabel.setVisible(false);
        vehiclePanel.add(vehicleTypesLabel, cc.rc(5, 1));
        vehicleTypesListModel = new DefaultListModel();
        vehicleTypesListWidget = new JList(vehicleTypesListModel);
        vehicleTypesListWidget.setToolTipText(TOOLTIP_VEHICLE_TYPES);
        vehicleTypesListWidget.setVisibleRowCount(10);
        vehicleTypesListWidget.setCellRenderer(new CheckListRenderer());
        vehicleTypesListWidget.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        vehicleTypesListWidget.addMouseListener(new MouseAdapter() {

            public void mouseClicked(MouseEvent event) {
                JList list = (JList) event.getSource();
                int index = list.locationToIndex(event.getPoint());
                CheckListItem item = (CheckListItem) list.getModel().getElementAt(index);
                Area area = (Area) selectedAreaItem.getAssociatedElement();
                area.addVehicleType(item.toString());
                item.setSelected(!item.isSelected());
                list.repaint(list.getCellBounds(index, index));
            }
        });
        vehiclePanel.add(vehicleTypesListWidget, cc.rc(6, 1));
        return vehiclePanel;
    }

    /**
	 * Registers changes concerning the identifier of the selected area.
	 */
    private void saveIdentifier() {
        if (selectedAreaItem != null) {
            Area area = (Area) selectedAreaItem.getAssociatedElement();
            area.setIdentifier(identifierLineEdit.getText());
        }
    }

    /**
	 * Saves the vehicle count value, in case it changed.
	 */
    private void saveVehicleCountValue() {
        if (selectedAreaItem != null) {
            Area area = (Area) selectedAreaItem.getAssociatedElement();
            area.setVehicleCount((Integer) vehicleCountSpinBox.getValue());
        }
    }

    /**
	 * Saves the vehicle emit interval value, in case it changed.
	 */
    private void saveVehicleEmitIntervalValue() {
        if (selectedAreaItem != null) {
            Area area = (Area) selectedAreaItem.getAssociatedElement();
            area.setVehicleEmitInterval((Integer) vehicleEmitIntervalSpinBox.getValue());
        }
    }

    /**
	 * Saves the simulation time value, in case it changed.
	 */
    private void saveSimulationTimeValue() {
        simulationTimeValueLabel.setText(String.valueOf(simulationTimeSlider.getValue()));
        if (selectedAreaItem != null) {
            Area area = (Area) selectedAreaItem.getAssociatedElement();
            area.setSimulationTime(simulationTimeSlider.getValue());
        }
    }

    /**
	 * Called, in case the user clicks an area on the map.
	 * 
	 * @param item
	 *            The view item, that has been clicked.
	 */
    public void onViewItemClicked(ViewItem item) {
        if (item instanceof AreaItem && !item.equals(selectedAreaItem)) {
            selectedAreaItem = (AreaItem) item;
            showInformation((AreaItem) item);
        }
    }

    /**
	 * Called, in case the user clicks in free space.
	 */
    public void onFreeSpaceClicked() {
        selectedAreaItem = null;
        hideInformation();
    }

    /**
	 * Shows information of the selected area and displays all gui elements.
	 * 
	 * @param item
	 *            The area item whose information should be shown.
	 */
    private void showInformation(AreaItem areaItem) {
        Area selectedArea = (Area) areaItem.getAssociatedElement();
        AreaType areaType = selectedArea.getAreaType();
        showVehicleInformation();
        identifierLineEdit.setText(selectedArea.getIdentifier());
        if (areaType == AreaType.START) {
            showAreaInformation();
            vehicleCountSpinBox.setValue(selectedArea.getVehicleCount());
            vehicleEmitIntervalSpinBox.setValue(selectedArea.getVehicleEmitInterval());
            loadVehicleTypesListWidget(selectedArea);
            simulationTimeLabel.setVisible(true);
            simulationTimeLabel.setText(LABEL_START_TIME);
            simulationTimeSlider.setVisible(true);
            simulationTimeSlider.setValue(selectedArea.getSimulationTime());
            simulationTimeValueLabel.setVisible(true);
            destinationAreasOfStartAreaLabel.setVisible(true);
            destinationAreasOfStartAreaListWidget.setVisible(true);
            loadDestinationAreasListWidget(selectedArea);
        } else if (selectedArea.getAreaType() == AreaType.DESTINATION) {
            if (numberOfStartAreas == 0) {
                showAreaInformation();
                vehicleCountSpinBox.setValue(selectedArea.getVehicleCount());
                vehicleEmitIntervalSpinBox.setValue(selectedArea.getVehicleEmitInterval());
                loadVehicleTypesListWidget(selectedArea);
            } else {
                hideAreaInformation();
            }
            if (numberOfStartAreas == 0) {
                simulationTimeLabel.setVisible(true);
                simulationTimeLabel.setText(LABEL_DESTINATION_TIME);
                simulationTimeSlider.setVisible(true);
                simulationTimeSlider.setValue(selectedArea.getSimulationTime());
                simulationTimeValueLabel.setVisible(true);
            } else {
                simulationTimeLabel.setVisible(false);
                simulationTimeSlider.setVisible(false);
                simulationTimeValueLabel.setVisible(false);
            }
            destinationAreasOfStartAreaLabel.setVisible(false);
        }
    }

    /**
	 * Hides all gui elements.
	 */
    private void hideInformation() {
        hideAreaInformation();
        hideVehicleInformation();
    }

    /**
	 * Enables area Information.
	 */
    private void showAreaInformation() {
        identifierLineEdit.setEnabled(true);
        simulationTimeSlider.setEnabled(true);
    }

    /**
	 * Disables Area Information
	 */
    private void hideAreaInformation() {
        identifierLineEdit.setText("<nothing selected>");
        identifierLineEdit.setEnabled(false);
        simulationTimeSlider.setEnabled(false);
    }

    /**
	 * Enables vehicle Information.
	 */
    private void showVehicleInformation() {
        vehicleCountSpinBox.setEnabled(true);
        vehicleEmitIntervalSpinBox.setEnabled(true);
    }

    /**
	 * Disables vehicle Information.
	 */
    private void hideVehicleInformation() {
        vehicleCountSpinBox.setEnabled(false);
        vehicleEmitIntervalSpinBox.setEnabled(false);
    }

    /**
	 * Loads the list widget with all potential destination areas for this start
	 * area. The icon (checkbox) in front of a destination area indicates
	 * whether it is a destination for this start area.
	 * 
	 * @param startArea
	 *            The start area whose potential destination areas should be
	 *            listed.
	 */
    private void loadDestinationAreasListWidget(Area startArea) {
        Collection<ModelElement> elements = ModelManager.getInstance().getAllModelElements();
        List<Area> allDestinationAreas = new ArrayList<Area>();
        for (ModelElement element : elements) {
            if (element instanceof Area) {
                Area area = (Area) element;
                if (area.getAreaType() == AreaType.DESTINATION) {
                    allDestinationAreas.add(area);
                }
            }
        }
        List<Area> destinationAreas = startArea.getDestinationAreas();
        if (destinationAreas == null) {
            destinationAreas = new ArrayList<Area>();
            startArea.setDestinationAreas(destinationAreas);
        }
        destinationAreasOfStartAreaListModel.clear();
        for (int i = 0; i < allDestinationAreas.size(); ++i) {
            Area destinationArea = allDestinationAreas.get(i);
            String destinationAreaId = destinationArea.getIdentifier();
            CheckListItem currentListItem = new CheckListItem(destinationAreaId);
            currentListItem.setData(destinationArea);
            currentListItem.setSelected(destinationAreas.contains(destinationArea));
            destinationAreasOfStartAreaListModel.add(0, currentListItem);
        }
    }

    /**
	 * Loads the list widget with all potential vehicle types for this area. The
	 * icon (checkbox) in front of a vehicle type indicates whether it is used
	 * for this area.
	 * 
	 * @param area
	 *            The area whose potential vehicle types should be listed.
	 */
    private void loadVehicleTypesListWidget(Area area) {
        ArrayList<SumoVehicleType> allVehicleTypes = SumoManager.getInstance().getVehicleTypes();
        List<String> vehicleTypes = area.getVehicleTypes();
        if (vehicleTypes == null) {
            vehicleTypes = new ArrayList<String>();
            area.setVehicleTypes(vehicleTypes);
        }
        vehicleTypesListModel.clear();
        for (SumoVehicleType sumoVehicleType : allVehicleTypes) {
            String vehicleType = sumoVehicleType.getId();
            CheckListItem currentListItem = new CheckListItem(vehicleType.toString());
            currentListItem.setSelected(vehicleTypes.contains(vehicleType));
            vehicleTypesListModel.add(0, currentListItem);
        }
    }

    /**
	 * Called, when a model element is added. Updates the counters and the state
	 * of the checkboxes.
	 * 
	 * @param modelElement
	 *            The model element which is added.
	 */
    @SuppressWarnings("unused")
    private void onModelElementAdded(ModelElement modelElement) {
        if (modelElement instanceof Area) {
            Area area = (Area) modelElement;
            if (area.getAreaType() == AreaType.START) {
                numberOfStartAreas++;
            } else if (area.getAreaType() == AreaType.DESTINATION) {
                numberOfDestinationAreas++;
            }
        }
    }

    /**
	 * Called, when a model element is removed. Updates the counters and the
	 * state of the checkboxes.
	 * 
	 * @param modelElement
	 *            The model element which is removed.
	 */
    @SuppressWarnings("unused")
    private void onModelElementRemoved(ModelElement modelElement) {
        if (modelElement instanceof Area) {
            Area area = (Area) modelElement;
            if (area.getAreaType() == AreaType.START) {
                numberOfStartAreas--;
            } else if (area.getAreaType() == AreaType.DESTINATION) {
                numberOfDestinationAreas--;
            }
        }
    }

    /**
	 * Called, when the simulation time range changed in the time line plugin.
	 */
    @SuppressWarnings("unused")
    private void onSimulationTimeRangeChanged() {
        ModelManager modelManager = ModelManager.getInstance();
        int simulationStartTime = modelManager.getSimulationStartTime();
        int simulationEndTime = modelManager.getSimulationEndTime();
        Collection<ModelElement> elements = modelManager.getAllModelElements();
        for (ModelElement element : elements) {
            if (element instanceof Area) {
                Area area = (Area) element;
                int actualSimulationTime = area.getSimulationTime();
                if (actualSimulationTime < simulationStartTime) {
                    if (area == selectedAreaItem.getAssociatedElement()) {
                        simulationTimeSlider.setValue(simulationStartTime);
                        simulationTimeValueLabel.setText(String.valueOf(simulationStartTime));
                    }
                    area.setSimulationTime(simulationStartTime);
                } else if (actualSimulationTime > simulationEndTime) {
                    if (area == selectedAreaItem.getAssociatedElement()) {
                        simulationTimeSlider.setValue(simulationEndTime);
                        simulationTimeValueLabel.setText(String.valueOf(simulationEndTime));
                    }
                    area.setSimulationTime(simulationEndTime);
                }
            }
        }
        simulationTimeSlider.setMinimum(simulationStartTime);
        simulationTimeSlider.setMaximum(simulationEndTime);
    }

    /**
	 * Looks up the given resource in the plugin resource folder and creates an
	 * icon using this resource.
	 * 
	 * @param resource
	 *            The path to a resource that should be used for creating an
	 *            icon.
	 * @return The created icon using the given resource.
	 */
    private Icon createIconFromLocalResource(String resource) {
        ClassLoader cl = getClass().getClassLoader();
        String iconUrl = cl.getResource(resource).toString();
        return null;
    }

    /**
	 * Shows or hides the dock widget depending on, whether the menu entry is
	 * checked or not.
	 * 
	 * @param checked
	 *            Indicates, whether the menu entry is checked or not.
	 */
    public void onViewAction(boolean checked) {
        if (checked) {
            this.show();
        } else {
            this.hide();
        }
    }
}
