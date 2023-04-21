package com.evaserver.rof.script;

/**
 *
 *
 * @author max
 * @version $Revision: 36 $
 */
public class ArgumentsTest extends _TesterBase {

    public void testInitialization() {
        eval("var x='global';var fn=function(x){return x;};var y=fn();");
        jsAssert("global", "x");
        jsAssert(null, "y");
    }
}
