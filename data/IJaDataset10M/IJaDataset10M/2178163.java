package com.ixora.jobs.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.JPanel;
import com.ixora.jobs.JobData;
import com.ixora.jobs.JobDefinition;
import com.ixora.jobs.JobsComponent;
import com.ixora.jobs.exception.CantBuildJobData;
import com.ixora.jobs.exception.CantBuildJobDefinition;
import com.ixora.jobs.impl.command.ExternalCommandJobData;
import com.ixora.jobs.impl.command.ExternalCommandJobDefinitionPanel;
import com.ixora.jobs.impl.commandtelnet.TelnetCommandJobData;
import com.ixora.jobs.impl.commandtelnet.TelnetCommandJobDefinitionPanel;
import com.ixora.jobs.library.JobLibraryDefinition;
import com.ixora.jobs.ui.messages.Msg;
import com.ixora.common.MessageRepository;
import com.ixora.common.ui.UIFactoryMgr;

/**
 * @author Daniel Moraru
 */
public final class JobDefinitionPanelContainer extends JPanel {

    private static final long serialVersionUID = -2272622920725100813L;

    /** Current job defintion panel */
    private JobDefinitionPanel current;

    /** Available hosts */
    private String[] hosts;

    /** True if editing for library */
    private boolean forLibrary;

    /** Whether or not in view mode */
    private boolean viewMode;

    /**
     * Constructor.
     * @param hosts
     * @param viewMode
     */
    public JobDefinitionPanelContainer(String[] hosts, boolean forLibrary, boolean viewMode) {
        super(new BorderLayout());
        this.hosts = hosts;
        this.forLibrary = forLibrary;
        this.viewMode = viewMode;
        setBorder(UIFactoryMgr.createTitledBorder(MessageRepository.get(JobsComponent.NAME, Msg.TEXT_JOB_DEFINITION)));
        setPreferredSize(new Dimension(400, 300));
    }

    /**
     * @param jobType
     */
    public void showJobDefinitionPanelFor(String jobType) {
        if (this.current != null) {
            remove(this.current);
            this.current = null;
        }
        if (jobType.equals(com.ixora.jobs.impl.command.messages.Msg.TEXT_JOB_EXTERNAL_COMMAND_NAME)) {
            this.current = new ExternalCommandJobDefinitionPanel(hosts, forLibrary, viewMode);
        } else if (jobType.equals(com.ixora.jobs.impl.commandtelnet.messages.Msg.TEXT_JOB_TELNET_EXTERNAL_COMMAND_NAME)) {
            this.current = new TelnetCommandJobDefinitionPanel(hosts, forLibrary, viewMode);
        }
        if (this.current != null) {
            add(this.current, BorderLayout.CENTER);
        }
        validate();
        repaint();
    }

    /**
     * @return the job definition or null
     * @throws CantBuildJobDefinition
     * @throws CantBuildJobData
     */
    public JobDefinition getJobDefinition() throws CantBuildJobDefinition, CantBuildJobData {
        if (this.current == null) {
            return null;
        }
        return this.current.getJobDefinition();
    }

    /**
	 * @return
     * @throws CantBuildJobData
     * @throws CantBuildJobDefinition
	 */
    public JobLibraryDefinition getJobLibraryDefinition() throws CantBuildJobDefinition, CantBuildJobData {
        if (this.current == null) {
            return null;
        }
        return this.current.getJobLibraryDefinition();
    }

    /**
     * Sets the job definition to edit
     * @param def
     */
    public void setJobDefinition(JobDefinition def) {
        JobData jd = def.getJobData();
        if (jd == null) {
            return;
        }
        updateJobDefPanelForJobData(jd);
        if (this.current != null) {
            this.current.setJobDefinition(def);
            validate();
            repaint();
        }
    }

    private void updateJobDefPanelForJobData(JobData jd) {
        if (jd instanceof ExternalCommandJobData) {
            showJobDefinitionPanelFor(com.ixora.jobs.impl.command.messages.Msg.TEXT_JOB_EXTERNAL_COMMAND_NAME);
        } else if (jd instanceof TelnetCommandJobData) {
            showJobDefinitionPanelFor(com.ixora.jobs.impl.commandtelnet.messages.Msg.TEXT_JOB_TELNET_EXTERNAL_COMMAND_NAME);
        }
    }

    /**
	 * @param def
	 */
    public void setJobDefinition(JobLibraryDefinition def) {
        JobData jd = def.getJobData();
        if (jd == null) {
            return;
        }
        updateJobDefPanelForJobData(jd);
        if (this.current != null) {
            this.current.setJobDefinition(def);
            validate();
            repaint();
        }
    }
}
