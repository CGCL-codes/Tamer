package de.hpi.eworld.extensions.tls;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.trolltech.qt.core.QPointF;
import com.trolltech.qt.core.Qt.AlignmentFlag;
import com.trolltech.qt.core.Qt.ItemFlag;
import com.trolltech.qt.core.Qt.PenStyle;
import com.trolltech.qt.gui.QAction;
import com.trolltech.qt.gui.QBrush;
import com.trolltech.qt.gui.QCloseEvent;
import com.trolltech.qt.gui.QColor;
import com.trolltech.qt.gui.QDialog;
import com.trolltech.qt.gui.QFont;
import com.trolltech.qt.gui.QFrame;
import com.trolltech.qt.gui.QGraphicsItemInterface;
import com.trolltech.qt.gui.QGraphicsScene;
import com.trolltech.qt.gui.QHBoxLayout;
import com.trolltech.qt.gui.QIcon;
import com.trolltech.qt.gui.QLabel;
import com.trolltech.qt.gui.QListView;
import com.trolltech.qt.gui.QListWidget;
import com.trolltech.qt.gui.QListWidgetItem;
import com.trolltech.qt.gui.QMenu;
import com.trolltech.qt.gui.QPalette;
import com.trolltech.qt.gui.QPushButton;
import com.trolltech.qt.gui.QTableWidget;
import com.trolltech.qt.gui.QTableWidgetItem;
import com.trolltech.qt.gui.QVBoxLayout;
import com.trolltech.qt.gui.QWidget;
import com.trolltech.qt.gui.QFrame.Shape;
import de.hpi.eworld.gui.eventlisteners.EventListener;
import de.hpi.eworld.gui.eventlisteners.MainApp;
import de.hpi.eworld.gui.model.DescriptionCircleItem;
import de.hpi.eworld.gui.model.EdgeItem;
import de.hpi.eworld.gui.model.NodeItem;
import de.hpi.eworld.gui.model.TrafficLightItem;
import de.hpi.eworld.model.db.InternalDB;
import de.hpi.eworld.model.db.data.Edge;
import de.hpi.eworld.model.db.data.Node;
import de.hpi.eworld.model.db.data.TrafficLight;
import de.hpi.eworld.model.db.data.TrafficLightStateList;
import de.hpi.eworld.model.db.data.TrafficLightStateListEntry;
import de.hpi.eworld.model.db.data.TrafficLight.TrafficLightState;
import de.hpi.eworld.util.Projection;
import de.hpi.eworld.util.ProjectionFactory;

/**
 * This class encapsulate the traffic light menu within eWorld.
 * In this menu the user can change the traffic light logics.
 * 
 * 
 * @author Martin Wolf
 *
 */
public class TLSMenu extends QDialog implements EventListener {

    private final String windowTitle = "Traffic Light Menu";

    private InternalDB db;

    private QListWidget contentWidget;

    private TLSView previewWidget;

    private QTableWidget logicTable;

    private QTableWidget detailsTable;

    private static final String[] DETAILSHEADERS = { "Id", "Description" };

    private QGraphicsScene previewModel;

    private TrafficLight currentTrafficLight = null;

    private List<Edge> currentIncomingEdges = null;

    private TrafficLight tlToEdit = null;

    private boolean itemChanged;

    QAction editAllTLL;

    QAction editOneTLL;

    /**
	 * Cache for storing changes made by the user. They are later written
	 * to the internal database.
	 */
    private HashMap<TrafficLight, Map<Edge, TrafficLightStateList>> tllCache = null;

    private HashMap<TrafficLight, int[]> durationsCache = null;

    private List<TrafficLight> trafficLights = new ArrayList<TrafficLight>();

