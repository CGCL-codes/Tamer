package org.ietr.preesm.codegen.model;

import net.sf.dftools.algorithm.model.parameters.InvalidExpressionException;
import net.sf.dftools.algorithm.model.sdf.esdf.SDFSinkInterfaceVertex;
import net.sf.dftools.architecture.slam.ComponentInstance;
import org.ietr.preesm.codegen.model.containers.AbstractCodeContainer;
import org.ietr.preesm.codegen.model.main.ICodeElement;

public class CodeGenSDFSinkInterfaceVertex extends SDFSinkInterfaceVertex implements ICodeGenSDFVertex {

    public ComponentInstance getOperator() {
        return (ComponentInstance) this.getPropertyBean().getValue(OPERATOR, ComponentInstance.class);
    }

    public void setOperator(ComponentInstance op) {
        this.getPropertyBean().setValue(OPERATOR, getOperator(), op);
    }

    public int getPos() {
        if (this.getPropertyBean().getValue(POS) != null) {
            return (Integer) this.getPropertyBean().getValue(POS, Integer.class);
        }
        return 0;
    }

    public void setPos(int pos) {
        this.getPropertyBean().setValue(POS, getPos(), pos);
    }

    public String toString() {
        return "";
    }

    @Override
    public ICodeElement getCodeElement(AbstractCodeContainer parentContainer) throws InvalidExpressionException {
        return null;
    }
}
