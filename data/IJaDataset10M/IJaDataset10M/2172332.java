package de.hpi.eworld.scenarios;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import com.trolltech.qt.core.QUrl;
import com.trolltech.qt.core.Qt;
import com.trolltech.qt.gui.QCheckBox;
import com.trolltech.qt.gui.QCloseEvent;
import com.trolltech.qt.gui.QDockWidget;
import com.trolltech.qt.gui.QDragLeaveEvent;
import com.trolltech.qt.gui.QDragMoveEvent;
import com.trolltech.qt.gui.QGridLayout;
import com.trolltech.qt.gui.QIcon;
import com.trolltech.qt.gui.QLabel;
import com.trolltech.qt.gui.QLineEdit;
import com.trolltech.qt.gui.QListWidget;
import com.trolltech.qt.gui.QListWidgetItem;
import com.trolltech.qt.gui.QMainWindow;
import com.trolltech.qt.gui.QSlider;
import com.trolltech.qt.gui.QTabWidget;
import com.trolltech.qt.gui.QWidget;
import de.hpi.eworld.core.ModelManager;
import de.hpi.eworld.exporter.sumo.data.SumoManager;
import de.hpi.eworld.exporter.sumo.data.SumoVehicleType;
import de.hpi.eworld.model.db.data.Area;
import de.hpi.eworld.model.db.data.ModelElement;
import de.hpi.eworld.model.db.data.Area.AreaType;
import de.hpi.eworld.networkview.NetworkView;
import de.hpi.eworld.networkview.model.AnnotationMimeData;
import de.hpi.eworld.networkview.model.AreaItem;
import de.hpi.eworld.networkview.model.ViewItem;

/**
 * The class represents a dock widget which contains a tab widget. The tab widget is divided into the tabs <i>Creation</i>,
 * <i>General</i> and <i>Vehicles</i>. The tab <i>Creation</i> contains widgets for the creation of scenarios. The tab
 * <i>General</i> contains widgets for customizing general attributes of an area like the identifier or the simulation time. The
 * tab <i>Vehicles</i> contains widgets for customizing vehicle attributes of an area like the number of vehicles and the vehicle
 * types.
 * @author Gerald Toepper
 */
public class Scenarios extends QDockWidget {

    private static final String LABEL_DEFINEMENT_VEHICLES_IN_DESTINATION = "define vehicles in destination";

    private static final String LABEL_DEFINEMENT_DESTINATION_TIMES = "define rough destination times";

    private static final String LABEL_IDENTIFIER = "identifier for area";

    private static final String LABEL_START_TIME = "simulation time in start area";

    private static final String LABEL_DESTINATION_TIME = "simulation time in destination area";

    private static final String LABEL_DESTINATION_AREAS = "destination areas of this start area";

    private static final String LABEL_START_VEHICLE_COUNT = "number of emitted vehicles in start area";

    private static final String LABEL_DESTINATION_VEHICLE_COUNT = "number of assimilated vehicles in destination area";

    private static final String LABEL_VEHICLE_TYPES = "vehicle types";

    private static final String TOOLTIP_DEFINEMENT_VEHICLES_IN_DESTINATION = "Specify whether you want to define the number and types of vehicles in the start or destination area.<br>Tick this box if you want to specify it in the destination area. Default is the start area.";

    private static final String TOOLTIP_DEFINEMENT_DESTINATION_TIMES = "Specify whether you want to define the start time in the start area or the arrival time in the destination area.<br>Tick this box if you want to specify the arrival time. Default is the start time.";

    private static final String TOOLTIP_AREA_CREATION = "Drag an area and drop it on the map. Each underlying edge will be considered to emit (start area) or to assimilate (destination area) vehicles.<br>If no start area is defined on the map, the vehicles will be emitted anywhere on the map. If no destination area is defined on the map, the vehicles will be assimilated anywhere on the map.";

    private static final String TOOLTIP_DESTINATION_AREAS = "Specify which destination areas should be a destination for this start area.<br>Click on an item in the list in order to change the selection.";

