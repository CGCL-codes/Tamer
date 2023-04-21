package de.java.com.office.word;

import com.jacob.com.Dispatch;
import com.jacob.com.Variant;
import de.java.com.office.AbstractOfficeObject;
import de.java.com.office.word.constants.WordMethods;
import de.java.com.office.word.constants.WordProperties;

/**
 * 
 * @author LuckySmile
 * @version $Date: $
 */
public class Hyperlinks extends AbstractOfficeObject {

    public Hyperlinks(Dispatch hyperlinks) {
        super(hyperlinks);
    }

    public Hyperlink add(Range anchor, String address) {
        Dispatch hyperlink = callMethod(WordMethods.ADD, anchor, address).toDispatch();
        return new Hyperlink(hyperlink);
    }

    public Hyperlink getItem(int index) {
        Dispatch hyperlink = callMethod(WordMethods.ITEM, new Integer(index)).toDispatch();
        return new Hyperlink(hyperlink);
    }

    public long getCount() {
        Variant hyperlinksCount = getProperty(WordProperties.COUNT);
        return hyperlinksCount.getInt();
    }
}
