package net.sf.jttslite.model;

import java.util.ArrayList;
import java.util.List;
import javax.jdo.annotations.Element;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

/**
 * L'attività è un nodo dell'albero di tracciamento del lavoro di un
 * {@link Workspace progetto}.
 * <p>
 * Il lavoro è rappresentato dalle {@link Action azioni} associate alle singole
 * attività. <br>
 * Ogni attività può avere delle sotto-attività figlie (di qui la struttura
 * gerarchica <i>ad albero</i>).
 * </p>
 * 
 * @author Davide Cavestro
 */
@PersistenceCapable(table = "JTTS_TASK", detachable = "true")
public class Task {

    /**
	 * Identifica univocamente questa tra tutte le attività del sistema.
	 */
    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Long id;

    /**
	 * Il codice attività.
	 */
    @Persistent
    private String code;

    /**
	 * Il nome attività.
	 */
    @Persistent
    private String name;

    /**
	 * La descrizione.
	 */
    @Persistent
    private String description;

    /**
	 * L'attività superiore (padre).
	 */
    @Persistent(defaultFetchGroup = "true", dependent = "false")
    private Task parent;

    /**
	 * Il workspace di appartenenza, dato ridondante che garantisce migliori performance.
	 */
    @Persistent(defaultFetchGroup = "true", dependent = "false")
    private Workspace workspace;

    /**
	 * Le sotto-attività figlie di questa attività.
	 */
    @Persistent(defaultFetchGroup = "true")
    @Element(mappedBy = "parent", dependent = "true")
    private List<Task> children = new ArrayList<Task>();

    /**
	 * Gli avanzamenti effettuati su questa attività.
	 */
    @Persistent(defaultFetchGroup = "true")
    @Element(mappedBy = "task", dependent = "true")
    private List<Action> actions = new ArrayList<Action>();

    public Task() {
    }

    public Task(final String name) {
        this.name = name;
    }

    /**
	 * @return the id
	 */
    public Long getId() {
        return id;
    }

    /**
	 * @param id
	 *            the id to set
	 */
    public void setId(final Long id) {
        this.id = id;
    }

    /**
	 * @return the code
	 */
    public String getCode() {
        return code;
    }

    /**
	 * @param code
	 *            the code to set
	 */
    public void setCode(final String code) {
        this.code = code;
    }

    /**
	 * @return the name
	 */
    public String getName() {
        return name;
    }

    /**
	 * @param name
	 *            the name to set
	 */
    public void setName(final String name) {
        this.name = name;
    }

    /**
	 * @return the description
	 */
    public String getDescription() {
        return description;
    }

    /**
	 * @param description
	 *            the description to set
	 */
    public void setDescription(final String description) {
        this.description = description;
    }

    /**
	 * @return the parent
	 */
    Task getParent() {
        return parent;
    }

    /**
	 * @param parent
	 *            the parent to set
	 */
    void setParent(final Task parent) {
        this.parent = parent;
    }

    /**
	 * @return the workspace
	 */
    Workspace getWorkspace() {
        return workspace;
    }

    /**
	 * @param workspace the workspace to set
	 */
    void setWorkspace(final Workspace workspace) {
        this.workspace = workspace;
    }

    /**
	 * @return the children
	 */
    List<Task> getChildren() {
        return children;
    }

    /**
	 * @param children
	 *            the children to set
	 */
    void setChildren(final List<Task> children) {
        this.children = children;
    }

    /**
	 * @return the actions
	 */
    List<Action> getActions() {
        return actions;
    }

    /**
	 * @param actions
	 *            the actions to set
	 */
    void setActions(final List<Action> actions) {
        this.actions = actions;
    }
}
