package com.google.code.sagetvaddons.sre.server;

/**
 * Concrete implementation of AiringMonitor that monitors NCAA Men's Basketball games via ESPN.com
 * @version $Id: AiringMonitorNCAABM.java 708 2009-12-20 01:46:13Z derek@battams.ca $
 */
class AiringMonitorNCAABM extends AiringMonitorNCAA {

    static final String FEED_NAME = "ncb";

    static final String TITLE = "College Basketball";

    AiringMonitorNCAABM(String subtitle, long airingDate) {
        super(TITLE, subtitle, airingDate, FEED_NAME);
    }
}
