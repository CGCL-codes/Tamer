package launcher.runtime.process;

import java.util.ArrayList;
import java.util.List;
import launcher.application.Constants.IO;

/**
 * Represents a process parent-child relationship. It is used to provide a
 * uniform representation of the results from various process listing
 * applications.
 * <p>
 * Each 'node' in the tree has a process id (pid), a command and any number of
 * child {@link ProcessTree} objects.
 * 
 * @see ProcessManager
 * @author Ramon Servadei
 * @version $Revision: 1.3 $
 * 
 */
public class ProcessTree {

    private String pid;

    private String processCommandLine;

    private List<ProcessTree> children = new ArrayList<ProcessTree>();

    public ProcessTree() {
        pid = "ROOT";
        processCommandLine = "";
    }

    /**
   * Copy constructor
   * 
   * @param toClone
   */
    public ProcessTree(ProcessTree toClone) {
        this.pid = toClone.pid;
        this.processCommandLine = toClone.processCommandLine;
        for (ProcessTree childToClone : toClone.children) {
            children.add(new ProcessTree(childToClone));
        }
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public ProcessTree setProcessCommandLine(String processCommandLine) {
        this.processCommandLine = processCommandLine;
        return this;
    }

    public String getProcessCommandLine() {
        return processCommandLine;
    }

    /**
   * Traverses the entire tree to find the process identified by the childPid
   * 
   * @param childPid
   * @return null if the childPid was not found
   */
    public ProcessTree getChildWithPid(String childPid) {
        if (childPid == null) return null;
        if (childPid.equals(pid)) return this;
        for (ProcessTree child : children) {
            if (childPid.equals(child.getPid())) {
                return child;
            }
            child = child.getChildWithPid(childPid);
            if (child != null) return child;
        }
        return null;
    }

    /**
   * Adds a new child ProcessTree to <b>this</b> ProcessTree.
   * 
   * @param childPid
   * @return the new ProcessTree for the childPid
   */
    public ProcessTree addChild(String childPid) {
        if (childPid == null) throw new NullPointerException("childpid should never be null");
        ProcessTree child = new ProcessTree();
        child.setPid(childPid);
        children.add(child);
        return child;
    }

    /**
   * Add the list of ProcessTree objects as <b>new</b> ProcessTree objects via
   * the copy constructor (deep copy)
   * 
   * @param other
   */
    public void addAllChildren(List<ProcessTree> other) {
        for (ProcessTree tree : other) {
            children.add(new ProcessTree(tree));
        }
    }

    /**
   * Blindly update this process tree from the state of the
   * <code>newVersion</code> process tree (deep copy).
   * 
   * @param newVersion
   */
    public void replaceWith(ProcessTree newVersion) {
        this.pid = newVersion.pid;
        this.processCommandLine = newVersion.processCommandLine;
        children.clear();
        addAllChildren(newVersion.children);
    }

    /**
   * Go through the tree and remove any child ProcessTree that has no command,
   * re-assign its children to the grandparent
   */
    public void removeInvalidParents() {
        List<ProcessTree> keepList = new ArrayList<ProcessTree>();
        for (ProcessTree child : children) {
            keepList.addAll(child.internalRemoveInvalidParents());
        }
        children = keepList;
    }

    public List<ProcessTree> children() {
        return children;
    }

    /**
   * Get the number of <b>immediate</b> child ProcessTree objects
   * 
   * @return
   */
    public int size() {
        return children.size();
    }

    @Override
    public int hashCode() {
        final int PRIME = 31;
        int result = 1;
        result = PRIME * result + ((children == null) ? 0 : children.hashCode());
        result = PRIME * result + ((pid == null) ? 0 : pid.hashCode());
        result = PRIME * result + ((processCommandLine == null) ? 0 : processCommandLine.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        final ProcessTree other = (ProcessTree) obj;
        if (children == null) {
            if (other.children != null) return false;
        } else if (!children.equals(other.children)) return false;
        if (pid == null) {
            if (other.pid != null) return false;
        } else if (!pid.equals(other.pid)) return false;
        if (processCommandLine == null) {
            if (other.processCommandLine != null) return false;
        } else if (!processCommandLine.equals(other.processCommandLine)) return false;
        return true;
    }

    public String toString() {
        return toString("+-->");
    }

    public String toString(String tabs) {
        StringBuffer sb = new StringBuffer();
        sb.append(IO.LS).append(tabs).append("[").append(pid).append(" ").append(processCommandLine).append("]");
        for (ProcessTree child : children) {
            sb.append(child.toString("|    " + tabs));
        }
        return sb.toString();
    }

    private List<ProcessTree> internalRemoveInvalidParents() {
        List<ProcessTree> keepList = new ArrayList<ProcessTree>();
        for (ProcessTree currentChild : children) {
            keepList.addAll(currentChild.internalRemoveInvalidParents());
        }
        if (processCommandLine != null && !processCommandLine.equals("")) {
            children.clear();
            children.addAll(keepList);
            keepList.clear();
            keepList.add(this);
        }
        return keepList;
    }
}
