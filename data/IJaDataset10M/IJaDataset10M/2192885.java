package takatuka.optimizer.deadCodeRemoval.dataObj.xml;

import java.util.*;

/**
 *
 * @author aslam
 */
public class ClassFileXML {

    private String name = null;

    private boolean includeAllFunctions = false;

    private boolean includeAllFields = false;

    private Vector functions = new Vector();

    private Vector fields = new Vector();

    public ClassFileXML(String name, boolean includeAllFunctions, boolean includeAllFields, Vector functions, Vector fields) {
        this.name = name;
        this.includeAllFunctions = includeAllFunctions;
        this.includeAllFields = includeAllFields;
        if (!includeAllFunctions) {
            this.functions = functions;
        }
        if (!includeAllFields) {
            this.fields = fields;
        }
    }

    public String getName() {
        return name;
    }

    public boolean isIncludeAllFunctions() {
        return includeAllFunctions;
    }

    public boolean isIncludeAllFields() {
        return includeAllFields;
    }

    public Vector getFunctions() {
        return (Vector) functions.clone();
    }

    public Vector getFields() {
        return (Vector) fields.clone();
    }

    @Override
    public String toString() {
        return name + ", " + includeAllFields + ", " + includeAllFunctions + ", Function = " + functions.toString() + ", Fields=" + fields.toString();
    }
}
