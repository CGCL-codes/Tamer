package net.sourceforge.buildprocess.webautodeploy.client;

import net.sourceforge.buildprocess.autodeploy.model.Agent;
import net.sourceforge.buildprocess.autodeploy.model.AutoDeploy;
import net.sourceforge.buildprocess.autodeploy.model.Environment;
import net.sourceforge.buildprocess.autodeploy.webservices.client.EnvironmentWebServiceClient;
import net.sourceforge.buildprocess.webautodeploy.configuration.WebAutoDeployConfigurationLoader;
import nextapp.echo2.app.Button;
import nextapp.echo2.app.Column;
import nextapp.echo2.app.ContentPane;
import nextapp.echo2.app.Label;
import nextapp.echo2.app.Row;
import nextapp.echo2.app.event.ActionEvent;
import nextapp.echo2.app.event.ActionListener;

class UpdateThread extends Thread {

    private Environment environment;

    private boolean completed = false;

    private boolean failure = false;

    private String errorMessage;

    public UpdateThread(Environment environment) {
        this.environment = environment;
    }

    public boolean getCompleted() {
        return this.completed;
    }

    public boolean getFailure() {
        return this.failure;
    }

    public String getErrorMessage() {
        return this.errorMessage;
    }

    public void run() {
        try {
            AutoDeploy autoDeploy = WebAutoDeployConfigurationLoader.loadAutoDeploy();
            Agent agent = autoDeploy.getAgent(environment.getAgent());
            if (agent == null) {
                errorMessage = "AutoDeploy Agent not found";
                completed = true;
                failure = true;
                return;
            }
            EnvironmentWebServiceClient webServiceClient = new EnvironmentWebServiceClient(agent.getHostname(), agent.getPort());
            webServiceClient.update(environment.getName());
            completed = true;
        } catch (Exception e) {
            errorMessage = e.getMessage();
            completed = true;
            failure = true;
        }
    }
}

/**
 * Environment actions tab <code>ContentPane</code>
 * 
 * @author <a href="mailto:jb@nanthrax.net">Jean-Baptiste Onofr�</a>
 */
public class EnvironmentActionsTabPane extends ContentPane {

    /**
    * 
    */
    private static final long serialVersionUID = -6346338727655055949L;

    private EnvironmentWindow parent;

    private ActionListener updateButtonActionListener = new ActionListener() {

        /**
       * 
       */
        private static final long serialVersionUID = 2981453704605941513L;

        public void actionPerformed(ActionEvent event) {
            if (!parent.getEnvironment().getLock().equals(WebAutoDeployApplication.getApplication().getUserid())) {
                parent.getContentPane().add(new ErrorWindow(Messages.getString("EnvironmentWindow.Error"), Messages.getString("EnvironmentWindow.Error.Locked")));
                return;
            }
            if (parent.getUpdated()) {
                parent.getContentPane().add(new ErrorWindow(Messages.getString("EnvironmentWindow.TabPane.Actions.Error"), Messages.getString("EnvironmentWindow.TabPane.Actions.Error.NotSaved")));
                return;
            }
            Row updateMessage = new Row();
            Label greenLabel = new Label(Styles.ICON_18_LAUNCH);
            greenLabel.setStyleName("Small");
            updateMessage.add(greenLabel);
            Label updateLabel = new Label(parent.getEnvironmentName() + " update");
            updateLabel.setToolTipText("The " + parent.getEnvironmentName() + " update is launched ...");
            updateLabel.setStyleName("Small");
            updateMessage.add(updateLabel);
            parent.getAccordionPane().getActionEventsColumn().add(updateMessage);
            final UpdateThread updateThread = new UpdateThread(parent.getEnvironment());
            updateThread.start();
            WebAutoDeployApplication.getApplication().enqueueTask(WebAutoDeployApplication.getApplication().getTaskQueue(), new Runnable() {

                public void run() {
                    if (updateThread.getCompleted()) {
                        if (updateThread.getFailure()) {
                            Row errorMessage = new Row();
                            Label redLabel = new Label(Styles.ICON_18_ERROR);
                            redLabel.setStyleName("Small");
                            errorMessage.add(redLabel);
                            Label errorLabel = new Label(parent.getEnvironmentName() + " update error");
                            errorLabel.setToolTipText(updateThread.getErrorMessage());
                            errorLabel.setStyleName("Small");
                            errorMessage.add(errorLabel);
                            parent.getAccordionPane().getActionEventsColumn().add(errorMessage);
                        } else {
                            Row infoMessage = new Row();
                            Label greenLabel = new Label(Styles.ICON_18_LAUNCH);
                            greenLabel.setStyleName("Small");
                            infoMessage.add(greenLabel);
                            Label infoLabel = new Label(parent.getEnvironmentName() + " update completed");
                            infoLabel.setToolTipText("Completed successfully");
                            infoLabel.setStyleName("Small");
                            infoMessage.add(infoLabel);
                            parent.getAccordionPane().getActionEventsColumn().add(infoMessage);
                        }
                    } else {
                        WebAutoDeployApplication.getApplication().enqueueTask(WebAutoDeployApplication.getApplication().getTaskQueue(), this);
                    }
                }
            });
        }
    };

