package org.jmlspecs.jir.binding;

import java.util.ArrayList;
import java.util.HashMap;
import org.jmlspecs.jir.ast.external.IJirAstConverterHelper;
import org.sireum.util.IProcedure;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.converters.SingleValueConverter;

public class BindingFactory {

    public static final JirNameBinding VOID_NAME_BINDING = BindingFactory.getNameBinding("V", "void");

    public static final JirNameBinding BOOLEAN_NAME_BINDING = BindingFactory.getNameBinding("Z", "boolean");

    public static final JirNameBinding BYTE_NAME_BINDING = BindingFactory.getNameBinding("B", "byte");

    public static final JirNameBinding CHAR_NAME_BINDING = BindingFactory.getNameBinding("C", "char");

    public static final JirNameBinding SHORT_NAME_BINDING = BindingFactory.getNameBinding("S", "short");

    public static final JirNameBinding INT_NAME_BINDING = BindingFactory.getNameBinding("I", "int");

    public static final JirNameBinding LONG_NAME_BINDING = BindingFactory.getNameBinding("J", "long");

    public static final JirNameBinding FLOAT_NAME_BINDING = BindingFactory.getNameBinding("F", "float");

    public static final JirNameBinding DOUBLE_NAME_BINDING = BindingFactory.getNameBinding("D", "double");

    @SuppressWarnings("unchecked")
    public static final JirTypeBinding VOID_TYPE_BINDING = new JirTypeBinding();

    @SuppressWarnings("unchecked")
    public static final JirTypeBinding BOOLEAN_TYPE_BINDING = new JirTypeBinding();

    @SuppressWarnings("unchecked")
    public static final JirTypeBinding BYTE_TYPE_BINDING = new JirTypeBinding();

    @SuppressWarnings("unchecked")
    public static final JirTypeBinding CHAR_TYPE_BINDING = new JirTypeBinding();

    @SuppressWarnings("unchecked")
    public static final JirTypeBinding SHORT_TYPE_BINDING = new JirTypeBinding();

    @SuppressWarnings("unchecked")
    public static final JirTypeBinding INT_TYPE_BINDING = new JirTypeBinding();

    @SuppressWarnings("unchecked")
    public static final JirTypeBinding LONG_TYPE_BINDING = new JirTypeBinding();

    @SuppressWarnings("unchecked")
    public static final JirTypeBinding FLOAT_TYPE_BINDING = new JirTypeBinding();

    @SuppressWarnings("unchecked")
    public static final JirTypeBinding DOUBLE_TYPE_BINDING = new JirTypeBinding();

    @SuppressWarnings("unchecked")
    public static final JirNaryOpBinding EVERYTHING_OP_BINDING = BindingFactory.getNaryOpBindingHelper(JirNaryOp.EVERYTHING);

    @SuppressWarnings("unchecked")
    public static final JirNaryOpBinding NOT_SPECIFIED_OP_BINDING = BindingFactory.getNaryOpBindingHelper(JirNaryOp.NOT_SPECIFIED);

    @SuppressWarnings("unchecked")
    private static final JirNaryOpBinding NOTHING_OP_BINDING = BindingFactory.getNaryOpBindingHelper(JirNaryOp.NOTHING);

    @SuppressWarnings("unchecked")
    private static final JirNaryOpBinding RESULT_OP_BINDING = BindingFactory.getNaryOpBindingHelper(JirNaryOp.RESULT);

    @SuppressWarnings("unchecked")
    private static final JirNaryOpBinding SAME_OP_BINDING = BindingFactory.getNaryOpBindingHelper(JirNaryOp.SAME);

    @SuppressWarnings("unchecked")
    private static final HashMap<String, JirTypeBinding> primitiveNameTypeBindingMap = new HashMap<String, JirTypeBinding>(10);

