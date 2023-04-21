package net.infordata.ifw2m.mdl.flds;

import net.infordata.ifw2.msgs.IMessage;
import net.infordata.ifw2.msgs.MessageTypeEnum;
import net.infordata.ifw2.msgs.SimpleMessage;

/**
 */
public class InRangeValidator<T extends Comparable<T>> implements IFieldSetValidator {

    private static final long serialVersionUID = 1L;

    private static enum MT {

        TK
    }

    private final String ivField;

    private final IMessage ivMsg;

    private final T ivFromValue;

    private final T ivToValue;

    private final String[] ivFieldNames;

    /** */
    public InRangeValidator(String field, T from, T to) {
        this(field, from, to, null);
    }

    /** */
    public InRangeValidator(String field, T from, T to, IMessage msg) {
        if (field == null) throw new NullPointerException();
        if (from != null && to != null && from.compareTo(to) > 0) throw new IllegalArgumentException("" + from + " > " + to);
        ivField = field;
        ivFromValue = from;
        ivToValue = to;
        if (msg == null) msg = new SimpleMessage(Messages.getString("InRangeValidator.2"), MessageTypeEnum.ERROR);
        ivMsg = msg;
        ivFieldNames = new String[] { ivField };
    }

    @Override
    public String[] getFieldNames() {
        return ivFieldNames;
    }

    @Override
    public boolean isApplicable() {
        return true;
    }

    @Override
    public void validate(IFieldSet fs, String... fieldsToValidate) {
        if (ivFromValue == null && ivToValue == null) return;
        IField<T> field = fs.<T>get(ivField);
        field.setMessage(MT.TK, null);
        T value = field.getValue();
        if (ivFromValue != null && value == null) {
            field.setMessage(MT.TK, ivMsg);
            return;
        }
        if (value == null) return;
        if (ivFromValue != null && ivFromValue.compareTo(value) > 0) {
            field.setMessage(MT.TK, ivMsg);
            return;
        }
        if (ivToValue != null && ivToValue.compareTo(value) < 0) {
            field.setMessage(MT.TK, ivMsg);
            return;
        }
    }

    @Override
    public boolean equals(IFieldSetValidator other) {
        return equals((Object) other);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((ivField == null) ? 0 : ivField.hashCode());
        result = prime * result + ((ivFromValue == null) ? 0 : ivFromValue.hashCode());
        result = prime * result + ((ivMsg == null) ? 0 : ivMsg.hashCode());
        result = prime * result + ((ivToValue == null) ? 0 : ivToValue.hashCode());
        return result;
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        final InRangeValidator other = (InRangeValidator) obj;
        if (ivField == null) {
            if (other.ivField != null) return false;
        } else if (!ivField.equals(other.ivField)) return false;
        if (ivFromValue == null) {
            if (other.ivFromValue != null) return false;
        } else if (!ivFromValue.equals(other.ivFromValue)) return false;
        if (ivMsg == null) {
            if (other.ivMsg != null) return false;
        } else if (!ivMsg.equals(other.ivMsg)) return false;
        if (ivToValue == null) {
            if (other.ivToValue != null) return false;
        } else if (!ivToValue.equals(other.ivToValue)) return false;
        return true;
    }
}
