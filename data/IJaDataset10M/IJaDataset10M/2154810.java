package presentation.com.sampleprj.common.displaytagdecorator;

import org.displaytag.decorator.ColumnDecorator;
import org.displaytag.exception.DecoratorException;

public class Text1ColumnDecorator implements ColumnDecorator {

    public String decorate(Object columnValue) throws DecoratorException {
        String val = (String) columnValue;
        StringBuffer chBoxStrBuffer = new StringBuffer();
        chBoxStrBuffer.append("<input name=\"textFieldList1\" type=\"text\" value=\"");
        chBoxStrBuffer.append(val);
        chBoxStrBuffer.append("\" >");
        return chBoxStrBuffer.toString();
    }
}
