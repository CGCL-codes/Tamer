package net.sf.webwarp.util.spring;

import java.sql.Time;
import org.joda.time.format.ISODateTimeFormat;

/**
 * Spring editor support class using the ISODateTimeFormat timeElement implementation.
 * 
 * @author mos
 * @see ISODateTimeFormat#time()
 */
public class SQLTimeEditorSupport extends ADateEditorSupport {

    public SQLTimeEditorSupport() {
        super(ISODateTimeFormat.timeElementParser(), Time.class);
    }
}
