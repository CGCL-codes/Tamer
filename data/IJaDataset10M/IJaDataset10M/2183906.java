package jayu;

public class Field {

    public static final String ROOTFIELD = "root";

    /** Long name of the field. In case of nested fields the longName contains
	 * the names of all parents, separated by 'dot'
	 * Example - callEventRecord.moCallRecord.servedIMSI 
	 * In this example servedIMSI is the field name, moCallRecord and callEventRecors 
	 * are its parent and grandparent field respectively. 
	 * */
    public String longName;

    /** name of the field */
    public String name;

    /** ASNClass of the field */
    public ASNClass type;

    /** Position of the field. 
	 * 
	 * The position at which the field is attached to its parent.
	 * as per the grammar file. If no position is specified in 
	 * grammar file then it is set to ASNConst.POS_NOT_SPECIFIED
	 * */
    public int pos;

    /** 
	 * TODO: Get rid of this variable, after cleanup.
	 * 
	 * Indicates that this field is an embedded field 
	 * Embedded mean that all the fields of this Field's ASNClass
	 * should behave as though they are a part of this Field's parent ASNClass.
	 * 
	 * Example - 
	 *  
	 * ClassA :: SET { 
	 * 	field_a1 [0] INTEGER
	 *  field_a2 [1] INTEGER
	 *  field_a3 [-1] ClassB      --Comment: -1 indicates embedding.
	 * }
	 * 
	 * ClassB :: SET {
	 *   field_b1 [2] INTEGER
	 *   field_b2 [3] INTEGER
	 * }
	 * 
	 * The above classDefination is equivalent to 
	 * 
	 * ClassA :: SET {
	 * 	field_a1 [0] INTEGER
	 *  field_a2 [1] INTEGER
	 *  field_b1 [2] INTEGER
	 *  field_b2 [3] INTEGER
	 * }
	 * 
	 **/
    static final int POS_EMBEDDED = -1;

    /** Reserved for other miscellaneous information */
    String extraInfo;

    Field cachedCloneNonArray;

    Field(String longName_, String name_, int pos_, ASNClass type_) {
        longName = longName_;
        name = name_;
        type = type_;
        pos = pos_;
    }

    public String toString() {
        return toConciseString();
    }

    /**
	 * @return String representation of the Field in a concise form.
	 */
    public String toConciseString() {
        String posStr = pos == ASNConst.POS_NOT_SPECIFIED ? "NO_POS" : ("" + pos);
        String ret = "Field [ " + longName + "(" + posStr + ") " + type.getName();
        if (type.isAssociatedWithTag()) {
            ret = ret + " p(" + type.getAssociatedTag() + ")";
        }
        ret = ret + " ]";
        return ret;
    }

    public boolean isArray() {
        return type.isArray();
    }

    public boolean isReference() {
        return type.isReference();
    }

    String toStringTree(int depth) {
        String ret = "";
        if (ASNConst.isPrimitive(type.name)) {
            ret += "\n" + Util.getSpace(depth) + type.getName() + " " + name + " " + longName + " [" + pos + "] ";
        } else {
            ret += "\n" + Util.getSpace(depth) + type.toStringTree(depth) + " " + name + " " + longName + " [" + pos + "] ";
        }
        return ret;
    }

    public Field clone() {
        ASNClass cloneClass = type.clone();
        Field retField = new Field(longName, name, pos, cloneClass);
        retField.extraInfo = this.extraInfo;
        return retField;
    }

    public Field cloneNonArray() {
        ASNClass cloneClass = type.getNonArrayClone();
        Field retField = new Field(longName, name, pos, cloneClass);
        retField.extraInfo = this.extraInfo;
        return retField;
    }

    /**
	 * @return cached Non Array Clone object.
	 * 
	 * The behavior is similar to that of calling cloneNonArray()
	 * except that repeated calls to this method will return from 
	 * cache. Though not enforced, it is expected that the returned 
	 * Field is used for read only purpose and not modified. 
	 * 
	 */
    public Field getCachedCloneNoneArray() {
        if (cachedCloneNonArray == null) {
            cachedCloneNonArray = cloneNonArray();
        }
        return cachedCloneNonArray;
    }

    /**
	 * Gets the child field at requested position.
	 * 
	 * @param sbpos - The position of the child field
	 * @return child Field that corresponds to sbpos, if no subField corresponds to sbpos returns null.
	 * @throws ASNException - If the calling Field object is a primitive. 
	 */
    public Field getChildField(int sbpos) {
        if (type.isPrimitive()) {
            throw new ASNException("getChildField(sbpos) called on a primitive field");
        }
        for (int i = 0; i < type.fields.length; i++) {
            Field subField = type.fields[i];
            if (subField.pos != ASNConst.POS_NOT_SPECIFIED) {
                if (type.fields[i].pos == sbpos) {
                    return subField;
                }
            } else {
                if (subField.type.getAssociatedTag() == sbpos) {
                    return subField;
                }
            }
        }
        return null;
    }

    /**
	 * Gets the child and Grand child field at requested position
	 * 
	 * @param sbpos - The position of the child field
	 * @return child and grandChild Fields that corresponds to sbpos.
	 *         if no corresponding grandChild field found it returns null.
	 *         else returns a array of Field of size 2.
	 *         In the returned Field[]
	 *         Field[0] - Contains the Child Field
	 *         Field[1] - Contains the Grand Child Field
	 *         
	 * @throws ASNException - If the calling Field object is a primitive. 
	 */
    public Field[] getGrandChildField(int sbpos) {
        if (type.isPrimitive()) {
            throw new ASNException("getChildField(sbpos) called on a primitive field");
        }
        for (int i = 0; i < type.fields.length; i++) {
            Field subField = type.fields[i];
            if (subField.isReference()) {
                Field grandChildField = subField.getChildField(sbpos);
                if (grandChildField != null) {
                    Field[] ret = new Field[2];
                    ret[0] = subField;
                    ret[1] = grandChildField;
                    return ret;
                }
            }
        }
        return null;
    }

    /**
	 * @return - First sub field which is 'an array' and whose pos is ASNConst.POS_NOT_SPECIFIED. If such a sub field
	 * does not exist it returns null.
	 * 
	 * @throws ASNException if the current field object is a primitive. 
	 *       
	 * Note - There can only be one such array subfield in any ASNClass, otherwise it will lead to ambiguous situation.
	 * The code doesn't do any check but simply returns the first occurrence of subfield that is 
	 * an array subfield and whose pos is ASNConst.POS_NOT_SPECIFIED.
	 *     
	 */
    public Field getArrayChildWithoutPos() {
        if (type.isPrimitive()) {
            throw new ASNException("getArrayChildWithoutPos() called on a primitive field");
        }
        for (int i = 0; i < type.fields.length; i++) {
            Field subField = type.fields[i];
            if (subField.isArray() && subField.pos == ASNConst.POS_NOT_SPECIFIED) {
                return subField;
            }
        }
        return null;
    }
}