    /**
	 * The constructor of this dialog creates the dialog window and
	 * all its elements. Furthermore the menu entry in the parent
	 * widget is created.
	 * 
	 * @author Martin Wolf
	 * @param parent Parent widget for this dialog
	 */
    public TLSMenu(QWidget parent) {
        super(parent);
        setWindowTitle(windowTitle);
        setWindowIcon(new QIcon("classpath:images/traffic-light.png"));
        setModal(true);
        resize(640, 480);
        QFont headlineFont = new QFont();
        headlineFont.setBold(true);
        headlineFont.setPointSize(10);
        QLabel contentWidgetLabel = new QLabel("<font color = white>Traffic Lights</font>");
        contentWidgetLabel.setFont(headlineFont);
        contentWidgetLabel.setPalette(new QPalette(new QColor(127, 157, 185)));
        contentWidgetLabel.setAutoFillBackground(true);
        contentWidgetLabel.setFrameShape(Shape.StyledPanel);
        contentWidgetLabel.setAlignment(AlignmentFlag.AlignCenter);
        contentWidgetLabel.setFixedSize(150, 20);
        contentWidget = new QListWidget(this);
        contentWidget.setViewMode(QListView.ViewMode.ListMode);
        contentWidget.currentItemChanged.connect(this, "showSelectedItem(QListWidgetItem, QListWidgetItem)");
        contentWidget.setFixedSize(150, 150);
        contentWidget.setAlternatingRowColors(true);
        QVBoxLayout content = new QVBoxLayout();
        content.setSpacing(0);
        content.addWidget(contentWidgetLabel);
        content.addWidget(contentWidget);
        QLabel previewWidgetLabel = new QLabel("<font color = white>Preview</font>");
        previewWidgetLabel.setFont(headlineFont);
        previewWidgetLabel.setPalette(new QPalette(new QColor(127, 157, 185)));
        previewWidgetLabel.setAutoFillBackground(true);
        previewWidgetLabel.setFrameShape(Shape.StyledPanel);
        previewWidgetLabel.setAlignment(AlignmentFlag.AlignCenter);
        previewWidgetLabel.setFixedSize(150, 20);
        previewModel = new QGraphicsScene();
        previewWidget = new TLSView(previewModel);
        previewWidget.setFixedSize(150, 150);
        previewWidget.scale(8, 8);
        QVBoxLayout preview = new QVBoxLayout();
        preview.setSpacing(0);
        preview.addWidget(previewWidgetLabel);
        preview.addWidget(previewWidget);
        QLabel detailsWidgetLabel = new QLabel("<font color = white>Details</font>");
        detailsWidgetLabel.setFont(headlineFont);
        detailsWidgetLabel.setPalette(new QPalette(new QColor(127, 157, 185)));
        detailsWidgetLabel.setAutoFillBackground(true);
        detailsWidgetLabel.setFrameShape(Shape.StyledPanel);
        detailsWidgetLabel.setAlignment(AlignmentFlag.AlignCenter);
        detailsWidgetLabel.setMinimumWidth(150);
        detailsWidgetLabel.setFixedHeight(20);
        detailsTable = new QTableWidget(this);
        detailsTable.setMinimumWidth(150);
        detailsTable.setFixedHeight(150);
        detailsTable.setGridStyle(PenStyle.NoPen);
        List<String> horizontalHeaders = new ArrayList<String>();
        for (int i = 0; i < DETAILSHEADERS.length; i++) {
            detailsTable.insertColumn(i);
            horizontalHeaders.add(DETAILSHEADERS[i]);
        }
        detailsTable.setHorizontalHeaderLabels(horizontalHeaders);
        QVBoxLayout details = new QVBoxLayout();
        details.setSpacing(0);
        details.addWidget(detailsWidgetLabel);
        details.addWidget(detailsTable);
        QHBoxLayout firstRow = new QHBoxLayout();
        firstRow.addLayout(content);
        firstRow.addLayout(details);
        firstRow.addLayout(preview);
        QPushButton greenButton = new QPushButton(tr("Green"));
        greenButton.clicked.connect(this, "changeToGreen()");
        QPushButton yellowButton = new QPushButton(tr("Yellow"));
        yellowButton.clicked.connect(this, "changeToYellow()");
        QPushButton redButton = new QPushButton(tr("Red"));
        redButton.clicked.connect(this, "changeToRed()");
        QVBoxLayout colorButtonLayout = new QVBoxLayout();
        colorButtonLayout.addStretch(1);
        colorButtonLayout.addWidget(greenButton);
        colorButtonLayout.addWidget(yellowButton);
        colorButtonLayout.addWidget(redButton);
        QLabel xLabel = new QLabel("Phases (duration in seconds)");
        xLabel.setFixedHeight(15);
        xLabel.setAlignment(AlignmentFlag.AlignHCenter, AlignmentFlag.AlignBottom);
        TLSLabel yLabel = new TLSLabel("Incoming Edges");
        yLabel.setRotation(270);
        yLabel.setFixedWidth(15);
        logicTable = new QTableWidget();
        logicTable.setHorizontalHeader(new TLSTableHeader(this));
        logicTable.setMinimumHeight(100);
        QHBoxLayout tableMainLayout = new QHBoxLayout();
        tableMainLayout.setSpacing(0);
        tableMainLayout.addWidget(yLabel);
        tableMainLayout.addWidget(logicTable);
        QHBoxLayout xTableLayout = new QHBoxLayout();
        xTableLayout.addSpacing(15);
        xTableLayout.addWidget(xLabel);
        QVBoxLayout tableLayout = new QVBoxLayout();
        tableLayout.setSpacing(0);
        tableLayout.addLayout(xTableLayout);
        tableLayout.addLayout(tableMainLayout);
        QLabel tableWidgetLabel = new QLabel("<font color = white>Logic</font>");
        tableWidgetLabel.setPalette(new QPalette(new QColor(127, 157, 185)));
        tableWidgetLabel.setAutoFillBackground(true);
        tableWidgetLabel.setFont(headlineFont);
        tableWidgetLabel.setAlignment(AlignmentFlag.AlignCenter);
        QHBoxLayout secondRow = new QHBoxLayout();
        secondRow.addLayout(tableLayout);
        secondRow.addLayout(colorButtonLayout);
        QPushButton saveButton = new QPushButton(tr("&Save"));
        saveButton.clicked.connect(this, "saveAllChanges()");
        QPushButton cancelButton = new QPushButton(tr("&Cancel"));
        cancelButton.clicked.connect(this, "close()");
        QHBoxLayout thirdRow = new QHBoxLayout();
        thirdRow.addStretch(1);
        thirdRow.addWidget(saveButton);
        thirdRow.addWidget(cancelButton);
        QLabel horizontalLine = new QLabel();
        horizontalLine.setFrameShadow(QFrame.Shadow.Sunken);
        horizontalLine.setFrameShape(QFrame.Shape.HLine);
        QVBoxLayout mainLayout = new QVBoxLayout();
        mainLayout.addLayout(firstRow);
        mainLayout.addWidget(tableWidgetLabel);
        mainLayout.addLayout(secondRow);
        mainLayout.addWidget(horizontalLine);
        mainLayout.addLayout(thirdRow);
        setLayout(mainLayout);
        MainApp app = (MainApp) parent;
        QMenu editMenu = app.addMenu(tr("&Edit"));
        editAllTLL = new QAction(tr("Edit all Traffic Light Logics"), parent);
        editAllTLL.setStatusTip(tr("Edit all Traffic Light Logics"));
        editAllTLL.triggered.connect(this, "editAllTLL()");
        editAllTLL.setEnabled(false);
        editOneTLL = new QAction(tr("Edit this Traffic Light Logic"), parent);
        editOneTLL.setStatusTip(tr("Edit this Traffic Light Logic"));
        editOneTLL.triggered.connect(this, "editOneTLL()");
        editMenu.addAction(editAllTLL);
    }

