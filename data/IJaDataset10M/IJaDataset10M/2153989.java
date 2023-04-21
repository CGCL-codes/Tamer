package org.tzi.use.parser.use;

import org.tzi.use.parser.Context;
import org.tzi.use.parser.MyToken;
import org.tzi.use.parser.SemanticException;
import org.tzi.use.uml.mm.MAssociationEnd;
import org.tzi.use.uml.mm.MClass;
import org.tzi.use.uml.mm.MModel;
import org.tzi.use.uml.mm.MNavigableElement;

/**
 * Contains functions and fields that are common to 
 * {@linkplain ASTSubset}, {@linkplain ASTRedefine} and {@linkplain ASTConform}. 
 * 
 * @author Vitaly Bichov <br>
 * Created on 01/09/10 
 * 
 * @see {@linkplain ASTSubset}, {@linkplain ASTRedefine} and {@linkplain ASTConform} 
 *
 */
abstract class ASTModify {

    protected MAssociationEnd fMassocEnd;

    protected final MyToken fassocEndName;

    ASTModify(MyToken assocEndName) {
        fassocEndName = assocEndName;
    }

    public void setMAssociationEnd(MAssociationEnd massocEnd) {
        fMassocEnd = massocEnd;
    }

    /**
	 * Finds an associationEnd that corresponds to <code>redef </code> and returns it.
	 * @param redef - container that contains ClassName token and propName token
	 * @param ctx - Context object
	 * @return MAssociationEnd object that described in <code>redef </code> or <code>null</code>.
	 * @throws SemanticException 
	 */
    protected MAssociationEnd getModifiedAssociationEnd(ASTModifyUnit redef, Context ctx) throws SemanticException {
        MNavigableElement navEl;
        MClass cls;
        MModel model = ctx.model();
        String roleName = redef.getRoleName().getText();
        String className = redef.getClassName().getText();
        if ((cls = model.getClass(className)) == null) {
            throw new SemanticException(redef.getClassName(), "Class '" + className + "' doesn't exist, can't modify role name '" + roleName + "'");
        }
        if ((navEl = cls.navigableEnd(roleName)) == null) {
            throw new SemanticException(redef.getRoleName(), "Role named '" + roleName + "' not found in class '" + className + "'");
        }
        if (!(navEl instanceof MAssociationEnd)) {
            throw new SemanticException(fassocEndName, "Role named '" + roleName + "' in class '" + className + "' is not an associationEnd" + " and can't be modified.");
        }
        return (MAssociationEnd) navEl;
    }
}