    private ActionListener publishReleaseButtonActionListener = new ActionListener() {

        /**
       * 
       */
        private static final long serialVersionUID = 6111885352229216769L;

        public void actionPerformed(ActionEvent event) {
            if (!parent.getEnvironment().getLock().equals(WebAutoDeployApplication.getApplication().getUserid())) {
                parent.getContentPane().add(new ErrorWindow(Messages.getString("EnvironmentWindow.Error"), Messages.getString("EnvironmentWindow.Error.Locked")));
                return;
            }
            if (parent.getUpdated()) {
                parent.getContentPane().add(new ErrorWindow(Messages.getString("EnvironmentWindow.TabPane.Actions.Error"), Messages.getString("EnvironmentWindow.TabPane.Actions.Error.NotSaved")));
                return;
            }
            if (parent.getContentPane().getComponent("environmentPublishReleaseWindow_" + parent.getEnvironmentName()) == null) {
                parent.getContentPane().add(new EnvironmentPublishReleaseWindow(parent));
            }
        }
    };

    private ActionListener publishHomePageButtonActionListener = new ActionListener() {

        /**
       * 
       */
        private static final long serialVersionUID = 2237160599980457943L;

        public void actionPerformed(ActionEvent event) {
            if (!parent.getEnvironment().getLock().equals(WebAutoDeployApplication.getApplication().getUserid())) {
                parent.getContentPane().add(new ErrorWindow(Messages.getString("EnvironmentWindow.Error"), Messages.getString("EnvironmentWindow.Error.Locked")));
                return;
            }
            if (parent.getUpdated()) {
                parent.getContentPane().add(new ErrorWindow(Messages.getString("EnvironmentWindow.TabPane.Actions.Error"), Messages.getString("EnvironmentWindow.TabPane.Actions.Error.NotSaved")));
                return;
            }
            if (parent.getContentPane().getComponent("environmentHomePageWindow_" + parent.getEnvironmentName()) == null) {
                parent.getContentPane().add(new EnvironmentHomePageWindow(parent));
            }
        }
    };

    /**
    * Create a new <code>EnvironmentActionsTabPane</code>
    * 
    * @author parent the parent <code>EnvironmentWindow</code>
    */
    public EnvironmentActionsTabPane(EnvironmentWindow parent) {
        super();
        setStyleName("EnvironmentWindow.TabPane");
        this.parent = parent;
        Column columnLayout = new Column();
        columnLayout.setStyleName("EnvironmentWindow.TabPane.Actions.ColumnLayout");
        add(columnLayout);
        Button updateButton = new Button(Messages.getString("EnvironmentWindow.TabPane.Actions.UpdateButton"), Styles.ICON_24_ACTION);
        updateButton.setStyleName("ControlPane.Button");
        updateButton.addActionListener(updateButtonActionListener);
        columnLayout.add(updateButton);
        Button publishReleaseButton = new Button(Messages.getString("EnvironmentWindow.TabPane.Actions.PublishReleaseButton"), Styles.ICON_24_ACTION);
        publishReleaseButton.setStyleName("ControlPane.Button");
        publishReleaseButton.addActionListener(publishReleaseButtonActionListener);
        columnLayout.add(publishReleaseButton);
        Button publishHomePageButton = new Button(Messages.getString("EnvironmentWindow.TabPane.Actions.PublishHomePageButton"), Styles.ICON_24_ACTION);
        publishHomePageButton.setStyleName("ControlPane.Button");
        publishHomePageButton.addActionListener(publishHomePageButtonActionListener);
        columnLayout.add(publishHomePageButton);
    }

    /**
    * Update the pane
    */
    public void update() {
    }
}