    private static final String TOOLTIP_VEHICLE_TYPES = "Specify which vehicle types should be used for this area.<br>Click on an item in the list in order to change the selection. If no vehicle type is selected, the DefaultVehicle will be used.";

    private static final int UNCHECKED = 0;

    private static final int CHECKED = 1;

    /**
	 * Signal for the case, that the dock widget is closed by using its own close button.
	 */
    public Signal0 closed = new Signal0();

    /**
	 * Reference to the main window.
	 */
    private QMainWindow mainWindow;

    /**
	 * Currently selected item in the network view.
	 */
    private AreaItem selectedAreaItem;

    /**
	 * Indicates, whether the user defines the number of vehicles and the vehicle types in the destination area instead of the
	 * start area.
	 */
    private boolean defineVehiclesInDestination = false;

    /**
	 * Indicates, whether the user defines the destination time (when vehicles should arrive in an area) instead of the start time
	 * (when vehicles should start in an area).
	 */
    private boolean defineDestinationTimes = false;

    /**
	 * Counter for the number of start areas.
	 */
    private int numberOfStartAreas = 0;

    /**
	 * Counter for the number of destination areas.
	 */
    private int numberOfDestinationAreas = 0;

    /**
	 * Tab widget which contains all the widgets used for parameterizing an area.
	 */
    private QTabWidget tabWidget;

    private QCheckBox defineDestinationTimesCheckBox;

    private QCheckBox defineVehiclesInDestinationCheckBox;

    private QLabel identifierLabel;

    private QLineEdit identifierLineEdit;

    private QLabel simulationTimeLabel;

    private QSlider simulationTimeSlider;

    private QLabel simulationTimeValueLabel;

    private QLabel destinationAreasOfStartAreaLabel;

    private QListWidget destinationAreasOfStartAreaListWidget;

    private QLabel vehicleCountLabel;

    private QSlider vehicleCountSlider;

    private QLabel vehicleCountValueLabel;

    private QLabel vehicleTypesLabel;

    private QListWidget vehicleTypesListWidget;

    private QIcon uncheckedIcon;

    private QIcon checkedIcon;

    /**
	 * Constructor.
	 * @param parent The parent widget of this dock widget.
	 * @param networkView A reference to the network view.
	 * @param title The title of the plugin.
	 */
    public Scenarios(QWidget parent, NetworkView networkView, String title) {
        super(parent);
        setAllowedAreas(Qt.DockWidgetArea.RightDockWidgetArea);
        setWindowTitle(title);
        ModelManager modelManager = ModelManager.getInstance();
        modelManager.elementAdded.connect(this, "onModelElementAdded(ModelElement)");
        modelManager.elementRemoved.connect(this, "onModelElementRemoved(ModelElement)");
        modelManager.simulationTimeRangeChanged.connect(this, "onSimulationTimeRangeChanged()");
        uncheckedIcon = createIconFromLocalResource("unchecked.png");
        checkedIcon = createIconFromLocalResource("checked.png");
        tabWidget = new QTabWidget();
        tabWidget.setMinimumWidth(150);
        tabWidget.setMinimumHeight(50);
        tabWidget.addTab(createCreationWidget(), "Creation");
        tabWidget.addTab(createGeneralWidget(), "General");
        tabWidget.addTab(createVehiclesWidget(), "Vehicles");
        tabWidget.setTabEnabled(1, false);
        tabWidget.setTabEnabled(2, false);
        this.setWidget(tabWidget);
    }