    /**
	 * Handle passed events.
	 * 
	 * - Load Events --> Initialize Caches
	 * - Hover Event --> Set hovered item as current traffic light 
	 * 
	 */
    public void eventTriggered(EventType type, Map<String, Object> data) {
        if (type == EventType.LoadEvent) {
            if ((data.get("loaded").toString().equals("true"))) {
                initialize();
                if (tllCache.size() > 0) {
                    editAllTLL.setEnabled(true);
                }
            } else {
                editAllTLL.setEnabled(false);
            }
        }
        if (type == EventType.ContextEvent) {
            List<?> items = (List<?>) data.get("items");
            for (int i = 0; i < items.size(); i++) {
                if (items.get(i) instanceof TrafficLightItem) {
                    QMenu menu = (QMenu) data.get("menu");
                    menu.addAction(editOneTLL);
                    this.tlToEdit = (TrafficLight) ((TrafficLightItem) items.get(i)).getAssociatedElement();
                }
            }
        }
    }

    /**
	 * Open the menu with all traffic lights in the loaded map.
	 */
    private void editAllTLL() {
        reset();
        for (TrafficLight tl : tllCache.keySet()) {
            contentWidget.addItem(tl.getPointerId());
        }
        showSelectedItem(contentWidget.item(0), null);
        show();
    }

    /**
	 * Open the menu with only the hovered item as traffic light.
	 */
    private void editOneTLL() {
        reset();
        contentWidget.addItem(tlToEdit.getPointerId());
        showSelectedItem(contentWidget.item(0), null);
        show();
    }

