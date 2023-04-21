package de.java.com.office.word;

import com.jacob.com.Dispatch;
import com.jacob.com.Variant;
import de.java.com.office.AbstractOfficeObject;
import de.java.com.office.word.constants.WordProperties;

/**
 * @author LuckySmile
 * @version $Date: $
 */
public class DocumentProperty extends AbstractOfficeObject {

    public DocumentProperty(Dispatch property) {
        super(property);
    }

    public String getName() {
        Variant name = getProperty(WordProperties.NAME);
        return name.toString();
    }

    public String getValue() {
        Variant name = getProperty(WordProperties.VALUE);
        return name.toString();
    }

    public void setName(String value) {
        setProperty(WordProperties.NAME, value);
    }

    public void setValue(String value) {
        setProperty(WordProperties.VALUE, value);
    }
}
