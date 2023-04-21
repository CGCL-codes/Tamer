package csiebug.test.web.firefox;

import csiebug.test.web.AdminScenario;

/**
 * @author George_Tsai
 * @version 2010/3/15
 */
public class AdminScenarioTest extends AdminScenario {

    public void setUp() throws Exception {
        setUp("*firefox");
    }
}
