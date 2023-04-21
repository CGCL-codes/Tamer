package org.armedbear.lisp;

import static org.armedbear.lisp.Nil.NIL;
import static org.armedbear.lisp.Lisp.*;
import java.util.Arrays;

public final class ComplexVector extends AbstractVector {

    private int capacity;

    private int fillPointer = -1;

    private boolean isDisplaced;

    private LispObject[] elements;

    private LispArray array;

    private int displacement;

    public ComplexVector(int capacity) {
        elements = new LispObject[capacity];
        Arrays.fill(elements, Fixnum.ZERO);
        this.capacity = capacity;
    }

    public ComplexVector(int capacity, LispArray array, int displacement) {
        this.capacity = capacity;
        this.array = array;
        this.displacement = displacement;
        isDisplaced = true;
    }

    @Override
    public LispObject typeOf() {
        return list(SymbolConstants.VECTOR, T, Fixnum.makeFixnum(capacity));
    }

    @Override
    public LispObject classOf() {
        return BuiltInClass.VECTOR;
    }

    @Override
    public boolean hasFillPointer() {
        return fillPointer >= 0;
    }

    @Override
    public int getFillPointer() {
        return fillPointer;
    }

    @Override
    public void setFillPointer(int n) {
        fillPointer = n;
    }

    @Override
    public void setFillPointer(LispObject obj) throws ConditionThrowable {
        if (obj == T) fillPointer = capacity(); else {
            int n = obj.intValue();
            if (n > capacity()) {
                StringBuffer sb = new StringBuffer("The new fill pointer (");
                sb.append(n);
                sb.append(") exceeds the capacity of the vector (");
                sb.append(capacity());
                sb.append(").");
                error(new LispError(sb.toString()));
            } else if (n < 0) {
                StringBuffer sb = new StringBuffer("The new fill pointer (");
                sb.append(n);
                sb.append(") is negative.");
                error(new LispError(sb.toString()));
            } else fillPointer = n;
        }
    }

    @Override
    public boolean isDisplaced() {
        return isDisplaced;
    }

    @Override
    public LispObject arrayDisplacement() throws ConditionThrowable {
        LispObject value1, value2;
        if (array != null) {
            value1 = array;
            value2 = Fixnum.makeFixnum(displacement);
        } else {
            value1 = NIL;
            value2 = Fixnum.ZERO;
        }
        return LispThread.currentThread().setValues(value1, value2);
    }

    @Override
    public LispObject getElementType() {
        return T;
    }

    @Override
    public boolean isSimpleVector() {
        return false;
    }

    @Override
    public int capacity() {
        return capacity;
    }

    @Override
    public int size() {
        return fillPointer >= 0 ? fillPointer : capacity;
    }

    @Override
    public LispObject elt(int index) throws ConditionThrowable {
        final int limit = size();
        if (index < 0 || index >= limit) badIndex(index, limit);
        return AREF(index);
    }

    @Override
    public LispObject AREF(int index) throws ConditionThrowable {
        if (elements != null) {
            try {
                return elements[index];
            } catch (ArrayIndexOutOfBoundsException e) {
                badIndex(index, elements.length);
                return NIL;
            }
        } else {
            if (index < 0 || index >= capacity) badIndex(index, capacity);
            return array.AREF(index + displacement);
        }
    }

    @Override
    public LispObject AREF(LispObject index) throws ConditionThrowable {
        return AREF(index.intValue());
    }

    @Override
    public void aset(int index, LispObject newValue) throws ConditionThrowable {
        if (elements != null) {
            try {
                elements[index] = newValue;
            } catch (ArrayIndexOutOfBoundsException e) {
                badIndex(index, elements.length);
            }
        } else {
            if (index < 0 || index >= capacity) badIndex(index, capacity); else array.aset(index + displacement, newValue);
        }
    }

    @Override
    public LispObject subseq(int start, int end) throws ConditionThrowable {
        SimpleVector v = new SimpleVector(end - start);
        int i = start, j = 0;
        try {
            while (i < end) v.aset(j++, AREF(i++));
            return v;
        } catch (ArrayIndexOutOfBoundsException e) {
            return error(new TypeError("Array index out of bounds: " + i + "."));
        }
    }

    @Override
    public void fillVoid(LispObject obj) throws ConditionThrowable {
        for (int i = capacity; i-- > 0; ) elements[i] = obj;
    }

