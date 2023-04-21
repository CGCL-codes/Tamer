package prest.process;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Represents a process context
 *
 * @author Daniel Buchta
 * @author Peter Rybar
 *
 */
public class ProcessContext {

    private static final Map<Long, ProcessContext> context = new HashMap<Long, ProcessContext>();

    /**
	 * The context getter
	 *
	 * @return the context
	 */
    public static ProcessContext get(long id) {
        return context.get(id);
    }

    public static Collection<ProcessContext> getAll() {
        return context.values();
    }

    public static ProcessContext create(long id) {
        ProcessContext result = new ProcessContext(id);
        context.put(id, result);
        return result;
    }

    private final long id;

    private Status status = Status.RUNNING;

    private final Map<String, Object> data = new HashMap<String, Object>();

    private final List<CheckPoint> checkPoints = new ArrayList<CheckPoint>();

    /**
	 * Constructor
	 *
	 * @param id
	 */
    public ProcessContext(long id) {
        this.id = id;
    }

    /**
	 * The id getter
	 *
	 * @return the id
	 */
    public long getId() {
        return this.id;
    }

    /**
	 * The status getter
	 *
	 * @return the status
	 */
    public Status getStatus() {
        return this.status;
    }

    /**
	 * The data getter
	 *
	 * @return the data
	 */
    public Map<String, Object> getData() {
        return this.data;
    }

    /**
	 * The checkPoints getter
	 *
	 * @return the checkPoints
	 */
    public List<CheckPoint> getCheckPoints() {
        return this.checkPoints;
    }

    /**
	 * The status setter
	 *
	 * @param status
	 *            the status to set
	 */
    public void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append("id=").append(this.id);
        result.append("; status=").append(this.status);
        result.append("; data=").append(this.data);
        result.append("; checkPoints=").append(this.checkPoints);
        return result.toString();
    }
}
