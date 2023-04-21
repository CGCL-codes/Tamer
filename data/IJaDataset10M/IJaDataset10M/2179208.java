package org.apache.hadoop.record.meta;

import java.io.IOException;
import org.apache.hadoop.record.RecordOutput;

/** 
 * Represents a type information for a field, which is made up of its 
 * ID (name) and its type (a TypeID object).
 */
public class FieldTypeInfo {

    private String fieldID;

    private TypeID typeID;

    /**
   * Construct a FiledTypeInfo with the given field name and the type
   */
    FieldTypeInfo(String fieldID, TypeID typeID) {
        this.fieldID = fieldID;
        this.typeID = typeID;
    }

    /**
   * get the field's TypeID object
   */
    public TypeID getTypeID() {
        return typeID;
    }

    /**
   * get the field's id (name)
   */
    public String getFieldID() {
        return fieldID;
    }

    void write(RecordOutput rout, String tag) throws IOException {
        rout.writeString(fieldID, tag);
        typeID.write(rout, tag);
    }

    /**
   * Two FieldTypeInfos are equal if ach of their fields matches
   */
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FieldTypeInfo)) return false;
        FieldTypeInfo fti = (FieldTypeInfo) o;
        if (!this.fieldID.equals(fti.fieldID)) {
            return false;
        }
        return (this.typeID.equals(fti.typeID));
    }

    /**
   * We use a basic hashcode implementation, since this class will likely not
   * be used as a hashmap key 
   */
    public int hashCode() {
        return 37 * 17 + typeID.hashCode() + 37 * 17 + fieldID.hashCode();
    }

    public boolean equals(FieldTypeInfo ti) {
        if (!this.fieldID.equals(ti.fieldID)) {
            return false;
        }
        return (this.typeID.equals(ti.typeID));
    }
}
