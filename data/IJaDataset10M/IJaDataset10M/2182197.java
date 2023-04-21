package com.versant.core.jdo.query.mem;

import com.versant.core.jdo.query.*;
import com.versant.core.jdo.query.MemVisitor;
import com.versant.core.metadata.FieldMetaData;
import com.versant.core.metadata.ClassMetaData;
import com.versant.core.metadata.MDStatics;
import com.versant.lib.bcel.generic.*;
import com.versant.lib.bcel.Constants;
import com.versant.core.common.BindingSupportImpl;
import com.versant.core.jdo.QueryStateWrapper;

/**
 * Compiles ordering expression to bytecode.
 */
public class ByteCodeQCompareVisitor implements MemVisitor {

    private ClassMetaData candidateClass;

    private ClassGen cg;

    private ConstantPoolGen cp;

    private InstructionList il;

    private MethodGen mg;

    private InstructionFactory factory;

    private boolean first = true;

    private static final String NAME_Q_STATE_WRAPPER = QueryStateWrapper.class.getName();

    private static final String M_NAME_GETSTATE = "getState";

    private static final String NAME_COMPARABLE = Comparable.class.getName();

    private static final Type[] ARG_TYPES_INT = new Type[] { Type.INT };

    private static final Type[] ARG_TYPES_OBJECT = new Type[] { Type.OBJECT };

    private int stateParamNo = 1;

    private boolean ascending = false;

    private int var1Index;

    public ByteCodeQCompareVisitor(ClassGen classGen, InstructionFactory factory, String name, ClassMetaData candidateClass) {
        this.candidateClass = candidateClass;
        cg = classGen;
        this.factory = factory;
        cp = cg.getConstantPool();
        this.il = new InstructionList();
        mg = new MethodGen(Constants.ACC_PUBLIC, Type.INT, new Type[] { new ObjectType(NAME_Q_STATE_WRAPPER), new ObjectType(NAME_Q_STATE_WRAPPER) }, new String[] { "state1", "state2" }, "compare", name, this.il, cp);
        il.append(InstructionConstants.ICONST_0);
        LocalVariableGen var1 = mg.addLocalVariable("compResult", Type.INT, null, null);
        var1Index = var1.getIndex();
        il.append(new ISTORE(var1Index));
        var1.setStart(il.append(new NOP()));
    }

    public void finish() {
        il.append(new ILOAD(var1Index));
        il.append(InstructionConstants.IRETURN);
        mg.removeNOPs();
        mg.setMaxLocals();
        mg.setMaxStack();
        cg.addMethod(mg.getMethod());
        il.dispose();
    }

    public Field visitNode(Node node, Object obj) {
        throw BindingSupportImpl.getInstance().notImplemented(null);
    }

    /**
     * Must refactor
     * @param node
     * @param obj
     * @return
     */
    public Field visitLiteralNode(LiteralNode node, Object obj) {
        throw BindingSupportImpl.getInstance().notImplemented(null);
    }

    /**
     * This may be an state field on a state eg person.person
     * @param node
     * @param obj
     * @return
     */
    public Field visitFieldNavNode(FieldNavNode node, Object obj) {
        return visitStateFieldNavNodeRoot(node, candidateClass);
    }

    private Field visitStateFieldNavNodeRoot(FieldNavNode node, ClassMetaData currentClass) {
        FieldMetaData f = null;
        f = currentClass.getFieldMetaData(node.lexeme);
        if (f == null) {
            throw BindingSupportImpl.getInstance().runtime("Class " + currentClass + " does not have a field " + node.lexeme);
        }
        switch(f.category) {
            case FieldMetaData.CATEGORY_REF:
                InstructionHandle ih = null;
                il.append(new ALOAD(stateParamNo));
                il.append(new PUSH(cp, f.stateFieldNo));
                il.append(factory.createInvoke(NAME_Q_STATE_WRAPPER, M_NAME_GETSTATE, new ObjectType(NAME_Q_STATE_WRAPPER), ARG_TYPES_INT, Constants.INVOKEVIRTUAL));
                first = false;
                if (node.childList instanceof FieldNavNode) {
                    visitStateFieldNavNode((FieldNavNode) node.childList, f.typeMetaData, ih);
                } else {
                    visitFieldNode((FieldNode) node.childList, f.typeMetaData);
                }
                break;
            default:
                throw BindingSupportImpl.getInstance().internal("Only PersistenceCapable fields can be navigated");
        }
        return null;
    }

