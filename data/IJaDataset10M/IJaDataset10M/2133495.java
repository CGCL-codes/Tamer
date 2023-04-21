package mathive.client.panels;

import mathive.client.PanelHandler;
import mathive.client.TestService;
import mathive.client.TestServiceAsync;
import mathive.client.UserHandler;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class ManageTestsPanel {

    private static final TestServiceAsync SERVICE = GWT.create(TestService.class);

    VerticalPanel mainPanel = new VerticalPanel();

    private VerticalPanel testsPanel = new VerticalPanel();

    private ScrollPanel scrollPanel = new ScrollPanel(testsPanel);

    private String[] avaibleTests;

    private Button createTestButton;

    public ManageTestsPanel() {
        scrollPanel.setSize("350px", "200px");
        createTestButton = new Button("Create new test");
        createTestButton.addClickHandler(new ClickHandler() {

            public void onClick(ClickEvent event) {
                PanelHandler.showCreateTest();
            }
        });
    }

    public Widget getPanel() {
        mainPanel.clear();
        initialize();
        return mainPanel;
    }

    public void initialize() {
        SERVICE.getMyTests(UserHandler.singleton.getSessionID(), new MyTestsResponse());
        mainPanel.add(new Label("Loading..."));
    }

    private class MyTestsResponse implements AsyncCallback<String[]> {

        @Override
        public void onFailure(Throwable caught) {
            Window.alert("The server malfunctioned. Please try again or contact admin.");
        }

        @Override
        public void onSuccess(String[] result) {
            testsPanel.clear();
            mainPanel.clear();
            mainPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
            mainPanel.add(scrollPanel);
            mainPanel.add(createTestButton);
            avaibleTests = new String[result.length];
            for (int i = 0; i < result.length; i++) {
                testsPanel.add(new MyTests(result[i], i));
            }
        }
    }

    private class MyTests extends HorizontalPanel {

        private int indexx;

        private Button statisticsButton;

        private Button deleteButton;

        public MyTests(String info, int index) {
            indexx = index;
            statisticsButton = new Button("See statistics");
            deleteButton = new Button("Delete test");
            final String name = info.substring(0, info.indexOf(' '));
            avaibleTests[index] = name;
            add(new Label(info));
            add(statisticsButton);
            add(deleteButton);
            statisticsButton.addClickHandler(new ClickHandler() {

                public void onClick(ClickEvent event) {
                    mainPanel.clear();
                    mainPanel.add(new StatisticsPanel(name));
                }
            });
            deleteButton.addClickHandler(new ClickHandler() {

                public void onClick(ClickEvent event) {
                    deleteTest(name);
                }
            });
        }
    }

    private void deleteTest(String name) {
        AsyncCallback<Boolean> callback = new AsyncCallback<Boolean>() {

            public void onFailure(Throwable caught) {
                Window.alert("The server malfunctioned. " + "The test was not deleted.");
            }

            public void onSuccess(Boolean deleted) {
                Window.alert("Test deleted.");
                initialize();
            }
        };
        SERVICE.deleteTest(name, callback);
    }
}
