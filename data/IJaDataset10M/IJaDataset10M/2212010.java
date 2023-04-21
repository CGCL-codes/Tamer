package net.sf.czarrental.pl;

import java.awt.geom.Point2D;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import net.sf.czarrental.dl.ReservationsDB;
import com.vaadin.Application;
import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.TabSheet.SelectedTabChangeEvent;
import com.vaadin.ui.Window.Notification;

@SuppressWarnings("serial")
public class ReservationsUI extends Application {

    private ReservationsDB db;

    ResourceSelectorPanel resourcePanel;

    private CalendarField reservedFrom;

    private static final long DEFAULT_GAP_MILLIS = 3600000;

    private long currentGapMillis = DEFAULT_GAP_MILLIS;

    private CalendarField reservedTo;

    private Label resourceName;

    private TextField description;

    private Button reservationButton;

    private Table allTable;

    private GoogleMap map;

    @Override
    public void init() {
        db = new ReservationsDB(getProperty("jdbcUrl"));
        VerticalLayout mainLayout = new VerticalLayout();
        mainLayout.setSizeFull();
        mainLayout.setHeight("600px");
        mainLayout.setMargin(true);
        final Window mainWindow = new Window("Reservation", mainLayout);
        setMainWindow(mainWindow);
        setTheme("reservr");
        Label slogan = new Label("Vehicles...");
        slogan.setStyleName("slogan");
        mainLayout.addComponent(slogan);
        final TabSheet mainTabs = new TabSheet();
        mainTabs.setSizeFull();
        mainLayout.addComponent(mainTabs);
        mainLayout.setExpandRatio(mainTabs, 1);
        final VerticalLayout reservationTab = new VerticalLayout();
        reservationTab.setWidth("100%");
        mainTabs.addTab(reservationTab, "Make reservation", null);
        resourcePanel = new ResourceSelectorPanel("Resources");
        resourcePanel.setResourceContainer(db.getResources(null));
        resourcePanel.addListener(ResourceSelectorPanel.SelectedResourcesChangedEvent.class, this, "selectedResourcesChanged");
        reservationTab.addComponent(resourcePanel);
        HorizontalLayout reservationLayout = new HorizontalLayout();
        final Panel reservationPanel = new Panel("Reservation", reservationLayout);
        reservationPanel.addStyleName(Panel.STYLE_LIGHT);
        reservationLayout.setMargin(true);
        reservationTab.addComponent(reservationPanel);
        final VerticalLayout infoLayout = new VerticalLayout();
        infoLayout.setSpacing(true);
        infoLayout.setSizeUndefined();
        infoLayout.setMargin(false, true, false, false);
        reservationPanel.addComponent(infoLayout);
        resourceName = new Label("From the list above");
        resourceName.setCaption("Choose resource");
        infoLayout.addComponent(resourceName);
        description = new TextField();
        description.setWidth("250px");
        description.setRows(5);
        infoLayout.addComponent(description);
        reservationButton = new Button("Make reservation", this, "makeReservation");
        infoLayout.addComponent(reservationButton);
        infoLayout.setComponentAlignment(reservationButton, Alignment.MIDDLE_CENTER);
        map = new GoogleMap();
        map.setWidth("250px");
        map.setHeight("250px");
        map.setItemMarkerHtmlPropertyId(ReservationsDB.Resource.PROPERTY_ID_NAME);
        map.setItemMarkerXPropertyId(ReservationsDB.Resource.PROPERTY_ID_LOCATIONX);
        map.setItemMarkerYPropertyId(ReservationsDB.Resource.PROPERTY_ID_LOCATIONY);
        map.setContainerDataSource(db.getResources(null));
        infoLayout.addComponent(map);
        final Calendar from = Calendar.getInstance();
        from.add(Calendar.HOUR, 1);
        from.set(Calendar.MINUTE, 0);
        from.set(Calendar.SECOND, 0);
        from.set(Calendar.MILLISECOND, 0);
        reservedFrom = new CalendarField("From");
        reservedFrom.setMinimumDate(from.getTime());
        reservedFrom.setValue(from.getTime());
        reservedFrom.setImmediate(true);
        initCalendarFieldPropertyIds(reservedFrom);
        reservationPanel.addComponent(reservedFrom);
        final Label arrowLabel = new Label("&raquo;");
        arrowLabel.setContentMode(Label.CONTENT_XHTML);
        arrowLabel.setStyleName("arrow");
        reservationPanel.addComponent(arrowLabel);
        final Calendar to = Calendar.getInstance();
        to.setTime(from.getTime());
        to.add(Calendar.MILLISECOND, (int) DEFAULT_GAP_MILLIS);
        reservedTo = new CalendarField("To");
        reservedTo.setMinimumDate(to.getTime());
        reservedTo.setValue(to.getTime());
        reservedTo.setImmediate(true);
        initCalendarFieldPropertyIds(reservedTo);
        reservationPanel.addComponent(reservedTo);
        reservedFrom.addListener(new ValueChangeListener() {

            public void valueChange(ValueChangeEvent event) {
                final Date fd = (Date) reservedFrom.getValue();
                if (fd == null) {
                    reservedTo.setValue(null);
                    reservedTo.setEnabled(false);
                    refreshSelectedResources(true);
                    return;
                } else {
                    reservedTo.setEnabled(true);
                }
                reservedTo.setMinimumDate(new Date(fd.getTime() + DEFAULT_GAP_MILLIS));
                final Calendar to = Calendar.getInstance();
                to.setTime(fd);
                to.add(Calendar.MILLISECOND, (int) currentGapMillis);
                reservedTo.setValue(to.getTime());
                refreshSelectedResources(true);
            }
        });
        reservedTo.addListener(new ValueChangeListener() {

            public void valueChange(ValueChangeEvent event) {
                final Date from = (Date) reservedFrom.getValue();
                final Date to = (Date) reservedTo.getValue();
                currentGapMillis = to.getTime() - from.getTime();
                if (currentGapMillis <= 0) {
                    final Calendar t = Calendar.getInstance();
                    t.setTime(from);
                    t.add(Calendar.MILLISECOND, (int) DEFAULT_GAP_MILLIS);
                    reservedTo.setValue(t.getTime());
                }
                refreshSelectedResources(true);
            }
        });
        allTable = new Table();
        allTable.setSizeFull();
        allTable.setColumnCollapsingAllowed(true);
        allTable.setColumnReorderingAllowed(true);
        mainTabs.addTab(allTable, "All reservations", null);
        mainTabs.addListener(new TabSheet.SelectedTabChangeListener() {

            public void selectedTabChange(SelectedTabChangeEvent event) {
                refreshReservations();
            }
        });
        resourcePanel.selectFirstCategory();
        refreshReservations();
        refreshSelectedResources(true);
    }

