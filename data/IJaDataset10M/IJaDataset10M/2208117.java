package name.vaccari.matteo.tai.lezione03;

import static org.junit.Assert.*;
import java.util.List;
import org.junit.Test;

public class TrisControllerTest {

    @Test
    public void testBlanks() throws Exception {
        String pathInfo = "/...";
        List<String> cells = new TrisController().execute(pathInfo, null);
        assertEquals(cells.get(0), "&nbsp;");
        assertEquals(cells.get(1), "&nbsp;");
        assertEquals(cells.get(2), "&nbsp;");
    }

    @Test
    public void testNaughtsAndCrosses() throws Exception {
        String pathInfo = "/X.0";
        List<String> cells = new TrisController().execute(pathInfo, null);
        assertEquals("X", cells.get(0));
        assertEquals("&nbsp;", cells.get(1));
        assertEquals("0", cells.get(2));
    }

    @Test
    public void testWithNextMove0() throws Exception {
        String pathInfo = "/X.0";
        List<String> cells = new TrisController().execute(pathInfo, "0");
        assertEquals("X", cells.get(0));
        assertEquals("0", cells.get(2));
        String newUrl = "X00?nextPlayer=X";
        assertEquals("<a href='" + newUrl + "'>0</a>", cells.get(1));
    }

    @Test
    public void testWithNextMoveX() throws Exception {
        String pathInfo = "/...";
        List<String> cells = new TrisController().execute(pathInfo, "X");
        String newUrl = "X..?nextPlayer=0";
        assertEquals("<a href='" + newUrl + "'>X</a>", cells.get(0));
    }
}
