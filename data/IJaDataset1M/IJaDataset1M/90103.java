package net.sourceforge.buildprocess.webautodeploy.client;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import net.sourceforge.buildprocess.autodeploy.model.Agent;
import net.sourceforge.buildprocess.autodeploy.model.AutoDeploy;
import net.sourceforge.buildprocess.webautodeploy.configuration.WebAutoDeployConfigurationLoader;
import nextapp.echo2.app.Button;
import nextapp.echo2.app.Column;
import nextapp.echo2.app.Extent;
import nextapp.echo2.app.Grid;
import nextapp.echo2.app.Label;
import nextapp.echo2.app.Row;
import nextapp.echo2.app.SplitPane;
import nextapp.echo2.app.TextField;
import nextapp.echo2.app.WindowPane;
import nextapp.echo2.app.event.ActionEvent;
import nextapp.echo2.app.event.ActionListener;

/**
 * Admin agents <code>WindowPane</code>
 * 
 * @author <a href="mailto:jb@nanthrax.net">Jean-Baptiste Onofr�</a>
 */
public class AdminAgentsWindow extends WindowPane {

    /**
    * Generated serial UID
    */
    private static final long serialVersionUID = -8580638381627048035L;

    private List agents;

    private Grid agentsGrid;

    private ActionListener closeButtonActionListener = new ActionListener() {

        /**
       * Generated Serial Version UID
       */
        private static final long serialVersionUID = 1955023483598866137L;

        public void actionPerformed(ActionEvent event) {
            AdminAgentsWindow.this.userClose();
        }
    };

    private ActionListener refreshButtonActionListener = new ActionListener() {

        /**
       * Generated Serial Version UID
       */
        private static final long serialVersionUID = -162377722231754735L;

        public void actionPerformed(ActionEvent event) {
            AutoDeploy autoDeploy = null;
            try {
                autoDeploy = WebAutoDeployConfigurationLoader.loadAutoDeploy();
            } catch (Exception e) {
                WebAutoDeployApplication.getApplication().getDefaultWindow().getContent().add(new ErrorWindow(Messages.getString("ErrorWindow.AutoDeployError.Parsing"), e.getMessage()));
                return;
            }
            agents = autoDeploy.getAgents();
            update();
        }
    };

    private ActionListener deleteAgentButtonActionListener = new ActionListener() {

        /**
       * Generated Serial Version UID
       */
        private static final long serialVersionUID = 5724297341468335754L;

        public void actionPerformed(ActionEvent event) {
            Agent agentToRemove = null;
            for (Iterator agentIterator = agents.iterator(); agentIterator.hasNext(); ) {
                Agent agent = (Agent) agentIterator.next();
                if (agent.getId().equals(event.getActionCommand())) {
                    agentToRemove = agent;
                    break;
                }
            }
            agents.remove(agentToRemove);
            update();
        }
    };

    private ActionListener saveButtonActionListener = new ActionListener() {

        /**
       * Generated Serial Version UID
       */
        private static final long serialVersionUID = -1227964971379830779L;

        public void actionPerformed(ActionEvent event) {
            AutoDeploy autoDeploy = null;
            try {
                autoDeploy = WebAutoDeployConfigurationLoader.loadAutoDeploy();
            } catch (Exception e) {
                WebAutoDeployApplication.getApplication().getDefaultWindow().getContent().add(new ErrorWindow(Messages.getString("ErrorWindow.AutoDeployError.Parsing"), e.getMessage()));
                return;
            }
            autoDeploy.setAgents((LinkedList) agents);
            try {
                autoDeploy.writeXMLFile(WebAutoDeployConfigurationLoader.getAutoDeployConfigurationFile());
            } catch (Exception e) {
                WebAutoDeployApplication.getApplication().getDefaultWindow().getContent().add(new ErrorWindow(Messages.getString("ErrorWindow.AutoDeployError.Writing"), e.getMessage()));
                return;
            }
            WebAutoDeployApplication.getApplication().getDefaultWindow().getContent().add(new InfoWindow(Messages.getString("InfoWindow.ConfigurationSaved")));
        }
    };

    private ActionListener viewAgentButtonActionListener = new ActionListener() {

        /**
       * Generated Serial Version UID
       */
        private static final long serialVersionUID = 6870122410628452728L;

        public void actionPerformed(ActionEvent event) {
            if (WebAutoDeployApplication.getApplication().getDefaultWindow().getComponent("adminAgentWindow_" + event.getActionCommand()) == null) {
                WebAutoDeployApplication.getApplication().getDefaultWindow().getContent().add(new AdminAgentWindow(AdminAgentsWindow.this, event.getActionCommand()));
            }
        }
    };

    private ActionListener createAgentButtonActionListener = new ActionListener() {

        /**
       * Generated Serial Version UID
       */
        private static final long serialVersionUID = -4290759454037325116L;

        public void actionPerformed(ActionEvent event) {
            WebAutoDeployApplication.getApplication().getDefaultWindow().getContent().add(new AdminAgentWindow(AdminAgentsWindow.this, null));
        }
    };

