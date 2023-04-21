package net.sf.dbchanges.integration;

import static net.sf.dbchanges.fs.LocationTD.INTEGRATION_LOC;
import net.sf.dbchanges.dbupdate.CheckAction;
import net.sf.dbchanges.fs.FilesTH;
import net.sf.dbchanges.fs.FsMapBuilder;
import org.junit.Test;

/**
 * @author olex
 */
@SuppressWarnings("unchecked")
public class CheckIntTest extends IntegrationTestCase {

    /**
	 * @see net.sf.dbchanges.integration.IntegrationTestCase#setUp()
	 */
    @Override
    public void setUp() throws Exception {
        super.setUp();
        FilesTH.initLocation(INTEGRATION_LOC);
    }

    @Test
    public void test_check_ok() throws Exception {
        FilesTH.makeAll(INTEGRATION_LOC, new FsMapBuilder().onlyTestdb1().build());
        runProcess(UnpackedDbCmdBuilder.run().action(CheckAction.INFO).build());
    }
}
