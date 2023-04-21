package sf.noen.webui.model;

import java.io.Serializable;

/**
 * Describes an update for the table of test results in the web UI.
 *
 * @author Teemu Kanstr�n
 */
public class Update implements Serializable {

    private final String name;

    private final String status;

    private final String description;

    public final UpdateType type;

    public final String style;

    public Update(String name, String status, String description, UpdateType type, String style) {
        this.name = name;
        this.status = status;
        this.description = description;
        this.type = type;
        this.style = style;
    }

    public String getName() {
        return name;
    }

    public String getStatus() {
        return status;
    }

    public String getDescription() {
        return description;
    }

    public boolean isNew() {
        if (type == UpdateType.TEST_STARTED || type == UpdateType.SUITE_STARTED || type == UpdateType.TEST_IGNORED) {
            return true;
        }
        return false;
    }
}