    /**
	 * Creates and returns a widget containing the areas, which can be dragged and dropped on the map.
	 * @return The widget containing the child widgets, that are necessary for the creation of scenarios.
	 */
    private QWidget createCreationWidget() {
        QWidget widget = new QWidget();
        QGridLayout layout = new QGridLayout();
        widget.setLayout(layout);
        defineDestinationTimesCheckBox = new QCheckBox(LABEL_DEFINEMENT_DESTINATION_TIMES);
        defineDestinationTimesCheckBox.setEnabled(false);
        defineDestinationTimesCheckBox.setToolTip(TOOLTIP_DEFINEMENT_DESTINATION_TIMES);
        defineDestinationTimesCheckBox.toggled.connect(this, "changeDefinementOfSimulationTime()");
        defineVehiclesInDestinationCheckBox = new QCheckBox(LABEL_DEFINEMENT_VEHICLES_IN_DESTINATION);
        defineVehiclesInDestinationCheckBox.setEnabled(false);
        defineVehiclesInDestinationCheckBox.setToolTip(TOOLTIP_DEFINEMENT_VEHICLES_IN_DESTINATION);
        defineVehiclesInDestinationCheckBox.toggled.connect(this, "changeDefinementOfVehicles()");
        AreaWidget areaWidget = new AreaWidget();
        areaWidget.setToolTip(TOOLTIP_AREA_CREATION);
        layout.addWidget(defineDestinationTimesCheckBox, 0, 0, 1, 2);
        layout.addWidget(defineVehiclesInDestinationCheckBox, 1, 0, 1, 2);
        layout.addWidget(areaWidget, 2, 0, 1, 2);
        return widget;
    }

    /**
	 * Creates and returns a widget containing the general information of this area.
	 * @return The widget containing the child widgets for setting general attributes of an area like the identifier or the
	 *         simulation time.
	 */
    private QWidget createGeneralWidget() {
        QWidget widget = new QWidget();
        QGridLayout layout = new QGridLayout();
        widget.setLayout(layout);
        identifierLabel = new QLabel(LABEL_IDENTIFIER);
        identifierLineEdit = new QLineEdit();
        identifierLineEdit.textChanged.connect(this, "saveIdentifier()");
        simulationTimeLabel = new QLabel(LABEL_START_TIME);
        simulationTimeSlider = new QSlider(Qt.Orientation.Horizontal);
        ModelManager modelManager = ModelManager.getInstance();
        simulationTimeSlider.setMinimum(modelManager.getSimulationStartTime());
        simulationTimeSlider.setMaximum(modelManager.getSimulationEndTime());
        simulationTimeSlider.setValue(Area.DEFAULT_SIMULATION_TIME);
        simulationTimeSlider.valueChanged.connect(this, "saveSimulationTimeValue()");
        simulationTimeValueLabel = new QLabel(String.valueOf(Area.DEFAULT_SIMULATION_TIME));
        destinationAreasOfStartAreaLabel = new QLabel(LABEL_DESTINATION_AREAS);
        destinationAreasOfStartAreaLabel.setToolTip(TOOLTIP_DESTINATION_AREAS);
        destinationAreasOfStartAreaListWidget = new QListWidget();
        destinationAreasOfStartAreaListWidget.setToolTip(TOOLTIP_DESTINATION_AREAS);
        destinationAreasOfStartAreaListWidget.itemClicked.connect(this, "destinationAreaClicked(QListWidgetItem)");
        layout.setAlignment(new Qt.Alignment(Qt.AlignmentFlag.AlignTop));
        layout.addWidget(identifierLabel, 5, 0, 1, 2);
        layout.addWidget(identifierLineEdit, 6, 0, 1, 2);
        layout.addWidget(simulationTimeLabel, 9, 0, 1, 2);
        layout.addWidget(simulationTimeSlider, 10, 0, 1, 1);
        layout.addWidget(simulationTimeValueLabel, 10, 1, 1, 1);
        layout.addWidget(destinationAreasOfStartAreaLabel, 11, 0, 1, 2);
        layout.addWidget(destinationAreasOfStartAreaListWidget, 12, 0, 1, 2);
        return widget;
    }