    public void makeReservation() {
        try {
            final Item resource = getActiveResource();
            if (resource != null) {
                db.addReservation(resource, 0, (Date) reservedFrom.getValue(), (Date) reservedTo.getValue(), (String) description.getValue());
                getMainWindow().showNotification("Success!", "You have reserved the resource for the selected period.", Notification.TYPE_WARNING_MESSAGE);
                refreshReservations();
                refreshSelectedResources(false);
            } else {
                getMainWindow().showNotification("Oops!", "Please select a resource (or category) to reserve.", Notification.TYPE_WARNING_MESSAGE);
            }
        } catch (final ResourceNotAvailableException e) {
            getMainWindow().showNotification("Not available!", "The selected resource is already reserved for the selected period.", Notification.TYPE_ERROR_MESSAGE);
            refreshReservations();
        }
    }

    private Item getActiveResource() throws ResourceNotAvailableException {
        final LinkedList<Item> rids = resourcePanel.getSelectedResources();
        if (rids != null && rids.size() > 0) {
            for (final Iterator<Item> it = rids.iterator(); it.hasNext(); ) {
                final Item resource = it.next();
                final int id = ((Integer) resource.getItemProperty(ReservationsDB.Resource.PROPERTY_ID_ID).getValue()).intValue();
                if (db.isAvailableResource(id, (Date) reservedFrom.getValue(), (Date) reservedTo.getValue())) {
                    return resource;
                }
            }
            throw new ResourceNotAvailableException("No available resources");
        } else {
            return null;
        }
    }

