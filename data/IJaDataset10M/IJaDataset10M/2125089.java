package org.jude.simplelogic;

import java.io.*;
import java.util.*;
import org.jude.client.editor.*;
import org.jude.client.*;
import org.jude.client.dataflavor.*;
import org.jude.simplelogic.*;

/**
 * <p> A Skeleton for a Oid implementation.
 * <p> This class permits to tract the Prolog and algebric convention that tuple (oid) == oid.
 *
 * @author Massimo Zaniboni
 * @version $Revision: 1.2 $   
 */
public abstract class AbstractOid implements Oid {

    /**
     * <p> The string rapresentation. 
     * <p> If you extend this class you must convert the
     *     Oid value given as constructor parameter to a valid and unique XSB/SimpleLogic
     *     rapresentation.
     * <p> This fields MUST BE CONSTANT.
     * <p> !! in the future abbandon this type of implementation in favor of a direct structure
     *        comparison...
     */
    protected String asStringConst;

    protected JudeObject judeObject;

    /**
       * <p> Constructor
       * <p> @param judeObject !! due to circular references problems this is passed as null and
       *        then the setJudeObject method is called before the usage of 
       *        object...
       */
    public AbstractOid(JudeObject judeObject) {
        this.judeObject = judeObject;
    }

    public void setJudeObject(JudeObject judeObject) {
        this.judeObject = judeObject;
    }

    public int getClassId() {
        return UNKNOW_ID;
    }

    public CompilablePart getVariableSubstitution(CompilationEnviroment env, VariableSubstitution subs) {
        return this;
    }

    public String toString() {
        return asStringConst;
    }

    /**
     * Compare toString() value
     */
    public int compareTo(Object o) {
        if (o instanceof Oid) {
            Oid other = (Oid) o;
            return other.toString().compareTo(this.toString());
        } else return 0;
    }

    /**
     * Compare toString() value
     */
    public boolean equals(Object other) {
        if (other instanceof Oid) return toString().equals(other.toString()); else return false;
    }

    public Collection getMetaInfoRules(CompilationEnviroment env) {
        return new LinkedList();
    }

    public JudeObject getJudeObject() {
        return judeObject;
    }

    public String getAsSimpleLogic() {
        return asStringConst;
    }

    public String getAsXSBPrologCode() {
        return asStringConst;
    }

    public int getTupleArity() {
        return 1;
    }

    public JudeObject getTupleElement(int i) {
        if (i == 0) return getJudeObject(); else return null;
    }

    public abstract boolean isGround(Set boundVariables, Set freeVariables);
}