    /**
	 * Creates an returns a widget containing the vehicle information of this area.
	 * @return The widget containing the child widgets for setting vehicle attributes on an area.
	 */
    private QWidget createVehiclesWidget() {
        QWidget widget = new QWidget();
        QGridLayout layout = new QGridLayout();
        widget.setLayout(layout);
        vehicleCountLabel = new QLabel(LABEL_START_VEHICLE_COUNT);
        vehicleCountLabel.setVisible(true);
        vehicleCountSlider = new QSlider(Qt.Orientation.Horizontal);
        vehicleCountSlider.setMinimum(1);
        vehicleCountSlider.setMaximum(100);
        vehicleCountSlider.setValue(Area.DEFAULT_NUMBER_OF_VEHICLES);
        vehicleCountSlider.valueChanged.connect(this, "saveVehicleCountValue()");
        vehicleCountValueLabel = new QLabel(String.valueOf(Area.DEFAULT_NUMBER_OF_VEHICLES));
        vehicleTypesLabel = new QLabel(LABEL_VEHICLE_TYPES);
        vehicleTypesLabel.setToolTip(TOOLTIP_VEHICLE_TYPES);
        vehicleTypesListWidget = new QListWidget();
        vehicleTypesListWidget.setToolTip(TOOLTIP_VEHICLE_TYPES);
        vehicleTypesListWidget.itemClicked.connect(this, "vehicleTypeClicked(QListWidgetItem)");
        layout.addWidget(vehicleCountLabel, 0, 0, 1, 2);
        layout.addWidget(vehicleCountSlider, 1, 0, 1, 1);
        layout.addWidget(vehicleCountValueLabel, 1, 1, 1, 1);
        layout.addWidget(vehicleTypesLabel, 2, 0, 1, 2);
        layout.addWidget(vehicleTypesListWidget, 3, 0, 1, 2);
        return widget;
    }

    /**
	 * Initializes the scenarios dock widget.
	 * @param mainWindow The reference to the main window.
	 */
    public void startUp(QMainWindow mainWindow) {
        this.mainWindow = mainWindow;
        this.mainWindow.addDockWidget(Qt.DockWidgetArea.RightDockWidgetArea, this);
    }

    /**
	 * Registers changes, whether the user defines the number of vehicles in the start or destination area.
	 */
    @SuppressWarnings("unused")
    private void changeDefinementOfVehicles() {
        defineVehiclesInDestination = defineVehiclesInDestinationCheckBox.isChecked();
        if (defineVehiclesInDestination) {
            vehicleCountLabel.setText(LABEL_DESTINATION_VEHICLE_COUNT);
        } else {
            vehicleCountLabel.setText(LABEL_START_VEHICLE_COUNT);
        }
        ModelManager modelManager = ModelManager.getInstance();
        Collection<ModelElement> elements = modelManager.getAllModelElements();
        for (ModelElement element : elements) {
            if (element instanceof Area) {
                ((Area) element).setDefineVehiclesInDestination(defineVehiclesInDestination);
            }
        }
        if (selectedAreaItem != null) {
            showInformation(selectedAreaItem);
        }
    }

    /**
	 * Registers changes, whether the user defines a destination time instead of a start time.
	 */
    @SuppressWarnings("unused")
    private void changeDefinementOfSimulationTime() {
        defineDestinationTimes = defineDestinationTimesCheckBox.isChecked();
        if (defineDestinationTimes) {
            simulationTimeLabel.setText(LABEL_DESTINATION_TIME);
        } else {
            simulationTimeLabel.setText(LABEL_START_TIME);
        }
        ModelManager modelManager = ModelManager.getInstance();
        Collection<ModelElement> elements = modelManager.getAllModelElements();
        for (ModelElement element : elements) {
            if (element instanceof Area) {
                ((Area) element).setDefineDestinationTimes(defineDestinationTimes);
            }
        }
        if (selectedAreaItem != null) {
            showInformation(selectedAreaItem);
        }
    }

    /**
	 * Registers changes concerning the identifier of the selected area.
	 */
    @SuppressWarnings("unused")
    private void saveIdentifier() {
        if (selectedAreaItem != null) {
            Area area = (Area) selectedAreaItem.getAssociatedElement();
            area.setIdentifier(identifierLineEdit.text());
        }
    }

