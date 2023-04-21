package edu.rice.cs.cunit.classFile.attributes;

import edu.rice.cs.cunit.classFile.code.instructions.LineNumberTable;
import edu.rice.cs.cunit.classFile.constantPool.AUTFPoolInfo;
import edu.rice.cs.cunit.classFile.constantPool.ConstantPool;
import edu.rice.cs.cunit.util.Types;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents an abstract annotations attribute in a class file that can contain annotations for multiple entities,
 * like for method parameters or local variables.
 *
 * @author Mathias Ricken
 */
public abstract class AMultipleAnnotationsAttributeInfo extends AAnnotationsAttributeInfo {

    /**
     * Creates a new abstract multiple annotations attribute.
     * @param name name of attribute
     * @param data data array
     * @param cp constant pool
     */
    public AMultipleAnnotationsAttributeInfo(AUTFPoolInfo name, byte data[], ConstantPool cp) {
        super(name, data, cp);
    }

    /**
     * Return the number of entities.
     *
     * @return number of entities
     *
     * @throws ClassFormatError
     */
    public short getEntityCount() throws ClassFormatError {
        return _data[0];
    }

    /**
     * Return the list of entity annotations arrays.
     *
     * @return list of arrays of entity annotations.
     *
     * @throws ClassFormatError
     */
    public List<Annotation[]> getEntityAnnotations() throws ClassFormatError {
        ArrayList<Annotation[]> entityAnnots = new ArrayList<Annotation[]>();
        int index = 1;
        for (int entity = 0; entity < getEntityCount(); ++entity) {
            int count = Types.ushortFromBytes(_data, index);
            assert (count <= 0xffff);
            index += 2;
            Annotation[] ann = new Annotation[count];
            for (short i = 0; i < count; ++i) {
                ann[i] = new Annotation(_constantPool, _data, index);
                index += ann[i].getSize();
            }
            entityAnnots.add(ann);
        }
        return entityAnnots;
    }

    /**
     * Set the entity annotations list.
     *
     * @param entityAnnots list of arrays of entity anotations
     *
     * @throws ClassFormatError
     */
    public void setEntityAnnotations(List<Annotation[]> entityAnnots) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        baos.write((byte) entityAnnots.size());
        for (Annotation[] ann : entityAnnots) {
            baos.write(Types.bytesFromShort((short) ann.length));
            for (int i = 0; i < ann.length; ++i) {
                ann[i].writeToByteArrayOutputStream(_constantPool, baos);
            }
        }
        setData(baos.toByteArray());
    }

    /**
     * Return a human-readable version of this attribute.
     *
     * @return string
     */
    public String toString() {
        StringBuilder x = new StringBuilder();
        x.append(_name);
        x.append(" <");
        x.append(getEntityCount());
        x.append(" ");
        x.append(getEntityName());
        x.append("(s) { ");
        boolean first = true;
        for (Annotation[] entityAnnot : getEntityAnnotations()) {
            if (first) {
                first = false;
            } else {
                x.append(", ");
            }
            boolean firstAnn = true;
            for (Annotation ann : entityAnnot) {
                if (firstAnn) {
                    firstAnn = false;
                } else {
                    x.append(", ");
                }
                x.append(ann.toString());
            }
            x.append(" }");
        }
        x.append(" } >");
        return x.toString();
    }

    /**
     * Return the name of the entity, e.g. "parameter" or "local variable".
     * @return name of tne entity
     */
    public abstract String getEntityName();

    /**
     * Adjust program counter values contained in this attribute, starting at startPC, by adding deltaPC to them.
     *
     * @param startPC program counter to start at
     * @param deltaPC change in program counter values
     */
    public void adjustPC(int startPC, int deltaPC) {
    }

    /**
     * Translate the program counter values contained in this attribute from an old line number table to a new one.
     *
     * @param index      critical point (insertion or deletion point)
     * @param deltaIndex delta value to add to all old line numbers greater than the critical point
     * @param oldLnt     old line number table
     * @param newLnt     new line number table
     */
    public void translatePC(int index, int deltaIndex, LineNumberTable oldLnt, LineNumberTable newLnt) {
    }

    /**
     * Creates and returns a copy of this object.
     */
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
