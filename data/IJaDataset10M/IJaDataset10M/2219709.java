package net.sf.osadm.linedata;

import java.util.HashSet;
import net.sf.osadm.linedata.domain.TableData;
import net.sourceforge.argval.ArgumentValidation;
import net.sourceforge.argval.impl.ArgumentValidationImpl;
import net.sourceforge.argval.message.impl.MessageItemImpl;

public class LineDataMessageItemImpl extends MessageItemImpl implements LineDataMessageItem {

    private TableData lineData;

    private String fieldName;

    public LineDataMessageItemImpl(TableData lineData, String fieldName, String priority, String message, Throwable cause) {
        super(priority, message, cause);
        ArgumentValidation argVal = new ArgumentValidationImpl();
        if (!argVal.isValidWhenNotNull("lineData", lineData)) {
            argVal.isValidWhenInSet("fieldName", fieldName, new HashSet<String>(lineData.getFieldNameList()));
        }
        if (argVal.containsIllegalArgument()) throw argVal.createIllegalArgumentException();
        this.lineData = lineData;
        this.fieldName = fieldName;
    }

    public TableData getLineData() {
        return lineData;
    }

    public String getFieldName() {
        return fieldName;
    }
}
