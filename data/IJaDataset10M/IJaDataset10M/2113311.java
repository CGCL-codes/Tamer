package com.newisys.langschema.constraint;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import com.newisys.langschema.ArrayAccess;
import com.newisys.langschema.Expression;
import com.newisys.langschema.java.JavaArrayType;
import com.newisys.langschema.java.JavaIntType;
import com.newisys.langschema.java.JavaType;

public final class ConsArrayAccess extends ConsExpression implements ArrayAccess {

    private final ConsExpression array;

    private final List indices = new LinkedList();

    public ConsArrayAccess(ConsExpression array) {
        super(array.schema);
        assert (array.getResultType() instanceof JavaArrayType);
        this.array = array;
    }

    public JavaType getResultType() {
        JavaArrayType arrayType = (JavaArrayType) array.getResultType();
        JavaType elementType = arrayType.getElementType();
        int dimensions = arrayType.getIndexTypes().length;
        int indexCount = indices.size();
        if (indexCount == dimensions) {
            return elementType;
        } else {
            assert (indexCount > 0 && indexCount <= dimensions);
            return schema.getArrayType(elementType, dimensions - indexCount);
        }
    }

    public Expression getArray() {
        return array;
    }

    public List getIndices() {
        return indices;
    }

    public void addIndex(ConsExpression expr) {
        assert (expr.getResultType() instanceof JavaIntType);
        indices.add(expr);
    }

    public String toSourceString() {
        StringBuffer buf = new StringBuffer();
        buf.append(array);
        Iterator iter = indices.iterator();
        while (iter.hasNext()) {
            buf.append('[');
            buf.append(iter.next());
            buf.append(']');
        }
        return buf.toString();
    }

    public boolean isConstant() {
        return false;
    }

    public void accept(ConsConstraintExpressionVisitor visitor) {
        visitor.visit(this);
    }
}
