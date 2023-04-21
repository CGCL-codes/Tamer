package de.jassda.csp;

import java.util.List;
import java.util.Vector;

/**
 * Class ProcessPath
 * 
 * 
 * @author Mark Broerkens
 * @version %I%, %G%
 */
public class ProcessPath {

    Vector path = new Vector();

    /**
	 * Constructor ProcessPath
	 * 
	 * 
	 */
    public ProcessPath() {
    }

    /**
	 * Constructor ProcessPath
	 * 
	 * 
	 * @param cspProcess
	 * 
	 */
    public ProcessPath(CspProcess cspProcess) {
        path.add(cspProcess);
    }

    /**
	 * Constructor ProcessPath
	 * 
	 * 
	 * @param path
	 * 
	 */
    public ProcessPath(List path) {
        if (path != null) {
            this.path.addAll(path);
        }
    }

    /**
	 * Constructor ProcessPath
	 * 
	 * 
	 * @param parent
	 * @param lastElement
	 * 
	 */
    protected ProcessPath(ProcessPath parent, CspProcess lastElement) {
        if (parent != null) {
            this.path.addAll(parent.getPath());
        }
        if (lastElement != null) {
            this.path.add(lastElement);
        }
    }

    /**
	 * Method getPath
	 * 
	 * 
	 * @return
	 * 
	 */
    public List getPath() {
        return this.path;
    }

    /**
	 * Method getLastPathComponent
	 * 
	 * 
	 * @return
	 * 
	 */
    public CspProcess getLastPathComponent() {
        return (CspProcess) path.lastElement();
    }

    /**
	 * Method getPathCount
	 * 
	 * 
	 * @return
	 * 
	 */
    public int getPathCount() {
        return this.path.size();
    }

    /**
	 * Method getPathComponent
	 * 
	 * 
	 * @param element
	 * 
	 * @return
	 * 
	 */
    public CspProcess getPathComponent(int element) {
        return (CspProcess) path.elementAt(element);
    }

    /**
	 * Method equals
	 * 
	 * 
	 * @param o
	 * 
	 * @return
	 * 
	 */
    public boolean equals(Object o) {
        return super.equals(o);
    }

    /**
	 * Method pathByAddingChild
	 * 
	 * 
	 * @param child
	 * 
	 * @return
	 * 
	 */
    public ProcessPath pathByAddingChild(CspProcess child) {
        return new ProcessPath(this, child);
    }

    /**
	 * Method getParentPath
	 * 
	 * 
	 * @return
	 * 
	 */
    public ProcessPath getParentPath() {
        if (path.isEmpty()) {
            return null;
        } else {
            List parentPath = path.subList(0, path.size() - 1);
            return new ProcessPath(parentPath);
        }
    }

    /**
	 * Method toString
	 * 
	 * 
	 * @return
	 * 
	 */
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("ProcessPath: {");
        for (int i = 0; i < path.size(); i++) {
            buffer.append(path.get(i));
            if (i < path.size() + 1) {
                buffer.append(",");
            }
        }
        buffer.append("}");
        return buffer.toString();
    }

    /**
	 * Method isEmpty
	 * 
	 * 
	 * @return
	 * 
	 */
    public boolean isEmpty() {
        return path.isEmpty();
    }
}