    static {
        BindingFactory.VOID_TYPE_BINDING.setTypeName(BindingFactory.VOID_NAME_BINDING);
        BindingFactory.BOOLEAN_TYPE_BINDING.setTypeName(BindingFactory.BOOLEAN_NAME_BINDING);
        BindingFactory.BYTE_TYPE_BINDING.setTypeName(BindingFactory.BYTE_NAME_BINDING);
        BindingFactory.CHAR_TYPE_BINDING.setTypeName(BindingFactory.CHAR_NAME_BINDING);
        BindingFactory.SHORT_TYPE_BINDING.setTypeName(BindingFactory.SHORT_NAME_BINDING);
        BindingFactory.INT_TYPE_BINDING.setTypeName(BindingFactory.INT_NAME_BINDING);
        BindingFactory.LONG_TYPE_BINDING.setTypeName(BindingFactory.LONG_NAME_BINDING);
        BindingFactory.FLOAT_TYPE_BINDING.setTypeName(BindingFactory.FLOAT_NAME_BINDING);
        BindingFactory.DOUBLE_TYPE_BINDING.setTypeName(BindingFactory.DOUBLE_NAME_BINDING);
        BindingFactory.primitiveNameTypeBindingMap.put(BindingFactory.VOID_NAME_BINDING.optionalSourceFullyQualifiedName, BindingFactory.VOID_TYPE_BINDING);
        BindingFactory.primitiveNameTypeBindingMap.put(BindingFactory.BOOLEAN_NAME_BINDING.optionalSourceFullyQualifiedName, BindingFactory.BOOLEAN_TYPE_BINDING);
        BindingFactory.primitiveNameTypeBindingMap.put(BindingFactory.BYTE_NAME_BINDING.optionalSourceFullyQualifiedName, BindingFactory.BYTE_TYPE_BINDING);
        BindingFactory.primitiveNameTypeBindingMap.put(BindingFactory.CHAR_NAME_BINDING.optionalSourceFullyQualifiedName, BindingFactory.CHAR_TYPE_BINDING);
        BindingFactory.primitiveNameTypeBindingMap.put(BindingFactory.SHORT_NAME_BINDING.optionalSourceFullyQualifiedName, BindingFactory.SHORT_TYPE_BINDING);
        BindingFactory.primitiveNameTypeBindingMap.put(BindingFactory.INT_NAME_BINDING.optionalSourceFullyQualifiedName, BindingFactory.INT_TYPE_BINDING);
        BindingFactory.primitiveNameTypeBindingMap.put(BindingFactory.LONG_NAME_BINDING.optionalSourceFullyQualifiedName, BindingFactory.LONG_TYPE_BINDING);
        BindingFactory.primitiveNameTypeBindingMap.put(BindingFactory.FLOAT_NAME_BINDING.optionalSourceFullyQualifiedName, BindingFactory.FLOAT_TYPE_BINDING);
        BindingFactory.primitiveNameTypeBindingMap.put(BindingFactory.DOUBLE_NAME_BINDING.optionalSourceFullyQualifiedName, BindingFactory.DOUBLE_TYPE_BINDING);
    }

    public static <T> JirVariableBinding<T> getAllFieldsBinding(final JirTypeBinding<T> declaringType, final boolean isStatic) {
        final JirFieldBinding<T> result = new JirFieldBinding<T>();
        result.setDeclaringTypeName(declaringType.getTypeName());
        result.setName("*");
        final JirTypeBinding<T> fieldType = new IProcedure<JirTypeBinding<T>>() {

            @Override
            public JirTypeBinding<T> proceed() {
                final JirTypeBinding<T> result = new JirTypeBinding<T>();
                result.setNumOfDimensions(0);
                result.setOptionalType(null);
                final JirNameBinding typeName = new JirNameBinding();
                typeName.setBinaryName("Ljava/lang/Object;");
                typeName.setOptionalSourceFullyQualifiedName("java.lang.Object");
                result.setTypeName(typeName);
                return result;
            }
        }.proceed();
        result.setType(fieldType);
        result.setIsStatic(isStatic);
        return result;
    }

