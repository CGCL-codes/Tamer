package tudresden.ocl20.pivot.metamodels.mof.internal.model;

import org.apache.log4j.Logger;
import java.util.Iterator;
import tudresden.ocl20.core.jmi.mof14.model.Association;
import tudresden.ocl20.core.jmi.mof14.model.AssociationEnd;
import tudresden.ocl20.core.jmi.mof14.model.ModelElement;
import tudresden.ocl20.pivot.pivotmodel.Property;
import tudresden.ocl20.pivot.pivotmodel.Type;
import tudresden.ocl20.pivot.pivotmodel.base.AbstractProperty;

/**
 * An implementation of the Pivot Model {@link Property} concept for
 * AssociationEnd of MOF metamodel in MDR.
 * 
 * @author Ronny Brandt
 * @version 1.0 09.05.2007
 */
public class MofAssociationEnd extends AbstractProperty implements Property {

    /**
	 * Logger for this class
	 */
    private static final Logger logger = Logger.getLogger(MofAssociationEnd.class);

    private AssociationEnd ae;

    /**
	 * Creates a new <code>MofAssociationEnd</code> instance.
	 * 
	 * @param ae the MOF {@link AssociationEnd} adapted by this class
	 */
    public MofAssociationEnd(AssociationEnd ae) {
        if (logger.isDebugEnabled()) {
            logger.debug("MofAssociationEnd(AssociationEnd ae=" + ae + ") - enter");
        }
        this.ae = ae;
        if (logger.isDebugEnabled()) {
            logger.debug("MofAssociationEnd(AssociationEnd) - exit");
        }
    }

    @Override
    public String getName() {
        if (logger.isDebugEnabled()) {
            logger.debug("getName() - enter");
        }
        String returnString = ae.getName();
        if (logger.isDebugEnabled()) {
            logger.debug("getName() - exit - return value=" + returnString);
        }
        return returnString;
    }

    @Override
    public Type getOwningType() {
        if (logger.isDebugEnabled()) {
            logger.debug("getOwningType() - enter");
        }
        Type owningType = null;
        Iterator it = ((Association) ae.getContainer()).getContents().iterator();
        while (it.hasNext()) {
            ModelElement me = (ModelElement) it.next();
            if (me instanceof AssociationEnd) if (!(me == ae)) owningType = MofAdapterFactory.INSTANCE.createType(((AssociationEnd) me).getType());
        }
        if (logger.isDebugEnabled()) {
            logger.debug("getOwningType() - exit - return value=" + owningType);
        }
        return owningType;
    }

    @Override
    public Type getType() {
        if (logger.isDebugEnabled()) {
            logger.debug("getType() - enter");
        }
        Type returnType = MofAdapterFactory.INSTANCE.createType(ae.getType());
        if (logger.isDebugEnabled()) {
            logger.debug("getType() - exit - return value=" + returnType);
        }
        return returnType;
    }

    public boolean isUnique() {
        if (logger.isDebugEnabled()) {
            logger.debug("isUnique() - enter");
        }
        boolean returnboolean = ae.isUniqueA();
        if (logger.isDebugEnabled()) {
            logger.debug("isUnique() - exit - return value=" + returnboolean);
        }
        return returnboolean;
    }

    public boolean isMultiple() {
        if (logger.isDebugEnabled()) {
            logger.debug("isMultiple() - enter");
        }
        boolean returnboolean = ae.isMultipleA();
        if (logger.isDebugEnabled()) {
            logger.debug("isMultiple() - exit - return value=" + returnboolean);
        }
        return returnboolean;
    }

    public boolean isOrdered() {
        if (logger.isDebugEnabled()) {
            logger.debug("isOrdered() - enter");
        }
        boolean returnboolean = ae.isOrderedA();
        if (logger.isDebugEnabled()) {
            logger.debug("isOrdered() - exit - return value=" + returnboolean);
        }
        return returnboolean;
    }
}
