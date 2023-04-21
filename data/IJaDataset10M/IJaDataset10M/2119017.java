package org.apache.fop.fo.properties;

import java.util.Hashtable;
import org.apache.fop.datatypes.*;
import org.apache.fop.fo.*;
import org.apache.fop.apps.FOPException;
import org.apache.fop.messaging.MessageHandler;

public class LeaderPatternWidthMaker extends LengthProperty.Maker {

    public static Property.Maker maker(String propName) {
        return new LeaderPatternWidthMaker(propName);
    }

    protected LeaderPatternWidthMaker(String name) {
        super(name);
    }

    public boolean isInherited() {
        return true;
    }

    public Property make(PropertyList propertyList) throws FOPException {
        return make(propertyList, "use-font-metrics", propertyList.getParentFObj());
    }

    static Hashtable s_htKeywords;

    static {
        s_htKeywords = new Hashtable(1);
        s_htKeywords.put("use-font-metrics", "0pt");
    }

    protected String checkValueKeywords(String keyword) {
        String value = (String) s_htKeywords.get(keyword);
        if (value == null) {
            return super.checkValueKeywords(keyword);
        } else return value;
    }

    /** Return object used to calculate base Length
     * for percent specifications.
     */
    public PercentBase getPercentBase(final FObj fo, final PropertyList propertyList) {
        return new LengthBase(fo, propertyList, LengthBase.CONTAINING_BOX);
    }
}
