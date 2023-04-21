package net.sf.mailand.pop3.command;

import static org.junit.Assert.assertEquals;
import java.io.IOException;
import org.junit.Test;
import net.sf.mailand.MailUtil;
import net.sf.mailand.ParseException;
import net.sf.mailand.TestUtil;

public class DeleCommandTest {

    @Test
    public void testValidCreation() throws IOException, ParseException {
        DeleCommand deleCommand = (DeleCommand) CommandFactory.createCommand(TestUtil.convert("DELE 1" + MailUtil.CRLF));
        assertEquals(1, deleCommand.getMessageNumber());
        deleCommand = new DeleCommand(1);
        assertEquals(1, deleCommand.getMessageNumber());
    }

    @Test
    public void testTOString() {
        new DeleCommand(1).toString();
    }
}
