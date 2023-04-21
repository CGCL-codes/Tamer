package org.jmlspecs.checker;

import java.util.ArrayList;
import java.util.Iterator;
import org.multijava.mjc.CAbstractMethodSet;
import org.multijava.mjc.CClass;
import org.multijava.mjc.CClassType;
import org.multijava.mjc.CMethodSet;
import org.multijava.util.compiler.PositionedError;
import org.multijava.util.compiler.TokenReference;

/**
 * This class represents a class read from a *.class file.  It is
 * primarily just a data structure that contains information for 
 * checking the signatures in a refinement sequence.  
 *
 * @see	org.multijava.mjc.CMember
 */
public class JmlBinaryType extends JmlBinaryMember {

    /**
     * Constructs a class export from file. */
    public JmlBinaryType(TokenReference where, JmlBinarySourceClass member, String prefix) throws PositionedError {
        super(where, member);
        member.setAST(this);
        CClassType[] innerTypes = member.getInnerClasses();
        inners = new ArrayList(innerTypes.length);
        for (int i = 0; i < innerTypes.length; i++) {
            String innerIdent = innerTypes[i].ident();
            JmlBinaryType innerType = JmlRefinePrefix.buildBinaryType(where, prefix + "$" + innerIdent);
            inners.add(innerType);
        }
    }

    /**
     * @return	the interface
     */
    public CClass getCClass() {
        return (CClass) member;
    }

    /** This method collects the AST's for the binary methods 
     *  declared in the original source file; they are 
     *  used to make sure there are no "extra" non-model methods 
     *  declared in the specification files.  Non-model methods must be 
     *  declared in the API for this type.
     */
    public void combineSpecifications() {
        CClass clazz = (JmlBinarySourceClass) member;
        CMethodSet methodSet = clazz.methods();
        ArrayList tempList = new ArrayList(methodSet.size());
        CAbstractMethodSet.Iterator iter = methodSet.iterator();
        while (iter.hasNext()) {
            JmlSourceMethod meth = (JmlSourceMethod) iter.next();
            tempList.add(meth.getAST());
        }
        methods = new JmlMemberDeclaration[tempList.size()];
        tempList.toArray(methods);
    }

    public JmlMemberDeclaration[] getCombinedMethods() {
        if (methods == null) {
            combineSpecifications();
        }
        return methods;
    }

    public ArrayList inners() {
        return inners;
    }

    protected JmlMemberDeclaration[] methods = null;

    protected ArrayList inners = null;
}