    /**
	 * Resets the tls edit dialog.
	 */
    public void reset() {
        while (logicTable.rowCount() > 0) {
            logicTable.removeRow(logicTable.rowCount() - 1);
        }
        while (logicTable.columnCount() > 0) {
            logicTable.removeColumn(logicTable.columnCount() - 1);
        }
        for (QGraphicsItemInterface item : previewModel.items()) {
            previewModel.removeItem(item);
        }
        contentWidget.clear();
    }

    /**
	 * This method is called in order to fill the dialog with
	 * content. All traffic lights in the current map are read
	 * from the internal database and visualized.
	 * Furthermore the original traffic light logics are written to a
	 * cache in order to let the user change them.
	 * 
	 * @author Martin Wolf
	 */
    private void initialize() {
        db = InternalDB.getInstance();
        itemChanged = false;
        tllCache = new HashMap<TrafficLight, Map<Edge, TrafficLightStateList>>();
        durationsCache = new HashMap<TrafficLight, int[]>();
        int hasMoreThanOneIncomingEdge;
        for (Node node : db.getNodes()) {
            if (node.getClass().equals(TrafficLight.class)) {
                TrafficLight tl = (TrafficLight) node;
                List<Edge> edges = node.getUsedBy();
                hasMoreThanOneIncomingEdge = 0;
                for (Edge edge : edges) {
                    if (tl == edge.getToNode()) {
                        hasMoreThanOneIncomingEdge++;
                    }
                }
                if (hasMoreThanOneIncomingEdge > 1) {
                    tllCache.put(tl, tl.getDefaultLogic());
                    durationsCache.put(tl, tl.getDurations());
                    trafficLights.add(tl);
                }
            }
        }
    }

    /**
	 * This slot method is called once the selection of a node
	 * is changed. The new node to show is searched in the database
	 * and visualized by calling buildPreview() and fillTable(). 
	 * 
	 * @author Martin Wolf
	 * @param newNode node that should shown in the dialog 
	 * @param oldNode
	 */
    protected void showSelectedItem(QListWidgetItem newNode, QListWidgetItem oldNode) {
        if (newNode != null) {
            if (currentTrafficLight != null && itemChanged) {
                saveTLS();
            }
            currentTrafficLight = null;
            for (Node node : db.getNodes()) {
                if (node.getPointerId().equals(newNode.text())) {
                    currentTrafficLight = (TrafficLight) node;
                    currentIncomingEdges = new ArrayList<Edge>();
                    for (Edge edge : tllCache.get(currentTrafficLight).keySet()) {
                        currentIncomingEdges.add(edge);
                    }
                }
            }
            if (currentTrafficLight != null) {
                buildPreview();
                fillLogicTable();
                fillDetailsTable();
            }
        }
    }

    /**
	 * Set the selected table items to green. Later this change will
	 * be made persistent and written to the traffic lights' logic.
	 * 
	 * @author Martin Wolf
	 */
    protected void changeToGreen() {
        for (QTableWidgetItem item : logicTable.selectedItems()) {
            item.setBackground(new QBrush(QColor.green));
            itemChanged = true;
            logicTable.clearSelection();
        }
    }

    /**
	 * Set the selected table items to yellow. Later this change will
	 * be made persistent and written to the traffic lights' logic.
	 * 
	 * @author Martin Wolf
	 */
    protected void changeToYellow() {
        for (QTableWidgetItem item : logicTable.selectedItems()) {
            item.setBackground(new QBrush(QColor.yellow));
            itemChanged = true;
            logicTable.clearSelection();
        }
    }

    /**
	 * Set the selected table items to red. Later this change will
	 * be made persistent and written to the traffic lights' logic.
	 * 
	 * @author Martin Wolf
	 */
    protected void changeToRed() {
        for (QTableWidgetItem item : logicTable.selectedItems()) {
            item.setBackground(new QBrush(QColor.red));
            itemChanged = true;
            logicTable.clearSelection();
        }
    }