    private ActionListener copyAgentButtonActionListener = new ActionListener() {

        /**
       * Generated Serial Version UID
       */
        private static final long serialVersionUID = -5874892800818193187L;

        public void actionPerformed(ActionEvent event) {
        }
    };

    /**
    * Create a new <code>WindowPane</code>
    */
    public AdminAgentsWindow() {
        super();
        if (!WebAutoDeployApplication.getApplication().getUserid().equals("admin")) {
            WebAutoDeployApplication.getApplication().getDefaultWindow().getContent().add(new ErrorWindow(Messages.getString("ErrorWindow.AccessError"), Messages.getString("ErrorWindow.AccessError.AdminRestricted")));
            return;
        }
        AutoDeploy autoDeploy = null;
        try {
            autoDeploy = WebAutoDeployConfigurationLoader.loadAutoDeploy();
        } catch (Exception e) {
            WebAutoDeployApplication.getApplication().getDefaultWindow().getContent().add(new ErrorWindow(Messages.getString("ErrorWindow.AutoDeployError.Parsing"), e.getMessage()));
            return;
        }
        this.agents = autoDeploy.getAgents();
        setTitle(Messages.getString("AdminAgentsWindow.Title"));
        setStyleName("AdminAgentsWindow");
        setId("adminAgentsWindow");
        setModal(false);
        setDefaultCloseOperation(WindowPane.DISPOSE_ON_CLOSE);
        SplitPane splitPane = new SplitPane(SplitPane.ORIENTATION_VERTICAL_BOTTOM_TOP, new Extent(32));
        add(splitPane);
        Row controlRow = new Row();
        controlRow.setStyleName("ControlPane");
        splitPane.add(controlRow);
        Button refreshButton = new Button(Messages.getString("Window.Refresh"), Styles.ICON_24_REFRESH);
        refreshButton.setStyleName("ControlPane.Button");
        refreshButton.addActionListener(refreshButtonActionListener);
        controlRow.add(refreshButton);
        Button saveButton = new Button(Messages.getString("Window.Save"), Styles.ICON_24_YES);
        saveButton.setStyleName("ControlPane.Button");
        saveButton.addActionListener(saveButtonActionListener);
        controlRow.add(saveButton);
        Button closeButton = new Button(Messages.getString("Window.Close"), Styles.ICON_24_EXIT);
        closeButton.setStyleName("ControlPane.Button");
        closeButton.addActionListener(closeButtonActionListener);
        controlRow.add(closeButton);
        Column content = new Column();
        splitPane.add(content);
        Button createAgentButton = new Button(Messages.getString("Window.Create"), Styles.ICON_24_CREATE);
        createAgentButton.setStyleName("ControlPane.Button");
        createAgentButton.setToolTipText(Messages.getString("AdminAgentsWindow.CreateAgentButtonHelp"));
        createAgentButton.addActionListener(createAgentButtonActionListener);
        content.add(createAgentButton);
        agentsGrid = new Grid(2);
        agentsGrid.setStyleName("AdminAgentsWindow.AgentsGrid");
        content.add(agentsGrid);
        update();
    }

    /**
    * 
    * 
    */
    protected void update() {
        agentsGrid.removeAll();
        Label agentIdHeaderLabel = new Label(Messages.getString("AdminAgentsWindow.AgentId"));
        agentIdHeaderLabel.setStyleName("Header");
        agentsGrid.add(agentIdHeaderLabel);
        Label blank = new Label("  ");
        blank.setStyleName("Header");
        agentsGrid.add(blank);
        for (Iterator agentIterator = agents.iterator(); agentIterator.hasNext(); ) {
            Agent agent = (Agent) agentIterator.next();
            Label agentIdLabel = new Label(agent.getId());
            agentIdLabel.setStyleName("Default");
            agentsGrid.add(agentIdLabel);
            Row actionRow = new Row();
            agentsGrid.add(actionRow);
            Button copyAgentButton = new Button(Styles.ICON_24_COPY);
            copyAgentButton.setStyleName("ControlPane.Button");
            copyAgentButton.setActionCommand(agent.getId());
            copyAgentButton.addActionListener(copyAgentButtonActionListener);
            actionRow.add(copyAgentButton);
            Button deleteAgentButton = new Button(Styles.ICON_24_NO);
            deleteAgentButton.setStyleName("ControlPane.Button");
            deleteAgentButton.setToolTipText(Messages.getString("AdminAgentsWindow.DeleteAgentButtonHelp"));
            deleteAgentButton.setActionCommand(agent.getId());
            deleteAgentButton.addActionListener(deleteAgentButtonActionListener);
            actionRow.add(deleteAgentButton);
            Button viewAgentButton = new Button(Styles.ICON_24_EDIT);
            viewAgentButton.setStyleName("Default");
            viewAgentButton.setActionCommand(agent.getId());
            viewAgentButton.addActionListener(viewAgentButtonActionListener);
            actionRow.add(viewAgentButton);
        }
    }

    /**
    * Get the agents list
    * 
    * @return the agents list
    */
    protected List getAgents() {
        return this.agents;
    }
}
