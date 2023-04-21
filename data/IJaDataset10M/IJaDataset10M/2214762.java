package gtf.client.tabs.trips;

import gtf.client.Data.Calendar_Data;
import gtf.client.Data.Exception_Data;
import gtf.client.Data.Frequency_Data;
import gtf.client.Data.Trip_Data;
import gtf.client.Service.Calendar_ServerService;
import gtf.client.Service.Calendar_ServerServiceAsync;
import gtf.client.Service.Route_ServerService;
import gtf.client.Service.Route_ServerServiceAsync;
import gtf.client.tabs.Tab;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ChangeListener;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.HorizontalSplitPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class Trips extends Tab implements ClickListener, ChangeListener {

    HorizontalSplitPanel main_container = new HorizontalSplitPanel();

    HorizontalPanel panel_left = new HorizontalPanel();

    HorizontalPanel panel_right = new HorizontalPanel();

    VerticalPanel panel_left_left = new VerticalPanel();

    VerticalPanel panel_left_right = new VerticalPanel();

    HorizontalPanel panel_list = new HorizontalPanel();

    HorizontalPanel panel_route = new HorizontalPanel();

    HorizontalPanel panel_date = new HorizontalPanel();

    HorizontalPanel panel_headsign = new HorizontalPanel();

    HorizontalPanel panel_frequency = new HorizontalPanel();

    HorizontalPanel panel_start = new HorizontalPanel();

    HorizontalPanel panel_end = new HorizontalPanel();

    HorizontalPanel panel_headway = new HorizontalPanel();

    HorizontalPanel panel_direction = new HorizontalPanel();

    HorizontalPanel panel_block = new HorizontalPanel();

    HorizontalPanel panel_shape = new HorizontalPanel();

    HorizontalPanel panel_button1 = new HorizontalPanel();

    HorizontalPanel panel_button2 = new HorizontalPanel();

    HorizontalPanel panel_button3 = new HorizontalPanel();

    HorizontalPanel panel_button4 = new HorizontalPanel();

    Label label_list = new Label("All Trips");

    Label label_route = new Label("Route");

    Label label_date = new Label("Calendar");

    Label label_headsign = new Label("Headsign");

    Label label_direction = new Label("Direction");

    Label label_block = new Label("Block");

    Label label_shape = new Label("Shape");

    Label label_frequency = new Label("Frequencies");

    Label label_start = new Label("Start Time");

    Label label_end = new Label("End Time");

    Label label_headway = new Label("Headway");

    TextBox tbox_headsign = new TextBox();

    TextBox tbox_block = new TextBox();

    TextBox tbox_start = new TextBox();

    TextBox tbox_end = new TextBox();

    TextBox tbox_headway = new TextBox();

    ListBox lbox_trip = new ListBox();

    ListBox lbox_route = new ListBox();

    ListBox lbox_date = new ListBox();

    ListBox lbox_direction = new ListBox();

    ListBox lbox_shape = new ListBox();

    ListBox lbox_frequency = new ListBox();

    Button bt_insert = new Button("Insert");

    Button bt_update = new Button("Update");

    Button bt_delete = new Button("Delete");

    Button bt_cancel = new Button("Cancel");

    Button bt_frequency_insert = new Button("Insert");

    Button bt_frequency_update = new Button("Update");

    Button bt_frequency_delete = new Button("Delete");

    Button bt_frequency_cancel = new Button("Cancel");

    VerticalPanel panel_right_left = new VerticalPanel();

    VerticalPanel panel_right_right = new VerticalPanel();

    HorizontalPanel panel_calendar_list = new HorizontalPanel();

    HorizontalPanel panel_calendar_name = new HorizontalPanel();

    HorizontalPanel panel_calendar_start = new HorizontalPanel();

    HorizontalPanel panel_calendar_end = new HorizontalPanel();

    HorizontalPanel panel_calendar_days1 = new HorizontalPanel();

    HorizontalPanel panel_calendar_days2 = new HorizontalPanel();

    HorizontalPanel panel_exception = new HorizontalPanel();

    HorizontalPanel panel_exception_list = new HorizontalPanel();

    HorizontalPanel panel_exception_date = new HorizontalPanel();

    HorizontalPanel panel_exception_type = new HorizontalPanel();

    HorizontalPanel panel_calendar_button1 = new HorizontalPanel();

    HorizontalPanel panel_calendar_button2 = new HorizontalPanel();

    HorizontalPanel panel_calendar_button3 = new HorizontalPanel();

    HorizontalPanel panel_calendar_button4 = new HorizontalPanel();

    Label label_calendar_list = new Label("All Calendars");

    Label label_calendar_name = new Label("Name");

    Label label_calendar_start = new Label("Start Date");

    Label label_calendar_end = new Label("End Date");

    Label label_calendar_days = new Label("Days");

    Label label_exception = new Label("Exception?");

    Label label_exception_list = new Label("All Exceptions");

    Label label_exception_date = new Label("Exception Date");

    Label label_exception_type = new Label("Type");

    Label label_monday = new Label("Monday");

    Label label_tuesday = new Label("Tuesday");

    Label label_wednesday = new Label("Wednesday");

    Label label_thursday = new Label("Thursday");

    Label label_friday = new Label("Friday");

    Label label_saturday = new Label("Saturday");

    Label label_sunday = new Label("Sunday");

    CheckBox ckbox_monday = new CheckBox();

    CheckBox ckbox_tuesday = new CheckBox();

    CheckBox ckbox_wednesday = new CheckBox();

    CheckBox ckbox_thursday = new CheckBox();

    CheckBox ckbox_friday = new CheckBox();

    CheckBox ckbox_saturday = new CheckBox();

    CheckBox ckbox_sunday = new CheckBox();

    CheckBox ckbox_exception = new CheckBox();

    TextBox tbox_calendar_name = new TextBox();

    TextBox tbox_calendar_start = new TextBox();

    TextBox tbox_calendar_end = new TextBox();

    TextBox tbox_exception_date = new TextBox();

    ListBox lbox_calendar = new ListBox();

    ListBox lbox_exception = new ListBox();

    ListBox lbox_exception_type = new ListBox();

    Button bt_calendar_insert = new Button("Insert");

    Button bt_calendar_update = new Button("Update");

    Button bt_calendar_delete = new Button("Delete");

    Button bt_calendar_cancel = new Button("Cancel");

    Button bt_exception_insert = new Button("Insert");

    Button bt_exception_update = new Button("Update");

    Button bt_exception_delete = new Button("Delete");

    Button bt_exception_cancel = new Button("Cancel");

    Trip_Data tripData = new Trip_Data();

    Frequency_Data frequencyData = new Frequency_Data();

    Calendar_Data calendarData = new Calendar_Data();

    Exception_Data exceptionData = new Exception_Data();

    int load = 0;

    public static TabInfo init() {
        return new TabInfo("Trips", "<h2>Trips</h2>" + "<p>Trips</p>") {

            public String[][] getACLResources() {
                return new String[][] { new String[] { "btn_atualizar", "Button", "Atualiza a tela" }, new String[] { "btn_save", "Button", "Salva os dados" }, new String[] { "btn_reload", "Button", "Descarta" } };
            }

            public Tab createInstance() {
                return new Trips();
            }

            public String getColor() {
                return "#94A18B";
            }
        };
    }

    public Trips() {
        main_container.setLeftWidget(panel_left);
        main_container.setRightWidget(panel_right);
        main_container.setSize("100%", "400px");
        panel_left.setSize("100%", "150px");
        panel_left.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        panel_left.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
        panel_left.add(panel_left_left);
        panel_left.add(panel_left_right);
        panel_right.setSize("100%", "100%");
        panel_right.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        panel_right.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
        panel_right.add(panel_right_left);
        panel_right.add(panel_right_right);
        panel_left_left.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        panel_left_left.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
        panel_left_left.add(panel_list);
        panel_left_left.add(panel_route);
        panel_left_left.add(panel_date);
        panel_left_left.add(panel_headsign);
        panel_left_left.add(panel_direction);
        panel_left_left.add(panel_block);
        panel_left_left.add(panel_shape);
        panel_left_left.add(panel_button1);
        panel_left_left.add(panel_button2);
        panel_left_right.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        panel_left_right.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
        panel_left_right.add(panel_frequency);
        panel_left_right.add(panel_start);
        panel_left_right.add(panel_end);
        panel_left_right.add(panel_headway);
        panel_left_right.add(panel_button3);
        panel_left_right.add(panel_button4);
        label_route.setStyleName("gtf-Label");
        label_date.setStyleName("gtf-Label");
        label_headsign.setStyleName("gtf-Label");
        label_direction.setStyleName("gtf-Label");
        label_block.setStyleName("gtf-Label");
        label_shape.setStyleName("gtf-Label");
        panel_list.setSpacing(5);
        panel_list.add(label_list);
        panel_list.add(lbox_trip);
        panel_route.setSpacing(5);
        panel_route.add(label_route);
        panel_route.add(lbox_route);
        panel_date.setSpacing(5);
        panel_date.add(label_date);
        panel_date.add(lbox_date);
        panel_headsign.setSpacing(5);
        panel_headsign.add(label_headsign);
        panel_headsign.add(tbox_headsign);
        panel_direction.setSpacing(5);
        panel_direction.add(label_direction);
        panel_direction.add(lbox_direction);
        panel_block.setSpacing(5);
        panel_block.add(label_block);
        panel_block.add(tbox_block);
        panel_shape.setSpacing(5);
        panel_shape.add(label_shape);
        panel_shape.add(lbox_shape);
        panel_button1.setSpacing(5);
        panel_button1.add(bt_insert);
        panel_button1.add(bt_update);
        panel_button2.setSpacing(5);
        panel_button2.add(bt_delete);
        panel_button2.add(bt_cancel);
        bt_insert.setWidth("75px");
        bt_update.setWidth("75px");
        bt_delete.setWidth("75px");
        bt_cancel.setWidth("75px");
        lbox_frequency.setVisibleItemCount(5);
        panel_frequency.setSpacing(5);
        panel_frequency.add(label_frequency);
        panel_frequency.add(lbox_frequency);
        panel_start.setSpacing(5);
        panel_start.add(label_start);
        panel_start.add(tbox_start);
        panel_end.setSpacing(5);
        panel_end.add(label_end);
        panel_end.add(tbox_end);
        panel_headway.setSpacing(5);
        panel_headway.add(label_headway);
        panel_headway.add(tbox_headway);
        panel_button3.setSpacing(5);
        panel_button3.add(bt_frequency_insert);
        panel_button3.add(bt_frequency_update);
        panel_button4.setSpacing(5);
        panel_button4.add(bt_frequency_delete);
        panel_button4.add(bt_frequency_cancel);
        bt_frequency_insert.setWidth("75px");
        bt_frequency_update.setWidth("75px");
        bt_frequency_delete.setWidth("75px");
        bt_frequency_cancel.setWidth("75px");
        panel_right_left.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        panel_right_left.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
        panel_right_left.add(panel_calendar_list);
        panel_right_left.add(panel_calendar_name);
        panel_right_left.add(panel_calendar_start);
        panel_right_left.add(panel_calendar_end);
        panel_right_left.add(panel_calendar_days1);
        panel_right_left.add(panel_calendar_days2);
        panel_right_left.add(panel_calendar_button1);
        panel_right_left.add(panel_calendar_button2);
        panel_right_right.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        panel_right_right.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
        panel_right_right.add(panel_exception);
        panel_right_right.add(panel_exception_list);
        panel_right_right.add(panel_exception_type);
        panel_right_right.add(panel_exception_date);
        panel_right_right.add(panel_calendar_button3);
        panel_right_right.add(panel_calendar_button4);
        panel_calendar_list.setSpacing(5);
        panel_calendar_list.add(label_calendar_list);
        panel_calendar_list.add(lbox_calendar);
        panel_calendar_name.setSpacing(5);
        panel_calendar_name.add(label_calendar_name);
        panel_calendar_name.add(tbox_calendar_name);
        panel_calendar_start.setSpacing(5);
        panel_calendar_start.add(label_calendar_start);
        panel_calendar_start.add(tbox_calendar_start);
        panel_calendar_end.setSpacing(5);
        panel_calendar_end.add(label_calendar_end);
        panel_calendar_end.add(tbox_calendar_end);
        panel_calendar_days1.setSpacing(5);
        panel_calendar_days1.add(label_calendar_days);
        panel_calendar_days1.add(ckbox_monday);
        panel_calendar_days1.add(label_monday);
        panel_calendar_days1.add(ckbox_tuesday);
        panel_calendar_days1.add(label_tuesday);
        panel_calendar_days1.add(ckbox_wednesday);
        panel_calendar_days1.add(label_wednesday);
        panel_calendar_days2.add(ckbox_thursday);
        panel_calendar_days2.add(label_thursday);
        panel_calendar_days2.add(ckbox_friday);
        panel_calendar_days2.add(label_friday);
        panel_calendar_days2.add(ckbox_saturday);
        panel_calendar_days2.add(label_saturday);
        panel_calendar_days2.add(ckbox_sunday);
        panel_calendar_days2.add(label_sunday);
        panel_calendar_button1.setSpacing(5);
        panel_calendar_button1.add(bt_calendar_insert);
        panel_calendar_button1.add(bt_calendar_update);
        panel_calendar_button2.setSpacing(5);
        panel_calendar_button2.add(bt_calendar_delete);
        panel_calendar_button2.add(bt_calendar_cancel);
        bt_calendar_insert.setWidth("75px");
        bt_calendar_update.setWidth("75px");
        bt_calendar_delete.setWidth("75px");
        bt_calendar_cancel.setWidth("75px");
        panel_exception.setSpacing(5);
        panel_exception.add(label_exception);
        panel_exception.add(ckbox_exception);
        lbox_exception.setVisibleItemCount(5);
        panel_exception_list.setSpacing(5);
        panel_exception_list.add(label_exception_list);
        panel_exception_list.add(lbox_exception);
        panel_exception_type.setSpacing(5);
        panel_exception_type.add(label_exception_type);
        panel_exception_type.add(lbox_exception_type);
        panel_exception_date.setSpacing(5);
        panel_exception_date.add(label_exception_date);
        panel_exception_date.add(tbox_exception_date);
        panel_calendar_button3.setSpacing(5);
        panel_calendar_button3.add(bt_exception_insert);
        panel_calendar_button3.add(bt_exception_update);
        panel_calendar_button4.setSpacing(5);
        panel_calendar_button4.add(bt_exception_delete);
        panel_calendar_button4.add(bt_exception_cancel);
        bt_exception_insert.setWidth("75px");
        bt_exception_update.setWidth("75px");
        bt_exception_delete.setWidth("75px");
        bt_exception_cancel.setWidth("75px");
        label_list.addClickListener(this);
        label_route.addClickListener(this);
        label_date.addClickListener(this);
        label_headsign.addClickListener(this);
        label_direction.addClickListener(this);
        label_block.addClickListener(this);
        label_shape.addClickListener(this);
        bt_insert.addClickListener(this);
        bt_update.addClickListener(this);
        bt_delete.addClickListener(this);
        bt_cancel.addClickListener(this);
        bt_frequency_insert.addClickListener(this);
        bt_frequency_update.addClickListener(this);
        bt_frequency_delete.addClickListener(this);
        bt_frequency_cancel.addClickListener(this);
        bt_calendar_insert.addClickListener(this);
        bt_calendar_update.addClickListener(this);
        bt_calendar_delete.addClickListener(this);
        bt_calendar_cancel.addClickListener(this);
        bt_exception_insert.addClickListener(this);
        bt_exception_update.addClickListener(this);
        bt_exception_delete.addClickListener(this);
        bt_exception_cancel.addClickListener(this);
        lbox_trip.addChangeListener(this);
        lbox_frequency.addChangeListener(this);
        lbox_calendar.addChangeListener(this);
        lbox_exception.addChangeListener(this);
        ckbox_exception.addClickListener(this);
        initWidget(main_container);
    }

    public void onShow() {
        visibleAllFrequencyFields(false);
        visibleAllExceptionFields(false);
        lbox_trip.clear();
        lbox_trip.addItem("All", "0");
        listTrip();
        lbox_date.clear();
        lbox_date.addItem("Choose", "0");
        lbox_calendar.clear();
        lbox_calendar.addItem("All", "0");
        listCalendar();
        if (load == 0) {
            load = 1;
            lbox_route.addItem("Choose", "0");
            listRoute();
            lbox_direction.addItem("Choose", "0");
            listDirection();
            lbox_exception_type.addItem("Choose", "0");
            listExceptionType();
            lbox_shape.addItem("Choose", "0");
            listShape();
        }
    }

    public void onClick(Widget sender) {
        if (sender == label_list) {
            MyPopup p = new MyPopup("label_list");
            p.setPopupPosition(sender.getAbsoluteLeft() + 10, sender.getAbsoluteTop() + 10);
            p.show();
        } else if (sender == label_route) {
            MyPopup p = new MyPopup("label_route");
            p.setPopupPosition(sender.getAbsoluteLeft() + 10, sender.getAbsoluteTop() + 10);
            p.show();
        } else if (sender == label_date) {
            MyPopup p = new MyPopup("label_date");
            p.setPopupPosition(sender.getAbsoluteLeft() + 10, sender.getAbsoluteTop() + 10);
            p.show();
        } else if (sender == label_headsign) {
            MyPopup p = new MyPopup("label_headsign");
            p.setPopupPosition(sender.getAbsoluteLeft() + 10, sender.getAbsoluteTop() + 10);
            p.show();
        } else if (sender == label_direction) {
            MyPopup p = new MyPopup("label_direction");
            p.setPopupPosition(sender.getAbsoluteLeft() + 10, sender.getAbsoluteTop() + 10);
            p.show();
        } else if (sender == label_block) {
            MyPopup p = new MyPopup("label_block");
            p.setPopupPosition(sender.getAbsoluteLeft() + 10, sender.getAbsoluteTop() + 10);
            p.show();
        } else if (sender == label_shape) {
            MyPopup p = new MyPopup("label_shape");
            p.setPopupPosition(sender.getAbsoluteLeft() + 10, sender.getAbsoluteTop() + 10);
            p.show();
        } else if (sender == bt_insert) {
            insertTrip_Data();
        } else if (sender == bt_frequency_insert) {
            insertFrequency_Data();
        } else if (sender == bt_calendar_insert) {
            insertCalendar_Data();
        } else if (sender == bt_exception_insert) {
            insertException_Data();
        } else if (sender == bt_update) {
            updateTrip_Data();
        } else if (sender == bt_calendar_update) {
            updateCalendar_Data();
        } else if (sender == bt_frequency_update) {
            updateFrequency_Data();
        } else if (sender == bt_exception_update) {
            updateException_Data();
        } else if (sender == bt_delete) {
            deleteTrip_Data();
        } else if (sender == bt_frequency_delete) {
            deleteFrequency_Data();
        } else if (sender == bt_calendar_delete) {
            deleteCalendar_Data();
        } else if (sender == bt_exception_delete) {
            deleteException_Data();
        } else if (sender == bt_cancel) {
            clearTrip_Data(true);
            visibleAllFrequencyFields(false);
        } else if (sender == bt_frequency_cancel) {
            clearFrequency_Data(false);
        } else if (sender == bt_calendar_cancel) {
            clearCalendar_Data(false);
            ckbox_exception.setChecked(false);
        } else if (sender == bt_calendar_cancel) {
            clearException_Data(false);
        } else if (sender == ckbox_exception) {
            visibleAllExceptionFields(ckbox_exception.isChecked());
        }
    }

    public void onChange(Widget sender) {
        if (sender == lbox_trip) {
            clearTrip_Data(false);
            if (Integer.parseInt(lbox_trip.getValue(lbox_trip.getSelectedIndex())) == 0) {
                visibleAllFrequencyFields(false);
            } else {
                getTrip_Data();
                visibleAllFrequencyFields(true);
            }
        } else if (sender == lbox_frequency) {
            clearFrequency_Data(false);
            getFrequency_Data();
        } else if (sender == lbox_calendar) {
            clearCalendar_Data(false);
            visibleAllExceptionFields(false);
            if (Integer.parseInt(lbox_calendar.getValue(lbox_calendar.getSelectedIndex())) != 0) {
                getCalendar_Data();
            }
        } else if (sender == lbox_exception) {
            clearException_Data(false);
            getException_Data();
        }
    }

    private void listTrip() {
    }

    private void listDirection() {
        lbox_direction.addItem("Going", "1");
        lbox_direction.addItem("Returning", "2");
    }

    private void listRoute() {
        Route_ServerServiceAsync serviceProxy = (Route_ServerServiceAsync) GWT.create(Route_ServerService.class);
        ServiceDefTarget target = (ServiceDefTarget) serviceProxy;
        target.setServiceEntryPoint(GWT.getModuleBaseURL() + "route_server");
        AsyncCallback callback = new AsyncCallback() {

            public void onFailure(Throwable caught) {
                GWT.log("Error", caught);
            }

            public void onSuccess(Object result) {
                String[][] tmp = (String[][]) result;
                for (int i = 0; i < tmp.length; i++) {
                    lbox_route.addItem(tmp[i][1], tmp[i][0]);
                }
            }
        };
        serviceProxy.listRoute(callback);
    }

    private void listCalendar() {
        Calendar_ServerServiceAsync serviceProxy = (Calendar_ServerServiceAsync) GWT.create(Calendar_ServerService.class);
        ServiceDefTarget target = (ServiceDefTarget) serviceProxy;
        target.setServiceEntryPoint(GWT.getModuleBaseURL() + "calendar_server");
        AsyncCallback callback = new AsyncCallback() {

            public void onFailure(Throwable caught) {
                GWT.log("Error", caught);
            }

            public void onSuccess(Object result) {
                String[][] tmp = (String[][]) result;
                for (int i = 0; i < tmp.length; i++) {
                    lbox_date.addItem(tmp[i][1], tmp[i][0]);
                    lbox_calendar.addItem(tmp[i][1], tmp[i][0]);
                }
            }
        };
        serviceProxy.listCalendar(callback);
    }

    private void listExceptionType() {
        lbox_exception_type.addItem("Include service", "1");
        lbox_exception_type.addItem("Exclude service", "2");
    }

    private void listFrequency(int id) {
    }

    private void listException(int id) {
        Calendar_ServerServiceAsync serviceProxy = (Calendar_ServerServiceAsync) GWT.create(Calendar_ServerService.class);
        ServiceDefTarget target = (ServiceDefTarget) serviceProxy;
        target.setServiceEntryPoint(GWT.getModuleBaseURL() + "calendar_server");
        AsyncCallback callback = new AsyncCallback() {

            public void onFailure(Throwable caught) {
                GWT.log("Error", caught);
            }

            public void onSuccess(Object result) {
                String[][] tmp = (String[][]) result;
                for (int i = 0; i < tmp.length; i++) {
                    ckbox_exception.setChecked(true);
                    visibleAllExceptionFields(true);
                    lbox_exception.addItem(tmp[i][1], tmp[i][0]);
                }
            }
        };
        serviceProxy.listException(id, callback);
    }

    private void listShape() {
    }

    private void insertTrip_Data() {
    }

    private void insertFrequency_Data() {
    }

    private void insertCalendar_Data() {
        Calendar_ServerServiceAsync serviceProxy = (Calendar_ServerServiceAsync) GWT.create(Calendar_ServerService.class);
        ServiceDefTarget target = (ServiceDefTarget) serviceProxy;
        target.setServiceEntryPoint(GWT.getModuleBaseURL() + "calendar_server");
        calendarData.setService_id(Integer.parseInt(lbox_calendar.getValue(lbox_calendar.getSelectedIndex())));
        calendarData.setCalendar_name(tbox_calendar_name.getText());
        calendarData.setStart_date(tbox_calendar_start.getText());
        calendarData.setEnd_date(tbox_calendar_end.getText());
        calendarData.setMonday(ckbox_monday.isChecked());
        calendarData.setTuesday(ckbox_tuesday.isChecked());
        calendarData.setWednesday(ckbox_wednesday.isChecked());
        calendarData.setThursday(ckbox_thursday.isChecked());
        calendarData.setFriday(ckbox_friday.isChecked());
        calendarData.setSaturday(ckbox_saturday.isChecked());
        calendarData.setSunday(ckbox_sunday.isChecked());
        AsyncCallback callback = new AsyncCallback() {

            public void onFailure(Throwable caught) {
                GWT.log("Error", caught);
            }

            public void onSuccess(Object result) {
                lbox_calendar.clear();
                lbox_calendar.addItem("All", "0");
                listCalendar();
                MyPopup p = new MyPopup(result.toString());
                p.setPopupPosition(bt_calendar_insert.getAbsoluteLeft() - 125, bt_calendar_insert.getAbsoluteTop() - 150);
                p.show();
            }
        };
        serviceProxy.insertCalendar_Data(calendarData, callback);
    }

    private void insertException_Data() {
        Calendar_ServerServiceAsync serviceProxy = (Calendar_ServerServiceAsync) GWT.create(Calendar_ServerService.class);
        ServiceDefTarget target = (ServiceDefTarget) serviceProxy;
        target.setServiceEntryPoint(GWT.getModuleBaseURL() + "calendar_server");
        exceptionData.setService_id(Integer.parseInt(lbox_calendar.getValue(lbox_calendar.getSelectedIndex())));
        exceptionData.setDate(tbox_exception_date.getText());
        exceptionData.setException_type(Integer.parseInt(lbox_exception_type.getValue(lbox_exception_type.getSelectedIndex())));
        AsyncCallback callback = new AsyncCallback() {

            public void onFailure(Throwable caught) {
                GWT.log("Error", caught);
            }

            public void onSuccess(Object result) {
                lbox_exception.clear();
                listException(exceptionData.getService_id());
                MyPopup p = new MyPopup(result.toString());
                p.setPopupPosition(bt_exception_insert.getAbsoluteLeft() - 125, bt_exception_insert.getAbsoluteTop() - 150);
                p.show();
            }
        };
        serviceProxy.insertException_Data(exceptionData, callback);
    }

    private void updateTrip_Data() {
    }

    private void updateFrequency_Data() {
    }

    private void updateCalendar_Data() {
        Calendar_ServerServiceAsync serviceProxy = (Calendar_ServerServiceAsync) GWT.create(Calendar_ServerService.class);
        ServiceDefTarget target = (ServiceDefTarget) serviceProxy;
        target.setServiceEntryPoint(GWT.getModuleBaseURL() + "calendar_server");
        calendarData.setService_id(Integer.parseInt(lbox_calendar.getValue(lbox_calendar.getSelectedIndex())));
        calendarData.setCalendar_name(tbox_calendar_name.getText());
        calendarData.setStart_date(tbox_calendar_start.getText());
        calendarData.setEnd_date(tbox_calendar_end.getText());
        calendarData.setMonday(ckbox_monday.isChecked());
        calendarData.setTuesday(ckbox_tuesday.isChecked());
        calendarData.setWednesday(ckbox_wednesday.isChecked());
        calendarData.setThursday(ckbox_thursday.isChecked());
        calendarData.setFriday(ckbox_friday.isChecked());
        calendarData.setSaturday(ckbox_saturday.isChecked());
        calendarData.setSunday(ckbox_sunday.isChecked());
        AsyncCallback callback = new AsyncCallback() {

            public void onFailure(Throwable caught) {
                GWT.log("Error", caught);
            }

            public void onSuccess(Object result) {
                MyPopup p = new MyPopup(result.toString());
                p.setPopupPosition(bt_calendar_insert.getAbsoluteLeft() - 125, bt_calendar_insert.getAbsoluteTop() - 150);
                p.show();
            }
        };
        serviceProxy.updateCalendar_Data(calendarData, callback);
    }

    private void updateException_Data() {
        Calendar_ServerServiceAsync serviceProxy = (Calendar_ServerServiceAsync) GWT.create(Calendar_ServerService.class);
        ServiceDefTarget target = (ServiceDefTarget) serviceProxy;
        target.setServiceEntryPoint(GWT.getModuleBaseURL() + "calendar_server");
        exceptionData.setException_id(Integer.parseInt(lbox_exception.getValue(lbox_exception.getSelectedIndex())));
        exceptionData.setService_id(Integer.parseInt(lbox_calendar.getValue(lbox_calendar.getSelectedIndex())));
        exceptionData.setDate(tbox_exception_date.getText());
        exceptionData.setException_type(Integer.parseInt(lbox_exception_type.getValue(lbox_exception_type.getSelectedIndex())));
        AsyncCallback callback = new AsyncCallback() {

            public void onFailure(Throwable caught) {
                GWT.log("Error", caught);
            }

            public void onSuccess(Object result) {
                lbox_exception.clear();
                listException(exceptionData.getService_id());
                MyPopup p = new MyPopup(result.toString());
                p.setPopupPosition(bt_exception_insert.getAbsoluteLeft() - 125, bt_exception_insert.getAbsoluteTop() - 150);
                p.show();
            }
        };
        serviceProxy.updateException_Data(exceptionData, callback);
    }

    private void deleteTrip_Data() {
    }

    private void deleteFrequency_Data() {
    }

    private void deleteCalendar_Data() {
        Calendar_ServerServiceAsync serviceProxy = (Calendar_ServerServiceAsync) GWT.create(Calendar_ServerService.class);
        ServiceDefTarget target = (ServiceDefTarget) serviceProxy;
        target.setServiceEntryPoint(GWT.getModuleBaseURL() + "calendar_server");
        calendarData.setService_id(Integer.parseInt(lbox_calendar.getValue(lbox_calendar.getSelectedIndex())));
        calendarData.setCalendar_name(tbox_calendar_name.getText());
        calendarData.setStart_date(tbox_calendar_start.getText());
        calendarData.setEnd_date(tbox_calendar_end.getText());
        calendarData.setMonday(ckbox_monday.isChecked());
        calendarData.setTuesday(ckbox_tuesday.isChecked());
        calendarData.setWednesday(ckbox_wednesday.isChecked());
        calendarData.setThursday(ckbox_thursday.isChecked());
        calendarData.setFriday(ckbox_friday.isChecked());
        calendarData.setSaturday(ckbox_saturday.isChecked());
        calendarData.setSunday(ckbox_sunday.isChecked());
        AsyncCallback callback = new AsyncCallback() {

            public void onFailure(Throwable caught) {
                GWT.log("Error", caught);
            }

            public void onSuccess(Object result) {
                lbox_calendar.removeItem(lbox_calendar.getSelectedIndex());
                clearCalendar_Data(true);
                MyPopup p = new MyPopup(result.toString());
                p.setPopupPosition(bt_calendar_insert.getAbsoluteLeft() - 125, bt_calendar_insert.getAbsoluteTop() - 150);
                p.show();
            }
        };
        serviceProxy.deleteCalendar_Data(calendarData, callback);
    }

    private void deleteException_Data() {
        Calendar_ServerServiceAsync serviceProxy = (Calendar_ServerServiceAsync) GWT.create(Calendar_ServerService.class);
        ServiceDefTarget target = (ServiceDefTarget) serviceProxy;
        target.setServiceEntryPoint(GWT.getModuleBaseURL() + "calendar_server");
        exceptionData.setException_id(Integer.parseInt(lbox_exception.getValue(lbox_exception.getSelectedIndex())));
        exceptionData.setService_id(Integer.parseInt(lbox_calendar.getValue(lbox_calendar.getSelectedIndex())));
        exceptionData.setDate(tbox_exception_date.getText());
        exceptionData.setException_type(Integer.parseInt(lbox_exception_type.getValue(lbox_exception_type.getSelectedIndex())));
        AsyncCallback callback = new AsyncCallback() {

            public void onFailure(Throwable caught) {
                GWT.log("Error", caught);
            }

            public void onSuccess(Object result) {
                lbox_exception.removeItem(lbox_exception.getSelectedIndex());
                clearException_Data(false);
                MyPopup p = new MyPopup(result.toString());
                p.setPopupPosition(bt_exception_insert.getAbsoluteLeft() - 125, bt_exception_insert.getAbsoluteTop() - 150);
                p.show();
            }
        };
        serviceProxy.deleteException_Data(exceptionData, callback);
    }

    private void clearTrip_Data(boolean all) {
        if (all) {
            lbox_trip.setItemSelected(0, true);
        }
        lbox_route.setItemSelected(0, true);
        lbox_date.setItemSelected(0, true);
        tbox_headsign.setText("");
        lbox_direction.setItemSelected(0, true);
        tbox_block.setText("");
        lbox_shape.setItemSelected(0, true);
    }

    private void clearFrequency_Data(boolean all) {
        if (all) {
            lbox_frequency.clear();
        }
        tbox_start.setText("");
        tbox_end.setText("");
        tbox_headway.setText("");
    }

    private void clearCalendar_Data(boolean all) {
        if (all) {
            lbox_calendar.setItemSelected(0, true);
        }
        tbox_calendar_name.setText("");
        tbox_calendar_start.setText("");
        tbox_calendar_end.setText("");
        ckbox_monday.setChecked(false);
        ckbox_tuesday.setChecked(false);
        ckbox_wednesday.setChecked(false);
        ckbox_thursday.setChecked(false);
        ckbox_friday.setChecked(false);
        ckbox_saturday.setChecked(false);
        ckbox_sunday.setChecked(false);
    }

    private void clearException_Data(boolean all) {
        if (all) {
            lbox_exception.clear();
        }
        lbox_exception_type.setItemSelected(0, true);
        tbox_exception_date.setText("");
    }

    private void getTrip_Data() {
    }

    private void getFrequency_Data() {
    }

    private void getCalendar_Data() {
        Calendar_ServerServiceAsync serviceProxy = (Calendar_ServerServiceAsync) GWT.create(Calendar_ServerService.class);
        ServiceDefTarget target = (ServiceDefTarget) serviceProxy;
        target.setServiceEntryPoint(GWT.getModuleBaseURL() + "calendar_server");
        AsyncCallback callback = new AsyncCallback() {

            public void onFailure(Throwable caught) {
                GWT.log("Error", caught);
            }

            public void onSuccess(Object result) {
                calendarData = (Calendar_Data) result;
                tbox_calendar_name.setText(calendarData.getCalendar_name());
                tbox_calendar_start.setText(calendarData.getStart_date());
                tbox_calendar_end.setText(calendarData.getEnd_date());
                ckbox_monday.setChecked(calendarData.isMonday());
                ckbox_tuesday.setChecked(calendarData.isTuesday());
                ckbox_wednesday.setChecked(calendarData.isWednesday());
                ckbox_thursday.setChecked(calendarData.isThursday());
                ckbox_friday.setChecked(calendarData.isFriday());
                ckbox_saturday.setChecked(calendarData.isSaturday());
                ckbox_sunday.setChecked(calendarData.isSunday());
                clearException_Data(true);
                listException(calendarData.getService_id());
            }
        };
        serviceProxy.getCalendar_Data(Integer.parseInt(lbox_calendar.getValue(lbox_calendar.getSelectedIndex())), callback);
    }

    private void getException_Data() {
        Calendar_ServerServiceAsync serviceProxy = (Calendar_ServerServiceAsync) GWT.create(Calendar_ServerService.class);
        ServiceDefTarget target = (ServiceDefTarget) serviceProxy;
        target.setServiceEntryPoint(GWT.getModuleBaseURL() + "calendar_server");
        AsyncCallback callback = new AsyncCallback() {

            public void onFailure(Throwable caught) {
                GWT.log("Error", caught);
            }

            public void onSuccess(Object result) {
                exceptionData = (Exception_Data) result;
                for (int i = 1; i < lbox_exception_type.getItemCount(); i++) {
                    if (exceptionData.getException_type() == Integer.parseInt(lbox_exception_type.getValue(i))) {
                        lbox_exception_type.setItemSelected(i, true);
                        break;
                    }
                }
                tbox_exception_date.setText(String.valueOf(exceptionData.getDate()));
            }
        };
        serviceProxy.getException_Data(Integer.parseInt(lbox_exception.getValue(lbox_exception.getSelectedIndex())), callback);
    }

    private void visibleAllFrequencyFields(boolean bool) {
        lbox_frequency.setVisible(bool);
        tbox_start.setVisible(bool);
        tbox_end.setVisible(bool);
        tbox_headway.setVisible(bool);
        bt_frequency_insert.setVisible(bool);
        bt_frequency_update.setVisible(bool);
        bt_frequency_delete.setVisible(bool);
        bt_frequency_cancel.setVisible(bool);
    }

    private void visibleAllExceptionFields(boolean bool) {
        ckbox_exception.setChecked(bool);
        lbox_exception.setVisible(bool);
        lbox_exception_type.setVisible(bool);
        tbox_exception_date.setVisible(bool);
        bt_exception_insert.setVisible(bool);
        bt_exception_update.setVisible(bool);
        bt_exception_delete.setVisible(bool);
        bt_exception_cancel.setVisible(bool);
    }

    protected static class MyPopup extends PopupPanel {

        public MyPopup(String x) {
            super(true);
            HTML contents = new HTML();
            if (x.compareTo("label_list") == 0) {
                contents.setHTML("label_list");
            } else if (x.compareTo("label_route") == 0) {
                contents.setHTML("label_route");
            } else if (x.compareTo("label_date") == 0) {
                contents.setHTML("label_date");
            } else if (x.compareTo("label_headsign") == 0) {
                contents.setHTML("label_headsign");
            } else if (x.compareTo("label_direction") == 0) {
                contents.setHTML("label_direction");
            } else if (x.compareTo("label_block") == 0) {
                contents.setHTML("label_block");
            } else if (x.compareTo("label_shape") == 0) {
                contents.setHTML("label_shape");
            } else {
                contents.setHTML(x);
                contents.setHeight("150px");
            }
            contents.setWidth("250px");
            setWidget(contents);
            setStyleName("ks-popups-Popup");
        }
    }

    public TabInfo getTabInfo() {
        return init();
    }
}
