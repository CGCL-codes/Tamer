package sg.com.fastwire.mediation.plugins.huaweiMTOSI.controller;

import org.junit.Test;
import sg.com.fastwire.mediation.plugins.huaweiMTOSI.entity.TerminationPoint;

public class EntityTest {

    @Test
    public void testGetTP() {
        TerminationPoint tPoint = new TerminationPoint();
        tPoint.setName("MD=Huawei/U2000 ME=3145739 PTP=/shelf=1/slot=1/domain=ptn/port=1");
    }
}