    @Override
    public void shrink(int n) throws ConditionThrowable {
        if (elements != null) {
            if (n < elements.length) {
                LispObject[] newArray = new LispObject[n];
                System.arraycopy(elements, 0, newArray, 0, n);
                elements = newArray;
                capacity = n;
                return;
            }
            if (n == elements.length) return;
        }
        error(new LispError());
    }

    @Override
    public LispObject reverse() throws ConditionThrowable {
        int length = size();
        SimpleVector result = new SimpleVector(length);
        int i, j;
        for (i = 0, j = length - 1; i < length; i++, j--) result.aset(i, AREF(j));
        return result;
    }

    @Override
    public LispObject nreverse() throws ConditionThrowable {
        if (elements != null) {
            int i = 0;
            int j = size() - 1;
            while (i < j) {
                LispObject temp = elements[i];
                elements[i] = elements[j];
                elements[j] = temp;
                ++i;
                --j;
            }
        } else {
            int length = size();
            LispObject[] data = new LispObject[length];
            int i, j;
            for (i = 0, j = length - 1; i < length; i++, j--) data[i] = AREF(j);
            elements = data;
            capacity = length;
            array = null;
            displacement = 0;
            isDisplaced = false;
            fillPointer = -1;
        }
        return this;
    }

    @Override
    public void vectorPushExtend(LispObject element) throws ConditionThrowable {
        if (fillPointer < 0) noFillPointer();
        if (fillPointer >= capacity) {
            ensureCapacity(capacity * 2 + 1);
        }
        aset(fillPointer++, element);
    }

    @Override
    public LispObject VECTOR_PUSH_EXTEND(LispObject element) throws ConditionThrowable {
        vectorPushExtend(element);
        return Fixnum.makeFixnum(fillPointer - 1);
    }

    @Override
    public LispObject VECTOR_PUSH_EXTEND(LispObject element, LispObject extension) throws ConditionThrowable {
        int ext = extension.intValue();
        if (fillPointer < 0) noFillPointer();
        if (fillPointer >= capacity) {
            ext = Math.max(ext, capacity + 1);
            ensureCapacity(capacity + ext);
        }
        aset(fillPointer, element);
        return Fixnum.makeFixnum(fillPointer++);
    }

    private final void ensureCapacity(int minCapacity) throws ConditionThrowable {
        if (elements != null) {
            if (capacity < minCapacity) {
                LispObject[] newArray = new LispObject[minCapacity];
                System.arraycopy(elements, 0, newArray, 0, capacity);
                elements = newArray;
                capacity = minCapacity;
            }
        } else {
            Debug.assertTrue(array != null);
            if (capacity < minCapacity || array.getTotalSize() - displacement < minCapacity) {
                elements = new LispObject[minCapacity];
                final int limit = Math.min(capacity, array.getTotalSize() - displacement);
                for (int i = 0; i < limit; i++) elements[i] = array.AREF(displacement + i);
                capacity = minCapacity;
                array = null;
                displacement = 0;
                isDisplaced = false;
            }
        }
    }

    @Override
    public LispVector adjustArray(int newCapacity, LispObject initialElement, LispObject initialContents) throws ConditionThrowable {
        if (initialContents != null) {
            LispObject[] newElements = new LispObject[newCapacity];
            if (initialContents.isList()) {
                LispObject list = initialContents;
                for (int i = 0; i < newCapacity; i++) {
                    newElements[i] = list.CAR();
                    list = list.CDR();
                }
            } else if (initialContents.isVector()) {
                for (int i = 0; i < newCapacity; i++) newElements[i] = initialContents.elt(i);
            } else error(new TypeError(initialContents, SymbolConstants.SEQUENCE));
            elements = newElements;
        } else {
            if (elements == null) {
                elements = new LispObject[newCapacity];
                final int limit = Math.min(capacity, newCapacity);
                for (int i = 0; i < limit; i++) elements[i] = array.AREF(displacement + i);
            } else if (capacity != newCapacity) {
                LispObject[] newElements = new LispObject[newCapacity];
                System.arraycopy(elements, 0, newElements, 0, Math.min(capacity, newCapacity));
                elements = newElements;
            }
            if (initialElement != null) for (int i = capacity; i < newCapacity; i++) elements[i] = initialElement;
        }
        capacity = newCapacity;
        array = null;
        displacement = 0;
        isDisplaced = false;
        return this;
    }

    @Override
    public LispVector adjustArray(int newCapacity, LispArray displacedTo, int displacement) throws ConditionThrowable {
        capacity = newCapacity;
        array = displacedTo;
        this.displacement = displacement;
        elements = null;
        isDisplaced = true;
        return this;
    }
}
