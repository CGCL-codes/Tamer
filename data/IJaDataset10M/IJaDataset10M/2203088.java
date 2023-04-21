package net.sf.orcc.backends.transformations;

import java.util.List;
import net.sf.orcc.df.Actor;
import net.sf.orcc.df.Pattern;
import net.sf.orcc.df.Port;
import net.sf.orcc.ir.ExprBinary;
import net.sf.orcc.ir.ExprUnary;
import net.sf.orcc.ir.Param;
import net.sf.orcc.ir.Procedure;
import net.sf.orcc.ir.Type;
import net.sf.orcc.ir.TypeInt;
import net.sf.orcc.ir.TypeList;
import net.sf.orcc.ir.TypeUint;
import net.sf.orcc.ir.Var;
import net.sf.orcc.ir.util.AbstractActorVisitor;

/**
 * This class defines a transformation that changes size of variable to fit
 * types of general-purpose programming language such as C, C++ or Java.
 * 
 * @author Jerome Gorin
 * @author Herve Yviquel
 * 
 */
public class TypeResizer extends AbstractActorVisitor<Object> {

    private boolean castToPow2bits;

    private boolean castTo32bits;

    private boolean castNativePort;

    private boolean castPort;

    public TypeResizer(boolean castToPow2bits, boolean castTo32bits, boolean castNativePort, boolean castPort) {
        super(true);
        this.castToPow2bits = castToPow2bits;
        this.castTo32bits = castTo32bits;
        this.castNativePort = castNativePort;
        this.castPort = castPort;
    }

    @Override
    public Object caseActor(Actor actor) {
        checkVariables(actor.getParameters());
        checkVariables(actor.getStateVars());
        if (castPort) {
            checkPorts(actor.getInputs());
            checkPorts(actor.getOutputs());
        }
        return super.caseActor(actor);
    }

    @Override
    public Object caseExprBinary(ExprBinary expr) {
        checkType(expr.getType());
        return super.caseExprBinary(expr);
    }

    @Override
    public Object caseExprUnary(ExprUnary expr) {
        checkType(expr.getType());
        return super.caseExprUnary(expr);
    }

    @Override
    public Object casePattern(Pattern pattern) {
        checkVariables(pattern.getVariables());
        return null;
    }

    @Override
    public Object caseProcedure(Procedure procedure) {
        checkParameters(procedure.getParameters());
        checkVariables(procedure.getLocals());
        checkType(procedure.getReturnType());
        return super.caseProcedure(procedure);
    }

    private void checkParameters(List<Param> parameters) {
        for (Param param : parameters) {
            checkType(param.getVariable().getType());
        }
    }

    private void checkPorts(List<Port> ports) {
        for (Port port : ports) {
            if (castNativePort || !port.isNative()) {
                checkType(port.getType());
            }
        }
    }

    private void checkType(Type type) {
        int size;
        if (type.isInt()) {
            TypeInt intType = (TypeInt) type;
            size = getIntSize(intType.getSize());
            intType.setSize(size);
        } else if (type.isUint()) {
            TypeUint uintType = (TypeUint) type;
            size = getIntSize(uintType.getSize());
            uintType.setSize(size);
        } else if (type.isList()) {
            TypeList listType = (TypeList) type;
            checkType(listType.getType());
        }
    }

    private void checkVariables(List<Var> vars) {
        for (Var var : vars) {
            checkType(var.getType());
        }
    }

    private int getIntSize(int size) {
        if (castToPow2bits) {
            if (size <= 8) {
                return 8;
            } else if (size <= 16) {
                return 16;
            } else if (size <= 32 || castTo32bits) {
                return 32;
            } else {
                return 64;
            }
        } else {
            if (size > 32) {
                if (castTo32bits) {
                    return 32;
                } else {
                    return 64;
                }
            } else {
                return size;
            }
        }
    }
}