    /**
	 * Saves the vehicle count value, in case it changed.
	 */
    @SuppressWarnings("unused")
    private void saveVehicleCountValue() {
        if (selectedAreaItem != null) {
            Area area = (Area) selectedAreaItem.getAssociatedElement();
            vehicleCountValueLabel.setText(String.valueOf(vehicleCountSlider.value()));
            area.setNumberOfVehicles(vehicleCountSlider.value());
        }
    }

    /**
	 * Saves the simulation time value, in case it changed.
	 */
    @SuppressWarnings("unused")
    private void saveSimulationTimeValue() {
        if (selectedAreaItem != null) {
            Area area = (Area) selectedAreaItem.getAssociatedElement();
            simulationTimeValueLabel.setText(String.valueOf(simulationTimeSlider.value()));
            area.setSimulationTime(simulationTimeSlider.value());
        }
    }

    /**
	 * Called, in case the user clicks an area on the map.
	 * @param item The view item, that has been clicked.
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
	 * @param item The area item whose information should be shown.
	 */
    private void showInformation(AreaItem areaItem) {
        Area selectedArea = (Area) areaItem.getAssociatedElement();
        selectedArea.setDefineVehiclesInDestination(defineVehiclesInDestination);
        selectedArea.setDefineDestinationTimes(defineDestinationTimes);
        defineVehiclesInDestinationCheckBox.setChecked(defineVehiclesInDestination);
        defineDestinationTimesCheckBox.setChecked(defineDestinationTimes);
        AreaType areaType = selectedArea.getAreaType();
        tabWidget.setTabEnabled(1, true);
        identifierLineEdit.setText(selectedArea.getIdentifier());
        if (areaType == AreaType.START) {
            if (defineVehiclesInDestination) {
                tabWidget.setTabEnabled(2, false);
            } else {
                tabWidget.setTabEnabled(2, true);
                vehicleCountSlider.setValue(selectedArea.getNumberOfVehicles());
                loadVehicleTypesListWidget(selectedArea);
            }
            if (defineDestinationTimes) {
                simulationTimeLabel.setVisible(false);
                simulationTimeSlider.setVisible(false);
                simulationTimeValueLabel.setVisible(false);
            } else {
                simulationTimeLabel.setVisible(true);
                simulationTimeSlider.setVisible(true);
                simulationTimeSlider.setValue(selectedArea.getSimulationTime());
                simulationTimeValueLabel.setVisible(true);
            }
            destinationAreasOfStartAreaLabel.setVisible(true);
            destinationAreasOfStartAreaListWidget.setVisible(true);
            loadDestinationAreasListWidget(selectedArea);
        } else if (selectedArea.getAreaType() == AreaType.DESTINATION) {
            if (defineVehiclesInDestination) {
                tabWidget.setTabEnabled(2, true);
                vehicleCountSlider.setValue(selectedArea.getNumberOfVehicles());
                loadVehicleTypesListWidget(selectedArea);
            } else {
                tabWidget.setTabEnabled(2, false);
            }
            if (defineDestinationTimes) {
                simulationTimeLabel.setVisible(true);
                simulationTimeSlider.setVisible(true);
                simulationTimeSlider.setValue(selectedArea.getSimulationTime());
                simulationTimeValueLabel.setVisible(true);
            } else {
                simulationTimeLabel.setVisible(false);
                simulationTimeSlider.setVisible(false);
                simulationTimeValueLabel.setVisible(false);
            }
            destinationAreasOfStartAreaLabel.setVisible(false);
            destinationAreasOfStartAreaListWidget.setVisible(false);
        }
    }

    /**
	 * Hides all gui elements.
	 */
    private void hideInformation() {
        tabWidget.setTabEnabled(1, false);
        tabWidget.setTabEnabled(2, false);
    }