    private Field visitStateFieldNavNode(FieldNavNode node, ClassMetaData currentClass, InstructionHandle ih) {
        Field result = null;
        FieldMetaData f = currentClass.getFieldMetaData(node.lexeme);
        if (f == null) {
            throw BindingSupportImpl.getInstance().runtime("Class " + currentClass + " does not have a field " + node.lexeme);
        }
        switch(f.category) {
            case FieldMetaData.CATEGORY_REF:
                il.append(new PUSH(cp, f.stateFieldNo));
                il.append(factory.createInvoke(NAME_Q_STATE_WRAPPER, M_NAME_GETSTATE, new ObjectType(NAME_Q_STATE_WRAPPER), ARG_TYPES_INT, Constants.INVOKEVIRTUAL));
                if (node.childList instanceof FieldNavNode) {
                    result = visitStateFieldNavNode((FieldNavNode) node.childList, f.typeMetaData, ih);
                } else {
                    result = visitFieldNode((FieldNode) node.childList, f.typeMetaData);
                }
                break;
            default:
                throw BindingSupportImpl.getInstance().internal("Only PersistenceCapable fields can be navigated");
        }
        return result;
    }

    public Field visitMethodNode(MethodNode node, Object obj) {
        throw BindingSupportImpl.getInstance().notImplemented(null);
    }

    public Field visitPrimaryExprNode(PrimaryExprNode node, Object obj) {
        return null;
    }

    public Field visitFieldNode(FieldNode node, ClassMetaData cmd) {
        if (cmd == null) {
            return visitFieldNodeImp(candidateClass.getFieldMetaData(node.lexeme), node);
        } else {
            return visitFieldNodeImp(cmd.getFieldMetaData(node.lexeme), node);
        }
    }

    private Field visitFieldNodeImp(FieldMetaData fmd, FieldNode node) {
        if (fmd == null) {
            throw BindingSupportImpl.getInstance().runtime("Class " + candidateClass.qname + " does not have a field: " + node.lexeme);
        }
        if (first) {
            first = false;
            il.append(new ALOAD(stateParamNo));
            il.append(new PUSH(cp, fmd.stateFieldNo));
        } else {
            il.append(new PUSH(cp, fmd.stateFieldNo));
        }
        il.append(factory.createInvoke(NAME_Q_STATE_WRAPPER, fmd.stateGetMethodName, getBCellStateFieldType(fmd), ARG_TYPES_INT, Constants.INVOKEVIRTUAL));
        if (stateParamNo == 2) {
            if (fmd.type.isPrimitive()) {
                switch(fmd.typeCode) {
                    case MDStatics.BYTE:
                    case MDStatics.SHORT:
                    case MDStatics.CHAR:
                    case MDStatics.INT:
                        il.append(InstructionConstants.ISUB);
                        break;
                    case MDStatics.LONG:
                        il.append(InstructionConstants.LSUB);
                        il.append(InstructionConstants.L2I);
                        break;
                    case MDStatics.FLOAT:
                        il.append(factory.createInvoke(Float.class.getName(), "compare", Type.INT, new Type[] { Type.FLOAT, Type.FLOAT }, Constants.INVOKEVIRTUAL));
                        break;
                    case MDStatics.DOUBLE:
                        il.append(factory.createInvoke(Double.class.getName(), "compare", Type.INT, new Type[] { Type.DOUBLE, Type.DOUBLE }, Constants.INVOKEVIRTUAL));
                        break;
                    default:
                        throw BindingSupportImpl.getInstance().notImplemented(null);
                }
            } else {
                il.append(factory.createInvoke(NAME_COMPARABLE, "compareTo", Type.INT, ARG_TYPES_OBJECT, Constants.INVOKEINTERFACE));
            }
            if (ascending) {
                il.append(InstructionConstants.ICONST_M1);
                il.append(InstructionConstants.IMUL);
            }
            il.append(new ISTORE(var1Index));
            il.append(new ILOAD(var1Index));
            BranchInstruction bInstr = new IFEQ(null);
            il.append(bInstr);
            il.append(new ILOAD(var1Index));
            il.append(InstructionConstants.IRETURN);
            InstructionHandle endHandle = il.append(InstructionConstants.NOP);
            bInstr.setTarget(endHandle);
        }
        return null;
    }

