package net.sf.antcontrib.logic;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.taskdefs.Exit;
import org.apache.tools.ant.types.Reference;

/**
 * Extension of <code>&lt;fail&gt;</code> that can throw an exception
 * that is a reference in the project.
 *
 * <p>This may be useful inside the <code>&lt;catch&gt;</code> block
 * of a <code>&lt;trycatch&gt;</code> task if you want to rethrow the
 * exception just caught.</p>
 */
public class Throw extends Exit {

    private Reference ref;

    /**
     * The reference that points to a BuildException.
     */
    public void setRefid(Reference ref) {
        this.ref = ref;
    }

    public void execute() throws BuildException {
        Object reffed = ref != null ? ref.getReferencedObject(getProject()) : null;
        if (reffed != null && reffed instanceof BuildException) {
            throw (BuildException) reffed;
        }
        super.execute();
    }
}