    private void refreshReservations() {
        final Container reservations = db.getReservations(resourcePanel.getSelectedResources());
        reservedFrom.setContainerDataSource(reservations);
        reservedTo.setContainerDataSource(reservations);
        final Container allReservations = db.getReservations(null);
        allTable.setContainerDataSource(allReservations);
        if (allReservations != null && allReservations.size() > 0) {
            allTable.setVisibleColumns(new Object[] { ReservationsDB.Reservation.PROPERTY_ID_RESERVED_FROM, ReservationsDB.Reservation.PROPERTY_ID_RESERVED_TO, ReservationsDB.Resource.PROPERTY_ID_NAME, ReservationsDB.Resource.PROPERTY_ID_DESCRIPTION, ReservationsDB.Reservation.PROPERTY_ID_DESCRIPTION });
            allTable.setColumnHeaders(new String[] { "From", "To", "Resource", "Description", "Message" });
        }
    }

    private void refreshSelectedResources(boolean alertIfNotAvailable) {
        Item resource = null;
        try {
            resource = getActiveResource();
        } catch (final ResourceNotAvailableException e) {
            if (alertIfNotAvailable) {
                getMainWindow().showNotification("Not available", "Please choose another resource or time period.", Notification.TYPE_HUMANIZED_MESSAGE);
            }
            refreshReservations();
            return;
        }
        map.clear();
        if (resource == null) {
            resourceName.setCaption("Choose resource above");
            resourceName.setValue("");
            map.setContainerDataSource(db.getResources(null));
            map.setZoomLevel(1);
        } else {
            String name = (String) resource.getItemProperty(ReservationsDB.Resource.PROPERTY_ID_NAME).getValue();
            String desc = (String) resource.getItemProperty(ReservationsDB.Resource.PROPERTY_ID_DESCRIPTION).getValue();
            resourceName.setCaption(name);
            resourceName.setValue(desc);
            final LinkedList<Item> srs = resourcePanel.getSelectedResources();
            for (final Iterator<Item> it = srs.iterator(); it.hasNext(); ) {
                resource = it.next();
                name = (String) resource.getItemProperty(ReservationsDB.Resource.PROPERTY_ID_NAME).getValue();
                desc = (String) resource.getItemProperty(ReservationsDB.Resource.PROPERTY_ID_DESCRIPTION).getValue();
                final Double x = (Double) resource.getItemProperty(ReservationsDB.Resource.PROPERTY_ID_LOCATIONX).getValue();
                final Double y = (Double) resource.getItemProperty(ReservationsDB.Resource.PROPERTY_ID_LOCATIONY).getValue();
                if (x != null && y != null) {
                    map.addMarker(name + "<br/>" + desc, new Point2D.Double(x.doubleValue(), y.doubleValue()));
                }
            }
            map.setZoomLevel((srs.size() == 1 ? 14 : 9));
        }
    }

    private void initCalendarFieldPropertyIds(CalendarField cal) {
        cal.setItemStyleNamePropertyId(ReservationsDB.Resource.PROPERTY_ID_STYLENAME);
        cal.setItemStartPropertyId(ReservationsDB.Reservation.PROPERTY_ID_RESERVED_FROM);
        cal.setItemEndPropertyId(ReservationsDB.Reservation.PROPERTY_ID_RESERVED_TO);
        cal.setItemTitlePropertyId(ReservationsDB.Resource.PROPERTY_ID_NAME);
        cal.setItemDescriptionPropertyId(ReservationsDB.Reservation.PROPERTY_ID_DESCRIPTION);
    }

    public void selectedResourcesChanged(ResourceSelectorPanel.SelectedResourcesChangedEvent event) {
        refreshReservations();
        refreshSelectedResources(true);
    }
}
