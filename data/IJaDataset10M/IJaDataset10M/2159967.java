package org.jiaho.client;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.EventListener;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.WindowCloseListener;
import com.google.gwt.user.client.ui.ChangeListener;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.MenuItem;
import com.google.gwt.user.client.ui.StackPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import java.util.Iterator;
import java.util.Vector;

/**
 *
 * @author Manuel Martins
 */
public class MainWindow extends DialogBox implements ClickListener, ChangeListener, EventListener, WindowCloseListener {

    private DockPanel dock = new DockPanel();

    private VerticalPanel vpanel = new VerticalPanel();

    private Image logImage = new Image();

    private MenuBar menu = new MenuBar();

    private ListBox list = new ListBox();

    private TextBox status = new TextBox();

    private UsersMenu umenu = new UsersMenu();

    private LoginMenu login = new LoginMenu();

    private HTML myinfo = new HTML();

    protected String conID = "";

    private Timer timer = new Timer() {

        public void run() {
            roster();
        }
    };

    private Timer msgtimer = new Timer() {

        public void run() {
            messages();
        }
    };

    public MainWindow() {
        MenuBar menubar = new MenuBar(true);
        this.setText("jiaho");
        this.dock.setSpacing(4);
        this.dock.setWidth("100%");
        this.logImage.addClickListener(this);
        this.dock.add(setLog(), dock.NORTH);
        this.dock.add(this.umenu, dock.CENTER);
        menubar.addItem("add/remove contact", new Command() {

            public void execute() {
                if (!conID.equalsIgnoreCase("")) {
                    NewContact newc = new NewContact();
                    newc.setConID(conID);
                    newc.newContactWindow();
                    newc.show();
                } else {
                    JiahoMessageBox msg = new JiahoMessageBox("not connected!", "warning", 2);
                    msg.show();
                }
            }
        });
        menubar.addItem("create MultiUserChat room", new Command() {

            public void execute() {
                Window.alert("not yet implemented!");
            }
        });
        menubar.addItem("join MultiUserChat room", new Command() {

            public void execute() {
                Window.alert("not yet implemented!");
            }
        });
        this.menu.addItem("(+) options", menubar);
        this.dock.add(this.menu, dock.SOUTH);
        Window.addWindowCloseListener(this);
        this.setWidget(this.dock);
    }

    private Widget setLog() {
        DockPanel doc = new DockPanel();
        HorizontalPanel hpanel = new HorizontalPanel();
        this.list.addChangeListener(this);
        this.list.addItem("online");
        this.list.addItem("away");
        this.list.addItem("busy");
        this.list.addItem("not here");
        hpanel.add(this.list);
        this.vpanel.add(hpanel);
        this.status.addChangeListener(this);
        this.vpanel.add(this.status);
        this.vpanel.add(this.myinfo);
        this.logImage.setUrl("style/images/meoffline.png");
        this.logImage.setSize("56", "75");
        this.logImage.setStyleName("gwt-Image");
        doc.add(this.logImage, doc.WEST);
        doc.add(this.vpanel, doc.WEST);
        return (doc);
    }

    /**
     * Sets colors from html user log and image log
     * 
     * @param state 0 - font color = green; 1 - font color = yellow; 2 - font color = red;
     * 3 - image = login waiting; 4 - image = offline; 5 - image = online
     */
    public void setState(int state) {
        if (state == 0) {
            this.myinfo.setHTML("<FONT COLOR='#008000'><B>.:" + this.getID(conID) + "</B></FONT>");
        } else if (state == 1) {
            this.myinfo.setHTML("<FONT COLOR='#FFFF00'><B>.:" + this.getID(conID) + "</B></FONT>");
        } else if (state == 2) {
            this.myinfo.setHTML("<FONT COLOR='#FF0000'><B>.:" + this.getID(conID) + "</B></FONT>");
        } else if (state == 3) {
            this.logImage.setSize("32", "32");
            this.logImage.setUrl("style/images/login.gif");
        } else if (state == 4) {
            this.logImage.setSize("56", "75");
            this.logImage.setUrl("style/images/meoffline.png");
        } else if (state == 5) {
            this.logImage.setSize("56", "75");
            this.logImage.setUrl("style/images/meonline.png");
        }
    }