    public static <T> JirArrayBinding<T> getArrayBinding(final JirTypeBinding<T> arrayType) {
        final JirArrayBinding<T> result = new JirArrayBinding<T>();
        result.setArrayType(arrayType);
        return result;
    }

    public static <T> JirTypeBinding<T> getArrayTypeBinding(final JirTypeBinding<T> jtb) {
        final JirTypeBinding<T> result = new JirTypeBinding<T>();
        result.setNumOfDimensions(jtb.getNumOfDimensions() + 1);
        result.setOptionalType(jtb.getOptionalType());
        result.setTypeName(jtb.getTypeName());
        return result;
    }

    public static <T> JirTypeBinding<T> getElementTypeBinding(final JirTypeBinding<T> jtb) {
        assert jtb.getNumOfDimensions() > 0;
        final JirTypeBinding<T> result = new JirTypeBinding<T>();
        result.setNumOfDimensions(jtb.getNumOfDimensions() - 1);
        result.setOptionalType(jtb.getOptionalType());
        result.setTypeName(jtb.getTypeName());
        return result;
    }

    public static <E, T> JirElementTypeOpBinding<E, T> getElementTypeOpBinding(final JirTypeBinding<T> jtb) {
        final JirElementTypeOpBinding<E, T> result = new JirElementTypeOpBinding<E, T>();
        result.setArrayType(jtb);
        return result;
    }

    public static <T> JirFieldBinding<T> getFieldBinding(final JirTypeBinding<T> declaringType, final String fieldName, final JirTypeBinding<T> fieldType, final boolean isStatic) {
        final JirFieldBinding<T> result = new JirFieldBinding<T>();
        result.setDeclaringTypeName(declaringType.getTypeName());
        result.setName(fieldName);
        result.setType(fieldType);
        result.setIsStatic(isStatic);
        return result;
    }

    public static <Expression> JirOpBinding getInstanceMethodOpBinding(final JirMethodBinding jmb, final Expression receiverExp, final ArrayList<Expression> argExps) {
        final JirInstanceMethodOpBinding<Expression> result = new JirInstanceMethodOpBinding<Expression>();
        result.setOp(JirMethodOp.DURATION);
        result.setMethodBinding(jmb);
        result.setReceiver(receiverExp);
        result.setArguments(argExps);
        return result;
    }

    public static <E, T> JirInvariantForOpBinding<E> getInvariantForOpBinding(final JirTypeBinding<T> jtb, final E e) {
        final JirInvariantForOpBinding<E> result = new JirInvariantForOpBinding<E>();
        result.setTypeName(jtb.getTypeName());
        result.setExpr(e);
        return result;
    }

    public static <T> JirIsInitializedOpBinding getIsInitializedOpBinding(final JirTypeBinding<T> jtb) {
        final JirIsInitializedOpBinding result = new JirIsInitializedOpBinding();
        result.setTypeName(jtb.getTypeName());
        return result;
    }

    public static <E> JirLabelOpBinding<E> getLabelBinding(final boolean isPositive, final String id, final E e) {
        final JirLabelOpBinding<E> result = new JirLabelOpBinding<E>();
        result.setIsPositive(isPositive);
        result.setLabelId(id);
        result.setArgument(e);
        return result;
    }

    public static <T> JirVariableBinding<T> getLocalBinding(final String sourceName, final String binaryName, final JirTypeBinding<T> type, final boolean isParam, final int slotIndex) {
        final JirLocalBinding<T> result = new JirLocalBinding<T>();
        result.setName(BindingFactory.getNameBinding(binaryName, sourceName));
        result.setType(type);
        result.setIsParameter(isParam);
        result.setSlotIndex(slotIndex);
        return result;
    }

