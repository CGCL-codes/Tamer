package components;

import java.io.DataInput;
import java.io.IOException;
import consts.Const;
import util.DataFormatException;
import util.ValidationException;

public abstract class ConstantObject extends ClassComponent implements Cloneable {

    public int references = 0;

    public int ldcReferences = 0;

    public boolean shared = false;

    public ConstantPool containingPool;

    public ClassInfo containingClass;

    public int index;

    public int tag;

    public int nSlots = 1;

    public ConstantObject(int tag) {
        this.tag = tag;
    }

    public void incldcReference() {
        ldcReferences++;
    }

    public void decldcReference() {
        ldcReferences--;
    }

    public void clearldcReference() {
        ldcReferences = 0;
    }

    public void incReference() {
        references++;
    }

    public void decReference() {
        references--;
    }

    public void clearReference() {
        references = 0;
    }

    public Object clone() {
        ConstantObject newC;
        try {
            newC = (ConstantObject) super.clone();
            newC.references = 0;
            newC.shared = false;
            newC.index = 0;
            return newC;
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
            System.exit(1);
        }
        return null;
    }

    public void validate() {
        if ((references == 0) && (ldcReferences == 0)) {
            throw new ValidationException("Unreferenced constant", this);
        }
        if (shared) {
            int upperBound = containingPool.n;
            if (this.index <= 0 || this.index >= upperBound) {
                throw new ValidationException("Constant index out of range", this);
            }
            if (this != containingPool.enumedEntries.elementAt(this.index)) {
                throw new ValidationException("Constant index incorrect", this);
            }
        } else {
            ConstantPool cp = containingClass.getConstantPool();
            int upperBound = cp.getLength();
            if (this.index <= 0 || this.index >= upperBound) {
                throw new ValidationException("Constant index out of range", this);
            }
            if (this != cp.elementAt(this.index)) {
                throw new ValidationException("Constant index incorrect", this);
            }
        }
    }

    public int getIndex() {
        return index;
    }

    public abstract boolean isResolved();

    public String prettyPrint() {
        return this.toString();
    }

    /**
     * Factory method for creating ConstantObjects from the classfile
     * constant pool entries data stream.
     */
    public static ConstantObject readObject(DataInput in) throws IOException {
        int tag = in.readUnsignedByte();
        switch(tag) {
            case Const.CONSTANT_UTF8:
                return UnicodeConstant.read(in);
            case Const.CONSTANT_INTEGER:
            case Const.CONSTANT_FLOAT:
                return SingleValueConstant.read(tag, in);
            case Const.CONSTANT_DOUBLE:
            case Const.CONSTANT_LONG:
                return DoubleValueConstant.read(tag, in);
            case Const.CONSTANT_STRING:
                return StringConstant.read(in);
            case Const.CONSTANT_NAMEANDTYPE:
                return NameAndTypeConstant.read(in);
            case Const.CONSTANT_CLASS:
                return ClassConstant.read(in);
            case Const.CONSTANT_FIELD:
                return FieldConstant.read(in);
            case Const.CONSTANT_METHOD:
                return MethodConstant.read(in);
            case Const.CONSTANT_INTERFACEMETHOD:
                return InterfaceMethodConstant.read(in);
            default:
                throw new DataFormatException("Format error (constant tag " + tag + " )");
        }
    }
}
