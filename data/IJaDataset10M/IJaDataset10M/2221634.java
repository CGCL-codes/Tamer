package tmacsoftware.whitematter;

import tmacsoftware.ursql.*;
import java.util.Vector;

public class ProcessLoader {

    public UrSQLController controller = null;

    private Sticklet sticklet = null;

    public ProcessLoader(Sticklet sticklet) {
        this.sticklet = sticklet;
        if (System.getProperty("os.name").compareTo("Linux") == 0) {
            this.controller = new UrSQLController("linuxprocesses.udb");
        } else if (System.getProperty("os.name").contains("Windows")) {
            this.controller = new UrSQLController("windowsprocesses.udb");
        }
    }

    /**
     * Load all entries from database into combo box
     */
    public void load() {
        Vector<UrSQLEntity> entities = controller.getAllEntities();
        for (int i = 0; i < entities.size(); i++) {
            sticklet.addProcessItem(entities.get(i).getEntry("name").getValue());
        }
    }

    /**
     * Get process name from application name
     * @param name Application name
     * @return Process name
     */
    public String getProcess(String name) {
        UrSQLEntity entity = controller.getEntity("name=" + name);
        if (entity.isEmpty()) {
            return name;
        }
        return entity.getEntry("process").getValue();
    }

    /**
     * Get application name from process name
     * @param process Process name
     * @return Application name
     */
    public String getName(String process) {
        UrSQLEntity entity = controller.getEntity("process=" + process);
        if (entity.isEmpty()) {
            return process;
        }
        return entity.getEntry("name").getValue();
    }
}