    public static <T> JirMethodBinding getMethodBinding(final JirTypeBinding<T> declaringType, final String methodName, final String methodDesc, final boolean isStatic) {
        final JirMethodBinding result = new JirMethodBinding();
        result.setDeclaringTypeName(declaringType.getTypeName());
        result.setName(methodName);
        result.setSignature(methodDesc);
        result.setIsStatic(isStatic);
        return result;
    }

    public static JirNameBinding getNameBinding(final String binaryName) {
        final JirNameBinding result = new JirNameBinding();
        result.setBinaryName(binaryName);
        return result;
    }

    public static JirNameBinding getNameBinding(final String binaryName, final String sourceName) {
        final JirNameBinding result = new JirNameBinding();
        result.setBinaryName(binaryName);
        result.setOptionalSourceFullyQualifiedName(sourceName);
        return result;
    }

    @SuppressWarnings("unchecked")
    public static <E> JirNaryOpBinding<E> getNaryOpBinding(final JirNaryOp op, final E... es) {
        switch(op) {
            case EVERYTHING:
                return BindingFactory.EVERYTHING_OP_BINDING;
            case NOT_SPECIFIED:
                return BindingFactory.NOT_SPECIFIED_OP_BINDING;
            case NOTHING:
                return BindingFactory.NOTHING_OP_BINDING;
            case RESULT:
                return BindingFactory.RESULT_OP_BINDING;
            case SAME:
                return BindingFactory.SAME_OP_BINDING;
        }
        return BindingFactory.getNaryOpBindingHelper(op, es);
    }

    static <E> JirNaryOpBinding<E> getNaryOpBindingHelper(final JirNaryOp op, final E... es) {
        final JirNaryOpBinding<E> result = new JirNaryOpBinding<E>();
        result.setOp(op);
        if ((es != null) && (es.length > 0)) {
            final ArrayList<E> args = new ArrayList<E>(es.length);
            for (final E e : es) {
                args.add(e);
            }
            result.setOptionalArguments(args);
        }
        return result;
    }

    public static JirPositionInfo getPosInfo(final int startPosition, final int endPosition, final int beginLine, final int endLine, final int beginColumn, final int endColumn) {
        final JirPositionInfo result = new JirPositionInfo();
        result.setBeginColumn(beginColumn);
        result.setBeginLine(beginLine);
        result.setStartPosition(startPosition);
        result.setEndColumn(endColumn);
        result.setEndLine(endLine);
        result.setEndPosition(endPosition);
        return result;
    }

    @SuppressWarnings("unchecked")
    static <T> JirTypeBinding<T> getPrimitiveJirTypeBinding(final char binaryTypeCode) {
        switch(binaryTypeCode) {
            case 'V':
                return BindingFactory.VOID_TYPE_BINDING;
            case 'Z':
                return BindingFactory.BOOLEAN_TYPE_BINDING;
            case 'B':
                return BindingFactory.BYTE_TYPE_BINDING;
            case 'C':
                return BindingFactory.CHAR_TYPE_BINDING;
            case 'S':
                return BindingFactory.SHORT_TYPE_BINDING;
            case 'I':
                return BindingFactory.INT_TYPE_BINDING;
            case 'J':
                return BindingFactory.LONG_TYPE_BINDING;
            case 'F':
                return BindingFactory.FLOAT_TYPE_BINDING;
            case 'D':
                return BindingFactory.DOUBLE_TYPE_BINDING;
        }
        assert false;
        return null;
    }

    public static <T> JirTypeBinding<T> getPrimitiveTypeBinding(final String sourceName) {
        @SuppressWarnings("unchecked") final JirTypeBinding<T> result = BindingFactory.primitiveNameTypeBindingMap.get(sourceName);
        return result;
    }

    public static <E, T> JirQuantificationOpBinding<E, T> getQuantificationBinding(final JirQuantifier q, final ArrayList<JirVarDeclaration<T>> vds, final E range, final E predicate) {
        final JirQuantificationOpBinding<E, T> result = new JirQuantificationOpBinding<E, T>();
        result.setQuantifier(q);
        result.setVarDeclarations(vds);
        result.setPredicateExpr(predicate);
        result.setRangeExpr(range);
        return result;
    }

