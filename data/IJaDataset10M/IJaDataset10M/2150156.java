package tudresden.ocl20.core.jmi.mof14.impl.model;

import tudresden.ocl20.core.jmi.mof14.model.*;
import org.netbeans.mdr.handlers.InstanceHandler;
import org.netbeans.mdr.storagemodel.StorableObject;
import java.util.*;

/** MOF1.4-specific implementations for Operations defined in
 * CommonModel::Operation
 * @author Administrator
 */
public abstract class OperationImpl extends ModelElementImpl implements Operation {

    /** Creates a new instance of OperationImpl */
    protected OperationImpl(StorableObject storable) {
        super(storable);
    }

    /** Checks, if the given types conform to the types of the in/inout parameters of
     * this operation. (parameter multiplicities are taken into account, because they
     * are not seen as a common model concept, but as MOF-specific)
     */
    public boolean hasMatchingSignature(java.util.List paramTypes) {
        int pos = 0;
        Iterator contentsIt = getContents().iterator();
        while (contentsIt.hasNext()) {
            ModelElement me = (ModelElement) contentsIt.next();
            if (me instanceof Parameter) {
                Parameter p = (Parameter) me;
                if (p.getDirection().equals(DirectionKindEnum.IN_DIR) || p.getDirection().equals(DirectionKindEnum.INOUT_DIR)) {
                    if (paramTypes == null || pos >= paramTypes.size()) {
                        return false;
                    }
                    if (!((MofClass) paramTypes.get(pos)).conformsTo(p.getTypeA())) {
                        return false;
                    }
                    pos++;
                }
            }
        }
        if (paramTypes != null && pos < paramTypes.size()) {
            return false;
        }
        return true;
    }

    /** get the out  and inout parameters */
    public java.util.List getOutParametersA() {
        List outparams = new ArrayList();
        Iterator it = getContents().iterator();
        while (it.hasNext()) {
            ModelElement me = (ModelElement) it.next();
            if (me instanceof Parameter) {
                Parameter p = (Parameter) me;
                if (p.getDirection().equals(DirectionKindEnum.OUT_DIR) || p.getDirection().equals(DirectionKindEnum.INOUT_DIR)) {
                    outparams.add(p);
                }
            }
        }
        return outparams;
    }

    /** get the in and inout parameters */
    public java.util.List getInParametersA() {
        List inparams = new ArrayList();
        Iterator it = getContents().iterator();
        while (it.hasNext()) {
            ModelElement me = (ModelElement) it.next();
            if (me instanceof Parameter) {
                Parameter p = (Parameter) me;
                if (p.getDirection().equals(DirectionKindEnum.IN_DIR) || p.getDirection().equals(DirectionKindEnum.INOUT_DIR)) {
                    inparams.add(p);
                }
            }
        }
        return inparams;
    }

    /** get all parameters */
    public java.util.List getParametersA() {
        List params = new ArrayList();
        Iterator it = getContents().iterator();
        while (it.hasNext()) {
            ModelElement me = (ModelElement) it.next();
            if (me instanceof Parameter) {
                Parameter p = (Parameter) me;
                if (!p.getDirection().equals(DirectionKindEnum.RETURN_DIR)) {
                    params.add(p);
                }
            }
        }
        return params;
    }

    /** get the owning classifier of this operation */
    public tudresden.ocl20.core.jmi.ocl.commonmodel.Classifier getOwnerA() {
        return (MofClass) getContainer();
    }

    /** is this a class or instance operation? */
    public boolean isInstanceLevelA() {
        return this.getScope().equals(ScopeKindEnum.INSTANCE_LEVEL);
    }

    /** get the return parameter */
    public tudresden.ocl20.core.jmi.ocl.commonmodel.Parameter getReturnParameterA() {
        Iterator it = getContents().iterator();
        while (it.hasNext()) {
            ModelElement me = (ModelElement) it.next();
            if (me instanceof Parameter) {
                Parameter p = (Parameter) me;
                if (p.getDirection().equals(DirectionKindEnum.RETURN_DIR)) {
                    return p;
                }
            }
        }
        return null;
    }
}
