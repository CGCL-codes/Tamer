package hermes.fix;

import java.util.Map;
import quickfix.Field;
import quickfix.field.SenderCompID;
import quickfix.field.TargetCompID;

/**
 * @author colincrist@hermesjms.com
 * @version $Id$
 */
public class VerboseFIXPrettyPrinter implements FIXPrettyPrinter {

    public String print(FIXMessage message) {
        StringBuffer rval = new StringBuffer();
        StringBuffer line = new StringBuffer();
        line.append(message.getString(SenderCompID.FIELD)).append(" -> ").append(message.getString(TargetCompID.FIELD)).append("\n");
        Map<Integer, Field> fields = message.getAllFields();
        for (final Map.Entry<Integer, Field> entry : fields.entrySet()) {
            final Field field = entry.getValue();
            line.append("    ");
            Object fieldValue = message.getObject(entry.getValue());
            String fieldValueName = message.getDictionary().getValueName(field.getTag(), fieldValue.toString());
            String tagText = message.getDictionary().getFieldName(field.getTag()) + " <" + field.getTag() + "> = " + fieldValue.toString();
            if (fieldValueName != null) {
                tagText = tagText + " <" + fieldValueName + ">";
            }
            line.append(tagText);
            line.append("\n");
        }
        if (line.length() > 0) {
            rval.append(line);
        }
        return rval.toString();
    }
}