    /**
	 * Loads the list widget with all potential destination areas for this start area. The icon (checkbox) in front of a
	 * destination area indicates whether it is a destination for this start area.
	 * @param startArea The start area whose potential destination areas should be listed.
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
        destinationAreasOfStartAreaListWidget.clear();
        List<Area> destinationAreas = startArea.getDestinationAreas();
        if (destinationAreas == null) {
            destinationAreas = new ArrayList<Area>();
            startArea.setDestinationAreas(destinationAreas);
        }
        for (Area destinationArea : allDestinationAreas) {
            QListWidgetItem itemToAdd = new QListWidgetItem(destinationArea.getIdentifier());
            itemToAdd.setData(Qt.ItemDataRole.UserRole, destinationArea);
            if (destinationAreas.contains(destinationArea)) {
                itemToAdd.setData(Qt.ItemDataRole.UserRole + 1, CHECKED);
                itemToAdd.setIcon(checkedIcon);
            } else {
                itemToAdd.setData(Qt.ItemDataRole.UserRole + 1, UNCHECKED);
                itemToAdd.setIcon(uncheckedIcon);
            }
            destinationAreasOfStartAreaListWidget.addItem(itemToAdd);
        }
        destinationAreasOfStartAreaListWidget.sortItems();
    }

    /**
	 * Loads the list widget with all potential vehicle types for this area. The icon (checkbox) in front of a vehicle type
	 * indicates whether it is used for this area.
	 * @param area The area whose potential vehicle types should be listed.
	 */
    private void loadVehicleTypesListWidget(Area area) {
        ArrayList<SumoVehicleType> allVehicleTypes = SumoManager.getInstance().getVehicleTypes();
        vehicleTypesListWidget.clear();
        List<String> vehicleTypes = area.getVehicleTypes();
        if (vehicleTypes == null) {
            vehicleTypes = new ArrayList<String>();
            area.setVehicleTypes(vehicleTypes);
        }
        for (SumoVehicleType sumoVehicleType : allVehicleTypes) {
            String vehicleType = sumoVehicleType.getId();
            QListWidgetItem itemToAdd = new QListWidgetItem(vehicleType);
            itemToAdd.setData(Qt.ItemDataRole.UserRole, vehicleType);
            if (vehicleTypes.contains(vehicleType)) {
                itemToAdd.setData(Qt.ItemDataRole.UserRole + 1, CHECKED);
                itemToAdd.setIcon(checkedIcon);
            } else {
                itemToAdd.setData(Qt.ItemDataRole.UserRole + 1, UNCHECKED);
                itemToAdd.setIcon(uncheckedIcon);
            }
            vehicleTypesListWidget.addItem(itemToAdd);
        }
        vehicleTypesListWidget.sortItems();
    }

    /**
	 * Called, when a destination area is clicked in the list widget.
	 * @param item The item that is clicked in the list widget.
	 */
    @SuppressWarnings("unused")
    private void destinationAreaClicked(QListWidgetItem item) {
        Area area = (Area) selectedAreaItem.getAssociatedElement();
        if (item.data(Qt.ItemDataRole.UserRole + 1).equals(UNCHECKED)) {
            area.addDestinationArea((Area) item.data(Qt.ItemDataRole.UserRole));
            item.setData(Qt.ItemDataRole.UserRole + 1, CHECKED);
            item.setIcon(checkedIcon);
        } else if (item.data(Qt.ItemDataRole.UserRole + 1).equals(CHECKED)) {
            area.removeDestinationArea((Area) item.data(Qt.ItemDataRole.UserRole));
            item.setData(Qt.ItemDataRole.UserRole + 1, UNCHECKED);
            item.setIcon(uncheckedIcon);
        }
    }

