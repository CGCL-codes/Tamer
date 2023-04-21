package com.evaserver.rof.generator;

import com.evaserver.rof.script.ScriptParserConfig;
import com.evaserver.rof.util.ROFScriptParserConfig;

/**
 *
 *
 * @author Max Antoni
 * @version $Revision: 172 $
 */
public class ROFGeneratorTest extends AbstractScriptParserConfigGeneratorTest {

    protected ScriptParserConfig getScriptParserConfig() {
        return ROFScriptParserConfig.INSTANCE;
    }

    protected String[] getLibs() {
        return new String[] { "rof.Function", "rof.Class" };
    }

    public void testSimple() {
        String script = generate("Simple");
        assertEquals("Class(\"Simple\",{\"toString\":function(){return \"[object Simple]\";}});", script);
    }

    public void testItem() {
        String script = generate("Item");
        System.out.println(script);
        assertTrue(script.endsWith("Class(\"Item\",rof.Item,{\"constructor\":function(h){Item._c.call(this,h);},\"toString\":function(){return \"[object Item]\";}});"));
    }
}
