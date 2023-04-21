package de.java.com.office.word;

import com.jacob.com.Dispatch;
import com.jacob.com.Variant;
import de.java.com.office.AbstractOfficeObject;
import de.java.com.office.word.constants.WordMethods;
import de.java.com.office.word.constants.WordProperties;

/**
 * @author LuckySmile
 * @version $Date: $
 */
public class Columns extends AbstractOfficeObject {

    public Columns(Dispatch columns) {
        super(columns);
    }

    public long getCount() {
        Variant columnCount = getProperty(WordProperties.COUNT);
        return columnCount.getInt();
    }

    public Column getItem(int index) {
        Dispatch column = callMethod(WordMethods.ITEM, new Integer(index)).toDispatch();
        return new Column(column);
    }
}
