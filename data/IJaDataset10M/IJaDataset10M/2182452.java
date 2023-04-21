package jimagick.utils;

import java.io.File;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import junit.framework.TestCase;

public class ImageFileFilterTest extends TestCase {

    private ImageFileFilter iff;

    private File file1, file2, file3;

    boolean accepted;

    @Before
    public void setUp() throws Exception {
        iff = new ImageFileFilter();
        file1 = new File("img.jpg");
        file2 = new File("img.png");
        file3 = new File("prova.txt");
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public final void testAccept() {
        accepted = iff.accept(file1, file1.getName());
        assertEquals(true, accepted);
        accepted = iff.accept(file2, file2.getName());
        assertEquals(true, accepted);
        accepted = iff.accept(file3, file3.getName());
        assertEquals(false, accepted);
    }
}
