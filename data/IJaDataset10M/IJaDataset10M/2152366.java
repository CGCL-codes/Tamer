package org.mojo.kokomo;

import org.objectweb.asm.*;

public class JField extends JVariable {

    private JClass _class;

    private IExpression _obj;

    private int _access = Opcodes.ACC_PRIVATE;

    public JField(JClass clazz) {
        super();
        _class = clazz;
    }

    public JField(JClass clazz, String name, Type type, int access) {
        super(name, type);
        _class = clazz;
        _access = access;
    }

    public JField(String name, Type type, int access) {
        super(name, type);
        _access = access;
    }

    public JField(JClass clazz, Type type) {
        super(type);
        _class = clazz;
    }

    public JClass getJClass() {
        return _class;
    }

    public void setJClass(JClass clazz) {
        _class = clazz;
    }

    public IExpression getObject() {
        return _obj;
    }

    public void setObject(IExpression expr) {
        _obj = expr;
    }

    public int getAccess() {
        return _access;
    }

    public IExpression accessVar() {
        return new IExpression() {

            final IExpression fobj = _obj;

            public void weaveSnippet(JClass clazz, JMethod meth, MethodVisitor mv) {
                fobj.weaveSnippet(clazz, meth, mv);
                System.out.println(" GETFIELD " + _class.getName() + "  " + getName() + " " + getType().getDescriptor());
                mv.visitFieldInsn(Opcodes.GETFIELD, _class.getName(), getName(), getType().getDescriptor());
            }

            public Type getReturnType() {
                return getType();
            }

            public int getMaxStack() {
                return 1;
            }
        };
    }

    class StoreField implements ICodeSnippet {

        private IExpression _expr;

        public StoreField(IExpression expr) {
            _expr = expr;
        }

        public void weaveSnippet(JClass clazz, JMethod meth, MethodVisitor mv) {
            _obj.weaveSnippet(clazz, meth, mv);
            _expr.weaveSnippet(clazz, meth, mv);
            mv.visitFieldInsn(Opcodes.PUTFIELD, _class.getName(), getName(), getType().getDescriptor());
        }

        public int getMaxStack() {
            return _expr.getMaxStack();
        }
    }

    public ICodeSnippet storeVariable(IExpression expr) {
        return new StoreField(expr);
    }
}
