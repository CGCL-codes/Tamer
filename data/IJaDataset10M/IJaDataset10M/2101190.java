package ucalgary.ebe.webui.client.ui;

import ucalgary.ebe.webui.client.WebUI2ServiceConnection;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.Widget;

public class WebUIMenu extends Grid {

    private WebUI2ServiceConnection con;

    public WebUIMenu(WebUI2ServiceConnection con) {
        super(1, 7);
        this.con = con;
        this.createMenu();
        this.addStyleName("webui-Menu");
        this.setBorderWidth(1);
    }

    /**
	 * Creates the left side menu
	 */
    private void createMenu() {
        ImageButton refresh_button = new ImageButton("images/refresh.gif", "Refresh");
        refresh_button.addClickListener(new ClickListener() {

            public void onClick(Widget arg0) {
                getConnection().loadProject(getConnection().getProject().getName());
            }
        });
        this.setWidget(0, 0, refresh_button);
        ImageButton create_backlog_button = new ImageButton("images/product_backlog.gif", "Backlog");
        create_backlog_button.addClickListener(new ClickListener() {

            public void onClick(Widget sender) {
                getConnection().createBacklog();
            }
        });
        this.setWidget(0, 1, create_backlog_button);
        ImageButton create_iteration_button = new ImageButton("images/iteration.gif", "Iteration");
        create_iteration_button.addClickListener(new ClickListener() {

            public void onClick(Widget sender) {
                try {
                    CreateIterationDialogBox db = new CreateIterationDialogBox(getConnection());
                    db.show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        this.setWidget(0, 2, create_iteration_button);
        ImageButton create_storycard_button = new ImageButton("images/new_story.gif", "Storycard");
        create_storycard_button.addClickListener(new ClickListener() {

            public void onClick(Widget sender) {
                try {
                    CreateStoryCardDialogBox db = new CreateStoryCardDialogBox(getConnection());
                    db.show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        this.setWidget(0, 3, create_storycard_button);
        ImageButton create_project_button = new ImageButton("images/new_project.gif", "Project");
        create_project_button.addClickListener(new ClickListener() {

            public void onClick(Widget sender) {
                Window.alert("Sorry, that function is not available yet");
            }
        });
        this.setWidget(0, 4, create_project_button);
    }

    public WebUI2ServiceConnection getConnection() {
        return this.con;
    }
}
