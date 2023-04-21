package com.sfeir.client.activity;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;
import com.sfeir.modding.client.app.Activity;
import com.sfeir.modding.client.content.Intent;
import com.sfeir.modding.client.content.ModdingAction;

@ModdingAction(defaultActivity = true, value = "hive")
public class Activity1 extends Activity {

    ListBox listBox = new ListBox();

    MenuBar menuBar = new MenuBar();

    interface Activity1UiBinder extends UiBinder<Widget, Activity1> {
    }

    interface Activity1CSS extends CssResource {

        String menu();
    }

    @UiField
    protected Activity1CSS style;

    private static Activity1UiBinder uiBinder = GWT.create(Activity1UiBinder.class);

    public Activity1() {
        setContentView(uiBinder.createAndBindUi(this));
    }

    public void displayData() {
        UIObject.setVisible(DOM.getElementById("loadingdiv"), false);
        initMenu();
        RootPanel.get("menu").add(menuBar);
    }

    public String getName() {
        return "Activite1";
    }

    public void initMenu() {
        menuBar.addStyleName(style.menu());
        menuBar.addItem("Acceuil", new Command() {

            @Override
            public void execute() {
                RootPanel.get("menu").clear();
                Intent intent = new Intent(Activity1.class);
                startActivity(intent);
            }
        });
        menuBar.addItem("List", new Command() {

            @Override
            public void execute() {
                Intent intent = new Intent(Activity2.class);
                startActivity(intent);
            }
        });
    }
}
