package com.acworks.acroute.impl;

import com.acworks.acroute.Datacenter;
import com.acworks.acroute.DatacenterFactory;
import com.acworks.acroute.Space;
import com.acworks.acroute.SpaceType;
import java.util.Arrays;
import java.util.List;
import junit.framework.TestCase;

/**
 *
 * @author nikita
 */
public class RouteImplTest extends TestCase {

    private Datacenter datacenter;

    private int width;

    private int height;

    private RouteImpl instance;

    public RouteImplTest(String testName) {
        super(testName);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        super.setUp();
        final Integer[] ints = { 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 3, 1 };
        width = 4;
        height = 3;
        datacenter = DatacenterFactory.buildDatacenter(width, height, Arrays.asList(ints));
        instance = new RouteImpl(datacenter);
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testGetDatacenter() {
        Datacenter result = instance.getDatacenter();
        assertEquals(datacenter, result);
    }

    public void testGetCurrentSpace() {
        Space expResult = datacenter.getStartingSpace();
        Space result = instance.getCurrentSpace();
        assertEquals(expResult, result);
    }

    public void testGetAdjacentUnvisitedOwnedRooms() {
        List<Space> result = instance.getAdjacentUnvisitedOwnedRooms();
        assertEquals(2, result.size());
        assertEquals(SpaceType.OWNED_ROOM, result.get(0).getSpaceType());
        assertEquals(SpaceType.OWNED_ROOM, result.get(1).getSpaceType());
    }
}
