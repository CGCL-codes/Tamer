package net.infordata.ifw2.web.form;

import java.io.Serializable;
import net.infordata.ifw2.msgs.IMessage;
import net.infordata.ifw2.msgs.MessageTypeEnum;
import net.infordata.ifw2.msgs.SimpleMessage;

/**
 * Checks that the provided fields values define a valid open-ended range.<br>
 * Make the same fields mandatory to have a closed range.<br>
 * Values must implement the {@link Comparable} interface. 
 * 
 * @author valentino.proietti
 */
public class RangeValidator implements IFormValidator, Serializable {

    private static final long serialVersionUID = 1L;

    private static enum MT {

        TK
    }

    private final String ivFrom;

    private final String ivTo;

    private final IMessage ivMsg;

    private final String[] ivFieldNames;

    /** */
    public RangeValidator(String fromField, String toField) {
        this(fromField, toField, null);
    }

    /** */
    public RangeValidator(String fromField, String toField, IMessage msg) {
        if (fromField == null || toField == null) throw new IllegalArgumentException();
        ivFrom = fromField;
        ivTo = toField;
        if (msg == null) msg = new SimpleMessage(Messages.getString("RangeValidator.0"), MessageTypeEnum.ERROR);
        ivMsg = msg;
        ivFieldNames = new String[] { ivFrom, ivTo };
    }

    @Override
    public String[] getFieldNames() {
        return ivFieldNames;
    }

    @Override
    public boolean isApplicable() {
        return true;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public void validate(IFormView fs, String... fieldsToValidate) {
        AFormField fromFld = fs.getField(ivFrom);
        AFormField toFld = fs.getField(ivTo);
        fromFld.setMessage(MT.TK, null);
        toFld.setMessage(MT.TK, null);
        Comparable from = (fromFld.hasMessage(MessageTypeEnum.ERROR)) ? null : (Comparable) fromFld.getValue();
        Comparable to = (toFld.hasMessage(MessageTypeEnum.ERROR)) ? null : (Comparable) toFld.getValue();
        if (from == null && to == null) return;
        if (from == null && to != null || from != null && to == null) return;
        if (from.compareTo(to) <= 0) return;
        fromFld.setMessage(MT.TK, ivMsg);
        toFld.setMessage(MT.TK, ivMsg);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((ivFrom == null) ? 0 : ivFrom.hashCode());
        result = prime * result + ((ivMsg == null) ? 0 : ivMsg.hashCode());
        result = prime * result + ((ivTo == null) ? 0 : ivTo.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        final RangeValidator other = (RangeValidator) obj;
        if (ivFrom == null) {
            if (other.ivFrom != null) return false;
        } else if (!ivFrom.equals(other.ivFrom)) return false;
        if (ivMsg == null) {
            if (other.ivMsg != null) return false;
        } else if (!ivMsg.equals(other.ivMsg)) return false;
        if (ivTo == null) {
            if (other.ivTo != null) return false;
        } else if (!ivTo.equals(other.ivTo)) return false;
        return true;
    }
}
