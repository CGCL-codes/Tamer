package de.fuberlin.wiwiss.r2r.functions;

import de.fuberlin.wiwiss.r2r.*;
import java.util.List;
import java.util.ArrayList;
import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;

public class ReplaceAllFunctionTest {

    private Function replaceAll;

    @Before
    public void init() {
        replaceAll = (new ReplaceAllFunctionFactory()).getInstance();
    }

    @Test
    public void replace1() {
        List<List<String>> argumentList = Helper.getArgumentLists("-", "", "43243-63634-123");
        List<String> result = replaceAll.execute(argumentList, null);
        assertEquals(result.get(0), "4324363634123");
    }
}
