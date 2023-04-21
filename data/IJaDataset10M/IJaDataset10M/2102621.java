package net.sf.jexpel.internal.expr;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.List;
import net.sf.jexpel.Expression;
import net.sf.jexpel.ExpressionException;
import net.sf.jexpel.internal.compiler.Compilable;
import net.sf.jexpel.internal.compiler.CompilerUtils;
import net.sf.jexpel.messages.MessageCodes;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;

public class PropertyAccess implements Expression, Compilable, MessageCodes {

    Method get_method;

    Method set_method;

    Field field;

    Class<?> type;

    String propName;

    Expression root;

    Class<?> rootClass;

    boolean isstatic = false;

    public PropertyAccess(Class<?> root, String propName) throws ExpressionException {
        this.propName = propName;
        this.rootClass = root;
        String nm = propName.substring(0, 1).toUpperCase() + propName.substring(1);
        try {
            get_method = root.getMethod("get" + nm);
        } catch (Exception e) {
            try {
                get_method = root.getMethod("is" + nm);
            } catch (Exception e1) {
            }
        }
        if (get_method != null) {
            type = get_method.getReturnType();
        }
        String nmset = "set" + nm;
        try {
            if (type != null) {
                set_method = root.getMethod(nmset, type);
            } else {
                Method[] mths = root.getMethods();
                for (Method method : mths) {
                    if (method.getName().equals(nmset) && method.getParameterTypes().length == 1) {
                        set_method = method;
                        break;
                    }
                }
            }
        } catch (Exception e1) {
        }
        if (get_method == null || set_method == null) {
            try {
                field = root.getField(propName);
                type = field.getType();
            } catch (Exception e) {
            }
        }
        if (type == null && set_method != null) {
            type = set_method.getParameterTypes()[0];
        }
        if (get_method == null && set_method == null && field == null) throw ExpressionException.forInputString(INVALID);
    }

    public PropertyAccess(Expression root, String propName) throws ExpressionException {
        this(root.getType(), propName);
        this.root = root;
        isstatic = root instanceof ClassAccess;
    }

    public Object get(Object scope) throws Exception {
        if (root != null) scope = root.get(scope);
        if (isstatic) scope = null;
        if (get_method != null) {
            try {
                return get_method.invoke(scope);
            } catch (Exception e) {
                throw new ExpressionException(e);
            }
        } else if (field != null) {
            try {
                return field.get(scope);
            } catch (Exception e) {
                throw new ExpressionException(e);
            }
        }
        throw ExpressionException.forInputString(PROP_WO, propName);
    }

    public Class<?> getType() {
        return type;
    }

    public void set(Object scope, Object value) throws Exception {
        if (root != null) scope = root.get(scope);
        if (isstatic) scope = null;
        if (set_method == null && (field == null || (field.getModifiers() & Modifier.FINAL) == Modifier.FINAL)) throw ExpressionException.forInputString(PROP_RO, propName);
        if (set_method != null) {
            try {
                set_method.invoke(scope, value);
            } catch (Exception e) {
                throw new ExpressionException(e);
            }
        } else if (field != null) {
            try {
                field.set(scope, value);
            } catch (Exception e) {
                throw new ExpressionException(e);
            }
        }
    }

    public String toString() {
        return (root != null ? (root.toString() + ".") : "") + propName;
    }

    public void compileGet(MethodVisitor mv) throws Exception {
        if (root != null && !isstatic) {
            ((Compilable) root).compileGet(mv);
        } else if (!isstatic) {
            mv.visitVarInsn(ALOAD, 1);
            mv.visitTypeInsn(CHECKCAST, Type.getInternalName(rootClass));
        }
        if (get_method != null) {
            CompilerUtils.invokeMethod(mv, rootClass, get_method.getName(), new Class<?>[] {}, isstatic);
        } else {
            mv.visitFieldInsn(isstatic ? GETSTATIC : GETFIELD, Type.getInternalName(rootClass), this.propName, Type.getDescriptor(field.getType()));
        }
    }

    public void compileSet(MethodVisitor mv) throws Exception {
        if (root != null && !isstatic) {
            ((Compilable) root).compileGet(mv);
        } else if (!isstatic) {
            mv.visitVarInsn(ALOAD, 1);
            mv.visitTypeInsn(CHECKCAST, Type.getInternalName(rootClass));
        }
        if (type.isPrimitive()) {
            mv.visitVarInsn(ALOAD, 2);
            mv.visitTypeInsn(CHECKCAST, Type.getInternalName(type));
            CompilerUtils.convert2primitive(mv, CompilerUtils.getPrimitiveWrapper(type), type);
        } else {
            mv.visitVarInsn(ALOAD, 2);
            mv.visitTypeInsn(CHECKCAST, Type.getInternalName(type));
        }
        if (get_method != null) {
            CompilerUtils.invokeMethod(mv, rootClass, set_method.getName(), set_method.getParameterTypes(), isstatic);
        } else {
            mv.visitFieldInsn(isstatic ? PUTSTATIC : PUTFIELD, Type.getInternalName(rootClass), this.propName, Type.getDescriptor(field.getType()));
        }
    }

    public void calculeGet(List<String> objects) throws Exception {
        if (root != null && !isstatic) {
            ((Compilable) root).calculeGet(objects);
        }
    }

    public void calculeSet(List<String> objects) throws Exception {
        if (root != null && !isstatic) {
            ((Compilable) root).calculeGet(objects);
        }
    }

    public boolean isConstant() {
        return false;
    }
}
