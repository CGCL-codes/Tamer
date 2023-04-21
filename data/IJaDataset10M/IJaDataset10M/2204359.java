package openfuture.bugbase.app.controller;

import java.awt.AWTEvent;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Vector;
import javax.swing.JComboBox;
import javax.swing.JTextArea;
import openfuture.bugbase.app.model.BugBaseAdminModel;
import openfuture.bugbase.app.view.DatabaseAdminView;
import openfuture.bugbase.model.TransactionResult;
import openfuture.bugbase.model.Version;
import openfuture.bugbase.servlet.BugBaseServletClient;
import openfuture.util.error.I18NException;

public class DatabaseAdminController extends AbsController {

    private String command;

    private String consoleText;

    private Version version;

    private BugBaseAdminModel adminModel;

    private LinkedList availableUpdates;

    public DatabaseAdminController(BugBaseServletClient servletClient, BugBaseAdminModel adminModel) {
        setServletClient(servletClient);
        this.adminModel = adminModel;
        availableUpdates = loadAvailableUpdates();
    }

    public void init() {
        consoleText = "";
    }

    public void handleEvents(AWTEvent event, Object source, String name) {
        if (name == "command") {
            command = ((JTextArea) source).getText();
        } else if (name == "console") {
        } else if (name == "Submit") {
            try {
                LinkedList result = getServletClient().executeQuery(command);
                Iterator it = result.iterator();
                while (it.hasNext()) {
                    addConsoleText(((LinkedList) it.next()).toString() + "\n");
                }
                getView().refreshView();
            } catch (I18NException e) {
                showError(e);
            }
        } else if (name == "Clear") {
            setConsoleText("");
            getView().refreshView();
        } else if (name == "UpdateTarget") {
            Version version = (Version) ((JComboBox) source).getSelectedItem();
            setVersion(version);
        } else if (name == "Update") {
            Version version = getVersion();
            if (version != null) {
                try {
                    TransactionResult result = getServletClient().updatePersistency(version);
                    LinkedList output = result.getOutput();
                    LinkedList warnings = result.getWarnings();
                    if (result.getStatus() == TransactionResult.SUCCESS) {
                        addConsoleText("*** Update succeeded. ***\n\n");
                    } else {
                        addConsoleText("!!! Update failed. !!!\n\n");
                    }
                    if (output != null && !output.isEmpty()) {
                        addConsoleText("Output: ");
                        addConsoleText(output);
                    }
                    if (warnings != null && !warnings.isEmpty()) {
                        addConsoleText("Warnings: ");
                        addConsoleText(warnings);
                    }
                    if (result.getStatus() == TransactionResult.SUCCESS) {
                        addConsoleText("*** Update succeeded. ***\n\n");
                    } else {
                        addConsoleText("!!! Update failed. !!!\n\n");
                    }
                    ((DatabaseAdminView) getView()).reloadView();
                } catch (I18NException e) {
                    showError(e);
                }
            }
        }
    }

    /**
     * Get the value of consoleText.
     * @return Value of consoleText.
     */
    public String getConsoleText() {
        return consoleText;
    }

    /**
     * Set the value of consoleText.
     * @param v  Value to assign to consoleText.
     */
    public void setConsoleText(String v) {
        this.consoleText = v;
    }

    /**
     * Add a string to the console area
     *
     * @param v a value of type 'String'
     */
    public void addConsoleText(String v) {
        this.consoleText += v;
    }

    /**
     * Add a list of strings to the console area
     *
     * @param v a value of type 'String'
     */
    public void addConsoleText(LinkedList list) {
        if (list != null) {
            Iterator it = list.iterator();
            while (it.hasNext()) {
                this.consoleText += it.next().toString() + "\n";
            }
        }
    }

    /**
     * Get the list of all available version labels
     *
     * @return list of version labels
     */
    public Vector getAvailableUpdates() {
        return (new Vector(availableUpdates));
    }

    /**
     * Load available database updates from the servlet.
     *
     * @return a value of type 'LinkedList'
     */
    private LinkedList loadAvailableUpdates() {
        try {
            return (getServletClient().getAvailableUpdates());
        } catch (I18NException e) {
            showError(e);
            return (new LinkedList());
        }
    }

    /**
     * Get the value of version.
     * @return Value of version.
     */
    public Version getVersion() {
        return version;
    }

    /**
     * Set the value of version.
     * @param v  Value to assign to version.
     */
    public void setVersion(Version v) {
        this.version = v;
    }
}