    public static <T> JirVariableBinding<T> getQuantifiedVariableBinding(final JirTypeBinding<T> varType, final String name) {
        final JirQuantifiedVariableBinding<T> result = new JirQuantifiedVariableBinding<T>();
        result.setType(varType);
        result.setName(name);
        return result;
    }

    public static <T> JirSpaceOpBinding getSpaceOpBinding(final JirTypeBinding<T> jtb) {
        final JirSpaceOpBinding result = new JirSpaceOpBinding();
        result.setTypeName(jtb.getTypeName());
        return result;
    }

    public static <Expression> JirOpBinding getStaticMethodOpBinding(final JirMethodBinding jmb, final ArrayList<Expression> argExps) {
        final JirStaticMethodOpBinding<Expression> result = new JirStaticMethodOpBinding<Expression>();
        result.setOp(JirMethodOp.DURATION);
        result.setMethodBinding(jmb);
        result.setArguments(argExps);
        return result;
    }

    public static <T> JirTypeBinding<T> getTypeBinding(final String binaryName, final String sourceName) {
        return BindingFactory.getTypeBinding(binaryName, sourceName, 0);
    }

    public static <T> JirTypeBinding<T> getTypeBinding(final String binaryName, final String sourceName, final int numOfDims) {
        @SuppressWarnings("unchecked") JirTypeBinding<T> result = numOfDims > 0 ? null : BindingFactory.primitiveNameTypeBindingMap.get(sourceName);
        if (result == null) {
            result = new JirTypeBinding<T>();
            result.setTypeName(BindingFactory.getNameBinding(binaryName, sourceName));
            result.setNumOfDimensions(numOfDims);
        }
        return result;
    }

    public static <E, T> JirTypeOfOpBinding<E, T> getTypeOfOpBinding(final JirTypeBinding<T> jtb, final E e) {
        final JirTypeOfOpBinding<E, T> result = new JirTypeOfOpBinding<E, T>();
        result.setArgument(e);
        result.setType(jtb);
        return result;
    }

    public static <T> JirTypeOpBinding<T> getTypeOpBinding(final JirTypeBinding<T> jtb) {
        final JirTypeOpBinding<T> result = new JirTypeOpBinding<T>();
        result.setType(jtb);
        return result;
    }

    public static <T> JirVarDeclaration<T> getVarDeclaration(final JirTypeBinding<T> type, final String name) {
        final JirVarDeclaration<T> result = new JirVarDeclaration<T>();
        result.setName(name);
        result.setType(type);
        return result;
    }

    public static <Expression, Type> String toString(final IJirAstConverterHelper<Expression, Type> ijach, final Class<Expression> clazzE, final Class<Type> clazzT, final JirBinding jb) {
        final XStream xs = new XStream();
        xs.registerConverter(new SingleValueConverter() {

            @Override
            @SuppressWarnings("unchecked")
            public boolean canConvert(final Class arg0) {
                return clazzE.isAssignableFrom(arg0);
            }

            @Override
            public Object fromString(final String arg0) {
                return null;
            }

            @Override
            @SuppressWarnings("unchecked")
            public String toString(final Object arg0) {
                return ijach.expressionToString((Expression) arg0);
            }
        });
        xs.registerConverter(new SingleValueConverter() {

            @Override
            @SuppressWarnings("unchecked")
            public boolean canConvert(final Class arg0) {
                return clazzT.isAssignableFrom(arg0);
            }

            @Override
            public Object fromString(final String arg0) {
                return null;
            }

            @Override
            @SuppressWarnings("unchecked")
            public String toString(final Object arg0) {
                return ijach.typeToString((Type) arg0);
            }
        });
        return xs.toXML(jb);
    }
}
