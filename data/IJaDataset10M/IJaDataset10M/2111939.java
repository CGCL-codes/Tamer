package fd.edit.tests;

import static org.junit.Assert.*;
import java.io.File;
import org.eclipse.emf.common.notify.impl.AdapterFactoryImpl;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import fd2.provider.CaseOrRelationItemProvider;
import fd2.provider.DiagramItemProvider;

public class DiagramItemProviderTest {

    private DiagramItemProvider f;

    @Before
    public void setUp() throws Exception {
        f = new DiagramItemProvider(new AdapterFactoryImpl());
    }

    @After
    public void tearDown() throws Exception {
        f = null;
    }

    @Test
    public void testGetImageObject() {
        Object c = f.getImage(new Object());
        String actualImage = "Diagram.gif";
        assertTrue(c.toString().indexOf(actualImage) > 0);
        assertTrue(new File(c.toString().replace("file:/", "")).exists());
    }

    @Test
    public void testGetTextObject() {
        assertTrue(f.getText(null).equals("Diagram"));
    }
}
