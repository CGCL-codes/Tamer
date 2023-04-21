package up2p.tspace;

import java.io.Serializable;
import lights.Field;
import lights.interfaces.IField;
import lights.interfaces.IValuedField;
import lights.utils.SerializeTuple;

/**
 *  This class is a Field with a value AND a name
 *  representing a key-value pair.
 *  
 *  A formal field with a non-null <i>varname</i> is for reading a value and storing it in the variable 
 *  specified by <i>varname</i>
 *  A litteral field with a non-null <i>varname</i> (and varname==value) is to be processed before output, 
 *  i.e. a value needs to be retrieved from previous processing.
 * @author alan
 *
 */
public class NameValueField extends Field {

    protected String varname;

    public IField setVarName(String name) {
        varname = name;
        return this;
    }

    public String getVarName() {
        return varname;
    }

    /** Returns a string representation of the tuple. */
    public String toString() {
        String result = null;
        if (varname != null) {
            if (isFormal()) result = "(?)" + varname; else result = "($)" + varname;
        } else result = super.toString();
        return result;
    }

    /**
	 *  Equals: determines if two Fields are identical (stronger than matching)
	 * @param o the field to compare to
	 * @return
	 */
    public boolean equals(Object o) {
        if (!(o instanceof IValuedField)) return false;
        IValuedField field = (IValuedField) o;
        boolean areEqual = type.equals(field.getType());
        if (formal) {
            return (areEqual && field.isFormal());
        } else if (((IValuedField) field).isFormal()) {
            return false;
        } else {
            if (varname != null) {
                if (!(o instanceof NameValueField)) return false; else areEqual = (areEqual && (varname.equals(((NameValueField) field).getVarName())));
            }
            return areEqual && value.equals(field.getValue());
        }
    }

    @Override
    public Object clone() {
        NameValueField field = new NameValueField();
        field.setType((Class<?>) SerializeTuple.getDeepCopy(this.type));
        if (!isFormal()) field.setValue(SerializeTuple.getDeepCopy((Serializable) this.value));
        if (varname != null) {
            String vn = new String(this.varname);
            field.setVarName(vn);
        }
        return field;
    }
}