    /**
	 * This method is called every time the user switches the shown
	 * node. In this case the logic represented by the coloring of
	 * the table is written back to the cache.
	 * 
	 * @author Martin Wolf
	 */
    protected void saveTLS() {
        int i, j;
        HashMap<Edge, TrafficLightStateList> newLogic = new HashMap<Edge, TrafficLightStateList>();
        QTableWidgetItem item;
        TrafficLightStateList tls;
        for (i = 0; i < logicTable.rowCount(); i++) {
            for (Edge edge : currentTrafficLight.getUsedBy()) {
                if (edge.getPointerId().equals(logicTable.verticalHeaderItem(i).text())) {
                    tls = new TrafficLightStateList();
                    for (j = 0; j < logicTable.columnCount(); j++) {
                        item = logicTable.item(i, j);
                        if (item.background().color().equals(QColor.green)) {
                            tls.add(new TrafficLightStateListEntry(TrafficLightState.Green));
                        }
                        if (item.background().color().equals(QColor.yellow)) {
                            tls.add(new TrafficLightStateListEntry(TrafficLightState.Yellow));
                        }
                        if (item.background().color().equals(QColor.red)) {
                            tls.add(new TrafficLightStateListEntry(TrafficLightState.Red));
                        }
                    }
                    newLogic.put(edge, tls);
                }
            }
        }
        tllCache.put(currentTrafficLight, newLogic);
        logicTable.clearSelection();
        newLogic = null;
        int[] durations = new int[logicTable.columnCount()];
        for (i = 0; i < logicTable.columnCount(); i++) {
            durations[i] = logicTable.columnWidth(i) / 5;
        }
        durationsCache.put(currentTrafficLight, durations);
        itemChanged = false;
    }

    /**
	 * All changes will be stored to the internal database. Therefore
	 * the temporary data from the cache is read and written to the 
	 * database. Afterwards the dialog is closed.
	 * 
	 * @author Martin Wolf
	 */
    protected void saveAllChanges() {
        if (currentTrafficLight != null && itemChanged) {
            saveTLS();
        }
        for (TrafficLight trafficLight : tllCache.keySet()) {
            trafficLight.setDefaultLogic(tllCache.get(trafficLight));
            trafficLight.setDurations(durationsCache.get(trafficLight));
        }
        close();
    }

    /**
	 * Fill the details table with the edges and their information for
	 * a special selected traffic light.
	 */
    private void fillDetailsTable() {
        while (detailsTable.rowCount() < currentTrafficLight.getNoOfIncomingEdges()) {
            detailsTable.insertRow(detailsTable.rowCount());
        }
        while (detailsTable.rowCount() > currentTrafficLight.getNoOfIncomingEdges()) {
            detailsTable.removeRow(detailsTable.rowCount() - 1);
        }
        for (int i = 0; i < detailsTable.rowCount(); i++) {
            detailsTable.setRowHeight(i, 20);
        }
        List<String> verticalHeaderLables = new ArrayList<String>();
        QTableWidgetItem item;
        int edgeCount = 0;
        int columnCount = 0;
        for (Edge edge : currentIncomingEdges) {
            if ((Node) currentTrafficLight == edge.getToNode()) {
                item = new QTableWidgetItem(edge.getPointerId());
                item.setFlags(ItemFlag.ItemIsEnabled, ItemFlag.ItemIsSelectable);
                detailsTable.setItem(edgeCount, columnCount, item);
                columnCount++;
                item = new QTableWidgetItem(edge.getParentWay().getDescription());
                item.setFlags(ItemFlag.ItemIsEnabled, ItemFlag.ItemIsSelectable);
                detailsTable.setItem(edgeCount, columnCount, item);
                columnCount = 0;
                edgeCount++;
                verticalHeaderLables.add(Integer.toString(edgeCount));
            }
        }
        detailsTable.setVerticalHeaderLabels(verticalHeaderLables);
    }

