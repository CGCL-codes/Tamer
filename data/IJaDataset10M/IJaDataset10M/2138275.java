package csiebug.test.web.chrome;

import csiebug.test.web.AdminScenario;

/**
 * @author George_Tsai
 * @version 2010/3/15
 */
public class AdminScenarioTest extends AdminScenario {

    public void setUp() throws Exception {
        setUp("*chrome");
    }
}
