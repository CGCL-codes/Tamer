package de.fzi.injectj.model.impl.recoder.transformation;

import recoder.CrossReferenceServiceConfiguration;
import recoder.ProgramFactory;
import recoder.abstraction.Constructor;
import recoder.abstraction.Type;
import recoder.java.declaration.ConstructorDeclaration;
import recoder.java.declaration.TypeDeclaration;
import recoder.kit.ProblemReport;
import recoder.kit.TwoPassTransformation;
import recoder.list.ConstructorList;
import recoder.list.TypeReferenceArrayList;
import recoder.list.TypeReferenceMutableList;
import recoder.service.NameInfo;

/**
 * @author genssler
 * This class is part of the Inject/J (http://injectj.sf.net) project. 
 * Inject/J is free software, available under the terms and conditions
 * of the GNU public license.
 *
 * 
 */
public class RenameClass extends TwoPassTransformation {

    private TypeDeclaration type;

    private String newName;

    private TypeReferenceMutableList refs;

    private ConstructorList cons;

    /**
	   Creates a new transformation object that renames a type declaration
	   and all known references to that type. The new name should not hide
	   another type in the declaration context.
	   @param sc the service configuration to use.
	   @param type the type declaration to be renamed;
	   may not be <CODE>null</CODE> and may not be an anonymous type.
	   @param newName the new name for the element;
	   may not be <CODE>null</CODE> and must denote a valid identifier name.
	 */
    public RenameClass(CrossReferenceServiceConfiguration sc, TypeDeclaration type, String newName) {
        super(sc);
        if (type == null) {
            throw new IllegalArgumentException("Missing type");
        }
        if (type.getName() == null) {
            throw new IllegalArgumentException("May not rename anonymous types");
        }
        if (newName == null) {
            throw new IllegalArgumentException("Missing name");
        }
        this.type = type;
        this.newName = newName;
    }

    /**
	   Collects all references to the type and all existing array variants,
	   as well as all constructor declarations.
	   Constructor references are not relevant, as they are either
	   nameless (super / this), or contain a type reference already.
	   @return the problem report.
	 */
    public ProblemReport analyze() {
        refs = new TypeReferenceArrayList();
        if (newName.equals(type.getName())) {
            return setProblemReport(IDENTITY);
        }
        NameInfo ni = getNameInfo();
        refs.add(getCrossReferenceSourceInfo().getReferences(type));
        cons = type.getConstructors();
        if (cons == null) {
            cons = ConstructorList.EMPTY_LIST;
        }
        Type atype = ni.getArrayType(type);
        while (atype != null) {
            refs.add(getCrossReferenceSourceInfo().getReferences(atype));
            atype = ni.getArrayType(atype);
        }
        return setProblemReport(EQUIVALENCE);
    }

    /**
	   Locally renames the type declaration, all type references and
	   constructors collected in the analyzation phase. 
	   @exception IllegalStateException if the analyzation has not been
	   called.
	   @see #analyze()
	 */
    public void transform() {
        super.transform();
        ProgramFactory pf = getProgramFactory();
        replace(type.getIdentifier(), pf.createIdentifier(newName));
        for (int i = cons.size() - 1; i >= 0; i -= 1) {
            Constructor con = cons.getConstructor(i);
            if (con instanceof ConstructorDeclaration) {
                replace(((ConstructorDeclaration) con).getIdentifier(), pf.createIdentifier(newName));
            }
        }
        for (int i = refs.size() - 1; i >= 0; i -= 1) {
            replace(refs.getTypeReference(i).getIdentifier(), pf.createIdentifier(newName));
        }
    }
}
