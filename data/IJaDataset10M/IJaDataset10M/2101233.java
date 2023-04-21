package org.sourceforge.jemm.itest.functionality;

import static org.junit.Assert.*;
import org.junit.Test;
import org.sourceforge.jemm.Session;
import org.sourceforge.jemm.itest.functionality.classes.UsesDate;

public class DateSerializedTest extends MemoryStoreSetupBase {

    @Test
    public void canUseDateAttributes() {
        UsesDate ud = new UsesDate();
        Session.setRoot("root", ud);
        assertNotNull(ud.getDate());
    }
}
