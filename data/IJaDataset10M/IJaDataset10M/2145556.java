package nz.net.juju.jaune;

import java.util.*;
import java.io.*;
import gnu.bytecode.*;

/** Output from the MethodCompiler, consisting of the compiled form of
    a method including all of the actual assembly, any external
    references, and any constant data.
*/
public class CompiledMethod {

    private String label;

    private int id;

    private CodeSection code;

    private ArrayList references;

    private ArrayList constantData;

    private boolean isClassInit;

    public CompiledMethod(Method method, CodeSection code, ArrayList references, ArrayList constantData) {
        this.code = code;
        this.references = references;
        this.constantData = constantData;
        isClassInit = method.getName().equals("<clinit>");
        label = Support.getLabel(method);
        id = Support.getMethodId(method);
        code.setLabel(label);
    }

    /** Returns the global method ID */
    public int getId() {
        return id;
    }

    /** Returns the unique method label */
    public String getLabel() {
        return label;
    }

    /** Returns the list of all methods and fields this method
	references. 
    */
    public ArrayList getReferences() {
        return references;
    }

    /** Returns the list of all constant data this method references.
     */
    public ArrayList getConstantData() {
        return constantData;
    }

    /** Returns true if this method is the class initialiser (clinit)
     */
    public boolean isClassInit() {
        return isClassInit;
    }

    /** Returns the actual code in this method.
     */
    public CodeSection getCode() {
        return code;
    }
}
