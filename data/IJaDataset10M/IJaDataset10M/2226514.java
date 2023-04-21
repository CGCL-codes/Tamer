package salto.fwk.filter.click;

import java.util.Iterator;
import javax.servlet.http.HttpServletResponse;

public class DateHeader extends Header {

    public DateHeader(String name, Long value) {
        super(name, value);
    }

    public void addToResponse(HttpServletResponse res) {
        if (mValues.size() == 1) {
            res.setDateHeader(mName, ((Long) mValues.get(0)).longValue());
            return;
        }
        for (Iterator it = mValues.iterator(); it.hasNext(); ) {
            res.addDateHeader(mName, ((Long) it.next()).longValue());
        }
    }
}
