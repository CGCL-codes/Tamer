package com.coloradoresearch.gridster.test;

import com.coloradoresearch.gridster.DataContainer;
import com.coloradoresearch.gridster.Job;
import com.coloradoresearch.gridster.Result;

/**
 * @author jford
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class TestPreparedJob extends Job {

    public TestPreparedJob() {
    }

    public Result execute() {
        try {
            Result r = super.execute();
            Thread.sleep(5000);
            DataContainer dow = this.getDataObject();
            String str = (String) dow.getMyDataObject();
            System.out.println("woo hoo: " + str);
            return r;
        } catch (Exception e) {
        }
        return null;
    }
}