    private String getID(String cid) {
        String[] id;
        id = cid.split("@");
        return (id[0]);
    }

    public void onClick(Widget sender) {
        if (sender == this.logImage) {
            if (login.getText().equalsIgnoreCase("")) {
                login.newLogin();
                int left = this.logImage.getAbsoluteLeft() + 247;
                int top = this.logImage.getAbsoluteTop() - 42;
                login.setPopupPosition(left, top);
                login.getBtnLogin().addClickListener(this);
                login.getBtnCreate().addClickListener(this);
                login.show();
            } else {
                login.show();
                int left = this.logImage.getAbsoluteLeft() + 247;
                int top = this.logImage.getAbsoluteTop() - 42;
                login.setPopupPosition(left, top);
            }
        }
        if (sender == login.getBtnCreate()) {
            if ((!login.getUsername().getText().equalsIgnoreCase("")) && (!login.getPassword().getText().equalsIgnoreCase(""))) {
                this.setState(3);
                int port = Integer.parseInt(login.getPort().getText());
                this.getService().connect(login.getUsername().getText(), login.getPassword().getText(), login.getServer().getText(), port, true, new AsyncCallback() {

                    public void onSuccess(Object result) {
                        String i = (String) result;
                        conID = i;
                        if (!i.equalsIgnoreCase("")) {
                            JiahoMessageBox msg = new JiahoMessageBox("success! connection ID : " + conID, "info", 1);
                            msg.show();
                            setState(0);
                            setState(5);
                            login.allDisable();
                            timer.scheduleRepeating(7000);
                            msgtimer.scheduleRepeating(2000);
                            umenu.setConID(conID);
                        } else {
                            JiahoMessageBox msg = new JiahoMessageBox("connection error! username already exists! ", "error", 0);
                            msg.show();
                            setState(4);
                        }
                    }

                    public void onFailure(Throwable caught) {
                        JiahoMessageBox msg = new JiahoMessageBox("connection error! server down!", "error", 0);
                        msg.show();
                        setState(4);
                    }
                });
            } else {
                JiahoMessageBox msg = new JiahoMessageBox("no username or password!", "warning", 2);
                msg.show();
                setState(4);
            }
        }
        if (sender == login.getBtnLogin()) {
            if ((login.getBtnLogin().getText().equalsIgnoreCase("login")) && (!login.getUsername().getText().equalsIgnoreCase("")) && (!login.getPassword().getText().equalsIgnoreCase(""))) {
                this.setState(3);
                int port = Integer.parseInt(login.getPort().getText());
                this.getService().connect(login.getUsername().getText(), login.getPassword().getText(), login.getServer().getText(), port, false, new AsyncCallback() {

                    public void onSuccess(Object result) {
                        String i = (String) result;
                        conID = i;
                        if (!i.equalsIgnoreCase("")) {
                            JiahoMessageBox msg = new JiahoMessageBox("success! connection ID : " + conID, "info", 1);
                            msg.show();
                            setState(0);
                            setState(5);
                            login.allDisable();
                            timer.scheduleRepeating(7000);
                            msgtimer.scheduleRepeating(2000);
                            umenu.setConID(conID);
                        } else {
                            JiahoMessageBox msg = new JiahoMessageBox("connection error! wrong password? ", "error", 0);
                            msg.show();
                            setState(4);
                        }
                    }

                    public void onFailure(Throwable caught) {
                        JiahoMessageBox msg = new JiahoMessageBox("connection error! server down!", "error", 0);
                        msg.show();
                        setState(4);
                    }
                });
            } else if (login.getBtnLogin().getText().equalsIgnoreCase("logout")) {
                this.setState(3);
                this.getService().disconnect(conID, new AsyncCallback() {

                    public void onSuccess(Object result) {
                        Boolean success = (Boolean) result;
                        if (success.booleanValue()) {
                            JiahoMessageBox msg = new JiahoMessageBox("successfully disconnected!", "info", 1);
                            msg.show();
                            timer.cancel();
                            msgtimer.cancel();
                            login.allEnabled();
                            myinfo.setHTML("");
                            setState(4);
                            umenu.fillTree(new Vector());
                        } else {
                            JiahoMessageBox msg = new JiahoMessageBox("connection error! cannot disconnect! connection already closed?", "error", 0);
                            msg.show();
                            setState(4);
                        }
                    }

                    public void onFailure(Throwable caught) {
                        JiahoMessageBox msg = new JiahoMessageBox("connection error! server down!", "error", 0);
                        msg.show();
                        setState(4);
                    }
                });
            } else {
                JiahoMessageBox msg = new JiahoMessageBox("no username or passord!", "warning", 2);
                msg.show();
                setState(4);
            }
        }
    }

