package net.sf.stitch.test;

import org.testng.annotations.Test;
import net.sf.stitch.crud.MessagesMojo;

public class MessagesTest extends MessagesMojo {

    @Test
    public void testTranslation() {
        assert "Camel Case".equals(guessTranslation("camelCase"));
    }
}
