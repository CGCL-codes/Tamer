package org.easypeas.mappings;

import java.util.List;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author S Owen
 */
public class PathTest {

    /**
     * Tests A/B/C = {Root:A, (Accessor:B, ID:B), (Endpoint:C, Accessor:C, ID:C), (Endpoint:index)}
     * 
     * @throws java.lang.Exception 
     */
    @Test
    public void simpleTest() throws Exception {
        Path path = new Path("A/B/C");
        assertEquals("Root should be A", "A", path.getRoot());
        assertEquals("There should be 3 possible elements", 3, path.getPossibleElements().size());
        List<Path.PathElement> possibleElements = path.getPossibleElements().get(0);
        assertEquals("There should be 2 possibilities for the first element", 2, possibleElements.size());
        Path.PathElement element = possibleElements.get(0);
        assertEquals(Path.PathType.ACCESSOR, element.type);
        assertEquals("B", element.name);
        element = possibleElements.get(1);
        assertEquals(Path.PathType.ID, element.type);
        assertEquals("B", element.name);
        possibleElements = path.getPossibleElements().get(1);
        assertEquals("There should be 3 possibilities for the 2nd element", 3, possibleElements.size());
        element = possibleElements.get(0);
        assertEquals(Path.PathType.ENDPOINT, element.type);
        assertEquals("C", element.name);
        element = possibleElements.get(1);
        assertEquals(Path.PathType.ACCESSOR, element.type);
        assertEquals("C", element.name);
        element = possibleElements.get(2);
        assertEquals(Path.PathType.ID, element.type);
        assertEquals("C", element.name);
        possibleElements = path.getPossibleElements().get(2);
        assertEquals("There should be 1 possibilities for the 3rd element", 1, possibleElements.size());
        element = possibleElements.get(0);
        assertEquals(Path.PathType.ENDPOINT, element.type);
        assertEquals("index", element.name);
    }

    /**
     * Tests that and empty path = {Root:index,Endpoint:index}
     * @throws java.lang.Exception 
     */
    @Test
    public void testEmptyPath() throws Exception {
        Path path = new Path("");
        assertEquals("Root should be index", "index", path.getRoot());
        assertEquals("There should be 1 element", 1, path.getPossibleElements().size());
        List<Path.PathElement> possibleElements = path.getPossibleElements().get(0);
        assertEquals("There should be 1 possibility for the first element", 1, possibleElements.size());
        Path.PathElement element = possibleElements.get(0);
        assertEquals(Path.PathType.ENDPOINT, element.type);
        assertEquals("index", element.name);
    }
}
