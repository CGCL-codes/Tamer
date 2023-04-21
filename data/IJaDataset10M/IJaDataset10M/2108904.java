package org.wcb.e6b;

import org.testng.annotations.*;

/**
 * <small>
 * <p/>
 * Copyright (c)  2006  wbogaardt.
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 * <p/>
 * $File:  $ <br>
 * $Change:  $ submitted by $Author: wbogaardt $ at $DateTime: Sep 18, 2006 10:29:36 AM $ <br>
 * </small>
 *
 * @author wbogaardt
 *         Test true airspeed
 */
public class TrueAirspeedTest {

    private TrueAirspeed e6b;

    @Configuration(beforeTestMethod = true)
    protected void setUp() throws Exception {
        e6b = new TrueAirspeed();
    }

    @Test
    public void testSpeed() {
        e6b.setCalibratedAirspeed(250);
        e6b.setPressureAltitude(10000);
        e6b.setOutsideAirTemp(2);
        e6b.calculate();
        assert 288.0 == e6b.getTAS();
        assert -6.719844485358863 == e6b.getTrueAirTemperature();
        assert 0.45227511143507054 == e6b.getMach();
    }
}
