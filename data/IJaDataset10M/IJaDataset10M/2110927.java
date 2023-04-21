package icm.unicore.plugins.dbaccess;

import java.io.File;
import java.util.*;
import javax.swing.ImageIcon;
import org.unicore.*;
import org.unicore.ajo.*;
import org.unicore.sets.*;
import com.pallas.unicore.client.panels.*;
import com.pallas.unicore.container.*;
import com.pallas.unicore.container.errorspec.*;
import com.pallas.unicore.extensions.*;
import com.pallas.unicore.resourcemanager.ResourceManager;

/**
 *
 *@author     Michal Wronski, based on ScriptContainer by Ralf Ratering
 */
public class ScriptContainer extends UserContainer implements IPanelProvider {

    static final long serialVersionUID = 8263949263798274357L;

    private static String[][] persistentXMLFields = { { "TASK", "task" }, { "FILENAME", "fileName" } };

    private Task task;

    private String fileName;

    private transient DBAccessOutcomePanel outcomePanel;

    public ScriptContainer(GroupContainer parentContainer) {
        super(parentContainer);
        task = new Task();
        fileName = null;
        outcomePanel = null;
        this.parentContainer = parentContainer;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    public Task getTask() {
        return task;
    }

    public ErrorSet checkContents() {
        ErrorSet err = super.checkContents();
        task.checkContents(getIdentifier(), err);
        setErrors(err);
        return err;
    }

    public String getFileName() {
        return fileName;
    }

    private String createActionName(String declarative) {
        return getName() + "_" + declarative + "_" + ResourceManager.getNextObjectIdentifier();
    }

    /**
	 *  Build ActionGroup to transfer and execute the script
	 */
    protected void buildExecuteGroup() {
        task.setAjoID(Integer.toHexString(parentContainer.getIdentifier().getValue()));
        fileName = "DBAccess" + getIdentifier().getValue();
        task.setOutput(fileName);
        String scriptFilename = "Script_" + getName() + ResourceManager.getNextObjectIdentifier();
        String[] scriptFile = { scriptFilename };
        byte[][] contents = new byte[1][];
        contents[0] = task.prepareScript().getBytes();
        String[] files = { scriptFilename };
        IncarnateFiles incarnateFiles = new IncarnateFiles(createActionName("INCARNATEFILES"), null, files, contents, true);
        MakePortfolio makePortfolio = new MakePortfolio(createActionName("MAKEPORTFOLIO"), null, scriptFile);
        ExecuteScriptTask executeScriptTask = new ExecuteScriptTask(getName(), getResourceSet().getResourceSet(), null, null, null, false, false, false, false, makePortfolio.getPortfolio(), getMeasureTime(), false, false, org.unicore.ajo.ScriptType.PERL);
        DeletePortfolio deletePortfolio = new DeletePortfolio(createActionName("DELETEPORTFOLIO"), null, makePortfolio.getPortfolio().getId());
        deletePortfolio.setPropagateSuccessfulIfNotSuccessful(true);
        executeGroup = new ActionGroup(createActionName("EXECUTION"));
        executeGroup.add(incarnateFiles);
        executeGroup.add(makePortfolio);
        executeGroup.add(executeScriptTask);
        executeGroup.add(deletePortfolio);
        try {
            executeGroup.addDependency(new Dependency(deletePortfolio, makePortfolio));
            executeGroup.addDependency(new Dependency(incarnateFiles, makePortfolio));
            executeGroup.addDependency(new Dependency(makePortfolio, executeScriptTask));
        } catch (ActionGroup.InvalidDependencyException ex) {
            System.err.println("Could not add dependency. " + ex);
        }
        if (task.isPuttingOutputToPanel()) {
            MakePortfolio resPortfolio = new MakePortfolio(createActionName("RESULTPORTFOLIO"));
            resPortfolio.addFile(task.getOutput());
            DeletePortfolio deleteResPortfolio = new DeletePortfolio(createActionName("DELETEPORTFOLIO"), null, resPortfolio.getPortfolio().getId());
            deleteResPortfolio.setPropagateSuccessfulIfNotSuccessful(true);
            CopyPortfolioToOutcome copyPortfolio = new CopyPortfolioToOutcome(createActionName("COPYPORTFOLIOTOOUTCOME"), null, resPortfolio.getPortfolio().getId());
            executeGroup.add(resPortfolio);
            executeGroup.add(copyPortfolio);
            executeGroup.add(deleteResPortfolio);
            try {
                executeGroup.addDependency(new Dependency(executeScriptTask, resPortfolio));
                executeGroup.addDependency(new Dependency(deleteResPortfolio, resPortfolio));
                executeGroup.addDependency(new Dependency(resPortfolio, copyPortfolio));
            } catch (ActionGroup.InvalidDependencyException ex) {
                System.err.println("Could not add dependency. " + ex);
            }
        }
    }

    public void finalizePanel() {
        outcomePanel = null;
    }

    public int getNrOfPanels() {
        return 1;
    }

    public javax.swing.JPanel getPanel(int param) {
        if (outcomePanel == null) outcomePanel = new DBAccessOutcomePanel(this);
        return outcomePanel;
    }

    public String getPanelTitle(int param) {
        return "DBAccess Outcome";
    }
}