    /**
	 * Called, when a vehicle type item is clicked in the list widget.
	 * @param item The item that is clicked in the list widget.
	 */
    @SuppressWarnings("unused")
    private void vehicleTypeClicked(QListWidgetItem item) {
        Area area = (Area) selectedAreaItem.getAssociatedElement();
        if (item.data(Qt.ItemDataRole.UserRole + 1).equals(UNCHECKED)) {
            area.addVehicleType((String) item.data(Qt.ItemDataRole.UserRole));
            item.setData(Qt.ItemDataRole.UserRole + 1, CHECKED);
            item.setIcon(checkedIcon);
        } else if (item.data(Qt.ItemDataRole.UserRole + 1).equals(CHECKED)) {
            area.removeVehicleType((String) item.data(Qt.ItemDataRole.UserRole));
            item.setData(Qt.ItemDataRole.UserRole + 1, UNCHECKED);
            item.setIcon(uncheckedIcon);
        }
    }

    /**
	 * Called, when a model element is added. Updates the counters and the state of the checkboxes.
	 * @param modelElement The model element which is added.
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
            updateCheckboxes();
        }
    }

    /**
	 * Called, when a model element is removed. Updates the counters and the state of the checkboxes.
	 * @param modelElement The model element which is removed.
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
            tabWidget.setCurrentIndex(0);
            updateCheckboxes();
        }
    }

    /**
	 * Updates the checkboxes in the first tab in the dock widget.
	 */
    private void updateCheckboxes() {
        if (numberOfStartAreas > 0 && numberOfDestinationAreas > 0) {
            defineDestinationTimesCheckBox.setEnabled(true);
            defineVehiclesInDestinationCheckBox.setEnabled(true);
        } else if (numberOfDestinationAreas == 0) {
            defineDestinationTimesCheckBox.setChecked(false);
            defineDestinationTimesCheckBox.setEnabled(false);
            defineVehiclesInDestinationCheckBox.setChecked(false);
            defineVehiclesInDestinationCheckBox.setEnabled(false);
        } else if (numberOfStartAreas == 0) {
            defineDestinationTimesCheckBox.setChecked(true);
            defineDestinationTimesCheckBox.setEnabled(false);
            defineVehiclesInDestinationCheckBox.setChecked(true);
            defineVehiclesInDestinationCheckBox.setEnabled(false);
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
	 * Looks up the given resource in the plugin resource folder and creates an icon using this resource.
	 * @param resource The path to a resource that should be used for creating an icon.
	 * @return The created icon using the given resource.
	 */
    private QIcon createIconFromLocalResource(String resource) {
        ClassLoader cl = getClass().getClassLoader();
        String iconUrl = cl.getResource(resource).toString();
        QUrl qIconUrl = new QUrl(iconUrl);
        return new QIcon(qIconUrl.toLocalFile());
    }

    /**
	 * Shows or hides the dock widget depending on, whether the menu entry is checked or not.
	 * @param checked Indicates, whether the menu entry is checked or not.
	 */
    public void onViewAction(boolean checked) {
        if (checked) {
            this.show();
        } else {
            this.hide();
        }
    }

    /**
	 * Called, when a drag moves within the widget. This drag is only allowed, if it has the correct mime type.
	 * @see com.trolltech.qt.gui.QWidget#dragMoveEvent(com.trolltech.qt.gui.QDragMoveEvent)
	 */
    @Override
    protected void dragMoveEvent(QDragMoveEvent event) {
        if (event.mimeData().hasFormat(AnnotationMimeData.MimeType)) {
            event.setDropAction(Qt.DropAction.MoveAction);
            event.accept();
        } else {
            event.ignore();
        }
    }

    /**
	 * Called, when a drag leaves the widget.
	 * @see com.trolltech.qt.gui.QWidget#dragLeaveEvent(com.trolltech.qt.gui.QDragLeaveEvent)
	 */
    @Override
    protected void dragLeaveEvent(QDragLeaveEvent event) {
        event.accept();
    }

    /**
	 * Called, when user clicks close button the dockwidgets close button.
	 * @see com.trolltech.qt.gui.QDockWidget#closeEvent(com.trolltech.qt.gui.QCloseEvent)
	 */
    @Override
    protected void closeEvent(QCloseEvent closeEvent) {
        super.closeEvent(closeEvent);
        closed.emit();
    }
}
