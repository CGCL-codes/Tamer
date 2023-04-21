package atgant;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class ModuleFilterTest {

    private AtgModule base;

    private AtgModule proj1;

    private AtgModule proj2;

    private AtgModule bigproj_one;

    private AtgModule bigproj_deep_four;

    @Before
    public void setup() {
        AtgSystem atgSystem = new AtgSystem("/Users/piran/Documents/projects/atgant/workspace/AtgAnt/testAtg");
        base = atgSystem.getModule("base");
        proj1 = atgSystem.getModule("proj1");
        proj2 = atgSystem.getModule("proj2");
        bigproj_deep_four = atgSystem.getModule("bigproj.deep.four");
        bigproj_one = atgSystem.getModule("bigproj.one");
    }

    @Test
    public void testSimpleFilter() throws Exception {
        ModuleFilter mf = ModuleFilter.parseStringList("base,proj2");
        assertTrue(mf.match(base));
        assertFalse(mf.match(proj1));
        assertTrue(mf.match(proj2));
    }

    @Test
    public void testWildCard() throws Exception {
        ModuleFilter mf = ModuleFilter.parseStringList("proj*");
        assertFalse(mf.match(base));
        assertTrue(mf.match(proj1));
        assertTrue(mf.match(proj2));
    }

    @Test
    public void testWildCardShallowNotDirecotry() throws Exception {
        ModuleFilter mf = ModuleFilter.parseStringList("bigproj*");
        assertFalse(mf.match(base));
        assertFalse(mf.match(bigproj_deep_four));
        assertFalse(mf.match(bigproj_one));
    }

    @Test
    public void testWildCardDeepNotDirecotry() throws Exception {
        ModuleFilter mf = ModuleFilter.parseStringList("bigproj**");
        assertFalse(mf.match(base));
        assertTrue(mf.match(bigproj_deep_four));
        assertTrue(mf.match(bigproj_one));
    }

    @Test
    public void testWildCardShallowInDirecotry() throws Exception {
        ModuleFilter mf = ModuleFilter.parseStringList("bigproj.*");
        assertFalse(mf.match(base));
        assertFalse(mf.match(bigproj_deep_four));
        assertTrue(mf.match(bigproj_one));
    }

    @Test
    public void testWildCardSDeepInDirecotry() throws Exception {
        ModuleFilter mf = ModuleFilter.parseStringList("bigproj.**");
        assertFalse(mf.match(base));
        assertTrue(mf.match(bigproj_deep_four));
        assertTrue(mf.match(bigproj_one));
    }
}