    /**
	 * Table representing the traffic light logic is created. 
	 * 
	 * Number of row = number of incoming edges
	 * Number of columns = number of phases
	 * Background color of items = traffic light color (per edge/phase)
	 * 
	 * @author Martin Wolf
	 */
    private void fillLogicTable() {
        while (logicTable.rowCount() < currentTrafficLight.getNoOfIncomingEdges()) {
            logicTable.insertRow(logicTable.rowCount());
        }
        while (logicTable.rowCount() > currentTrafficLight.getNoOfIncomingEdges()) {
            logicTable.removeRow(logicTable.rowCount() - 1);
        }
        while (logicTable.columnCount() < currentTrafficLight.getNoOfPhases()) {
            logicTable.insertColumn(logicTable.columnCount());
            logicTable.setColumnWidth(logicTable.columnCount() - 1, 30);
        }
        while (logicTable.columnCount() > currentTrafficLight.getNoOfPhases()) {
            logicTable.removeColumn(logicTable.columnCount() - 1);
        }
        for (int i = 0; i < logicTable.rowCount(); i++) {
            logicTable.setRowHeight(i, 20);
        }
        List<String> verticalHeaderLables = new ArrayList<String>();
        List<String> horizontalHeaderLables = new ArrayList<String>();
        QTableWidgetItem item;
        int edgeCount = 0;
        int tlsCount;
        for (Edge edge : currentIncomingEdges) {
            if ((Node) currentTrafficLight == edge.getToNode()) {
                tlsCount = 0;
                for (TrafficLightStateListEntry tls : tllCache.get(currentTrafficLight).get(edge).getStates()) {
                    item = new QTableWidgetItem();
                    item.setFlags(ItemFlag.ItemIsEnabled, ItemFlag.ItemIsSelectable);
                    if (tls.getState() == TrafficLightState.Green) {
                        item.setBackground(new QBrush(QColor.green));
                    }
                    if (tls.getState() == TrafficLightState.Red) {
                        item.setBackground(new QBrush(QColor.red));
                    }
                    if (tls.getState() == TrafficLightState.Yellow) {
                        item.setBackground(new QBrush(QColor.yellow));
                    }
                    logicTable.setItem(edgeCount, tlsCount, item);
                    tlsCount++;
                }
                edgeCount++;
                verticalHeaderLables.add(Integer.toString(edgeCount));
            }
        }
        for (int i = 0; i < durationsCache.get(currentTrafficLight).length; i++) {
            logicTable.setColumnWidth(i, durationsCache.get(currentTrafficLight)[i] * 5);
            horizontalHeaderLables.add(Integer.toString(durationsCache.get(currentTrafficLight)[i]));
        }
        logicTable.setVerticalHeaderLabels(verticalHeaderLables);
        logicTable.setHorizontalHeaderLabels(horizontalHeaderLables);
    }

    /**
	 * Builds a preview of the intersection belonging to the node chosen
	 * by the user.
	 * 
	 * @author Martin Wolf
	 */
    private void buildPreview() {
        for (QGraphicsItemInterface item : previewModel.items()) {
            previewModel.removeItem(item);
        }
        ProjectionFactory.getInstance().setProjection(new Projection());
        NodeItem nodeItem = new NodeItem(currentTrafficLight);
        nodeItem.setToolTip(currentTrafficLight.getPointerId());
        previewModel.addItem(nodeItem);
        double x1, y1, x2, y2, xResult, yResult;
        QPointF resultPosition;
        int size = 3;
        int radius = 7;
        for (Edge edge : currentTrafficLight.getUsedBy()) {
            EdgeItem edgeItem = new EdgeItem(edge);
            previewModel.addItem(edgeItem);
            if ((Node) currentTrafficLight == edge.getToNode()) {
                x1 = edge.getToNode().getPosition().projected().x();
                y1 = edge.getToNode().getPosition().projected().y();
                x2 = edge.getFromNode().getPosition().projected().x();
                y2 = edge.getFromNode().getPosition().projected().y();
                xResult = x1 + (x2 - x1) * radius / Math.sqrt(Math.pow((x2 - x1), 2) + Math.pow((y2 - y1), 2));
                yResult = y1 + (y2 - y1) * radius / Math.sqrt(Math.pow((y2 - y1), 2) + Math.pow((x2 - x1), 2));
                resultPosition = new QPointF(xResult, yResult);
                previewModel.addItem(new DescriptionCircleItem(resultPosition, Integer.toString(currentIncomingEdges.indexOf(edge) + 1), size));
            }
        }
        previewWidget.recalculateSceneRect();
        if (nodeItem != null) previewWidget.centerOn(nodeItem);
    }

    /**
	 * Register the event listener and the context menu entry
	 */
    public void startUp(MainApp app) {
        app.addLoadEventListener(this);
        app.addContextEventListener(this);
    }

    /**
	 * Necessary to get the size change events influence the itemChanged status
	 * 
	 * @param itemChanged true, if the width of column has changed
	 */
    public void setItemChanged(boolean itemChanged) {
        this.itemChanged = itemChanged;
    }

    /**
	 * Close the dialog without saving. All important variables are reseted.
	 */
    protected void closeEvent(QCloseEvent event) {
        itemChanged = false;
        currentTrafficLight = null;
        super.closeEvent(event);
    }
}
