package reasoning;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.eclipse.uml2.uml.AssociationClass;
import org.eclipse.uml2.uml.Property;
import org.tzi.use.parser.Context;
import org.tzi.use.uml.mm.MAssociationClass;
import org.tzi.use.uml.mm.MAssociationEnd;
import org.tzi.use.uml.mm.MInvalidModelException;

public class AssociationClassAdaptor extends ClassAdaptor {

    private AssociationClass emfAssoationClass;

    private MAssociationClass fAssocClass;

    private ArrayList<AssociationEndAdaptor> fAssociationEndsAdaptor;

    public AssociationClassAdaptor(AssociationClass emfAsCls) throws Exception {
        super(emfAsCls);
        emfAssoationClass = emfAsCls;
        fAssociationEndsAdaptor = new ArrayList<AssociationEndAdaptor>();
        initializeAssociationEnds();
    }

    /**
	 * Initialize the association ends. 
	 */
    private void initializeAssociationEnds() throws Exception {
        System.out.println(emfAssoationClass == null);
        List<Property> memberEnds = emfAssoationClass.getMemberEnds();
        System.out.println("HI2");
        Iterator<Property> iter = memberEnds.iterator();
        AssociationEndAdaptor assendAdap;
        Property memberEnd;
        while (iter.hasNext()) {
            memberEnd = (Property) iter.next();
            System.out.println(memberEnd.getName());
            assendAdap = new AssociationEndAdaptor(memberEnd);
            fAssociationEndsAdaptor.add(assendAdap);
        }
    }

    public MAssociationClass genEmptyAssocClass(Context ctx) {
        fAssocClass = ctx.modelFactory().createAssociationClass(className, fIsAbstract);
        return fAssocClass;
    }

    /**
	 * Add associationsclasses as associations.	
	 * @param ctx
	 * @return
	 * @throws Exception
	 * TODO: Change the exception
	 */
    public MAssociationClass genAssociation(Context ctx) throws Exception {
        Iterator it = fAssociationEndsAdaptor.iterator();
        try {
            while (it.hasNext()) {
                AssociationEndAdaptor ae = (AssociationEndAdaptor) it.next();
                MAssociationEnd aend = ae.gen(ctx);
                fAssocClass.addAssociationEnd(aend);
            }
        } catch (MInvalidModelException e) {
            throw new Exception();
        }
        return fAssocClass;
    }

    /**
	 * TODO: Generate Attributes and Operation Signatures to the class
	 * @param ctx
	 */
    public void genAttributesOperationSignatures(Context ctx) {
    }
}
