package org.dcm4chee.xero.metadata;

import org.testng.annotations.Test;

/** Tests that the property provider works
 * 
 * @author bwallace
 *
 */
public class PropertyProviderTest {

    static MetaDataBean mdb = StaticMetaData.getMetaData("metadata-test.metadata");

    /** Tests that a simple property include works correctly. */
    @Test
    public void simplePropertyTest() {
        assert mdb != null;
        assert mdb.getValue("nonprefix").equals("NonPrefix");
    }

    /** Tests that an include with a prefix value works as expected. */
    @Test
    public void prefixPropertyTest() {
        assert mdb != null;
        assert mdb.getChild("prefixTest") != null;
        assert mdb.getChild("prefixTest").getValue("prefixValue").equals("prefixValue");
    }
}
