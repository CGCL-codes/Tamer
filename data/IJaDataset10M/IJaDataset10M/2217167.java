package net.sf.webwarp.util.types;

import static org.junit.Assert.assertEquals;
import java.util.Locale;
import net.sf.webwarp.util.types.Language;
import org.junit.Test;

public class LangaugeTest {

    @Test
    public void testLanguage() {
        assertEquals("Deutsch", Language.DEU.getLabel(new Locale("DE")));
    }
}