    private void isclose() {
        this.getService().isClose(conID, new AsyncCallback() {

            public void onSuccess(Object result) {
                Boolean isclose = (Boolean) result;
                if (isclose.booleanValue()) {
                    JiahoMessageBox msg = new JiahoMessageBox("connection error! disconnected! reconnecting...", "warning", 2);
                    msg.show();
                    timer.cancel();
                    login.allEnabled();
                }
            }

            public void onFailure(Throwable caught) {
            }
        });
    }

    private void roster() {
        this.getService().getRoster(conID, new AsyncCallback() {

            public void onSuccess(Object result) {
                Vector roster = (Vector) result;
                umenu.fillTree(roster);
            }

            public void onFailure(Throwable caught) {
            }
        });
    }

    private void messages() {
        this.getService().getChatMessages(conID, new AsyncCallback() {

            public void onSuccess(Object result) {
                Vector messagelist = (Vector) result;
                umenu.setMessages(messagelist);
            }

            public void onFailure(Throwable caught) {
            }
        });
    }

    public static jiahoServerServiceAsync getService() {
        jiahoServerServiceAsync service = (jiahoServerServiceAsync) GWT.create(jiahoServerService.class);
        ServiceDefTarget endpoint = (ServiceDefTarget) service;
        String moduleRelativeURL = GWT.getModuleBaseURL() + "jiahoserverservice";
        endpoint.setServiceEntryPoint(moduleRelativeURL);
        return service;
    }

    public void onChange(Widget sender) {
        if ((sender == this.list) || (sender == this.status)) {
            String type = null;
            if (this.list.getValue(this.list.getSelectedIndex()).equalsIgnoreCase("away")) {
                type = "away";
                this.setState(1);
            } else if (this.list.getValue(this.list.getSelectedIndex()).equalsIgnoreCase("busy")) {
                type = "dnd";
                this.setState(2);
            } else if ((this.list.getValue(this.list.getSelectedIndex()).equalsIgnoreCase("not here")) || (this.list.getValue(this.list.getSelectedIndex()).equalsIgnoreCase("offline"))) {
                type = "xa";
                setState(1);
            } else {
                type = "available";
                this.setState(0);
            }
            this.getService().sendPresence(this.conID, type, this.status.getText(), new AsyncCallback() {

                public void onSuccess(Object result) {
                }

                public void onFailure(Throwable caught) {
                    JiahoMessageBox msg = new JiahoMessageBox("connection error! server down!", "error", 0);
                    msg.show();
                }
            });
        }
    }

    public String onWindowClosing() {
        return ("");
    }

    public void onWindowClosed() {
        this.getService().disconnect(conID, new AsyncCallback() {

            public void onSuccess(Object result) {
            }

            public void onFailure(Throwable caught) {
            }
        });
        Window.alert("thank you for using jiaho!");
    }
}
