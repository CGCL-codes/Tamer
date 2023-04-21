package org.jxstar.service.util;

import org.jxstar.service.BoException;
import org.jxstar.test.AbstractTest;

/**
 * 
 *
 * @author TonyTan
 * @version 1.0, 2012-1-5
 */
public class SysDataTest extends AbstractTest {

    /**
	 * @param args
	 */
    public static void main(String[] args) {
        try {
            String sql = SysDataUtil.getDataWhere("jxstar56335", "money_plan_up");
            System.out.println("...........sql=" + sql);
        } catch (BoException e) {
            e.printStackTrace();
        }
    }
}
