package org.vesuf.runtime.uml.foundation.vepl;

import org.vesuf.model.uml.foundation.vepl.*;
import org.vesuf.model.uml.foundation.core.*;
import org.vesuf.runtime.uml.foundation.core.*;

/**
 *  Operation value path element instance.
 */
public class OperationValuePathElementInstance extends PathElementInstance {

    /**
	 *  Create a path element.
	 *  @param pe The path element.
	 *	@param runtime The runtime.
	 *  @param parent The parent.
	 *  @param element The instance element.
	 */
    public OperationValuePathElementInstance(IPathElement pe, IRuntime runtime, IPathElementInstance parent, IInstanceElement element) {
        super(pe, runtime, parent, element);
    }

    /**
	 *  Get the model element represented by this path element.
	 */
    public IInstanceElement getElement() {
        IInstanceElement ret = this.element;
        if (ret == null) {
            ICallAction ca = (ICallAction) parent.getElement();
            if (ca != null) {
                PathElement[] pathes = ((OperationValuePathElement) getPathElement()).getPathes();
                IInstance[] argvals = new IInstance[pathes.length];
                for (int i = 0; i < pathes.length; i++) {
                    argvals[i] = (IInstance) pathes[i].createPathInstance(getRuntime(), (IInstanceElement) getRoot().getElement()).getElement();
                }
                IArgument[] inargs = ca.getArguments(IParameter.KIND_IN);
                for (int i = 0; i < inargs.length; i++) {
                    inargs[i].setValue(argvals[i]);
                }
                try {
                    ca.execute();
                } catch (Exception e) {
                    e.printStackTrace();
                    throw new RuntimeException("Error during method execution!");
                }
                ret = ca.getReturnArgument().getValue();
            }
        }
        return ret;
    }
}
