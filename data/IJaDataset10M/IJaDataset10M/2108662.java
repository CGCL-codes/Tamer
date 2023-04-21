package test.org.wsmoss.core;

import java.text.SimpleDateFormat;
import java.util.Date;
import org.springframework.mock.web.MockHttpSession;

/**
 * The Class MyMockHttpSession.
 */
public class MyMockHttpSession extends MockHttpSession {

    /** The new id. */
    private String newId = null;

    public String getId() {
        if (newId == null) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
            this.newId = sdf.format(new Date()) + "_" + super.getId();
        }
        return this.newId;
    }
}