    public Field visitFieldNode(FieldNode node, Object obj) {
        return visitFieldNodeImp(candidateClass.getFieldMetaData(node.lexeme), node);
    }

    private static final Type getBCellStateFieldType(FieldMetaData fmd) {
        switch(fmd.category) {
            case MDStatics.CATEGORY_SIMPLE:
                return getTypeFromTypeCode(fmd.typeCode);
            default:
                return Type.OBJECT;
        }
    }

    private static Type getTypeFromTypeCode(int typeCode) {
        switch(typeCode) {
            case MDStatics.INT:
                return Type.INT;
            case MDStatics.LONG:
                return Type.LONG;
            case MDStatics.SHORT:
                return Type.SHORT;
            case MDStatics.STRING:
                return Type.STRING;
            case MDStatics.BOOLEAN:
                return Type.BOOLEAN;
            case MDStatics.BYTE:
                return Type.BYTE;
            case MDStatics.CHAR:
                return Type.CHAR;
            case MDStatics.DOUBLE:
                return Type.DOUBLE;
            case MDStatics.FLOAT:
                return Type.FLOAT;
            default:
                return Type.OBJECT;
        }
    }

    public Field visitEqualNode(EqualNode node, Object obj) {
        throw BindingSupportImpl.getInstance().notImplemented(null);
    }

    public Field visitNotEqualNode(NotEqualNode node, Object obj) {
        throw BindingSupportImpl.getInstance().notImplemented(null);
    }

    public Field visitLikeNode(LikeNode node, Object obj) {
        return null;
    }

    public Field visitAndNode(AndNode node, Object obj) {
        throw BindingSupportImpl.getInstance().notImplemented(null);
    }

    public Field visitOrNode(OrNode node, Object obj) {
        throw BindingSupportImpl.getInstance().notImplemented(null);
    }

    public Field visitMultiplyNode(MultiplyNode node, Object obj) {
        throw BindingSupportImpl.getInstance().notImplemented(null);
    }

    public Field visitAddNode(AddNode node, Object obj) {
        throw BindingSupportImpl.getInstance().notImplemented(null);
    }

    public Field visitUnaryOpNode(UnaryOpNode node, Object obj) {
        throw BindingSupportImpl.getInstance().notImplemented(null);
    }

    public Field visitCompareOpNode(CompareOpNode node, Object obj) {
        throw BindingSupportImpl.getInstance().notImplemented(null);
    }

    public Field visitUnaryNode(UnaryNode node, Object obj) {
        node.childList.visit(this, obj);
        return null;
    }

    public Field visitBinaryNode(BinaryNode node, Object obj) {
        return null;
    }

    public Field visitMultiNode(Node node, Object obj) {
        return null;
    }

    public Field visitCastNode(CastNode node, Object obj) {
        return null;
    }

    public Field visitParamNode(ParamNode node, Object obj) {
        throw BindingSupportImpl.getInstance().notImplemented(null);
    }

    public Field visitParamNodeProxy(ParamNodeProxy node, Object obj) {
        return node.getParamNode().visit(this, obj);
    }

    public Field visitArgNode(ArgNode node, Object obj) {
        return null;
    }

    public Field visitArrayNode(ArrayNode node, Object obj) {
        return null;
    }

    public Field visitImportNode(ImportNode node, Object obj) {
        return null;
    }

    public Field visitLeafNode(LeafNode node, Object obj) {
        return null;
    }

    public Field visitOrderNode(OrderNode node, Object obj) {
        if (node.order == OrderNode.ORDER_ASCENDING) {
            ascending = true;
        } else {
            ascending = false;
        }
        first = true;
        stateParamNo = 1;
        node.childList.visit(this, obj);
        first = true;
        stateParamNo = 2;
        node.childList.visit(this, obj);
        return null;
    }

    public Field visitVarNode(VarNode node, Object obj) {
        return null;
    }

    public Field visitVarNodeProxy(VarNodeProxy node, Object obj) {
        return node.getVarNode().visit(this, obj);
    }

    public Field visitReservedFieldNode(ReservedFieldNode node, Object obj) {
        return null;
    }
}
